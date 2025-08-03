package com.bank.fund.dto;

public class FundAccountDto {

	private Integer id;
	private String name;
	private String riskType;
	private String status;
	
	public FundAccountDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FundAccountDto(String name, String riskType, String status) {
		super();
		this.name = name;
		this.riskType = riskType;
		this.status = status;
	}

	public FundAccountDto(Integer id, String name, String riskType, String status) {
		super();
		this.id = id;
		this.name = name;
		this.riskType = riskType;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
