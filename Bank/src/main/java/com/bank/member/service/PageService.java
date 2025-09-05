package com.bank.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.member.bean.Page;
import com.bank.member.dao.PageRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PageService {
	
	@Autowired
	private PageRepository pageRepository;
	
	public List<Page> getAllPage() {
        return pageRepository.findAll();
    }
	
	public Page getPageById(Integer id) {
        Optional<Page> op = pageRepository.findById(id);
        if(op.isPresent()) {
        	return op.get();
        }
        return null;
    }
	
}
