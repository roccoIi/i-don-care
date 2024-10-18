package com.idoncare.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.idoncare.user.domain.enums.GenderType;
import com.idoncare.user.domain.enums.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET delete_yn = true WHERE user_id = ?")
@SQLRestriction("delete_yn = false")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "user_phone")
	private String userPhone;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "birth")
	private LocalDate birth;

	@Column(name = "gender")
	@Enumerated(value = EnumType.STRING)
	private GenderType genderType;

	@Column(name = "role")
	@Enumerated(value = EnumType.STRING)
	private RoleType roleType;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "create_at")
	private LocalDateTime createAt;

	@Column(name = "relation_yn")
	private Boolean relationYn;

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateRole(RoleType role) {
		this.roleType = role;
	}

	public void updateRelationYn() {
		this.relationYn = true;
	}
}
