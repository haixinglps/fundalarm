package cn.exrick.manager.service.qq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * QQ Bot 配置类
 * MultiQQBotManager 是 @Component，自动扫描创建
 */
@Configuration
public class QQBotConfiguration {
    private static final Logger log = LoggerFactory.getLogger(QQBotConfiguration.class);

    @Value("${qqbot.enabled:false}")
    private boolean enabled;

    @PostConstruct
    public void init() {
        if (enabled) {
            log.info("[QQBot] 多机器人配置已加载");
        } else {
            log.info("[QQBot] 已禁用");
        }
    }
}
