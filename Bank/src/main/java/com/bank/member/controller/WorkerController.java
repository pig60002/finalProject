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

import com.bank.member.bean.Worker;
import com.bank.member.service.WorkerService;

@RestController
@RequestMapping(path = "/worker")
public class WorkerController {
	
	@Autowired
	private WorkerService workerService;
	
	@GetMapping("/workerAll")
	public List<Worker> getAllMembers() {
	    return workerService.getAllWorker();
	}
	@GetMapping("/{id}")
	public Worker getMemberById(@PathVariable Integer id) {
		Worker w = new Worker();
		w = (Worker)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		w.getwId();
	     return workerService.getWorkerById(id);
	}
	@PostMapping("/worker")
	public Worker createWorker(@RequestBody Worker worker) {
	     return workerService.inserWorker(worker);
	}
	@PutMapping("/{id}")
	public Worker updateWorker(@PathVariable Integer id ,@RequestBody Worker worker) {
		worker.setwId(id);
	    return workerService.updateWorker(worker);
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
