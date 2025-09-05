package com.bank.loan.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileDownloadController {

//    private final String uploadDir = "C:/bankSpringBoot/Bank/uploadImg/loanImg/";
    private final String uploadDir = "uploadImg/loanImg/";

    @GetMapping("/download/loanImg/{filename:.+}")
    public ResponseEntity<Resource> downloadLoanImage(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
    
    @GetMapping("/download/contract/{filename:.+}")
    public ResponseEntity<Resource> downloadContractFile(@PathVariable String filename) throws MalformedURLException {
//        String contractDir = "C:/bankSpringBoot/Bank/uploadImg/contract/";
        String contractDir = "uploadImg/loanImg/";
        Path filePath = Paths.get(contractDir).resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 例如從檔名擷取 loanId（假設格式是 loanId_timestamp.ext）
        String loanId = filename.split("_")[0];
        String downloadName = loanId + "_contract" + getFileExtension(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadName + "\"")
                .body(resource);
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex) : "";
    }

}
