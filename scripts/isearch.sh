#!/bin/bash
# Isearch 命令行搜索工具
# 用法: ./isearch.sh <关键词> [最大结果数]
# 示例: ./isearch.sh "芃芃 腋下" 50

set -e

KEYWORD="${1:-测试}"
MAX_RESULTS="${2:-100}"

# 项目路径
PROJECT_DIR="/home/www/code/fundalarmcode"
TOMCAT_WEBAPPS="/home/www/tomcat/apache-tomcat-9.0.102/webapps/ROOT"
JAVA21="/usr/lib/jvm/java-21-openjdk-amd64/bin/java"
JAVAC21="/usr/lib/jvm/java-21-openjdk-amd64/bin/javac"

# 构建 classpath
LIB_JARS=$(find "${TOMCAT_WEBAPPS}/WEB-INF/lib" -name "*.jar" ! -name "aspectjweaver*" | tr '\n' ':')
CLASSES_DIR="${TOMCAT_WEBAPPS}/WEB-INF/classes"
CP="${LIB_JARS}${CLASSES_DIR}:${PROJECT_DIR}"

# 编译（如需要）
if [ ! -f "${PROJECT_DIR}/scripts/SearchTest.class" ] || \
   [ "${PROJECT_DIR}/scripts/SearchTest.java" -nt "${PROJECT_DIR}/scripts/SearchTest.class" ]; then
    echo "[编译 SearchTest.java...]"
    ${JAVAC21} -cp "${CP}" "${PROJECT_DIR}/scripts/SearchTest.java"
fi

# 执行搜索
echo "[开始搜索...]"
${JAVA21} -cp "${CP}" scripts.SearchTest "${KEYWORD}" "${MAX_RESULTS}"
