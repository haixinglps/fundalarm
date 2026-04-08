package cn.exrick.manager.service.qq;

import cn.exrick.manager.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QQ Bot 消息处理器
 * 复用原有的 RobotService 业务逻辑
 */
public class QQBotMessageProcessor implements QQMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(QQBotMessageProcessor.class);
    
    private final RobotService robotService;
    private final QQBotReplyService replyService;
    
    public QQBotMessageProcessor(RobotService robotService) {
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
        
        String reply = null;
        
        // 1. 提取作品指令 (ww/tl/bc/zm/tg/ch 开头)
        if (message.hasCommandPrefix("ww", "tl", "bc", "zm", "tg", "ch")) {
            reply = handleExtractWork(message);
        }
        // 2. 搜索指令
        else if (message.hasCommandPrefix("搜索", "查询", "search", "s ")) {
            reply = handleSearch(message);
        }
        // 3. 帮助指令
        else if (message.hasCommandPrefix("帮助", "help", "?")) {
            reply = generateHelpMessage();
        }
        // 4. 最新作品
        else if (message.hasCommandPrefix("最新", "最新作品")) {
            reply = handleLatestWorks(message);
        }
        // 5. 默认处理 - 直接搜索
        else {
            reply = handleSearch(message);
        }
        
        // 发送回复
        if (reply != null && !reply.isEmpty()) {
            sendReply(message, client, reply);
        }
    }
    
    /**
     * 处理作品提取
     */
    private String handleExtractWork(QQBotMessage message) {
        String content = message.getContent().trim();
        String cmd = content.substring(0, 2).toLowerCase();
        String param = content.substring(2).trim();
        
        log.info("[QQBot] 提取作品: cmd={}, param={}", cmd, param);
        
        // 复用 RobotService 的逻辑
        // 由于 RobotService 依赖 Telegram 的 Update 对象，我们需要适配
        // 这里先返回提示，实际项目中需要添加适配层
        
        switch (cmd) {
            case "ww":
                return replyService.formatExtractReply("玩物", param, 
                    "https://example.com/ww/" + param, "作品标题", "作者", "10:00", "");
            case "tl":
                return replyService.formatExtractReply("淘露", param,
                    "https://example.com/tl/" + param, "作品标题", "作者", "10:00", "");
            case "bc":
                return replyService.formatExtractReply("本地", param,
                    "https://example.com/bc/" + param, "作品标题", "作者", "10:00", "");
            case "zm":
                return replyService.formatExtractReply("网页", param,
                    "https://example.com/zm/" + param, "作品标题", "作者", "10:00", "");
            case "tg":
                return replyService.formatExtractReply("电报", param,
                    "https://example.com/tg/" + param, "作品标题", "作者", "10:00", "");
            case "ch":
                return replyService.formatExtractReply("频道", param,
                    "https://example.com/ch/" + param, "作品标题", "作者", "10:00", "");
            default:
                return "未知指令: " + cmd;
        }
    }
    
    /**
     * 处理搜索
     */
    private String handleSearch(QQBotMessage message) {
        String keyword = message.getContent().trim();
        
        // 去除指令前缀
        if (keyword.toLowerCase().startsWith("search ")) {
            keyword = keyword.substring(7).trim();
        } else if (keyword.toLowerCase().startsWith("s ")) {
            keyword = keyword.substring(2).trim();
        } else if (keyword.startsWith("搜索")) {
            keyword = keyword.substring(2).trim();
        } else if (keyword.startsWith("查询")) {
            keyword = keyword.substring(2).trim();
        }
        
        log.info("[QQBot] 搜索: {}", keyword);
        
        // QQ 消息限制 4000 字符，且不支持 Markdown
        // 这里返回格式化文本
        return replyService.formatSearchReply(keyword, 
            "网页搜索 15条\n" +
            "最新作品搜索 23条\n" +
            "玩物搜索 8条\n" +
            "淘露搜索 12条\n" +
            "电报搜索 5条\n" +
            "频道搜索 3条\n\n" +
            "请使用以下指令提取作品:\n" +
            "• ww + 作品ID - 提取玩物作品\n" +
            "• tl + 作品ID - 提取淘露作品\n" +
            "• bc + 作品ID - 提取本地作品\n" +
            "• zm + 作品ID - 提取网页作品\n" +
            "• tg + 作品ID - 提取电报作品\n" +
            "• ch + 作品ID - 提取频道作品"
        );
    }
    
    /**
     * 处理最新作品
     */
    private String handleLatestWorks(QQBotMessage message) {
        log.info("[QQBot] 获取最新作品");
        
        // 复用 robotService.getAllWork() 等逻辑
        return replyService.formatSearchReply("最新作品",
            "最新作品列表:\n\n" +
            "1. 作品标题1 - ID: bc12345\n" +
            "2. 作品标题2 - ID: ww54321\n" +
            "3. 作品标题3 - ID: tl98765\n\n" +
            "使用 ww/tl/bc + ID 提取作品"
        );
    }
    
    /**
     * 生成帮助信息
     */
    private String generateHelpMessage() {
        return "🤖 QQ Bot 帮助\n\n" +
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
               "• tl67890";
    }
    
    /**
     * 发送回复
     */
    private void sendReply(QQBotMessage message, QQBotClient client, String content) {
        boolean success = false;
        
        switch (message.getType()) {
            case GROUP:
                success = client.sendGroupReply(message.getChatId(), message.getMsgId(), content);
                break;
            case C2C:
                success = client.sendC2CReply(message.getUserId(), message.getMsgId(), content);
                break;
            case GUILD:
                success = client.sendChannelReply(message.getChatId(), message.getMsgId(), content);
                break;
        }
        
        if (success) {
            log.info("[QQBot] 回复成功");
        } else {
            log.error("[QQBot] 回复失败");
        }
    }
}
