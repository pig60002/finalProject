package com.bank.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.member.bean.Page;
import com.bank.member.service.PageService;

@RestController
@RequestMapping(path = "/page")
public class PageController {
	
	
	@Autowired
	private PageService pageService;
	
	@GetMapping("/pageAll")
	public List<Page> getAllPage(){
		return pageService.getAllPage();
	}

}
