package com.bank.creditCard.transaction.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bank.creditCard.dto.CreditTransactionDTO;
import com.bank.creditCard.transaction.model.CreditTransactionBean;
import com.bank.creditCard.transaction.service.CreditTransactionService;
import com.bank.creditCard.transaction.service.PayPalApiClient;
import com.bank.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/transactionFront")
public class CreditTransactionFrontController {

    @Autowired
    private CreditTransactionService creditTransactionService;

    // 新增：與 PayPal 溝通用
    @Autowired
    private PayPalApiClient payPalApi;

    // 共用：從 Authorization 取 memberId
    private Integer getMemberIdFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("未登入");
        }
        String token = header.substring("Bearer ".length());
        return Integer.parseInt(JwtUtil.getSubject(token));
    }

    // =========================
    // A.（Card Fields 必備）拿 client token
    // =========================
    @PostMapping("/paypal/client-token")
    public Map<String, String> getClientToken() {
        String clientToken = payPalApi.generateClientToken();
        return Map.of("clientToken", clientToken);
    }

    // =========================
    // B. 建立 PayPal 訂單（Order）
    // =========================
    @PostMapping("/paypal/orders")
    public Map<String, String> createOrder(@RequestBody Map<String, Object> body) {
        String currency = String.valueOf(body.getOrDefault("currency", "TWD"));
        double amt = Double.parseDouble(String.valueOf(body.get("amount")));
        // PayPal 對 TWD 視為零小數 → 轉整數字串
        if ("TWD".equalsIgnoreCase(currency)) {
            amt = Math.round(amt);
        }
        String orderId = payPalApi.createOrder(currency, String.valueOf((long) amt));
        return Map.of("orderId", orderId);
    }

    // =========================
    // C. 核准後：capture + 寫入你自己的交易表
    // =========================
    @PostMapping("/paypal/capture-and-add")
    public CreditTransactionBean captureAndAdd(@RequestBody Map<String, Object> body,
                                               HttpServletRequest request) {
        Integer mId = getMemberIdFromRequest(request);

        String orderId = String.valueOf(body.get("orderId"));
        Integer cardId = Integer.parseInt(String.valueOf(body.get("cardId")));
        String merchantType = String.valueOf(body.getOrDefault("merchantType", "其他"));
        String description  = String.valueOf(body.getOrDefault("description", "PayPal CardFields 交易"));

        // 1) 後端向 PayPal 扣款
        Map<String, Object> cap = payPalApi.captureOrder(orderId);
        String status = String.valueOf(cap.get("status"));
        if (!"COMPLETED".equalsIgnoreCase(status)) {
            throw new RuntimeException("PayPal 付款未完成：" + status);
        }

        // 2) 從回應取金額/幣別（第一個 purchase_unit 的第一筆 capture）
        List<Map> pus = (List<Map>) cap.get("purchase_units");
        Map payments = (Map) pus.get(0).get("payments");
        List<Map> captures = (List<Map>) payments.get("captures");
        Map firstCapture = captures.get(0);
        Map amount = (Map) firstCapture.get("amount");

        String currency = String.valueOf(amount.get("currency_code"));
        String valueStr = String.valueOf(amount.get("value"));
        if (!"TWD".equalsIgnoreCase(currency)) {
            throw new RuntimeException("目前僅支援 TWD，取得：" + currency);
        }

        // 3) 入庫（金額用 PayPal 實際扣款回傳值）
        CreditTransactionDTO dto = new CreditTransactionDTO();
        dto.setMemberId(mId);
        dto.setCardId(cardId);
        dto.setMerchantType(merchantType);
        dto.setDescription(description);
        dto.setAmount(new BigDecimal(valueStr)); // TWD 無小數，安全起見仍用 BigDecimal

        return creditTransactionService.addTransaction(dto);
    }

    // =========================
    // 你原本的「純模擬新增」還想留就保留（可選）
    // =========================
    @PostMapping("/addTransaction")
    public CreditTransactionBean createTransaction(@RequestBody CreditTransactionDTO dto,
                                                   HttpServletRequest request) {
        Integer mId = getMemberIdFromRequest(request);
        dto.setMemberId(mId);
        // 不經 PayPal，直接寫 DB（留作本地模擬）
        return creditTransactionService.addTransaction(dto);
    }

    // =========================
    // 查詢：會員全部交易
    // =========================
    @GetMapping("/getTransactionByMember")
    public List<CreditTransactionBean> getTransactionByMember(HttpServletRequest request) {
        Integer mId = getMemberIdFromRequest(request);
        return creditTransactionService.getTransactionsByMemberId(mId);
    }

    // =========================
    // 查詢：指定卡片的交易
    // =========================
    @GetMapping("/getTransactionsByCard")
    public List<CreditTransactionBean> getTransactionsByCard(
            @RequestParam("cardId") Integer cardId,
            HttpServletRequest request) {
        Integer mId = getMemberIdFromRequest(request);
        return creditTransactionService.getTransactionsByCardIdAndMemberId(cardId, mId);
    }
}
