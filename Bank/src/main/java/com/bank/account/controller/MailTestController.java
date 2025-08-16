package com.bank.account.controller;

import jakarta.mail.MessagingException;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.account.service.MailService;

@RestController
public class MailTestController {

	@Autowired
	private  MailService mailService;

	@GetMapping("/mail")
	public ResponseEntity<String> testSend( @RequestParam String to,
								            @RequestParam(defaultValue = "測試寄信") String subject,
								            @RequestParam(defaultValue = "哈囉，這是測試信～") String text){
		mailService.sendText(to, subject, text);
		return ResponseEntity.ok("OK");
	}
	
	@PostMapping("/mailhtml")
	public ResponseEntity<String> testSendHtml(@RequestParam String to,
								               @RequestParam(defaultValue = "HTML 測試信") String subject,
								               // 可選：附件的本機檔案路徑（例如 C:/temp/test.pdf）
								               @RequestPart(required = false) MultipartFile attach) throws MessagingException, IOException{
		 String html = """
		            <div style="font-family:system-ui,Segoe UI,Arial">
		              <h2>柚子銀行 - 測試 HTML 信</h2>
		              <p>這是一封 <b>HTML</b> 測試信，若看到粗體代表成功渲染。</p>
		            </div>
		            """;
		 
		 mailService.sendHTML(to, subject, html, attach);
		 return ResponseEntity.ok("OK");
		 
	}
	
//	public ResponseEntity<String> testSendHtmlImg(@RequestParam String to,
//												  @RequestParam String subject,
//												  @RequestParam String html,
//												  @RequestPart){
//		
//	}
	
	
}
