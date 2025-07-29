package com.bank.member.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Member;
import com.bank.member.dao.MemberRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberRepository mRepos;
	
	public Member insertMember(Member member) {
		LocalDate currentDate = LocalDate.now();
		member.setCreation(java.sql.Date.valueOf(currentDate));
		member.setmState(1);	
	    return mRepos.save(member);
	        
	 }
	
	public Member updateMember(Member member) {
        return mRepos.save(member);
	}
	public Member getMemberById(Integer id) {
        Optional<Member> op = mRepos.findById(id);
        if(op.isPresent()) {
        	return op.get();
        }
        return null;
    }
	
	public List<Member> getAllMembers() {
        return mRepos.findAll();
    }
	public void deleteById(Integer id) {
		mRepos.deleteById(id);
	}
	
}
