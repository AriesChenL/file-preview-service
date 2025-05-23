package com.lynn.filepreviewservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FilePreviewService {
    /**
     * 生成文件预览图
     * @param file 文件
     * @param outputPath 输出路径
     * @return 预览图路径
     */
    String generatePreview(MultipartFile file, String outputPath) throws Exception;

    /**
     * 支持的文件类型
     */
    boolean isSupported(String fileExtension);
}