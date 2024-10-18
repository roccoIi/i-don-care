package com.idoncare.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idoncare.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByUserPhone(String userPhone);

	Optional<UserEntity> findByUserPhone(String userPhone);
}
