package com.bank.account.controller.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class CaptchaController {

	@GetMapping("/captcha")
	public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("image/png");
		
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		kaptcha.setConfig(new com.google.code.kaptcha.util.Config(new java.util.Properties()));
		
		// 1. 生成隨機文字
		String text = kaptcha.createText();
		
		// 2. 存到session
		request.getSession().setAttribute("captcha", text);
		
		// 3. 畫圖
		BufferedImage image = kaptcha.createImage(text);
		try {
			ImageIO.write(image, "png", response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@PostMapping("/verifyCaptcha")
	public ResponseEntity<?> verifyCaptcha(HttpServletRequest request, @RequestBody Map<String, String> body){
		String input = body.get("captcha");
		String expected = (String)request.getSession().getAttribute("captcha");
		
		if(expected != null && expected.equalsIgnoreCase(input)) {
			return ResponseEntity.ok(Map.of("passed",true));
		} else {
			return ResponseEntity.status(400).body(Map.of("error","驗證碼錯誤"));
		}
	}
	
}
