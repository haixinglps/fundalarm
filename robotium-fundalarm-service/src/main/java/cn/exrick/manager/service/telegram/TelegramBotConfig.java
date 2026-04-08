package cn.exrick.manager.service.telegram;

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
	public TelegramChannelMonitor telegramChannelMonitor(@Value("${telegram.bot.token}") String botToken,
			@Value("${telegram.channel.username}") String channelUsername, RobotService robotService,
			@Qualifier("taskExecutor") Executor taskExecutor) {

		DefaultBotOptions botOptions = new DefaultBotOptions();
		botOptions.setMaxThreads(10); // 并发处理10条消息
		
		TelegramChannelMonitor monitor = new TelegramChannelMonitor(botToken, channelUsername, botOptions, robotService, taskExecutor);
		
		// 解决循环依赖：手动设置到 RobotServiceImpl
		try {
			RobotServiceImpl robotServiceImpl = applicationContext.getBean(RobotServiceImpl.class);
			robotServiceImpl.setTelegramChannelMonitor(monitor);
			System.out.println("成功设置 telegramChannelMonitor 到 RobotServiceImpl");
		} catch (Exception e) {
			System.err.println("设置 telegramChannelMonitor 失败: " + e.getMessage());
			e.printStackTrace();
		}
		
		return monitor;
	}
}
