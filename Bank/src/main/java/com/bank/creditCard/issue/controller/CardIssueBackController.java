package com.bank.creditCard.issue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.creditCard.issue.service.CardIssueService;

@RestController
@RequestMapping("/adminCard")
public class CardIssueBackController {

	@Autowired
	private CardIssueService cardIssueService;
	
	@PostMapping("/issue")
	public String issueCard(@RequestParam("applicationId") Integer applicationId) {
		try {
			cardIssueService.issueCard(applicationId);
			return "卡片已成功核發";
		} catch (IllegalStateException e) {
			return "錯誤"+e.getMessage();
		}
	}
}
