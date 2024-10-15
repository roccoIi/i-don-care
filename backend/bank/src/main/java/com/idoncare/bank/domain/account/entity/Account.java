package com.idoncare.bank.domain.account.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.idoncare.bank.domain.account.dto.resp.responseServer.SsafyAccountDetailResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE account SET deleteYn = true WHERE account_id = ?")
@SQLRestriction("delete_yn = false")

public class Account {

	@Id
	@Column(name = "account_id", nullable = false)
	private Long accountId;

	@Column(name = "account_num", nullable = false,  unique = true)
	private String accountNum;

	@Column(name = "bank_name", nullable = false)
	private String bankName;

	@Column(name = "balance", nullable = false)
	private Long balance;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	@ColumnDefault("0")
	@Column(name = "delete_yn", nullable = false)
	private boolean deleteYn;

	public static Account toEntity(String accountNum, String bankName, LocalDateTime createAt) {
		return Account.builder()
			.accountNum(accountNum)
			.bankName(bankName)
			.balance(0L)
			.createAt(createAt).build();
	}

	public static Account toEntity(SsafyAccountDetailResponse ssafyAccountDetailResponse, Long userId) {
		String dateTimePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String millis = String.format("%03d", System.currentTimeMillis() % 1000);  // 밀리초 부분만 추출
		String uuidPart =  String.format("%02d", ThreadLocalRandom.current().nextInt(0, 99)); // 5자리 랜덤 숫자

		return Account.builder()
			.accountId(Long.parseLong(dateTimePart + millis + uuidPart))
			.accountNum(ssafyAccountDetailResponse.getRec().getAccountNo())
			.bankName(ssafyAccountDetailResponse.getRec().getBankName())
			.balance(ssafyAccountDetailResponse.getRec().getAccountBalance())
			.userId(userId)
			.createAt(LocalDateTime.now())
			.build();
	}
}
