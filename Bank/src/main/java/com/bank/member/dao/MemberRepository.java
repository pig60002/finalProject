package com.bank.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.member.bean.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

}
