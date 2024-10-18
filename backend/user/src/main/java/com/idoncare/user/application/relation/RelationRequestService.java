package com.idoncare.user.application.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.domain.relation.RelationRequest;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.domain.enums.RelationType;
import com.idoncare.user.domain.mapper.RelationMapper;
import com.idoncare.user.entity.RelationRequestEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.NotFoundException;
import com.idoncare.user.repository.RelationRequestRepository;

@Service
public class RelationRequestService {

	@Autowired
	private RelationRequestRepository requestRepository;

	@Transactional
	public void requestRelation(User parent, User child, Notification notification) {
		RelationRequest relationRequest = RelationMapper.fromDomainToDomain(parent, child, notification);
		relationRequest.updateState(RelationType.REQUEST);
		requestRepository.save(
			RelationMapper.toEntity(relationRequest)
		);
	}

	@Transactional(readOnly = true)
	public RelationRequestEntity findRequestByNotiId(Long notificationId) {
		return requestRepository.findByNotificationNotificationId(notificationId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U007));
	}

	@Transactional(readOnly = true)
	public RelationRequest findRequestStatusByNotiId(Long notificationId) {
		RelationRequestEntity requestEntity = requestRepository.findByNotificationNotificationId(notificationId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U007));

		return RelationMapper.toDomain(requestEntity);
	}

	@Transactional
	public RelationRequest updateState(Long notificationId, RelationType relationType) {
		RelationRequestEntity entity = findRequestByNotiId(notificationId);
		entity.updateState(relationType);
		return RelationMapper.toDomain(entity);
	}

}
