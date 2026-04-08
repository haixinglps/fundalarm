package cn.exrick.manager.service.qq;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * QQ Bot 回复上下文
 * 用于多次回复同一用户消息（异步消息流）
 * 
 * QQ 限制：同一 msg_id 1小时内最多回复 4 次
 */
public class QQBotReplyContext {
    
    private final QQBotMessage originalMessage;
    private final QQBotClient client;
    private final AtomicInteger replyCount;
    private final long createdAt;
    
    // QQ 限制：同一消息最多回复次数
    private static final int MAX_REPLIES = 4;
    // 限制时间窗口：1小时
    private static final long TIME_WINDOW_MS = 60 * 60 * 1000;
    
    public QQBotReplyContext(QQBotMessage originalMessage, QQBotClient client) {
        this.originalMessage = originalMessage;
        this.client = client;
        this.replyCount = new AtomicInteger(0);
        this.createdAt = System.currentTimeMillis();
    }
    
    /**
     * 发送阶段性消息（如"正在搜索..."）
     * 这会消耗 1 次回复次数
     */
    public boolean sendProgress(String content) {
        return sendReply(content);
    }
    
    /**
     * 发送最终结果
     * 分多条发送（如果内容超长）
     */
    public void sendResults(String content) {
        // 检查是否还有剩余次数
        int remaining = MAX_REPLIES - replyCount.get();
        
        if (remaining <= 0) {
            // 超过限制，尝试发送主动消息
            sendProactiveMessage("[继续] " + content);
            return;
        }
        
        // 分割长消息
        String[] chunks = splitMessage(content, 4000);
        
        for (int i = 0; i < chunks.length && replyCount.get() < MAX_REPLIES; i++) {
            String chunk = chunks[i];
            if (i == chunks.length - 1) {
                // 最后一条标记为完成
                chunk = chunk + "\n\n✅ 搜索完成";
            } else {
                chunk = chunk + "\n\n...(还有 " + (chunks.length - i - 1) + " 条)";
            }
            
            sendReply(chunk);
            
            // 短暂延迟，避免触发频率限制
            if (i < chunks.length - 1) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    /**
     * 发送图片+文字结果
     * QQ 不支持直接发送网络图片，只能发 URL 或先上传
     */
    public boolean sendImageResult(String caption, String imageUrl) {
        if (!canReply()) {
            // 超过限制，只发文字
            return sendReply("[图片] " + imageUrl + "\n" + caption);
        }
        
        // QQ 发送图片需要特殊处理，这里先发文字+链接
        String content = caption + "\n\n[封面] " + imageUrl;
        return sendReply(content);
    }
    
    /**
     * 发送文件给用户
     * @param file 要发送的文件
     * @param caption 文件说明文字
     * @return 是否发送成功
     */
    public boolean sendFile(java.io.File file, String caption) {
        String userId = originalMessage.getUserId();
        return client.sendFileToUser(userId, file, caption);
    }
    
    /**
     * 是否可以继续回复
     */
    public boolean canReply() {
        if (System.currentTimeMillis() - createdAt > TIME_WINDOW_MS) {
            return false; // 超过时间窗口
        }
        return replyCount.get() < MAX_REPLIES;
    }
    
    /**
     * 获取剩余回复次数
     */
    public int getRemainingReplies() {
        return Math.max(0, MAX_REPLIES - replyCount.get());
    }
    
    /**
     * 发送主动消息（不引用原消息）
     * 不占用回复次数，但有其他频率限制
     */
    private boolean sendProactiveMessage(String content) {
        String chatId = originalMessage.getChatId();
        String userId = originalMessage.getUserId();
        
        // 主动消息不引用 msg_id
        switch (originalMessage.getType()) {
            case GROUP:
                return client.sendGroupMessage(chatId, content);
            case C2C:
                return client.sendC2CMessage(userId, content);
            case GUILD:
                return client.sendChannelMessage(chatId, content);
            default:
                return false;
        }
    }
    
    /**
     * 内部方法：发送回复（引用原消息）
     */
    private boolean sendReply(String content) {
        if (!canReply()) {
            return sendProactiveMessage(content);
        }
        
        String chatId = originalMessage.getChatId();
        String userId = originalMessage.getUserId();
        String msgId = originalMessage.getMsgId();
        
        boolean success = false;
        
        switch (originalMessage.getType()) {
            case GROUP:
                success = client.sendGroupReply(chatId, msgId, content);
                break;
            case C2C:
                success = client.sendC2CReply(userId, msgId, content);
                break;
            case GUILD:
                success = client.sendChannelReply(chatId, msgId, content);
                break;
        }
        
        if (success) {
            replyCount.incrementAndGet();
        }
        
        return success;
    }
    
    /**
     * 分割长消息
     */
    private String[] splitMessage(String content, int maxLength) {
        if (content.length() <= maxLength) {
            return new String[]{content};
        }
        
        java.util.List<String> chunks = new java.util.ArrayList<>();
        int start = 0;
        
        while (start < content.length()) {
            int end = Math.min(start + maxLength, content.length());
            
            // 尝试在换行处分割
            if (end < content.length()) {
                int lastNewline = content.lastIndexOf('\n', end);
                if (lastNewline > start) {
                    end = lastNewline;
                }
            }
            
            chunks.add(content.substring(start, end));
            start = end;
        }
        
        return chunks.toArray(new String[0]);
    }
}
