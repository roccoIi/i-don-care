package com.idoncare.quest.domain.mission.entity;

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
@Table(name = "mission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mission_id")
	private Long missionId;

	private String state;

	private String title;

	private String content;

	private Long amount;

	@Column(name = "proof_picture_url")
	private String proofPictureUrl;

	@Column(name = "input_date")
	private LocalDateTime inputDate;

	@Column(name = "relation_id")
	private Long relationId;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
}

