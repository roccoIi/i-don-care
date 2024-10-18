package com.idoncare.user.domain.mapper;

import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.domain.relation.Relation;
import com.idoncare.user.domain.relation.RelationRequest;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.domain.user.UserResp;
import com.idoncare.user.entity.RelationEntity;
import com.idoncare.user.entity.RelationRequestEntity;

public class RelationMapper {

	public static RelationRequestEntity toEntity(RelationRequest relationRequest) {
		return RelationRequestEntity.builder()
			.parent(UserMapper.toEntity(relationRequest.getParent()))
			.child(UserMapper.toEntity(relationRequest.getChild()))
			.state(relationRequest.getState())
			.notification(NotificationMapper.toEntity(relationRequest.getNotification()))
			.build();
	}

	public static RelationRequest toDomain(RelationRequestEntity entity) {
		return RelationRequest.builder()
			.parent(UserMapper.toDomain(entity.getParent()))
			.child(UserMapper.toDomain(entity.getChild()))
			.state(entity.getState())
			.notification(NotificationMapper.fromEntity(entity.getNotification()))
			.build();
	}

	public static RelationEntity toEntity(Relation relation) {
		return RelationEntity.builder()
			.child(UserMapper.toEntity(relation.getChild()))
			.parent(UserMapper.toEntity(relation.getParent()))
			.build();
	}

	public static Relation toDomain(RelationEntity entity) {
		return Relation.builder()
			.relationId(entity.getRelationId())
			.child(UserMapper.toDomain(entity.getChild()))
			.parent(UserMapper.toDomain(entity.getParent()))
			.build();
	}

	public static RelationRequest fromDomainToDomain(User parent, User child, Notification notification) {
		return RelationRequest.builder()
			.parent(parent)
			.child(child)
			.notification(notification)
			.build();
	}

	public static Relation fromDomainToDomain(RelationRequest relationRequest) {
		return Relation.builder()
			.parent(relationRequest.getParent())
			.child(relationRequest.getChild())
			.build();
	}

	public static UserResp fromDomainToDto(User user, Relation relation) {
		return UserResp.builder()
			.relationId(relation.getRelationId())
			.userId(user.getUserId())
			.userName(user.getUserName())
			.build();
	}

}
