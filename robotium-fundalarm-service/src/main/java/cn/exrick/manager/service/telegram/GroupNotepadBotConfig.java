package cn.exrick.manager.service.telegram;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.tg.GroupNotepadBot;

/**
 * 群记事本机器人配置
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class GroupNotepadBotConfig {

    @Bean
    @Scope("singleton")
    public GroupNotepadBot groupNotepadBot(
            RobotService robotService,
            @Qualifier("taskExecutor") Executor taskExecutor) {
        
        // 直接读取配置文件
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (is != null) {
                props.load(is);
                System.out.println("[GroupNotepadBotConfig] 成功加载 application.properties");
            } else {
                System.out.println("[GroupNotepadBotConfig] 无法找到 application.properties");
            }
        } catch (IOException e) {
            System.out.println("[GroupNotepadBotConfig] 读取配置文件出错: " + e.getMessage());
        }
        
        String botToken = props.getProperty("group.notepad.bot.token", "");
        String targetGroupId = props.getProperty("group.notepad.target.group", "");
        String targetGroupId2 = props.getProperty("group.notepad.target.group2", "");
        
        // 如果没有配置token，返回null（不启用）
        System.out.println("[GroupNotepadBotConfig] 读取到的 token: '" + botToken + "', length=" + botToken.length());
        if (botToken.trim().isEmpty()) {
            System.out.println("[GroupNotepadBotConfig] 未配置 bot token，群记事本机器人未启用");
            return null;
        }
        
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setMaxThreads(10);
        
        GroupNotepadBot bot = new GroupNotepadBot(botToken, targetGroupId, targetGroupId2, botOptions, robotService, taskExecutor);
        bot.startMonitoring();
        
        System.out.println("[GroupNotepadBotConfig] 群记事本机器人启动成功");
        return bot;
    }
}
