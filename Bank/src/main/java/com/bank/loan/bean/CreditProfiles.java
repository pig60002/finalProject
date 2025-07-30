package com.bank.loan.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_profiles")
public class CreditProfiles {
	
	private int profileId;
	private int mId;

}
