package com.bank.config;

import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebAppConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET","POST","PUT","DELETE")
			.allowedOrigins("*")
			.allowedHeaders("*")
			.allowCredentials(false);
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

       
		String realPath = "file:" + Paths.get("uploadImg", "creditCardImg").toAbsolutePath().toString() + "/";

		 System.out.println("啟用圖片靜態資源映射！");

        registry.addResourceHandler("/uploadImg/creditCardImg/**")
                .addResourceLocations(realPath);
        System.out.println("realPath: " + realPath);
        // 第二個資料夾，映射到 /accountImg/user2/**
        registry.addResourceHandler("/uploadImg/accountImg/**")
                .addResourceLocations("file:C:/bankSpringBoot/Bank/uploadImg/accountImg/");
        
        registry.addResourceHandler("/uploadImg/memberImg/**")
        .addResourceLocations("file:C:/bankSpringBoot/Bank/uploadImg/memberImg/");
        // 貸款財力證明上傳靜態資源映射
        String basePath = System.getProperty("user.dir");
        String loanImgPath = "file:" + Paths.get(basePath, "uploadImg", "loanImg").toString() + "/";

        System.out.println("啟用 loanImg 靜態資源映射: " + loanImgPath);

        registry.addResourceHandler("/uploadImg/loanImg/**")
        .addResourceLocations(loanImgPath);
        
        // 貸款合約上傳靜態資源映射
        String contractPath = "file:" + Paths.get(basePath, "uploadImg", "contract").toString() + "/";
        System.out.println("啟用 contract 靜態資源映射: " + contractPath);

        registry.addResourceHandler("/uploadImg/contract/**")
                .addResourceLocations(contractPath);


    }
	
	

}
