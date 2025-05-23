# file-preview-service

基于 Spring Boot 的文件预览服务，支持多种常见文件格式的在线预览与转换。

## 功能特性

- 支持 Office 文档（Word、Excel、PPT）在线预览
- 支持 PDF、图片等格式预览
- 文件格式自动转换（依赖 JodConverter、PDFBox、Thumbnailator 等）
- RESTful API 接口

## 技术栈

- Java 21
- Spring Boot 3.5.x
- JodConverter
- Apache PDFBox
- Thumbnailator

## 环境要求

- JDK 21 及以上
- Maven 3.8+（推荐使用项目自带的 `mvnw` 脚本）
- LibreOffice（用于 Office 文档转换，需本地安装）

## 快速开始

1. **克隆项目**
   ```bash
   git clone <repo-url>
   cd file-preview-service
   ```

2. **配置 LibreOffice 路径（如有需要）**
   - 可在 `application.properties` 中配置 `jodconverter.local.office-home`。

3. **构建并运行**
   ```bash
   ./mvnw clean package
   java -jar target/file-preview-service-0.0.1-SNAPSHOT.jar
   ```

4. **访问接口**
   - 默认服务端口：`8080`
   - 示例接口：`http://localhost:8080/preview?fileUrl=xxx`

## 目录结构

```
file-preview-service/
├── src/
│   └── main/
│       ├── java/com/lynn/filepreviewservice/   # 业务代码
│       └── resources/                          # 配置文件等
├── pom.xml                                     # Maven 配置
├── README.md
└── ...
```

## 贡献说明

欢迎提交 Issue 或 Pull Request，建议先提 Issue 讨论需求或问题。

## License

本项目采用 MIT License，详见 LICENSE 文件。
