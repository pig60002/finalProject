package com.bank.member.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bank.member.bean.Member;
import com.bank.member.dao.MemberRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MemberService {
	
	private static final String UPLOAD_DIR = "C:/bankSpringBoot/Bank/uploadImg/memberImg/";
	@Autowired
	private MemberRepository mRepos;
	
	public Member insertMember(Member member) {
		Integer max = mRepos.findMaxId();
		LocalDate currentDate = LocalDate.now();
		member.setCreation(java.sql.Date.valueOf(currentDate));
		member.setmState(1);	
		member.setmId(max+1);
		member.setmImage("/bank/uploadImg/memberImg/defultpeople.png");
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
	
	public Member getMemberByEmail(String email) {
        Optional<Member> op = mRepos.findByMEmail(email);
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
	        String identity,
	        String name,
	        Integer state,
	        Date birthday,
	        Date startDate,
	        Date endDate,
	        int page,
	        int size
	    ) {
	        Pageable pageable = PageRequest.of(page, size, Sort.by("mId").ascending());

	        return mRepos.searchByConditions(
	            isBlank(identity) ? null : identity,
	            isBlank(name) ? null : name,
	            state,
	            birthday,
	            startDate,
	            endDate,
	            pageable
	        );
	    }
	
	    private boolean isBlank(String s) {
	        return s == null || s.trim().isEmpty();
	    }

	    public String updateMemberImage(Member member , MultipartFile file) throws IOException, java.io.IOException {
	    		    	
	        // 刪除舊圖（如果有）
	        if (member.getmImage()!= null) {
	            File oldFile = new File(member.getmImage());
	            if (oldFile.exists()) oldFile.delete();
	        }

	        // 產生新檔名
	        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

	        // 建立資料夾
	        Path uploadPath = Paths.get(UPLOAD_DIR);
	        Files.createDirectories(uploadPath);

	        // 儲存新圖
	        Path filePath = uploadPath.resolve(filename);
	        Files.write(filePath, file.getBytes());

	        // 更新會員資料
	        String newmimagePath = "/bank/uploadImg/memberImg/" + filename;
	        member.setmImage(newmimagePath);
	        mRepos.save(member);

	        return newmimagePath;
	    }
}
