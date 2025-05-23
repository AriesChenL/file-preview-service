FROM openjdk:11-jre-slim

# 安装LibreOffice
RUN apt-get update && \
    apt-get install -y libreoffice && \
    rm -rf /var/lib/apt/lists/*

COPY target/file-preview-service.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]