package com.bank.loan.enums;

public enum LoanTermEnum {
	TERM001("短期貸款"),
	TERM002("中期貸款"),
	TERM003("長期貸款"),
    UNKNOWN("未知類型");

    private final String displayName;

    LoanTermEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String fromId(String id) {
        for (LoanTermEnum type : values()) {
            if (type.name().equals(id)) {
                return type.getDisplayName();
            }
        }
        return UNKNOWN.getDisplayName();
    }
}
