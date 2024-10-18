package com.idoncare.user.application.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.domain.enums.RoleType;
import com.idoncare.user.domain.mapper.UserMapper;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.UserInfoDto;
import com.idoncare.user.entity.UserEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.BadRequestException;
import com.idoncare.user.global.exception.NotFoundException;
import com.idoncare.user.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public User saveUser(final User user) {
		return UserMapper.toDomain(
			userRepository.save(UserMapper.createEntity(user))
		);
	}

	@Transactional(readOnly = true)
	public boolean isExistUser(final String phoneNumber) {
		return userRepository.existsByUserPhone(phoneNumber);
	}

	@Transactional(readOnly = true)
	public User findUser(final Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		return UserMapper.toDomain(userEntity);
	}

	@Transactional(readOnly = true)
	public User findUserByTelOfUser(String tel) {
		UserEntity userEntity = userRepository.findByUserPhone(tel)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		return UserMapper.toDomain(userEntity);
	}

	@Transactional(readOnly = true)
	public User findUserByTelChild(final String tel) {
		UserEntity userEntity = userRepository.findByUserPhone(tel)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		return UserMapper.toDomain(userEntity);
	}

	@Transactional(readOnly = true)
	public int comparePassword(String password, Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		return password.compareTo(userEntity.getPassword());
	}

	@Transactional
	public void updateUserOfPassword(String password, Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		userEntity.updatePassword(password);
		userRepository.save(userEntity);
	}

	@Transactional
	public void updateUserOfRole(RoleType role, Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		userEntity.updateRole(role);
		userRepository.save(userEntity);
	}

	@Transactional
	public void deleteUser(String password, Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		if (password.compareTo(userEntity.getPassword()) != 0) { // 다르면
			throw new BadRequestException(ErrorCode.U008);
		}
		userRepository.deleteById(userId);
	}

	@Transactional(readOnly = true)
	public UserInfoDto findUserNameFromId(Long userId) {
		Optional<UserEntity> userEntity = userRepository.findById(userId);
		return userEntity.map(entity -> new UserInfoDto(userId, entity.getUserName())).orElse(null);
	}

	@Transactional(readOnly = true)
	public void updateUserOfRelationYn(Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U002));
		userEntity.updateRelationYn();
		userRepository.save(userEntity);
	}
}
