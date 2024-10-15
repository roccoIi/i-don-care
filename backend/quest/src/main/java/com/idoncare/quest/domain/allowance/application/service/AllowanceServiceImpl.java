package com.idoncare.quest.domain.allowance.application.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.idoncare.quest.domain.allowance.dto.AllowanceDto;
import com.idoncare.quest.domain.allowance.dto.req.AllowanceIdReq;
import com.idoncare.quest.domain.allowance.dto.req.AllowanceReq;
import com.idoncare.quest.domain.allowance.dto.resp.AllowanceDetailResp;
import com.idoncare.quest.domain.allowance.repository.AllowanceRepository;
import com.idoncare.quest.domain.mission.dto.req.MissionIdReq;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.global.common.ErrorCode;
import com.idoncare.quest.global.exception.NotFoundException;

@Service
public class AllowanceServiceImpl implements AllowanceService {

	private final AllowanceRepository allowanceRepository;

	public AllowanceServiceImpl(AllowanceRepository allowanceRepository) {
		this.allowanceRepository = allowanceRepository;
	}

	@Override
	public AllowanceDetailResp detailAllowance(RelationIdReq relationIdReq) {
		AllowanceDto allowanceDto = allowanceRepository.detailAllowance(relationIdReq.getRelationId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.Q008));

		AllowanceDetailResp allowanceDetailResp = AllowanceDetailResp.builder()
			.allowanceId(allowanceDto.getAllowanceId())
			.amount(allowanceDto.getAmount())
			.type(allowanceDto.getType())
			.day(allowanceDto.getDay())
			.relationId(allowanceDto.getRelationId())
			.build();
		return allowanceDetailResp;
	}

	@Override
	public void registAllowance(AllowanceReq allowanceReq) {
		allowanceRepository.registAllowance(allowanceReq.getRelationId(),
			allowanceReq.getType(),
			allowanceReq.getDay(),
			allowanceReq.getAmount(),
			false,
			LocalDateTime.now());
	}

	@Override
	public void modifyAllowance(AllowanceReq allowanceReq) {
		allowanceRepository.modifyAllowance(allowanceReq.getRelationId(),
			allowanceReq.getType(),
			allowanceReq.getDay(),
			allowanceReq.getAmount());
	}

	@Override
	public void deleteAllowance(AllowanceIdReq allowanceIdReq) {
		allowanceRepository.deleteAllowance(allowanceIdReq.getAllowanceId());
	}
}
