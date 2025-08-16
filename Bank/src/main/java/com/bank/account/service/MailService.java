package com.bank.account.service;

import java.io.File;
import java.io.IOException;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.common.lang.Nullable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	
	private final JavaMailSender mailSender;

	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	// 寄存文字信
	public void sendText(String to, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);			 // 收件地址
		msg.setSubject(subject); // 主旨
		msg.setText(text);		 // 內容
		msg.setFrom("柚子銀行 YuzuBank<yuzubank202@gmail.com>"); // 定義寄件名字
		mailSender.send(msg);
	}
	
	// 寄 HTML信 含附件
	public void sendHTML(String to, String subject, String html, @Nullable MultipartFile attach) throws MessagingException, IOException {
		MimeMessage mime = mailSender.createMimeMessage();
		 boolean hasAttach = attach != null && !attach.isEmpty();
		MimeMessageHelper helper = new MimeMessageHelper(mime, hasAttach, "UTF-8");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(html, true);
//		if(attach != null) {
//			//把 java.io.File 包成 Spring 的 Resource，好讓 Mail API 讀取檔案內容。
//			FileSystemResource res = new FileSystemResource(attach);
//			//把這個資源加成附件。第一個參數是寄出去時顯示的檔名。
//			// ⚠️ 前提：你的 MimeMessageHelper 必須是 multipart，否則無法加附件：
//			helper.addAttachment(res.getFilename(), res);
//		}
		if (hasAttach) {
	        helper.addAttachment(
	            attach.getOriginalFilename(),
	            new org.springframework.core.io.ByteArrayResource(attach.getBytes())
	        );
	    }
		
		mailSender.send(mime);
	}
	
	// 寄HTML信 + 內嵌圖片
	public void sendHTMLWithInline(String to, String subject, String html, File inlineImage, String contentId) throws MessagingException {
		MimeMessage mime = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(html,true);
		helper.addInline(contentId,inlineImage);
		mailSender.send(mime);
	}
	
}
