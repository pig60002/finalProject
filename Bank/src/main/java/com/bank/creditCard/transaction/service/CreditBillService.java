package com.bank.creditCard.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.creditCard.issue.dao.CardDetailRepository;
import com.bank.creditCard.issue.model.CardDetailBean;
import com.bank.creditCard.transaction.dao.CreditBillRepository;
import com.bank.creditCard.transaction.dao.CreditTransactionRepository;
import com.bank.creditCard.transaction.model.CreditBillBean;
import com.bank.creditCard.transaction.model.CreditTransactionBean;

@Service
public class CreditBillService {
	
	@Autowired
	private CreditBillRepository creditBillRepository;
	
	@Autowired
	private CreditTransactionRepository creditTransactionRepository;

	@Autowired
	private CardDetailRepository cardDetailRepository;
	
	/** 應繳基準：本期應繳金額 = max(本期淨額, 0) */
    private BigDecimal payBase(BigDecimal net) {
        return (net == null ? BigDecimal.ZERO : net).max(BigDecimal.ZERO);
    }
    
    /** 依「應繳基準」重算狀態 */
    private void recomputeStatus(CreditBillBean bill) {
        BigDecimal base = payBase(bill.getTotalAmount()); // ← 用淨額的 max(,0)
        BigDecimal min  = base.multiply(new BigDecimal("0.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        bill.setMinimumPayment(min);

        BigDecimal paid = bill.getPaidAmount() == null ? BigDecimal.ZERO : bill.getPaidAmount();
        if (paid.compareTo(base) >= 0) {
            bill.setStatus("已繳清");
        } else if (paid.compareTo(min) >= 0 && base.signum() > 0) {
            bill.setStatus("已繳最低");
        } else {
            bill.setStatus(base.signum() == 0 ? "本期無須繳款" : "未繳");
        }
    }
	
    public CreditBillBean generateBill(Integer cardId, String billingYearMonth) {
        CardDetailBean card = cardDetailRepository.findById(cardId)
            .orElseThrow(() -> new RuntimeException("卡片不存在,ID=" + cardId));

        YearMonth ym = YearMonth.parse(billingYearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
        LocalDate billingDate = ym.atEndOfMonth();
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end   = ym.atEndOfMonth().atTime(23,59,59);

        var txs = creditTransactionRepository
            .findByCardDetailCardIdAndTransactionTimeBetween(cardId, start, end);

        // 本期「淨額」(可為負，含退款)
        BigDecimal net = txs.stream()
            .map(CreditTransactionBean::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        var opt = creditBillRepository.findByCardDetailCardIdAndBillingDate(cardId, billingDate);
        CreditBillBean bill = opt.orElseGet(CreditBillBean::new);

        bill.setCardDetail(card);
        bill.setBillingDate(billingDate);
        bill.setDueDate(billingDate.plusDays(20));
        bill.setTotalAmount(net); // ✅ 保留淨額（可為負）
        if (bill.getPaidAmount() == null) bill.setPaidAmount(BigDecimal.ZERO);

        // 掛帳 + 狀態、最低應繳重算
        CreditBillBean saved = creditBillRepository.save(bill);
        for (var tx : txs) tx.setCreditBill(saved);
        creditTransactionRepository.saveAll(txs);

        recomputeStatus(saved);
        saved = creditBillRepository.save(saved);

        // 更新卡片可用額度（仍以交易總和）
        BigDecimal usedAmount = creditTransactionRepository.findByCardDetail_CardId(card.getCardId())
            .stream().map(CreditTransactionBean::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        card.setCurrentBalance(card.getCreditLimit().subtract(usedAmount));
        cardDetailRepository.save(card);

        return saved;
    }

	
	public List<CreditBillBean> findAllBills(){
		return creditBillRepository.findAll();
	}
	
	public List<CreditBillBean> findBillsByMemberId(Integer memberId){
		List<CreditBillBean> list = creditBillRepository.findBillsOfMember(memberId);
	    System.out.println("[myBills] mId=" + memberId + " -> " + list.size() + " bills");
	    return list;
	}
	
	public CreditBillBean findBillDetail(Integer billId) {
		return creditBillRepository.findById(billId).orElseThrow(()->new RuntimeException("帳單不存在,ID="+billId));
	}
}
