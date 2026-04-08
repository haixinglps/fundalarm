package cn.exrick.manager.service.qq;

import java.util.List;

/**
 * QQ Bot 回复格式化服务
 * 处理 QQ 与 Telegram 的格式差异
 * QQ 限制：单条消息 4000 字符，不支持直接上传图片，只能发 URL
 */
public class QQBotReplyService {
    
    // QQ 单条消息最大长度
    private static final int MAX_LENGTH = 4000;
    
    /**
     * 格式化搜索回复（带图片 URL）
     */
    public String formatSearchReply(String keyword, String results) {
        StringBuilder sb = new StringBuilder();
        sb.append("🔍 搜索: ").append(keyword).append("\n");
        sb.append(repeatChar('=', 30)).append("\n\n");
        sb.append(results);
        
        return truncate(sb.toString());
    }
    
    /**
     * 格式化作品提取回复（带图片 URL）
     */
    public String formatExtractReply(String source, String vid, 
                                      String url, String title, 
                                      String author, String duration,
                                      String coverUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("📹 作品提取\n");
        sb.append(repeatChar('-', 20)).append("\n");
        sb.append("来源: ").append(source).append("\n");
        sb.append("ID: ").append(vid).append("\n");
        sb.append("标题: ").append(title).append("\n");
        sb.append("作者: ").append(author).append("\n");
        sb.append("时长: ").append(duration).append("\n");
        
        // 添加封面 URL（QQ 点击可查看）
        if (coverUrl != null && !coverUrl.isEmpty()) {
            sb.append("封面: ").append(coverUrl).append("\n");
        }
        
        sb.append(repeatChar('-', 20)).append("\n");
        sb.append("链接: ").append(url);
        
        return truncate(sb.toString());
    }
    
    /**
     * 格式化作品列表（每项带封面 URL）
     */
    public String formatWorkListWithCovers(String keyword, List<WorkInfo> works) {
        StringBuilder sb = new StringBuilder();
        sb.append("🔍 ").append(keyword).append(" 搜索结果\n");
        sb.append("共 ").append(works.size()).append(" 条\n");
        sb.append(repeatChar('=', 30)).append("\n\n");
        
        for (int i = 0; i < works.size() && i < 10; i++) {
            WorkInfo work = works.get(i);
            sb.append(i + 1).append(". ").append(work.getTitle()).append("\n");
            sb.append("   ID: ").append(work.getVid()).append("\n");
            sb.append("   作者: ").append(work.getAuthor()).append("\n");
            sb.append("   时长: ").append(work.getDuration()).append("\n");
            
            // QQ 方式：直接显示封面 URL
            if (work.getCover() != null && !work.getCover().isEmpty()) {
                sb.append("   封面: ").append(work.getCover()).append("\n");
            }
            
            sb.append("   指令: ").append(work.getCommand()).append("\n\n");
        }
        
        if (works.size() > 10) {
            sb.append("... 还有 ").append(works.size() - 10).append(" 条结果\n");
        }
        
        sb.append(repeatChar('-', 20)).append("\n");
        sb.append("使用 ww/tl/bc/zm/tg/ch + ID 提取作品");
        
        return truncate(sb.toString());
    }
    
    /**
     * 将 Telegram Markdown 转换为 QQ 纯文本
     * QQ 不支持 Markdown，需要去除格式符号
     */
    public String convertFromTelegramMarkdown(String markdown) {
        if (markdown == null) return "";
        
        String text = markdown;
        
        // 去除粗体 **text**
        text = text.replaceAll("\\*\\*(.+?)\\*\\*", "$1");
        
        // 去除斜体 *text* 或 _text_
        text = text.replaceAll("\\*(.+?)\\*", "$1");
        text = text.replaceAll("_(.+?)_", "$1");
        
        // 去除行内代码 `code`
        text = text.replaceAll("`(.+?)`", "$1");
        
        // 去除代码块 ```code```
        text = text.replaceAll("```[\\s\\S]*?```", "[代码块]");
        
        // 转换链接 [text](url) -> url
        text = text.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "$2");
        
        // 去除转义字符
        text = text.replace("\\*", "*");
        text = text.replace("\\_", "_");
        text = text.replace("\\[", "[");
        text = text.replace("\\]", "]");
        text = text.replace("\\(", "(");
        text = text.replace("\\)", ")");
        
        return text;
    }
    
    /**
     * 截断超长消息
     */
    private String truncate(String content) {
        if (content.length() <= MAX_LENGTH) {
            return content;
        }
        return content.substring(0, MAX_LENGTH - 20) + "\n\n...(消息过长，已截断)";
    }
    
    /**
     * Java 8 兼容的字符串重复
     */
    private String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    /**
     * 作品信息内部类
     */
    public static class WorkInfo {
        private String vid;
        private String title;
        private String author;
        private String duration;
        private String cover;
        private String command;
        
        // Getters and Setters
        public String getVid() { return vid; }
        public void setVid(String vid) { this.vid = vid; }
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        
        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
        
        public String getCover() { return cover; }
        public void setCover(String cover) { this.cover = cover; }
        
        public String getCommand() { return command; }
        public void setCommand(String command) { this.command = command; }
    }
}
