package com.idoncare.user.domain.relation;

import com.idoncare.user.domain.enums.RelationType;
import com.idoncare.user.domain.notification.Notification;
import com.idoncare.user.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RelationRequest {

	private Long requestId;
	private User child;
	private User parent;
	private Notification notification;
	private RelationType state;

	public void updateState(RelationType changedState) {
		this.state = changedState;
	}
}
