package com.idoncare.quest.domain.mission.application.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.idoncare.quest.domain.mission.application.openfeign.MissionOpenFeign;
import com.idoncare.quest.domain.mission.dto.MissionDto;
import com.idoncare.quest.domain.mission.dto.req.CheckMissionReq;
import com.idoncare.quest.domain.mission.dto.req.MissionIdReq;
import com.idoncare.quest.domain.mission.dto.req.MissionReq;
import com.idoncare.quest.domain.mission.dto.req.ModifyMissionReq;
import com.idoncare.quest.domain.mission.dto.resp.MissionDetailResp;
import com.idoncare.quest.domain.mission.repository.MissionRepository;
import com.idoncare.quest.domain.notification.application.service.NotificationProducerService;
import com.idoncare.quest.domain.notification.dto.req.MissionNotiReq;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.global.common.ErrorCode;
import com.idoncare.quest.global.exception.NotFoundException;

@Service
public class MissionServiceImpl implements MissionService {

	@Autowired
	public AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucketName}")
	private String bucketName;

	private final MissionOpenFeign missionOpenFeign;
	private final MissionRepository missionRepository;
	private final NotificationProducerService notificationProducerService;

	public MissionServiceImpl(MissionOpenFeign missionOpenFeign, MissionRepository missionRepository,
		NotificationProducerService notificationProducerService) {
		this.missionOpenFeign = missionOpenFeign;
		this.missionRepository = missionRepository;
		this.notificationProducerService = notificationProducerService;
	}

	public String missionToBankTest(int mission_serial) {
		System.out.println("mission service");
		return missionOpenFeign.missionToBankTest(mission_serial);
	}

	@Override
	public void registMission(MissionReq missionReq) {
		missionRepository.registMission(missionReq.getRelationId(),
			"IN PROGRESS",
			missionReq.getTitle(),
			missionReq.getContent(),
			missionReq.getAmount (),
			LocalDateTime.now(),
			false,
			LocalDateTime.now());
	}

	@Override
	public List<MissionDetailResp> getMissionList(RelationIdReq relationIdReq) {
		List<MissionDto> missionDtoList = missionRepository.getMissionList(relationIdReq.getRelationId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.Q003));

		List<MissionDetailResp> missionList = new ArrayList<>();
		for (MissionDto missionDto : missionDtoList) {
			MissionDetailResp missionDetailResp = MissionDetailResp.builder()
				.relationId(relationIdReq.getRelationId())
				.missionId(missionDto.getMissionId())
				.state(missionDto.getState())
				.title(missionDto.getTitle())
				.content(missionDto.getContent())
				.amount(missionDto.getAmount())
				.proofPictureUrl(missionDto.getProofPictureUrl())
				.build();
			missionList.add(missionDetailResp);
		}
		return missionList;
	}

	@Override
	public List<MissionDetailResp> getMissionInProgessList(RelationIdReq relationIdReq) {
		List<MissionDto> missionDtoList = missionRepository.getMissionInProgessList(relationIdReq.getRelationId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.Q003));

		List<MissionDetailResp> missionList = new ArrayList<>();
		for (MissionDto missionDto : missionDtoList) {
			MissionDetailResp missionDetailResp = MissionDetailResp.builder()
				.relationId(relationIdReq.getRelationId())
				.missionId(missionDto.getMissionId())
				.state(missionDto.getState())
				.title(missionDto.getTitle())
				.content(missionDto.getContent())
				.amount(missionDto.getAmount())
				.proofPictureUrl(missionDto.getProofPictureUrl())
				.build();
			missionList.add(missionDetailResp);
		}
		return missionList;
	}

	@Override
	public List<MissionDetailResp> getMissionCompleteList(RelationIdReq relationIdReq) {
		List<MissionDto> missionDtoList = missionRepository.getMissionCompleteList(relationIdReq.getRelationId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.Q003));

		List<MissionDetailResp> missionList = new ArrayList<>();
		for (MissionDto missionDto : missionDtoList) {
			MissionDetailResp missionDetailResp = MissionDetailResp.builder()
				.relationId(relationIdReq.getRelationId())
				.missionId(missionDto.getMissionId())
				.state(missionDto.getState())
				.title(missionDto.getTitle())
				.content(missionDto.getContent())
				.amount(missionDto.getAmount())
				.proofPictureUrl(missionDto.getProofPictureUrl())
				.build();
			missionList.add(missionDetailResp);
		}
		return missionList;
	}

	@Override
	public void modifyMission(ModifyMissionReq modifyMissionReq) {
		missionRepository.modifyMission(modifyMissionReq.getMissionId(),
			modifyMissionReq.getTitle(),
			modifyMissionReq.getContent(),
			modifyMissionReq.getAmount());
	}

	@Override
	public void deleteMission(MissionIdReq missionIdReq) {
		missionRepository.deleteMission(missionIdReq.getMissionId());
	}

	@Override
	public void reviewMission(MultipartFile image, String missionId) {
		try {
			String url = this.uploadImage(image);
			Long mId = Long.parseLong(missionId);
			missionRepository.reviewMission(url, mId);
			MissionDto missionDto = missionRepository.getMission(mId);
			MissionNotiReq missionNotiReq = new MissionNotiReq(missionDto.getRelationId(), missionDto.getTitle(), 0L, true);
			notificationProducerService.sendMissionNotification("mission-review", missionNotiReq);
		} catch (IOException e) {
			throw new NotFoundException(ErrorCode.Q005);
		}
	}

	@Override
	public void checkMission(CheckMissionReq checkMissionReq) {
		MissionDto missionDto = missionRepository.getMission(checkMissionReq.getMissionId());
		MissionNotiReq missionNotiReq = new MissionNotiReq(missionDto.getRelationId(), missionDto.getTitle(), missionDto.getAmount(), true);
		notificationProducerService.sendMissionNotification("mission-complete", missionNotiReq);
		missionRepository.checkMission(checkMissionReq.getMissionId());
	}

	private String uploadImage(MultipartFile image) throws IOException {
		String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
		InputStream inputStream = image.getInputStream();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(image.getSize());
		amazonS3.putObject(bucketName, fileName, inputStream, metadata);
		return amazonS3.getUrl(bucketName, fileName).toString();
	}
}
