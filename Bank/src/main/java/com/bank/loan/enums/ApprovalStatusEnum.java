package com.bank.loan.enums;

public enum ApprovalStatusEnum {
    approved("審核通過"),
    supplement("補件中"),
    rejected("拒絕申請"),
    pending("待審核"),
    unknown("未知狀態");

    private final String displayName;

    ApprovalStatusEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String fromCode(String code) {
        for (ApprovalStatusEnum status : values()) {
            if (status.name().equals(code)) {
                return status.getDisplayName();
            }
        }
        return unknown.getDisplayName();
    }
}
