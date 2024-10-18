package com.idoncare.user.domain.relation;

import com.idoncare.user.domain.user.User;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Relation {
	private Long relationId;
	private User child;
	private User parent;
}
