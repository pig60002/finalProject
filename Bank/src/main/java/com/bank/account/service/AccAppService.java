package com.bank.account.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bank.account.bean.AccountApplication;
import com.bank.account.dao.AccAppRepository;
import com.bank.utils.AccountUtils;


@Service
@Transactional
public class AccAppService {

	@Autowired
	private AccAppRepository accAppRepos;
	@Autowired
	private SerialControlService scService;

	// 新增 
	public AccountApplication insertAccApp(MultipartFile idfront, MultipartFile idback, MultipartFile secdoc,Integer mid ,String status) {
		
		AccountApplication accApp = new AccountApplication();
		
		AccountUtils accountUtil = new AccountUtils();
		accApp.setIdCardFront( accountUtil.savePicture(idfront, mid) );
		accApp.setIdCardBack( accountUtil.savePicture(idback, mid) );
		String saveSecDoc = null;
		
		if( secdoc != null && !secdoc.isEmpty() ) {
			saveSecDoc = accountUtil.savePicture(secdoc, mid);
		} 
		accApp.setSecondDoc(saveSecDoc);
		
		String id = scService.getSCNB("application", AccountUtils.getTodayCode() );
		accApp.setApplicationId(id);
		accApp.setmId(mid);
		accApp.setStatus(status);
		accApp.setApplyTime( LocalDateTime.now() );

		return accAppRepos.save(accApp);
	}
	
	

	// 修改 updateAccApp(AccountApplication accApp)
	public AccountApplication updateAccApp(AccountApplication accApp) {
		return accAppRepos.save(accApp);
	}

	// 修改單筆開戶申請表 (審核狀態) updateAccApp(String appId, String status, String reason, int reviewerId)
	public int updateAccApp( String status, Integer reviewerId, String reason,String appId) {
		return accAppRepos.updateAccApp(status, reviewerId, LocalDateTime.now(), reason, appId);
	}

	// 刪除 deleteAccAppById(String id)
	public void deleteAccAppById(String appid) {
		accAppRepos.deleteById(appid);
	}

	// 依編號查詢單筆開戶申請表 getAccAppById(String id)
	public AccountApplication getAccAppById(String id) {
		Optional<AccountApplication> op = accAppRepos.findById(id);
		if (op.isPresent()) {
			return op.get();
		}
		return null;
	}

	// 依狀態查詢 開戶申請表 getAccAppByStatus()
	public List<AccountApplication> getAccAppByStatus(List<String> statuses) {
		
		return accAppRepos.getAccAppByStatus(statuses);
	}

	// 查詢會員詳細資料(審核用) getAccAppDetail(String applicationId)
	public AccountApplication getAccAppDetail(String applicationId) {
		Optional<AccountApplication> appResult = accAppRepos.findById(applicationId);
		
		if(appResult.isPresent()) {
			return appResult.get();
		}
		return null;
	}

}
