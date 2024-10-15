package com.idoncare.user.domain;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class User {

	private Long userId;
	private Long accountId;
	private String userPhone;
	private String password;
	private LocalDate birth;
	private Gender gender;
	private Role role;

}
