package com.bank.account.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.account.bean.Transactions;
import com.bank.account.service.transactions.DepositWithdrawalTxService;
import com.bank.account.service.transactions.InternalTransferService;
import com.bank.account.service.transactions.TransactionsService;
import com.bank.member.bean.Worker;
import com.bank.member.service.WorkerLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TransactionsController {
	
	@Autowired
	private TransactionsService transactionsService;
	
	@Autowired
	private DepositWithdrawalTxService dwTxSerice;
	
	@Autowired
	private InternalTransferService interTxService;

	@Autowired
	private ObjectMapper mapper; // 由 Spring 注入，已註冊 JavaTimeModule

	@Autowired
	private WorkerLogService workerLogService;
	
	// 提款or存款
	// Postman : {"accountId":"7100000237","transactionType":"存款","amount":"1000","memo":"07/31"}
	@PutMapping("/account/transaction/depositwithdrawal")
	public Transactions processDepositWithdrawalAction(@RequestBody Transactions txRequest) {
		Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Transactions txRs = dwTxSerice.depositWithdrawalAction(txRequest);
		
		if(principal instanceof Worker) {
			Worker worker  = (Worker) principal;
			workerLogService.logAction(worker.getwId(),txRequest.getTransactionType(),"帳戶編號:"+txRequest.getAccountId()+",金額:"+txRequest.getAmount()+",餘額:"+txRs.getBalanceAfter());
		}
		
		return txRs;
	}
	
	// 內部轉帳
	// Postman :{"accountId":"7100000028","transactionType":"內部轉帳","toAccountId":"7100000237","amount":"1000","operatorId":1}
	@PutMapping("/account/transaction/internaltransfer")
	public Transactions processInternalTransferAction(@RequestBody Transactions txRequest) {
		Object principal =SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			Transactions txRs = interTxService.internalTransferAction(txRequest);
			
			if(principal instanceof Worker) {
				Worker worker  = (Worker) principal;
				workerLogService.logAction(worker.getwId(),"轉帳","帳戶編號:"+txRequest.getAccountId()+",金額:"+txRequest.getAmount()+",餘額:"+txRs.getBalanceAfter());
			}
			
			return txRs;
			
		} catch (RuntimeException e) {

			Transactions errorTx = new Transactions();
			errorTx.setStatus("交易失敗");
			errorTx.setMemo("交易錯誤:"+ e.getMessage());
			return errorTx;
		}
		
	}
	
	// 依帳戶號碼查尋交易明細
	@GetMapping("/account/transaction/gettransactionsrecords")
	public List<Transactions> processGetTransactionsRecordAction(@RequestParam String accountId,
																 @RequestParam(required = false) String startDate, 
																 @RequestParam(required = false) String endDate){
		
		return transactionsService.getTransactionByAccountId(accountId, startDate, endDate);
	}
	
	// 輸出json檔
	@GetMapping("/account/transaction/exportjson")
	public ResponseEntity<byte[]> exportJson(@RequestParam String accountId,
											 @RequestParam(required = false) String startDate, 
											 @RequestParam(required = false) String endDate,
											 @RequestParam String type) throws Exception{
		
		List<Transactions> txRecords = null;
		
		if( "client".equals(type) ) {
			txRecords = transactionsService.getTxSuccessRecords(accountId, startDate, endDate);
		} else if("worker".equals(type)) {
			txRecords = transactionsService.getTransactionByAccountId(accountId, startDate, endDate);
		}
		
		byte[] bytes = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(txRecords);

		String s = (startDate == null || startDate.isBlank()) ? "NA" : startDate;
		String e = (endDate == null || endDate.isBlank()) ? "NA" : endDate;
		String filename = "transactions_%s_%s_%s.json".formatted(accountId, s, e);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.contentType(MediaType.APPLICATION_JSON)
				.body(bytes);
	}

	// 查詢帳戶所有"成功"交易
	@GetMapping("/account/transaction/getsuccesstxrecords")
	public List<Transactions> processTxSuccessRecordAction(@RequestParam String accountId, 
														   @RequestParam(required = false) String startDate,
														   @RequestParam(required = false) String endDate){
		
		return transactionsService.getTxSuccessRecords(accountId, startDate, endDate);
	}
	
	// 找最近有轉出的帳號
	@GetMapping("/account/transaction/getrecenttoaccountid")
	public List<String> getRecentToAccountIdAction(@RequestParam String accountId){
		return transactionsService.findOutgoingSuccess(accountId);
	}
	
}
