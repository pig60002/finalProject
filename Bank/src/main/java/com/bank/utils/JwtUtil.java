package com.bank.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

	private static final SecretKey SECRET_KEY = Keys
			.hmacShaKeyFor("ThisIsMySuperSecretAndRidiculouslyLongUltraConfidentialToken".getBytes());
	private static final int EXPIRATION_IN_SECONDS = 60 * 60;

   
	public static String generateToken(String memberId) {
		return Jwts.builder() // 使用 builder 模式設定 token
				.subject(memberId) // 設定 token 主體(Subject)，通常存放使用者的 ID
				.issuedAt(new Date()) // 設定 token 發行時間
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_IN_SECONDS * 1000)) // 設定 token 到期日期
				.signWith(SECRET_KEY) // 使用密鑰對 token 進行簽名
				.compact(); // 生成 JWT token
	}
	
	//使用上面的方法
	public static String generateToken(Integer memberId) {
		return generateToken(memberId.toString());
	}
}