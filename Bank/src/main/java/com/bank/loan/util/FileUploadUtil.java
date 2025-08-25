package com.bank.loan.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    // 取得基底目錄，固定為專案根目錄下的 uploadImg
    private static final String BASE_UPLOAD_DIR = System.getProperty("user.dir") + "/uploadImg";

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

        // 判斷uploadDir是否為相對路徑（沒有以 C:/ 或 / 開頭），拼成絕對路徑
        Path basePath = Paths.get(BASE_UPLOAD_DIR);
        Path uploadPath = Paths.get(uploadDir);
        Path targetPath;

        if (uploadPath.isAbsolute()) {
            targetPath = uploadPath.resolve(newFileName);
        } else {
            targetPath = basePath.resolve(uploadPath).resolve(newFileName);
        }

        Files.createDirectories(targetPath.getParent());
        file.transferTo(targetPath.toFile());
        System.out.println("/" + uploadDir.replace("\\", "/") + "/" + newFileName);

        // 回傳可用於前端的相對路徑，方便瀏覽器存取
        return "/" + uploadDir.replace("\\", "/") + "/" + newFileName;
    }
}
