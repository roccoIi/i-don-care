package com.idoncare.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.idoncare.user.domain.enums.NotificationType;

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
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE notification SET delete_yn = true WHERE notification_id = ?")
@Where(clause = "delete_yn = false")
public class NotificationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long notificationId;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private NotificationType type;

	@Column(name = "sender_id", nullable = false)
	private Long senderId; // 보내는사람

	@Column(name = "sender_name", nullable = false)
	private String senderName; // 보내는사람 이름

	@Column(name = "receiver_id", nullable = false)
	private Long receiverId; // 받는사람

	@Column(name = "is_read", nullable = false)
	private Boolean isRead;

	@Column(name = "delete_yn", nullable = false)
	private Boolean deleteYn = false;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	public void updateReadIsTrue() {
		this.isRead = true;
	}
}
