package cn.exrick.manager.service.tg;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TelegramDeepLink {
    /**
     * 生成Telegram机器人深度链接
     * @param botUsername 机器人用户名(不带@)
     * @param prefillText 预填充文本
     * @return 格式化后的tg://链接
     */
    public static String generateLink(String botUsername, String prefillText) {
        try {
            return String.format("tg://resolve?domain=%s&text=%s", 
                botUsername, 
                URLEncoder.encode(prefillText, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("URL编码失败", e);
        }
    }

    public static void main(String[] args) {
        String link = generateLink("MyDemoBot", "你好，这是自动填充的文本");
        System.out.println("生成链接: " + link);
        // 输出示例: tg://resolve?domain=MyDemoBot&text=%E4%BD%A0%E5%A5%BD%EF%BC%8C%E8%BF%99%E6%98%AF%E8%87%AA%E5%8A%A8%E5%A1%AB%E5%85%85%E7%9A%84%E6%96%87%E6%9C%AC
    }
}
