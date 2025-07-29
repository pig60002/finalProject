package com.bank.account.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.bean.SerialControl;
import com.bank.account.dao.SerialControlRepository;

@Service
@Transactional
public class SerialControlService {

	@Autowired
	private SerialControlRepository scRepos;
	
	public String getSCNB(String type, String keyCode) {
		
		// 找到最大的號碼
		Integer scResult = scRepos.getlastNB(type, keyCode);
		
		int newNB = 1;
		
		//如果有查詢到lastNB就數字+1
		if(scResult != null) {
			
			int lastNB = scResult;
			newNB = lastNB+1;
			
			// 更新lastNB
			boolean updateRS = scRepos.updatelastNB(newNB, type, keyCode)>0;
			if(updateRS) {
				System.out.println("LastNumber更新成功");
			} else {
				System.out.println("LastNumber更新失敗");
			}
		} else {
			// 如果沒有查到就新增
			SerialControl updateBean = new SerialControl(type,keyCode,newNB);
			SerialControl saveRS = scRepos.save(updateBean);
			if(saveRS != null) {
				System.out.println("流水號新增成功");
			} else {
				System.out.println("流水號新增失敗");
			}
		} 
		
		if("account".equals(type)) {
			return "7"+keyCode+String.format("%05d", newNB)+(int)(Math.random()*10);
		} else {
			return keyCode+String.format("%04d", newNB);
		}
		
	}
}
