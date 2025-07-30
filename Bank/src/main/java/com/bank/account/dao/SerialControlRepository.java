package com.bank.account.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.account.bean.SerialControl;
import com.bank.account.bean.SerialControlId;

public interface SerialControlRepository extends JpaRepository<SerialControl, SerialControlId> {

	// 查詢 lastNumber 如果沒有 Integer 是回傳null
	@Query("SELECT sc.lastNumber FROM SerialControl sc WHERE sc.type=:type AND sc.keyCode=:keyCode")
	Integer getlastNB(@Param("type") String type, @Param("keyCode") String keyCode);

	// 更新lastNumber
	@Modifying
	@Query("update SerialControl sc set sc.lastNumber=:lastnb WHERE sc.type=:type AND sc.keyCode=:keyCode")
	int updatelastNB(@Param("lastnb") Integer lastnb, @Param("type") String type, @Param("keyCode") String keyCode);
	
}
