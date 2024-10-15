package com.idoncare.quest.domain.savings.application.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idoncare.quest.domain.notification.application.service.NotificationProducerService;
import com.idoncare.quest.domain.notification.dto.req.SavingsCancelNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsProgressNotiReq;
import com.idoncare.quest.domain.notification.dto.req.SavingsRegistNotiReq;
import com.idoncare.quest.domain.openfeign.application.SavingsCheckOpenFeign;
import com.idoncare.quest.domain.openfeign.dto.resp.SavingsCheckDto;
import com.idoncare.quest.domain.quiz.dto.req.RelationIdReq;
import com.idoncare.quest.domain.savings.dto.CoinboxDto;
import com.idoncare.quest.domain.savings.dto.req.AddAmountReq;
import com.idoncare.quest.domain.savings.dto.req.CancelCoinboxReq;
import com.idoncare.quest.domain.savings.dto.req.CoinboxReq;
import com.idoncare.quest.domain.savings.dto.resp.ChildCoinboxDetailResp;
import com.idoncare.quest.domain.savings.repository.SavingsRepository;
import com.idoncare.quest.global.common.ErrorCode;
import com.idoncare.quest.global.exception.NotFoundException;
import com.idoncare.quest.global.exception.UnAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SavingsServiceImpl implements SavingsService {

	private final SavingsRepository savingsRepository;
	private final NotificationProducerService notificationProducerService;
	private final SavingsCheckOpenFeign savingsCheckOpenFeign;
	private final ObjectMapper objectMapper;

	public SavingsServiceImpl(SavingsRepository savingsRepository,
		NotificationProducerService notificationProducerService,
		SavingsCheckOpenFeign savingsCheckOpenFeign, ObjectMapper objectMapper) {
		this.savingsRepository = savingsRepository;
		this.notificationProducerService = notificationProducerService;
		this.savingsCheckOpenFeign = savingsCheckOpenFeign;
		this.objectMapper = objectMapper;
	}

	@Override
	public void registCoinbox(CoinboxReq coinboxReq) {
		savingsRepository.registCoinbox(coinboxReq.getGoalTitle(), coinboxReq.getGoalAmount(), 0L, 0L, coinboxReq.getRelationId(), false, LocalDateTime.now());
		SavingsRegistNotiReq savingsRegistNotiReq = new SavingsRegistNotiReq(coinboxReq.getRelationId());
		notificationProducerService.sendSavingsRegistNotification("savings-regist-complete", savingsRegistNotiReq);
	}

	@Override
	public ChildCoinboxDetailResp getChildDetail(RelationIdReq relationIdReq) {
		CoinboxDto coinboxDto = savingsRepository.getChildDetail(relationIdReq.getRelationId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.Q003));

		ChildCoinboxDetailResp childCoinboxDetailResp = ChildCoinboxDetailResp.builder()
			.relationId(relationIdReq.getRelationId())
			.coinboxId(coinboxDto.getCoinboxId())
			.goalTitle(coinboxDto.getGoalTitle())
			.goalAmount(coinboxDto.getGoalAmount())
			.amount(coinboxDto.getAmount())
			.interestAmount(coinboxDto.getInterestAmount())
			.build();
		return childCoinboxDetailResp;
	}

	@Override
	public void cancelCoinbox(CancelCoinboxReq cancelCoinboxReq) {
		CoinboxDto coinboxDto = savingsRepository.getCoinbox(cancelCoinboxReq.getCoinboxId());
		SavingsCancelNotiReq savingsCancelNotiReq = new SavingsCancelNotiReq(coinboxDto.getRelationId(),
			coinboxDto.getAmount(), 0L, true);
		notificationProducerService.sendSavingsCancelNotification("savings-cancel", savingsCancelNotiReq);
		savingsRepository.cancelCoinbox(cancelCoinboxReq.getCoinboxId());
	}

	@Override
	public void addAmount(AddAmountReq addAmountReq, HttpServletRequest request) {
		Long userId = Optional.ofNullable(request.getHeader("X-User-Id"))
			.map(Long::parseLong)
			.orElseThrow(() -> new UnAuthorizedException(ErrorCode.Q006));

		String responseJson = savingsCheckOpenFeign.savingsCheckToBank(userId);

		SavingsCheckDto savingsCheckDto;
		try {
			savingsCheckDto = objectMapper.readValue(responseJson, SavingsCheckDto.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("JSON processing error", e);
		}

		if (savingsCheckDto.getData().getBalance() < addAmountReq.getAddAmount()) {
			throw new NotFoundException(ErrorCode.Q007);
		} else {
			savingsRepository.addAmount(addAmountReq.getCoinboxId(), addAmountReq.getAddAmount());
			CoinboxDto coinboxDto = savingsRepository.getCoinbox(addAmountReq.getCoinboxId());
			SavingsProgressNotiReq savingsProgressNotiReq = new SavingsProgressNotiReq(coinboxDto.getRelationId(),
				addAmountReq.getAddAmount());
			notificationProducerService.sendSavingsProgressNotification("savings-progress", savingsProgressNotiReq);
			if (coinboxDto.getAmount() >= coinboxDto.getGoalAmount()) {
				log.info("목표 달성");
				log.info("불러온 coinboxdto : {}", coinboxDto);
				savingsRepository.cancelCoinbox(coinboxDto.getCoinboxId());
				SavingsNotiReq savingsNotiReq = new SavingsNotiReq(coinboxDto.getRelationId(), coinboxDto.getAmount(), coinboxDto.getInterestAmount(), true);
				log.info("notification 구성 : {}", savingsNotiReq);
				notificationProducerService.sendSavingsNotification("savings-complete", savingsNotiReq);
				log.info("발행완료");
			}
		}
	}

	@Override
	public void addInterest(AddAmountReq addAmountReq) {
		savingsRepository.addInterest(addAmountReq.getCoinboxId(), addAmountReq.getAddAmount());
	}
}
