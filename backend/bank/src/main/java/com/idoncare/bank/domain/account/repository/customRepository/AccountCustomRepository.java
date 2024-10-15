package com.idoncare.bank.domain.account.repository.customRepository;

import java.util.Optional;

import com.idoncare.bank.domain.account.entity.Account;

public interface AccountCustomRepository {

	Optional<Long> findBalanceByUserId(Long userId);

	Optional<Long> findUserIdByAccountNum(String accountNum);

	Optional<String> findAccountNumByUserId(Long userId);

	Long findUserIdByAccountNumNull(String accountNum);

	Optional<Long> findBalanceByAccountNum(String accountNum);
}
