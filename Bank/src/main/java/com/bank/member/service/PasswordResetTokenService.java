package com.bank.member.service;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bank.member.bean.Member;
import com.bank.member.bean.PasswordResetToken;
import com.bank.member.dao.PasswordResetTokenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PasswordResetTokenService {
	
	private PasswordResetTokenRepository prtRepos;
	
	public PasswordResetToken insertPasswordResetToken(PasswordResetToken passwordResetToken) {
		String token = UUID.randomUUID().toString();
		passwordResetToken.setToken(token);
	    return prtRepos.save(passwordResetToken);
	}
	
	
}
