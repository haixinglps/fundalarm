package cn.exrick.manager.service.qq;

/**
 * QQ Bot 消息封装
 */
public class QQBotMessage {
    
    public enum Type {
        GROUP,    // 群消息
        C2C,      // 私聊
        GUILD     // 频道
    }
    
    private final Type type;
    private final String chatId;     // 群号/频道号（私聊为 null）
    private final String userId;     // 发送者 QQ 号
    private final String nickname;   // 发送者昵称
    private final String content;    // 消息内容（已去除 @机器人）
    private final String msgId;      // 消息 ID（用于回复）
    
    public QQBotMessage(Type type, String chatId, String userId, 
                        String nickname, String content, String msgId) {
        this.type = type;
        this.chatId = chatId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.msgId = msgId;
    }
    
    // Getters
    public Type getType() { return type; }
    public String getChatId() { return chatId; }
    public String getUserId() { return userId; }
    public String getNickname() { return nickname; }
    public String getContent() { return content; }
    public String getMsgId() { return msgId; }
    
    /**
     * 是否包含指令前缀
     */
    public boolean hasCommandPrefix(String... prefixes) {
        if (content == null) return false;
        String lower = content.toLowerCase().trim();
        for (String prefix : prefixes) {
            if (lower.startsWith(prefix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 提取指令后的参数
     */
    public String extractCommandParam() {
        if (content == null) return "";
        String[] parts = content.trim().split("\\s+", 2);
        return parts.length > 1 ? parts[1] : "";
    }
    
    @Override
    public String toString() {
        return String.format("QQBotMessage[type=%s, chat=%s, user=%s, content=%s]", 
            type, chatId, userId, 
            content.length() > 20 ? content.substring(0, 20) + "..." : content);
    }
}
