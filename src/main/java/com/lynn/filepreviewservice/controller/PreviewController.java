package com.lynn.filepreviewservice.controller;

import com.lynn.filepreviewservice.service.PreviewServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/preview")
public class PreviewController {

    @Autowired
    private PreviewServiceManager previewServiceManager;

    @PostMapping("/generate")
    public ResponseEntity<?> generatePreview(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
            }

            String previewPath = previewServiceManager.generatePreview(file);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("previewUrl", "/api/preview/image/" + new File(previewPath).getName());
            result.put("message", "预览图生成成功");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "预览图生成失败: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getPreviewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("previews").resolve(imageName);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
