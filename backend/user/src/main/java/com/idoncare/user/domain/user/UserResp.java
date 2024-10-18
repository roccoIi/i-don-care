package com.idoncare.user.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResp {
	private Long relationId;
	private Long userId;
	private String userName;
}
