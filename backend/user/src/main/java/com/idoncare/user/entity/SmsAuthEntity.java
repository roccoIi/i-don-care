package com.idoncare.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "sms_auth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE sms_auth SET delete_yn = true WHERE auth_tel_id = ?")
@SQLRestriction("delete_yn = false")
public class SmsAuthEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_tel_id")
	private Long id;

	@Column(name = "tel", nullable = false)
	private String tel;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "expire_at", nullable = false)
	private LocalDateTime expireAt;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "create_at")
	private LocalDateTime createAt;

	public static SmsAuthEntity toEntity(String phoneNumber, String code, LocalDateTime expiredAt) {
		return SmsAuthEntity.builder()
			.tel(phoneNumber)
			.code(code)
			.expireAt(expiredAt)
			.deleteYn(false)
			.createAt(LocalDateTime.now())
			.build();
	}
}
