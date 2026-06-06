package com.ecommerce.project.services.impl;

import com.ecommerce.project.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        String filePath = path + File.separator + uniqueFileName;

        File dest = new File(filePath);
        dest.getParentFile().mkdirs();

        Files.copy(file.getInputStream(), Paths.get(filePath));
        return uniqueFileName;
    }
}
