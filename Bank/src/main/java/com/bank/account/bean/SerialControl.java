package com.bank.account.bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity @Table(name="serial_control")
@IdClass(SerialControlId.class) //複合主鍵 要加上 @IdClass(實作Serializable的class)
public class SerialControl {

	@Id @Column(name="type") //兩個主鍵都要加 @Id
	private String type;
	
	@Id @Column(name="key_code") //兩個主鍵都要加 @Id
	private String keyCode;
	
	@Column(name="last_number")
	private int lastNumber;
	
	public SerialControl() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public int getLastNumber() {
		return lastNumber;
	}

	public void setLastNumber(int lastNumber) {
		this.lastNumber = lastNumber;
	}

	public SerialControl(String type, String keyCode, int lastNumber) {
		super();
		this.type = type;
		this.keyCode = keyCode;
		this.lastNumber = lastNumber;
	}

}
