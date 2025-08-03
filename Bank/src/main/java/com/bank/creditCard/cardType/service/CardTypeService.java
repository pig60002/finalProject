package com.bank.creditCard.cardType.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.creditCard.application.dao.CardApplicationRepository;
import com.bank.creditCard.application.service.CardApplicationService;
import com.bank.creditCard.cardType.dao.CardTypeRepository;
import com.bank.creditCard.cardType.model.CardTypeBean;

@Service
@Transactional
public class CardTypeService {

    private final CardApplicationRepository cardApplicationRepository;

    private final CardApplicationService cardApplicationService;

	@Autowired
	private CardTypeRepository CTRepos;

    CardTypeService(CardApplicationService cardApplicationService, CardApplicationRepository cardApplicationRepository) {
        this.cardApplicationService = cardApplicationService;
        this.cardApplicationRepository = cardApplicationRepository;
    }
	
	public List<CardTypeBean> findAll(){
		return CTRepos.findAll();
	}
	
	public CardTypeBean findById(int cardTypeId) {
		return CTRepos.findById(cardTypeId).orElse(null);
	}
	
	public Map<Integer, String> getCardTypeMap(){
		List<Object[]> rawList = CTRepos.findIdAndTypeCode();
		HashMap<Integer, String> result = new HashMap<>();
		for(Object[] row:rawList) {
			Integer id=(Integer) row[0];
			String code=(String) row[1];
			result.put(id, code);
		}
		return result;
	}
}
