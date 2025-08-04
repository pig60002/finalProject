package com.bank.member.dao;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.member.bean.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	Optional<Member> findByMIdentity(String mIdentity);
	
	 @Query("SELECT m FROM Member m WHERE " +
	           "(:name IS NULL OR m.mName LIKE %:name%) AND " +
	           "(:account IS NULL OR m.mAccount LIKE %:account%) AND " +
	           "(:phone IS NULL OR m.mPhone LIKE %:phone%) AND " +
	           "(:email IS NULL OR m.mEmail LIKE %:email%) AND " +
	           "(:gender IS NULL OR m.mGender = :gender) AND " +
	           "(:state IS NULL OR m.mState = :state) AND " +
	           "(:startDate IS NULL OR m.mBirthday >= :startDate) AND " +
	           "(:endDate IS NULL OR m.mBirthday <= :endDate)")
	    Page<Member> searchByConditions(
	        @Param("name") String name,
	        @Param("account") String account,
	        @Param("phone") String phone,
	        @Param("email") String email,
	        @Param("gender") String gender,
	        @Param("state") Integer state,
	        @Param("startDate") Date startDate,
	        @Param("endDate") Date endDate,
	        Pageable pageable
	    );
}
