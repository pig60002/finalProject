package com.bank.creditCard.payment.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.account.bean.Transactions;
import com.bank.account.service.transactions.InternalTransferService;
import com.bank.creditCard.cardReward.service.CardRewardService;
import com.bank.creditCard.dto.PayWithRewardResult;
import com.bank.creditCard.payment.dao.CardPaymentRepository;
import com.bank.creditCard.payment.model.CardPaymentBean;
import com.bank.creditCard.transaction.dao.CreditBillRepository;
import com.bank.creditCard.transaction.model.CreditBillBean;


@Service
public class CardPaymentService {
	
	@Autowired
	private CardPaymentRepository cardPaymentRepository;

	@Autowired
	private InternalTransferService internalTransferService;
	
	@Autowired
	private CreditBillRepository creditBillRepository;
	
	@Autowired
    private CardRewardService cardRewardService;
	
	private static final String TRANSACTION_TYPE = "信用卡扣款";
	private static final String TO_ACCOUNT="7999999987";
	private static final Integer OPERATOR_ID=1;
	
	private static final String BILL_STATUS_PAID= "已繳清";
	private static final String BILL_STATUS_MINIMUM_PAID = "已繳最低";
	private static final String BILL_STATUS_UNPAID="未繳";
	
	private void increasePaidAndRecalcStatus(CreditBillBean bill, BigDecimal increase) {
        if (increase == null || increase.signum() <= 0) return;

        BigDecimal total = nvl(bill.getTotalAmount());
        BigDecimal oldPaid = nvl(bill.getPaidAmount());
        BigDecimal newPaid = oldPaid.add(increase);

        // 夾限避免超繳
        if (newPaid.compareTo(total) >= 0) {
            newPaid = total;
            bill.setStatus(BILL_STATUS_PAID);
        } else if (newPaid.compareTo(nvl(bill.getMinimumPayment())) >= 0) {
            bill.setStatus(BILL_STATUS_MINIMUM_PAID);
        } else {
            bill.setStatus(BILL_STATUS_UNPAID);
        }

        bill.setPaidAmount(newPaid);
        creditBillRepository.save(bill);
    }
	 
	
	//信用卡扣款及建立繳費紀錄
	 @Transactional
	public CardPaymentBean payCreditCard(Integer billId, Integer cardId, Integer memberId,
            String payerAccountId, BigDecimal payAmount) {
		 System.out.println("【LOG】payCreditCard 開始: billId=" + billId + ", cardId=" + cardId + ", memberId=" + memberId + ", accountId=" + payerAccountId + ", amount=" + payAmount);
		
		CreditBillBean bill = creditBillRepository.findById(billId)
	                .orElseThrow(() -> new RuntimeException("找不到帳單: " + billId));
		System.out.println("【LOG】找到帳單: " + bill);
		// 建立付款紀錄（初始狀態 PROCESSING）
        CardPaymentBean payment = new CardPaymentBean();
        payment.setCardId(cardId);
        payment.setMemberId(memberId);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setAmount(payAmount);
        payment.setPaymentMethod("ACCOUNT_DEBIT");
        payment.setStatus("PROCESSING");
        payment = cardPaymentRepository.save(payment);
        System.out.println("【LOG】初始付款紀錄已建立: " + payment);
		try {
			System.out.println("【LOG】開始 internalTransferAction...");
			Transactions txReq = new Transactions();
            txReq.setAccountId(payerAccountId);
            txReq.setToAccountId(TO_ACCOUNT);
            txReq.setTransactionType(TRANSACTION_TYPE);
            txReq.setAmount(payAmount);
            txReq.setMemo("信用卡繳費 - billId=" + billId);
            txReq.setOperatorId(OPERATOR_ID);
			
            Transactions txRes = internalTransferService.internalTransferAction(txReq);
            System.out.println("【LOG】internalTransferAction 回傳: " + txRes);
            // 依帳戶模組狀態對應付款紀錄
            System.out.println("【DEBUG】txRes.getStatus() = '" + txRes.getStatus() + "'");
            String mappedStatus = mapTransferStatus(txRes.getStatus());
            payment.setStatus(mappedStatus);
            
            if ("COMPLETED".equals(mappedStatus)) {
            	increasePaidAndRecalcStatus(bill, payAmount);
            }

		} catch (Exception e) {
			payment.setStatus("FAILED");
			System.out.println("【LOG】internalTransfer 發生例外: " + e.getMessage());
	        e.printStackTrace();
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
        return cardPaymentRepository.findByMemberId(memberId);
    }
	
	public CardPaymentBean updatePaymentStatus(Integer paymentId, String status) {
        CardPaymentBean p = cardPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        p.setStatus(status);
        return cardPaymentRepository.save(p);
    }
	
	public BigDecimal getTotalPaidByCardId(Integer cardId) {
        return cardPaymentRepository.findByCardId(cardId).stream()
                .filter(p -> "COMPLETED".equals(p.getStatus())) // 只算成功的
                .map(CardPaymentBean::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
	
	// 查會員自己的某張卡付款紀錄
	public List<CardPaymentBean> getPaymentsByCardIdAndMemberId(Integer cardId, Integer memberId) {
	    return cardPaymentRepository.findByCardIdAndMemberId(cardId, memberId);
	}

	// 查會員自己的單一付款紀錄
	public Optional<CardPaymentBean> getPaymentByIdAndMemberId(Integer paymentId, Integer memberId) {
	    return cardPaymentRepository.findById(paymentId)
	            .filter(p -> p.getMemberId().equals(memberId));
	}
	 
	private String mapTransferStatus(String txStatus) {
	        if (txStatus == null) return "FAILED";
	        switch (txStatus) {
	            case "轉帳成功":
	            case "交易成功":
	            case "成功":
	            case "COMPLETED":
	            case "SUCCESS":
	                return "COMPLETED";
	            default:
	                return "FAILED";
	        }
	    }
	
	@Transactional(rollbackFor = Exception.class)
    public PayWithRewardResult payWithReward(Integer billId, Integer cardId, Integer memberId,
                                             String payerAccountId, BigDecimal plannedPayAmount,
                                             Integer redeemPointsWanted) {

        Objects.requireNonNull(billId);
        Objects.requireNonNull(cardId);
        Objects.requireNonNull(memberId);
        Objects.requireNonNull(payerAccountId);
        Objects.requireNonNull(plannedPayAmount);

        if (plannedPayAmount.signum() < 0) throw new IllegalArgumentException("付款金額不可為負");

        // 1) 讀帳單並確認所有權與對應卡
        CreditBillBean bill = creditBillRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("找不到帳單"));
        if (!bill.getCardDetail().getMember().getmId().equals(memberId)) throw new IllegalStateException("非本人帳單");
        if (!bill.getCardDetail().getCardId().equals(cardId)) throw new IllegalStateException("帳單與卡片不一致");

        BigDecimal total = nvl(bill.getTotalAmount());
        BigDecimal paid = nvl(bill.getPaidAmount());
        BigDecimal outstanding = total.subtract(paid);
        if (outstanding.signum() <= 0) {
            PayWithRewardResult r = new PayWithRewardResult();
            r.setStatus("FAILED");
            r.setBillId(billId); r.setCardId(cardId);
            r.setPlannedPay(plannedPayAmount); r.setPlannedRedeem(redeemPointsWanted);
            r.setActualPay(BigDecimal.ZERO); r.setActualRedeem(0);
            r.setMessage("本期無須繳款");
            r.setOutstandingAfter(outstanding);
            return r;
        }

        // 2) 夾限計算
        BigDecimal planPay = plannedPayAmount.min(outstanding);
        int wantRedeem = Math.max(0, redeemPointsWanted == null ? 0 : redeemPointsWanted);
        int available = cardRewardService.getAvailablePoints(memberId, cardId);
        int capByAmount = planPay.intValue();          // 折抵不可超過要付金額
        int capByOutstanding = outstanding.intValue(); // 也不可超過未繳
        int actualRedeem = Math.min(wantRedeem, Math.min(available, Math.min(capByAmount, capByOutstanding)));
        BigDecimal actualPay = planPay.subtract(BigDecimal.valueOf(actualRedeem));

        if (actualRedeem + actualPay.intValue() == 0) {
            throw new IllegalArgumentException("折抵與付款皆為 0");
        }

        // 3) 先扣點（失敗丟例外回滾）
        Integer remainPoints = null;
        if (actualRedeem > 0) {
            remainPoints = cardRewardService.redeem(memberId, cardId, actualRedeem, String.valueOf(billId));
        }

        // 4) 再付款（>0 才呼叫；失敗丟例外回滾）
        if (actualPay.signum() > 0) {
            CardPaymentBean payRes = payCreditCard(billId, cardId, memberId, payerAccountId, actualPay);
            if (!"COMPLETED".equals(payRes.getStatus())) {
                throw new RuntimeException("付款失敗，已回滾紅利折抵與付款紀錄");
            }
        }

        // 5) ✅ 把「折抵點數（1點=1元）」也加到帳單已繳金額，並重算狀態
        if (actualRedeem > 0) {
            CreditBillBean fresh = creditBillRepository.findById(billId)
                    .orElseThrow(() -> new IllegalArgumentException("找不到帳單"));
            increasePaidAndRecalcStatus(fresh, BigDecimal.valueOf(actualRedeem));
        }

        // 回傳結果（用最新帳單計算未繳）
        CreditBillBean latest = creditBillRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("找不到帳單"));

        PayWithRewardResult r = new PayWithRewardResult();
        r.setStatus("COMPLETED");
        r.setBillId(billId); r.setCardId(cardId);
        r.setPlannedPay(plannedPayAmount); r.setPlannedRedeem(wantRedeem);
        r.setActualPay(actualPay); r.setActualRedeem(actualRedeem);
        r.setRemainPoints(remainPoints);
        r.setOutstandingAfter(nvl(latest.getTotalAmount()).subtract(nvl(latest.getPaidAmount())).max(BigDecimal.ZERO));
        r.setMessage("折抵與付款已完成");
        return r;
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
