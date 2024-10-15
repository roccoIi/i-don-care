package com.idoncare.bank.domain.account.repository.customRepository;

import java.util.List;

import com.idoncare.bank.domain.account.dto.resp.RecentUser_temp;
import com.idoncare.bank.domain.account.dto.resp.responseClient.RecentTransactionPeople;
import com.idoncare.bank.domain.account.entity.Trans;
import com.querydsl.core.Tuple;

public interface TransCustomRepository {

	List<Trans> findByAccountNumDESC(String accountNum);

	List<String> findAccountNumByAccountId(Long userId);
}
