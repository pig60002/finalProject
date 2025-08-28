package com.bank.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Member;
import com.bank.member.bean.Worker;
import com.bank.member.bean.WorkerLog;
import com.bank.member.dao.WorkerLogRepository;
import com.bank.member.dao.WorkerRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class WorkerLogService {

    @Autowired
    private WorkerLogRepository workerLogRepository;

    @Autowired
    private WorkerRepository workerRepository;

    /**
     * 新增 WorkerLog 記錄
     * 
     * @param workerId 工作人員ID
     * @param action 操作名稱
     * @param message 訊息描述
     */
    public void logAction(Integer workerId, String action, String message) {

    		Worker worker = workerRepository.findById(workerId)
    				.orElseThrow(() -> new RuntimeException("Worker not found"));
    		
    		WorkerLog log = new WorkerLog(worker, action, message);
    		workerLogRepository.save(log);
    	
    }
    
    public List<WorkerLog> findByWorker_wAccount(String wAccount){
    	return workerLogRepository.findByWorker_wAccount(wAccount);
    }

    /**
     * 查詢所有 WorkerLog 記錄
     * 
     * @return 所有 WorkerLog 記錄
     */
    public List<WorkerLog> getAllLogs() {
        return workerLogRepository.findAll(Sort.by(Sort.Direction.DESC, "time"));
    }

    /**
     * 根據 Worker ID 查詢 WorkerLog 記錄
     * 
     * @param workerId 工作人員ID
     * @return WorkerLog 記錄
     */
    public List<WorkerLog> getLogsByWorkerId(Integer workerId) {
        return workerLogRepository.findLogsByWorkerWIdOrderByTimeDesc(workerId);
    }

    /**
     * 根據 ID 查詢 WorkerLog 記錄
     * 
     * @param logId WorkerLog ID
     * @return WorkerLog 記錄
     */
    public WorkerLog getLogById(Integer logId) {
    	
    	Optional<WorkerLog> op = workerLogRepository.findById(logId);
        if(op.isPresent()) {
        	return op.get();
        }
        return null;
    }

    /**
     * 刪除 WorkerLog 記錄
     * 
     * @param logId WorkerLog ID
     */
    public void deleteLog(Integer logId) {
        workerLogRepository.deleteById(logId);
    }

    /**
     * 修改 WorkerLog 記錄
     * 
     * @param logId WorkerLog ID
     * @param action 新的操作名稱
     * @param message 新的訊息描述
     */
    public void updateLog(Integer logId, String action, String message) {
        WorkerLog log = workerLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        log.setAction(action);
        log.setMessage(message);
        workerLogRepository.save(log);
    }
    
    public Page<WorkerLog> searchLogs(String action, String account, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // 0-based page index
        return workerLogRepository.findByActionAndWorkerAccount(action, account, pageable);
    }
    
    
    

}