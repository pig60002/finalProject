package com.bank.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.member.bean.Member;
import com.bank.member.service.MemberService;

@RestController
@RequestMapping(path = "/member")
public class MemberController {
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/memberAll")
	public List<Member> getAllMembers() {
	    return memberService.getAllMembers();
	}
	@GetMapping("/{id}")
	public Member getMemberById(@PathVariable Integer id) {
		Member m = new Member();
		m = (Member)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
}
