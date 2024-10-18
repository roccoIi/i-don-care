package com.idoncare.user.domain.mapper;

import java.time.LocalDateTime;

import com.idoncare.user.domain.user.User;
import com.idoncare.user.entity.UserEntity;

public class UserMapper {

	public static User toDomain(UserEntity entity) {
		return User.builder()
			.userId(entity.getUserId())
			.accountId(entity.getAccountId())
			.userPhone(entity.getUserPhone())
			.userName(entity.getUserName())
			.password(entity.getPassword())
			.birth(entity.getBirth())
			.genderType(entity.getGenderType())
			.roleType(entity.getRoleType())
			.relationYn(entity.getRelationYn())
			.build();
	}

	public static UserEntity createEntity(User user) {
		return UserEntity.builder()
			.userId(user.getUserId())
			.accountId(user.getAccountId())
			.userPhone(user.getUserPhone())
			.userName(user.getUserName())
			.password(user.getPassword())
			.birth(user.getBirth())
			.genderType(user.getGenderType())
			.roleType(user.getRoleType())
			.deleteYn(false)  // 기본값 설정
			.createAt(LocalDateTime.now())  // 현재 시간 설정
			.relationYn(false)  // 기본값 설정
			.build();
	}

	public static UserEntity toEntity(User user) {
		return UserEntity.builder()
			.userId(user.getUserId())
			.accountId(user.getAccountId())
			.userPhone(user.getUserPhone())
			.userName(user.getUserName())
			.password(user.getPassword())
			.birth(user.getBirth())
			.genderType(user.getGenderType())
			.roleType(user.getRoleType())
			.relationYn(user.getRelationYn())  // 기본값 설정
			.build();
	}

}
