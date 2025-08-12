package com.bank.loan.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    public static String saveFile(String loanId, MultipartFile file, String uploadDir) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        String newFileName = loanId + "_" + System.currentTimeMillis() + extension;

        Path targetPath = Paths.get(uploadDir).resolve(newFileName);
        Files.createDirectories(targetPath.getParent());

        file.transferTo(targetPath.toFile());

        return "/" + uploadDir.replace("\\", "/") + "/" + newFileName;
    }
}

