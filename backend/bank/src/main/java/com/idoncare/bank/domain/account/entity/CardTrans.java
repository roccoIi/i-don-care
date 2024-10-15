package com.idoncare.bank.domain.account.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransHistoryList;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransResult;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE trans SET delete_yn = true WHERE card_trans_id = ?")
@SQLRestriction("delete_yn = false")
@ToString
public class CardTrans {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_trans_id", nullable = false)
	private Long cardTransId;

	@Column(name = "transaction_unique_no")
	private Long transactionUniqueNo;

	@Column(name = "merchant_id")
	private Long merchantId;

	@Column(name = "merchant_name")
	private String merchantName;

	@Column(name = "transaction_date")
	private String transactionDate;

	@Column(name = "transaction_time")
	private String transactionTime;

	@Column(name = "transaction_balance")
	private Long transactionBalance;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	@ColumnDefault("0")
	@Column(name = "delete_yn", nullable = false)
	private boolean deleteYn;

	public static CardTrans toEntity(CardTransHistoryList.REC.TransactionList transactionList, Account account) {
		return CardTrans.builder()
			.transactionUniqueNo(transactionList.getTransactionUniqueNo())
			.merchantId(transactionList.getMerchantId())
			.merchantName(transactionList.getMerchantName())
			.transactionDate(transactionList.getTransactionDate())
			.transactionTime(transactionList.getTransactionTime())
			.transactionBalance(transactionList.getTransactionBalance())
			.account(account)
			.createAt(LocalDateTime.now())
			.build();
	}

	public static CardTrans toEntity(CardTransResult.Result result, Account account){
		return CardTrans.builder()
			.transactionUniqueNo(result.getTransactionUniqueNo())
			.merchantId(result.getMerchantId())
			.merchantName(result.getMerchantName())
			.transactionDate(result.getTransactionDate())
			.transactionTime(result.getTransactionTime())
			.transactionBalance(result.getPaymentBalance())
			.account(account)
			.createAt(LocalDateTime.now())
			.build();

	}
}
