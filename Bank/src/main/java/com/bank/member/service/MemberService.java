package com.bank.member.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	public Member getMemberByIdentity(String identity) {
		Optional<Member> op = mRepos.findByMIdentity(identity);
        if(op.isPresent()) {
        	return op.get();
        }
        return null;
	}
	public void deleteById(Integer id) {
		mRepos.deleteById(id);
	}
	
	public Page<Member> searchMembers(
	        String name,
	        String account,
	        String phone,
	        String email,
	        String gender,
	        Integer state,
	        Date startDate,
	        Date endDate,
	        int page,
	        int size
	    ) {
	        Pageable pageable = PageRequest.of(page, size, Sort.by("mId").ascending());

	        return mRepos.searchByConditions(
	            isBlank(name) ? null : name,
	            isBlank(account) ? null : account,
	            isBlank(phone) ? null : phone,
	            isBlank(email) ? null : email,
	            isBlank(gender) ? null : gender,
	            state,
	            startDate,
	            endDate,
	            pageable
	        );
	    }

	    private boolean isBlank(String s) {
	        return s == null || s.trim().isEmpty();
	    }
	
}
