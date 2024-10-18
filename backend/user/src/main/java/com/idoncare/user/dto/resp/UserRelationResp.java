package com.idoncare.user.dto.resp;

import java.util.List;

import com.idoncare.user.domain.user.UserResp;
import com.idoncare.user.domain.enums.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRelationResp {
	private UserResp parent;
	private List<UserResp> children;
	private RoleType role;

	public UserRelationResp(List<UserResp> userResps) {
		this.children = userResps;
		this.role = RoleType.PARENT;
	}

	public UserRelationResp(UserResp userResp) {
		this.parent = userResp;
		this.role = RoleType.CHILD;
	}
}

