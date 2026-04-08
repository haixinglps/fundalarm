package cn.exrick.manager.service.qq;

/**
 * QQ 消息处理器接口
 */
public interface QQMessageHandler {
    
    /**
     * 处理 QQ 消息
     * 
     * @param message QQ 消息
     * @param client  QQ Bot 客户端（用于回复）
     */
    void handleMessage(QQBotMessage message, QQBotClient client);
}
