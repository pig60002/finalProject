package com.bank.config;

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
        // 第一個資料夾，映射到 /accountImg/user1/**
        registry.addResourceHandler("/uploadsImg/creditCardImg/**")
                .addResourceLocations("file:C:/bankSpringBoot/Bank/uploadsImg/creditCardImg/");

        // 第二個資料夾，映射到 /accountImg/user2/**
        registry.addResourceHandler("/accountImg/user2/**")
                .addResourceLocations("file:C:/bankSpringBoot/Bank/accountImg/user2/");
    }
	
=======
        registry.addResourceHandler("/uploadImg/creditCardImg/**")
                .addResourceLocations("file:C:/bankSpringBoot/Bank/uploadImg/creditCardImg/");

        // 第二個資料夾，映射到 /accountImg/user2/**
        registry.addResourceHandler("/uploadImg/accountImg/**")
                .addResourceLocations("file:C:/bankSpringBoot/Bank/uploadImg/accountImg/");
    }
	
	
>>>>>>> master
}
