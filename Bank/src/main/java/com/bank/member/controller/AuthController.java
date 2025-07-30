package com.bank.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bank.member.bean.Member;
import com.bank.member.bean.MemberDto;
import com.bank.member.service.MemberService;
import com.bank.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	  @Autowired
	  private JwtUtil jwtUtil;
	  @Autowired
	  private MemberService memberService;
	  
	  
	  @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
	        // 假設帳密驗證成功（這裡沒用資料庫，為了簡單）
		    Member member = memberService.getMemberByIdentity(login.mIdentity);
		    
		    if(member==null) {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("查無身分證");}
		  
	        if (member.getmAccount().equals(login.mAccount) && member.getmPassword().equals(login.mPassword)) {
	            String token = jwtUtil.generateToken(member.getmId()); // 模擬 userId 為 1001
	            MemberDto mDto = new MemberDto(member,token);
	            
	            return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(mDto);
	        }

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳密錯誤");
	    }

	    static class LoginRequest {
	    	public String mIdentity;
	    	public String mAccount;
	    	public String mPassword;
	    }

}
