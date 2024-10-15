package com.idoncare.quest.domain.savings.entitiy;

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
@Table(name = "coinbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE account SET delete_yn = true WHERE coinbox_id = ?")
@SQLRestriction("delete_yn = false")
public class CoinboxEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coinbox_id")
	private Long coinboxId;

	@Column(name = "goal_title")
	private String goalTitle;

	@Column(name = "goal_amount")
	private Long goalAmount;

	private Long amount;

	@Column(name = "interest_amount")
	private Long interestAmount;

	@Column(name = "relation_id")
	private Long relationId;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
}
