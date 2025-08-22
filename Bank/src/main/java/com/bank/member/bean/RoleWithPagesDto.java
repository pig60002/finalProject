package com.bank.member.bean;

import java.util.List;

public class RoleWithPagesDto {
	private Role role;
    private List<Page> pages;

    public RoleWithPagesDto(Role role, List<Page> pages) {
        this.role = role;
        this.pages = pages;
    }

    // Getters and Setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
