package com.idoncare.user.dto.req;

import com.idoncare.user.domain.enums.RoleType;
import com.idoncare.user.global.common.validation.EnumValid;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntegrateRoleReq {
	@EnumValid(enumClass = RoleType.class, message = "유효하지 않은 Role입니다. [PARENT 혹은 CHILD]만 입력 가능합니다.")
	private String role;

	private Long userId;
}
