package com.idoncare.user.global.common.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
	public static LocalDateTime getCurrentTime() {
		return LocalDateTime.now();
	}

	public static LocalDateTime addMinutes(LocalDateTime time, int minutes) {
		return time.plusMinutes(minutes);
	}
}
