package com.idoncare.quest.domain.allowance.application.service;

import com.idoncare.quest.domain.allowance.dto.req.AllowanceIdReq;
import com.idoncare.quest.domain.allowance.dto.req.AllowanceReq;
import com.idoncare.quest.domain.allowance.dto.resp.AllowanceDetailResp;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;

import jakarta.validation.Valid;

public interface AllowanceService {
	AllowanceDetailResp detailAllowance(RelationIdReq relationIdReq);

	void registAllowance(AllowanceReq allowanceReq);

	void modifyAllowance(AllowanceReq allowanceReq);

	void deleteAllowance(AllowanceIdReq allowanceIdReq);
}
