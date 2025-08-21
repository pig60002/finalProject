package com.bank.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Page;
import com.bank.member.bean.Permission;
import com.bank.member.bean.Role;
import com.bank.member.dao.PermissionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PermissionService {
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	
	public List<Permission> findByRole(Role role){
		return permissionRepository.findByRole(role);
	}
	
	public List<Permission> findByPage(Page page){
		return permissionRepository.findByPage(page);
	}
	
	public  List<Permission> findByRole_RoleId(Integer roleId){
		return permissionRepository.findByRole_RoleId(roleId);
	}
	
	public List<Page> getPagesByRole(Role role) {
        // 1. 根據角色查詢所有的 Permission 權限
        List<Permission> permissions = permissionRepository.findByRole(role);
        
        // 2. 使用 Stream API 從 Permission 物件中取出 Page 物件
        return permissions.stream()
                          .map(Permission::getPage)
                          .collect(Collectors.toList());
    }
	
	
	
	
	
	public void deletRolewithPage(Integer roleId ,Integer pageId) {
		permissionRepository.deleteByRoleAndPageUsingQuery(roleId,pageId);
	}
	
	public void saveRolewithPage(Permission permission) {
		
        permissionRepository.save(permission);
	}
	
	
	
	
}
