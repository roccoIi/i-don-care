package com.idoncare.bank.domain.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idoncare.bank.domain.account.entity.Account;
import com.idoncare.bank.domain.account.repository.customRepository.AccountCustomRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountCustomRepository {

	boolean existsByAccountNum(String accountNum);

	Optional<Account> findByAccountNum(String accountNum);

	Optional<Account> findByUserId(Long userId);

}
