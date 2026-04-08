#!/bin/bash
# QQ Bot 代码验证脚本

echo "=================================="
echo "QQ Bot 集成代码验证"
echo "=================================="
echo ""

cd /home/www/code/fundalarmcode

# 1. 检查新增文件
echo "[1/4] 检查新增文件..."
files=(
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotClient.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotConfiguration.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotMessage.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotMessageProcessor.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotAsyncMessageProcessor.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotRealDataProcessor.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotReplyContext.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQBotReplyService.java"
    "robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/qq/QQMessageHandler.java"
    "fundalarm-common/src/main/java/cn/exrick/manager/dto/SearchResultDTO.java"
)

all_exist=true
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "  ✓ $file"
    else
        echo "  ✗ $file (缺失)"
        all_exist=false
    fi
done

echo ""
echo "[2/4] 检查原有文件是否被修改..."

# 检查 RobotService.java 是否被修改
if grep -q "dealSearch.*QQBot" robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/impl/RobotServiceImpl.java 2>/dev/null; then
    echo "  ✗ RobotServiceImpl.java 被意外修改"
else
    echo "  ✓ RobotServiceImpl.java 未被修改"
fi

if grep -q "QQBot" robotium-fundalarm-service/src/main/java/cn/exrick/manager/service/tg/TelegramChannelMonitor.java 2>/dev/null; then
    echo "  ✗ TelegramChannelMonitor.java 被意外修改"
else
    echo "  ✓ TelegramChannelMonitor.java 未被修改"
fi

echo ""
echo "[3/4] 检查依赖..."

# 检查 pom.xml 依赖
if grep -q "Java-WebSocket" robotium-fundalarm-service/pom.xml; then
    echo "  ✓ Java-WebSocket 依赖已存在"
else
    echo "  ✗ Java-WebSocket 依赖缺失"
fi

if grep -q "okhttp" robotium-fundalarm-service/pom.xml; then
    echo "  ✓ OkHttp 依赖已存在"
else
    echo "  ✗ OkHttp 依赖缺失"
fi

if grep -q "kotlin-stdlib" robotium-fundalarm-service/pom.xml; then
    echo "  ✓ Kotlin 依赖已存在"
else
    echo "  ⚠ Kotlin 依赖缺失（OkHttp 4.x 需要）"
fi

if grep -q "gson" fundalarm-common/pom.xml; then
    echo "  ✓ Gson 依赖已存在 (fundalarm-common)"
else
    echo "  ✗ Gson 依赖缺失"
fi

echo ""
echo "[4/4] 编译测试..."
cd robotium-fundalarm-service

# 尝试编译 QQ Bot 相关文件
mvn compile -q 2>&1 | grep -E "(ERROR|error|cannot find symbol|BUILD)" | head -20

if [ $? -eq 0 ]; then
    echo "  ⚠ 编译可能有错误，请检查日志"
else
    echo "  ✓ 编译成功 (或无错误)"
fi

echo ""
echo "=================================="
echo "验证完成"
echo "=================================="
echo ""
echo "使用说明:"
echo "1. 在 application.properties 中添加:"
echo "   qqbot.enabled=true"
echo "   qqbot.appId=你的AppId"
echo "   qqbot.clientSecret=你的ClientSecret"
echo ""
echo "2. 重新编译部署"
echo ""
echo "3. 查看日志确认 QQ Bot 启动:"
echo "   tail -f /var/log/tomcat*/catalina.out | grep 'QQBot'"
