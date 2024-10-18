package com.idoncare.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.idoncare.user.entity.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	@Query("SELECT n FROM NotificationEntity n WHERE n.receiverId = :receiverId AND n.isRead = false")
	List<NotificationEntity> findByReceiverId(long receiverId);

}
