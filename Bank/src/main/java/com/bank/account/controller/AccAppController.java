package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bank.account.bean.AccountApplication;
import com.bank.account.service.AccAppService;

@RestController
public class AccAppController {

	@Autowired
	private AccAppService accAppService;

	// 依狀態查詢
	@GetMapping("/account/application/getbystatus.controller")
	public List<AccountApplication> processGetAccAppByStatusAction(){
		List<AccountApplication> list1 = accAppService.getAccAppByStatus("通過", "未通過");
		return list1;
	}
	
	// 修改單筆開戶申請表 (審核狀態)
	@PutMapping("/account/application/update.controller/{appid}")
	public int processAccAppAction(@RequestParam String status, @RequestParam int rwId, 
			@RequestParam String rjReason, @PathVariable String appid) {
		return accAppService.updateAccApp(status,rwId,rjReason,appid);
	}
	
	// 新增帳戶申請
	@PostMapping("/account/application/insert.controller/{mid}")
	public AccountApplication processInsertAction(@RequestParam MultipartFile idfront,
												  @RequestParam MultipartFile idback,
												  @RequestParam(required = false) MultipartFile secDec, //(required = false)可以沒有
												  @PathVariable Integer mid) {
		return accAppService.insertAccApp(idfront, idback, secDec, mid);
	}
	
	// 依申請編號查詢會員詳細資料(審核用)
	@GetMapping("/account/application/getdetail.controller/{appid}")
	public AccountApplication processGetAccAppDetailsAction(@PathVariable String appid) {
		return accAppService.getAccAppDetail(appid);
	}
	
	// 依申請編號刪除
	@DeleteMapping("/account/application/delete.controller/{appid}")
	public void processDeleteAccAppByAppIdAction(@PathVariable String appid) {
		accAppService.deleteAccAppById(appid);
	}

}
