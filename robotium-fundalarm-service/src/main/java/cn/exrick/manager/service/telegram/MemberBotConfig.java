package cn.exrick.manager.service.telegram;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.tg.TelegramChannelMonitor;

/**
 * 会员自定义 Bot 配置
 * 从 tb_wallet 读取 vip > 0 且配置了 bot_token + bot_username 的会员
 * 为每个会员启动一个独立的 TelegramChannelMonitor
 * 监控私聊和任意 supergroup 消息
 */
@Configuration
public class MemberBotConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${telegram.channel.username}")
    private String channelUsername;

    @Autowired
    private RobotService robotService;

    @Autowired
    @Qualifier("taskExecutor")
    private Executor taskExecutor;

    private static final String SQL = "SELECT uid, bot_token, bot_username FROM tb_wallet " +
            "WHERE vip > 0 AND bot_token IS NOT NULL AND bot_token != '' " +
            "AND bot_username IS NOT NULL AND bot_username != ''";

    @PostConstruct
    public void initMemberBots() {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setMaxThreads(1);

        int success = 0;
        int fail = 0;

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {
            while (rs.next()) {
                String uid = rs.getString("uid");
                String token = rs.getString("bot_token");
                String username = rs.getString("bot_username");
                if (token == null || username == null) {
                    continue;
                }
                token = token.trim();
                username = username.trim();
                try {
                    new TelegramChannelMonitor(
                            token, username, channelUsername, botOptions,
                            robotService, taskExecutor,
                            new HashSet<>(), new HashMap<>(), true);
                    success++;
                    System.out.println("【会员Bot】启动成功: @" + username + ", uid=" + uid);
                } catch (Exception e) {
                    fail++;
                    System.err.println("【会员Bot】启动失败 @" + username + " uid=" + uid + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("【会员Bot】加载数据库失败: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("【会员Bot】共启动 " + success + " 个会员机器人，失败 " + fail + " 个");
    }
}
