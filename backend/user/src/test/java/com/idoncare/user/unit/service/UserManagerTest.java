package com.idoncare.user.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.idoncare.user.application.user.UserManager;
import com.idoncare.user.application.user.UserService;
import com.idoncare.user.dto.req.TelReq;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserManager userManager;

	private TelReq telReq;

	@BeforeEach
	void setUp() {
		telReq = new TelReq("01012345678");
	}

	@Test
	@DisplayName("전화번호로 유저 존재 확인 - 유저가 존재할 때 true 반환")
	void testCheckUserExist_True() {
		when(userService.isExistUser(anyString())).thenReturn(true);

		boolean result = userManager.checkUserExist(telReq);

		assertTrue(result);

		verify(userService, times(1)).isExistUser(anyString());
	}

	@Test
	@DisplayName("전화번호로 유저 존재 확인 - 유저가 존재하지 않을 때 false 반환")
	void testCheckUserExist_False() {
		when(userService.isExistUser(anyString())).thenReturn(false);

		boolean result = userManager.checkUserExist(telReq);

		assertFalse(result);

		verify(userService, times(1)).isExistUser(anyString());
	}
}
