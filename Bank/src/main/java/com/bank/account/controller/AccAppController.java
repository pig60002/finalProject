package com.bank.account.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.account.bean.AccountApplication;
import com.bank.account.service.AccAppService;

@RestController
public class AccAppController {

	@Autowired
	private AccAppService accAppService;
	

	// 查詢 "已審核"狀態 
	@GetMapping("/account/application/getrwdone")
	public List<AccountApplication> processGetAccAppReviewDoneAction(){
		return accAppService.getAccAppByStatus(Arrays.asList("通過", "未通過" ,"待補件"));
	}

	// 查詢 "未審核"狀態
	@GetMapping("/account/application/getrwundone")
	public List<AccountApplication> processGetAccAppReviewUnDoneAction() {
		return accAppService.getAccAppByStatus(Arrays.asList("待審核", "已補件待審核"));
	}

	// 修改單筆開戶申請表 (審核狀態)ResponseEntity 
	// ResponseEntity是 Spring MVC 裡用來完整控制 HTTP 回應（response）內容的類別。
	// 回傳的狀態碼（status code）回傳的資料內容（body），可以是字串、物件、JSON 等
	@PutMapping("/account/application/update")
	public ResponseEntity<String> processAccAppAction(@RequestBody AccountApplication accapp) {
		// PostMan測試{"status":"待審核","reviewerId":1,"rejectionReason":"身分不符","applicationId":"202506270007"} 
		int updateRS = accAppService.updateAccApp(
				accapp.getStatus(),
				accapp.getReviewerId(),
				accapp.getRejectionReason(),
				accapp.getApplicationId(),
				accapp.getmId()
		);
		
		
		if( updateRS > 0 ) {
			return ResponseEntity.ok("更新成功");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("更新失敗");
		}
		
	}
	
	// 新增帳戶申請
	@PostMapping("/account/application/insert")
	public ResponseEntity<Map<String, Object>> processInsertAction(@RequestParam MultipartFile idfront,
												  	  @RequestParam MultipartFile idback,
												  	  @RequestParam(required = false) MultipartFile secDoc, //(required = false)可以沒有
												  	  @RequestParam Integer mid,
												  	  @RequestParam(required = false)String status,
												  	  @RequestParam String mName,
												  	  @RequestParam String mEmail) {
		
		status = (status!=null) ? status : "待審核" ;
		
		AccountApplication insertRS = accAppService.insertAccApp(idfront, idback, secDoc, mid, status, mName, mEmail);
		
		
		if( insertRS != null ) {
			return ResponseEntity.ok(Map.<String, Object>of("success",true,"appId",insertRS.getApplicationId()));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body( Map.<String, Object>of("success", false));
		}
	}
	
	// 依申請編號查詢會員詳細資料(審核用)
	@GetMapping("/account/application/getdetail.controller/{appid}")
	public AccountApplication processGetAccAppDetailsAction(@PathVariable String appid) {
		return accAppService.getAccAppDetail(appid);
	}
	
	@GetMapping("/account/application/status")
    public String getApplicationStatus(@RequestParam Integer mId) {
		return accAppService.getLatestStatus(mId);
    }

}
