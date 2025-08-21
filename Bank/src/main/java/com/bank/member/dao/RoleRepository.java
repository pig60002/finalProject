package com.bank.member.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.member.bean.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(String roleName);
	
	 @Query("SELECT MAX(r.roleId) FROM Role rw")
	 Integer findMaxId();
}
