package com.bank.creditCard.payment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.account.bean.Transactions;
import com.bank.account.service.transactions.InternalTransferService;
import com.bank.creditCard.payment.dao.CardPaymentRepository;
import com.bank.creditCard.payment.model.CardPaymentBean;
import com.bank.creditCard.transaction.dao.CreditBillRepository;
import com.bank.creditCard.transaction.model.CreditBillBean;

import jakarta.transaction.Transactional;

@Service
public class CardPaymentService {
	
	@Autowired
	private CardPaymentRepository cardPaymentRepository;

	@Autowired
	private InternalTransferService internalTransferService;
	
	@Autowired
	private CreditBillRepository creditBillRepository;
	
	private static final String TRANSACTION_TYPE = "信用卡扣款";
	private static final String TO_ACCOUNT="79999999987";
	private static final Integer OPERATOR_ID=1;
	
	 private static final String BILL_STATUS_PAID= "已繳清";
	 private static final String BILL_STATUS_MINIMUM_PAID = "已繳最低";
	 private static final String BILL_STATUS_UNPAID="未繳";
	 
	
	//信用卡扣款及建立繳費紀錄
	 @Transactional
	public CardPaymentBean payCreditCard(Integer billId, Integer cardId, Integer memberId,
            String payerAccountId, BigDecimal payAmount) {
		 
		CreditBillBean bill = creditBillRepository.findById(billId)
	                .orElseThrow(() -> new RuntimeException("找不到帳單: " + billId));
		// 建立付款紀錄（初始狀態 PROCESSING）
        CardPaymentBean payment = new CardPaymentBean();
        payment.setCardId(cardId);
        payment.setMId(memberId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(payAmount);
        payment.setPaymentMethod("ACCOUNT_DEBIT");
        payment.setStatus("PROCESSING");
        payment = cardPaymentRepository.save(payment);
		
		try {
			Transactions txReq = new Transactions();
            txReq.setAccountId(payerAccountId);
            txReq.setToAccountId(TO_ACCOUNT);
            txReq.setTransactionType(TRANSACTION_TYPE);
            txReq.setAmount(payAmount);
            txReq.setMemo("信用卡繳費 - billId=" + billId);
            txReq.setOperatorId(OPERATOR_ID);
			
            Transactions txRes = internalTransferService.internalTransferAction(txReq);
         // 依帳戶模組狀態對應付款紀錄
            String mappedStatus = mapTransferStatus(txRes.getStatus());
            payment.setStatus(mappedStatus);
            
            if ("COMPLETED".equals(mappedStatus)) {
                // 更新帳單已繳金額
                BigDecimal oldPaid = bill.getPaidAmount() == null ? BigDecimal.ZERO : bill.getPaidAmount();
                BigDecimal newPaid = oldPaid.add(payAmount);
                bill.setPaidAmount(newPaid);

                // 判斷帳單狀態
                if (newPaid.compareTo(bill.getTotalAmount()) >= 0) {
                    bill.setStatus(BILL_STATUS_PAID);
                } else if (newPaid.compareTo(bill.getMinimumPayment()) >= 0) {
                    bill.setStatus(BILL_STATUS_MINIMUM_PAID);
                } else {
                    bill.setStatus(BILL_STATUS_UNPAID);
                }
                creditBillRepository.save(bill);
            }

		} catch (Exception e) {
			payment.setStatus("FAILED");
		}
		return cardPaymentRepository.save(payment);
	}
	// === 查詢/統計方法 ===
	public Optional<CardPaymentBean> getPaymentById(Integer paymentId) {
	     return cardPaymentRepository.findById(paymentId);
	}
	public List<CardPaymentBean> getPaymentsByCardId(Integer cardId) {
        return cardPaymentRepository.findByCardId(cardId);
    }
	public List<CardPaymentBean> getPaymentsByMemberId(Integer memberId) {
        return cardPaymentRepository.findBymId(memberId);
    }
	
	public CardPaymentBean updatePaymentStatus(Integer paymentId, String status) {
        CardPaymentBean p = cardPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        p.setStatus(status);
        return cardPaymentRepository.save(p);
    }
	
	public BigDecimal getTotalPaidByCardId(Integer cardId) {
        return cardPaymentRepository.findByCardId(cardId).stream()
                .map(CardPaymentBean::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
	 
	private String mapTransferStatus(String txStatus) {
	        if (txStatus == null) return "FAILED";
	        switch (txStatus) {
	            case "轉帳成功":
	            case "交易成功":
	            case "成功":
	                return "COMPLETED";
	            default:
	                return "FAILED";
	        }
	    }
}
