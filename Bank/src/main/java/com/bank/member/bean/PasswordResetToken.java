package com.bank.member.bean;



import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_token")
@Component
public class PasswordResetToken {
	
	public PasswordResetToken() {
		super();
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id", referencedColumnName = "m_id")
	private Member member;
	
	@Column(name = "token")
	private String token;
	
	@CreationTimestamp
	@Column(name = "expiry", updatable = false)
	private Date expiry;
	
	
	
	public PasswordResetToken(Integer id, Member member, String token, Date expiry) {
		super();
		this.id = id;
		this.member = member;
		this.token = token;
		this.expiry = expiry;
	}
	
	
	public PasswordResetToken(Member member, String token, Date expiry) {
		super();
		this.member = member;
		this.token = token;
		this.expiry = expiry;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	
	
	

}
