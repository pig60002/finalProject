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
import com.bank.member.bean.Worker;
import com.bank.member.bean.WorkerDto;
import com.bank.member.service.MemberService;
import com.bank.member.service.WorkerService;
import com.bank.utils.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	  @Autowired
	  private JwtUtil jwtUtil;
	  @Autowired
	  private MemberService memberService;
	  
	  @Autowired
	  private WorkerService workerService;
	  
	  
	  @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest login) {
	        
		    Member member = memberService.getMemberByIdentity(login.mIdentity);
		    
		    if(member==null) {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("查無身分證");}
		  
	        if (member.getmAccount().equals(login.mAccount) && member.getmPassword().equals(login.mPassword)) {
	            String token = jwtUtil.generateToken(member.getmId(),"member"); // 模擬 userId 為 1001
	            MemberDto mDto = new MemberDto(member,token);
	            

	            return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(mDto);
	        }

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳密錯誤");
	    }

	    static class LoginRequest {
	    	public String mIdentity;
	    	public String mAccount;
	    	public String mPassword;
			public String getmIdentity() {
				return mIdentity;
			}
			public void setmIdentity(String mIdentity) {
				this.mIdentity = mIdentity;
			}
			public String getmAccount() {
				return mAccount;
			}
			public void setmAccount(String mAccount) {
				this.mAccount = mAccount;
			}
			public String getmPassword() {
				return mPassword;
			}
			public void setmPassword(String mPassword) {
				this.mPassword = mPassword;
			}
	    	
	    	
	    }
	    @PostMapping("/backlogin")
	    public ResponseEntity<?> backlogin(@RequestBody LoginRequest login) {
	        // 假設帳密驗證成功（這裡沒用資料庫，為了簡單）
		    Worker w = workerService.getWorkerByAccount(login.getmAccount());
		    
		    if(w==null) {return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("查無帳號");}
		  
	        if (w.getwAccount().equals(login.mAccount) && w.getwPassword().equals(login.mPassword)) {
	            String token = jwtUtil.generateToken(w.getwId(),"worker"); // 模擬 userId 為 1001
	            WorkerDto wDto = new WorkerDto(w,token);
	            
	            return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(wDto);
	        }

	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("帳密錯誤");
	    }

	   

}
