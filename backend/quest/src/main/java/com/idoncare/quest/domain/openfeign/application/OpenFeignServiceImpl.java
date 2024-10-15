package com.idoncare.quest.domain.openfeign.application;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.idoncare.quest.domain.openfeign.repository.OpenFeignRepository;
import com.idoncare.quest.global.common.ErrorCode;
import com.idoncare.quest.global.exception.DuplicateException;

@Service
public class OpenFeignServiceImpl implements OpenFeignService {

	private OpenFeignRepository openFeignRepository;

	public OpenFeignServiceImpl(OpenFeignRepository openFeignRepository) {
		this.openFeignRepository = openFeignRepository;
	}

	@Override
	public void addUser(Long relationId) {
		try {
			openFeignRepository.addUser(false, 0L, relationId, false, LocalDateTime.now());
		} catch (DataIntegrityViolationException e){
			throw new DuplicateException(ErrorCode.Q001);
		}
	}
}
