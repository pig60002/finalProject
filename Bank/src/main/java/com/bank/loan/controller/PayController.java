package com.bank.loan.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bank.loan.dto.LineRequestParam;
import com.bank.loan.dto.LineRequestParam.Prdocut;
import com.bank.loan.dto.LineRequestParam.Package;
import com.bank.loan.dto.LineRequestParam.RedirectUrls;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@RequestMapping("/api/pay")
@RestController
public class PayController {

	private final String CHANNEL_SECRET = "486fac3bc391d673f7bc1b10b72ed189"; // 改成自己的
	private final String API_PATH = "/v3/payments/request";

	/**
	 * 告訴我訂單號碼，我給你結帳網址
	 */
	@GetMapping(path = "/order/{orderId}") // 這邊要跟前端串接
	public void getOrderPaymentURLById(String orderId, HttpServletResponse response) throws Exception {

		// === 每次請求的唯一識別 ===
		UUID randomUUID = UUID.randomUUID();

		// === 建立請求 body ===
		LineRequestParam param = new LineRequestParam();
		param.setAmount(5000);
		param.setOrderId(1001);

		// 設定 URL
		RedirectUrls redirectUrls = new LineRequestParam.RedirectUrls();
		redirectUrls.setConfirmUrl("localhost:5173/view/order"); // 結帳成功後導向 (也要跟前端串接)
		redirectUrls.setCancelUrl("localhost:8080/cancel"); // 點擊取消後導向
		param.setRedirectUrls(redirectUrls);

		// 設定商品
		Prdocut p1 = new LineRequestParam.Prdocut();
		p1.setId("p1");
		p1.setImageUrl("");
		p1.setName("商品A");
		p1.setPrice(500);
		p1.setQuantity(5);

		Prdocut p2 = new LineRequestParam.Prdocut();
		p2.setId("p2");
		p2.setImageUrl("");
		p2.setName("商品B");
		p2.setPrice(500);
		p2.setQuantity(5);

		List<Prdocut> products = List.of(p1, p2);

		// 設定 package
		Package package1 = new LineRequestParam.Package();
		package1.setId("package1");
		package1.setAmount(5000);
		package1.setName("商品組A");
		package1.setProducts(products);

		List<Package> packages = List.of(package1);
		param.setPackages(packages);

		// 把請求的「物件」轉型成 JSON 格式的字串
		ObjectMapper mapper = new ObjectMapper();
		String body = mapper.writeValueAsString(param);

		// 算出 mac 值，即是 request header 中的 「X-LINE-Authorization」
		String message = CHANNEL_SECRET + API_PATH + body + randomUUID;
		Mac mac = Mac.getInstance("HmacSHA256");
		SecretKeySpec keySpec = new SecretKeySpec(CHANNEL_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		mac.init(keySpec);
		byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

		// === 發送請求給 LINE ===
		RestTemplate template = new RestTemplate();
		String url = "https://sandbox-api-pay.line.me" + API_PATH; // 測試環境 URL

		// 設定 HTTP Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-LINE-Authorization", Base64.getEncoder().encodeToString(rawHmac));
		headers.set("X-LINE-Authorization-Nonce", randomUUID.toString());
		headers.set("X-LINE-ChannelId", "2007899434"); // 改成自己的

		// 包裝 RequestEntity
		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		// 發送 POST 請求
		ResponseEntity<String> reslt = template.postForEntity(url, entity, String.class);
		System.out.println(reslt.getBody());

		// 解析回應中的 付款網址
		JsonNode root = mapper.readTree(reslt.getBody());
		String paymentUrl = root.path("info").path("paymentUrl").path("web").asText();

		// 告訴使用者哪裡可以付款(請他的瀏覽器導向付款畫面)
		response.sendRedirect(paymentUrl);

	}
}


