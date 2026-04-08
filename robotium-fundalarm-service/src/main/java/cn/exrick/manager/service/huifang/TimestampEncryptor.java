package cn.exrick.manager.service.huifang;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class TimestampEncryptor {
    private static final String KEY = "dingHao-disk-app";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    // 加密方法
    public static String encryptTimestamp() {
        try {
            // 1. 获取当前时间戳（毫秒）
            long currentTime = new Date().getTime();
            
            // 2. 创建AES加密器
            SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            // 3. 加密时间戳
            byte[] encryptedBytes = cipher.doFinal(String.valueOf(currentTime).getBytes(StandardCharsets.UTF_8));
            
            // 4. 将加密结果转为16进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : encryptedBytes) {
                hexString.append(String.format("%02x", b));
            }
            
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    public static void main(String[] args) {
        String encryptedTimestamp = encryptTimestamp();
        System.out.println("加密后的timestamp: " + encryptedTimestamp);
    }
}
