package com.bank.member.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "member")
@Component
public class Member implements Serializable{
	

	private static final long serialVersionUID = 1L;

	public Member() {
		super();
	}
	@Id
	@Column(name = "m_id")
	private Integer mId;
	
	@Column(name = "m_name")
	private String mName;
	
	@Column(name = "m_identity")
	private String mIdentity;
	
	@Column(name = "m_gender")
	private String mGender;
	
	@Column(name = "m_account")
	private String mAccount;
	
	@Column(name = "m_password")
	private String mPassword;
	
	@Column(name = "m_address")
	private String mAddress;
	
	@Column(name = "m_phone")
	private String mPhone;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	@Column(name = "m_birthday")
	private Date mBirthday;
	
	@Column(name = "m_email")
	private String mEmail;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	@Column(name = "creation")
	private Date creation;
	
	@Column(name = "m_state")
	private Integer mState;
	

	public Member(Integer mId, String mName, String mIdentity, String mGender, String mAccount, String mPassword,
			String mAddress, String mPhone, Date mBirthday, String mEmail, Date creation, Integer mState) {
		super();
		this.mId = mId;
		this.mName = mName;
		this.mIdentity = mIdentity;
		this.mGender = mGender;
		this.mAccount = mAccount;
		this.mPassword = mPassword;
		this.mAddress = mAddress;
		this.mPhone = mPhone;
		this.mBirthday = mBirthday;
		this.mEmail = mEmail;
		this.creation = creation;
		this.mState = mState;
	}

	public Member(String mName, String mIdentity, String mGender, String mAccount, String mPassword, String mAddress,
			String mPhone, Date mBirthday, String mEmail, Date creation, Integer mState) {
		super();
		this.mName = mName;
		this.mIdentity = mIdentity;
		this.mGender = mGender;
		this.mAccount = mAccount;
		this.mPassword = mPassword;
		this.mAddress = mAddress;
		this.mPhone = mPhone;
		this.mBirthday = mBirthday;
		this.mEmail = mEmail;
		this.creation = creation;
		this.mState = mState;
	}

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public String getmIdentity() {
		return mIdentity;
	}

	public void setmIdentity(String mIdentity) {
		this.mIdentity = mIdentity;
	}

	public String getmGender() {
		return mGender;
	}

	public void setmGender(String mGender) {
		this.mGender = mGender;
	}

	public String getmAccount() {
		return mAccount;
	}

	public void setmAccount(String mAccount) {
		this.mAccount = mAccount;
	}

	public String getmPassword() {
		return mPassword;
	}

	public void setmPassword(String mPassword) {
		this.mPassword = mPassword;
	}

	public String getmAddress() {
		return mAddress;
	}

	public void setmAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getmPhone() {
		return mPhone;
	}

	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	public Date getmBirthday() {
		return mBirthday;
	}

	public void setmBirthday(Date mBirthday) {
		this.mBirthday = mBirthday;
	}

	public String getmEmail() {
		return mEmail;
	}

	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public Date getCreation() {
		return creation;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public Integer getmState() {
		return mState;
	}

	public void setmState(Integer mState) {
		this.mState = mState;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Member [mId=" + mId + ", mName=" + mName + ", mIdentity=" + mIdentity + ", mGender=" + mGender
				+ ", mAccount=" + mAccount + ", mPassword=" + mPassword + ", mAddress=" + mAddress + ", mPhone="
				+ mPhone + ", mBirthday=" + mBirthday + ", mEmail=" + mEmail + ", creation=" + creation + ", mState="
				+ mState + "]";
	}
	
//	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
//    private List<LoanBean> loans;
	

}