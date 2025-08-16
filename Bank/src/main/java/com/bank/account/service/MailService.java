package com.bank.account.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.lang.Nullable;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailUser; // 寄件者帳號（Gmail 會以此為 from）

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /** 審核結果通知：通過 / 未通過 / 待補件（待補件可帶補件連結） 
     * @throws UnsupportedEncodingException */
    public void sendReviewDecisionEmail(
            String to,
            String applicationId,
            String nameMasked,
            String status,                      // 僅允許：通過 / 未通過 / 待補件
            @Nullable String reason,            // 非通過時可帶原因
            @Nullable String supplementUrl      // 待補件時可帶 URL
    ) throws MessagingException, UnsupportedEncodingException {

        if (!Set.of("通過", "未通過", "待補件").contains(status)) {
            throw new IllegalArgumentException("不支援的狀態: " + status);
        }

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        String reasonBlock = ("通過".equals(status) || reason == null || reason.isBlank()) ? "" : """
          <div style="margin-top:8px;padding:12px;border:1px solid #e5e7eb;background:#f9fafb;border-radius:8px">
            <div style="font-weight:600;margin-bottom:4px">原因 / 說明：</div>
            <div>%s</div>
          </div>
        """.formatted(e(reason));

        boolean showCta = "待補件".equals(status) && supplementUrl != null && !supplementUrl.isBlank();
        String ctaBlock = showCta ? """
          <div style="margin-top:12px">
            <a href="%s" target="_blank"
               style="display:inline-block;background:#111827;color:#ffffff;text-decoration:none;
                      padding:10px 16px;border-radius:8px;font-weight:600">
              前往補件
            </a>
            <div style="font-size:12px;color:#6b7280;margin-top:6px;word-break:break-all">
              若按鈕無法開啟，請複製此連結至瀏覽器：%s
            </div>
          </div>
        """.formatted(e(supplementUrl), e(supplementUrl)) : "";

        String subject = "【帳戶申請審核結果通知】" + status + " - " + applicationId;

        // 文字方塊用 String#formatted，HTML 內若有 % 需寫成 %%（你已經有做）
        String html = """
          <div style="background:#f5f7fb;padding:24px 12px;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Noto Sans TC','Microsoft JhengHei',Arial,sans-serif;color:#111827;">
            <table role="presentation" align="center" width="600" style="max-width:600px;background:#fff;border:1px solid #e5e7eb;border-radius:12px">
              <tr><td style="padding:16px 20px">
                <img src="cid:logo" alt="YuzuBank" style="height:32px;width:auto;display:block">
              </td></tr>

              <tr><td style="padding:0 20px">
                <h2 style="margin:0 0 8px 0;font-size:20px;line-height:28px">【帳戶申請審核結果通知】</h2>
                <p style="margin:0 0 12px 0;line-height:1.7">
                  親愛的客戶 %s 您好：您的帳戶申請已完成審核，結果如下。
                </p>
              </td></tr>

              <tr><td style="padding:6px 20px 0 20px">
                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="border:1px solid #e5e7eb;border-radius:8px">
                  <tr>
                    <td style="width:28%%;background:#f9fafb;padding:10px 12px;border-bottom:1px solid #e5e7eb">申請編號</td>
                    <td style="padding:10px 12px;border-bottom:1px solid #e5e7eb">%s</td>
                  </tr>
                  <tr>
                    <td style="background:#f9fafb;padding:10px 12px;border-bottom:1px solid #e5e7eb">申請人</td>
                    <td style="padding:10px 12px;border-bottom:1px solid #e5e7eb">%s</td>
                  </tr>
                  <tr>
                    <td style="background:#f9fafb;padding:10px 12px;border-bottom:1px solid #e5e7eb">審核結果</td>
                    <td style="padding:10px 12px;border-bottom:1px solid #e5e7eb"><b>%s</b></td>
                  </tr>
                  <tr>
                    <td style="background:#f9fafb;padding:10px 12px">通知時間</td>
                    <td style="padding:10px 12px">%s</td>
                  </tr>
                </table>
                %s
                %s
              </td></tr>

              <tr><td style="padding:18px 20px 22px 20px;color:#6b7280;font-size:12px;text-align:center;border-top:1px solid #e5e7eb;margin-top:14px">
                柚子銀行股份有限公司　敬啟
              </td></tr>
            </table>
          </div>
        """.formatted(e(nameMasked), e(applicationId), e(nameMasked), e(status), e(now), reasonBlock, ctaBlock);

        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
        helper.setFrom(mailUser, "柚子銀行 YuzuBank"); // ← 顯示名稱 + 寄件帳號
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        helper.addInline("logo", new ClassPathResource("email/yuzubank-logo-160-green.png"), "image/png");
        mailSender.send(mime);
    }

    // HTML 轉義：用 Spring 的 HtmlUtils（也可改成你自己的 replace 版本）
    private static String e(String s) {
        return HtmlUtils.htmlEscape(s == null ? "-" : s);
    }
}
