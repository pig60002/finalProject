package com.bank.member.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.member.bean.Page;

public interface PageRepository extends JpaRepository<Page, Integer> {
	Optional<Page> findByPageName(String pageName);

}
