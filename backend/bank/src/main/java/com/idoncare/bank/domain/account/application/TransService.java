package com.idoncare.bank.domain.account.application;

import java.util.List;

import com.idoncare.bank.domain.account.dto.req.requestClient.CardTransHistory;
import com.idoncare.bank.domain.account.dto.req.requestClient.SendInfo;
import com.idoncare.bank.domain.account.dto.resp.responseClient.RecentTransactionPeople;
import com.idoncare.bank.domain.account.dto.resp.responseClient.TransactionHistoryInfo;
import com.idoncare.bank.domain.account.dto.resp.responseServer.CardTransResult;

public interface TransService {

	TransactionHistoryInfo getTransactionList(Long userId, String accountNum);

	void giveMoneyToComm(Long userId, SendInfo sendInfo);

	List<RecentTransactionPeople> getTransactionNameList(Long userId);

	CardTransResult addCardTransHistory(CardTransHistory cardTransHistory);
}
