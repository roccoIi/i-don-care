package com.idoncare.user.application.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.domain.mapper.UserMapper;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.entity.UserEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.NotFoundException;
import com.idoncare.user.repository.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	// @Transactional(readOnly = true)
	// public int comparePassword(String tel, String password) {
	// 	return password.compareTo(userEntity.getPassword());
	// }

	@Transactional(readOnly = true)
	public User findUserByTel(String tel) {
		UserEntity userEntity = userRepository.findByUserPhone(tel)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		return UserMapper.toDomain(userEntity);
	}

	@Transactional(readOnly = true)
	public User findUserById(Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		return UserMapper.toDomain(userEntity);
	}
}
