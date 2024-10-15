package com.idoncare.bank.domain.account.repository.customRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.idoncare.bank.domain.account.entity.QAccount.*;
import static com.idoncare.bank.domain.account.entity.QTrans.*;

import com.idoncare.bank.domain.account.dto.resp.RecentUser_temp;
import com.idoncare.bank.domain.account.dto.resp.responseClient.RecentTransactionPeople;
import com.idoncare.bank.domain.account.entity.QAccount;
import com.idoncare.bank.domain.account.entity.QTrans;
import com.idoncare.bank.domain.account.entity.Trans;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransCustomRepositoryImpl implements TransCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Trans> findByAccountNumDESC(String accountNum) {
		return jpaQueryFactory
			.selectFrom(trans)
			.where(account.accountNum.eq(accountNum).and(account.deleteYn.eq(false)))
			.orderBy(trans.createAt.desc())
			.fetch();
	}

	//subquery 를 사용했지만 비효율적이라는 이야기를 듣고 변경
//	@Operation
//	public List<String> findAccountNumByAccountIdd(Long userId) {
//		return jpaQueryFactory
//				.select(trans.transactionAccountNo)
//				.from(trans)
//				.where(account.accountId.eq(
//						JPAExpressions.select(account.accountId)
//								.from(account)
//								.where(account.userId.eq(userId))
//				))
//				.groupBy(trans.transactionAccountNo)
//				.orderBy(trans.createAt.max().desc())
//				.limit(10)
//				.fetch();
//	}

	@Operation
	public List<String> findAccountNumByAccountId(Long userId) {
		return jpaQueryFactory
				.select(trans.transactionAccountNo)
				.from(trans)
				.join(trans.account, account)
				.where(account.userId.eq(userId)
						.and(trans.transactionAccountNo.isNotEmpty()))
				.groupBy(trans.transactionAccountNo)
				.orderBy(trans.createAt.max().desc())
				.limit(10)
				.fetch();
	}
}
