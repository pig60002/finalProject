package com.bank.member.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Member;
import com.bank.member.bean.PasswordResetToken;
import com.bank.member.dao.PasswordResetTokenRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PasswordResetTokenService {
	
	@Autowired
	private PasswordResetTokenRepository prtRepos;
	
	public PasswordResetToken insertPasswordResetToken(PasswordResetToken passwordResetToken) {
	    return prtRepos.save(passwordResetToken);
	}
	public PasswordResetToken findToken(String token) {
		Optional<PasswordResetToken> op = prtRepos.findByToken(token);
		if(op.isPresent()) {
        	return op.get();
        }
        return null;
	}
	
	public void deleteById(Integer id) {
		prtRepos.deleteById(id);
	}
	
	
}
