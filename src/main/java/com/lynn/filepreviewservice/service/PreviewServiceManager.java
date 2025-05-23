package com.lynn.filepreviewservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
public class PreviewServiceManager {

    private final List<FilePreviewService> previewServices;

    public PreviewServiceManager(List<FilePreviewService> previewServices) {
        this.previewServices = previewServices;
    }

    public String generatePreview(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName);

        FilePreviewService service = previewServices.stream()
                .filter(s -> s.isSupported(extension))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("不支持的文件类型: " + extension));

        String outputDir = "previews"; // 可配置
        createDirectoryIfNotExists(outputDir);

        return service.generatePreview(file, outputDir);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private void createDirectoryIfNotExists(String dir) {
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
