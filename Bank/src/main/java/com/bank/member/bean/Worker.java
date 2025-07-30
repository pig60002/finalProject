package com.bank.member.bean;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Component
@Entity
@Table(name = "worker")
public class Worker implements Serializable{
	

	private static final long serialVersionUID = 1L;

	public Worker() {
		super();
	}
	@Id
	@Column(name = "w_id")
	private Integer wId;
	@Column(name = "w_Name")
	private String wName;
	@Column(name = "w_Account")
	private String wAccount;
	@Column(name = "w_Password")
	private String wPassword;
	
	
	/*@ManyToOne
    @JoinColumn(name = "role_id")
	private Role role;
	*/
	

	public Integer getWId() {
		return wId;
	}
	public void setWId(Integer wId) {
		this.wId = wId;
	}
	public String getWName() {
		return wName;
	}
	public void setWName(String wName) {
		this.wName = wName;
	}
	public String getWAccount() {
		return wAccount;
	}
	public void setWAccount(String wAccount) {
		this.wAccount = wAccount;
	}
	public String getWPassword() {
		return wPassword;
	}
	public void setWPassword(String wPassword) {
		this.wPassword = wPassword;
	}
	/*
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	*/
}
