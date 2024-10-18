package com.idoncare.user.application.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idoncare.user.domain.enums.GenderType;
import com.idoncare.user.domain.enums.RoleType;
import com.idoncare.user.domain.user.Birth;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.UserInfoDto;
import com.idoncare.user.dto.req.IntegrateRoleReq;
import com.idoncare.user.dto.req.IntegrateSignUpReq;
import com.idoncare.user.dto.req.SimplePasswordRegistReq;
import com.idoncare.user.dto.req.SimplePasswordReq;
import com.idoncare.user.dto.req.TelReq;
import com.idoncare.user.dto.resp.LoginSimpleSignInResp;
import com.idoncare.user.dto.resp.UserSignUpResp;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.BadRequestException;

@Component
public class UserManager {

	@Autowired
	private UserService userService;

	public boolean checkUserExist(final TelReq req) {
		return userService.isExistUser(req.getTel());
	}

	public UserSignUpResp signup(final IntegrateSignUpReq req) {

		User user;
		// if (userService.isExistUser(req.getTel())) {
		// 	user = userService.findUserByTelOfUser(req.getTel());
		// 	isExist = true;
		// 	// 리프레시, 엑세스 토큰 발급
		// } else {
		user = userService.saveUser(User.builder()
			.userPhone(req.getTel())
			.birth(Birth.toBirth(req.getBirth()))
			.userName(req.getUserName())
			.genderType(GenderType.toGender(req.getBirth()))
			.build());
		// }

		return new UserSignUpResp(user.getUserId(), false);
	}

	public void registerPassword(final SimplePasswordRegistReq req) {
		User user = userService.findUser(req.getUserId());
		user.updatePassword(req.getPassword());
		userService.saveUser(user);
	}

	public LoginSimpleSignInResp signin(SimplePasswordReq req, Long userId) {
		if (userService.comparePassword(req.getPassword(), userId) != 0) {
			throw new BadRequestException(ErrorCode.A004);
		}
		return new LoginSimpleSignInResp();
	}

	public void updatePassword(SimplePasswordReq req, Long userId) {
		userService.updateUserOfPassword(req.getPassword(), userId);
	}

	public void signout(SimplePasswordReq req, Long userId) {
		userService.deleteUser(req.getPassword(), userId);
	}

	public void registerRole(IntegrateRoleReq req) {
		userService.updateUserOfRole(RoleType.valueOf(req.getRole()), req.getUserId());
	}

	public User findUser(String tel) {
		return userService.findUserByTelOfUser(tel);
	}

	public List<UserInfoDto> getUserNameFromIds(List<Long> userIds) {
		return userIds.stream()
			.map(userId -> userService.findUserNameFromId(userId))
			.toList();
	}
}

