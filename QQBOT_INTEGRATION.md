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

系统配置了 **5 个 QQ 机器人**实现负载均衡：

| 机器人 | AppId | 用途 |
|--------|-------|------|
| 机器人1 | 1903745193 | 默认/用户分配 |
| 机器人2 | 1903756971 | 用户分配 |
| 机器人3 | 1903768319 | 用户分配 |
| 机器人4 | 1903777125 | 用户分配 |
| 机器人5 | 1903781933 | 用户分配 |

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

## 后续优化建议

1. **完全复用 RobotService**: 创建 `TelegramUpdateAdapter` 将 QQ 消息转为 Telegram Update
2. **图片发送**: QQ 需要先将图片上传到图床，再发送 URL
3. **群白名单**: 添加配置 `qqbot.allowedGroups` 限制响应群组
4. **用户绑定**: QQ 用户 ID 和 Telegram 用户 ID 映射
5. **消息限流**: QQ API 有频率限制，可添加限流器
