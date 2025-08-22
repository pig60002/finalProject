package com.bank.account.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.account.bean.AccountApplication;


public interface AccAppRepository extends JpaRepository<AccountApplication, String> {

	// 依狀態查詢 開戶申請表 getAccAppByStatus(String status1,String status2)
	@Query("SELECT aa FROM AccountApplication aa WHERE aa.status IN :statuses")
	List<AccountApplication> getAccAppByStatus(@Param("statuses") List<String> statuses);
	
	// 查詢會員詳細資料(審核用) getAccAppDetail(String applicationId)
//	@Query("SELECT aa.applicationId,m.mId,m.mName,m.mIdentity,m.mGender,m.mAddress,m.mPhone,m.mBirthday"
//			+ ",m.mEmail,aa.idCardFront,aa.idCardBack,aa.secondDoc,aa.status,aa.reviewerId,aa.reviewTime,aa.rejectionReason,aa.applyTime FROM AccountApplication aa JOIN aa.member "
//			+ " m WHERE aa.applicationId=:appId")
//	Object[] getAccAppDetail(@Param("appId") String applicationId);
	
	AccountApplication findByApplicationId(String applicationId); //可以直接用這個代替
	
	// 修改單筆開戶申請表 (審核狀態) updateAccApp(String appId, String status, String reason, int reviewerId)
	@Modifying // JPQL 修改刪除都要加 @Modifying
	@Query("UPDATE AccountApplication aa SET aa.status=:status,aa.reviewerId=:rwId,aa.reviewTime=:rwTime,"
			+ "aa.rejectionReason=:rjReason WHERE aa.applicationId=:appId")
	int updateAccApp (@Param("status")	  String status,
					   @Param("rwId") 	  Integer rwId,
					   @Param("rwTime")   LocalDateTime rwTime,
					   @Param("rjReason") String rjReason,
					   @Param("appId")    String appId);
	
	// 查詢最新筆申請
	AccountApplication findTopByMIdOrderByApplyTimeDesc(Integer mId);
}
