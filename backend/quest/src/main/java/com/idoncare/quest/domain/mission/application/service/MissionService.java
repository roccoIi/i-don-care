package com.idoncare.quest.domain.mission.application.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.idoncare.quest.domain.mission.dto.req.CheckMissionReq;
import com.idoncare.quest.domain.mission.dto.req.MissionIdReq;
import com.idoncare.quest.domain.mission.dto.req.MissionReq;
import com.idoncare.quest.domain.mission.dto.req.ModifyMissionReq;
import com.idoncare.quest.domain.mission.dto.resp.MissionDetailResp;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;

import jakarta.validation.Valid;

public interface MissionService {
	String missionToBankTest(int mission_serial);

	void registMission(@Valid MissionReq missionReq);

	List<MissionDetailResp> getMissionList(@Valid RelationIdReq relationIdReq);

	List<MissionDetailResp> getMissionInProgessList(@Valid RelationIdReq relationIdReq);

	List<MissionDetailResp> getMissionCompleteList(@Valid RelationIdReq relationIdReq);

	void modifyMission(@Valid ModifyMissionReq modifyMissionReq);

	void reviewMission(MultipartFile image, String missionId);

	void deleteMission(@Valid MissionIdReq missionIdReq);

	void checkMission(@Valid CheckMissionReq checkMissionReq);
}
