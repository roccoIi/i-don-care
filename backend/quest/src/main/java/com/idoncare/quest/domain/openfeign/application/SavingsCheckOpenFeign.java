package com.idoncare.quest.domain.openfeign.application;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SavingsCheckClient", url = "https://j11a603.p.ssafy.io/api/bank")
public interface SavingsCheckOpenFeign {

	@PostMapping("/inner/check")
	String savingsCheckToBank(@RequestBody Long userId);
}
