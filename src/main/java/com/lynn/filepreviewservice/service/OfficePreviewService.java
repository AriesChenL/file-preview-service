package com.lynn.filepreviewservice.service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jodconverter.core.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.UUID;

@Service
public class OfficePreviewService implements FilePreviewService {

    @Autowired
    private DocumentConverter documentConverter;

    @Override
    public String generatePreview(MultipartFile file, String outputPath) {
        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName).toLowerCase();

        // 临时文件路径
        String tempDir = System.getProperty("java.io.tmpdir");
        String inputPath = tempDir + "/" + UUID.randomUUID() + "." + extension;
        String pdfPath = tempDir + "/" + UUID.randomUUID() + ".pdf";
        String imagePath = outputPath + "/" + UUID.randomUUID() + ".png";

        try {
            // 1. 保存上传的文件
            File inputFile = new File(inputPath);
            file.transferTo(inputFile);

            // 2. 转换为PDF
            File pdfFile = new File(pdfPath);
            documentConverter.convert(inputFile).to(pdfFile).execute();

            // 3. PDF转图片（第一页）
            generateImageFromPdf(pdfPath, imagePath);

            // 4. 清理临时文件
            inputFile.delete();
            pdfFile.delete();

            return imagePath;

        } catch (Exception e) {
            throw new RuntimeException("预览图生成失败", e);
        }
    }

    private void generateImageFromPdf(String pdfPath, String imagePath) throws Exception {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 150); // 第一页，150DPI

            // 生成缩略图
            Thumbnails.of(image)
                    .size(800, 600)
                    .outputFormat("png")
                    .toFile(imagePath);
        }
    }

    @Override
    public boolean isSupported(String fileExtension) {
        return Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx")
                .contains(fileExtension.toLowerCase());
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
