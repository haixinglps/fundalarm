package cn.exrick.manager.service.qq;

import cn.exrick.common.jedis.JedisClient;
import cn.exrick.manager.dto.SearchResultDTO;
import cn.exrick.manager.mapper.TbWalletMapper;
import cn.exrick.manager.pojo.Taolu3Video;
import cn.exrick.manager.pojo.TbWallet;
import cn.exrick.manager.pojo.TbWalletExample;
import cn.exrick.manager.pojo.Waiwang2Video;
import cn.exrick.manager.pojo.WaiwangVideo;
import cn.exrick.manager.pojo.WanwuVideo;
import cn.exrick.manager.pojo.ZmqVideo;
import cn.exrick.manager.service.RobotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * QQ Bot 真实数据处理器（私聊专用）
 * 只处理私聊消息，不处理群/频道
 * 
 * 扣费逻辑：提取作品扣 1 点，搜索不扣费
 * 新用户默认赠送 5 点
 */
public class QQBotRealDataProcessor implements QQMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(QQBotRealDataProcessor.class);
    
    private final RobotService robotService;
    private final QQBotReplyService replyService;
    private final int resultLimit;
    private final TbWalletMapper tbWalletMapper;
    private final JedisClient jedisClient;
    
    // 默认每类返回条数
    private static final int DEFAULT_LIMIT = 3;
    // 新用户默认余额
    private static final int DEFAULT_BALANCE = 5;
    // 提取作品扣费金额
    private static final int DEDUCT_AMOUNT = 1;
    // QQ 用户 topic 标识（用于区分 Telegram 的不同话题类型）
    private static final int QQ_TOPIC_TYPE = 3;
    // Redis 队列键名（和 Telegram 保持一致）
    private static final String REDIS_QUEUE_KEY = "videos";
    
    private cn.exrick.manager.service.impl.AsyncEventPublisher publisher;
    private QQBotClient currentClient;  // 当前处理消息的客户端（用于获取appId）
    
    // 消息去重：最近处理的消息ID
    private final java.util.Set<String> recentMessageIds = java.util.concurrent.ConcurrentHashMap.newKeySet();
    
    public QQBotRealDataProcessor(RobotService robotService, TbWalletMapper tbWalletMapper, JedisClient jedisClient) {
        this(robotService, tbWalletMapper, jedisClient, DEFAULT_LIMIT);
    }
    
    public QQBotRealDataProcessor(RobotService robotService, TbWalletMapper tbWalletMapper, JedisClient jedisClient, int resultLimit) {
        this.robotService = robotService;
        this.tbWalletMapper = tbWalletMapper;
        this.jedisClient = jedisClient;
        this.replyService = new QQBotReplyService();
        this.resultLimit = Math.max(1, Math.min(resultLimit, 5));
    }
    
    /**
     * 设置 AsyncEventPublisher（用于搜索后发送记事本）
     */
    public void setPublisher(cn.exrick.manager.service.impl.AsyncEventPublisher publisher) {
        this.publisher = publisher;
        log.info("[QQBot] AsyncEventPublisher 已设置");
    }
    
    /**
     * 设置当前客户端（用于获取appId）
     */
    public void setClient(QQBotClient client) {
        this.currentClient = client;
    }
    
    @Override
    public void handleMessage(QQBotMessage message, QQBotClient client) {
        // 只处理私聊
        if (message.getType() != QQBotMessage.Type.C2C) {
            log.debug("[QQBot] 忽略非私聊消息: {}", message.getType());
            return;
        }
        
        // 消息去重检查
        String msgId = message.getMsgId();
        if (msgId != null && !recentMessageIds.add(msgId)) {
            log.warn("[QQBot] 重复消息，跳过处理: {}", msgId);
            return;
        }
        // 清理旧消息ID（防止内存无限增长）
        if (recentMessageIds.size() > 1000) {
            recentMessageIds.clear();
        }
        
        log.info("[QQBot] 处理私聊消息: {} (机器人: {})", message, client.getAppId());
        
        String content = message.getContent();
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        
        // 创建回复上下文
        QQBotReplyContext context = new QQBotReplyContext(message, client);
        
        // 异步处理
        CompletableFuture.runAsync(() -> {
            try {
                processPrivateMessage(message, context);
            } catch (Exception e) {
                log.error("[QQBot] 处理消息异常", e);
                context.sendResults("❌ 处理失败，请稍后重试");
            }
        });
    }
    
    /**
     * 处理私聊消息
     * 注意：搜索功能必须使用密文，明文搜索已关闭（防止腾讯监测）
     */
    private void processPrivateMessage(QQBotMessage message, QQBotReplyContext context) {
        String content = message.getContent().trim();
        
        // 1. 提取作品指令
        if (message.hasCommandPrefix("ww", "tl", "bc", "zm", "tg", "ch")) {
            handleExtractWork(message, context);
            return;
        }
        
        // 2. 帮助
        if (message.hasCommandPrefix("帮助", "help", "?")) {
            context.sendResults(generateHelpMessage());
            return;
        }
        
        // 3. 最新作品
        if (message.hasCommandPrefix("最新", "最新作品")) {
            handleLatestWorks(message, context);
            return;
        }
        
        // 4. 明文搜索（直接处理）
        // 所有非指令输入都作为明文关键词搜索
        handleSearch(message, context);
    }
    
    /**
     * 处理作品提取（带扣费 + 文件下载逻辑，和 Telegram 保持一致）
     */
    private void handleExtractWork(QQBotMessage message, QQBotReplyContext context) {
        String content = message.getContent().trim();
        String cmd = content.substring(0, 2).toLowerCase();
        String vid = content.substring(2).trim();
        String userId = message.getUserId();
        String nickname = message.getNickname();
        
        log.info("[QQBot] 提取作品: cmd={}, vid={}, user={}", cmd, vid, userId);
        
        // 1. 检查余额（扣费前置检查）
        TbWallet wallet = getOrCreateWallet(userId, nickname);
        if (wallet.getBalance() == null || wallet.getBalance() < DEDUCT_AMOUNT) {
            context.sendResults("❌ 余额不足\n" +
                "当前余额: " + (wallet.getBalance() != null ? wallet.getBalance() : 0) + "\n" +
                "提取作品需要: " + DEDUCT_AMOUNT + "\n\n" +
                "请联系客服 QQ: 2167485304 充值");
            return;
        }
        
        // 发送"正在提取"提示
        context.sendProgress("⏳ 正在提取作品...\n" +
            "来源: " + getSourceName(cmd) + "\n" +
            "ID: " + vid + "\n" +
            "当前余额: " + wallet.getBalance());
        
        try {
            // 提取作品信息（和 Telegram 逻辑一致）
            VideoInfo videoInfo = extractVideoInfo(cmd, vid);
            
            if (videoInfo == null || videoInfo.url == null) {
                context.sendResults("❌ 未找到作品或链接为空，ID可能有误: " + vid);
                return;
            }
            
            // 2. 扣费（提取成功才扣费）
            int balanceBefore = wallet.getBalance();
            wallet.setBalance(balanceBefore - DEDUCT_AMOUNT);
            wallet.setNickname(nickname + "[QQ:" + QQ_TOPIC_TYPE + "]");
            tbWalletMapper.updateByPrimaryKeySelective(wallet);
            
            log.info("[QQBot] 扣费成功: user={}, topic={}, 扣费前={}, 扣费后={}, 扣费={}", 
                userId, QQ_TOPIC_TYPE, balanceBefore, wallet.getBalance(), DEDUCT_AMOUNT);
            
            // 3. 判断是否需要下载文件（和 Telegram 一致）
            boolean needDownload = isNeedDownload(videoInfo.byString, videoInfo.url, cmd);
            
            if (needDownload) {
                // 需要下载，推送到 Redis 队列
                String info = buildRedisMessage(userId, videoInfo, content, nickname);
                
                // 事务提交后推送
                if (TransactionSynchronizationManager.isSynchronizationActive()) {
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            jedisClient.rpush(REDIS_QUEUE_KEY, info);
                            log.info("[QQBot] 推送到下载队列: user={}, vid={}", userId, vid);
                        }
                    });
                } else {
                    // 无事务，直接推送
                    jedisClient.rpush(REDIS_QUEUE_KEY, info);
                }
                
                // 4. 发送简单提示（不返回作品信息）
                context.sendResults("⏳ 作品正在准备中，稍后发送...\n" +
                    "（记事本文件，请注意查收）");
            } else {
                // 不需要下载，直接发送简单提示
                context.sendResults("⏳ 作品正在准备中，稍后发送...\n" +
                    "（记事本文件，请注意查收）");
                
                // 生成文本内容并发送（直接发送明文记事本）
                String result = formatVideoExtract((Object)videoInfo, getSourceName(cmd), cmd, vid, 
                    balanceBefore, wallet.getBalance());
                
                // 创建文本文件并发送
                try {
                    File txtFile = createEncryptedTextFile(userId, vid, result);
                    if (txtFile != null && txtFile.exists()) {
                        // 发送文件
                        sendFileToQQ(context, txtFile, "📄 作品清单");
                        // 删除临时文件
                        txtFile.delete();
                    } else {
                        // 文件创建失败，发送错误信息
                        context.sendResults("❌ 创建文件失败，请稍后重试或联系客服");
                    }
                } catch (Exception e) {
                    log.error("[QQBot] 发送文件失败", e);
                    context.sendResults("❌ 发送失败: " + e.getMessage() + "\n请联系客服 QQ: 2167485304");
                }
            }
            
        } catch (Exception e) {
            log.error("[QQBot] 提取作品失败", e);
            context.sendResults("❌ 提取失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理搜索
     * 支持明文和 AES-256-ECB 加密的关键词
     * 搜索结果通过 AsyncEventPublisher 生成记事本并发送文件
     */
    private void handleSearch(QQBotMessage message, QQBotReplyContext context) {
        String rawContent = message.getContent();
        String keyword;
        boolean isAesEncrypted = false;
        
        // 检查是否是 AES 加密的关键词
        if (isAesEncryptedKeyword(rawContent)) {
            log.info("[QQBot] 检测到 AES 加密搜索请求");
            keyword = decryptAesKeyword(rawContent);
            isAesEncrypted = true;
            
            if (keyword == null) {
                // 解密失败，作为明文处理
                keyword = extractKeyword(message);
                isAesEncrypted = false;
                log.info("[QQBot] AES 解密失败，作为明文处理: {}", rawContent.substring(0, Math.min(20, rawContent.length())));
            } else {
                log.info("[QQBot] AES 解密成功，开始搜索");
            }
        } else {
            keyword = extractKeyword(message);
        }
        
        String userId = message.getUserId();
        log.info("[QQBot] 搜索: keyword={}, isAesEncrypted={}", 
            isAesEncrypted ? "[已解密]" : keyword, isAesEncrypted);
        
        // 1. 发送"搜索中"提示
        String searchHint = isAesEncrypted ? 
            "⏳ 正在搜索...\n请稍候..." : 
            "⏳ 正在搜索: " + keyword + "\n请稍候...";
        context.sendProgress(searchHint);
        
        try {
            // 2. 查询所有库（返回结果）
            long startTime = System.currentTimeMillis();
            SearchResultDTO result = robotService.searchAll(keyword, 1, 100);
            long costTime = System.currentTimeMillis() - startTime;
            
            log.info("[QQBot] 搜索完成: 耗时={}ms, 总结果={}", costTime, result.getTotalCount());
            
            // 3. 使用 AsyncEventPublisher 生成记事本并推送到队列
            if (publisher != null) {
                // 获取当前机器人的 appId 和 clientSecret
                String appId = (currentClient != null) ? currentClient.getAppId() : null;
                String clientSecret = (currentClient != null) ? currentClient.getClientSecret() : null;
                publisher.publishQQBotSearchAsync(userId, keyword, result, appId, clientSecret);
                log.info("[QQBot] 搜索任务已提交到 publisher: user={}, keyword={}, appId={}", userId, keyword, appId);
                
                // 只提示用户记事本随后送达，不显示任何结果
                context.sendResults("🔍 搜索请求已收到\n" +
                    "==============================\n" +
                    "📋 作品清单正在生成，随后送达...\n" +
                    "（记事本文件，请注意查收）\n\n" +
                    "关键词: " + keyword);
            } else {
                // publisher 未注入，降级为直接发送文本结果
                log.warn("[QQBot] publisher 未注入，降级发送文本结果");
                String formattedResult = formatSearchResult(result, keyword, isAesEncrypted);
                context.sendResults(formattedResult);
            }
            
        } catch (Exception e) {
            log.error("[QQBot] 搜索失败", e);
            context.sendResults("❌ 搜索失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理最新作品
     */
    private void handleLatestWorks(QQBotMessage message, QQBotReplyContext context) {
        log.info("[QQBot] 获取最新作品");
        
        // 发送"加载中"
        context.sendProgress("⏳ 正在加载最新作品...");
        
        try {
            List<Waiwang2Video> latestWorks = robotService.getLatestWorks(1, resultLimit);
            
            StringBuilder sb = new StringBuilder();
            sb.append("📅 最新作品列表\n");
            sb.append("==============================\n\n");
            
            for (int i = 0; i < latestWorks.size(); i++) {
                Waiwang2Video video = latestWorks.get(i);
                sb.append(i + 1).append(". ")
                  .append(getTitle(video.getTitle())).append("\n")
                  .append("   ID: bc").append(video.getId())
                  .append(" | 作者: ").append(video.getNickname())
                  .append(" | 时长: ").append(video.getDuration()).append("分\n");
                // 添加封面 URL
                if (video.getCover() != null && !video.getCover().isEmpty()) {
                    sb.append("   封面: ").append(video.getCover()).append("\n");
                }
                sb.append("\n");
            }
            
            sb.append("==============================\n");
            sb.append("使用 bc + ID 提取作品\n");
            sb.append("点击封面链接可查看预览图");
            
            context.sendResults(sb.toString());
            
        } catch (Exception e) {
            log.error("[QQBot] 获取最新作品失败", e);
            context.sendResults("❌ 获取失败: " + e.getMessage());
        }
    }
    
    /**
     * 格式化搜索结果（带封面 URL）
     */
    /**
     * 格式化搜索结果（带 AES 加密标记）
     */
    private String formatSearchResult(SearchResultDTO result, String keyword, boolean isAesEncrypted) {
        StringBuilder sb = new StringBuilder();
        if (isAesEncrypted) {
            sb.append("🔐 加密搜索\n");
        } else {
            sb.append("🔍 搜索: ").append(keyword).append("\n");
        }
        sb.append("==============================\n\n");
        
        // 网页搜索 (zmq)
        if (result.getZmqTotal() > 0) {
            sb.append("【网页搜索】").append(result.getZmqTotal()).append("条\n");
            List<ZmqVideo> list = result.getZmqVideos();
            for (int i = 0; i < list.size(); i++) {
                ZmqVideo v = list.get(i);
                sb.append(i + 1).append(". ")
                  .append(v.getTitle()).append("\n")
                  .append("   指令: zm").append(v.getVid())
                  .append(" | 时长: ").append(v.getDuration()).append("\n");
                if (v.getCover() != null && !v.getCover().isEmpty()) {
                    sb.append("   封面: ").append(v.getCover()).append("\n");
                }
            }
            if (result.getZmqTotal() > resultLimit) {
                sb.append("   ... 还有 ").append(result.getZmqTotal() - resultLimit).append(" 条\n");
            }
            sb.append("\n");
        }
        
        // 最新作品 (waiwang2)
        if (result.getWaiwang2Total() > 0) {
            sb.append("【最新作品】").append(result.getWaiwang2Total()).append("条\n");
            List<Waiwang2Video> list = result.getWaiwang2Videos();
            for (int i = 0; i < list.size(); i++) {
                Waiwang2Video v = list.get(i);
                sb.append(i + 1).append(". ")
                  .append(getTitle(v.getTitle())).append("\n")
                  .append("   指令: bc").append(v.getId())
                  .append(" | 作者: ").append(v.getNickname())
                  .append(" | 时长: ").append(v.getDuration()).append("分\n");
                if (v.getCover() != null && !v.getCover().isEmpty()) {
                    sb.append("   封面: ").append(v.getCover()).append("\n");
                }
            }
            if (result.getWaiwang2Total() > resultLimit) {
                sb.append("   ... 还有 ").append(result.getWaiwang2Total() - resultLimit).append(" 条\n");
            }
            sb.append("\n");
        }
        
        // 玩物 (wanwu)
        if (result.getWanwuTotal() > 0) {
            sb.append("【玩物搜索】").append(result.getWanwuTotal()).append("条\n");
            List<WanwuVideo> list = result.getWanwuVideos();
            for (int i = 0; i < list.size(); i++) {
                WanwuVideo v = list.get(i);
                sb.append(i + 1).append(". ")
                  .append(getTitle(v.getTitle())).append("\n")
                  .append("   指令: ww").append(v.getVid())
                  .append(" | 作者: ").append(v.getUrlkey2())
                  .append(" | 时长: ").append(v.getDuration()).append("\n");
                if (v.getCover() != null && !v.getCover().isEmpty()) {
                    sb.append("   封面: ").append(v.getCover()).append("\n");
                }
            }
            if (result.getWanwuTotal() > resultLimit) {
                sb.append("   ... 还有 ").append(result.getWanwuTotal() - resultLimit).append(" 条\n");
            }
            sb.append("\n");
        }
        
        // 淘露 (taolu3)
        if (result.getTaolu3Total() > 0) {
            sb.append("【淘露搜索】").append(result.getTaolu3Total()).append("条\n");
            List<Taolu3Video> list = result.getTaolu3Videos();
            for (int i = 0; i < list.size(); i++) {
                Taolu3Video v = list.get(i);
                sb.append(i + 1).append(". ")
                  .append(getTitle(v.getTitle())).append("\n")
                  .append("   指令: tl").append(v.getVid())
                  .append(" | 作者: ").append(v.getUrlkey2()).append("\n");
                if (v.getCover() != null && !v.getCover().isEmpty()) {
                    sb.append("   封面: ").append(v.getCover()).append("\n");
                }
            }
            if (result.getTaolu3Total() > resultLimit) {
                sb.append("   ... 还有 ").append(result.getTaolu3Total() - resultLimit).append(" 条\n");
            }
            sb.append("\n");
        }
        
        // 电报 (waiwang)
        if (result.getWaiwangTotal() > 0) {
            sb.append("【电报搜索】").append(result.getWaiwangTotal()).append("条\n");
            List<WaiwangVideo> list = result.getWaiwangVideos();
            for (int i = 0; i < list.size(); i++) {
                WaiwangVideo v = list.get(i);
                sb.append(i + 1).append(". ")
                  .append(getTitle(v.getTitle())).append("\n")
                  .append("   指令: tg").append(v.getId())
                  .append(" | 时间: ").append(v.getDt()).append("\n");
            }
            if (result.getWaiwangTotal() > resultLimit) {
                sb.append("   ... 还有 ").append(result.getWaiwangTotal() - resultLimit).append(" 条\n");
            }
            sb.append("\n");
        }
        
        // 总计
        long total = result.getTotalCount();
        if (total == 0) {
            sb.append("❌ 未找到相关作品\n\n");
        } else {
            sb.append("==============================\n");
            sb.append("总计: ").append(total).append(" 条\n\n");
        }
        
        sb.append("使用以下指令提取作品:\n");
        sb.append("• ww + ID - 玩物\n");
        sb.append("• tl + ID - 淘露\n");
        sb.append("• bc + ID - 本地\n");
        sb.append("• zm + ID - 网页\n");
        sb.append("• tg + ID - 电报\n");
        
        return sb.toString();
    }
    
    /**
     * 格式化搜索结果（兼容旧调用）
     */
    private String formatSearchResult(SearchResultDTO result, String keyword) {
        return formatSearchResult(result, keyword, false);
    }
    
    /**
     * 格式化作品提取结果（带余额信息）
     */
    private String formatVideoExtract(Object video, String sourceName, String cmd, String vid,
                                       int balanceBefore, int balanceAfter) {
        StringBuilder sb = new StringBuilder();
        sb.append("📹 作品提取成功\n");
        sb.append("==============================\n");
        sb.append("来源: ").append(sourceName).append("\n");
        sb.append("ID: ").append(vid).append("\n");
        
        String playUrl = null;
        String coverUrl = null;
        
        if (video instanceof WanwuVideo) {
            WanwuVideo v = (WanwuVideo) video;
            sb.append("标题: ").append(getTitle(v.getTitle())).append("\n");
            sb.append("作者: ").append(v.getUrlkey2()).append("\n");
            sb.append("时长: ").append(v.getDuration()).append("\n");
            playUrl = v.getUrl();
            coverUrl = v.getCover();
        } else if (video instanceof Waiwang2Video) {
            Waiwang2Video v = (Waiwang2Video) video;
            sb.append("标题: ").append(getTitle(v.getTitle())).append("\n");
            sb.append("作者: ").append(v.getNickname()).append("\n");
            sb.append("时长: ").append(v.getDuration()).append("分\n");
            playUrl = v.getUrl();
            coverUrl = v.getCover();
        } else if (video instanceof Taolu3Video) {
            Taolu3Video v = (Taolu3Video) video;
            sb.append("标题: ").append(getTitle(v.getTitle())).append("\n");
            sb.append("作者: ").append(v.getUrlkey2()).append("\n");
            playUrl = v.getUrl();
            coverUrl = v.getCover();
        } else if (video instanceof ZmqVideo) {
            ZmqVideo v = (ZmqVideo) video;
            sb.append("标题: ").append(v.getTitle()).append("\n");
            sb.append("时长: ").append(v.getDuration()).append("\n");
            playUrl = v.getUrl();
            coverUrl = v.getCover();
        } else if (video instanceof WaiwangVideo) {
            WaiwangVideo v = (WaiwangVideo) video;
            sb.append("标题: ").append(getTitle(v.getTitle())).append("\n");
            sb.append("时间: ").append(v.getDt()).append("\n");
            playUrl = v.getUrl();
        }
        
        // 添加封面 URL
        if (coverUrl != null && !coverUrl.isEmpty()) {
            sb.append("------------------------------\n");
            sb.append("📷 封面预览:\n");
            sb.append(coverUrl).append("\n");
        }
        
        // 添加播放链接
        sb.append("------------------------------\n");
        if (playUrl != null && !playUrl.isEmpty()) {
            sb.append("▶️ 播放链接:\n");
            sb.append(playUrl).append("\n");
        } else {
            sb.append("⚠️ 暂无播放链接，请联系客服\n");
        }
        
        sb.append("------------------------------\n");
        sb.append("💰 余额: ").append(balanceBefore).append(" → ").append(balanceAfter)
          .append(" (扣费 ").append(DEDUCT_AMOUNT).append(")\n");
        sb.append("------------------------------\n");
        sb.append("如果无法播放，请联系客服 QQ: 2167485304");
        
        return sb.toString();
    }
    
    /**
     * 获取或创建用户钱包
     * 新用户默认赠送 DEFAULT_BALANCE 点
     */
    /**
     * 获取或创建钱包（QQ 用户专用）
     * 新用户昵称格式: appId_机器人名_QQ号
     */
    private TbWallet getOrCreateWallet(String userId, String nickname) {
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
        
        // 新用户昵称格式: appId_机器人名_QQ号
        String robotName = (currentClient != null && currentClient.getName() != null) 
            ? currentClient.getName() 
            : "QQBot";
        String appId = (currentClient != null && currentClient.getAppId() != null) 
            ? currentClient.getAppId() 
            : "unknown";
        
        // 格式: appId_机器人名_QQ号，例如: 1903745193_QQBot_2167485304
        String autoNickname = appId + "_" + robotName + "_" + userId;
        wallet.setNickname(autoNickname);
        
        // 小飞机账号为空（使用默认值）
        wallet.setFeijiUsername(null);
        wallet.setFeijiPassword(null);
        
        tbWalletMapper.insertSelective(wallet);
        log.info("[QQBot] 创建新用户钱包: user={}, nickname={}, 初始余额={}", userId, autoNickname, DEFAULT_BALANCE);
        return wallet;
    }
    
    /**
     * 提取关键词
     */
    private String extractKeyword(QQBotMessage message) {
        String keyword = message.getContent().trim();
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
            default: return "未知";
        }
    }
    
    /**
     * 获取标题（去掉后缀）
     */
    private String getTitle(String title) {
        if (title == null) return "";
        int idx = title.indexOf('_');
        if (idx > 0) {
            return title.substring(0, idx);
        }
        return title;
    }
    
    /**
     * 生成帮助信息
     */
    private String generateHelpMessage() {
        return "🤖 QQ Bot 帮助 (topic=" + QQ_TOPIC_TYPE + ")\n" +
               "==============================\n\n" +
               "【扣费说明】\n" +
               "• 新用户赠送 " + DEFAULT_BALANCE + " 点\n" +
               "• 搜索作品: 免费\n" +
               "• 提取作品: 扣 " + DEDUCT_AMOUNT + " 点 (topic=" + QQ_TOPIC_TYPE + ")\n\n" +
               "【搜索功能】\n" +
               "• 直接发送关键词 - 明文搜索\n" +
               "• 发送密文 - 加密搜索（更安全）\n" +
               "• 最新作品 - 查看最新作品\n\n" +
               "【加密搜索步骤】（可选）\n" +
               "1. 打开加密工具: aes_encrypt_tool.html\n" +
               "2. 输入关键词，点击加密\n" +
               "3. 复制生成的密文\n" +
               "4. 直接发送密文到 QQ\n" +
               "5. 机器人自动解密并返回结果\n\n" +
               "【加密标准】\n" +
               "• 算法: AES-256-ECB\n" +
               "• 填充: PKCS7\n" +
               "• 输出: Base64\n" +
               "• 密钥: GqAE@n^m0ZFI8e&1o5V4\n\n" +
               "【提取作品】\n" +
               "• ww + 作品ID - 提取玩物作品\n" +
               "• tl + 作品ID - 提取淘露作品\n" +
               "• bc + 作品ID - 提取本地作品\n" +
               "• zm + 作品ID - 提取网页作品\n" +
               "• tg + 作品ID - 提取电报作品\n\n" +
               "【示例】\n" +
               "• 美女（明文搜索）\n" +
               "• U2FsdGVkX1+abc123...（密文搜索）\n" +
               "• ww12345（提取作品）\n" +
               "• tl67890（提取作品）\n" +
               "• 最新（查看最新作品）\n" +
               "• 帮助（显示本信息）\n\n" +
               "【注意】\n" +
               "• 搜索结果每类仅显示前" + resultLimit + "条\n" +
               "• 如需查看更多，请精确搜索\n" +
               "• 余额不足请联系客服 QQ: 2167485304 充值";
    }
    
    // ==================== 内部类和方法 ====================
    
    /**
     * 作品信息内部类
     */
    private static class VideoInfo {
        String url;
        String title;
        String byString;  // 用于判断是否下载
        String wpString;
        String cover;
        String author;
        int zhindex = 0;
    }
    
    /**
     * 提取作品信息（和 Telegram 逻辑一致）
     */
    private VideoInfo extractVideoInfo(String cmd, String vid) {
        VideoInfo info = new VideoInfo();
        
        switch (cmd) {
            case "ww":
                WanwuVideo ww = robotService.getVideoWw(vid);
                if (ww != null) {
                    info.url = ww.getUrl();
                    info.title = ww.getTitle();
                    info.byString = ww.getTria();
                    info.wpString = ww.getUptag3() + "";
                    info.cover = ww.getCover();
                    info.author = ww.getUrlkey2();
                }
                break;
            case "tl":
                Taolu3Video tl = robotService.getVideotl(vid);
                if (tl != null) {
                    info.url = tl.getUrl();
                    info.title = tl.getTitle();
                    info.byString = tl.getTria();
                    info.wpString = tl.getUptag3() + "";
                    info.cover = tl.getCover();
                    info.author = tl.getUrlkey2();
                }
                break;
            case "bc":
                Waiwang2Video bc = robotService.getVideo(vid);
                if (bc != null) {
                    info.url = bc.getUrl();
                    info.title = bc.getTitle() + "_" + bc.getVid();
                    info.byString = bc.getPantag();
                    info.wpString = bc.getChannel();
                    info.cover = bc.getCover();
                    info.author = bc.getNickname();
                }
                break;
            case "zm":
                ZmqVideo zm = robotService.getVideoZmq(vid);
                if (zm != null) {
                    info.url = zm.getUrl();
                    info.title = zm.getTitle();
                    info.byString = zm.getTria();
                    info.wpString = "";
                    info.cover = zm.getCover();
                    info.author = "";
                }
                break;
            case "tg":
                WaiwangVideo tg = robotService.getVideoTG(vid);
                if (tg != null) {
                    info.url = tg.getUrl();
                    info.title = tg.getTitle();
                    info.byString = tg.getChannel();
                    info.wpString = "";
                    info.cover = "";
                    info.author = "";
                }
                break;
        }
        
        return info;
    }
    
    /**
     * 判断是否需要下载文件（和 Telegram 逻辑完全一致）
     * 
     * 下载条件：
     * - zm/ww/tl: byString = "sk" / 包含 "t.me" / 包含 "pikpak"
     * - tg/ch: byString = null / "" / "null" / 包含 "t.me"  或 url 包含 "t.me"
     * 
     * 注意：feijipan.com 直接给用户，不走下载！
     */
    private boolean isNeedDownload(String byString, String url, String cmd) {
        if (url == null) {
            url = "";
        }
        
        // zm/ww/tl 判断：sk / t.me / pikpak / 直接下载
        // 【注意】QQ Bot 所有作品都走下载-上传feijipan流程
        if (cmd.equals("zm") || cmd.equals("ww") || cmd.equals("tl")) {
            // QQ 用户所有作品都下载（避免直接发送链接被封）
            return true;
        }
        
        // tg 判断：
        // Telegram 原逻辑：if (byString == null || byString.indexOf("feijipan.com") == -1)
        // 意思是：不包含 feijipan.com 才需要下载
        if (cmd.equals("tg")) {
            // 包含 feijipan.com 直接给链接，不下载
            if (byString != null && byString.contains("feijipan.com")) {
                return false;
            }
            // 其他情况：null / "" / "null" / t.me 需要下载
            if (byString == null || 
                byString.isEmpty() || 
                byString.equals("null") || 
                byString.contains("t.me") || 
                url.contains("t.me")) {
                return true;
            }
            return false;
        }
        
        // ch 判断：和 tg 相同
        if (cmd.equals("ch")) {
            // 包含 feijipan.com 直接给链接，不下载
            if (byString != null && byString.contains("feijipan.com")) {
                return false;
            }
            // 其他情况需要下载
            if (byString == null || 
                byString.isEmpty() || 
                byString.equals("null") || 
                byString.contains("t.me") || 
                url.contains("t.me")) {
                return true;
            }
            return false;
        }
        
        // bc 默认不需要下载（直接给链接）
        return false;
    }
    
    /**
     * 构建 Redis 队列消息（15个字段）
     * 
     * 字段顺序:
     * 0: chatroom (QQ号)
     * 1: url (视频URL)
     * 2: title (视频标题)
     * 3: vid (作品ID)
     * 4: chatid (QQ号)
     * 5: cover (封面图URL)
     * 6: byString (VIP频道链接/PikPak链接)
     * 7: wpString (备用字段)
     * 8: author (作者名)
     * 9: zhindex (索引)
     * 10: vip (VIP等级，QQ用户固定为3)
     * 11: appId (QQ Bot AppId)
     * 12: clientSecret (QQ Bot ClientSecret)
     * 13: feijiUsername (小飞机网盘用户名)
     * 14: feijiPassword (小飞机网盘密码)
     */
    private String buildRedisMessage(String userId, VideoInfo info, String receivedText, String nickname) {
        // 获取用户钱包信息（用于读取绑定的小飞机账号）
        TbWallet wallet = getOrCreateWallet(userId, nickname);
        
        StringBuilder sb = new StringBuilder();
        sb.append(userId).append(",");              // [0] identifier = QQ号
        sb.append(info.url).append(",");            // [1] url
        sb.append(info.title).append(",");          // [2] title
        sb.append(receivedText).append(",");        // [3] vid (ww12345等)
        sb.append(userId).append(",");              // [4] chatId = QQ号
        sb.append(info.cover != null ? info.cover : "").append(",");      // [5] cover
        sb.append(info.byString != null ? info.byString : "").append(","); // [6] byString
        sb.append(info.wpString != null ? info.wpString : "").append(","); // [7] wpString
        sb.append(info.author != null ? info.author : "").append(",");     // [8] author
        sb.append(info.zhindex).append(",");        // [9] zhindex
        sb.append(QQ_TOPIC_TYPE).append(",");       // [10] vip = 3 (QQ用户固定VIP3)
        
        // [11-12] appId 和 clientSecret
        if (currentClient != null) {
            sb.append(currentClient.getAppId()).append(",");
            sb.append(currentClient.getClientSecret()).append(",");
        } else {
            sb.append(",");  // 空appId
            sb.append(",");  // 空clientSecret
        }
        
        // [13-14] 小飞机网盘账号（用户绑定的，如果没有则传空字符串，Python端使用默认值）
        String feijiUsername = (wallet != null && wallet.getFeijiUsername() != null) 
            ? wallet.getFeijiUsername() 
            : "";
        String feijiPassword = (wallet != null && wallet.getFeijiPassword() != null) 
            ? wallet.getFeijiPassword() 
            : "";
        sb.append(feijiUsername).append(",");  // [13] feijiUsername
        sb.append(feijiPassword);               // [14] feijiPassword
        
        return sb.toString();
    }
    
    /**
     * 格式化带下载提示的结果（隐藏URL，和Telegram一致）
     */
    private String formatVideoExtractWithDownload(VideoInfo info, String cmd, String vid,
                                                   int balanceBefore, int balanceAfter) {
        StringBuilder sb = new StringBuilder();
        sb.append("📹 作品提取成功\n");
        sb.append("==============================\n");
        sb.append("来源: ").append(getSourceName(cmd)).append("\n");
        sb.append("ID: ").append(vid).append("\n");
        sb.append("标题: ").append(getTitle(info.title)).append("\n");
        if (info.author != null && !info.author.isEmpty()) {
            sb.append("作者: ").append(info.author).append("\n");
        }
        
        // 添加封面 URL（封面可以显示）
        if (info.cover != null && !info.cover.isEmpty()) {
            sb.append("------------------------------\n");
            sb.append("📷 封面预览:\n");
            sb.append(info.cover).append("\n");
        }
        
        // 重要提示：会发送文件（隐藏真实URL）
        sb.append("------------------------------\n");
        sb.append("📥 文件下载提示:\n");
        sb.append("该作品需要下载后发送，\n");
        sb.append("稍后会自动发送视频文件到您的 QQ。\n");
        sb.append("（全自动发送，无需找客服）\n");
        // 不显示真实URL，防止用户直接访问
        
        sb.append("------------------------------\n");
        sb.append("💰 余额: ").append(balanceBefore).append(" → ").append(balanceAfter)
          .append(" (扣费 ").append(DEDUCT_AMOUNT).append(")\n");
        sb.append("------------------------------\n");
        sb.append("如果长时间未收到文件，请联系客服 QQ: 2167485304");
        
        return sb.toString();
    }
    
    // ==================== 加密文件发送相关方法 ====================
    
    // 固定 ZIP 密码（和解压密码一致）
    private static final String ZIP_PASSWORD = "GqAE@n^m0ZFI8e&1o5V4";
    
    // AES-256 加密密钥（用于解密用户加密的关键词）
    // 标准: AES-256-ECB + PKCS7/PKCS5 + Base64
    private static final String AES_KEY = "GqAE@n^m0ZFI8e&1o5V4";
    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    
    /**
     * 创建普通文本文件（暂不使用加密压缩，直接发送明文记事本）
     * 
     * @param userId QQ 用户 ID
     * @param vid 作品 ID
     * @param content 文本内容
     * @return 文本文件
     */
    private File createEncryptedTextFile(String userId, String vid, String content) {
        try {
            // 创建临时目录
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "qqbot_txt");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            
            // 创建文本文件（外部使用随机 UUID 文件名）
            String randomTxtName = UUID.randomUUID().toString().replace("-", "").substring(0, 16) + ".txt";
            File txtFile = new File(tempDir, randomTxtName);
            
            // 写入带 BOM 的 UTF-8 内容（安卓兼容）
            try (FileOutputStream fos = new FileOutputStream(txtFile);
                 OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
                // 写入 UTF-8 BOM
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);
                // 写入内容
                writer.write(content);
                writer.flush();
            }
            
            log.info("[QQBot] 创建文本文件成功: user={}, vid={}, file={}", userId, vid, txtFile.getAbsolutePath());
            return txtFile;
            
        } catch (Exception e) {
            log.error("[QQBot] 创建文本文件失败: user={}, vid={}", userId, vid, e);
            return null;
        }
    }
    
    /**
     * 创建密码保护的 ZIP 文件
     * 使用 Python 脚本创建 AES-256 加密的 ZIP 文件
     */
    private void createPasswordProtectedZip(File sourceFile, File zipFile, String password) throws IOException {
        // 使用 Python 脚本创建加密 ZIP（依赖 pyzipper 库）
        String pythonScript = "/home/www/telegramsender/Telegram_Restricted_Media_Downloader-main/create_encrypted_zip.py";
        
        // 检查 Python 脚本是否存在
        File scriptFile = new File(pythonScript);
        if (!scriptFile.exists()) {
            // 创建 Python 脚本
            createPythonZipScript(pythonScript);
        }
        
        // 调用 Python 脚本创建加密 ZIP
        // 使用固定的内部文件名 "作品.txt"，避免暴露原始文件名
        ProcessBuilder pb = new ProcessBuilder(
            "python3", pythonScript,
            sourceFile.getAbsolutePath(),
            zipFile.getAbsolutePath(),
            password,
            "作品.txt"  // 内部文件名（第4个参数）
        );
        
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        // 读取输出
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("[QQBot] Python: {}", line);
            }
        }
        
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Python 脚本执行失败，退出码: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Python 脚本被中断", e);
        }
        
        if (!zipFile.exists()) {
            throw new IOException("加密 ZIP 文件创建失败");
        }
    }
    
    /**
     * 创建 Python 加密脚本
     */
    private void createPythonZipScript(String scriptPath) throws IOException {
        String script = "#!/usr/bin/env python3\n" +
            "import sys\n" +
            "import os\n" +
            "\n" +
            "# 尝试使用 pyzipper (AES-256)，如果不存在则使用标准 zipfile\n" +
            "try:\n" +
            "    import pyzipper\n" +
            "    USE_PYZIPPER = True\n" +
            "except ImportError:\n" +
            "    import zipfile\n" +
            "    USE_PYZIPPER = False\n" +
            "\n" +
            "def create_encrypted_zip(source_file, output_zip, password, inner_name=None):\n" +
            "    if inner_name is None:\n" +
            "        inner_name = os.path.basename(source_file)\n" +
            "    if USE_PYZIPPER:\n" +
            "        # 使用 AES-256 加密\n" +
            "        with pyzipper.AESZipFile(output_zip, 'w', compression=pyzipper.ZIP_DEFLATED, encryption=pyzipper.WZ_AES) as zf:\n" +
            "            zf.setpassword(password.encode('utf-8'))\n" +
            "            zf.write(source_file, inner_name)\n" +
            "        print(f'[OK] AES-256 加密 ZIP 创建成功: {output_zip}')\n" +
            "    else:\n" +
            "        # 降级使用标准 ZIP 加密\n" +
            "        with zipfile.ZipFile(output_zip, 'w', compression=zipfile.ZIP_DEFLATED) as zf:\n" +
            "            zf.setpassword(password.encode('utf-8'))\n" +
            "            zf.write(source_file, inner_name)\n" +
            "        print(f'[OK] 标准 ZIP 加密创建成功: {output_zip}')\n" +
            "\n" +
            "if __name__ == '__main__':\n" +
            "    if len(sys.argv) < 4:\n" +
            "        print('Usage: python3 script.py <source_file> <output_zip> <password> [inner_name]')\n" +
            "        sys.exit(1)\n" +
            "    inner_name = sys.argv[4] if len(sys.argv) > 4 else None\n" +
            "    create_encrypted_zip(sys.argv[1], sys.argv[2], sys.argv[3], inner_name)\n";
        
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                new java.io.FileWriter(scriptPath))) {
            writer.write(script);
        }
        
        // 设置可执行权限
        new File(scriptPath).setExecutable(true);
        log.info("[QQBot] Python 加密脚本已创建: {}", scriptPath);
    }
    
    /**
     * 发送文件到 QQ（暂不加密压缩，直接发送明文记事本）
     * 通过 QQBotClient 发送文件
     */
    private void sendFileToQQ(QQBotReplyContext context, File file, String caption) {
        // 直接发送文本文件（不加密不压缩）
        boolean success = context.sendFile(file, caption);
        
        if (success) {
            log.info("[QQBot] 文件发送成功: file={}, size={}", file.getName(), file.length());
        } else {
            log.error("[QQBot] 文件发送失败: file={}", file.getName());
            // 发送失败时提示用户
            context.sendResults("⚠️ 文件发送失败，请联系客服 QQ: 2167485304");
        }
    }
    
    // ==================== AES-256 解密相关方法 ====================
    
    /**
     * 判断内容是否可能是 AES 密文（无前缀格式）
     * 特征：纯 Base64 字符，长度合理（通常是 16 的倍数）
     */
    private boolean isLikelyCiphertext(String content) {
        if (content == null || content.length() < 20) {
            return false;
        }
        
        // 检查是否只包含 Base64 字符
        String trimmed = content.trim();
        if (!trimmed.matches("^[A-Za-z0-9+/=]+$")) {
            return false;
        }
        
        // 长度检查：AES 密文长度通常是 16 的倍数（经过 Base64 编码后）
        // Base64 每 3 字节变成 4 字符，所以长度应该是 4 的倍数
        if (trimmed.length() % 4 != 0) {
            return false;
        }
        
        // 排除明显的非密文（如纯数字、纯字母等）
        // 密文应该包含混合字符
        boolean hasUpper = trimmed.matches(".*[A-Z].*");
        boolean hasLower = trimmed.matches(".*[a-z].*");
        boolean hasDigit = trimmed.matches(".*[0-9].*");
        boolean hasSpecial = trimmed.matches(".*[+/=].*");
        
        // 至少包含大写、小写、数字中的两种，以及特殊字符
        int typeCount = 0;
        if (hasUpper) typeCount++;
        if (hasLower) typeCount++;
        if (hasDigit) typeCount++;
        
        return typeCount >= 2 && hasSpecial;
    }
    
    /**
     * 尝试解密搜索关键词（无前缀密文格式）
     * @param content 密文
     * @return 解密后的关键词，失败返回 null
     */
    private String tryDecryptSearchKeyword(String content) {
        try {
            String trimmed = content.trim();
            
            // 1. Base64 解码得到 AES 密文
            byte[] encryptedBytes = Base64.getDecoder().decode(trimmed);
            
            // 2. AES-256-ECB 解密
            byte[] decryptedBytes = aes256EcbDecrypt(encryptedBytes);
            
            // 3. 解密结果是 Base64 编码的原始关键词
            String base64Keyword = new String(decryptedBytes, StandardCharsets.UTF_8);
            
            // 4. Base64 解码得到真实关键词
            byte[] keywordBytes = Base64.getDecoder().decode(base64Keyword);
            String realKeyword = new String(keywordBytes, StandardCharsets.UTF_8);
            
            // 5. 验证解密结果是否合理（非空，长度合适）
            if (realKeyword == null || realKeyword.trim().isEmpty() || realKeyword.length() > 100) {
                return null;
            }
            
            log.info("[QQBot] 密文解密成功: 长度={}, 关键词长度={}", 
                trimmed.length(), realKeyword.length());
            
            return realKeyword.trim();
            
        } catch (Exception e) {
            // 解密失败，不是有效的密文
            log.debug("[QQBot] 密文解密失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 处理加密搜索（无前缀密文）
     * 搜索后通过 AsyncEventPublisher 生成记事本并推送到队列
     * 不返回任何结果，只提示用户记事本随后送达
     */
    private void handleEncryptedSearch(QQBotMessage message, QQBotReplyContext context, String keyword) {
        log.info("[QQBot] 加密搜索: 已解密, keyword长度={}", keyword.length());
        String userId = message.getUserId();
        
        // 发送搜索中提示（不显示关键词）
        context.sendProgress("⏳ 正在处理...\n请稍候...");
        
        try {
            // 执行搜索（只用于生成记事本，不返回给用户）
            long startTime = System.currentTimeMillis();
            SearchResultDTO result = robotService.searchAll(keyword, 1, 100);
            long costTime = System.currentTimeMillis() - startTime;
            
            log.info("[QQBot] 加密搜索完成: 耗时={}ms, 总结果={}", costTime, result.getTotalCount());
            
            // 使用 AsyncEventPublisher 生成记事本并推送到队列
            if (publisher != null) {
                // 获取当前机器人的 appId 和 clientSecret
                String appId = (currentClient != null) ? currentClient.getAppId() : null;
                String clientSecret = (currentClient != null) ? currentClient.getClientSecret() : null;
                publisher.publishQQBotSearchAsync(userId, keyword, result, appId, clientSecret);
                log.info("[QQBot] 搜索任务已提交到 publisher: user={}, keyword={}, appId={}", userId, keyword, appId);
                
                // 只提示用户记事本随后送达，不显示任何结果
                context.sendResults("🔐 收到搜索请求\n" +
                    "==============================\n" +
                    "📋 作品清单正在生成，随后送达...\n" +
                    "（加密压缩文件，请注意查收）");
            } else {
                // publisher 未注入，简单提示
                log.warn("[QQBot] publisher 未注入");
                context.sendResults("❌ 服务暂时不可用，请稍后重试");
            }
            
        } catch (Exception e) {
            log.error("[QQBot] 加密搜索失败", e);
            context.sendResults("❌ 搜索失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成搜索记事本文件
     */
    private File createSearchNotebook(String userId, String keyword, SearchResultDTO result) {
        try {
            // 创建临时目录
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "qqbot_notebook");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }
            
            // 生成文件名
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String safeKeyword = keyword.replaceAll("[^\\w\\u4e00-\\u9fa5]", "_");
            if (safeKeyword.length() > 20) {
                safeKeyword = safeKeyword.substring(0, 20);
            }
            String fileName = "搜索_" + safeKeyword + "_" + timestamp + ".txt";
            File notebookFile = new File(tempDir, fileName);
            
            // 写入内容
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(notebookFile), "UTF-8"))) {
                
                writer.write("🔐 加密搜索 - 作品清单\n");
                writer.write("==============================\n");
                writer.write("搜索时间: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
                writer.write("总结果: " + result.getTotalCount() + " 条\n\n");
                
                // 写入各类结果
                writeNotebookSection(writer, "【网页搜索】", result.getZmqVideos(), "zm");
                writeNotebookSection(writer, "【最新作品】", result.getWaiwang2Videos(), "bc");
                writeNotebookSection(writer, "【玩物搜索】", result.getWanwuVideos(), "ww");
                writeNotebookSection(writer, "【淘露搜索】", result.getTaolu3Videos(), "tl");
                writeNotebookSection(writer, "【外网搜索】", result.getWaiwangVideos(), "tg");
                
                writer.write("==============================\n");
                writer.write("使用指令提取作品:\n");
                writer.write("• ww + ID - 玩物\n");
                writer.write("• tl + ID - 淘露\n");
                writer.write("• bc + ID - 本地\n");
                writer.write("• zm + ID - 网页\n");
                writer.write("• tg + ID - 电报\n");
            }
            
            log.info("[QQBot] 搜索记事本生成成功: user={}, file={}, size={}bytes", 
                userId, notebookFile.getName(), notebookFile.length());
            
            return notebookFile;
            
        } catch (Exception e) {
            log.error("[QQBot] 生成搜索记事本失败: user={}", userId, e);
            return null;
        }
    }
    
    /**
     * 写入记事本章节
     */
    private void writeNotebookSection(BufferedWriter writer, String sectionName, List<?> videos, String cmd) throws IOException {
        if (videos == null || videos.isEmpty()) {
            return;
        }
        
        writer.write(sectionName + " " + videos.size() + "条\n");
        
        int index = 1;
        for (Object video : videos) {
            String vid = getVideoId(video);
            String title = getVideoTitle(video);
            String author = getVideoAuthor(video);
            String duration = getVideoDuration(video);
            
            writer.write(index + ". " + (title != null ? title : "无标题") + "\n");
            writer.write("   指令: " + cmd + vid + "\n");
            if (author != null && !author.isEmpty()) {
                writer.write("   作者: " + author + "\n");
            }
            if (duration != null && !duration.isEmpty()) {
                writer.write("   时长: " + duration + "\n");
            }
            writer.write("\n");
            index++;
        }
        
        writer.write("\n");
    }
    
    /**
     * 获取视频ID
     */
    private String getVideoId(Object video) {
        if (video instanceof ZmqVideo) return String.valueOf(((ZmqVideo) video).getVid());
        if (video instanceof Waiwang2Video) return String.valueOf(((Waiwang2Video) video).getId());
        if (video instanceof WanwuVideo) return String.valueOf(((WanwuVideo) video).getVid());
        if (video instanceof Taolu3Video) return String.valueOf(((Taolu3Video) video).getVid());
        if (video instanceof WaiwangVideo) return String.valueOf(((WaiwangVideo) video).getId());
        return "";
    }
    
    /**
     * 获取视频标题
     */
    private String getVideoTitle(Object video) {
        if (video instanceof ZmqVideo) return ((ZmqVideo) video).getTitle();
        if (video instanceof Waiwang2Video) return ((Waiwang2Video) video).getTitle();
        if (video instanceof WanwuVideo) return ((WanwuVideo) video).getTitle();
        if (video instanceof Taolu3Video) return ((Taolu3Video) video).getTitle();
        if (video instanceof WaiwangVideo) return ((WaiwangVideo) video).getTitle();
        return "";
    }
    
    /**
     * 获取视频作者
     */
    private String getVideoAuthor(Object video) {
        if (video instanceof ZmqVideo) return "";
        if (video instanceof Waiwang2Video) return ((Waiwang2Video) video).getNickname();
        if (video instanceof WanwuVideo) return ((WanwuVideo) video).getUrlkey2();
        if (video instanceof Taolu3Video) return ((Taolu3Video) video).getUrlkey2();
        if (video instanceof WaiwangVideo) return "";
        return "";
    }
    
    /**
     * 获取视频时长
     */
    private String getVideoDuration(Object video) {
        if (video instanceof ZmqVideo) return ((ZmqVideo) video).getDuration();
        if (video instanceof Waiwang2Video) return ((Waiwang2Video) video).getDuration() + "分";
        if (video instanceof WanwuVideo) return ((WanwuVideo) video).getDuration();
        if (video instanceof Taolu3Video) return "";
        if (video instanceof WaiwangVideo) return "";
        return "";
    }
    
    /**
     * 构建记事本消息（格式与提取作品一致，len(info)==2）
     */
    private String buildNotebookMessage(String userId, File notebookFile) {
        // 格式: 用户ID_随机数,文件路径
        // videodonw 识别 len(info)==2 为记事本任务
        String randomSuffix = String.valueOf(System.currentTimeMillis() % 10000);
        return userId + "_" + randomSuffix + "," + notebookFile.getAbsolutePath();
    }
    
    /**
     * 检查是否是 AES 加密的关键词（带前缀格式，保留向后兼容）
     * 格式: 搜 AES:xxxxx 或 AES:xxxxx
     */
    private boolean isAesEncryptedKeyword(String content) {
        if (content == null) return false;
        String trimmed = content.trim();
        return trimmed.startsWith("AES:") || 
               trimmed.toUpperCase().startsWith("搜 AES:") ||
               trimmed.toUpperCase().startsWith("搜索 AES:") ||
               trimmed.toUpperCase().startsWith("SEARCH AES:");
    }
    
    /**
     * 提取并解密 AES 加密的关键词
     * 标准: AES-256-ECB + PKCS5/PKCS7 + Base64
     * 解密后的明文本身也是 Base64 编码（避免中文编码问题）
     * 
     * @param content 用户发送的内容
     * @return 解密后的真实关键词，失败返回 null
     */
    private String decryptAesKeyword(String content) {
        try {
            // 提取密文部分
            String ciphertext = extractCiphertext(content);
            if (ciphertext == null || ciphertext.isEmpty()) {
                log.warn("[QQBot] 无法提取 AES 密文: {}", content);
                return null;
            }
            
            log.info("[QQBot] AES 密文提取成功: {}", ciphertext.substring(0, Math.min(20, ciphertext.length())) + "...");
            
            // 1. Base64 解码得到 AES 密文
            byte[] encryptedBytes = Base64.getDecoder().decode(ciphertext);
            
            // 2. AES-256-ECB 解密
            byte[] decryptedBytes = aes256EcbDecrypt(encryptedBytes);
            
            // 3. 解密结果是 Base64 编码的原始关键词
            String base64Keyword = new String(decryptedBytes, StandardCharsets.UTF_8);
            
            // 4. Base64 解码得到真实关键词
            byte[] keywordBytes = Base64.getDecoder().decode(base64Keyword);
            String realKeyword = new String(keywordBytes, StandardCharsets.UTF_8);
            
            log.info("[QQBot] AES 解密成功: 原始长度={}, 解密后长度={}", 
                ciphertext.length(), realKeyword.length());
            
            return realKeyword.trim();
            
        } catch (Exception e) {
            log.error("[QQBot] AES 解密失败", e);
            return null;
        }
    }
    
    /**
     * 从用户输入中提取密文
     */
    private String extractCiphertext(String content) {
        if (content == null) return null;
        
        String trimmed = content.trim();
        
        // 支持多种前缀格式
        String[] prefixes = {"AES:", "搜 AES:", "搜索 AES:", "SEARCH AES:",
                             "aes:", "搜 aes:", "搜索 aes:", "search aes:"};
        
        for (String prefix : prefixes) {
            if (trimmed.toUpperCase().startsWith(prefix.toUpperCase())) {
                return trimmed.substring(prefix.length()).trim();
            }
        }
        
        return null;
    }
    
    /**
     * AES-256-ECB 解密
     * 
     * @param encryptedData Base64解码后的密文
     * @return 解密后的明文
     */
    private byte[] aes256EcbDecrypt(byte[] encryptedData) throws Exception {
        // 密钥处理：直接使用字符串，填充/截断到 32 字节（AES-256）
        byte[] keyBytes = adjustKeyLength(AES_KEY.getBytes(StandardCharsets.UTF_8), 32);
        
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        
        return cipher.doFinal(encryptedData);
    }
    
    /**
     * 调整密钥长度（填充或截断）
     * 与大多数在线 AES 工具兼容
     */
    private byte[] adjustKeyLength(byte[] key, int length) {
        byte[] result = new byte[length];
        if (key.length >= length) {
            // 截断
            System.arraycopy(key, 0, result, 0, length);
        } else {
            // 复制原密钥，剩余补 0
            System.arraycopy(key, 0, result, 0, key.length);
            for (int i = key.length; i < length; i++) {
                result[i] = 0;
            }
        }
        return result;
    }
}
