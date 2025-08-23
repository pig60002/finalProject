package com.bank.member.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.member.bean.Page;
import com.bank.member.bean.Permission;
import com.bank.member.bean.Role;

import jakarta.transaction.Transactional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    // 依角色查詢所有權限
    List<Permission> findByRole(Role role);
    
    // 依頁面查詢所有權限
    List<Permission> findByPage(Page page);
    
    // 依角色id查權限
    List<Permission> findByRole_RoleId(Integer roleId);
    
    
    @Transactional
    @Modifying 
    @Query("DELETE FROM Permission p WHERE p.role.id = :roleId AND p.page.id = :pageId")
    void deleteByRoleAndPageUsingQuery(@Param("roleId") Integer roleId, @Param("pageId") Integer pageId);
}
