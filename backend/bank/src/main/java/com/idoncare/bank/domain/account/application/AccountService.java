package com.idoncare.bank.domain.account.application;

import java.util.List;

import com.idoncare.bank.domain.account.dto.req.requestClient.AccountAuth;
import com.idoncare.bank.domain.account.dto.req.requestClient.AccountNumber;
import com.idoncare.bank.domain.account.dto.resp.responseClient.AuthNumResponse;
import com.idoncare.bank.domain.account.dto.resp.responseClient.UserAccountDetail;
import com.idoncare.bank.domain.account.dto.resp.responseClient.BalanceInfo;

public interface AccountService {
	void sendAuthNum(String accountNum);

	void checkAuthNum(AccountAuth accountAuth);

	void registAccount(AccountAuth accountAuth, Long userId);

	BalanceInfo getBalance(Long userId);

	UserAccountDetail getAccountDetail(Long userId);

	String getUserId(AccountNumber accountNumber);

	AuthNumResponse accountHistoryList(AccountNumber accountNumber);

	List<AccountNumber> unRegisterAccount();

	void registCard(Long userid);
}
