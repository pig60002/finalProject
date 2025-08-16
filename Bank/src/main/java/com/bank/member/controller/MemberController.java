package com.bank.member.controller;

import java.time.Instant;
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

@RestController
@RequestMapping(path = "/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PasswordResetTokenService PRTService; 
	@Autowired
	private EmailService emailService;
	
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
	public Member createMember(@RequestBody Member member) {
		
	     return memberService.insertMember(member);
	}
	@PutMapping("/{id}")
	public Member updateMember(@PathVariable Integer id ,@RequestBody Member member) {
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
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
    	
    	Member member =memberService.getMemberByEmail(email);
        if (member == null) {
            return ResponseEntity.badRequest().body("No account found with that email.");
        }

        // 產生 token
        String token = UUID.randomUUID().toString();
        Date expiry = Date.from(Instant.now().plus(1, ChronoUnit.HOURS)); // 1小時後過期

        PasswordResetToken resetToken = new PasswordResetToken(member, token, expiry);
        PRTService.insertPasswordResetToken(resetToken);

        // 傳送 email
        String resetLink = "http://your-domain.com/reset-password?token=" + token;
        emailService.sendResetEmail(member.getmEmail(), resetLink);

        return ResponseEntity.ok("Reset password link sent to your email.");
    }
	
	
}
