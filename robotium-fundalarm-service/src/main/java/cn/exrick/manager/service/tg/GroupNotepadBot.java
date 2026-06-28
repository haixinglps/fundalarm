package cn.exrick.manager.service.tg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.isearch.Isearch;
import cn.exrick.manager.isearch.query.Search;
import cn.exrick.manager.isearch.query.SearchFactory;
import cn.exrick.manager.mapper.Taolu3VideoMapper;
import cn.exrick.manager.mapper.TbWalletMapper;
import cn.exrick.manager.mapper.Waiwang2VideoMapper;
import cn.exrick.manager.mapper.WaiwangVideoMapper;
import cn.exrick.manager.mapper.WanwuVideoMapper;
import cn.exrick.manager.mapper.ZmqVideoMapper;
import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.Taolu3VideoExample;
import cn.exrick.manager.pojo.TbWallet;
import cn.exrick.manager.pojo.TbWalletExample;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.Waiwang2VideoExample;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WaiwangVideoExample;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.WanwuVideoExample;
import cn.exrick.manager.pojo.ZmqVideo;
import cn.exrick.manager.pojo.ZmqVideoExample;
import cn.exrick.manager.service.RobotService;

import com.github.pagehelper.PageHelper;
import com.zhongsou.search.core.query.Hits;

@Component
public class GroupNotepadBot extends TelegramLongPollingBot {
    
    @Autowired
    private JedisClient jedisClient;
    
    @Autowired
    private RobotService robotService;
    
    private final Executor taskExecutor;
    private final String targetGroupId;
    private final String targetGroupId2;
    private volatile boolean running = true;
    private static volatile boolean started = false;
    
    @Autowired
    private Waiwang2VideoMapper waiwang2VideoMapper;
    
    @Autowired
    private WaiwangVideoMapper waiwangVideoMapper;
    
    @Autowired
    private WanwuVideoMapper wanwuVideoMapper;
    
    @Autowired
    private Taolu3VideoMapper taolu3VideoMapper;
    
    @Autowired
    private ZmqVideoMapper zmqVideoMapper;
    
    @Autowired
    private TbWalletMapper tbWalletMapper;
    
    // 新用户默认余额
    private static final int DEFAULT_BALANCE = 5;
    // 提取作品扣费金额
    private static final int DEDUCT_AMOUNT = 1;
    // Telegram 用户 topic 标识（用于区分不同平台）
    private static final int TG_TOPIC_TYPE = 4;
    
    public GroupNotepadBot(String botToken, String targetGroupId, String targetGroupId2, DefaultBotOptions botOptions, 
                          RobotService robotService, Executor taskExecutor) {
        super(botOptions, botToken);
        this.robotService = robotService;
        this.taskExecutor = taskExecutor;
        this.targetGroupId = targetGroupId;
        this.targetGroupId2 = targetGroupId2;
    }
    
    public synchronized void startMonitoring() {
        if (started) {
            System.out.println("[GroupNotepadBot] 机器人已启动，跳过重复启动");
            return;
        }
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            started = true;
            System.out.println("[GroupNotepadBot] 启动成功，监控群组: " + targetGroupId + 
                               (targetGroupId2 != null && !targetGroupId2.isEmpty() ? ", " + targetGroupId2 : ""));
        } catch (TelegramApiException e) {
            System.err.println("[GroupNotepadBot] 启动失败: " + e.getMessage());
        }
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String chatType = message.getChat().getType();
        
        // 支持群聊和私聊
        boolean isGroup = "group".equals(chatType) || "supergroup".equals(chatType);
        boolean isPrivate = "private".equals(chatType);
        
        // 群聊只响应指定群组，私聊全部响应
        boolean isTargetGroup = targetGroupId.equals(chatId.toString()) || 
                                (targetGroupId2 != null && targetGroupId2.equals(chatId.toString()));
        if (isGroup && !isTargetGroup) {
            System.out.println("[GroupNotepadBot] 非目标群组，忽略: " + chatId);
            return;
        }
        
        // 群聊只回应话题1（普通聊天），其他话题忽略
        Integer messageThreadId = message.getMessageThreadId();
        if (isGroup && messageThreadId != null && messageThreadId != 1) {
            System.out.println("[GroupNotepadBot] 非话题1的消息，忽略。topicId=" + messageThreadId);
            return;
        }
        
        String text = message.hasText() ? message.getText() : "";
        String caption = message.getCaption();
        // 如果 text 为空但 caption 有内容，使用 caption
        if ((text == null || text.trim().isEmpty()) && caption != null && !caption.trim().isEmpty()) {
            text = caption;
        }
        User fromUser = message.getFrom();
        String username = fromUser.getUserName();
        String firstName = fromUser.getFirstName();
        String lastName = fromUser.getLastName();
        String displayName = (username != null && !username.isEmpty()) ? "@" + username 
            : firstName + (lastName != null ? " " + lastName : "");
        Integer messageId = message.getMessageId();
        
        // 判断是否为媒体消息（视频/图片/文档等）
        boolean isMediaMessage = message.hasVideo() || message.hasPhoto() || message.hasDocument() 
                                 || message.hasAudio() || message.hasVoice() || message.hasAnimation();
        
        System.out.println("[GroupNotepadBot] 收到" + (isPrivate ? "私聊" : "群聊") + "消息 from " + displayName 
            + " | hasText=" + message.hasText() + " | isMedia=" + isMediaMessage + " | caption='" + caption + "' | text='" + text + "' | chatId=" + chatId 
            + " | msgId=" + messageId + " | threadId=" + messageThreadId);
        
        // 逻辑：先尝试提取作品，前缀不符合才降级到发记事本
        if (isExtractCommand(text)) {
            handleExtractWork(update, displayName);
            return;
        }
        
        // 非提取指令，降级为搜索发记事本
        // 媒体消息的 caption 不触发搜索，避免把视频标题当搜索关键词
        if (!isMediaMessage && text != null && !text.trim().isEmpty() && !text.startsWith("/")) {
            handleSearchAndNotepad(chatId, messageId, text.trim(), displayName);
        }
    }
    
    private boolean isExtractCommand(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        String cmd = text.trim().toLowerCase();
        return cmd.matches("^(ww|zm|tl|tg|ch|bc|zb)\\d+.*");
    }
    
    private void handleExtractWork(Update update, String displayName) {
        Message message = update.getMessage();
        String text = message.getText().trim();
        String[] parts = text.split("\\p{javaWhitespace}+");
        String cmd = parts[0].toLowerCase();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        
        // 判断是私聊还是群聊
        String chatType = message.getChat().getType();
        boolean isPrivate = "private".equals(chatType);
        
        // 获取 Telegram 用户 ID 和话题信息
        User fromUser = message.getFrom();
        String userId = String.valueOf(fromUser.getId());
        String username = fromUser.getUserName();
        String firstName = fromUser.getFirstName();
        Integer messageThreadId = message.getMessageThreadId();
        
        System.out.println("[GroupNotepadBot] 作品提取: " + cmd + " from " + displayName + " (userId=" + userId + ", 私聊=" + isPrivate + ")");
        
        String prefix = cmd.replaceAll("\\d+$", "");
        String vid = cmd.replaceAll("^[^\\d]+", "");
        
        if (vid.isEmpty()) {
            sendReply(chatId, messageId, "❌ 指令格式错误，正确格式: ww12345 或 zm12345");
            return;
        }
        
        // 1. 先查询作品获取 byString
        String title = null;
        String url = null;
        String cover = null;
        String author = null;
        String byString = null;
        String wpString = null;
        int zhindex = 0;
        boolean found = false;
        
        try {
            switch (prefix) {
                case "ww":
                    WanwuVideoExample wwExample = new WanwuVideoExample();
                    wwExample.createCriteria().andVidEqualTo(Integer.parseInt(vid));
                    List<WanwuVideo> wwList = wanwuVideoMapper.selectByExample(wwExample);
                    if (!wwList.isEmpty()) {
                        found = true;
                        WanwuVideo v = wwList.get(0);
                        title = v.getTitle();
                        cover = v.getCover();
                        // 与 RobotServiceImpl 一致: author = urlkey2
                        author = v.getUrlkey2();
                        byString = v.getTria();
                        wpString = String.valueOf(v.getUptag3());
                        zhindex = 0;
                        // 与 RobotServiceImpl 一致: url 从 getUrl() 获取
                        url = v.getUrl();
                        // tria 字段用于判断是否是有效网盘链接
                        String triaUrl = v.getTria();
                        boolean isFeijipan = triaUrl != null && (triaUrl.contains("feijipan.com") || triaUrl.contains("feijipan.cn"));
                        boolean isQuark = triaUrl != null && (triaUrl.contains("quark.cn") || triaUrl.contains("quark.com"));
                        if (isFeijipan || isQuark) {
                            System.out.println("[GroupNotepadBot] WW类型有网盘链接，直接返回: " + triaUrl);
                        } else {
                            System.out.println("[GroupNotepadBot] WW类型无网盘链接，推队列下载: url=" + url);
                        }
                        // 参考 RobotServiceImpl: 更新 goodtag
                        WanwuVideo wwRecord = new WanwuVideo();
                        wwRecord.setGoodtag(1);
                        wanwuVideoMapper.updateByExampleSelective(wwRecord, wwExample);
                    }
                    break;
                case "zm":
                    ZmqVideoExample zmExample = new ZmqVideoExample();
                    zmExample.createCriteria().andVidEqualTo(Integer.parseInt(vid));
                    List<ZmqVideo> zmList = zmqVideoMapper.selectByExample(zmExample);
                    if (!zmList.isEmpty()) {
                        found = true;
                        ZmqVideo v = zmList.get(0);
                        title = v.getTitle();
                        cover = v.getCover();
                        author = v.getAuthor() != null ? String.valueOf(v.getAuthor()) : null;
                        byString = v.getTria();
                        wpString = String.valueOf(v.getUptag3());
                        zhindex = 0;
                        // 与 RobotServiceImpl 一致: url 从 getUrl() 获取
                        url = v.getUrl();
                        // tria 字段用于判断是否是有效网盘链接（直接返回或推队列）
                        String triaUrl = v.getTria();
                        boolean isFeijipan = triaUrl != null && (triaUrl.contains("feijipan.com") || triaUrl.contains("feijipan.cn"));
                        boolean isQuark = triaUrl != null && (triaUrl.contains("quark.cn") || triaUrl.contains("quark.com"));
                        if (isFeijipan || isQuark) {
                            // 有小飞机/夸克网盘链接，直接返回（不推队列）
                            System.out.println("[GroupNotepadBot] ZM类型有网盘链接，直接返回: " + triaUrl);
                        } else {
                            // 不是有效网盘，推队列下载（url 仍使用 getUrl()）
                            System.out.println("[GroupNotepadBot] ZM类型无网盘链接，推队列下载: url=" + url);
                        }
                        // 参考 RobotServiceImpl: 更新 goodtag
                        ZmqVideo zmRecord = new ZmqVideo();
                        zmRecord.setGoodtag(1);
                        zmqVideoMapper.updateByExampleSelective(zmRecord, zmExample);
                    }
                    break;
                case "tl":
                    Taolu3VideoExample tlExample = new Taolu3VideoExample();
                    tlExample.createCriteria().andVidEqualTo(Integer.parseInt(vid));
                    List<Taolu3Video> tlList = taolu3VideoMapper.selectByExample(tlExample);
                    if (!tlList.isEmpty()) {
                        found = true;
                        Taolu3Video v = tlList.get(0);
                        title = v.getTitle();
                        cover = v.getCover();
                        // 与 RobotServiceImpl 一致: author 使用 getUrlkey2()
                        author = v.getUrlkey2();
                        byString = v.getTria();
                        wpString = String.valueOf(v.getUptag3());
                        zhindex = 0;
                        // 与 RobotServiceImpl 一致: url 从 getUrl() 获取
                        url = v.getUrl();
                        // tria 字段用于判断是否是有效网盘链接
                        String triaUrl = v.getTria();
                        boolean isFeijipan = triaUrl != null && (triaUrl.contains("feijipan.com") || triaUrl.contains("feijipan.cn"));
                        boolean isQuark = triaUrl != null && (triaUrl.contains("quark.cn") || triaUrl.contains("quark.com"));
                        if (isFeijipan || isQuark) {
                            System.out.println("[GroupNotepadBot] TL类型有网盘链接，直接返回: " + triaUrl);
                        } else {
                            System.out.println("[GroupNotepadBot] TL类型无网盘链接，推队列下载: url=" + url);
                        }
                        // 参考 RobotServiceImpl: 更新 goodtag
                        Taolu3Video tlRecord = new Taolu3Video();
                        tlRecord.setGoodtag(1);
                        taolu3VideoMapper.updateByExampleSelective(tlRecord, tlExample);
                    }
                    break;
                case "bc":
                    // bc: 使用 selectByPrimaryKey 查询 Waiwang2Video
                    Waiwang2Video bcVideo = waiwang2VideoMapper.selectByPrimaryKey(Integer.parseInt(vid));
                    System.out.println("[GroupNotepadBot] 查询 bc" + vid + ", 结果=" + (bcVideo != null ? "找到" : "未找到"));
                    if (bcVideo != null) {
                        found = true;
                        // 参考 RobotServiceImpl: title + "_" + vid
                        title = bcVideo.getTitle() + "_" + bcVideo.getVid();
                        // url 字段是加密/密文，使用 pantag 作为网盘链接（用于队列推送）
                        url = bcVideo.getPantag();
                        cover = bcVideo.getCover();
                        // 参考 RobotServiceImpl: author = nickname
                        author = bcVideo.getNickname();
                        byString = bcVideo.getPantag();
                        wpString = bcVideo.getChannel();
                        zhindex = 0;
                        // 标记为 bc 类型，不直接展示网盘链接给用户
                        System.out.println("[GroupNotepadBot] 找到作品: " + title + ", url=" + url + " (bc类型，不展示链接，推队列)");
                        // 参考 RobotServiceImpl: 更新 goodtag
                        bcVideo.setGoodtag(1);
                        waiwang2VideoMapper.updateByPrimaryKeySelective(bcVideo);
                    } else {
                        System.out.println("[GroupNotepadBot] 未找到作品: bc" + vid);
                    }
                    break;
                    
                case "tg":
                    // tg: 使用 selectByPrimaryKey 查询 WaiwangVideo
                    WaiwangVideo tgVideo = waiwangVideoMapper.selectByPrimaryKey(Integer.parseInt(vid));
                    System.out.println("[GroupNotepadBot] 查询 " + prefix + vid + ", 结果=" + (tgVideo != null ? "找到" : "未找到"));
                    if (tgVideo != null) {
                        // 排除 friendindex = 1 的记录
                        if (tgVideo.getFriendindex() == 1) {
                            sendReply(chatId, messageId, "该作品暂不可提取: " + cmd);
                            return;
                        }
                        found = true;
                        title = tgVideo.getTitle();
                        // WaiwangVideo 的 url 是电报资源链接，用于队列推送下载
                        url = tgVideo.getUrl();  // 用于队列下载，不直接返回给用户
                        // WaiwangVideo 没有 cover 字段
                        cover = "";
                        // 与 RobotServiceImpl 保持一致: author 为空字符串, byString 使用 getAuthor()
                        author = "";  // RobotServiceImpl 中 tg 类型的 author 是空字符串
                        byString = tgVideo.getAuthor();
                        wpString = "";
                        zhindex = tgVideo.getFriendindex();
                        // 参考 RobotServiceImpl: 更新 goodtag
                        tgVideo.setGoodtag(1);
                        waiwangVideoMapper.updateByPrimaryKeySelective(tgVideo);
                        System.out.println("[GroupNotepadBot] 找到作品: " + title + ", url=" + url + ", byString=" + byString + " (电报资源)");
                    } else {
                        System.out.println("[GroupNotepadBot] 未找到作品: " + prefix + vid);
                    }
                    break;
                    
                case "ch":
                    // ch: 使用 isearch 查询，与 RobotServiceImpl 保持一致
                    try {
                        Isearch search = new Isearch();
                        long vids = Long.parseLong(vid);
                        search.in("ID", new long[] { vids });
                        Hits hits = search.queryHits();
                        
                        System.out.println("[GroupNotepadBot] 查询 " + prefix + vid + ", isearch结果=" + (hits != null && hits.size() > 0 ? "找到" : "未找到"));
                        
                        if (hits != null && hits.size() > 0) {
                            found = true;
                            url = hits.get(0).getArticle().getString("UR");
                            title = hits.get(0).getArticle().getString("TX");
                            byString = hits.get(0).getArticle().getString("DL");
                            wpString = "";
                            cover = "";
                            author = "";
                            String channel = hits.get(0).getArticle().getString("CH");
                            // 根据频道设置 zhindex
                            if ("kaikai".equals(channel)) {
                                zhindex = 2;
                            } else if ("zuoyou".equals(channel)) {
                                zhindex = 1;
                            } else {
                                zhindex = 0;
                            }
                            System.out.println("[GroupNotepadBot] 找到作品: " + title + ", url=" + url + ", channel=" + channel);
                        } else {
                            System.out.println("[GroupNotepadBot] 未找到作品: " + prefix + vid);
                        }
                    } catch (Exception e) {
                        System.err.println("[GroupNotepadBot] isearch 查询失败: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                    
                case "zb":
                    // zb: 直播录制，调用 RobotService 处理
                    try {
                        String zbIdentifier = (username != null && !username.isEmpty()) ? "@" + username : userId;
                        String zbResult = robotService.handleZhiboCommand(Integer.parseInt(vid), zbIdentifier);
                        sendReply(chatId, messageId, zbResult);
                    } catch (Exception e) {
                        System.err.println("[GroupNotepadBot] zb指令处理失败: " + e.getMessage());
                        e.printStackTrace();
                        sendReply(chatId, messageId, "❌ 直播录制请求失败: " + e.getMessage());
                    }
                    return;
            }
        } catch (Exception e) {
            System.err.println("[GroupNotepadBot] 查询作品失败: " + e.getMessage());
        }
        
        if (title == null) {
            sendReply(chatId, messageId, "❌ 未找到作品: " + cmd);
            return;
        }
        
        // 2. 检查余额（查询到作品后检查）- 仅私聊检测，群聊免费
        TbWallet wallet = getOrCreateWallet(userId, username, firstName);
        if (isPrivate) {
            // 私聊：检测余额
            if (wallet.getBalance() == null || wallet.getBalance() < DEDUCT_AMOUNT) {
                sendReply(chatId, messageId, "❌ 余额不足\n" +
                    "当前余额: " + (wallet.getBalance() != null ? wallet.getBalance() : 0) + "\n" +
                    "提取作品需要: " + DEDUCT_AMOUNT + "\n\n" +
                    "请联系客服充值");
                return;
            }
        }
        int balanceBefore = wallet.getBalance() != null ? wallet.getBalance() : 0;
        int balanceAfter = balanceBefore;
        if (isPrivate) {
            // 私聊：原子扣费，防止并发 Lost Update
            int deductRows = tbWalletMapper.deductBalance(userId);
            if (deductRows == 0) {
                sendReply(chatId, messageId, "❌ 余额不足\n" +
                    "当前余额: " + balanceBefore + "\n" +
                    "提取作品需要: " + DEDUCT_AMOUNT + "\n\n" +
                    "请联系客服充值");
                return;
            }
            balanceAfter = balanceBefore - DEDUCT_AMOUNT;
            wallet.setBalance(balanceAfter);
            System.out.println("[GroupNotepadBot] 扣费成功: user=" + userId + ", topic=" + TG_TOPIC_TYPE + 
                ", 扣费前=" + balanceBefore + ", 扣费后=" + balanceAfter + ", 扣费=" + DEDUCT_AMOUNT);
        } else {
            // 群聊：不扣费
            System.out.println("[GroupNotepadBot] 群聊免费提取: user=" + userId + ", 余额=" + balanceBefore);
        }
        
        System.out.println("[GroupNotepadBot] 准备构建回复, url=" + url + ", byString=" + byString + ", zhindex=" + zhindex + ", prefix=" + prefix);
        
        StringBuilder reply = new StringBuilder();
        reply.append("📹 作品提取成功\n");
        reply.append("==============================\n");
        reply.append("来源: ").append(prefix.toUpperCase()).append("\n");
        reply.append("ID: ").append(vid).append("\n");
        reply.append("标题: ").append(title).append("\n");
        if (author != null && !author.isEmpty()) {
            reply.append("作者: ").append(author).append("\n");
        }
        reply.append("------------------------------\n");
        
        // 网盘判断：feijipan/quark/pikpak 展示分享链接；feijipan/quark 不推队列，pikpak/其他推队列
        boolean isFeijipan = byString != null && (byString.contains("feijipan.com") || byString.contains("feijipan.cn"));
        boolean isQuark = byString != null && (byString.contains("quark.cn") || byString.contains("quark.com"));
        boolean isPikpak = byString != null && byString.contains("pikpak");
        boolean isValidPan = isFeijipan || isQuark || isPikpak;
        
        System.out.println("[GroupNotepadBot] 网盘判断: isFeijipan=" + isFeijipan + ", isQuark=" + isQuark + ", isPikpak=" + isPikpak + ", byString=" + byString);
        
        // 管理员判断
        boolean isAdmin = "kaikak09818".equals(username) || "linyuan56".equals(username);
        
        // 展示网盘分享链接（feijipan/quark/pikpak）
        if (isValidPan) {
            System.out.println("[GroupNotepadBot] 有网盘分享链接，展示给用户: " + byString);
            reply.append("☁️ 网盘链接:\n").append(byString).append("\n");
        }
        
        // feijipan/quark 直接返回，不推队列；pikpak/其他推队列
        if (isFeijipan || isQuark) {
            System.out.println("[GroupNotepadBot] 有小飞机/夸克网盘链接，直接返回不推队列");
        } else {
            // pikpak/其他推队列
            System.out.println("[GroupNotepadBot] 非小飞机/夸克网盘链接(byString=" + byString + ")，推队列");
            
            // 每日提取次数限制：topicok=4 每日限10次
            if (!checkDailyExtractLimit(userId, 4)) {
                reply.append("⚠️ 今日提取次数已达上限（5次），请明日再试。\n");
            } else {
                reply.append("⏳ 视频将通过机器人自动发送\n");
                // 禁止展示 URL、网盘链接、电报链接（推队列即可）
                // 处理可能包含逗号的字段，避免 CSV 格式混乱
                String safeTitle = title != null ? title.replace(",", " ") : "";
                String safeDisplayName = displayName != null ? displayName.replace(",", " ") : "";
                String safeUrl = url != null ? url.replace(",", " ") : "";
                String safeCover = cover != null ? cover.replace(",", " ") : "";
                String safeByString = byString != null ? byString.replace(",", " ") : "";
                String safeWpString = wpString != null ? wpString.replace(",", " ") : "";
                String safeAuthor = author != null ? author.replace(",", " ") : "";
                String info = safeDisplayName + "," + safeUrl + "," + safeTitle + "," + cmd + "," + chatId + ","
                        + safeCover + "," + safeByString + "," + safeWpString + "," + safeAuthor + "," + zhindex + ",4," + userId + "," + messageId + "," + (messageThreadId != null ? messageThreadId : "") + ",1" + "," + (wallet.getFeijiUsername() != null ? wallet.getFeijiUsername() : "") + "," + (wallet.getFeijiPassword() != null ? wallet.getFeijiPassword() : "");
                System.out.println("[GroupNotepadBot] 队列信息: " + info);
                boolean isWckbot = safeUrl.contains("wckbot");
                if (isWckbot) {
                    jedisClient.rpush("wckbot_extract", info);
                    System.out.println("[GroupNotepadBot] 已推送到 wckbot_extract 队列(vipok=4): " + cmd);
                } else {
                    jedisClient.rpush("videos", info);
                    System.out.println("[GroupNotepadBot] 已推送到队列(vipok=4): " + cmd);
                }
            }
        }
        
        reply.append("------------------------------\n");
        if (isPrivate) {
            // 私聊：显示余额变化
            reply.append("💰 余额: ").append(balanceBefore).append(" → ").append(balanceAfter)
                  .append(" (扣费 ").append(DEDUCT_AMOUNT).append(")\n");
        } else {
            // 群聊：显示免费
            reply.append("🎉 群聊免费提取\n");
        }
        if (wallet != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTimeStr = wallet.getVidEndTime() != null ? sdf.format(wallet.getVidEndTime()) : "未设置";
            reply.append("------------------------------\n");
            reply.append("【会员截止日期：").append(endTimeStr).append("】\n");
        }
        reply.append("==============================");
        
        System.out.println("[GroupNotepadBot] 准备发送回复, chatId=" + chatId + ", reply长度=" + reply.length());
        sendReply(chatId, messageId, reply.toString());
        System.out.println("[GroupNotepadBot] 回复发送完成");
    }
    
    private void handleSearchAndNotepad(Long chatId, Integer replyToMessageId, String keyword, String displayName) {
        System.out.println("[GroupNotepadBot] 搜索: " + keyword);
        
        Integer loadingMsgId = sendReply(chatId, replyToMessageId, "⏳ 正在生成作品清单，请稍候...");
        
        try {
            List<SearchResult> results = new ArrayList<>();
            
            // 0. isearch 搜索引擎（全文检索）
            System.out.println("[GroupNotepadBot] 检查 isearch 可用性: " + SearchFactory.isIsearchAvailable());
            if (SearchFactory.isIsearchAvailable()) {
                try {
                    System.out.println("[GroupNotepadBot] 开始 isearch 搜索: " + keyword);
                    Isearch isearch = new Isearch();
                    // DM字段过滤已移除，搜索所有来源
                    isearch.andText("TX", keyword);
                    // 使用 notIndexedText 过滤 CH 字段
                    isearch.andNotIndexedText("CH", "kaikai");
                    isearch.greatThan("RQ", System.currentTimeMillis() - 10L * 365 * 24 * 60 * 60 * 1000);
                    isearch.setMaxResults(10000);
                    
                    Hits hits = isearch.queryHits();
                    System.out.println("[GroupNotepadBot] isearch 搜索结果: " + (hits != null ? hits.size() : 0) + " 条");
                    
                    if (hits != null) {
                        for (int i = 0; i < hits.size() && i < 10000; i++) {
                            String title = hits.get(i).getArticle().getString("TX");
                            if (title != null && !title.isEmpty()) {
                                // isearch 结果标记为 CH 类型（频道），id 使用 hit.getId() 与 RobotServiceImpl 保持一致
                                // 使用 long 类型避免 ID 截断
                                long id = hits.get(i).getId();
                                String duration = hits.get(i).getArticle().getString("CC");
                                long rq = hits.get(i).getArticle().getLong("RQ");
                                String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(rq));
                                results.add(new SearchResult("CH", id, title, "", duration, time));
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("[GroupNotepadBot] isearch 搜索失败: " + e.getMessage());
                }
            } else {
                System.out.println("[GroupNotepadBot] isearch 不可用，跳过全文检索");
            }
            
            // 1. 搜索网页 (ZMQ)
            try {
                ZmqVideoExample zmExample = new ZmqVideoExample();
                ZmqVideoExample.Criteria criteria = zmExample.createCriteria();
                if (keyword != null && !keyword.trim().isEmpty()) {
					String[] parts = keyword.trim().split("\\p{javaWhitespace}+");
					for (String part : parts) {
						if (part.length() > 0) {
							criteria.andTitleLike("%" + part + "%");
						}
					}
				}
                criteria.andDurationIsNotNull();
                zmExample.setOrderByClause("addtime desc");
                PageHelper.startPage(1, 10000);
                List<ZmqVideo> zmList = zmqVideoMapper.selectByExample(zmExample);
                
                for (ZmqVideo v : zmList) {
                    results.add(new SearchResult("ZM", v.getVid(), v.getTitle(), v.getCover(), v.getDuration(), v.getDt()));
                }
            } catch (Exception e) {
                System.err.println("[GroupNotepadBot] ZMQ搜索失败: " + e.getMessage());
            }
            
            // 2. 搜索玩物
            try {
                WanwuVideoExample wwExample = new WanwuVideoExample();
                WanwuVideoExample.Criteria criteria = wwExample.createCriteria();
                if (keyword != null && !keyword.trim().isEmpty()) {
					String[] parts = keyword.trim().split("\\p{javaWhitespace}+");
					for (String part : parts) {
						if (part.length() > 0) {
							criteria.andTitleLike("%" + part + "%");
						}
					}
				}
                wwExample.setOrderByClause("addtime desc");
                PageHelper.startPage(1, 10000);
                List<WanwuVideo> wwList = wanwuVideoMapper.selectByExample(wwExample);
                
                for (WanwuVideo v : wwList) {
                    results.add(new SearchResult("WW", v.getVid(), v.getTitle(), v.getCover(), v.getDuration(), v.getAddtime()));
                }
            } catch (Exception e) {
                System.err.println("[GroupNotepadBot] WW搜索失败: " + e.getMessage());
            }
            
            // 3. 搜索套路
            try {
                Taolu3VideoExample tlExample = new Taolu3VideoExample();
                Taolu3VideoExample.Criteria criteria = tlExample.createCriteria();
                if (keyword != null && !keyword.trim().isEmpty()) {
					String[] parts = keyword.trim().split("\\p{javaWhitespace}+");
					for (String part : parts) {
						if (part.length() > 0) {
							criteria.andTitleLike("%" + part + "%");
						}
					}
				}
                tlExample.setOrderByClause("dt desc");
                PageHelper.startPage(1, 10000);
                List<Taolu3Video> tlList = taolu3VideoMapper.selectByExample(tlExample);
                
                for (Taolu3Video v : tlList) {
                    results.add(new SearchResult("TL", v.getVid(), v.getTitle(), v.getCover(), null, v.getDt()));
                }
            } catch (Exception e) {
                System.err.println("[GroupNotepadBot] TL搜索失败: " + e.getMessage());
            }
            
            // 4. 搜索最新 (BC) - waiwang2 表，不区分 type，参考 RobotServiceImpl.searchWaiwang2
            try {
                Waiwang2VideoExample bcExample = new Waiwang2VideoExample();
                Waiwang2VideoExample.Criteria criteria = bcExample.createCriteria();
                if (keyword != null && !keyword.trim().isEmpty()) {
					String[] parts = keyword.trim().split("\\p{javaWhitespace}+");
					for (String part : parts) {
						if (part.length() > 0) {
							criteria.andTitleLike("%" + part + "%");
						}
					}
				}
                criteria.andDurationIsNotNull();
                // 排除 pantag 不含 http 的结果
                criteria.andPantagLike("%http%");
                bcExample.setOrderByClause("dt desc");
                PageHelper.startPage(1, 10000);
                List<Waiwang2Video> bcList = waiwang2VideoMapper.selectByExample(bcExample);
                
                // 额外过滤：确保 pantag 不为 null 且不为空字符串，且包含有效的网盘链接
                int filteredCount = 0;
                for (Waiwang2Video v : bcList) {
                    String pantag = v.getPantag();
                    if (pantag != null && !pantag.trim().isEmpty() && 
                        (pantag.contains("http://") || pantag.contains("https://"))) {
                        results.add(new SearchResult("BC", v.getId(), v.getTitle(), v.getCover(), v.getDuration(), v.getDt()));
                    } else {
                        filteredCount++;
                        System.out.println("[GroupNotepadBot] 过滤无效pantag记录: BC" + v.getId() + ", pantag=" + pantag);
                    }
                }
                System.out.println("[GroupNotepadBot] BC搜索: " + keyword + ", 原始结果数=" + bcList.size() + ", 过滤后=" + (bcList.size() - filteredCount));
            } catch (Exception e) {
                System.err.println("[GroupNotepadBot] BC搜索失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            // 5. 搜索外网 (TG) - waiwang 表，参考 RobotServiceImpl.searchWaiwang
            try {
                WaiwangVideoExample tgExample = new WaiwangVideoExample();
                WaiwangVideoExample.Criteria criteria = tgExample.createCriteria();
                if (keyword != null && !keyword.trim().isEmpty()) {
					String[] parts = keyword.trim().split("\\p{javaWhitespace}+");
					for (String part : parts) {
						if (part.length() > 0) {
							criteria.andTitleLike("%" + part + "%");
						}
					}
				}
                criteria.andTitleNotEqualTo("");
                criteria.andDurationGreaterThan("600");
                // 排除 friendindex = 1 的记录
                criteria.andFriendindexNotEqualTo(1);
                tgExample.setOrderByClause("dt desc");
                PageHelper.startPage(1, 10000);
                List<WaiwangVideo> tgList = waiwangVideoMapper.selectByExample(tgExample);
                System.out.println("[GroupNotepadBot] TG搜索: " + keyword + ", 结果数=" + tgList.size());
                
                for (WaiwangVideo v : tgList) {
                    // WaiwangVideo 没有 cover 字段
                    results.add(new SearchResult("TG", v.getId(), v.getTitle(), "", secondsToHMS(v.getDuration()), v.getDt()));
                }
            } catch (Exception e) {
                System.err.println("[GroupNotepadBot] TG搜索失败: " + e.getMessage());
                e.printStackTrace();
            }
            
            if (results.isEmpty()) {
                deleteMessage(chatId, loadingMsgId);
                sendReply(chatId, replyToMessageId, "❌ 未找到相关作品: " + keyword);
                return;
            }
            
            File txtFile = generateNotepadFile(keyword, displayName, results);
            deleteMessage(chatId, loadingMsgId);
            sendTxtFile(chatId, replyToMessageId, txtFile, keyword, results.size());
            
        } catch (Exception e) {
            System.err.println("[GroupNotepadBot] 搜索处理失败: " + e.getMessage());
            e.printStackTrace();
            deleteMessage(chatId, loadingMsgId);
            sendReply(chatId, replyToMessageId, "❌ 搜索失败: " + e.getMessage());
        }
    }
    
    private File generateNotepadFile(String keyword, String displayName, List<SearchResult> results) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        String safeKeyword = keyword.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "_");
        String filename = "作品清单_" + safeKeyword + "_" + System.currentTimeMillis() + ".txt";
        File file = new File("/tmp/" + filename);
        
        List<SearchResult> zmList = new ArrayList<>();
        List<SearchResult> bcList = new ArrayList<>();
        List<SearchResult> wwList = new ArrayList<>();
        List<SearchResult> tlList = new ArrayList<>();
        List<SearchResult> tgList = new ArrayList<>();
        List<SearchResult> chList = new ArrayList<>();
        
        for (SearchResult r : results) {
            switch (r.type) {
                case "ZM": zmList.add(r); break;
                case "BC": bcList.add(r); break;
                case "WW": wwList.add(r); break;
                case "TL": tlList.add(r); break;
                case "TG": tgList.add(r); break;
                case "CH": chList.add(r); break;
            }
        }
        
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(osw)) {
            
            // 写入 UTF-8 BOM
            fos.write(0xEF);
            fos.write(0xBB);
            fos.write(0xBF);
            
            writer.write("=======================================\r\n");
            writer.write("🔐 作品搜索清单\r\n");
            writer.write("=======================================\r\n");
            writer.write("搜索时间: " + timestamp + "\r\n");
            writer.write("关键词: " + keyword + "\r\n");
            writer.write("搜索人: " + displayName + "\r\n");
            writer.write("---------------------------------------\r\n");
            writer.write("网页: " + zmList.size() + " 条\r\n");
            writer.write("最新: " + bcList.size() + " 条\r\n");
            writer.write("玩物: " + wwList.size() + " 条\r\n");
            writer.write("套路: " + tlList.size() + " 条\r\n");
            writer.write("外网: " + tgList.size() + " 条\r\n");
            writer.write("全文搜索: " + chList.size() + " 条\r\n");
            writer.write("总计: " + results.size() + " 条\r\n");
            writer.write("=======================================\r\n\r\n");
            
            // 按时间倒序混合排列
            results.sort((a, b) -> {
                if (a.time == null && b.time == null) return 0;
                if (a.time == null) return 1;
                if (b.time == null) return -1;
                return b.time.compareTo(a.time);
            });
            writeTypeSectionTxt(writer, "【搜索结果】", results);
            
            writer.write("\r\n=======================================\r\n");
            writer.write("发送指令（如 ww12345, zm12345）即可提取作品\r\n");
            writer.write("=======================================\r\n");
            
        } catch (IOException e) {
            System.err.println("[GroupNotepadBot] 生成记事本文件失败: " + e.getMessage());
        }
        
        return file;
    }
    
    private void writeTypeSectionTxt(BufferedWriter writer, String typeName, 
                                      List<SearchResult> list) throws IOException {
        if (list.isEmpty()) return;
        
        writer.write(typeName + " (" + list.size() + " 条)\r\n");
        writer.write("---------------------------------------\r\n");
        
        int num = 1;
        for (SearchResult r : list) {
            String durationStr = (r.duration != null && !r.duration.isEmpty()) ? " 时长:" + r.duration : "";
            String timeStr = (r.time != null && !r.time.isEmpty()) ? " 时间:" + r.time : "";
            String extra = (durationStr + timeStr).trim();
            String cleanTitle = r.title.replace("\n", "").replace("\r", "");
            writer.write(num + ". " + cleanTitle + (extra.isEmpty() ? "" : " " + extra) + "\r\n");
            writer.write("   指令: " + r.getCommand() + "\r\n\r\n");
            num++;
        }
        
        writer.write("\r\n");
    }
    
    private void writeTypeSection(BufferedWriter writer, String type, String typeName, 
                                   List<SearchResult> list, String color) throws IOException {
        // 此方法已废弃，保留兼容性
        writeTypeSectionTxt(writer, typeName, list);
    }
    
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }
    
    private void sendTxtFile(Long chatId, Integer replyToMessageId, File file, String keyword, int count) {
        SendDocument document = new SendDocument();
        document.setChatId(chatId.toString());
        document.setReplyToMessageId(replyToMessageId);
        document.setDocument(new InputFile(file));
        document.setCaption("✅ 作品清单生成完成\n" +
                "关键词: " + keyword + "\n" +
                "共 " + count + " 条结果\n" +
                "请打开记事本查看提取指令");
        
        try {
            execute(document);
            System.out.println("[GroupNotepadBot] 记事本文件发送成功");
        } catch (TelegramApiException e) {
            System.err.println("[GroupNotepadBot] 发送文件失败: " + e.getMessage());
            sendReply(chatId, replyToMessageId, "❌ 文件发送失败: " + e.getMessage());
        }
        
        file.delete();
    }
    
    private Integer sendReply(Long chatId, Integer replyToMessageId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setReplyToMessageId(replyToMessageId);
        message.setText(text);
        
        try {
            Message sent = execute(message);
            return sent.getMessageId();
        } catch (TelegramApiException e) {
            System.err.println("[GroupNotepadBot] 发送消息失败: " + e.getMessage());
            return null;
        }
    }
    
    private void deleteMessage(Long chatId, Integer messageId) {
        if (messageId == null) return;
        
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(chatId.toString());
        delete.setMessageId(messageId);
        
        try {
            execute(delete);
        } catch (TelegramApiException e) {
            // 忽略删除失败
        }
    }
    
    @Override
    public String getBotUsername() {
        return "GroupNotepadBot";
    }
    
    /**
     * 获取或创建用户钱包
     */
    private TbWallet getOrCreateWallet(String userId, String username, String firstName) {
        TbWalletExample example = new TbWalletExample();
        example.createCriteria().andUidEqualTo(userId);
        List<TbWallet> wallets = tbWalletMapper.selectByExample(example);
        
        if (wallets != null && !wallets.isEmpty()) {
            return wallets.get(0);
        }
        
        // 创建新用户
        TbWallet wallet = new TbWallet();
        wallet.setUid(userId);
        wallet.setBalance(DEFAULT_BALANCE);
        wallet.setCreated(new Date());
        wallet.setUpdated(new Date());
        wallet.setVersion(1);
        
        // 新用户昵称格式: Telegram用户名或firstName
        String autoNickname = (username != null && !username.isEmpty()) 
            ? username + "_TG_" + userId 
            : (firstName != null ? firstName : "User") + "_TG_" + userId;
        wallet.setNickname(autoNickname);
        
        // 小飞机账号为空（使用默认值）
        wallet.setFeijiUsername(null);
        wallet.setFeijiPassword(null);
        
        tbWalletMapper.insertSelective(wallet);
        System.out.println("[GroupNotepadBot] 创建新用户钱包: userId=" + userId + ", 默认余额=" + DEFAULT_BALANCE);
        
        return wallet;
    }
    
    private static class SearchResult {
        String type;
        Long id;
        String title;
        String cover;
        String duration;
        String time;
        
        SearchResult(String type, long id, String title, String cover, String duration, String time) {
            this.type = type;
            this.id = id;
            this.title = title;
            this.cover = cover;
            this.duration = duration;
            this.time = time;
        }
        
        // 重载构造函数，兼容 Integer 类型 (ZM/WW/TL/BC/TG 等数据库类型)
        SearchResult(String type, Integer id, String title, String cover, String duration, String time) {
            this.type = type;
            this.id = id != null ? id.longValue() : null;
            this.title = title;
            this.cover = cover;
            this.duration = duration;
            this.time = time;
        }
        
        String getCommand() {
            return type.toLowerCase() + id;
        }
    }

    /**
     * 检查每日提取次数限制
     * @param userId 用户标识
     * @param topicok 1=VIP群, 4=GroupNotepadBot
     * @return true=允许提取, false=已达上限
     */
    private boolean checkDailyExtractLimit(String userId, int topicok) {
        // 已解除每日提取次数限制
        return true;
    }

    /**
     * 秒数 → HH:MM:SS 格式（外网 TG 时长单位是秒）
     */
    private String secondsToHMS(String secondsStr) {
        if (secondsStr == null || secondsStr.isEmpty()) return "";
        try {
            int sec = Integer.parseInt(secondsStr);
            int h = sec / 3600;
            int m = (sec % 3600) / 60;
            int s = sec % 60;
            if (h > 0) return String.format("%d:%02d:%02d", h, m, s);
            return String.format("%d:%02d", m, s);
        } catch (NumberFormatException e) {
            return secondsStr;
        }
    }
}
