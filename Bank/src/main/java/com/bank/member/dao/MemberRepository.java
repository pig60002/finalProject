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
			   "(:identity IS NULL OR m.mIdentity =:identity) AND " +
	           "(:name IS NULL OR m.mName LIKE %:name%) AND " +
	           "(:state IS NULL OR m.mState = :state) AND " +
	           "(:birthday IS NULL OR m.mBirthday = :birthday) AND " +
	           "(:startDate IS NULL OR m.mBirthday >= :startDate) AND " +
	           "(:endDate IS NULL OR m.mBirthday <= :endDate)")
	    Page<Member> searchByConditions(
	    	@Param("identity") String identity,
	        @Param("name") String name,
	        @Param("state") Integer state,
	        @Param("birthday") Date birthday,
	        @Param("startDate") Date startDate,
	        @Param("endDate") Date endDate,
	        Pageable pageable
	    );

}
