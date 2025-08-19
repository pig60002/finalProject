package com.bank.member.bean;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "page")
@Component
public class Page {
	
	@Id
	@Column(name="page_id")
	private Integer pageId;
	@Column(name="page_name")
	private String pageName;
	@Column(name="page_exp")
	private String pageExp;
	
	public Page() {
		super();
	}

	public Page(Integer pageId, String pageName, String pageExp) {
		super();
		this.pageId = pageId;
		this.pageName = pageName;
		this.pageExp = pageExp;
	}

	public Page(String pageName, String pageExp) {
		super();
		this.pageName = pageName;
		this.pageExp = pageExp;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageExp() {
		return pageExp;
	}

	public void setPageExp(String pageExp) {
		this.pageExp = pageExp;
	}
	
	
	

}
