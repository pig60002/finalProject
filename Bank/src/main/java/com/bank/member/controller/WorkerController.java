package com.bank.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.member.bean.Role;
import com.bank.member.bean.Worker;
import com.bank.member.service.RoleService;
import com.bank.member.service.WorkerLogService;
import com.bank.member.service.WorkerService;

@RestController
@RequestMapping(path = "/worker")
public class WorkerController {
	
	@Autowired
	private WorkerService workerService;
	
	@Autowired
	private WorkerLogService workerLogService;
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping("/workerAll")
	public List<Worker> getAllMembers() {
	    return workerService.getAllWorker();
	}
	@GetMapping("/{id}")
	public Worker getMemberById(@PathVariable Integer id) {
	     return workerService.getWorkerById(id);
	}
	@PostMapping("/worker")
	public Worker createWorker(@RequestBody Worker worker) {
		Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Worker w = workerService.inserWorker(worker);
		if(principal instanceof Worker) {
			Worker worker1  = (Worker) principal;
			workerLogService.logAction(worker1.getwId(),"新增","管理員編號:"+w.getwId()+",名稱:"+w.getwName());	
		}
	     return w;
	}
	@PutMapping("/{id}")
	public Worker updateWorker(@PathVariable Integer id ,@RequestBody Worker worker) {
		Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(principal instanceof Worker) {
			String message="";
			Worker worker1  = (Worker) principal;
			Worker oldw = workerService.getWorkerById(id);
			
			if(!oldw.getwName().equals(worker.getwName())) {
				message = message+"名稱:"+oldw.getwName()+"->"+worker.getwName()+" ";
			}
			
			if(!oldw.getwAccount().equals(worker.getwAccount())) {
				message = message+"帳號:"+oldw.getwAccount()+"->"+worker.getwAccount()+" ";
			}
			if(!oldw.getwPassword().equals(worker.getwPassword())) {
				message = message+"密碼:"+oldw.getwPassword()+"->"+worker.getwPassword()+" ";
			}
			if(!oldw.getRole().getRoleId().equals(worker.getRole().getRoleId())) {
				Role r = roleService.getRoleById(worker.getRole().getRoleId());
				message = message+"角色:"+oldw.getRole().getRoleName()+"->"+r.getRoleName()+" ";
			}
			
			
			workerLogService.logAction(worker1.getwId(),"修改","管理員編號:"+oldw.getwId()+",資料變更:"+message);
		}
		
		
		
		
		worker.setwId(id);
	    return  workerService.updateWorker(worker);
	}
	@DeleteMapping("/{id}")
	public String deleteMember(@PathVariable Integer id) {
		Worker worker = workerService.getWorkerById(id);
		if(worker == null) {
			return "刪除失敗";
		}
		workerService.deleteById(id);
		return"已刪除"+id+"編號";
	}
	
	

	
}
