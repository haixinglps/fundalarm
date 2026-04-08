#!/bin/bash
# FundAlarm 编译部署脚本
# 日期: 2026-04-08

set -e

echo "=================================="
echo "FundAlarm 编译部署脚本"
echo "=================================="
echo ""

# 配置
PROJECT_DIR="/home/www/code/fundalarmcode"
TOMCAT_HOME="/home/www/tomcat/apache-tomcat-9.0.102"
TOMCAT_WEBAPPS="$TOMCAT_HOME/webapps"
BACKUP_DIR="$PROJECT_DIR/backup/$(date +%Y%m%d_%H%M%S)"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 1. 进入项目目录
cd "$PROJECT_DIR"
log_info "进入项目目录: $PROJECT_DIR"

# 2. 创建备份目录
mkdir -p "$BACKUP_DIR"
log_info "创建备份目录: $BACKUP_DIR"

# 3. 备份当前部署（如果存在）
if [ -d "$TOMCAT_WEBAPPS/ROOT" ]; then
    log_info "备份当前部署..."
    cp -r "$TOMCAT_WEBAPPS/ROOT" "$BACKUP_DIR/"
    log_info "备份完成"
fi

# 4. 编译 fundalarm-common 模块
log_info "========================================="
log_info "步骤 1/4: 编译 fundalarm-common 模块"
log_info "========================================="
cd "$PROJECT_DIR/fundalarm-common"
mvn clean install -DskipTests -q
if [ $? -eq 0 ]; then
    log_info "fundalarm-common 编译成功 ✓"
else
    log_error "fundalarm-common 编译失败 ✗"
    exit 1
fi

# 5. 编译 robotium-fundalarm-service 模块
log_info ""
log_info "========================================="
log_info "步骤 2/4: 编译 robotium-fundalarm-service 模块"
log_info "========================================="
cd "$PROJECT_DIR/robotium-fundalarm-service"
mvn clean package -DskipTests -q
if [ $? -eq 0 ]; then
    log_info "robotium-fundalarm-service 编译成功 ✓"
else
    log_error "robotium-fundalarm-service 编译失败 ✗"
    exit 1
fi

# 6. 检查WAR文件
WAR_FILE="$PROJECT_DIR/robotium-fundalarm-service/target/fundalarm-manager-service-1.0-SNAPSHOT.war"
if [ ! -f "$WAR_FILE" ]; then
    log_error "WAR文件未找到: $WAR_FILE"
    exit 1
fi

log_info ""
log_info "WAR文件信息:"
ls -lh "$WAR_FILE"

# 7. 停止Tomcat
log_info ""
log_info "========================================="
log_info "步骤 3/4: 停止 Tomcat"
log_info "========================================="

# 检查Tomcat是否运行
TOMCAT_PID=$(ps aux | grep tomcat | grep -v grep | grep apache-tomcat-9.0.102 | awk '{print $2}')
if [ -n "$TOMCAT_PID" ]; then
    log_info "找到Tomcat进程 PID: $TOMCAT_PID，正在停止..."
    kill -15 "$TOMCAT_PID" 2>/dev/null || true
    
    # 等待进程结束
    for i in {1..30}; do
        if ! ps -p "$TOMCAT_PID" > /dev/null 2>&1; then
            log_info "Tomcat 已停止"
            break
        fi
        sleep 1
    done
    
    # 强制终止
    if ps -p "$TOMCAT_PID" > /dev/null 2>&1; then
        log_warn "强制终止Tomcat进程..."
        kill -9 "$TOMCAT_PID" 2>/dev/null || true
    fi
else
    log_warn "Tomcat 未运行"
fi

# 8. 清理旧部署
cd "$TOMCAT_WEBAPPS"
log_info ""
log_info "========================================="
log_info "步骤 4/4: 部署新版本"
log_info "========================================="

if [ -d "ROOT" ]; then
    log_info "删除旧部署..."
    rm -rf ROOT
fi

if [ -f "ROOT.war" ]; then
    rm -f ROOT.war
fi

# 9. 复制WAR文件
cp "$WAR_FILE" "$TOMCAT_WEBAPPS/ROOT.war"
log_info "WAR文件已复制到 webapps/ROOT.war"

# 10. 启动Tomcat
log_info ""
log_info "启动 Tomcat..."
cd "$TOMCAT_HOME/bin"
chmod +x *.sh
./startup.sh

# 11. 等待启动
log_info ""
log_info "等待Tomcat启动 (约10秒)..."
sleep 10

# 12. 检查启动状态
TOMCAT_PID=$(ps aux | grep tomcat | grep -v grep | grep apache-tomcat-9.0.102 | awk '{print $2}')
if [ -n "$TOMCAT_PID" ]; then
    log_info ""
    log_info "========================================="
    log_info "部署成功！"
    log_info "========================================="
    log_info "Tomcat PID: $TOMCAT_PID"
    log_info "部署路径: $TOMCAT_WEBAPPS/ROOT"
    log_info "备份路径: $BACKUP_DIR"
    log_info ""
    log_info "部署时间: $(date '+%Y-%m-%d %H:%M:%S')"
    log_info "========================================="
else
    log_error ""
    log_error "========================================="
    log_error "Tomcat 启动失败，请检查日志"
    log_error "========================================="
    log_error "查看日志: $TOMCAT_HOME/logs/catalina.out"
    exit 1
fi

# 13. 显示关键文件时间戳（用于验证）
log_info ""
log_info "关键类文件时间戳验证:"
echo "----------------------------------------"
ls -la "$TOMCAT_WEBAPPS/ROOT/WEB-INF/classes/cn/exrick/manager/service/impl/FundServiceImpl.class" 2>/dev/null || echo "FundServiceImpl.class 未找到"
ls -la "$TOMCAT_WEBAPPS/ROOT/WEB-INF/classes/cn/exrick/manager/service/task/DailyProfitTManager.class" 2>/dev/null || echo "DailyProfitTManager.class 未找到"
ls -la "$TOMCAT_WEBAPPS/ROOT/WEB-INF/classes/cn/exrick/manager/service/task/FundPriceUpdate2.class" 2>/dev/null || echo "FundPriceUpdate2.class 未找到"
echo "----------------------------------------"

echo ""
echo "部署完成！"
