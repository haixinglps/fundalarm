package cn.exrick.manager.service.qq;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * QQ Bot 加密压缩文件发送 Demo
 * 
 * 功能：
 * 1. 将文件/文件夹压缩为带密码的 ZIP 文件（AES加密）
 * 2. 上传到文件服务器获取下载链接
 * 3. 通过 QQ Bot 发送下载链接和密码给用户
 * 
 * 注意：QQ Bot API 限制，不能直接发送本地文件，需先上传获取 URL
 */
public class QQBotFileEncryptDemo {
    private static final Logger log = LoggerFactory.getLogger(QQBotFileEncryptDemo.class);
    
    // AES 加密配置
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String IV = "0000000000000000";
    
    // 文件上传服务配置（示例使用 file.io 临时存储，生产环境请使用自己的 OSS/S3）
    private static final String UPLOAD_URL = "https://file.io";
    
    private final QQBotClient qqBotClient;
    
    public QQBotFileEncryptDemo(QQBotClient qqBotClient) {
        this.qqBotClient = qqBotClient;
    }
    
    /**
     * 发送加密压缩文件给 QQ 用户
     * 
     * @param userId     QQ 用户 ID
     * @param msgId      原消息 ID（用于引用回复）
     * @param sourceFile 要发送的文件/文件夹
     * @param password   ZIP 密码（如果为 null 则自动生成）
     * @param expireDays 文件过期天数（1-14）
     */
    public void sendEncryptedFile(String userId, String msgId, 
                                   File sourceFile, String password, int expireDays) {
        try {
            // 1. 自动生成密码（如果未提供）
            String zipPassword = (password != null && !password.isEmpty()) 
                ? password 
                : generateRandomPassword(8);
            
            log.info("[QQBotFileDemo] 开始处理文件: {}, 密码: {}", sourceFile.getName(), zipPassword);
            
            // 2. 创建带密码的加密 ZIP 文件
            File zipFile = createEncryptedZip(sourceFile, zipPassword);
            log.info("[QQBotFileDemo] ZIP 文件创建成功: {}", zipFile.getAbsolutePath());
            
            // 3. 上传文件获取下载链接
            String downloadUrl = uploadFile(zipFile, expireDays);
            log.info("[QQBotFileDemo] 文件上传成功: {}", downloadUrl);
            
            // 4. 删除临时 ZIP 文件
            zipFile.delete();
            
            // 5. 发送消息给用户（包含下载链接和密码）
            String message = formatFileMessage(sourceFile.getName(), downloadUrl, zipPassword, expireDays);
            boolean success = qqBotClient.sendC2CReply(userId, msgId, message);
            
            if (success) {
                log.info("[QQBotFileDemo] 文件信息发送成功 to user: {}", userId);
            } else {
                log.error("[QQBotFileDemo] 文件信息发送失败 to user: {}", userId);
            }
            
        } catch (Exception e) {
            log.error("[QQBotFileDemo] 发送加密文件失败", e);
            // 发送错误提示
            qqBotClient.sendC2CReply(userId, msgId, "❌ 文件发送失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建 AES 加密保护的 ZIP 文件
     * 
     * 注意：标准 Java Zip 不支持密码保护，这里使用 AES 加密 ZIP 内容
     * 需要配合专用解压工具或脚本使用
     */
    private File createEncryptedZip(File sourceFile, String password) throws Exception {
        // 创建临时 ZIP 文件
        File zipFile = File.createTempFile("encrypted_", ".zip");
        
        // 1. 先创建普通 ZIP
        File tempZip = File.createTempFile("temp_", ".zip");
        try (FileOutputStream fos = new FileOutputStream(tempZip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            if (sourceFile.isDirectory()) {
                // 压缩文件夹
                zipDirectory(sourceFile, sourceFile.getName(), zos);
            } else {
                // 压缩单个文件
                zipFile(sourceFile, sourceFile.getName(), zos);
            }
        }
        
        // 2. 读取 ZIP 内容并 AES 加密
        byte[] zipContent = readFileToByteArray(tempZip);
        byte[] encryptedContent = aesEncrypt(zipContent, password);
        
        // 3. 将加密后的内容写入最终文件（添加 .enc 后缀标识）
        try (FileOutputStream fos = new FileOutputStream(zipFile)) {
            // 写入魔数（标识这是一个加密 ZIP）
            fos.write("ENCZIP".getBytes(StandardCharsets.UTF_8));
            fos.write(encryptedContent);
        }
        
        // 清理临时文件
        tempZip.delete();
        
        return zipFile;
    }
    
    /**
     * 压缩文件夹
     */
    private void zipDirectory(File folder, String parentName, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            String entryName = parentName + "/" + file.getName();
            if (file.isDirectory()) {
                zipDirectory(file, entryName, zos);
            } else {
                zipFile(file, entryName, zos);
            }
        }
    }
    
    /**
     * 压缩单个文件
     */
    private void zipFile(File file, String entryName, ZipOutputStream zos) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zos.putNextEntry(zipEntry);
        
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
        }
        zos.closeEntry();
    }
    
    /**
     * AES 加密
     */
    private byte[] aesEncrypt(byte[] data, String password) throws Exception {
        // 从密码生成 16 字节密钥（MD5 哈希）
        String key = generateKeyFromPassword(password);
        
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8)));
        
        return cipher.doFinal(data);
    }
    
    /**
     * AES 解密（用于验证）
     */
    public byte[] aesDecrypt(byte[] encryptedData, String password) throws Exception {
        String key = generateKeyFromPassword(password);
        
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8)));
        
        return cipher.doFinal(encryptedData);
    }
    
    /**
     * 从密码生成密钥（使用 SHA-256 后取前 16 字节）
     */
    private String generateKeyFromPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // 取前 16 字节作为 AES 密钥
        return new String(hash, 0, 16, StandardCharsets.UTF_8);
    }
    
    /**
     * 上传文件到临时存储服务
     * 
     * 生产环境建议替换为自己的：
     * - 阿里云 OSS
     * - 腾讯云 COS  
     * - AWS S3
     * - 自建文件服务器
     */
    private String uploadFile(File file, int expireDays) throws Exception {
        // 这里使用 file.io 作为示例（临时存储，自动过期）
        // 生产环境请替换为正式的文件存储服务
        
        OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();
        
        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("application/octet-stream"), file))
            .addFormDataPart("expires", String.valueOf(expireDays * 24 * 60 * 60)) // 转换为秒
            .build();
        
        Request request = new Request.Builder()
            .url(UPLOAD_URL)
            .post(requestBody)
            .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("上传失败: " + response.code());
            }
            
            String responseBody = response.body().string();
            // 解析 JSON 获取下载链接
            // file.io 返回格式: {"success":true,"status":200,"id":"xxx","key":"xxx","link":"xxx","expiry":"xxx"}
            
            // 简单提取 link 字段（生产环境建议使用 Gson 解析）
            if (responseBody.contains("\"link\":")) {
                int start = responseBody.indexOf("\"link\":") + 8;
                int end = responseBody.indexOf("\"", start);
                return responseBody.substring(start, end).replace("\\/", "/");
            }
            
            return responseBody;
        }
    }
    
    /**
     * 格式化发送消息
     */
    private String formatFileMessage(String fileName, String downloadUrl, 
                                      String password, int expireDays) {
        StringBuilder sb = new StringBuilder();
        sb.append("📦 加密文件发送\n");
        sb.append("=============================\n");
        sb.append("📄 文件名: ").append(fileName).append("\n");
        sb.append("⏰ 有效期: ").append(expireDays).append(" 天\n");
        sb.append("🔐 解压密码: ").append(password).append("\n");
        sb.append("-----------------------------\n");
        sb.append("⬇️ 下载链接:\n").append(downloadUrl).append("\n");
        sb.append("-----------------------------\n");
        sb.append("⚠️ 注意事项:\n");
        sb.append("1. 文件已加密，解压需要密码\n");
        sb.append("2. 链接 ").append(expireDays).append(" 天后失效\n");
        sb.append("3. 请及时下载保存\n");
        sb.append("-----------------------------\n");
        sb.append("🔓 解密方式:\n");
        sb.append("使用配套解密工具或联系管理员");
        
        return sb.toString();
    }
    
    /**
     * 生成随机密码
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    /**
     * 读取文件到字节数组
     */
    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }
    
    // ==================== 使用示例 ====================
    
    /**
     * 示例：在 QQBotMessageProcessor 中调用
     */
    public static void handleSendFileCommand(QQBotMessage message, QQBotClient client) {
        String content = message.getContent().trim();
        
        // 命令格式: "发送文件 文件路径 [密码] [过期天数]"
        if (content.startsWith("发送文件 ")) {
            String[] parts = content.substring(5).trim().split(" ");
            if (parts.length < 1) {
                client.sendC2CReply(message.getUserId(), message.getMsgId(), 
                    "❌ 格式错误，请使用: 发送文件 <文件路径> [密码] [过期天数]");
                return;
            }
            
            String filePath = parts[0];
            String password = parts.length > 1 ? parts[1] : null;
            int expireDays = parts.length > 2 ? Integer.parseInt(parts[2]) : 7;
            
            File file = new File(filePath);
            if (!file.exists()) {
                client.sendC2CReply(message.getUserId(), message.getMsgId(), 
                    "❌ 文件不存在: " + filePath);
                return;
            }
            
            // 创建发送器并发送文件
            QQBotFileEncryptDemo sender = new QQBotFileEncryptDemo(client);
            sender.sendEncryptedFile(message.getUserId(), message.getMsgId(), 
                                     file, password, expireDays);
        }
    }
    
    /**
     * 测试主方法
     */
    public static void main(String[] args) throws Exception {
        // 测试加密/解密
        System.out.println("=== QQ Bot 加密压缩文件发送 Demo ===\n");
        
        // 创建测试文件
        File testFile = File.createTempFile("test_", ".txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("这是一个测试文件内容，用于演示加密压缩发送功能。\n");
            writer.write("敏感信息请使用加密方式传输。\n");
        }
        System.out.println("测试文件创建: " + testFile.getAbsolutePath());
        
        // 生成密码
        String password = "Test1234";
        System.out.println("解压密码: " + password);
        
        // 创建加密 ZIP
        QQBotFileEncryptDemo demo = new QQBotFileEncryptDemo(null);
        File encryptedZip = demo.createEncryptedZip(testFile, password);
        System.out.println("加密 ZIP 创建: " + encryptedZip.getAbsolutePath());
        System.out.println("文件大小: " + encryptedZip.length() + " 字节");
        
        // 验证解密
        byte[] encryptedContent = demo.readFileToByteArray(encryptedZip);
        // 跳过魔数
        byte[] actualEncrypted = new byte[encryptedContent.length - 6];
        System.arraycopy(encryptedContent, 6, actualEncrypted, 0, actualEncrypted.length);
        
        byte[] decrypted = demo.aesDecrypt(actualEncrypted, password);
        System.out.println("\n解密验证成功，ZIP 大小: " + decrypted.length + " 字节");
        
        // 清理
        testFile.delete();
        encryptedZip.delete();
        
        System.out.println("\n=== 测试完成 ===");
        System.out.println("\n使用说明:");
        System.out.println("1. 将此 Demo 集成到 QQBotMessageProcessor 中");
        System.out.println("2. 替换 uploadFile 方法中的上传服务为生产环境 OSS/S3");
        System.out.println("3. 用户发送 '发送文件 <路径> [密码]' 即可使用");
    }
}
