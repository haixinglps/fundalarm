package cn.exrick.manager.service.qq;

import cn.exrick.manager.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * QQ Bot 异步消息处理器
 * 支持多次回复：发送"搜索中" → 逐步返回结果 → "搜索完成"
 * 
 * QQ 限制：同一 msg_id 1小时内最多回复 4 次
 * 超过限制后转为发送主动消息
 */
public class QQBotAsyncMessageProcessor implements QQMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(QQBotAsyncMessageProcessor.class);
    
    private final RobotService robotService;
    private final QQBotReplyService replyService;
    
    public QQBotAsyncMessageProcessor(RobotService robotService) {
        this.robotService = robotService;
        this.replyService = new QQBotReplyService();
    }
    
    @Override
    public void handleMessage(QQBotMessage message, QQBotClient client) {
        log.info("[QQBot] 处理消息: {}", message);
        
        String content = message.getContent();
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        
        // 创建回复上下文（管理多次回复）
        QQBotReplyContext context = new QQBotReplyContext(message, client);
        
        // 异步处理，避免阻塞 WebSocket
        CompletableFuture.runAsync(() -> {
            try {
                processMessageAsync(message, context);
            } catch (Exception e) {
                log.error("[QQBot] 处理消息异常", e);
                context.sendResults("❌ 处理失败，请稍后重试");
            }
        });
    }
    
    /**
     * 异步处理消息
     */
    private void processMessageAsync(QQBotMessage message, QQBotReplyContext context) {
        String content = message.getContent().trim();
        
        // 1. 提取作品指令 (ww/tl/bc/zm/tg/ch 开头)
        if (message.hasCommandPrefix("ww", "tl", "bc", "zm", "tg", "ch")) {
            handleExtractWorkAsync(message, context);
        }
        // 2. 搜索指令
        else if (message.hasCommandPrefix("搜索", "查询", "search", "s ")) {
            handleSearchAsync(message, context);
        }
        // 3. 帮助指令
        else if (message.hasCommandPrefix("帮助", "help", "?")) {
            context.sendResults(generateHelpMessage());
        }
        // 4. 最新作品
        else if (message.hasCommandPrefix("最新", "最新作品")) {
            handleLatestWorksAsync(message, context);
        }
        // 5. 默认处理 - 直接搜索
        else {
            handleSearchAsync(message, context);
        }
    }
    
    /**
     * 异步处理作品提取（带进度提示）
     */
    private void handleExtractWorkAsync(QQBotMessage message, QQBotReplyContext context) {
        String content = message.getContent().trim();
        String cmd = content.substring(0, 2).toLowerCase();
        String param = content.substring(2).trim();
        
        log.info("[QQBot] 提取作品: cmd={}, param={}", cmd, param);
        
        // 1. 发送"正在提取"提示
        context.sendProgress("⏳ 正在提取作品，请稍候...\n来源: " + getSourceName(cmd) + "\nID: " + param);
        
        // 2. 模拟查询耗时
        try {
            Thread.sleep(1000); // 模拟数据库查询
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 3. 发送结果
        String result = buildExtractResult(cmd, param);
        context.sendResults(result);
    }
    
    /**
     * 异步处理搜索（带分阶段提示）
     */
    private void handleSearchAsync(QQBotMessage message, QQBotReplyContext context) {
        String keyword = extractKeyword(message);
        
        log.info("[QQBot] 搜索: {}", keyword);
        
        // 1. 发送"搜索中"提示（消耗第1次回复）
        boolean progressSent = context.sendProgress("⏳ 正在搜索: " + keyword + "\n请稍候...");
        
        if (!progressSent) {
            log.warn("[QQBot] 发送进度消息失败，可能超过限制");
        }
        
        StringBuilder result = new StringBuilder();
        result.append("🔍 搜索: ").append(keyword).append("\n");
        result.append("==============================\n\n");
        
        // 2. 搜索各个库（模拟异步搜索）
        // 网页搜索
        result.append("【网页搜索】15条\n");
        simulateDelay(500);
        
        // 最新作品
        result.append("【最新作品】23条\n");
        simulateDelay(500);
        
        // 玩物搜索
        result.append("【玩物搜索】8条\n");
        result.append("1. 作品标题1 - ID: ww12345 - 作者: xxx - 时长: 10:00\n");
        result.append("2. 作品标题2 - ID: ww54321 - 作者: yyy - 时长: 15:00\n\n");
        simulateDelay(500);
        
        // 淘露搜索
        result.append("【淘露搜索】12条\n");
        result.append("1. 作品标题1 - ID: tl12345 - 作者: xxx\n\n");
        simulateDelay(500);
        
        // 电报搜索
        result.append("【电报搜索】5条\n\n");
        
        // 频道搜索
        result.append("【频道搜索】3条\n\n");
        
        // 3. 发送结果（消耗第2-3次回复，如果内容超长会分多条）
        result.append("==============================\n");
        result.append("请使用以下指令提取作品:\n");
        result.append("• ww + 作品ID - 提取玩物作品\n");
        result.append("• tl + 作品ID - 提取淘露作品\n");
        result.append("• bc + 作品ID - 提取本地作品\n");
        result.append("• zm + 作品ID - 提取网页作品\n");
        result.append("• tg + 作品ID - 提取电报作品\n");
        result.append("• ch + 作品ID - 提取频道作品\n\n");
        result.append("剩余回复次数: ").append(context.getRemainingReplies());
        
        context.sendResults(result.toString());
        
        log.info("[QQBot] 搜索完成，剩余回复次数: {}", context.getRemainingReplies());
    }
    
    /**
     * 异步处理最新作品
     */
    private void handleLatestWorksAsync(QQBotMessage message, QQBotReplyContext context) {
        log.info("[QQBot] 获取最新作品");
        
        // 1. 发送"加载中"
        context.sendProgress("⏳ 正在加载最新作品...");
        
        simulateDelay(800);
        
        // 2. 发送结果
        StringBuilder result = new StringBuilder();
        result.append("📅 最新作品列表\n");
        result.append("==============================\n\n");
        
        result.append("1. 作品标题1 - ID: bc12345\n");
        result.append("2. 作品标题2 - ID: ww54321\n");
        result.append("3. 作品标题3 - ID: tl98765\n");
        result.append("4. 作品标题4 - ID: zm45678\n");
        result.append("5. 作品标题5 - ID: tg11223\n\n");
        
        result.append("==============================\n");
        result.append("使用 ww/tl/bc/zm/tg/ch + ID 提取作品");
        
        context.sendResults(result.toString());
    }
    
    /**
     * 提取关键词
     */
    private String extractKeyword(QQBotMessage message) {
        String keyword = message.getContent().trim();
        
        // 去除指令前缀
        String[] prefixes = {"搜索", "查询", "search ", "s "};
        for (String prefix : prefixes) {
            if (keyword.toLowerCase().startsWith(prefix.toLowerCase())) {
                keyword = keyword.substring(prefix.length()).trim();
                break;
            }
        }
        
        return keyword.isEmpty() ? "最新作品" : keyword;
    }
    
    /**
     * 获取来源名称
     */
    private String getSourceName(String cmd) {
        switch (cmd) {
            case "ww": return "玩物";
            case "tl": return "淘露";
            case "bc": return "本地";
            case "zm": return "网页";
            case "tg": return "电报";
            case "ch": return "频道";
            default: return "未知";
        }
    }
    
    /**
     * 构提取结果
     */
    private String buildExtractResult(String cmd, String param) {
        StringBuilder sb = new StringBuilder();
        sb.append("📹 作品提取成功\n");
        sb.append("==============================\n");
        sb.append("来源: ").append(getSourceName(cmd)).append("\n");
        sb.append("ID: ").append(param).append("\n");
        sb.append("标题: 示例作品标题\n");
        sb.append("作者: 示例作者\n");
        sb.append("时长: 10:00\n");
        sb.append("==============================\n");
        sb.append("链接: https://example.com/").append(cmd).append("/").append(param).append("\n\n");
        sb.append("如果链接无法播放，请联系客服 QQ: 2167485304");
        return sb.toString();
    }
    
    /**
     * 生成帮助信息
     */
    private String generateHelpMessage() {
        return "🤖 QQ Bot 帮助\n" +
               "==============================\n\n" +
               "【搜索功能】\n" +
               "• 直接输入关键词 - 搜索所有库\n" +
               "• 搜索 关键词 - 同上\n" +
               "• 最新作品 - 查看最新作品\n\n" +
               "【提取作品】\n" +
               "• ww + 作品ID - 提取玩物作品\n" +
               "• tl + 作品ID - 提取淘露作品\n" +
               "• bc + 作品ID - 提取本地作品\n" +
               "• zm + 作品ID - 提取网页作品\n" +
               "• tg + 作品ID - 提取电报作品\n" +
               "• ch + 作品ID - 提取频道作品\n\n" +
               "【示例】\n" +
               "• 搜索 美女\n" +
               "• ww12345\n" +
               "• tl67890\n\n" +
               "【注意】\n" +
               "• 每条消息最多回复 4 次\n" +
               "• 超过限制会转为发送新消息";
    }
    
    /**
     * 模拟延迟
     */
    private void simulateDelay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
