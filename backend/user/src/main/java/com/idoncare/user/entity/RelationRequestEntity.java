package com.idoncare.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.idoncare.user.domain.enums.RelationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@Table(name = "relation_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE relation_request SET delete_yn = true WHERE request_id = ?")
@SQLRestriction("delete_yn = false")
public class RelationRequestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Long requestId;

	@ManyToOne
	@JoinColumn(name = "child_id", nullable = false)
	private UserEntity child;

	@ManyToOne
	@JoinColumn(name = "parent_id", nullable = false)
	private UserEntity parent;

	@OneToOne
	@JoinColumn(name = "notification_id", nullable = false)
	private NotificationEntity notification;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "state", nullable = false)
	private RelationType state;

	@Column(name = "delete_yn", nullable = false)
	private Boolean deleteYn;

	@Column(name = "create_at", nullable = false)
	private LocalDateTime createAt;

	public void updateState(RelationType relationType) {
		this.state = relationType;
	}

	@PrePersist
	public void prePersist() {
		this.createAt = this.createAt == null ? LocalDateTime.now() : this.createAt;
		this.deleteYn = this.deleteYn == null ? false : this.deleteYn;
	}
}
