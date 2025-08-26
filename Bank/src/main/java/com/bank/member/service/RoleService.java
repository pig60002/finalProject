package com.bank.member.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Page;
import com.bank.member.bean.Permission;
import com.bank.member.bean.Role;
import com.bank.member.dao.PageRepository;
import com.bank.member.dao.PermissionRepository;
import com.bank.member.dao.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private PageRepository pageRepository;

	// 取得某角色的所有頁面權限
	public Map<Integer,String> getPagesByRoleId(Integer roleId) {
		Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
		/* 上面那句等同於下面那句
		 * Optional<Role> optionalRole = roleRepository.findById(roleId);
		 * 
		 * if (optionalRole.isPresent()) { Role role = optionalRole.get(); // 用 role
		 * 做後續處理 } else { throw new RuntimeException("Role not found"); }
		 */
		
		List<Permission> permissions = permissionRepository.findByRole_RoleId(role.getRoleId());
		return permissions.stream().map(Permission::getPage).collect(Collectors.toMap(Page::getPageId,Page::getPageName));
	}
	
	public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
	
	public Role getRoleById(Integer roleId) {
		Optional<Role> op = roleRepository.findById(roleId);
        if(op.isPresent()) {
        	return op.get();
        }
        return null;
	}
	
	public Role insertRole(Role role) {
		Integer id= roleRepository.findMaxId();
		role.setRoleId(id+1);
		return roleRepository.save(role);
	}
	
	public boolean deleteRoleById(Integer roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);

        if (optionalRole.isPresent()) {
            roleRepository.deleteById(roleId);
            return true;
        } else {
            return false;
        }
    }
	

}
