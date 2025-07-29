package com.bank.account.bean;

import java.io.Serializable;
import java.util.Objects;

public class SerialControlId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 兩個複合主鍵 要另外寫一個class 並實作Serializable ，並且要覆寫 hashCode,equals
	private String type;
	private String keyCode;
	
	public SerialControlId() {
	}

	public SerialControlId(String type, String keyCode) {
		super();
		this.type = type;
		this.keyCode = keyCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(keyCode, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerialControlId other = (SerialControlId) obj;
		return Objects.equals(keyCode, other.keyCode) && Objects.equals(type, other.type);
	}

}
