package com.bank.member.bean;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerDto{
	



	public WorkerDto() {
		super();
	}

	private Integer wId;
	private String wName;
	private String wAccount;
	private String token;
	private Role role;
	
	public WorkerDto(Integer wId, String wName, String wAccount, String token) {
		super();
		this.wId = wId;
		this.wName = wName;
		this.wAccount = wAccount;
		this.token = token;
	}
	public WorkerDto(Worker worker,String token) {
		this.wId = worker.getwId();
		this.wName = worker.getwName();
		this.wAccount = worker.getwAccount();
		this.token = token;
		this.role = worker.getRole();
		
	}
	
	


	
}
