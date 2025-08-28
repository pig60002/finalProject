package com.bank.member.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.member.bean.Worker;
import com.bank.member.bean.WorkerLog;
import com.bank.member.service.WorkerLogService;
import com.bank.member.service.WorkerService;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/workerlog")
public class WorkerLogController {

    @Autowired
    private WorkerLogService workerLogService;
    @Autowired
    private WorkerService workerService;

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
 
    	List<WorkerLog> wls = workerLogService.getLogsByWorkerId(workerId);
    
        return wls;
    }

    /**
     * 根據 Log ID 查詢 WorkerLog 記錄
     */
    @GetMapping("/{logId}")
    public WorkerLog getLogById(@PathVariable Integer logId) {
        return workerLogService.getLogById(logId);
    }
    
    @GetMapping("/account/{waccount}")
    public ResponseEntity<?> getLogByAccount(@PathVariable String waccount) {
    	Worker w=  workerService.getWorkerByAccount(waccount);
    	if(w==null) {
    		return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("沒有此員工帳號：" + waccount);
    	}
    	List<WorkerLog> logs = workerLogService.getLogsByWorkerId(w.getwId());

    	    if (logs == null) {
    	        return ResponseEntity
    	            .status(HttpStatus.NOT_FOUND)
    	            .body("該員工尚無操作紀錄");
    	    }

    	    return ResponseEntity.ok(logs);
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
    
    @GetMapping
    public Page<WorkerLog> getWorkerLogs(
        @RequestParam(required = false) String action,
        @RequestParam(required = false) String account,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return workerLogService.searchLogs(action, account, page, size);
    }
    

    @GetMapping("/api/download-workerlogs")
    public void downloadWorkerLogs(@RequestParam(required = false) String workerName,HttpServletResponse response) throws IOException {
        
    	List<WorkerLog> logs = null;
    	
    	if(workerName == "") {
    		 logs = workerLogService.getAllLogs();

    	}else {
    		 logs = workerLogService.findByWorker_wAccount(workerName);
			
		}
    	

        // 設定 response header，告訴瀏覽器下載檔案
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"worker_logs.csv\"");
        byte[] bom = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
        // 建立 CSV header
        String header = "ID,wid,WorkerName,Action,Message,Time\n";

        // 使用 StringBuilder 儲存 CSV 內容
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(header);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (WorkerLog log : logs) {
            // 注意：這裡假設 Worker 有 getName() 方法取得姓名
            String workerName1 = log.getWorker() != null ? log.getWorker().getwName() : "無資料";
            Integer wid = log.getWorker() != null ? log.getWorker().getwId() :0;

            // 把欄位用逗號隔開，字串用雙引號包起來（簡單處理，避免逗號斷行問題）
            csvBuilder.append(log.getId()).append(",");
            csvBuilder.append("\"").append(wid).append("\",");
            csvBuilder.append("\"").append(workerName1).append("\",");
            csvBuilder.append("\"").append(log.getAction()).append("\",");
            csvBuilder.append("\"").append(log.getMessage()).append("\",");
            csvBuilder.append(sdf.format(log.getTime())).append("\n");
        }

        // 寫出 CSV 內容
        try {
        	
        	response.getOutputStream().write(bom); 
        	
			response.getOutputStream().write(csvBuilder.toString().getBytes(StandardCharsets.UTF_8));
			response.getOutputStream().flush();
			
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}