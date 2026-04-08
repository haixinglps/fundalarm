package cn.exrick.manager.service.qq;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * QQ Bot 客户端 - 直接对接 QQ 官方 API
 * 独立于 Telegram，不改动原有逻辑
 */
public class QQBotClient {
    private static final Logger log = LoggerFactory.getLogger(QQBotClient.class);
    
    private static final String API_BASE = "https://api.sgroup.qq.com";
    private static final String TOKEN_URL = "https://bots.qq.com/app/getAppAccessToken";
    
    private final String appId;
    private final String clientSecret;
    private final Gson gson = new Gson();
    private final OkHttpClient httpClient;
    private final Executor taskExecutor;
    private final QQMessageHandler messageHandler;
    
    private String accessToken;
    private long tokenExpiresAt;
    private WebSocketClient wsClient;
    private volatile int seq = 0;
    private volatile boolean running = false;
    private volatile boolean ready = false;
    private volatile boolean reconnecting = false;
    private volatile int reconnectAttempts = 0;
    private static final int MAX_RECONNECT_ATTEMPTS = 10;
    private static final long INITIAL_RECONNECT_DELAY = 5000; // 5秒
    
    // QQ Bot intents - C2C 私聊 + 频道私信
    private static final int INTENT_C2C = 1 << 25;      // 33554432 - C2C 私聊
    private static final int INTENT_DIRECT = 1 << 12;   // 4096 - 频道私信
    private static final int INTENT_ALL = INTENT_C2C | INTENT_DIRECT;
    
    private final String userId;  // 绑定的用户ID（该机器人专属服务的用户）
    
    public QQBotClient(String appId, String clientSecret, 
                       Executor taskExecutor, 
                       QQMessageHandler messageHandler) {
        this(appId, clientSecret, taskExecutor, messageHandler, null);
    }
    
    public QQBotClient(String appId, String clientSecret, 
                       Executor taskExecutor, 
                       QQMessageHandler messageHandler,
                       String userId) {
        this.appId = appId;
        this.clientSecret = clientSecret;
        this.taskExecutor = taskExecutor;
        this.messageHandler = messageHandler;
        this.userId = userId;
        
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    }
    
    /**
     * 获取绑定的用户ID
     */
    public String getUserId() {
        return userId;
    }
    
    /**
     * 获取 AppId
     */
    public String getAppId() {
        return appId;
    }
    
    /**
     * 获取 ClientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }
    
    /**
     * 获取机器人名称（用于生成用户昵称）
     */
    public String getName() {
        // 从 appId 获取机器人名称，如果没有则返回默认值
        return "QQBot";
    }
    
    private volatile boolean starting = false;  // 防止重复启动
    
    /**
     * 启动 QQ Bot
     */
    public synchronized void start() {
        if (running || starting) {
            log.warn("[QQBot] 机器人 {} 已在运行或启动中，跳过", appId);
            return;
        }
        starting = true;
        reconnecting = false;
        
        new Thread(() -> {
            try {
                log.info("[QQBot] 启动中...");
                
                // 1. 获取 Token
                refreshToken();
                log.info("[QQBot] Token 获取成功");
                
                // 2. 获取网关地址
                String wsUrl = getGatewayUrl();
                log.info("[QQBot] 网关地址: {}", wsUrl);
                
                // 3. 连接 WebSocket
                connectWebSocket(wsUrl);
                
                running = true;
                reconnecting = false;
                
                // 如果是重连成功，重置计数
                if (reconnectAttempts > 0) {
                    log.info("[QQBot] 重连成功（第{}次尝试），重置重连计数", reconnectAttempts);
                    reconnectAttempts = 0;
                }
                
                log.info("[QQBot] 启动完成，等待消息...");
                
            } catch (Exception e) {
                log.error("[QQBot] 启动失败", e);
                running = false;
            } finally {
                starting = false;
            }
        }, "QQBot-Startup").start();
    }
    
    /**
     * 停止 Bot
     */
    public void stop() {
        running = false;
        ready = false;
        if (wsClient != null) {
            wsClient.close();
        }
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
        log.info("[QQBot] 已停止");
    }
    
    /**
     * 是否已就绪
     */
    public boolean isReady() {
        return ready && wsClient != null && wsClient.isOpen();
    }
    
    /**
     * 获取 Access Token
     */
    private void refreshToken() throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("appId", appId);
        body.addProperty("clientSecret", clientSecret);
        
        Request request = new Request.Builder()
            .url(TOKEN_URL)
            .post(RequestBody.create(
                MediaType.parse("application/json"), 
                body.toString()
            ))
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            String respJson = response.body().string();
            JsonObject result = new JsonParser().parse(respJson).getAsJsonObject();
            
            if (result.has("access_token")) {
                accessToken = result.get("access_token").getAsString();
                int expiresIn = result.has("expires_in") 
                    ? Integer.parseInt(result.get("expires_in").getAsString())
                    : 7200;
                tokenExpiresAt = System.currentTimeMillis() + (expiresIn - 300) * 1000;
            } else {
                throw new RuntimeException("获取 Token 失败: " + respJson);
            }
        }
    }
    
    /**
     * 获取 WebSocket 网关地址
     */
    private String getGatewayUrl() throws Exception {
        ensureTokenValid();
        
        Request request = new Request.Builder()
            .url(API_BASE + "/gateway")
            .header("Authorization", "QQBot " + accessToken)
            .build();
        
        try (Response response = httpClient.newCall(request).execute()) {
            String respJson = response.body().string();
            JsonObject result = new JsonParser().parse(respJson).getAsJsonObject();
            return result.get("url").getAsString();
        }
    }
    
    /**
     * 确保 Token 有效
     */
    private void ensureTokenValid() throws Exception {
        if (System.currentTimeMillis() >= tokenExpiresAt) {
            refreshToken();
        }
    }
    
    /**
     * 连接 WebSocket
     */
    private void connectWebSocket(String url) {
        wsClient = new WebSocketClient(URI.create(url)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                log.info("[QQBot] WebSocket 已连接");
                sendIdentify();
            }
            
            @Override
            public void onMessage(String message) {
                log.info("[QQBot] 收到原始消息: {}", message.substring(0, Math.min(200, message.length())));
                handleWebSocketMessage(message);
            }
            
            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.warn("[QQBot] WebSocket 关闭: {} - {}", code, reason);
                ready = false;
                
                // 清理资源
                if (wsClient != null) {
                    try {
                        wsClient.close();
                    } catch (Exception e) {
                        // ignore
                    }
                    wsClient = null;
                }
                
                // 只有正常运行的状态下才重连，停止状态下不重连
                if (running && !reconnecting) {
                    running = false;
                    scheduleReconnect();
                }
            }
            
            @Override
            public void onError(Exception ex) {
                log.error("[QQBot] WebSocket 错误", ex);
            }
        };
        wsClient.connect();
    }
    
    /**
     * 发送鉴权消息
     * 只订阅私聊消息，减少不必要的消息处理
     */
    private void sendIdentify() {
        try {
            ensureTokenValid();
        } catch (Exception e) {
            log.error("[QQBot] Token 刷新失败", e);
            return;
        }
        
        // 订阅 C2C 私聊 + 频道私信
        int intents = INTENT_ALL;
        
        JsonObject data = new JsonObject();
        data.addProperty("token", "QQBot " + accessToken);
        data.addProperty("intents", intents);
        // 不指定 shard，让服务器自动分配（避免多机器人消息广播）
        
        JsonObject identify = new JsonObject();
        identify.addProperty("op", 2);
        identify.add("d", data);
        
        wsClient.send(identify.toString());
        log.info("[QQBot] 发送鉴权消息（仅私聊）");
    }
    
    /**
     * 处理 WebSocket 消息
     */
    private void handleWebSocketMessage(String message) {
        try {
            JsonObject msg = new JsonParser().parse(message).getAsJsonObject();
            int op = msg.get("op").getAsInt();
            
            switch (op) {
                case 0: // Dispatch
                    handleDispatch(msg);
                    break;
                case 10: // Hello
                    int interval = msg.get("d").getAsJsonObject()
                        .get("heartbeat_interval").getAsInt();
                    startHeartbeat(interval);
                    break;
                case 11: // Heartbeat ACK
                    break;
                case 7: // Reconnect
                    log.info("[QQBot] 服务器要求重连");
                    break;
            }
            
            if (msg.has("s") && !msg.get("s").isJsonNull()) {
                seq = msg.get("s").getAsInt();
            }
        } catch (Exception e) {
            log.error("[QQBot] 处理消息失败", e);
        }
    }
    
    /**
     * 处理业务事件
     * 只处理私聊消息
     */
    private void handleDispatch(JsonObject msg) {
        String eventType = msg.get("t").getAsString();
        
        log.info("[QQBot] 收到事件: {}", eventType);
        
        switch (eventType) {
            case "READY":
                ready = true;
                String sessionId = msg.get("d").getAsJsonObject()
                    .get("session_id").getAsString();
                log.info("[QQBot] 就绪! Session: {}...", sessionId.substring(0, 8));
                break;
                
            case "C2C_MESSAGE_CREATE":
                handleC2CMessage(msg.get("d").getAsJsonObject());
                break;
                
            // 群消息和频道消息不处理（未订阅）
            case "GROUP_AT_MESSAGE_CREATE":
            case "AT_MESSAGE_CREATE":
                log.debug("[QQBot] 忽略群/频道消息（未订阅）");
                break;
        }
    }
    
    /**
     * 处理群消息
     */
    private void handleGroupMessage(JsonObject data) {
        String groupId = data.get("group_id").getAsString();
        String userId = data.get("author").getAsJsonObject().get("id").getAsString();
        String content = data.get("content").getAsString();
        String msgId = data.get("id").getAsString();
        String nickname = data.get("author").getAsJsonObject().get("username").getAsString();
        
        // 去除 @机器人的部分
        String pureContent = content.replaceAll("<qq:at[^>]+/?>", "").trim();
        
        log.info("[QQBot] 群消息 [{}] {}: {}", groupId, nickname, pureContent);
        
        // 构建 QQ 消息对象
        QQBotMessage qqMsg = new QQBotMessage(
            QQBotMessage.Type.GROUP,
            groupId,
            userId,
            nickname,
            pureContent,
            msgId
        );
        
        // 异步处理
        if (taskExecutor != null) {
            taskExecutor.execute(() -> messageHandler.handleMessage(qqMsg, this));
        } else {
            new Thread(() -> messageHandler.handleMessage(qqMsg, this)).start();
        }
    }
    
    /**
     * 处理私聊消息
     */
    private void handleC2CMessage(JsonObject data) {
        log.info("[QQBot] 处理私聊消息: {}", data);
        
        String userId = data.get("author").getAsJsonObject().get("id").getAsString();
        String content = data.get("content").getAsString();
        String msgId = data.get("id").getAsString();
        String nickname = data.get("author").getAsJsonObject().has("username") 
            ? data.get("author").getAsJsonObject().get("username").getAsString() 
            : userId;
        
        log.info("[QQBot] 私聊消息 [{}] {}: {}", userId, nickname, content);
        
        QQBotMessage qqMsg = new QQBotMessage(
            QQBotMessage.Type.C2C,
            null,
            userId,
            nickname,
            content,
            msgId
        );
        
        if (taskExecutor != null) {
            taskExecutor.execute(() -> messageHandler.handleMessage(qqMsg, this));
        } else {
            new Thread(() -> messageHandler.handleMessage(qqMsg, this)).start();
        }
    }
    
    /**
     * 处理频道消息
     */
    private void handleGuildMessage(JsonObject data) {
        String channelId = data.get("channel_id").getAsString();
        String userId = data.get("author").getAsJsonObject().get("id").getAsString();
        String content = data.get("content").getAsString();
        String msgId = data.get("id").getAsString();
        String nickname = data.get("author").getAsJsonObject().get("username").getAsString();
        
        String pureContent = content.replaceAll("<qq:at[^>]+/?>", "").trim();
        
        log.info("[QQBot] 频道消息 [{}] {}: {}", channelId, nickname, pureContent);
        
        QQBotMessage qqMsg = new QQBotMessage(
            QQBotMessage.Type.GUILD,
            channelId,
            userId,
            nickname,
            pureContent,
            msgId
        );
        
        if (taskExecutor != null) {
            taskExecutor.execute(() -> messageHandler.handleMessage(qqMsg, this));
        } else {
            new Thread(() -> messageHandler.handleMessage(qqMsg, this)).start();
        }
    }
    
    /**
     * 发送群消息回复
     */
    public boolean sendGroupReply(String groupId, String msgId, String content) {
        try {
            ensureTokenValid();
            
            JsonObject body = new JsonObject();
            body.addProperty("content", content);
            body.addProperty("msg_id", msgId);
            body.addProperty("msg_seq", System.currentTimeMillis() % 100000);
            
            Request request = new Request.Builder()
                .url(API_BASE + "/v2/groups/" + groupId + "/messages")
                .header("Authorization", "QQBot " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 群回复失败: {}", response.body().string());
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送群消息失败", e);
            return false;
        }
    }
    
    /**
     * 发送私聊回复
     */
    public boolean sendC2CReply(String userId, String msgId, String content) {
        try {
            ensureTokenValid();
            
            JsonObject body = new JsonObject();
            body.addProperty("content", content);
            body.addProperty("msg_id", msgId);
            body.addProperty("msg_seq", System.currentTimeMillis() % 100000);
            
            Request request = new Request.Builder()
                .url(API_BASE + "/v2/users/" + userId + "/messages")
                .header("Authorization", "QQBot " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 私聊回复失败: {}", response.body().string());
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送私聊失败", e);
            return false;
        }
    }
    
    /**
     * 发送频道消息回复
     */
    public boolean sendChannelReply(String channelId, String msgId, String content) {
        try {
            ensureTokenValid();
            
            JsonObject body = new JsonObject();
            body.addProperty("content", content);
            body.addProperty("msg_id", msgId);
            body.addProperty("msg_seq", System.currentTimeMillis() % 100000);
            
            Request request = new Request.Builder()
                .url(API_BASE + "/channels/" + channelId + "/messages")
                .header("Authorization", "QQBot " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 频道回复失败: {}", response.body().string());
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送频道消息失败", e);
            return false;
        }
    }
    
    /**
     * 发送主动群消息（不引用原消息）
     * 用于超过回复限制后的主动推送
     */
    public boolean sendGroupMessage(String groupId, String content) {
        try {
            ensureTokenValid();
            
            JsonObject body = new JsonObject();
            body.addProperty("content", content);
            // 主动消息不设置 msg_id
            body.addProperty("msg_seq", System.currentTimeMillis() % 100000);
            
            Request request = new Request.Builder()
                .url(API_BASE + "/v2/groups/" + groupId + "/messages")
                .header("Authorization", "QQBot " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 主动群消息失败: {}", response.body().string());
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送主动群消息失败", e);
            return false;
        }
    }
    
    /**
     * 发送主动私聊消息（不引用原消息）
     */
    public boolean sendC2CMessage(String userId, String content) {
        try {
            ensureTokenValid();
            
            JsonObject body = new JsonObject();
            body.addProperty("content", content);
            body.addProperty("msg_seq", System.currentTimeMillis() % 100000);
            
            Request request = new Request.Builder()
                .url(API_BASE + "/v2/users/" + userId + "/messages")
                .header("Authorization", "QQBot " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 主动私聊失败: {}", response.body().string());
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送主动私聊失败", e);
            return false;
        }
    }
    
    /**
     * 发送主动频道消息（不引用原消息）
     */
    public boolean sendChannelMessage(String channelId, String content) {
        try {
            ensureTokenValid();
            
            JsonObject body = new JsonObject();
            body.addProperty("content", content);
            body.addProperty("msg_seq", System.currentTimeMillis() % 100000);
            
            Request request = new Request.Builder()
                .url(API_BASE + "/channels/" + channelId + "/messages")
                .header("Authorization", "QQBot " + accessToken)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 主动频道消息失败: {}", response.body().string());
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送主动频道消息失败", e);
            return false;
        }
    }
    
    /**
     * 发送文件到用户（C2C私聊）
     * 使用 /v2/users/{userId}/files 接口
     * @param userId 用户ID
     * @param file 要发送的文件
     * @param caption 文件说明文字
     * @return 是否发送成功
     */
    public boolean sendFileToUser(String userId, java.io.File file, String caption) {
        try {
            ensureTokenValid();
            
            // 读取文件并转换为Base64
            byte[] fileBytes = new byte[(int) file.length()];
            try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
                fis.read(fileBytes);
            }
            String base64Data = java.util.Base64.getEncoder().encodeToString(fileBytes);
            
            log.info("[QQBot] 发送文件: userId={}, fileName={}, size={}, base64Length={}", 
                userId, file.getName(), fileBytes.length, base64Data.length());
            
            // 构建请求体
            JsonObject body = new JsonObject();
            body.addProperty("file_type", 4);  // 4 = 文件
            body.addProperty("file_data", base64Data);
            body.addProperty("file_name", file.getName());  // 指定文件名
            body.addProperty("srv_send_msg", true);  // 直接发送
            
            if (caption != null && !caption.isEmpty()) {
                body.addProperty("content", caption);
            }
            
            String url = API_BASE + "/v2/users/" + userId + "/files";
            
            Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "QQBot " + accessToken)
                .header("X-Union-Appid", appId)
                .header("Content-Type", "application/json")
                .post(RequestBody.create(
                    MediaType.parse("application/json"),
                    body.toString()
                ))
                .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String respBody = response.body().string();
                boolean success = response.isSuccessful();
                if (!success) {
                    log.error("[QQBot] 发送文件失败: HTTP={}, response={}", response.code(), respBody);
                } else {
                    log.info("[QQBot] 发送文件成功: {}", respBody);
                }
                return success;
            }
        } catch (Exception e) {
            log.error("[QQBot] 发送文件异常", e);
            return false;
        }
    }
    
    /**
     * 启动心跳
     */
    private void startHeartbeat(int interval) {
        new Thread(() -> {
            while (running && wsClient != null && wsClient.isOpen()) {
                try {
                    Thread.sleep(interval);
                    
                    JsonObject heartbeat = new JsonObject();
                    heartbeat.addProperty("op", 1);
                    heartbeat.add("d", gson.toJsonTree(seq));
                    
                    wsClient.send(heartbeat.toString());
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "QQBot-Heartbeat").start();
        log.info("[QQBot] 心跳线程已启动");
    }
    
    /**
     * 计划重连 - 使用指数退避策略
     */
    private void scheduleReconnect() {
        if (reconnecting) {
            return;
        }
        
        reconnecting = true;
        reconnectAttempts++;
        
        if (reconnectAttempts > MAX_RECONNECT_ATTEMPTS) {
            log.error("[QQBot] 重连次数超过最大限制({})，停止重连", MAX_RECONNECT_ATTEMPTS);
            running = false;
            reconnecting = false;
            return;
        }
        
        // 指数退避：5秒, 10秒, 20秒, 40秒... 最大60秒
        long delay = INITIAL_RECONNECT_DELAY * (1L << (reconnectAttempts - 1));
        delay = Math.min(delay, 60000); // 最大60秒
        
        final long reconnectDelay = delay;
        log.info("[QQBot] 第{}次重连，{}秒后尝试...", reconnectAttempts, reconnectDelay / 1000);
        
        new Thread(() -> {
            try {
                Thread.sleep(reconnectDelay);
                
                // 重置状态
                running = false;
                ready = false;
                
                // 确保旧连接彻底关闭
                if (wsClient != null) {
                    try {
                        wsClient.close();
                    } catch (Exception e) {
                        // ignore
                    }
                    wsClient = null;
                }
                
                // 重新启动
                start();
                
                // 如果启动成功，重置重连计数
                if (running) {
                    reconnectAttempts = 0;
                    reconnecting = false;
                    log.info("[QQBot] 重连成功，重置重连计数");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                reconnecting = false;
            }
        }, "QQBot-Reconnect-" + reconnectAttempts).start();
    }
}
