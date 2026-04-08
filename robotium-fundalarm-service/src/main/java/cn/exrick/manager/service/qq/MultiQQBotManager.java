package cn.exrick.manager.service.qq;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 多 QQ Bot 管理器 只配置 appId + clientSecret，每个配置建立一个独立的 WebSocket 连接
 * 消息从哪个连接进来，就用哪个连接回复（自动识别来源）
 */
@Component
public class MultiQQBotManager {
	private static final Logger log = LoggerFactory.getLogger(MultiQQBotManager.class);

	private static final String DEFAULT_CONFIG_PATH = "classpath:qqbot_bots.json";

	@Value("${qqbot.config.path:" + DEFAULT_CONFIG_PATH + "}")
	private String configPath;

	@Value("${qqbot.resultLimit:3}")
	private int resultLimit;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private Executor taskExecutor;

	@Autowired
	private cn.exrick.manager.service.RobotService robotService;

	@Autowired
	private cn.exrick.manager.mapper.TbWalletMapper tbWalletMapper;

	@Autowired
	private cn.exrick.common.jedis.JedisClient jedisClient;

	@Autowired
	private cn.exrick.manager.service.impl.AsyncEventPublisher publisher;

	// appId -> Bot客户端映射
	private final Map<String, QQBotClient> botClients = new ConcurrentHashMap<>();

	// appId -> 配置映射
	private final Map<String, BotConfig> botConfigs = new ConcurrentHashMap<>();

	private final Gson gson = new Gson();

	/**
	 * 机器人配置（只包含 appId 和 clientSecret）
	 */
	public static class BotConfig {
		private String appId;
		private String clientSecret;
		private String name;

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getClientSecret() {
			return clientSecret;
		}

		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 配置文件结构
	 */
	public static class BotConfigFile {
		private List<BotConfig> bots;

		public List<BotConfig> getBots() {
			return bots;
		}

		public void setBots(List<BotConfig> bots) {
			this.bots = bots;
		}
	}

	@PostConstruct
	public void init() {
		log.info("[MultiQQBot] 初始化多机器人管理器...");
//        loadConfigAndStartBots();
	}

	@EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
	public void onApplicationReady() {
		log.info("[MultiQQBot] 应用已就绪，开始启动机器人...");
		loadConfigAndStartBots();
	}

	/**
	 * 加载配置并启动所有机器人
	 */
	private void loadConfigAndStartBots() {
		try {
			BotConfigFile configFile = loadConfigFile();

			if (configFile == null || configFile.getBots() == null || configFile.getBots().isEmpty()) {
				log.warn("[MultiQQBot] 没有配置机器人");
				return;
			}

			log.info("[MultiQQBot] 发现 {} 个机器人配置", configFile.getBots().size());

			// 为每个配置创建并启动机器人
			for (BotConfig config : configFile.getBots()) {
				if (config.getAppId() == null || config.getClientSecret() == null) {
					log.warn("[MultiQQBot] 配置不完整，跳过: {}", config.getName());
					continue;
				}

				startBot(config);
			}

			log.info("[MultiQQBot] 初始化完成，已启动 {} 个机器人", botClients.size());

		} catch (Exception e) {
			log.error("[MultiQQBot] 加载配置失败", e);
		}
	}

	/**
	 * 加载配置文件
	 */
	private BotConfigFile loadConfigFile() throws IOException {
		Resource resource = resourceLoader.getResource(configPath);

		if (!resource.exists()) {
			log.error("[MultiQQBot] 配置文件不存在: {}", configPath);
			return null;
		}

		try (Reader reader = new FileReader(resource.getFile())) {
			Type type = new TypeToken<BotConfigFile>() {
			}.getType();
			return gson.fromJson(reader, type);
		}
	}

	/**
	 * 启动单个机器人
	 */
	private void startBot(BotConfig config) {
		String appId = config.getAppId();

		try {
			log.info("[MultiQQBot] 启动机器人: {} (appId: {})", config.getName(), appId);

			// 创建消息处理器（传入 manager 引用，用于后续回复）
			QQBotRealDataProcessor handler = new QQBotRealDataProcessor(robotService, tbWalletMapper, jedisClient,
					resultLimit);
			handler.setPublisher(publisher);

			// 创建并启动客户端
			QQBotClient client = new QQBotClient(appId, config.getClientSecret(), taskExecutor, handler);

			// 设置 client，让处理器知道用哪个 appId 发送
			handler.setClient(client);

			client.start();

			// 保存映射
			botClients.put(appId, client);
			botConfigs.put(appId, config);

			log.info("[MultiQQBot] 机器人 {} 启动成功", appId);

		} catch (Exception e) {
			log.error("[MultiQQBot] 机器人 {} 启动失败", appId, e);
		}
	}

	/**
	 * 根据 appId 获取客户端
	 */
	public QQBotClient getClient(String appId) {
		return botClients.get(appId);
	}

	/**
	 * 获取所有客户端
	 */
	public Map<String, QQBotClient> getAllClients() {
		return new ConcurrentHashMap<>(botClients);
	}

	@PreDestroy
	public void destroy() {
		log.info("[MultiQQBot] 关闭所有机器人连接...");

		for (Map.Entry<String, QQBotClient> entry : botClients.entrySet()) {
			try {
				entry.getValue().stop();
				log.info("[MultiQQBot] 机器人 {} 已关闭", entry.getKey());
			} catch (Exception e) {
				log.error("[MultiQQBot] 关闭机器人 {} 失败", entry.getKey(), e);
			}
		}

		botClients.clear();
		botConfigs.clear();

		log.info("[MultiQQBot] 所有机器人已关闭");
	}
}
