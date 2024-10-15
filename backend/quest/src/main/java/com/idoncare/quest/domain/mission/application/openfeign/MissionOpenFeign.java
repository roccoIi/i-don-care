package com.idoncare.quest.domain.mission.application.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "BankClient", url = "http://localhost:8083/bank")
public interface MissionOpenFeign {
	@GetMapping("/test")
	String missionToBankTest(@RequestParam("mission") int mission_serial);
}
