package com.bank.member.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.member.bean.Member;
import com.bank.member.bean.PasswordResetToken;
import com.bank.member.bean.Worker;
import com.bank.member.service.EmailService;
import com.bank.member.service.MemberService;
import com.bank.member.service.PasswordResetTokenService;
import com.bank.member.service.WorkerLogService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping(path = "/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PasswordResetTokenService PRTService; 
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WorkerLogService workerLogService;
	
	String newAction = "新增";
	String updateAction = "修改";
	
	@GetMapping("/memberAll")
	public List<Member> getAllMembers() {
	    return memberService.getAllMembers();
	}
	@GetMapping("/{id}")
	public Member getMemberById(@PathVariable Integer id) {
		
		Member m = new Member();
		//m = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		m.getmId();
	     return memberService.getMemberById(id);
	}
	@PostMapping("/member")
	public ResponseEntity<?> createMember(@RequestBody Member member) {
		Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Member m= memberService.getMemberByIdentity(member.getmIdentity());
		if(m != null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("身分證已註冊過");
		}
		m= memberService.getMemberByEmail(member.getmEmail());
		if(m != null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("信箱已註冊過");
		}
		 m= memberService.insertMember(member);
		if(check(principal)) {
			Worker worker  = (Worker) principal;
			workerLogService.logAction(worker.getwId(),newAction,"會員編號:"+m.getmId()+",名字:"+m.getmName()+"的會員");
		}
	     return ResponseEntity.ok(m);
	}
	@PutMapping("/{id}")
	public Member updateMember(@PathVariable Integer id ,@RequestBody Member member) {
		Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(check(principal)) {
			Worker worker  = (Worker) principal;
			Member OldMember =memberService.getMemberById(id);
			String message = CompareMembers(OldMember,member);
			if(!message.isEmpty()) {
				workerLogService.logAction(worker.getwId(),updateAction,"會員編號:"+id+",資料更動:"+message);		
			}
			
		}
		member.setmId(id);
		
	    return memberService.updateMember(member);
	}
	@DeleteMapping("/{id}")
	public String deleteMember(@PathVariable Integer id) {
		Member member = memberService.getMemberById(id);
		if(member == null) {
			return "刪除失敗";
		}
		memberService.deleteById(id);
		return"已刪除"+id+"編號";
	}
	
	@GetMapping("/search")
	public ResponseEntity<Page<Member>> searchMembers(
	    @RequestParam(required = false) String identity,
	    @RequestParam(required = false) String name,
	    @RequestParam(required = false) Integer state,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
	    @RequestParam(defaultValue = "1") int currentPage,
	    @RequestParam(defaultValue = "10") int pageSize
	) {
		System.out.println(birthday);
	    Page<Member> result = memberService.searchMembers(
	    		identity, name, state, birthday, startDate, endDate,currentPage-1, pageSize
	    );
	    return ResponseEntity.ok(result);
	}
	
    @PostMapping("/upload-mimage")
    public ResponseEntity<?> uploadAvatar(
        @RequestPart("file") MultipartFile file
    ) {
    	System.out.println("有近來摟上傳圖片");
        try {
        	 String newAvatarUrl="";
        	Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	if (principal instanceof Member) {
        		System.out.println("我有近來會員喔");
        	   Member member = (Member) principal;
        	  newAvatarUrl = memberService.updateMemberImage(member, file);
        	} else if (principal instanceof Worker ) {
        		System.out.println("我是員工");
        		Worker worker  = (Worker) principal;
        	} else {
        	    System.out.println("都不是員工跟會員");
        	}
        	System.out.println("這裡沒錯");
        	return ResponseEntity.ok(Map.of("新增成功路徑為", newAvatarUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("上傳失敗：" + e.getMessage());
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequest email) throws MessagingException {
    	System.out.println("我有近來喔");
    	Member member =memberService.getMemberByEmail(email.getEmail());
        if (member == null) {
            return ResponseEntity.badRequest().body("沒有找到email");
        }

        // 產生 token
        String token = UUID.randomUUID().toString();
        Date expiry = Date.from(Instant.now().plus(15, ChronoUnit.MINUTES)); // 15分鐘後過期

        PasswordResetToken resetToken = new PasswordResetToken(member, token, expiry);
        PRTService.insertPasswordResetToken(resetToken);

        // 傳送 email
        String resetLink = "http://localhost:5173/yuzubank/memberResetPassword?token=" + token;
        emailService.sendResetEmail(member.getmEmail(), resetLink);

        return ResponseEntity.ok("Reset password link sent to your email.");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetRequest resetRequest) {
    	
    	
    	PasswordResetToken prt = PRTService.findToken(resetRequest.getToken());
    	
    	if(prt ==null) {
    		return ResponseEntity.badRequest().body("沒有找到token");
    	}
    	Date now = new Date();
    	if(prt.getExpiry().before(now)){
    		return ResponseEntity.badRequest().body("Token 無效或已過期");
    	}
    	prt.getMember().setmPassword(resetRequest.getPassword());
    	memberService.updateMember(prt.getMember());
    	PRTService.deleteById(prt.getId());
       return ResponseEntity.ok("密碼已成功重設");
       
    }
    
	public static class EmailRequest {
	    private String email;

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}
	    
	}
	public static class ResetRequest {
	    private String password;
	    private String token;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}

		
	}
    public boolean check(Object principal) {
    	if (principal instanceof Worker) {
    		return true; 
    	}
    	return false;
    }
    
    public String CompareMembers(Member oldMember, Member newMember) {
    	String message = "";
    	if(!oldMember.getmName().equals(newMember.getmName())) {
    		message = message+"姓名:"+oldMember.getmName()+"->"+newMember.getmName()+" ";
    	}
    	if(!oldMember.getmIdentity().equals( newMember.getmIdentity())) {
    		message = message+"身分證:"+oldMember.getmIdentity()+"->"+newMember.getmIdentity()+" ";
    	}
    	if(!oldMember.getmGender().equals(newMember.getmGender())) {
    		message = message+"性別:"+oldMember.getmGender()+"->"+newMember.getmGender()+" ";
    	}
     	if(!oldMember.getmAccount().equals(newMember.getmAccount())) {
    		message = message+"帳號:"+oldMember.getmAccount()+"->"+newMember.getmAccount()+" ";
    	}
    	if(!oldMember.getmPassword().equals(newMember.getmPassword())) {
    		message = message+"密碼:"+oldMember.getmPassword()+"->"+newMember.getmPassword()+" ";
    	}
    	if(!oldMember.getmAddress().equals(newMember.getmAddress())) {
    		message = message+"地址:"+oldMember.getmAddress()+"->"+newMember.getmAddress()+" ";
    	}
    	if(!oldMember.getmPhone().equals(newMember.getmPhone())) {
    		message = message+"電話:"+oldMember.getmPhone()+"->"+newMember.getmPhone()+" ";
    	}
    	LocalDate oldDate = oldMember.getmBirthday().toInstant()
    		    .atZone(ZoneId.systemDefault())
    		    .toLocalDate();

    		LocalDate newDate = newMember.getmBirthday().toInstant()
    		    .atZone(ZoneId.systemDefault())
    		    .toLocalDate();
    	
    	if(!oldDate.equals(newDate)) {
    		message = message+"生日:"+oldMember.getmBirthday()+"->"+newMember.getmBirthday()+" ";
    	}
    	if(!oldMember.getmEmail().equals(newMember.getmEmail())) {
    		message = message+"信箱:"+oldMember.getmEmail()+"->"+newMember.getmEmail()+" ";
    	}
    	if(!oldMember.getmState().equals(newMember.getmState())) {
    		String oldState =null;
			String newState =null;
    		if(oldMember.getmState().equals(1)) {
    			oldState ="正常";
    			newState ="停權";
    		}else {
    			 oldState ="停權";
    			 newState ="正常";
			}
    		
    		message = message+"狀態:"+oldState+"->"+newState+" ";
    	}
    	return message;
    }
	
	
}
