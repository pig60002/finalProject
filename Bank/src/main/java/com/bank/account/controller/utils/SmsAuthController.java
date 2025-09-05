package com.bank.account.controller.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.service.utils.OtpService;

@RestController
public class SmsAuthController {

	@Autowired
	private OtpService otpService;
	
	@PostMapping("/account/send")
	public ResponseEntity<?> send(@RequestBody Map<String, String> body){
		
		String phone = body.get("phone");
		
		if(phone == null || !phone.matches("^09\\d{8}")) {
			return ResponseEntity.badRequest().body(Map.of("error","手機格式錯誤"));
		}
		
		try {
			
			otpService.sendOtp(phone);
			return ResponseEntity.ok(Map.of("status","sent"));
		
		} catch (com.twilio.exception.ApiException e) {
			e.printStackTrace();
		    return ResponseEntity.status(400).body(Map.of(
		      "error", "twilio_api_error",
		      "code", e.getCode(),                 // 例如 21408 / 21211 ...
		      "message", e.getMessage(),           // 人類可讀訊息
		      "moreInfo", e.getMoreInfo()          // 官方說明頁 URL	
		    ));
		} catch (Exception e) {
		    e.printStackTrace();
		    return ResponseEntity.status(500).body(Map.of("error", "server_error", "message", e.getMessage()));
		  }
	}
	
	@PostMapping("/account/verify")
	public ResponseEntity<?> verify(@RequestBody Map<String, String> body){
		
		String phone = body.get("phone");
		String code = body.get("code");
		
		if(phone == null || code == null) {
			return ResponseEntity.badRequest().body(Map.of("error","缺少參數"));			
		}
		
		try {
		
			boolean passed = otpService.checkOtp(phone, code);
			return ResponseEntity.ok(Map.of("passed",passed));

		} catch (Exception e) {
			
			return ResponseEntity.badRequest().body(Map.of("error","驗證失敗"));			
		}
 	}
	
}
