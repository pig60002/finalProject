package com.bank.member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.bank.member.bean.Worker;
import com.bank.member.service.RoleService;

@RestController
@RequestMapping(path="/api/roles")
public class RoleController {

	

	    @Autowired
	    private RoleService roleService;

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
}
