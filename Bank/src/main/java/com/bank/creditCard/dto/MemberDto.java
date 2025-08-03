package com.bank.creditCard.dto;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({"mname", "midentity", "mbirthday", "mphone", "maddress"})
public class MemberDto {

	private String mName;
	private String mIdentity;
	private String mAddress;
	private String mPhone;
	private Date mBirthday;
	
}
