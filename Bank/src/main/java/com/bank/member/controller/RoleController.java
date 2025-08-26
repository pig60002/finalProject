package com.bank.member.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.member.bean.Member;
import com.bank.member.bean.Page;
import com.bank.member.bean.Permission;
import com.bank.member.bean.Role;
import com.bank.member.bean.RoleWithPagesDto;
import com.bank.member.bean.Worker;
import com.bank.member.service.PageService;
import com.bank.member.service.PermissionService;
import com.bank.member.service.RoleService;

@RestController
@RequestMapping(path="/api/roles")
public class RoleController {

	

	    @Autowired
	    private RoleService roleService;
	    
	    @Autowired
	    private PermissionService permissionService;
	    
	    @Autowired
	    private PageService pageService;

	    // GET /api/roles/{roleId}/pages
	    // 取得指定角色的頁面列表
	    @GetMapping("/pages")
	    public ResponseEntity<Map<Integer,String>> getPages() {
	    	Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	Integer roleId = null;
	    	if (principal instanceof Worker ) {
        		System.out.println("我是員工");
        		Worker worker  = (Worker) principal;
        		roleId = worker.getRole().getRoleId();
        	}
	        try {
	            Map<Integer,String> pages = roleService.getPagesByRoleId(roleId);
	            return ResponseEntity.ok(pages);
	        } catch (RuntimeException e) {
	            // 角色不存在，回 404 Not Found
	            return ResponseEntity.notFound().build();
	        }
	    }
		@GetMapping("/roleAll")
		public List<Role> getAllMembers() {
		    return roleService.getAllRoles();
		}
		
	    @GetMapping("/rolesWithPages")
	    public List<RoleWithPagesDto> getAllRolesWithPages() {
	        // 查詢所有角色
	        List<Role> roles = roleService.getAllRoles();
	        
	        // 將每個角色對應的頁面組合成資料
	        return roles.stream()
	                .map(role -> {
	                    List<Page> pages = permissionService.findByRole_RoleId(role.getRoleId()).stream()
	                            .map(Permission::getPage)
	                            .collect(Collectors.toList());
	                    return new RoleWithPagesDto(role, pages);  // 封裝角色和對應頁面
	                })
	                .collect(Collectors.toList());
	    }
	    
	    @PostMapping("/updatePermissions")
	    public ResponseEntity<?> updatePermissions(@RequestBody List<RoleWithPagesDto> roleUpdates) {
	    	 for (RoleWithPagesDto roleUpdate : roleUpdates) {
	    	        // 根據角色 ID 查詢角色
	    	        Role role = roleService.getRoleById(roleUpdate.getRole().getRoleId());
	    	        if(role==null) {
	    	        	return ResponseEntity.ok("沒有這個角色");
	    	        }
	    	        

	    	        // 獲取該角色現有的頁面權限
	    	        Set<Integer> currentPageIds = role.getPermissions().stream()
	    	                                          .map(permission -> permission.getPage().getPageId())
	    	                                          .collect(Collectors.toSet());
	    	        List<Page> pages = permissionService.getPagesByRole(role);
	    	        // 刪除不再選擇的頁面權限
	    	        for (Page oldPage : pages) {
	    	            if (!roleUpdate.getPages().contains(oldPage)) {
	    	            	permissionService.deletRolewithPage(role.getRoleId(), oldPage.getPageId());
	    	            }
	    	        }

	    	        // 新增未選擇的頁面權限
	    	        for (Page page : roleUpdate.getPages()) {
	    	            if (!currentPageIds.contains(page.getPageId())) {
	    	                Page newpage = pageService.getPageById(page.getPageId());          
	    	                Permission permission = new Permission(role, newpage);
	    	                permissionService.saveRolewithPage(permission);
	    	            }
	    	        }
	    	    }
	    	
	    	return ResponseEntity.ok("Permissions updated successfully");
	    }
	    
	    @PostMapping("/role")
		public ResponseEntity<?> insertRoleWidthPage(@RequestBody RoleWithPagesDto roleWithPagesDto) {
	    	Role role = roleService.insertRole(roleWithPagesDto.getRole());
	    	
	    	List<Page> pages = roleWithPagesDto.getPages();
	    	
	    	for(Page page : pages) {
	    		Permission ps = new Permission(role,page);	
	    		permissionService.saveRolewithPage(ps);
	    	}
	    	
		    return ResponseEntity.ok("角色新增成功");
		}
	    @DeleteMapping("/{roleId}")
	    public ResponseEntity<?> deleteRole(@PathVariable Integer roleId) {
	        boolean deleted = roleService.deleteRoleById(roleId);
	        if (deleted) {
	            return ResponseEntity.ok("角色已刪除");
	        } else {
	            return ResponseEntity.status(404).body("找不到指定的角色");
	        }
	    }
	    
}
