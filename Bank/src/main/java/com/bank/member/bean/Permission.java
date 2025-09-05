package com.bank.member.bean;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "permission")
@Component
public class Permission {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore 
	private Role role;
    
    @ManyToOne
    @JoinColumn(name = "page_id")
	private Page page;
    
    
    

	public Permission() {
		super();
	}


	

	public Permission(Role role, Page page) {
		super();
		this.role = role;
		this.page = page;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
    
    
}
