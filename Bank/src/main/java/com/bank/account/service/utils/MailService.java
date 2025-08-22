package com.bank.account.service.utils;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUser; // 寄件者帳號（Gmail 會以此為 from）

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /** 舊呼叫相容：沒有補件連結時直接呼叫這個 
     * @throws IllegalAccessException */
    public void sendApplicationRSEmail(String nameMasked, String to, String status)
            throws UnsupportedEncodingException, MessagingException, IllegalAccessException {
        sendApplicationRSEmail(nameMasked, to, status, null);
    }
    
    public void sendApplicationRSEmail(String mName, String mEmail, String status,@Nullable String supplementUrl) throws MessagingException, UnsupportedEncodingException, IllegalAccessException {
    	String subject = "審核結果通知";
    	
    	if(Set.of("通過","未通過","待補件").contains(subject)) {
    		throw new IllegalAccessException("不支援的狀態：" + status);
    	}
    	
    	String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    	String message = defaultMessageByStatus(status);
    	
    	boolean showCta = "待補件".equals(status) && supplementUrl != null && !supplementUrl.isBlank();
    	String ctaBlock = showCta ? """
    	          <div style="margin-top:12px">
    	            <a href="%s" target="_blank"
    	               style="display:inline-block;background:#16a34a;color:#ffffff;text-decoration:none;
    	                      padding:10px 16px;border-radius:8px;font-weight:600">
    	              前往補件
    	            </a>
    	            <div style="font-size:12px;color:#6b7280;margin-top:6px;word-break:break-all">
    	              若按鈕無法開啟，請複製此連結至瀏覽器：%s
    	            </div>
    	          </div>
    	        """.formatted(e(supplementUrl), e(supplementUrl)) : "";
    
    	// 純文字備援
    	String text = """
                【帳戶申請審核結果通知】
                親愛的 %s 您好：
                審核結果：%s
                通知時間：%s

                %s

                %s

                本信為系統通知，請勿直接回覆。
                """.formatted(mName, status, now, message,
                    showCta ? ("補件連結：" + supplementUrl) : "");

            // HTML（跟你之前的版型、含小 logo）
            String html = """
              <div style="background:#f5f7fb;padding:24px 12px;
                          font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Noto Sans TC','Microsoft JhengHei',Arial,sans-serif;color:#111827;">
                <table role="presentation" align="center" width="600"
                       style="max-width:600px;background:#fff;border:1px solid #e5e7eb;border-radius:12px">
                  <tr><td style="padding:16px 20px">
                    <img src="cid:logo" alt="YuzuBank" style="height:32px;width:auto;display:block">
                  </td></tr>

                  <tr><td style="padding:0 20px">
                    <h2 style="margin:0 0 8px 0;font-size:20px;line-height:28px">【帳戶申請審核結果通知】</h2>
                    <p style="margin:0 0 12px 0;line-height:1.7">
                      親愛的 %s 您好：您的申請已完成審核，結果如下。
                    </p>
                  </td></tr>

                  <tr><td style="padding:6px 20px 0 20px">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0"
                           style="border:1px solid #e5e7eb;border-radius:8px">
                      <tr>
                        <td style="width:28%%;background:#f9fafb;padding:10px 12px;border-bottom:1px solid #e5e7eb">審核結果</td>
                        <td style="padding:10px 12px;border-bottom:1px solid #e5e7eb"><b>%s</b></td>
                      </tr>
                      <tr>
                        <td style="background:#f9fafb;padding:10px 12px">通知時間</td>
                        <td style="padding:10px 12px">%s</td>
                      </tr>
                    </table>

                    <div style="margin-top:12px;line-height:1.7">%s</div>
                    %s
                  </td></tr>

                  <tr><td style="padding:18px 20px 22px 20px;color:#6b7280;font-size:12px;text-align:center;border-top:1px solid #e5e7eb;margin-top:14px">
                    柚子銀行股份有限公司　敬啟
                  </td></tr>
                </table>
              </div>
            """.formatted(e(mName), e(status), e(now), e(message), ctaBlock);

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,"UTF-8");
            helper.setFrom(mailUser, "柚子銀行 YuzuBank");
            helper.setTo(mEmail);
            helper.setSubject("【帳戶申請審核結果通知】" + status);
            helper.setText(text, html);
            
            helper.addInline("logo", new ClassPathResource("logo_white-160-blackbg.png"),"image/png");
            mailSender.send(mime);
    }

    
	private static String e(String s) {
		return HtmlUtils.htmlEscape(s == null ? "-" : s);
	}

	private String defaultMessageByStatus(String status) {
		
		Map<String, String> map = Map.of(
				"通過",   "恭喜您，申請已通過。後續帳務與權益將依本行規範提供，如需協助請與客服聯繫。",
	            "未通過", "很抱歉，本次申請未符合本行審核標準；如需協助或了解詳情，請聯繫客服。",
	            "待補件", "為完成審核，請於 7 日內補齊指定資料，以利本行盡速處理。"
		);
		
		return map.getOrDefault(status, "");
	}
	
	public void sendApplicationSubmittedEmail(String mName, String mEmail, String appId) throws MessagingException, UnsupportedEncodingException {
		 final String now = LocalDateTime.now()
			        .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	
		// 純文字備援
		    String text = """
		            【帳戶申請已送出】
		            親愛的 %s 您好：
		            我們已收到您的資料，將於 5 個工作天完成審核。
		            申請編號：%s
		            通知時間：%s

		            注意事項：
		            ・請勿重複送出申請，避免申請編號混淆。
		            ・審核結果將以 Email 通知；若需補件會另行通知。
		            ・如對申請有疑問，請洽客服。

		            本信為系統通知，請勿直接回覆。
		            """.formatted(mName, appId , now);
		 // HTML 內容（和 Step3 風格一致）
		    String html = """
		        <div style="background:#f5f7fb;padding:24px 12px;
		                    font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Noto Sans TC','Microsoft JhengHei',Arial,sans-serif;color:#111827;">
		          <table role="presentation" align="center" width="600"
		                 style="max-width:600px;background:#fff;border:1px solid #e5e7eb;border-radius:12px">
		            <tr><td style="padding:16px 20px">
		              <img src="cid:logo" alt="YuzuBank" style="height:32px;width:auto;display:block">
		            </td></tr>

		            <tr><td style="padding:0 20px">
		              <h2 style="margin:0 0 8px 0;font-size:20px;line-height:28px">【帳戶申請已送出】</h2>
		              <p style="margin:0 0 12px 0;line-height:1.7">
		                親愛的 %s 您好：我們已收到您的資料，審核將於 <b>5 個工作天</b> 完成。
		              </p>
		            </td></tr>

		            <tr><td style="padding:6px 20px 0 20px">
		              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0"
		                     style="border:1px solid #e5e7eb;border-radius:8px">
		                <tr>
		                  <td style="width:28%%;background:#f9fafb;padding:10px 12px;border-bottom:1px solid #e5e7eb">申請編號</td>
		                  <td style="padding:10px 12px;border-bottom:1px solid #e5e7eb"><code style="background:#f0f3f6;padding:2px 6px;border-radius:6px">%s</code></td>
		                </tr>
		                <tr>
		                  <td style="background:#f9fafb;padding:10px 12px">狀態</td>
		                  <td style="padding:10px 12px"><b>已送出，審核中</b></td>
		                </tr>
		                <tr>
		                  <td style="background:#f9fafb;padding:10px 12px">通知時間</td>
		                  <td style="padding:10px 12px">%s</td>
		                </tr>
		              </table>

		              <ul style="margin:12px 0 0 18px;line-height:1.7;color:#374151;padding:0">
		                <li>審核時間約 5 個工作天，結果將以 Email 通知。</li>
		                <li>請勿重複送出申請，避免申請編號混淆。</li>
		                <li>若需補件，將以 Email 告知補件內容與方式。</li>
		                <li>若申請未通過，將以 Email 告知原因。</li>
		              </ul>
		        
		            </td></tr>

		            <tr><td style="padding:18px 20px 22px 20px;color:#6b7280;font-size:12px;text-align:center;border-top:1px solid #e5e7eb;margin-top:14px">
		              柚子銀行股份有限公司　敬啟
		            </td></tr>
		          </table>
		        </div>
		        """.formatted(
		            e(mName),
		            e(appId),
		            e(now)
		        );

		    MimeMessage mime = mailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(
		        mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

		    helper.setFrom(mailUser, "柚子銀行 YuzuBank");
		    helper.setTo(mEmail);
		    helper.setSubject("【帳戶申請已送出】");
		    helper.setText(text, html);

		    
		    helper.addInline("logo", new ClassPathResource("logo_white-160-blackbg.png"),"image/png");
		    mailSender.send(mime);
	}
   
}
