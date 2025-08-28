package com.bank.creditCard.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.creditCard.cardReward.service.CardRewardService;
import com.bank.creditCard.dto.CreditTransactionDTO;
import com.bank.creditCard.issue.dao.CardDetailRepository;
import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.transaction.dao.CreditBillRepository;
import com.bank.creditCard.transaction.dao.CreditTransactionRepository;
import com.bank.creditCard.transaction.model.CreditTransactionBean;
import com.bank.member.dao.MemberRepository;

@Service
public class CreditTransactionService {

	@Autowired
	private CreditTransactionRepository creditTransactionRepository;
	
	@Autowired
    private CardDetailRepository cardDetailRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private CreditBillRepository creditBillRepository;
	
	@Autowired
	private CardRewardService cardRewardService;
	
	private final Random random=new Random();
	
	 /**
     * 新增交易，自動生成交易碼 transactionCode，若未指定交易時間則用現在時間
     */
    public CreditTransactionBean addTransaction(CreditTransactionDTO dto) {
    	CardDetailBean card = cardDetailRepository.findById(dto.getCardId())
                .orElseThrow(() -> new RuntimeException("卡片不存在，ID=" + dto.getCardId()));

        CreditTransactionBean transaction = new CreditTransactionBean();
        transaction.setTransactionCode(generateTransactionCode());
        transaction.setCardDetail(card);
        transaction.setMember(memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("會員不存在，ID=" + dto.getMemberId())));
        transaction.setAmount(dto.getAmount());
        transaction.setMerchantType(dto.getMerchantType());
        transaction.setDescription(dto.getDescription());
        transaction.setTransactionTime(LocalDateTime.now());

        // 計算回饋
        BigDecimal cashback = calculateCashback(card, dto.getAmount(), dto.getMerchantType());
        transaction.setCashback(cashback);
        
        // 儲存交易
        CreditTransactionBean saved = creditTransactionRepository.save(transaction);
        
        int points = saved.getCashback()
                .setScale(0, java.math.RoundingMode.DOWN)
                .intValue();

        if (points > 0) {
        	cardRewardService.earnFromTransaction(
        			saved.getCardDetail().getCardId(),
        			points,
        			String.valueOf(saved.getTransactionId()) // 防重：TXN:交易ID
        		);
        }

        // 計算已用額度
        BigDecimal usedAmount = creditTransactionRepository
                .findByCardDetail_CardId(card.getCardId())
                .stream()
                .map(CreditTransactionBean::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 更新剩餘額度
        BigDecimal currentBalance = card.getCreditLimit().subtract(usedAmount);
        card.setCurrentBalance(currentBalance);
        cardDetailRepository.save(card);
        
        attachToBillIfExists(card, saved);

        return saved;
    }
    
    //計算回饋邏輯
    private BigDecimal calculateCashback(CardDetailBean card,BigDecimal amount,String merchantType) {
    	if(amount==null||amount.compareTo(BigDecimal.ZERO)<=0) {
    		return BigDecimal.ZERO;
    	}
    	
    	//判斷useCustomRule,null視為0(不使用自訂規則)
    	BigDecimal baseRate=card.getCardType().getCashbackRate();
    	Boolean useCustomRuleFlag=card.getCardType().getUseCustomRule();
    	boolean useCustomRule=Boolean.TRUE.equals(useCustomRuleFlag);
    	BigDecimal cashbackRate=baseRate;
    	
    	if(useCustomRule) {
    		BigDecimal categoryRate=getCategoryRate(merchantType);
    		//取高(不疊加)
    		//cashbackRate=categoryRate.max(baseRate);
    		//加成
    		cashbackRate = baseRate.add(categoryRate);
    	}
    	return amount.multiply(cashbackRate).divide(new BigDecimal("100"))
    				 .setScale(2,BigDecimal.ROUND_HALF_UP);
    }
    
    //消費類別回饋率
    private BigDecimal getCategoryRate(String merchantType) {
    	if("餐飲".equalsIgnoreCase(merchantType)) {
    		return new BigDecimal("3.0");
    	}else if("加油".equalsIgnoreCase(merchantType)) {
    		return new BigDecimal("2.0");
    	}
    	return BigDecimal.ZERO;
    }
    
    private int generateTransactionCode() {
        int timePart = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        int randomPart = random.nextInt(9999);
        return timePart + randomPart;
    }
	
	//查詢卡片交易
	public List<CreditTransactionBean> getTransactionsByCardId(Integer cardId){
		return creditTransactionRepository.findByCardDetail_CardId(cardId);
	}
	
	//查詢會員交易
	public List<CreditTransactionBean> getTransactionsByMemberId(Integer mId){
		return creditTransactionRepository.findByMember_MId(mId);
	}
	
	//依卡片ID和會員ID與帳單年月(yyyyMM)查詢該月所有交易
	public List<CreditTransactionBean> findByCardIdAndMemberIdAndBillingYearMonth(Integer cardId, Integer mId, String billingYearMonth) {
	    YearMonth ym = YearMonth.parse(billingYearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
	    LocalDate startDate = ym.atDay(1);
	    LocalDate endDate = ym.atEndOfMonth();

	    LocalDateTime startDateTime = startDate.atStartOfDay();
	    LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

	    return creditTransactionRepository.findByCardDetailCardIdAndMemberMIdAndTransactionTimeBetween(cardId, mId, startDateTime, endDateTime);
	}
	
	public List<CreditTransactionBean> getTransactionsByCardIdAndMemberId(Integer cardId, Integer mId) {
	    return creditTransactionRepository.findByCardDetailCardIdAndMemberMId(cardId, mId);
	}
	
	//退款
	public CreditTransactionBean refundTransaction(Integer transactionId) {
		CreditTransactionBean original = creditTransactionRepository.findById(transactionId)
				.orElseThrow(()->new RuntimeException("交易不存在,ID="+transactionId));
		
		if(original.getDescription()!=null&&original.getDescription().contains("已退款")) {
			throw new RuntimeException("該交易已退款,不能重複退款");
		}
		// 更新原交易紀錄 description
	    String oldDesc = original.getDescription() == null ? "" : original.getDescription();
	    original.setDescription(oldDesc + "（已退款）");
	    creditTransactionRepository.save(original);
		
		CreditTransactionBean refund = new CreditTransactionBean();
		refund.setTransactionCode(generateTransactionCode());
	    refund.setCardDetail(original.getCardDetail());
	    refund.setMember(original.getMember());
	    refund.setAmount(original.getAmount().negate()); // 負數金額
	    refund.setMerchantType(original.getMerchantType());
	    refund.setDescription("退款：" + original.getDescription());
	    refund.setTransactionTime(LocalDateTime.now());
	    refund.setCashback(original.getCashback().negate());

	    
	    int reversePoints = original.getCashback()
                .setScale(0, java.math.RoundingMode.DOWN)
                .intValue();
	    if (reversePoints > 0) {
	        cardRewardService.earnFromTransaction(
	            original.getCardDetail().getCardId(),
	            -reversePoints,
	            "REFUND:" + original.getTransactionId() // 防重：退款對應原交易
	        );
	    }
	    return creditTransactionRepository.save(refund);
	}
	
	// 依會員姓名模糊 + 年月查詢交易
    public List<CreditTransactionBean> findByMemberNameLikeAndYearMonth(String name, String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return creditTransactionRepository.findByMemberNameLikeAndTransactionTimeBetween(
                name, startDateTime, endDateTime);
    }
    
    private void attachToBillIfExists(CardDetailBean card, CreditTransactionBean tx) {
        YearMonth ym = YearMonth.from(tx.getTransactionTime());
        LocalDate billingDate = ym.atEndOfMonth();

        creditBillRepository.findByCardDetailCardIdAndBillingDate(card.getCardId(), billingDate)
            .ifPresent(bill -> {
                tx.setCreditBill(bill);
                creditTransactionRepository.save(tx);

                LocalDateTime start = ym.atDay(1).atStartOfDay();
                LocalDateTime end   = ym.atEndOfMonth().atTime(23,59,59);

                BigDecimal net = creditTransactionRepository
                    .findByCardDetailCardIdAndTransactionTimeBetween(card.getCardId(), start, end)
                    .stream()
                    .map(CreditTransactionBean::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                bill.setTotalAmount(net); // ✅ 保留淨額

                // === 以「應繳基準 = max(淨額,0)」重算最低與狀態 ===
                BigDecimal base = net.max(BigDecimal.ZERO);
                BigDecimal min  = base.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
                bill.setMinimumPayment(min);

                BigDecimal paid = bill.getPaidAmount() == null ? BigDecimal.ZERO : bill.getPaidAmount();
                if (paid.compareTo(base) >= 0)       bill.setStatus("已繳清");
                else if (paid.compareTo(min) >= 0 && base.signum() > 0) bill.setStatus("已繳最低");
                else                                  bill.setStatus(base.signum()==0 ? "本期無須繳款" : "未繳");

                creditBillRepository.save(bill);
            });
    }
}
