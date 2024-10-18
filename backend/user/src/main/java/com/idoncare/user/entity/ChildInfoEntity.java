package com.idoncare.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "child_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE child_info SET delete_yn = true WHERE child_info_id = ?")
@SQLRestriction("delete_yn = false")
public class ChildInfoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "info_id")
	private Long infoId;

	@OneToOne
	@JoinColumn(name = "child_id")
	private UserEntity child;

	@Column(name = "user_phone")
	private String userPhone;

	@Column(name = "coinbox_success_cnt")
	private Integer coinboxSuccessCnt;

	@Column(name = "mission_success_cnt")
	private Integer missionSuccessCnt;

	@Column(name = "quiz_rate")
	private Integer quizRate;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "create_at")
	private LocalDateTime createAt;
}
