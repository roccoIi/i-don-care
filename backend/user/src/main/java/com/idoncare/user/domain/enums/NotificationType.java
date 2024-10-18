package com.idoncare.user.domain.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
	REQUEST_RELATION, // 수락, 거절 - 버튼 두개 (추가 API)
	COINBOX_START,    // 이자 더하기 - 버튼 하나 + 인풋 (추가 API)
	COINBOX_SUCCESS,  // 저금 성공
	COINBOX_PUT,	  // 저축
	MISSION_SUCCESS,  // 돈빠져나감 알림 - 없음
	QUIZ_SUCCESS,     // 돈빠져나감 알림 - 없음
	LESS_BALANCE	  // 잔액 부족 알람
}
