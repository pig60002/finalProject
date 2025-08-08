package com.bank.member.bean;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
	private Integer mId;
	private String mName;
	private String mIdentity;
	private String mGender;
	private String mAddress;
	private String mPhone;
	private Date mBirthday;
	private String mEmail;
	private String token;
	
	public MemberDto(Integer mId, String mName, String mIdentity, String mGender, String mAddress, String mPhone,
			Date mBirthday, String mEmail, Integer mState, String token) {
		super();
		this.mId = mId;
		this.mName = mName;
		this.mIdentity = mIdentity;
		this.mGender = mGender;
		this.mAddress = mAddress;
		this.mPhone = mPhone;
		this.mBirthday = mBirthday;
		this.mEmail = mEmail;
		this.token = token;
	}
	
	public MemberDto(Member m,String token) {
		this.mId = m.getmId();
		this.mName = m.getmName();
		this.mIdentity = m.getmIdentity();
		this.mGender = m.getmGender();
		this.mAddress = m.getmAddress();
		this.mPhone = m.getmPhone();
		this.mBirthday = m.getmBirthday();
		this.mEmail = m.getmEmail();
		this.token = token;		
	}
}
