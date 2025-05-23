# 使用 OpenJDK 21
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 设置非交互模式和时区
ENV DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Shanghai

# 更新包管理器并安装软件 - 注意这里的换行
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y --no-install-recommends \
        libreoffice \
        fonts-dejavu-core \
        fonts-liberation \
        fonts-noto-cjk \
        curl \
        ca-certificates && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* && \
    rm -rf /tmp/* /var/tmp/*

# 创建预览图存储目录 - 这是单独的RUN命令
RUN mkdir -p /app/previews

# 复制jar包
COPY target/file-preview-service-*.jar app.jar

# 设置环境变量
ENV JAVA_OPTS="-Xmx1024m -Xms512m -XX:+UseZGC -XX:+UseContainerSupport"
ENV SPRING_PROFILES_ACTIVE=docker

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/preview/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]