/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.exrick.manager.service.RobotService
 *  cn.exrick.manager.service.tg.TelegramChannelMonitor$GetUpdatesResponse
 *  com.google.gson.FieldNamingPolicy
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  org.telegram.telegrambots.bots.DefaultBotOptions
 *  org.telegram.telegrambots.bots.DefaultBotOptions$ProxyType
 *  org.telegram.telegrambots.bots.TelegramLongPollingBot
 *  org.telegram.telegrambots.meta.TelegramBotsApi
 *  org.telegram.telegrambots.meta.api.methods.BotApiMethod
 *  org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup
 *  org.telegram.telegrambots.meta.api.methods.send.SendMessage
 *  org.telegram.telegrambots.meta.api.methods.send.SendPhoto
 *  org.telegram.telegrambots.meta.api.objects.InputFile
 *  org.telegram.telegrambots.meta.api.objects.Message
 *  org.telegram.telegrambots.meta.api.objects.Update
 *  org.telegram.telegrambots.meta.api.objects.media.InputMedia
 *  org.telegram.telegrambots.meta.exceptions.TelegramApiException
 *  org.telegram.telegrambots.meta.generics.LongPollingBot
 *  org.telegram.telegrambots.updatesreceivers.DefaultBotSession
 */
package cn.exrick.manager.service.tg;

import cn.exrick.manager.service.RobotService;
import cn.exrick.manager.service.impl.RobotServiceImpl;
import cn.exrick.manager.service.tg.TelegramChannelMonitor;
import cn.exrick.manager.service.util.ThreadSafeFileWriter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramChannelMonitor
extends TelegramLongPollingBot {
    private final String targetChannel;
    private volatile boolean running = true;
    private RobotService robotService;
    private final Executor taskExecutor;
    private final String botUsername;
    private final String botToken;
    private long lastUpdateId = 0L;
    private final Set<Long> targetGroupIds;
    private final Map<Long, List<Integer>> groupTopics;
    private static final Map<Long, TelegramChannelMonitor> CHAT_TO_MONITOR = new ConcurrentHashMap<Long, TelegramChannelMonitor>();
    private static final Set<String> REGISTERED_BOTS = new HashSet<String>();
    static ThreadSafeFileWriter writerLog = null;
    private static final Set<Long> BLOCKED_USER_IDS = new HashSet<Long>(Arrays.asList(8565146565L, 8254338746L, 8596885082L, 8790000265L, 8705924312L, 8509629103L, 7536707140L));
    private static final Set<String> BLOCKED_USERNAMES = new HashSet<String>(Arrays.asList("ZNPPgy", "xanthindaba"));
    private static final Map<Long, Long> PRIVATE_CHAT_COOLDOWN = new ConcurrentHashMap<Long, Long>();
    private static final long PRIVATE_COOLDOWN_MS = 1000L;
    private static final Set<Long> PRIVATE_ADMIN_IDS = new HashSet<Long>(Arrays.asList(1399330035L));

    public TelegramChannelMonitor(String botToken, String botUsername, String channelUsername, DefaultBotOptions botOptions, RobotService robotService, Executor taskExecutor, Set<Long> targetGroupIds, Map<Long, List<Integer>> groupTopics) {
        super(botOptions, botToken);
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.robotService = robotService;
        this.taskExecutor = taskExecutor;
        this.targetGroupIds = targetGroupIds != null ? targetGroupIds : new HashSet();
        this.groupTopics = groupTopics != null ? groupTopics : new HashMap();
        this.targetChannel = channelUsername.startsWith("@") ? channelUsername : "@" + channelUsername;
        for (Long gid : this.targetGroupIds) {
            CHAT_TO_MONITOR.put(gid, this);
        }
        this.startMonitoring();
        try {
            writerLog = new ThreadSafeFileWriter("/tmp/robot.txt");
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String botToken = "8485884288:AAFQj7kI1xPSk6HZDPK4LT18LFd5b50C8zQ";
        String botUsername = "summer0011999bot";
        String channelName = "taoluwanwuzhibo";
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
        botOptions.setProxyHost("localhost");
        botOptions.setProxyPort(7890);
        RobotServiceImpl robotServicea = new RobotServiceImpl();
        HashSet<Long> groups = new HashSet<Long>(Arrays.asList(-1003867299066L));
        HashMap<Long, List<Integer>> topics = new HashMap<Long, List<Integer>>();
        topics.put(-1003867299066L, Arrays.asList(206, 2564));
        TelegramChannelMonitor bot = new TelegramChannelMonitor(botToken, botUsername, channelName, botOptions, robotServicea, null, groups, topics);
        bot.startMonitoring();
        System.out.println("\u7ed3\u675f\u4e86");
    }

    public synchronized void startMonitoring() {
        if (REGISTERED_BOTS.contains(this.botUsername)) {
            System.out.println("\u673a\u5668\u4eba " + this.botUsername + " \u5df2\u542f\u52a8\uff0c\u8df3\u8fc7\u91cd\u590d\u542f\u52a8");
            return;
        }
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot((LongPollingBot)this);
            REGISTERED_BOTS.add(this.botUsername);
            System.out.println("[" + this.botUsername + "] DefaultBotSession \u542f\u52a8\u6210\u529f");
            System.out.println("\u5f00\u59cb\u76d1\u63a7\u9891\u9053: {}" + this.targetChannel);
        }
        catch (TelegramApiException e) {
            System.err.println("[" + this.botUsername + "] DefaultBotSession \u542f\u52a8\u5931\u8d25\uff0cfallback \u5230\u81ea\u5b9a\u4e49\u8f6e\u8be2: " + e.getMessage());
            this.startCustomPolling();
            REGISTERED_BOTS.add(this.botUsername);
            System.out.println("[" + this.botUsername + "] \u81ea\u5b9a\u4e49\u8f6e\u8be2\u5df2\u542f\u52a8");
        }
    }

    private void startCustomPolling() {
        new Thread(() -> {
            while (this.running) {
                try {
                    String line;
                    String url = "https://api.telegram.org/bot" + this.botToken + "/getUpdates?offset=" + (this.lastUpdateId + 1L) + "&limit=100";
                    HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(50000);
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                    GetUpdatesResponse updatesResponse = (GetUpdatesResponse)gson.fromJson(response.toString(), GetUpdatesResponse.class);
                    if (updatesResponse == null || !updatesResponse.ok || updatesResponse.result == null) continue;
                    for (Update update : updatesResponse.result) {
                        if (update.getUpdateId() != null) {
                            this.lastUpdateId = update.getUpdateId().intValue();
                        }
                        this.onUpdateReceived(update);
                    }
                }
                catch (Exception e) {
                    System.err.println("[" + this.botUsername + "] \u8f6e\u8be2\u5f02\u5e38: " + e.getMessage());
                    try {
                        Thread.sleep(5000L);
                    }
                    catch (InterruptedException ie) {
                        break;
                    }
                }
            }
        }, this.botUsername + " Custom Polling").start();
    }

    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> {
            Long fromUserId = null;
            String fromUsername = null;
            if (update.hasChannelPost() && update.getChannelPost().getFrom() != null) {
                fromUserId = update.getChannelPost().getFrom().getId();
                fromUsername = update.getChannelPost().getFrom().getUserName();
            } else if (update.hasMessage() && update.getMessage().getFrom() != null) {
                fromUserId = update.getMessage().getFrom().getId();
                fromUsername = update.getMessage().getFrom().getUserName();
            }
            if (fromUserId != null && BLOCKED_USER_IDS.contains(fromUserId)) {
                System.out.println("\u3010\u9ed1\u540d\u5355\u62e6\u622a\u3011\u7528\u6237ID: " + fromUserId + "\uff0c\u6d88\u606f\u5df2\u5ffd\u7565");
                return;
            }
            if (fromUsername != null && BLOCKED_USERNAMES.contains(fromUsername)) {
                System.out.println("\u3010\u9ed1\u540d\u5355\u62e6\u622a\u3011\u7528\u6237\u540d: @" + fromUsername + "\uff0c\u6d88\u606f\u5df2\u5ffd\u7565");
                return;
            }
            System.out.println("new message");
            System.out.println(update.hasChannelPost());
            if (update.hasChannelPost() && this.targetChannel.equals("@" + update.getChannelPost().getChat().getUserName())) {
                Message channelMsg = update.getChannelPost();
                System.out.println(update.getChannelPost().getChat().getUserName());
                Long chatId = channelMsg.getChatId();
                String text = channelMsg.getText();
                System.out.println("\u65b0\u6d88\u606f---: " + update.getChannelPost().getText());
                if (update.getChannelPost().hasPhoto()) {
                    System.out.println("\u68c0\u6d4b\u5230\u56fe\u7247\u9644\u4ef6");
                }
                System.out.println("\u6d88\u606f\u6765\u81ea: " + channelMsg.toString());
                writerLog.write(channelMsg.toString() + "\t" + text);
                try {
                    RobotServiceImpl.setCurrentBot(this);
                    this.robotService.dealSearch(update);
                }
                finally {
                    RobotServiceImpl.clearCurrentBot();
                }
            }
            if (update.hasMessage()) {
                boolean isGroupByType;
                if (!update.getMessage().hasText()) {
                    System.out.println("\u6ca1\u6709\u6587\u672c");
                    return;
                }
                Message receivedMessage = update.getMessage();
                Long chatId = receivedMessage.getChatId();
                String userName = receivedMessage.getFrom().getUserName();
                String receivedText = receivedMessage.getText();
                String identifier = receivedMessage.getFrom().getUserName() != null ? "@" + receivedMessage.getFrom().getUserName() : receivedMessage.getFrom().getFirstName() + receivedMessage.getFrom().getLastName() + " (ID:" + receivedMessage.getFrom().getId() + ")";
                System.out.println(receivedMessage);
                String chatType = receivedMessage.getChat().getType();
                boolean bl = isGroupByType = "group".equals(chatType) || "supergroup".equals(chatType);
                if (isGroupByType) {
                    block51: {
                        System.out.println("\u7fa4\u7ec4\u6d88\u606f\uff1a");
                        try {
                            System.out.println("type3:---------------------:lps::::" + chatType);
                            boolean isGroup = "supergroup".equals(chatType);
                            if (!isGroup) {
                                System.out.println("\u4e0d\u662f\u79c1\u6709\u7fa4\u7ec4\u6d88\u606f\uff0c\u5ffd\u7565:" + chatType);
                            } else {
                                System.out.println("\u53d1\u73b0\u7fa4\u7c7b\u578b:" + chatType);
                            }
                            boolean vip = false;
                            int groupok = 0;
                            int topicok = 0;
                            if (!this.targetGroupIds.contains(chatId)) {
                                System.out.println("333\u4e0d\u662f\u76ee\u6807\u7fa4\u7ec4\uff0c\u5f53\u524d\u7fa4\u7ec4ID: " + chatId);
                            } else {
                                groupok = 1;
                            }
                            if (groupok == 1) {
                                Integer messageThreadId = receivedMessage.getMessageThreadId();
                                if (messageThreadId == null) {
                                    System.out.println("\u8fd9\u662f\u666e\u901a\u6d88\u606f\uff08\u975e\u8bdd\u9898\uff09");
                                    List<Integer> topics = this.groupTopics.get(chatId);
                                    if (topics == null || topics.isEmpty()) {
                                        System.out.println("\u3010\u65e0\u8bdd\u9898\u9650\u5236\u7fa4\u3011\u666e\u901a\u6d88\u606f\u89c6\u4e3a\u76ee\u6807\u8bdd\u9898");
                                        topicok = 1;
                                    } else if (chatId.equals(-1003992613609L)) {
                                        System.out.println("\u3010399\u7fa4\u3011\u666e\u901a\u6d88\u606f\u89c6\u4e3a\u76ee\u6807\u8bdd\u9898");
                                        topicok = 1;
                                    }
                                } else {
                                    System.out.println("\u8bdd\u9898IDs: " + messageThreadId);
                                    List<Integer> topics = this.groupTopics.get(chatId);
                                    if (topics != null && !topics.isEmpty()) {
                                        if (messageThreadId.equals(topics.get(0))) {
                                            System.out.println("\u2705 \u6765\u81ea\u76ee\u6807\u7fa4\u7ec4\u7684\u76ee\u6807\u8bdd\u9898\uff0c\u5fc5\u987b\u54cd\u5e94\u3002");
                                            topicok = 1;
                                        } else if (topics.size() > 1 && messageThreadId.equals(topics.get(1))) {
                                            System.out.println("\u2705 \u6765\u81ea\u76ee\u6807\u7fa4\u7ec4\u7684 \u5c0f\u98de\u673a\u7f51\u76d8 \u8bdd\u9898\uff0c\u5fc5\u987b\u54cd\u5e94\u3002");
                                            topicok = 2;
                                        } else {
                                            System.out.println("\u6765\u81ea\u76ee\u6807\u7fa4\u7ec4\u7684\u5176\u4ed6\u8bdd\u9898,\u4e0d\u54cd\u5e94\u3002");
                                        }
                                    } else {
                                        System.out.println("\u6765\u81ea\u76ee\u6807\u7fa4\u4f53\u672a\u914d\u7f6e\u8be5\u8bdd\u9898\uff0c\u4e0d\u54cd\u5e94\u3002");
                                    }
                                }

                            }
                            if (topicok == 1 || topicok == 2) {
                                System.out.println("vip\u7fa4 \u641c\u7d22\u548c\u63d0\u53d6");
                                try {
                                    RobotServiceImpl.setCurrentBot(this);
                                    this.robotService.dealGetWork(update);
                                    break block51;
                                }
                                finally {
                                    RobotServiceImpl.clearCurrentBot();
                                }
                            }
                            if (groupok == 1 || groupok == 2) {
                                System.out.println("vip\u7fa4/\u5907\u4efd\u7fa4\u5185\u90e8\u975e\u641c\u7d22\u548c\u63d0\u53d6");
                                break block51;
                            }
                            try {
                                RobotServiceImpl.setCurrentBot(this);
                                this.robotService.dealSearch(update);
                            }
                            finally {
                                RobotServiceImpl.clearCurrentBot();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }
                System.out.println("\u79c1\u804a");
                boolean isPrivateChat = "private".equals(chatType);
                if (isPrivateChat && fromUserId != null && !PRIVATE_ADMIN_IDS.contains(fromUserId)) {
                    long now = System.currentTimeMillis();
                    Long lastTime = PRIVATE_CHAT_COOLDOWN.get(fromUserId);
                    if (lastTime != null && now - lastTime < 1000L) {
                        long waitSec = (1000L - (now - lastTime)) / 1000L;
                        System.out.println("\u3010\u79c1\u804a\u9650\u901f\u3011\u7528\u6237 " + fromUserId + " \u89e6\u53d1\u51b7\u5374\uff0c\u8fd8\u9700\u7b49\u5f85 " + waitSec + " \u79d2");
                        try {
                            SendMessage limitMsg = new SendMessage();
                            limitMsg.setChatId(chatId.toString());
                            limitMsg.setText("\u23f3 \u64cd\u4f5c\u592a\u9891\u7e41\uff0c\u8bf7 " + waitSec + " \u79d2\u540e\u518d\u8bd5\u3002");
                            this.execute(limitMsg);
                        }
                        catch (TelegramApiException te) {
                            System.err.println("\u53d1\u9001\u9650\u901f\u63d0\u793a\u5931\u8d25: " + te.getMessage());
                        }
                        return;
                    }
                    PRIVATE_CHAT_COOLDOWN.put(fromUserId, now);
                }
                System.out.println("\u6d88\u606f\u6765\u81ea: " + identifier);
                if (writerLog != null) {
                    try {
                        writerLog.write(identifier + "\t" + receivedText);
                    }
                    catch (Exception e) {
                        System.err.println("writerLog \u5199\u5165\u5931\u8d25: " + e.getMessage());
                    }
                }
                System.out.println("\u8c03\u7528 dealGetWork, robotService=" + (this.robotService != null));
                try {
                    RobotServiceImpl.setCurrentBot(this);
                    this.robotService.dealGetWork(update);
                    System.out.println("dealGetWork \u8c03\u7528\u5b8c\u6210");
                }
                catch (Exception e) {
                    System.err.println("dealGetWork \u5f02\u5e38: " + e.getMessage());
                    e.printStackTrace();
                    try {
                        SendMessage errorMsg = new SendMessage();
                        errorMsg.setChatId(chatId.toString());
                        errorMsg.setText("\u274c \u5904\u7406\u5931\u8d25: " + e.getMessage());
                        this.execute(errorMsg);
                    }
                    catch (Exception e2) {
                        System.err.println("\u53d1\u9001\u9519\u8bef\u6d88\u606f\u5931\u8d25: " + e2.getMessage());
                    }
                }
                finally {
                    RobotServiceImpl.clearCurrentBot();
                }
            }
        }, this.taskExecutor);
    }

    public String getBotUsername() {
        return this.botUsername;
    }

    private Long extractChatId(BotApiMethod<?> method) {
        try {
            Method getChatIdMethod = method.getClass().getMethod("getChatId", new Class[0]);
            Object chatIdObj = getChatIdMethod.invoke(method, new Object[0]);
            if (chatIdObj instanceof String) {
                return Long.valueOf((String)chatIdObj);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        Long chatId = this.extractChatId(method);
        if (chatId != null) {
            if (chatId > 0L) {
                System.out.println("[\u79c1\u804a\u53d1\u9001] bot=" + this.botUsername + ", chatId=" + chatId + ", tokenPrefix=" + (this.botToken != null ? this.botToken.substring(0, 15) : "null"));
                try {
                    Serializable result = super.execute(method);
                    System.out.println("[\u79c1\u804a\u53d1\u9001\u6210\u529f] bot=" + this.botUsername + ", resultClass=" + (result != null ? result.getClass().getName() : "null"));
                    return (T)result;
                }
                catch (Throwable e) {
                    System.out.println("[\u79c1\u804a\u53d1\u9001\u5931\u8d25] bot=" + this.botUsername + ", error=" + e.getClass().getName() + ": " + e.getMessage());
                    if (e instanceof TelegramApiException) {
                        throw (TelegramApiException)e;
                    }
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException)e;
                    }
                    if (e instanceof Error) {
                        throw (Error)e;
                    }
                    throw new RuntimeException(e);
                }
            }
            TelegramChannelMonitor monitor = CHAT_TO_MONITOR.get(chatId);
            if (monitor != null && monitor != this) {
                return monitor.execute(method);
            }
            if (monitor == null && !CHAT_TO_MONITOR.isEmpty()) {
                for (TelegramChannelMonitor fallback : CHAT_TO_MONITOR.values()) {
                    if (fallback == this) continue;
                    try {
                        return fallback.superExecute(method);
                    }
                    catch (TelegramApiException e) {
                        String msg = e.getMessage();
                        if (msg != null && (msg.contains("bot was kicked") || msg.contains("Chat not found") || msg.contains("chat not found") || msg.contains("bot can't initiate conversation"))) {
                            System.out.println("[Fallback] " + fallback.getBotUsername() + " \u65e0\u6cd5\u53d1\u9001\u5230 " + chatId + ", \u5c1d\u8bd5\u4e0b\u4e00\u4e2a bot");
                            continue;
                        }
                        throw e;
                    }
                }
            }
        }
        return (T)super.execute(method);
    }

    public <T extends Serializable> T superExecute(BotApiMethod<T> method) throws TelegramApiException {
        return (T)super.execute(method);
    }

    public void sendChannelReply(Long chatId, String replyText, Integer replyToMsgId) {
        SendMessage reply = new SendMessage();
        reply.setChatId(chatId.toString());
        reply.setReplyToMessageId(replyToMsgId);
        reply.setText(replyText);
        // 禁用 MarkdownV2，搜索汇总文字含 = - 等保留字符会导致 400 Bad Request
        // reply.enableMarkdownV2(true);
        try {
            System.out.println("[sendChannelReply] bot=" + this.getBotUsername() + ", chatId=" + chatId + ", textLen=" + replyText.length());
            if (CHAT_TO_MONITOR.get(chatId) == null) {
                System.out.println("[sendChannelReply] \u7fa4\u4e0d\u5728CHAT_TO_MONITOR\uff0c\u76f4\u63a5\u53d1");
                super.execute((BotApiMethod)reply);
            } else {
                this.execute(reply);
            }
            System.out.println("\u2705 \u56de\u590d\u9891\u9053\u6d88\u606f\u6210\u529f");
        }
        catch (TelegramApiException e) {
            System.err.println("\u274c \u56de\u590d\u5931\u8d25: " + e.getMessage());
        }
    }

    public int sendChannelReplyWithPhoto(Long chatId, String caption, Integer replyToMsgId, String photoUrl) {
        SendPhoto reply = new SendPhoto();
        reply.setChatId(chatId.toString());
        reply.setReplyToMessageId(replyToMsgId);
        reply.setPhoto(new InputFile(photoUrl));
        reply.setCaption(caption);
        try {
            if (CHAT_TO_MONITOR.get(chatId) == null) {
                System.out.println("[sendChannelReplyWithPhoto] \u7fa4\u4e0d\u5728CHAT_TO_MONITOR\uff0c\u76f4\u63a5\u53d1\uff0cbot=" + this.getBotUsername() + ", chatId=" + chatId);
                super.execute(reply);
            } else {
                this.execute(reply);
            }
            System.out.println("\u2705 \u5e26\u56fe\u7247\u7684\u56de\u590d\u53d1\u9001\u6210\u529f");
            return 1;
        }
        catch (TelegramApiException e) {
            System.err.println("\u274c \u53d1\u9001\u5931\u8d25: " + e.getMessage());
            return 0;
        }
    }

    public void sendPhotos(Long chatId, Integer replyToMsgId, List<InputMedia> mediaList) {
        SendMediaGroup mediaGroup = new SendMediaGroup();
        mediaGroup.setChatId(chatId.toString());
        mediaGroup.setMedias(mediaList);
        mediaGroup.setReplyToMessageId(replyToMsgId);
        try {
            this.execute(mediaGroup);
            System.out.println("\u2705 \u5e26\u56fe\u7247\u7684\u56de\u590d\u53d1\u9001\u6210\u529f");
        }
        catch (TelegramApiException e) {
            System.err.println("\u274c \u53d1\u9001\u5931\u8d25: " + e.getMessage());
        }
    }

    private static class GetUpdatesResponse {
        public boolean ok;
        public Update[] result;
    }
}
