# QQ Bot 集成说明

## 概述

新增 QQ Bot 私聊消息处理，**完全不影响原有 Telegram Bot 逻辑**。两者独立运行，复用同一套业务服务。

**注意**：本实现只处理**私聊消息**，不处理群消息和频道消息。

```
┌─────────────────────────────────────────────────────────────────┐
│                         Spring Boot 应用                          │
├─────────────────────────────────────────────────────────────────┤
│  TelegramChannelMonitor          QQBotClient                    │
│  (Telegram 群/频道/私聊)          (QQ 私聊专用)                   │
│        │                              │                         │
│        ▼                              ▼                         │
│  ┌─────────────────────────────────────────┐                    │
│  │         RobotService (复用)              │                    │
│  │  dealSearch() / dealGetWork()            │                    │
│  │  searchAll() / getVideoXxx()             │                    │
│  └─────────────────────────────────────────┘                    │
│        │                              │                         │
│        ▼                              ▼                         │
│  Telegram 回复格式              QQ 回复格式                      │
│  (Markdown + 图片)              (纯文本 4000字限制)              │
└─────────────────────────────────────────────────────────────────┘
```

## 新增文件

```
robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/
├── QQBotClient.java              # QQ Bot WebSocket 客户端
├── QQBotConfiguration.java       # Spring Boot 自动配置
├── QQBotMessage.java             # QQ 消息封装
├── QQBotMessageProcessor.java    # 消息处理器（复用 RobotService）
├── QQBotReplyService.java        # QQ 格式回复服务
└── QQMessageHandler.java         # 消息处理器接口
```

## 配置方法

### 1. 在 application.properties 中添加配置

```properties
# QQ Bot 配置
qqbot.enabled=true
qqbot.appId=1903745193
qqbot.clientSecret=MnBZuBHKKORVVegc
```

### 2. 确保依赖已存在

检查 `robotium-fundalarm-service/pom.xml` 是否已有以下依赖：

```xml
<!-- OkHttp -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.2.2</version>
</dependency>

<!-- WebSocket -->
<dependency>
    <groupId>org.java-websocket</groupId>
    <artifactId>Java-WebSocket</artifactId>
    <version>1.5.7</version>
</dependency>

<!-- Gson (通常在 fundalarm-common 中) -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.7</version>
</dependency>
```

**注意**: OkHttp 4.x 需要 Kotlin 标准库，如果运行时出现 `ClassNotFoundException`，请添加：

```xml
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib</artifactId>
    <version>1.3.50</version>
</dependency>
```

## 使用方式

### 推荐配置（真实数据 + 异步 + 图片URL）

```properties
qqbot.enabled=true
qqbot.appId=1903745193
qqbot.clientSecret=MnBZuBHKKORVVegc
qqbot.realData=true      # 使用真实数据库查询
qqbot.async=true         # 启用异步多次回复
qqbot.resultLimit=3      # 每类返回条数（默认3，范围1-5）
```

**注意**：`resultLimit` 控制每类搜索结果返回的条数。QQ Bot 限制同一消息1小时内最多回复4次，所以：
- 设置太大 → 消息过长，分多条发送，可能超过限制
- 设置太小 → 用户体验不好
- **推荐 3 条**（平衡体验和限制）

### AES-256 加密搜索（必须）

**⚠️ 明文搜索已关闭**

为防止腾讯服务器监测搜索关键词，所有搜索必须使用 AES-256-ECB 加密后发送。
直接发送明文关键词将被拒绝，并提示使用加密搜索。

**加密标准：**
- 算法：AES-256-ECB
- 填充：PKCS5/PKCS7
- 输出：Base64 编码
- 密钥：`GqAE@n^m0ZFI8e&1o5V4`
- 密钥处理：字符串直接作为密钥（UTF-8 编码，不足 32 字节补 0，超过截断）

**加密步骤：**
```
关键词 → Base64 编码 → AES-256-ECB 加密 → Base64 编码 → 密文
```

**示例（关键词 "美女"）：**
```
1. 关键词: 美女
2. Base64 编码: 576O5aW6
3. AES-256-ECB 加密（密钥: GqAE@n^m0ZFI8e&1o5V4）
4. Base64 输出: U2FsdGVkX1+abc123...（示例）
5. 发送到 QQ Bot: 搜 AES:U2FsdGVkX1+abc123...
```

**唯一支持的搜索方式：**
```
密文（无前缀，自动识别）
```

直接发送 AES 加密后的密文，机器人自动识别、解密并搜索。
明文搜索已关闭，发送明文将被拒绝。

**可用的第三方加密网站：**
1. [https://tool.chinaz.com/tools/textencrypt.aspx](https://tool.chinaz.com/tools/textencrypt.aspx) - 选 AES，ECB 模式
2. [https://www.lddgo.net/encrypt/aes](https://www.lddgo.net/encrypt/aes) - 支持 AES-256-ECB

**注意：**
- 明文必须先转 Base64，避免中文编码问题
- 密钥是固定的，所有用户使用相同密钥
- 密文传输过程中腾讯无法解密（没有密钥）

**⚠️ 关于第三方网站加密结果不同：**
不同网站对密钥的处理方式不同：
- **有些网站**使用 PBKDF2 加盐派生密钥（每次结果不同）
- **有些网站**自动添加随机 IV（即使 ECB 模式）
- **有些网站**直接截断/填充字符串作为密钥

**建议：** 如果某个网站每次加密结果不同，请尝试其他网站，或使用固定密钥的离线工具。

消息示例：
```
用户发送: 美女
(明文搜索已被拒绝)

机器人回复: ⚠️ 为保护隐私，明文搜索已关闭
             
             请使用加密方式搜索：
             1. 打开加密工具: aes_encrypt_tool.html
             2. 输入关键词并加密
             3. 直接发送生成的密文

用户发送: U2FsdGVkX1+abc123...
(直接发送密文，腾讯无法检测真实关键词)

机器人回复1: ⏳ 正在搜索...
             请稍候...

机器人回复2: 🔐 搜索完成
             ==============================
             
             【玩物搜索】8条
             1. 作品标题1
                指令: ww12345 | 作者: xxx | 时长: 10:00
             ...

用户发送: ww12345

机器人回复: 📹 作品提取成功
            ==============================
            来源: 玩物
            ID: 12345
            标题: 作品标题
            作者: xxx
            时长: 10:00
            ------------------------------
            📷 封面预览:
            https://cover1.jpg
            ------------------------------
            ▶️ 播放链接:
            https://play.example.com/12345
            ------------------------------
            如果无法播放，请联系客服 QQ: 2167485304
```

## QQ Bot 图片发送说明

由于 QQ Bot API 限制：

| 方式 | 支持 | 说明 |
|------|------|------|
| 直接上传图片 | ❌ 不支持 | QQ Bot 不允许直接上传本地图片 |
| 发送图片 URL | ✅ 支持 | 在消息文本中显示图片链接 |
| 富文本图片 | ⚠️ 需申请权限 | 需要申请 "消息模板" 权限 |

**当前方案**：在消息中直接显示封面 URL，用户点击链接即可查看图片。

优点：
- ✅ 无需额外权限
- ✅ 实现简单
- ✅ 用户点击即可查看
- ✅ 支持所有图片格式

缺点：
- 需要用户手动点击链接查看
- 不能像 Telegram 那样直接显示缩略图

### 方式二：深度集成（复用 RobotService 数据库查询）

需要修改 `QQBotMessageProcessor` 中的 `handleExtractWork` 和 `handleSearch` 方法，调用 `RobotService` 的具体方法：

```java
@Override
public void handleMessage(QQBotMessage message, QQBotClient client) {
    // 将 QQ 消息适配为 Telegram Update 格式
    Update update = adaptToTelegramUpdate(message);
    
    // 复用原有逻辑
    if (isExtractCommand(message)) {
        robotService.dealGetWork(update);
    } else {
        robotService.dealSearch(update);
    }
}
```

**注意**: `RobotService` 的方法依赖 Telegram 的 `Update` 对象，如果需要完全复用，需要创建一个适配器。

## 消息格式差异处理

| 功能 | Telegram | QQ |
|------|----------|-----|
| 消息接收 | HTTP 长轮询 | WebSocket 实时推送 |
| 消息长度 | 4096 字符 | 4000 字符 |
| 格式支持 | Markdown/HTML | 纯文本（有限 Markdown）|
| 图片发送 | 直接上传 | URL 方式 |
| 提及用户 | `@username` | `<qq:at id="123" />` |

### 自动转换

`QQBotReplyService.convertFromTelegramMarkdown()` 自动将 Telegram Markdown 转为 QQ 纯文本：

```java
// Telegram 格式
String tg = "**粗体** 和 `代码` 和 [链接](http://a.com)";

// 转换后 QQ 格式
String qq = QQBotReplyService.convertFromTelegramMarkdown(tg);
// 结果: "粗体 和 代码 和 http://a.com"
```

## 测试方式

### 1. 启动 Tomcat 后检查日志

```
[QQBot] 初始化客户端, AppId: 1903745193
[QQBot] 正在启动...
[QQBot] Token 获取成功
[QQBot] 网关地址: wss://api.sgroup.qq.com/websocket
[QQBot] WebSocket 已连接
[QQBot] 心跳线程已启动
[QQBot] 就绪! Session: a1b2c3d4...
```

### 2. 在 QQ 群/@机器人/私聊发送消息

- `帮助` - 显示帮助信息
- `密文` - 发送 AES 加密密文进行搜索（**必须**，明文已关闭）
- `最新作品` - 查看最新作品（无需加密）
- `ww12345` - 提取玩物作品（示例）

**⚠️ 注意：** 明文搜索已关闭，直接发送关键词将被拒绝并提示使用加密搜索。

### 加密搜索测试步骤

#### 方式一：使用本地 HTML 工具（推荐）
1. 打开 `aes_encrypt_tool.html` 文件（在浏览器中）
2. 输入关键词（如 `美女`）
3. 点击"加密"按钮
4. 复制生成的密文
5. 在 QQ 直接发送密文（无需任何前缀）
6. 机器人自动识别并返回搜索结果

#### 方式二：使用第三方网站
1. 访问 AES 加密网站（如 https://www.lddgo.net/encrypt/aes）
2. 输入关键词先进行 Base64 编码
3. 使用 AES-256-ECB 模式加密（密钥：`GqAE@n^m0ZFI8e&1o5V4`）
4. 复制输出的 Base64 密文
5. 在 QQ 直接发送密文
6. 机器人自动识别密文格式并解密搜索

**注意：** 如果第三方网站每次加密结果不同，请使用本地 HTML 工具。

### 3. 查看回复

QQ Bot 会自动回复纯文本消息。

## 禁用 QQ Bot

在 `application.properties` 中设置：

```properties
qqbot.enabled=false
```

或直接删除/注释配置项。

## 与 Telegram 对比

| 特性 | 当前实现 |
|------|---------|
| 多平台支持 | ✅ Telegram + QQ 同时运行 |
| 业务复用 | ✅ 复用 RobotService |
| 格式转换 | ✅ 自动 Markdown → 纯文本 |
| 图片发送 | ⚠️ QQ 仅支持 URL 方式 |
| 长消息处理 | ✅ 自动截断 4000 字符 |
| 心跳保活 | ✅ 自动心跳 |
| 断线重连 | ✅ 自动重连 |

## 注意事项

1. **不要修改原有 Telegram 代码** - QQ Bot 是独立模块
2. **QQ 消息限制** - 单条消息最长 4000 字符，超长会自动截断
3. **Token 过期** - QQ Token 2小时过期，客户端会自动刷新
4. **并发处理** - 使用 Spring TaskExecutor 异步处理消息
5. **日志分离** - QQ Bot 日志前缀 `[QQBot]`，方便区分
6. **图片发送** - QQ Bot 不支持直接上传图片，只能发送 URL 链接
7. **文件发送** - QQ Bot 不支持直接发送文件，需要申请特殊权限

## QQ Bot 与 Telegram Bot 对比

| 功能 | Telegram | QQ Bot |
|------|----------|--------|
| **群消息** | ✅ 支持 | ❌ **不支持（本实现）** |
| **频道消息** | ✅ 支持 | ❌ **不支持（本实现）** |
| **私聊消息** | ✅ 支持 | ✅ **支持** |
| 发送本地图片 | ✅ 直接上传 | ❌ 不支持 |
| 发送网络图片 URL | ✅ | ✅ |
| 发送本地文件 | ✅ | ❌ |
| 批量发图 | ✅ mediaGroup | ❌ 不支持 |
| 消息编辑 | ✅ | ❌ |
| 消息删除 | ✅ | ❌ |
| 消息长度 | 4096 字符 | 4000 字符 |
| Markdown | ✅ 完整支持 | ⚠️ 仅支持基础格式 |
| **多次回复** | **无限制** | **1小时内最多4次** ⚠️ |

**为什么限制返回条数？**

QQ Bot 限制同一消息 ID 1小时内最多回复 4 次。如果返回太多结果：
- 消息长度超过 4000 字符 → 需要分多条发送
- 分条数过多 → 超过 4 次限制 → 后续消息无法发送

**解决方案**：
- 每类只显示前 3 条（可配置 1-5 条）
- 显示总数和提示"还有更多结果"
- 引导用户精确搜索

## 多机器人架构

系统配置了 **38 个 QQ 机器人**实现负载均衡：

| 机器人 | AppId | 用途 |
|--------|-------|------|
| 机器人1 | 1903745193 | 默认/用户分配 |
| 机器人2 | 1903756971 | 用户分配 |
| 机器人3 | 1903768319 | 用户分配 |
| 机器人4 | 1903777125 | 用户分配 |
| 机器人5 | 1903781933 | 用户分配 |
| 机器人6 | 1903828854 | 用户分配 |
| 机器人7 | 1903828836 | 用户分配 |
| 机器人8 | 1903778154 | 用户分配 |
| 机器人9 | 1903830381 | 用户分配 |
| 机器人10 | 1903830571 | 用户分配 |
| 机器人11 | 1903837008 | 用户分配 |
| 机器人12 | 1903849229 | 用户分配 |
| 机器人13 | 1903849797 | 用户分配 |
| 机器人14 | 1903871246 | 用户分配 |
| 机器人15 | 1903900136 | 用户分配 |
| 机器人16 | 1903918975 | 用户分配 |
| 机器人17 | 1903918989 | 用户分配 |
| 机器人18 | 1903922462 | 用户分配 |
| 机器人19 | 1903922103 | 用户分配 |
| 机器人20 | 1903919041 | 用户分配 |
| 机器人21 | 1903970085 | 用户分配 |
| 机器人22 | 1903974632 | 用户分配 |
| 机器人23 | 1903983558 | 用户分配 |
| 机器人24 | 1904003772 | 用户分配 |
| 机器人25 | 1904036064 | 用户分配 |
| 机器人26 | 1904044034 | 用户分配 |
| 机器人27 | 1904048000 | 用户分配 |
| 机器人28 | 1904050561 | 用户分配 |
| 机器人29 | 1904050908 | 用户分配 |
| 机器人30 | 1904055337 | 用户分配 |
| 机器人31 | 1904055039 | 用户分配 |
| 机器人37 | 1904107192 | 用户分配 |
| 机器人38 | 1904130584 | 用户分配 |

配置文件：`/home/www/tomcat/apache-tomcat-9.0.102/webapps/ROOT/WEB-INF/classes/qqbot_bots.json`

### 机器人分配规则

用户首次搜索时，系统自动分配到其中一个机器人（轮询或随机）。后续该用户的所有操作（搜索、提取作品）都使用**同一个机器人**，确保消息发送权限正确。

**⚠️ 重要：记事本发送必须使用对应机器人**

QQ Bot API 限制：**机器人只能给与其交互过的用户发送消息**。如果用户用4号机器人搜索，但记事本用1号机器人发送，会返回错误：
```
{"message":"invalid request","code":11255,...}
```

## 记事本发送机制

### 队列数据格式

搜索任务完成后，Java端推送队列数据到 Redis（`videos`队列）：

**格式：15个字段**（与作品下载任务统一）
```
userId, url(文件路径), title, vid, chatid, cover, byString, tgLink, zhindex, author, vip, appId, clientSecret, feijiUser, feijiPass
```

**示例：**
```
A5FF9CF8165F456CD8B779AFE8A351FE,/home/www/data/QQ_xxx_安晴大大_xxx.txt,QQ_安晴大大,notebook,A5FF9CF8165F456CD8B779AFE8A351FE,,,,0,,3,1903745193,MnBZuBHKKORVVegc,,
```

字段说明：
- `[0]` userId: QQ用户ID（32位十六进制）
- `[1]` url: 记事本文件路径
- `[2]` title: 显示标题
- `[3]` vid: 固定为 `"notebook"` 表示记事本任务
- `[4]` chatid: 同userId
- `[5-9]`: 空（记事本不需要）
- `[10]` vip: 固定为 `"3"`（QQ用户VIP等级）
- `[11]` appId: 机器人AppId
- `[12]` clientSecret: 机器人密钥
- `[13-14]`: 空（记事本不需要）

### Python端处理流程

1. 从 `videos` 队列获取任务
2. 判断 `len(info) == 15 and info[3] == "notebook"`
3. 提取 `appId = info[11]`, `clientSecret = info[12]`
4. 调用 `send_secure_file_to_qq(user_id, file_path, title, safe_name, app_id, client_secret)`
5. 使用指定机器人发送记事本文件

### 关键代码

**Java端 - AsyncEventPublisher.java:**
```java
public void publishQQBotSearchAsync(String userId, String keyword, SearchResultDTO result, String appId, String clientSecret) {
    // ... 生成记事本文件 ...
    
    // 构建15字段队列数据
    StringBuilder sb = new StringBuilder();
    sb.append(userId).append(",");           // [0]
    sb.append(path).append(",");              // [1]
    sb.append("QQ_").append(safeKeyword).append(","); // [2]
    sb.append("notebook").append(",");        // [3] 标记为记事本
    sb.append(userId).append(",");            // [4]
    sb.append("").append(",").append("").append(",").append("").append(","); // [5-7]
    sb.append("0").append(",").append("").append(","); // [8-9]
    sb.append("3").append(",");               // [10] vip
    sb.append(appId != null ? appId : "").append(",");      // [11]
    sb.append(clientSecret != null ? clientSecret : "").append(","); // [12]
    sb.append("").append(",").append("");      // [13-14]
    
    jedisClient.rpush("videos", queueData);
}
```

**Python端 - qq_bot_sender.py:**
```python
def send_secure_file_to_qq(user_id, file_path, title="", safe_name=None, app_id=None, client_secret=None):
    # 使用指定机器人配置
    if app_id and client_secret:
        qq_sender = QQBotSender(app_id, client_secret)
    else:
        qq_sender = QQBotSender()  # 使用默认机器人（兼容旧格式）
    
    success = qq_sender.send_file(user_id, file_path, caption)
    return success
```

## VIP 用户管理

### 表结构

QQ/Telegram 用户共用 `test.tb_wallet`：

| 字段 | 说明 |
|------|------|
| `uid` | 用户唯一标识（Telegram 数字 ID、QQ 号或哈希字符串） |
| `nickname` | 用户昵称，`QQBot` 用户的 nickname 格式为 `{qq号}_QQBot_xxx` |
| `balance` | 余额/积分 |
| `vip` | VIP 等级，`1` 表示 VIP |
| `vid_end_time` | VIP 到期时间 |

### 查询 QQBot 已过期用户

```sql
SELECT uid, nickname, balance, vip, vid_end_time,
       TIMESTAMPDIFF(DAY, vid_end_time, NOW()) AS expired_days
FROM test.tb_wallet
WHERE vip = 1
  AND vid_end_time IS NOT NULL
  AND vid_end_time < NOW()
  AND nickname LIKE '%QQBot%'
ORDER BY vid_end_time DESC;
```

### 查询 QQBot 7 天内到期用户

```sql
SELECT uid, nickname, balance, vip, vid_end_time,
       TIMESTAMPDIFF(DAY, NOW(), vid_end_time) AS remain_days
FROM test.tb_wallet
WHERE vip = 1
  AND vid_end_time IS NOT NULL
  AND vid_end_time >= NOW()
  AND vid_end_time <= DATE_ADD(NOW(), INTERVAL 7 DAY)
  AND nickname LIKE '%QQBot%'
ORDER BY vid_end_time ASC;
```

### 当前状态（2026-06-20 查询）

- **已过期 QQBot 用户**：0 个
- **7 天内到期 QQBot 用户**：4 个

| nickname | 余额 | 到期时间 | 剩余天数 |
|---|---|---|---|
| 1904048000_QQBot_2iP6oWFzjUF1naNBzodTJA1tleXRLGC8 | 9999 | 2026-06-22 00:00:00 | 0 |
| 1904050908_QQBot_ukOq44sTq0xf8NN8 | 9999 | 2026-06-23 00:00:00 | 1 |
| 1904055039_QQBot_hFoOyZAmO1eIwbHxeL3lUDxhSDzmZNB0 | 9999 | 2026-06-24 00:00:00 | 2 |
| 1904003772_QQBot_44rRqGalsurfIsT3 | 9999 | 2026-06-24 00:00:00 | 2 |

> QQ 号就是 nickname 中下划线前面的数字部分。

## 常见问题

### 错误 code:11255 - "invalid request"

**原因：** 机器人尝试给未与其交互过的用户发送消息。

**场景：**
- 用户用4号机器人搜索
- 记事本用1号机器人发送
- QQ API 拒绝，返回11255

**解决：** 确保记事本发送使用用户搜索时分配的机器人（通过15字段队列传递appId）。

### 记事本发送失败但无错误日志

**检查点：**
1. Python脚本是否在运行：`ps aux | grep donwloadFileAndSendToUser.py`
2. 队列是否有任务：`redis-cli -n 4 LLEN videos`
3. 日志中是否识别为15字段格式：查找 `"[DEBUG] 15字段记事本任务"`
4. appId/clientSecret是否正确传递：查找 `"[QQBot Config] appId="`

### 旧格式兼容

系统同时支持两种格式：
- **旧格式（2字段）**：`QQ_用户ID_关键词,文件路径`
  - 使用默认机器人（1号）发送
  - 仅用于兼容重启前遗留任务
- **新格式（15字段）**：包含机器人配置
  - 使用指定机器人发送
  - 推荐方式

## 近期更新记录

### 2026-04-12 GroupNotepadBot 功能增强

#### 1. 新增 GroupNotepadBot（群记事本机器人）

**Token**: `8766973549:AAFKJb6cNz3WIB31mdLnsBMxH6s8BVZJdIM`
**目标群组**: `-1003205013648`
**主题类型**: `topic=4` (GroupNotepadBot)

**功能特性**:
- 支持提取命令：`ww/zm/tl/tg/ch/bc` + vid
- 提取消耗积分：每次扣除 1 积分（tb_wallet 表）
- 新用户默认积分：5 分
- 积分不足时提示用户充值

**文件**: `GroupNotepadBot.java`

#### 2. 文件格式变更

| 格式 | 旧 | 新 | 说明 |
|------|-----|-----|------|
| 扩展名 | `.html` | `.txt` | 改为纯文本 |
| 编码 | UTF-8 | UTF-8 with BOM | Windows 记事本兼容 |
| 换行符 | `\n` | `\r\n` | Windows 换行符 |

**原因**: Windows 记事本默认使用 `\r\n` 作为换行符，只有 UTF-8 BOM + `\r\n` 才能正确显示中文。

#### 3. VIP4 支持 - 转发到指定群组

**配置**:
- VIP 等级 4 的作品下载后转发到 `-1003205013648`
- 转发后删除本地文件

**Python 端实现** (`donwloadFileAndSendToUser.py`):
```python
if vip == 4:
    await client.forward_messages(-1003205013648, ...)
    # 删除本地文件
```

#### 4. zhindex 客户端选择机制

根据 `info[9]` (zhindex) 选择 Telegram 客户端：

| zhindex | 客户端 | 变量名 |
|---------|--------|--------|
| 0 | 默认 | `client` |
| 1 | 机器人1 | `client1` / `mybot1` |
| 2 | 机器人2 | `client2` / `mybot2` |

**应用场景**:
- VIP4 默认使用 `zhindex=0` (默认客户端)
- VIP1 使用 `zhindex=1` (client1)
- VIP2 使用 `zhindex=2` (client2)

#### 5. bc 命令修复

**问题**: `bc` 命令提取时查询方式错误，导致找不到数据。

**修复**:
```java
// 修复前（错误）
Waiwang2VideoExample example = new Waiwang2VideoExample();
example.createCriteria().andVidEqualTo(vid);
List<Waiwang2Video> videos = waiwang2VideoMapper.selectByExample(example);

// 修复后（正确）
Waiwang2Video bcVideo = waiwang2VideoMapper.selectByPrimaryKey(Integer.parseInt(vid));
```

**标题格式**: `title_vid` (如 `视频标题_12345`)

**作者来源**: `nickname` 字段

#### 6. Pantag 过滤

**新增**: 搜索 `waiwang2_video` 时排除 `pantag` 不包含 `"http"` 的结果。

**实现**:
```java
criteria.andPantagLike("%http%");  // 排除无效/加密链接
```

**原因**: `pantag` 字段不含 `"http"` 的行通常是失效链接或加密数据。

#### 7. 文件换行符统一修复

**受影响文件**:
- `GroupNotepadBot.java` - 搜索生成的 TXT 文件
- `AsyncEventPublisher.java` - QQ Bot 搜索生成的 TXT 文件

**修改**: 所有 `\n` 改为 `\r\n`

```java
// 示例
sb.append("=======================================\r\n");
sb.append("🔐 作品搜索清单\r\n");
```

---

## 队列数据格式（15字段）

完整字段说明：

```
[0]  chatroom      - 用户ID（QQ/Telegram）
[1]  url           - 文件路径或下载链接
[2]  title         - 作品标题
[3]  vid           - 视频ID（记事本任务为 "notebook"）
[4]  chatid        - 聊天ID（同userId）
[5]  cover         - 封面URL
[6]  byString      - 来源标识
[7]  wpString      - 频道/来源
[8]  author        - 作者
[9]  zhindex       - 客户端选择索引 (0=默认, 1=client1, 2=client2)
[10] vip           - VIP等级 (0-4)
[11] appId         - QQ机器人AppId
[12] clientSecret  - QQ机器人密钥
[13] feijiUsername - 飞机账号（备用）
[14] feijiPassword - 飞机密码（备用）
```

---

## 后续优化建议

1. **完全复用 RobotService**: 创建 `TelegramUpdateAdapter` 将 QQ 消息转为 Telegram Update
2. **图片发送**: QQ 需要先将图片上传到图床，再发送 URL
3. **群白名单**: 添加配置 `qqbot.allowedGroups` 限制响应群组
4. **用户绑定**: QQ 用户 ID 和 Telegram 用户 ID 映射
5. **消息限流**: QQ API 有频率限制，可添加限流器
