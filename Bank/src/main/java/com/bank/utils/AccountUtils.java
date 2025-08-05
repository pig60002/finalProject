package com.bank.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

public class AccountUtils {
			// 取得當天日期
			public static String getTodayCode() {
				return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			}
			
			// 處理檔案照片 回傳路徑字串
			public String savePicture(MultipartFile m , Integer mid){
				/*
				 *  System是Java標準類別 可以取得系統層級的資訊或輸入輸出
				 *  getProperty( ) 靜態方法，取得指定的系統屬性
				 *  "user.dir" 系統屬性名稱，代表工作目錄路徑
				 * */
				
				// 取得目前執行目錄
				String userDir = System.getProperty("user.dir");
				String userFolder = "accountImg";  // 假設這裡是要存 user1 的資料夾，可改成變數或參數
				// 使用 Paths 建構跨平台的資料夾路徑 .get() 會自動處理路徑分隔符，確保 Linux/Mac/Windows 都適用
		        Path uploadDir = Paths.get(userDir, "uploadImg", userFolder);
				
		        // 如果資料夾不存在，則建立
		        if (!Files.exists(uploadDir)) {
		            try {
						Files.createDirectories(uploadDir);
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		       
		        // 取得檔案副檔名
		        String originalFilename = m.getOriginalFilename();
		        if(originalFilename == null || originalFilename.isEmpty()) {
		        	return null;
		        }
		        
		        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		        
		        // 產生日期時間
		        /*
		         * DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")「時間格式化器」
		         * 把 LocalDateTime 格式化成你要的樣子。
		         * .format( ) 把 LocalDateTime 依照 DateTimeFormatter 的格式轉成字串。
		         * */
		        String timeStamp = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
		        																  .format(LocalDateTime.now());
		        
		        String newFileName = mid + "_"+ timeStamp + extension;
		        
		        // 寫入檔案
		        /*
		         *  resolve(newFileName) 是幫你在 uploadDir 底下補上新的檔名，組成完整路徑。
		         *  ex. targetPath = /Users/你名子/upload-images/MID123_20250728132059.jpg
		         *  m.transferTo(...) 是 Spring 提供的方法，把上傳的 MultipartFile 實體寫進硬碟中指定的位置。
		         *  targetPath.toFile() 是把 Path 轉成 File 物件。
		         * */
		        Path targetPath = uploadDir.resolve(newFileName);
		        System.out.println("完整儲存路徑：" + targetPath.toAbsolutePath());
		        try {
					m.transferTo(targetPath.toFile());
				} catch (IllegalStateException | IOException e) {
					e.printStackTrace();
					return null;
				}
		        
		        return newFileName ;
			}
}
