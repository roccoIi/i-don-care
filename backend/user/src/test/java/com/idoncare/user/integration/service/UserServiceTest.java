package com.idoncare.user.integration.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.application.user.UserManager;
import com.idoncare.user.domain.enums.RoleType;
import com.idoncare.user.dto.req.IntegrateSignUpReq;
import com.idoncare.user.dto.req.TelReq;
import com.idoncare.user.entity.UserEntity;
import com.idoncare.user.global.exception.BadRequestException;
import com.idoncare.user.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserManager userManager;

	@Autowired
	private UserRepository userRepository;

	@Test
	@Transactional
	@DisplayName("회원가입 후 유저 저장 여부 확인")
	public void testSignupAndCheckUserExist() {
		// Given
		IntegrateSignUpReq signUpReq = IntegrateSignUpReq.builder()
			.tel("01012345678")
			.birth("9801082")
			.userName("최소현")
			.build();

		// When: 회원가입 요청
		userManager.signup(signUpReq);

		// Then: 유저가 저장되었는지 확인
		boolean userExists = userManager.checkUserExist(new TelReq("01012345678"));
		assertThat(userExists).isTrue();

		// 데이터베이스에 유저 저장 여부 확인
		UserEntity savedUser = userRepository.findByUserPhone("01012345678").orElseThrow();
		assertThat(savedUser.getUserPhone()).isEqualTo("01012345678");
		assertThat(savedUser.getRoleType()).isEqualTo(RoleType.CHILD);
	}

	@Test
	@Transactional
	@DisplayName("유저가 존재하지 않는 경우 확인")
	public void testUserNotExist() {
		// Given: 존재하지 않는 전화번호
		String nonExistentPhone = "01099999999";

		// When: 유저 존재 여부 확인
		boolean userExists = userManager.checkUserExist(new TelReq(nonExistentPhone));

		// Then: 유저가 존재하지 않아야 함
		assertThat(userExists).isFalse();
	}

	@Test
	@Transactional
	@DisplayName("회원가입시 유저가 중복하는 않는 경우 U001 발생")
	public void testUserDuplicated() {
		// Given
		IntegrateSignUpReq signUpReq = IntegrateSignUpReq.builder()
			.tel("01012345678")
			.birth("9801082")
			.userName("최소현")
			.build();
		userManager.signup(signUpReq);

		// When & Then
		assertThatThrownBy(() -> userManager.signup(signUpReq))
			.isInstanceOf(BadRequestException.class);
	}
}
