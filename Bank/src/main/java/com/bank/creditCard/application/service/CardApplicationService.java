package com.bank.creditCard.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.member.bean.Member;
import com.bank.creditCard.application.dao.CardApplicationRepository;
import com.bank.creditCard.application.model.CardApplicationBean;
import com.bank.creditCard.cardType.dao.CardTypeRepository;
import com.bank.creditCard.dto.CardApplicationDTO;
import com.bank.creditCard.dto.MemberDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CardApplicationService {

    private final CardTypeRepository cardTypeRepository;

	@Autowired
	private CardApplicationRepository cardApplicationRepos;

    CardApplicationService(CardTypeRepository cardTypeRepository) {
        this.cardTypeRepository = cardTypeRepository;
    }

	public int insertApplication(CardApplicationBean bean) {
		bean.setStatus(CardApplicationBean.STATUS_PENDING);
		CardApplicationBean save = cardApplicationRepos.save(bean);
		return save.getApplicationId();
	}
	
	public List<CardApplicationBean> findByUserId(int userId){
		return cardApplicationRepos.findByUserId(userId);
	}
	
	public CardApplicationBean findById(int applicationId) {
		return cardApplicationRepos.findById(applicationId).orElse(null);
	}
	
	public List<CardApplicationBean> findAll(){
		return cardApplicationRepos.findAll();
	}
	
	public List<CardApplicationBean> findPending(){
		return cardApplicationRepos.findByStatusOrderByApplyDateDesc(CardApplicationBean.STATUS_PENDING);
	}
	
	public boolean updateStatus(int applicationId, String status, LocalDateTime reviewDate,String reviewComment) {
		CardApplicationBean bean = cardApplicationRepos.findById(applicationId).orElse(null);
		if(bean!=null) {
			bean.setStatus(status);
			bean.setReviewDate(reviewDate);
			bean.setReviewComment(reviewComment);
			return true;
		}
		return false;
	}
	public List<CardApplicationBean> findByStatus(String status){
		return cardApplicationRepos.findByStatusOrderByApplyDateDesc(status);
	}
	
	public List<CardApplicationBean> findByCardType(int cardType){
		return cardApplicationRepos.findByCardTypeOrderByApplyDateDesc(cardType);
	}
	
	public List<CardApplicationDTO> findPendingWithMemberInfo(){
		List<CardApplicationBean> list = cardApplicationRepos.findWithMemberByStatus("PENDING");
		return list.stream().map(this::toDto).collect(Collectors.toList());
	}

	public List<CardApplicationDTO> findApprovedWithMemberInfo(){
		List<CardApplicationBean> list = cardApplicationRepos.findWithMemberByStatus("APPROVED");
		return list.stream().map(this::toDto).collect(Collectors.toList());
	}
	
	private CardApplicationDTO toDto(CardApplicationBean bean) {
		CardApplicationDTO cDto = new CardApplicationDTO();
		cDto.setApplicationId(bean.getApplicationId());
		cDto.setUserId(bean.getUserId());
		cDto.setCardType(bean.getCardType());
		cDto.setIdPhotoFront(bean.getIdPhotoFront());
		cDto.setIdPhotoBack(bean.getIdPhotoBack());
		cDto.setFinancialProof(bean.getFinancialProof());
		cDto.setApplyDate(bean.getApplyDate());
		cDto.setStatus(bean.getStatus());
		
		Member member = bean.getMember();
		MemberDto mDto = new MemberDto();
		mDto.setMName(member.getmName());
		mDto.setMIdentity(member.getmIdentity());
		mDto.setMBirthday(member.getmBirthday());
		mDto.setMPhone(member.getmPhone());
		mDto.setMAddress(member.getmAddress());
		
		cDto.setMember(mDto);
		return cDto;
	}
}
