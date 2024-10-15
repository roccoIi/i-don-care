package com.idoncare.bank.domain.account.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.idoncare.bank.domain.account.dto.resp.responseClient.AuthNumResponse;
import com.idoncare.bank.domain.account.dto.resp.responseClient.TransactionHistoryInfo;
import com.idoncare.bank.domain.account.dto.resp.responseServer.TransactionHistoryList;

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

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE trans SET delete_yn = true WHERE trans_id = ?")
@SQLRestriction("delete_yn = false")

public class Trans {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trans_id", nullable = false)
	private Long transId;

	@ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	private Account account;

	@Column(name = "transaction_name")
	private String transactionName;

	@Column(name = "transaction_type_name", nullable = false)
	private String transactionTypeName;

	@Column(name= "transaction_account_no")
	private String transactionAccountNo;

	@Column(name = "cost", nullable = false)
	private Long cost;

	@Column(name = "balance", nullable = false)
	private Long balance;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	@ColumnDefault("0")
	@Column(name = "delete_yn", nullable = false)
	private boolean deleteYn;

	public static Trans toEntity(Account account, TransactionHistoryList.REC.History history){
		return Trans.builder()
			.account(account)
			.transactionAccountNo(history.getTransactionAccountNo())
			.transactionName(history.getTransactionMemo())
			.transactionTypeName(history.getTransactionTypeName())
			.cost(history.getTransactionBalance())
			.balance(history.getTransactionAfterBalance())
			.createAt(LocalDateTime.of(
				LocalDate.parse(history.getTransactionDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
				LocalTime.parse(history.getTransactionTime(), DateTimeFormatter.ofPattern("HHmmss"))
			))
			.build();
	}

	public static Trans toEntity(TransactionHistoryList.REC.History history){
		return Trans.builder()
			.transactionName(history.getTransactionMemo())
			.transactionAccountNo(history.getTransactionAccountNo())
			.transactionTypeName(history.getTransactionTypeName())
			.cost(history.getTransactionBalance())
			.balance(history.getTransactionAfterBalance())
			.createAt(LocalDateTime.of(
				LocalDate.parse(history.getTransactionDate(), DateTimeFormatter.ofPattern("yyyyMMdd")),
				LocalTime.parse(history.getTransactionTime(), DateTimeFormatter.ofPattern("HHmmss"))
			))
			.build();
	}

	public TransactionHistoryInfo.MiniTransactionHistoryInfo toTransactionInfo() {
		return TransactionHistoryInfo.MiniTransactionHistoryInfo.builder()
			.transactionName(this.transactionName)
			.transactionTypeName(this.transactionTypeName)
			.cost(this.cost)
			.balance(this.balance)
			.createAt(this.createAt)
			.build();
	}

}

