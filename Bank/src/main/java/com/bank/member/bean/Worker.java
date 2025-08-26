package com.bank.member.bean;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Component

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
	
	@ManyToOne
    @JoinColumn(name = "role_id")
	private Role role;

	public Integer getwId() {
		return wId;
	}
	public void setwId(Integer wId) {
		this.wId = wId;
	}
	public String getwName() {
		return wName;
	}
	public void setwName(String wName) {
		this.wName = wName;
	}
	public String getwAccount() {
		return wAccount;
	}
	public void setwAccount(String wAccount) {
		this.wAccount = wAccount;
	}
	public String getwPassword() {
		return wPassword;
	}
	public void setwPassword(String wPassword) {
		this.wPassword = wPassword;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	
	
	
	
	

	
	
}
