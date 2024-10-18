package com.idoncare.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE refresh_token SET delete_yn = true WHERE token_id = ?")
@SQLRestriction("delete_yn = false")
public class RefreshTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tokenId;

	@Column(nullable = false, unique = true)
	private String refreshToken;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private LocalDateTime expiryDate;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "create_at")
	private LocalDateTime createAt;

	@PrePersist
	public void prePersist() {
		this.createAt = this.createAt == null ? LocalDateTime.now() : this.createAt;
		this.deleteYn = this.deleteYn == null ? false : this.deleteYn;
	}
}
