package com.bank.creditCard.application.controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.application.model.CardApplicationBean;
import com.bank.creditCard.application.service.CardApplicationService;
import com.bank.creditCard.dto.CardApplicationDTO;

@RestController
@RequestMapping("/cardBack")
public class CardApplicationBackController {
	
	@Autowired
	private CardApplicationService cService;
	
	@PostMapping("/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestParam("applicationId") int applicationId,@RequestParam("action") String action){
		
		String status;
		if("approve".equals(action)) {
			status=CardApplicationBean.STATUS_APPROVED;
		}else if("reject".equals(action)) {
			status=CardApplicationBean.STATUS_REJECTED;
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知的操作"+action);
		}
		boolean updated = cService.updateStatus(applicationId, status, LocalDateTime.now());
		if(updated) {
			return ResponseEntity.ok("成功");
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失敗");
		}
	}
	

	@GetMapping("/review")
	public ResponseEntity reviewApp() {

		List<CardApplicationDTO> pendingList = cService.findPendingWithMemberInfo();
		List<CardApplicationDTO> approvedList = cService.findApprovedWithMemberInfo();
		
		Map<String, List<CardApplicationDTO>> result = new HashMap<>();
        result.put("pendingList", pendingList);
        result.put("approvedList", approvedList);

        return ResponseEntity.ok(result);
	}
	

}
