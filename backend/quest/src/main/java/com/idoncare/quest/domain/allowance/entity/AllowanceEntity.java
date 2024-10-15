package com.idoncare.quest.domain.allowance.entity;

import java.time.LocalDateTime;

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
@Table(name = "allowance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AllowanceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "allowance_id")
	private Long allowanceId;

	@Column(name = "relation_id")
	private Long relationId;

	private Long type;

	private Long day;

	private Long amount;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
}
