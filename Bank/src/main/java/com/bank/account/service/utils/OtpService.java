package com.bank.account.service.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.config.TwilioConfig;
import com.bank.utils.AccountUtils;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@Service
public class OtpService {

	@Autowired
	private TwilioConfig cfg;
	
	
	 /** 發送 OTP（會自動產生/管理驗證碼） */
	public void sendOtp(String localTwPhone) {
		AccountUtils accountUtils = new AccountUtils();
		String e164 = accountUtils.phoneUtil(localTwPhone);
		/* Verification.creator(...)  Twilio 提供的 SDK 方法，用來「發送驗證碼」給某個號碼
		 * .create()真正送出請求  Twilio 自己負責產生/儲存 OTP */
		com.twilio.rest.verify.v2.service.Verification
			.creator(cfg.getVerifyServiceSid(), e164, "sms")
			.create();
	}
	
	/** 驗證 OTP，正確回 true */
	public boolean checkOtp(String localTwPhone, String code) {
		AccountUtils accountUtils = new AccountUtils();
		String e164 = accountUtils.phoneUtil(localTwPhone);
		/*
		 * VerificationCheck.creator(...)  檢查使用者輸入的驗證碼
		 * cfg.getVerifyServiceSid()→ 同一個 Verify Service（必須和發送時一致）。
		 * .setTo(e164) → 驗證的目標號碼。  .setCode(code) → 使用者輸入的 6 碼 OTP。
		 * 送出請求到 Twilio → Twilio 會去比對該號碼最近一次發送的 OTP。
		 * verificationCheck.getStatus() Twilio 回傳的狀態字串，常見值："pending" → 發送過，但還沒驗證。
		 * "approved" → 驗證成功。"canceled" → 驗證取消（很少用）。
		 */
		VerificationCheck verificationCheck = com.twilio.rest.verify.v2.service.VerificationCheck
			.creator(cfg.getVerifyServiceSid())
			.setTo(e164)
			.setCode(code)
			.create();
		
		return "approved".equalsIgnoreCase(verificationCheck.getStatus());
	}
}
