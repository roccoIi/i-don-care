package com.idoncare.bank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idoncare.bank.domain.account.entity.Trans;
import com.idoncare.bank.domain.account.repository.customRepository.TransCustomRepository;

public interface TransRepository extends JpaRepository<Trans, Long> , TransCustomRepository {

}
