package com.idoncare.quest.domain.savings.application.service;

import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.domain.savings.dto.req.AddAmountReq;
import com.idoncare.quest.domain.savings.dto.req.CancelCoinboxReq;
import com.idoncare.quest.domain.savings.dto.req.CoinboxReq;
import com.idoncare.quest.domain.savings.dto.resp.ChildCoinboxDetailResp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface SavingsService {
	void registCoinbox(@Valid CoinboxReq coinboxReq);

	ChildCoinboxDetailResp getChildDetail(@Valid RelationIdReq relationIdReq);

	void cancelCoinbox(@Valid CancelCoinboxReq cancelCoinboxReq);

	void addAmount(@Valid AddAmountReq addAmountReq, HttpServletRequest request);

	void addInterest(@Valid AddAmountReq addAmountReq);
}
