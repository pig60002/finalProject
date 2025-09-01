package com.bank.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String to, String resetLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("【柚子銀行】重新設定您的密碼");

        // HTML 內容
        String htmlContent = """
            <html>
              <body style="font-family: Arial, sans-serif; color: #333;">
                <h2 style="color: #2E86C1;">親愛的用戶您好，</h2>
                <p>我們收到了您要求重設密碼的請求。</p>
                <p>請點擊以下按鈕來重設您的密碼：15分鐘後失效</p>
                <p style="margin: 20px 0;">
                  <a href='%s' 
                     style="background: #2E86C1; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                     🔑 重新設定密碼
                  </a>
                </p>
                <p>如果您沒有提出此請求，請忽略這封郵件。</p>
                <hr>
                <p style="font-size: 12px; color: #888;">這是一封系統自動寄送的郵件，請勿直接回覆。</p>
              </body>
            </html>
            """.formatted(resetLink);

        helper.setText(htmlContent, true); // 第二個參數 true 表示啟用 HTML

        mailSender.send(message);
    }
}