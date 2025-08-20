package com.bank.creditCard.issue.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.stereotype.Service;

import com.bank.creditCard.application.dao.CardApplicationRepository;
import com.bank.creditCard.application.model.CardApplicationBean;

import com.bank.creditCard.cardType.dao.CardTypeRepository;
import com.bank.creditCard.cardType.model.CardTypeBean;
import com.bank.creditCard.issue.dao.CardDetailRepository;
import com.bank.creditCard.issue.dao.CardLimitRepository;
import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.issue.model.CardLimitBean;

@Service
public class CardIssueService {
	
	@Autowired
	private CardApplicationRepository cardApplicationRepository;
	
	@Autowired
	private CardTypeRepository cardTypeRepository;
	
	@Autowired
	private CardDetailRepository cardDetailRepository;
	
	@Autowired
	private CardLimitRepository cardLimitRepository;
	
	public void issueCard(Integer applicationId) {
		//1.取得申請單
		CardApplicationBean app = cardApplicationRepository.findById(applicationId)
				.orElseThrow(()->new IllegalStateException("找不到申請單"));
		//2.確認申請狀態
		if(!"approved".equalsIgnoreCase(app.getStatus())) {
			throw new IllegalStateException("尚未通過審核");
		}
		//3.取得卡種資料
		CardTypeBean cardType = cardTypeRepository.findById(app.getCardType())
				.orElseThrow(()->new IllegalStateException("找不到卡種"));
		//4.產生卡號
		String cardCode=generateCardCode();
		while (cardDetailRepository.existsByCardCode(cardCode)) {
			cardCode=generateCardCode();
		}
		
		//5.產生CVV
		String cvv=generateCVV();
		
		//6.有效期限
		LocalDate issueDate = LocalDate.now();
		LocalDate expirationDate = issueDate.plusYears(5);
		
		//7.建立卡片
		CardDetailBean cardDetail = new CardDetailBean();
		cardDetail.setCardCode(cardCode);
        cardDetail.setMember(app.getMember()); 
        cardDetail.setCardType(cardType);      
        cardDetail.setCvvCode(cvv);
        cardDetail.setExpirationDate(expirationDate);
        cardDetail.setIssuedDate(issueDate);
        cardDetail.setCreditLimit(cardType.getDefaultLimit()); // 從卡種抓預設額度
        cardDetail.setCurrentBalance(BigDecimal.ZERO); // 初始為 0
        cardDetail.setStatus(CardDetailBean.STATUS_INACTIVE);
        cardDetailRepository.save(cardDetail);
        
     // 8. 建立額度紀錄
        CardLimitBean limitRecord = new CardLimitBean();
        limitRecord.setCardDetail(cardDetail);
        limitRecord.setCreditLimit(cardType.getDefaultLimit()); // 從卡種抓預設額度
        limitRecord.setEffectiveDate(issueDate);
        limitRecord.setStatus("ACTIVE");
        cardLimitRepository.save(limitRecord);
        
     // 9. 更新申請單狀態為 ISSUED（已發卡）
        app.setStatus(CardApplicationBean.STATUS_ISSUED);
        cardApplicationRepository.save(app);
        		
	}
	
	private String generateCardCode() {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<16;i++) {
			sb.append(rand.nextInt(10));
		}
		return sb.toString();
	}
	
	private String generateCVV() {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<3;i++) {
			sb.append(rand.nextInt(10));
		}
		return sb.toString();
	}
	
	//mId查詢卡片
	public List<CardDetailBean> getCardByMemberId(Integer mId) {
        return cardDetailRepository.findByMember_MId(mId);
    }
	
	// 通用改卡片狀態（開卡 / 停卡）
    public boolean updateCardStatus(Integer cardId, String status) {
    	List<String> validStatuses = List.of(
    	        CardDetailBean.STATUS_ACTIVE.toLowerCase(),
    	        CardDetailBean.STATUS_INACTIVE.toLowerCase(),
    	        CardDetailBean.STATUS_SUSPEND.toLowerCase()
    	    );
    	    if (!validStatuses.contains(status.toLowerCase())) {
    	        return false;
    	    }
    	    return cardDetailRepository.findById(cardId).map(cardDetail -> {
    	        cardDetail.setStatus(status.toLowerCase());
    	        cardDetailRepository.save(cardDetail);
    	        return true;
    	    }).orElse(false);
    }

}
