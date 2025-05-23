package com.lynn.filepreviewservice.service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.UUID;

@Service
public class PdfPreviewService implements FilePreviewService {

    @Override
    public String generatePreview(MultipartFile file, String outputPath) throws Exception {
        String imagePath = outputPath + "/" + UUID.randomUUID() + ".png";

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 150);

            Thumbnails.of(image)
                    .size(800, 600)
                    .outputFormat("png")
                    .toFile(imagePath);

            return imagePath;
        }
    }

    @Override
    public boolean isSupported(String fileExtension) {
        return "pdf".equalsIgnoreCase(fileExtension);
    }
}
