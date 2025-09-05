package com.bank.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.member.bean.WorkerLog;
import com.bank.member.service.WorkerLogService;

@RestController
@RequestMapping("/workerlog")
public class WorkerLogController {

    @Autowired
    private WorkerLogService workerLogService;

    /**
     * 新增 WorkerLog 記錄
     */
    @PostMapping("/log-action")
    public String logAction(@RequestParam Integer workerId, @RequestParam String action, @RequestParam String message) {
        workerLogService.logAction(workerId, action, message);
        return "Action logged successfully";
    }

    /**
     * 查詢所有 WorkerLog 記錄
     */
    @GetMapping("/all")
    public List<WorkerLog> getAllLogs() {
        return workerLogService.getAllLogs();
    }

    /**
     * 根據 Worker ID 查詢 WorkerLog 記錄
     */
    @GetMapping("/worker/{workerId}")
    public List<WorkerLog> getLogsByWorkerId(@PathVariable Integer workerId) {
        return workerLogService.getLogsByWorkerId(workerId);
    }

    /**
     * 根據 Log ID 查詢 WorkerLog 記錄
     */
    @GetMapping("/{logId}")
    public WorkerLog getLogById(@PathVariable Integer logId) {
        return workerLogService.getLogById(logId);
    }

    /**
     * 刪除 WorkerLog 記錄
     */
    @DeleteMapping("/{logId}")
    public String deleteLog(@PathVariable Integer logId) {
        workerLogService.deleteLog(logId);
        return "Log deleted successfully";
    }

    /**
     * 修改 WorkerLog 記錄
     */
    @PutMapping("/{logId}")
    public String updateLog(@PathVariable Integer logId, @RequestParam String action, @RequestParam String message) {
        workerLogService.updateLog(logId, action, message);
        return "Log updated successfully";
    }
}