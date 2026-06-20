package cn.exrick.manager.service.telegram;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.impl.RobotServiceImpl;
import cn.exrick.manager.service.tg.TelegramChannelMonitor;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class TelegramBotConfig {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	@Scope("singleton")
	public TelegramChannelMonitor telegramChannelMonitor(
			@Value("${telegram.bot.token}") String botToken,
			@Value("${telegram.channel.username}") String channelUsername,
			RobotService robotService,
			@Qualifier("taskExecutor") Executor taskExecutor) {

		DefaultBotOptions botOptions = new DefaultBotOptions();
		botOptions.setMaxThreads(10);

		Set<Long> groups = new HashSet<>(Arrays.asList(-1003867299066L));
		Map<Long, List<Integer>> topics = new HashMap<>();
		topics.put(-1003867299066L, Arrays.asList(206, 2564));

		TelegramChannelMonitor monitor = new TelegramChannelMonitor(
				botToken, "summer0011999bot", channelUsername, botOptions,
				robotService, taskExecutor, groups, topics);

		System.out.println("【Bot1】第一个机器人启动成功: summer0011999bot, 服务群: -1003867299066");
		return monitor;
	}

	@Bean
	@Scope("singleton")
	public TelegramChannelMonitor telegramChannelMonitor2(
			@Value("${telegram.bot2.token:}") String botToken2,
			@Value("${telegram.bot2.username:}") String botUsername2,
			@Value("${telegram.channel.username}") String channelUsername,
			RobotService robotService,
			@Qualifier("taskExecutor") Executor taskExecutor) {

		if (botToken2 == null || botToken2.isEmpty()) {
			System.out.println("【Bot2】token 未配置，跳过第二个机器人启动");
			return null;
		}

		DefaultBotOptions botOptions = new DefaultBotOptions();
		botOptions.setMaxThreads(10);

		Set<Long> groups = new HashSet<>(Arrays.asList(-1003992613609L, -1004298122427L));
		Map<Long, List<Integer>> topics = new HashMap<>();

		TelegramChannelMonitor monitor = new TelegramChannelMonitor(
				botToken2, botUsername2, channelUsername, botOptions,
				robotService, taskExecutor, groups, topics);

		System.out.println("【Bot2】第二个机器人启动成功: " + botUsername2 + ", 服务群: -1003992613609, wwsearchcenter2");
		return monitor;
	}
}
