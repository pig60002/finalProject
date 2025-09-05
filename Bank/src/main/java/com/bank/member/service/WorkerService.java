package com.bank.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Worker;
import com.bank.member.dao.WorkerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WorkerService {
	@Autowired
	private WorkerRepository wRepos;
	
	public Worker inserWorker(Worker worker) {
		Integer id =wRepos.findMaxId();
		worker.setwId(id+1);
		return wRepos.save(worker);
	}
	public Worker updateWorker(Worker worker) {
		return wRepos.save(worker);
	}
	public Worker getWorkerById(Integer id) {
		Optional<Worker> op = wRepos.findById(id);
		if(op.isPresent()) {
			return op.get();
		}
		return null;
	}
	public Worker getWorkerByAccount(String account) {
		Optional<Worker> op = wRepos.findBywAccount(account);
        if(op.isPresent()) {
        	return op.get();
        }
        return null;
	}
	public List<Worker> getAllWorker() {
		return wRepos.findAll();
	}
	public void deleteById(Integer id) {
		wRepos.deleteById(id);
	}
	
	
}
