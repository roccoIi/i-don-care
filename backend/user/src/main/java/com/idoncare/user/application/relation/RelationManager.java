package com.idoncare.user.application.relation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.idoncare.user.api.openfeign.QuestOpenFeign;
import com.idoncare.user.application.notification.NotificationService;
import com.idoncare.user.application.user.UserService;
import com.idoncare.user.domain.enums.NotificationType;
import com.idoncare.user.domain.enums.RelationType;
import com.idoncare.user.domain.enums.RoleType;
import com.idoncare.user.domain.mapper.NotificationMapper;
import com.idoncare.user.domain.mapper.RelationMapper;
import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.domain.relation.Relation;
import com.idoncare.user.domain.relation.RelationRequest;
import com.idoncare.user.domain.user.User;
import com.idoncare.user.dto.req.RelationDeleteReq;
import com.idoncare.user.dto.req.RelationReq;
import com.idoncare.user.dto.req.RelationResponseReq;
import com.idoncare.user.dto.req.TelReq;
import com.idoncare.user.dto.resp.UserRelationResp;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.BadRequestException;

@Component
public class RelationManager {

	@Autowired
	private RelationService relationService;

	@Autowired
	private RelationRequestService requestService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;

	@Autowired
	private QuestOpenFeign questOpenFeign;

	public void processRelationRequest(TelReq req, Long userId) {
		User parent = userService.findUser(userId);
		User child = userService.findUserByTelChild(req.getTel());

		validateParentState(parent);
		validateChildState(child);

		Notification notification = notificationService.saveNotification(
			NotificationMapper.fromDomainToDomain(
				parent,
				child,
				NotificationType.REQUEST_RELATION
			)
		);
		requestService.requestRelation(parent, child, notification);
	}

	@Transactional
	public void processRelationResponse(RelationResponseReq req) {
		System.out.println(req.getNotificationId());
		RelationRequest relationRequest = requestService.findRequestStatusByNotiId(req.getNotificationId());
		if (relationRequest.getState() != RelationType.REQUEST) {
			throw new BadRequestException(ErrorCode.U009);
		}

		// 이미 관계가 설정된 자녀면, 예외처리
		boolean isExistRelation = relationService.isExistRelationBetweenUsers(
			relationRequest.getChild().getUserId(),
			relationRequest.getParent().getUserId()
		);
		if (isExistRelation) {
			throw new BadRequestException(ErrorCode.U011);
		}

		if (req.getIsAccept()) { // 승인 했으면 관계요청 COMPLETE -> 관계 등록
			Relation relation = relationService.registerRelation(
				RelationMapper.fromDomainToDomain(requestService.updateState(
						req.getNotificationId(),
						RelationType.COMPLETE
					)
				)
			);
			questOpenFeign.registerRelationId(new RelationReq(relation.getRelationId()));
			userService.updateUserOfRelationYn(relation.getChild().getUserId());
			userService.updateUserOfRelationYn(relation.getParent().getUserId());
		} else { // 거절 했으면 관계요청 CANCEL
			requestService.updateState(req.getNotificationId(), RelationType.CANCEL);
		}
	}

	public UserRelationResp getUserRelations(long userId) {
		User user = userService.findUser(userId);
		UserRelationResp resp;
		if (user.getRelationYn() && user.getRoleType() == RoleType.CHILD) {
			Relation relation = relationService.findParentOfChild(user.getUserId());
			resp = new UserRelationResp(
				RelationMapper.fromDomainToDto(relation.getParent(), relation)
			);
		} else if (user.getRelationYn() && user.getRoleType() == RoleType.PARENT) { // Role.PARENT
			List<Relation> relations = relationService.findChildrenOfParent(user.getUserId());
			resp = new UserRelationResp(
				relations.stream()
					.map(r -> RelationMapper.fromDomainToDto(r.getChild(), r))
					.toList()
			);
		} else {
			resp = UserRelationResp
				.builder()
				.role(user.getRoleType())
				.build();
		}
		return resp;
	}

	private void validateChildState(User child) {
		if (child.getRoleType() != RoleType.CHILD) {
			throw new BadRequestException(ErrorCode.U004);
		}
		if (child.getRelationYn()) {
			throw new BadRequestException(ErrorCode.U003);
		}
	}

	private void validateParentState(User parent) {
		if (parent.getRoleType() != RoleType.PARENT) {
			throw new BadRequestException(ErrorCode.U006);
		}
	}

	public void deleteRelation(RelationDeleteReq req) {
		relationService.deleteRelation(req.getRelationId());
	}
}
