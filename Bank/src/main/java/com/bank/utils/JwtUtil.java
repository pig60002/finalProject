package com.bank.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
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
	//解析token
	private static Claims getClaims(String token) {
		return Jwts.parser() // 使用 parser() 取得解析器
				.verifyWith(SECRET_KEY) // 設定解密用密鑰
				.build() // 建立解析器
				.parseSignedClaims(token) // 解析 token
				.getPayload(); // 取得解析後結果
	}
	//拿取token 
	public static String getSubject(String token) {
		return getClaims(token).getSubject();
	}

	/**
	 * 從 token 中取得自定義的 payload
	 */
	//拿取自訂義 payload的值
	public static String getValue(String token, String key) {
		return (String) getClaims(token).get(key);
	}
	
	//確認簽名是否正確
	public static Boolean isTokenValid(String token) {
		getClaims(token); // 若 token 有任何異常，則由 jjwt 套件直接拋出錯誤。

		return true; // 能走到回傳表示驗證通過，token 合法
	}

}