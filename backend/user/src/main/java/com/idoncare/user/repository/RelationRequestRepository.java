package com.idoncare.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.idoncare.user.entity.RelationRequestEntity;

public interface RelationRequestRepository extends JpaRepository<RelationRequestEntity, Long> {

	@Query("SELECT r FROM RelationRequestEntity r WHERE r.notification.notificationId = :notificationId")
	Optional<RelationRequestEntity> findByNotificationNotificationId(Long notificationId);
}
