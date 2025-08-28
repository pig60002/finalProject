package com.bank.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoanEmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Async
    public void sendReviewDecisionEmail(String to, String name, String decision, String notes) {
        String subject = "【柚子銀行】您的貸款審核結果通知";
        String content = getEmailTemplate(name, decision, notes); // HTML 內容中 img src 將使用 cid:logoImage

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);

            // 設定 HTML 內容
            helper.setText(content, true);

            // 將 logo 圖片從 resources 嵌入
            ClassPathResource logo = new ClassPathResource("logo_white.png"); // 放在 src/main/resources
            helper.addInline("logoImage", logo); // 這裡的 "logoImage" 就是 HTML 中的 cid

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("寄送 Email 失敗", e);
        }
    }
    
    private String getEmailTemplate(String name, String decision, String notes) {
        // 根據審核結果決定狀態樣式
        String statusClass = getStatusClass(decision);
        String defaultNotes = getDefaultNotes(decision);
        String finalNotes = (notes != null && !notes.trim().isEmpty()) ? notes : defaultNotes;
        String localizedDecision = getLocalizedDecision(decision);
        return "<!DOCTYPE html>" +
            "<html lang='zh-TW'>" +
            "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Yuzu Bank 貸款審核通知</title>" +
                "<style>" +
                    "* {" +
                        "margin: 0;" +
                        "padding: 0;" +
                        "box-sizing: border-box;" +
                    "}" +
                    
                    "body {" +
                        "font-family: 'Microsoft JhengHei', 'PingFang TC', sans-serif;" +
                        "line-height: 1.6;" +
                        "color: #222626;" +
                        "background-color: #f5f5f5;" +
                        "padding: 20px 10px;" +
                    "}" +
                    
                    ".email-container {" +
                        "max-width: 600px;" +
                        "margin: 0 auto;" +
                        "background: #ffffff;" +
                        "border-radius: 12px;" +
                        "box-shadow: 0 4px 20px rgba(34, 38, 38, 0.08);" +
                        "overflow: hidden;" +
                    "}" +
                    
                    ".header {" +
                        "background: linear-gradient(135deg, #222626 0%, #333838 100%);" +
                        "text-align: center;" +
                        "position: relative;" +
                    "}" +
                    
                    ".header::after {" +
                        "content: '';" +
                        "position: absolute;" +
                        "bottom: 0;" +
                        "left: 0;" +
                        "right: 0;" +
                        "height: 4px;" +
                        "background: linear-gradient(90deg, #EBB211 0%, #CE1465 100%);" +
                    "}" +
                    
                    ".logo {" +
                        "width: 160px;" +
                        "height: auto;" +
                    "}" +
                    
                    ".content {" +
                        "padding: 40px 30px;" +
                    "}" +
                    
                    ".greeting {" +
                        "margin-bottom: 30px;" +
                    "}" +
                    
                    ".greeting h2 {" +
                        "color: #222626;" +
                        "font-size: 20px;" +
                        "font-weight: 500;" +
                        "margin-bottom: 15px;" +
                    "}" +
                    
                    ".greeting p {" +
                        "color: #666;" +
                        "font-size: 16px;" +
                    "}" +
                    
                    ".result-section {" +
                        "background: #f8f9fa;" +
                        "border-radius: 10px;" +
                        "padding: 25px;" +
                        "margin: 30px 0;" +
                    "}" +
                    
                    ".result-label {" +
                        "font-size: 16px;" +
                        "color: #666;" +
                        "margin-bottom: 10px;" +
                        "font-weight: 500;" +
                    "}" +
                    
                    ".result-status {" +
                        "display: flex;" +
                        "align-items: center;" +
                        "margin-bottom: 20px;" +
                    "}" +
                    
                    ".status-approved .status-text {" +
	                    "color: #EBB211;" +
	                    "background: rgba(235, 178, 17, 0.1);" +
	                    "padding: 8px 16px;" +
	                    "border-radius: 6px;" +
	                    "border: 1px solid rgba(235, 178, 17, 0.3);" +
                    "}" +
                    
                    ".status-rejected .status-text {" +
                        "color: #CE1465;" +
                        "background: rgba(206, 20, 101, 0.1);" +
                        "padding: 8px 16px;" +
                        "border-radius: 6px;" +
                        "border: 1px solid rgba(206, 20, 101, 0.3);" +
                    "}" +
                    
                    ".status-pending .status-text {" +
                        "color: #EBB211;" +
                        "background: rgba(235, 178, 17, 0.1);" +
                        "padding: 8px 16px;" +
                        "border-radius: 6px;" +
                        "border: 1px solid rgba(235, 178, 17, 0.3);" +
                    "}" +
                        
					".status-supplement .status-text {" +
                    "color: #28a745;" +
                    "background: rgba(40, 167, 69, 0.1);" +
                    "padding: 8px 16px;" +
                    "border-radius: 6px;" +
                    "border: 1px solid rgba(40, 167, 69, 0.3);" +
					"}" +
                    
                    ".status-text {" +
                        "font-size: 18px;" +
                        "font-weight: 600;" +
                        "color: #222626;" +
                    "}" +
                    
                    ".details-section {" +
                        "margin: 20px 0;" +
                    "}" +
                    
                    ".details-label {" +
                        "font-size: 16px;" +
                        "color: #666;" +
                        "margin-bottom: 10px;" +
                        "font-weight: 500;" +
                    "}" +
                    
                    ".details-content {" +
                        "background: #fff;" +
                        "padding: 15px;" +
                        "border-radius: 8px;" +
                        "border: 1px solid #e9ecef;" +
                        "color: #222626;" +
                        "line-height: 1.8;" +
                    "}" +
                    
                    ".action-section {" +
                        "margin: 35px 0;" +
                        "text-align: center;" +
                    "}" +
                    
                    ".action-button {" +
                        "display: inline-block;" +
                        "text-decoration: none;" +
                        "padding: 12px 32px;" +
                        "border-radius: 12px;" +
                        "font-weight: 500;" +
                        "font-size: 14px;" +
                        "letter-spacing: 0.5px;" +
                        "transition: all 0.2s ease;" +
                        "background-color: #EBB211;" +
                    "}" +
                    
                    ".action-button:hover {" +
                        "background-color: #dba611;" +
                        "color: #fff;" +
                    "}" +
                    
                    ".contact-info {" +
                        "background: #f8f9fa;" +
                        "padding: 25px;" +
                        "border-radius: 10px;" +
                        "margin-top: 30px;" +
                    "}" +
                    
                    ".contact-title {" +
                        "font-size: 16px;" +
                        "font-weight: 600;" +
                        "color: #222626;" +
                        "margin-bottom: 15px;" +
                    "}" +
                    
                    ".contact-details {" +
                        "color: #666;" +
                        "font-size: 14px;" +
                        "line-height: 1.8;" +
                    "}" +
                    
                    ".footer {" +
                        "background: #f8f9fa;" +
                        "padding: 20px 30px;" +
                        "text-align: center;" +
                        "border-top: 1px solid #e9ecef;" +
                    "}" +
                    
                    ".footer p {" +
                        "color: #999;" +
                        "font-size: 12px;" +
                        "margin: 5px 0;" +
                    "}" +
                    
                    "@media (max-width: 600px) {" +
                        ".email-container {" +
                            "margin: 0;" +
                            "border-radius: 0;" +
                        "}" +
                        ".header, .content, .footer {" +
                            "padding: 20px 15px;" +
                        "}" +
                        ".result-section {" +
                            "padding: 20px;" +
                            "margin: 20px 0;" +
                        "}" +
                        ".bank-name {" +
                            "font-size: 20px;" +
                        "}" +
                        ".greeting h2 {" +
                            "font-size: 18px;" +
                        "}" +
                    "}" +
                "</style>" +
            "</head>" +
            "<body>" +
                "<div class='email-container'>" +
                    "<div class='header'>" +
                        "<img src='cid:logoImage' alt='Yuzu Bank Logo' class='logo'/>\r\n"
                        + "" +
                    "</div>" +
                    
                    "<div class='content'>" +
                        "<div class='greeting'>" +
                            "<h2>親愛的 " + name + " 您好，</h2>" +
                            "<p>感謝您申請本行的貸款服務，我們已完成您的案件審核，詳細結果如下：</p>" +
                        "</div>" +
                        
                        "<div class='result-section'>" +
                            "<div class='result-label'>審核結果</div>" +
                            "<div class='result-status " + statusClass + "'>" +
                                "<div class='status-text'>" + localizedDecision + "</div>" +
                            "</div>" +
                            
                            "<div class='details-section'>" +
                                "<div class='details-label'>備註說明</div>" +
                                "<div class='details-content'>" + finalNotes + "</div>" +
                            "</div>" +
                        "</div>" +
                        
                        "<div class='action-section'>" +
                            "<a href='http://localhost:5173/yuzubank/loanHome' class='action-button' style='color: #fff;'>" +
                                "前往貸款首頁" +
                            "</a>" +
                        "</div>" +
                        
                        "<div class='contact-info'>" +
                            "<div class='contact-title'>客服中心</div>" +
                            "<div class='contact-details'>" +
                                "服務時間：週一至週五 09:00-18:00<br>" +
                                "客服專線：(02) 2345-6789<br>" +
                                "電子信箱：yuzubank202@gmail.com" +
                            "</div>" +
                        "</div>" +
                    "</div>" +
                    
                    "<div class='footer'>" +
                        "<p>此信件由系統自動發送，請勿直接回覆</p>" +
                        "<p>© 2025 Yuzu Bank. All rights reserved.</p>" +
                    "</div>" +
                "</div>" +
            "</body>" +
            "</html>";
    }
    
    /**
     * 審核狀態更改為中文
     */
    private String getLocalizedDecision(String decision) {
        if (decision == null) return "待審核";
        
        switch (decision.trim().toLowerCase()) {
            case "approved":
                return "審核通過";
            case "rejected":
                return "審核拒絕";
            case "pending":
                return "待審核";
            case "supplement":
                return "補件中";
            default:
                return decision; // 如果是中文就直接回傳
        }
    }

    
    /**
     * 根據審核結果決定狀態樣式
     */
    private String getStatusClass(String decision) {
        if (decision == null) return "status-pending";
        
        switch (decision.trim()) {
            case "審核通過":
            case "通過":
            case "approved":
                return "status-approved";
            case "審核拒絕":
            case "拒絕":
            case "rejected":
                return "status-rejected";
            case "待審核":
            case "審核中":
            case "pending":
                return "status-pending";
            case "補件中":
            case "supplement":
                return "status-supplement";
            default:
                return "status-pending";
        }
    }
    
    /**
     * 根據審核結果提供預設備註說明
     */
    private String getDefaultNotes(String decision) {
        if (decision == null) return "您的申請正在審核中，我們將盡快通知您結果。";
        
        switch (decision.trim()) {
            case "審核通過":
            case "通過":
            case "approved":
                return "恭喜您！您的貸款申請已獲得批准。我們的專員將在3個工作日內與您聯繫，協助您完成後續的對保及撥款程序。";
            case "審核拒絕":
            case "拒絕":
            case "rejected":
                return "很抱歉，經過審慎評估後，您的貸款申請未能通過審核。如有疑問，歡迎聯繫我們的客服中心了解詳情。";
            case "待審核":
            case "審核中":
            case "pending":
                return "您的申請正在審核中，我們將盡快通知您結果。審核期間如有需要補件，我們會主動與您聯繫。";
            default:
                return "感謝您的申請，如有任何疑問，請聯繫我們的客服中心。";
        }
    }
}