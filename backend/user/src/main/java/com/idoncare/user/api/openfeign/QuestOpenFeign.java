package com.idoncare.user.api.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.idoncare.user.dto.req.RelationReq;

@FeignClient(name = "QuestClient", url = "https://j11a603.p.ssafy.io/api/quest")
public interface QuestOpenFeign {

	@PostMapping("/inner/relation")
	void registerRelationId (@RequestBody RelationReq relationId);

}
