package com.bank.member.service;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bank.member.bean.Member;

@SpringBootTest
class MemberServiceTest {
	@Autowired
	private MemberService memberService;

	//@Test
	void testInsertMember() {
		Member m = new Member();
		m.setmId(1010);
		m.setmName("測試用的");
		m.setmIdentity("123456789");
		m.setmGender("男");
		m.setmAccount("testgg");
		m.setmPassword("test123");
		m.setmAddress("桃園縣致力路");
		m.setmPhone("0851233544");
		Date mBirthday = java.sql.Date.valueOf("1800-12-05");
		m.setmBirthday(mBirthday);
		LocalDate currentDate = LocalDate.now();
		m.setCreation(java.sql.Date.valueOf(currentDate));
		m.setmEmail("123456789@");
		m.setmState(1);
		memberService.updateMember(m);
	}

	//@Test
	void testUpdateMember() {
		Member m = new Member();
		m.setmId(1010);
		m.setmName("已更改");
		m.setmIdentity("123456789");
		m.setmGender("男");
		m.setmAccount("testgg");
		m.setmPassword("test123");
		m.setmAddress("桃園縣致力路");
		m.setmPhone("0851233544");
		Date mBirthday = java.sql.Date.valueOf("1800-12-05");
		m.setmBirthday(mBirthday);
		LocalDate currentDate = LocalDate.now();
		m.setCreation(java.sql.Date.valueOf(currentDate));
		m.setmEmail("123456789@");
		m.setmState(1);
		memberService.insertMember(m);
	}

	@Test
	void testGetMemberById() {
		Member m =	memberService.getMemberById(1010);
		System.out.println(m.toString()); 
		
	}

	//@Test
	void testGetAllMembers() {
		fail("Not yet implemented");
	}

	//@Test
	void testDeleteById() {
		fail("Not yet implemented");
	}

}
