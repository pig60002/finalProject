package com.bank.loan.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

    // 預設上傳目錄
    public static final String UPLOAD_DIR = "C:/bankSpringBoot/Bank/uploadImg/loanImg/";

    /**
     * 儲存檔案並回傳相對路徑
     * @param loanId
     * @param file
     * @return 相對路徑，例如 /uploadImg/loanImg/loanId_timestamp_原始檔名
     * @throws IOException
     */
    public static String saveFile(String loanId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex); // 取得副檔名，例如 .jpg
        }
        String newFileName = loanId + "_" + System.currentTimeMillis() + extension;

        Path targetPath = Paths.get(UPLOAD_DIR).resolve(newFileName);
        Files.createDirectories(targetPath.getParent());

        file.transferTo(targetPath.toFile());

        return "/uploadImg/loanImg/" + newFileName;
    }
}
