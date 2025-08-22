package com.bank.loan.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.bank.loan.bean.LoanPayment;
import com.bank.loan.bean.LoanRepaymentSchedule;
import com.bank.loan.dto.LineRequestParam;
import com.bank.loan.dto.LineRequestParam.RedirectUrls;
import com.bank.loan.service.LoanPaymentService;
import com.bank.loan.service.LoanRepaymentScheduleService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/pay")
@RestController
public class PayController {

    private final String CHANNEL_SECRET = "486fac3bc391d673f7bc1b10b72ed189"; // 改成自己的
    private final String API_PATH = "/v3/payments/request";

    @Autowired
    private LoanRepaymentScheduleService lrsService;

    @Autowired
    private LoanPaymentService lpService;

    /**
     * 取得 Line Pay 結帳網址
     */
    @GetMapping("/order/{loanId}")
    public ResponseEntity<Map<String, String>> getOrderPaymentURLById(@PathVariable String loanId) throws Exception {
        UUID randomUUID = UUID.randomUUID();

        // 取得下一期尚未繳款的排程
        LoanRepaymentSchedule schedule = lrsService.getNextPendingSchedule(loanId);
        if (schedule == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "沒有找到待繳費的期數"));
        }

        int amountDue = schedule.getAmountDue().intValue();

        LineRequestParam param = new LineRequestParam();
        param.setAmount(amountDue);
        param.setOrderId(loanId);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setConfirmUrl("http://localhost:8080/bank/pay/linepay-success/" + loanId);
        redirectUrls.setCancelUrl("http://localhost:8080/bank/pay/cancel/" + loanId);
        param.setRedirectUrls(redirectUrls);

        // 商品資訊
        LineRequestParam.Prdocut product = new LineRequestParam.Prdocut();
        product.setId("repay");
        product.setName("貸款繳款");
        product.setPrice(amountDue);
        product.setQuantity(1);

        LineRequestParam.Package package1 = new LineRequestParam.Package();
        package1.setId("package1");
        package1.setName("貸款繳費");
        package1.setAmount(amountDue);
        package1.setProducts(List.of(product));

        param.setPackages(List.of(package1));

        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(param);

        // 計算 MAC
        String message = CHANNEL_SECRET + API_PATH + body + randomUUID;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(CHANNEL_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        RestTemplate template = new RestTemplate();
        String url = "https://sandbox-api-pay.line.me" + API_PATH;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-Authorization", Base64.getEncoder().encodeToString(rawHmac));
        headers.set("X-LINE-Authorization-Nonce", randomUUID.toString());
        headers.set("X-LINE-ChannelId", "2007899434");

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> result = template.postForEntity(url, entity, String.class);

        JsonNode root = mapper.readTree(result.getBody());
        String paymentUrl = root.path("info").path("paymentUrl").path("web").asText();

        return ResponseEntity.ok(Map.of(
                "paymentUrl", paymentUrl,
                "amountDue", String.valueOf(amountDue),
                "scheduleId", String.valueOf(schedule.getScheduleId())
        ));
    }

    /**
     * Line Pay
     */
    @PostMapping("/linepay-confirm/{loanId}")
    public ResponseEntity<String> confirmLinePayPayment(@PathVariable String loanId, @RequestBody Map<String, Object> payload) {
        try {
            System.out.println("=== Line Pay 付款確認 ===");
            System.out.println("LoanId: " + loanId);
            System.out.println("Payload: " + payload);
            
            // 取得下一期待繳費排程
            LoanRepaymentSchedule nextSchedule = lrsService.getNextPendingSchedule(loanId);
            if (nextSchedule == null) {
                System.out.println("沒有找到待繳費的排程");
                return ResponseEntity.badRequest().body("沒有找到待繳費的排程");
            }
            
            // 建立付款記錄
            LoanPayment payment = new LoanPayment();
            payment.setLoanId(loanId);
            payment.setAmountPaid(nextSchedule.getAmountDue()); // 使用排程中的應繳金額
            payment.setPaymentMethod("Line Pay");
            payment.setPaymentReference("貸款 " + loanId + " Line Pay 繳款");
            payment.setPaymentDate(LocalDateTime.now());
            payment.setScheduleId(nextSchedule.getScheduleId());
            
            // 如果有交易 ID，也記錄下來
            if (payload.containsKey("transactionId")) {
                payment.setPaymentReference(payment.getPaymentReference() + " (交易ID: " + payload.get("transactionId") + ")");
            }
            
            // 保存付款記錄並更新排程
            LoanPayment savedPayment = lpService.savePaymentAndUpdateSchedule(payment);
            
            System.out.println("Line Pay 付款處理成功，付款ID: " + savedPayment.getPaymentId());
            return ResponseEntity.ok("付款確認成功");
            
        } catch (Exception e) {
            System.err.println("Line Pay 付款確認失敗: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("付款確認失敗: " + e.getMessage());
        }
    }

    /**
     * Line Pay 付款完成跳轉頁面
     */
    @GetMapping("/linepay-success/{loanId}")
    public RedirectView linePaySuccessPage(@PathVariable String loanId, 
                                         @RequestParam(required = false) String transactionId,
                                         @RequestParam(required = false) String orderId) {
        String redirectUrl = "/bank/linepay-success.html?loanId=" + loanId;
        
        if (transactionId != null) {
            redirectUrl += "&transactionId=" + transactionId;
        }
        if (orderId != null) {
            redirectUrl += "&orderId=" + orderId;
        }
        
        return new RedirectView(redirectUrl);
    }

}
