package com.bank.loan.enums;

public enum LoanTypeEnum {
    LT001("車貸"),
    LT002("房貸"),
    LT003("學貸"),
    UNKNOWN("未知類型");

    private final String displayName;

    LoanTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String fromId(String id) {
        for (LoanTypeEnum type : values()) {
            if (type.name().equals(id)) {
                return type.getDisplayName();
            }
        }
        return UNKNOWN.getDisplayName();
    }
}
