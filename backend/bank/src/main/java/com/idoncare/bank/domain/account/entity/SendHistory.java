package com.idoncare.bank.domain.account.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

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
@SQLDelete(sql = "UPDATE sendHistory SET delete_yn = true WHERE history_id = ?")
@SQLRestriction("delete_yn = false")

public class SendHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id", nullable = false)
	private Long historyId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@Column(name = "send_account", nullable = false)
	private String sendAccount;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	@ColumnDefault("0")
	@Column(name = "delete_yn", nullable = false)
	private boolean deleteYn;

	private static SendHistory toEntity(Account account, String sendAccount, LocalDateTime createAt) {
		return SendHistory.builder()
			.account(account)
			.sendAccount(sendAccount)
			.createAt(createAt).build();
	}
}
