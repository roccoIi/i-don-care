package com.idoncare.bank.domain.account.repository.customRepository;

import static com.idoncare.bank.domain.account.entity.QAccount.*;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountCustomRepositoryImpl implements AccountCustomRepository{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<Long> findBalanceByUserId(Long userId) {

		return Optional.ofNullable(jpaQueryFactory
			.select(account.balance)
			.from(account)
			.where(account.userId.eq(userId)
				.and(account.deleteYn.eq(false)))
			.fetchOne()
		); // fetchOne()은 결과가 1개일 때 사용
	}

	@Override
	public Optional<Long> findUserIdByAccountNum(String accountNum){
		return Optional.ofNullable(jpaQueryFactory
			.select(account.userId)
			.from(account)
			.where(account.accountNum.eq(accountNum))
			.fetchOne()
		);
	}

	@Override
	public Optional<String> findAccountNumByUserId(Long userId) {
		return Optional.ofNullable(jpaQueryFactory
			.select(account.accountNum)
			.from(account)
			.where(account.userId.eq(userId))
			.fetchOne()
		);
	}

	@Override
	public Long findUserIdByAccountNumNull(String accountNum) {
		return jpaQueryFactory
			.select(account.userId)
			.from(account)
			.where(account.accountNum.eq(accountNum))
			.fetchOne();
	}

	@Override
	public Optional<Long> findBalanceByAccountNum(String accountNum) {
		return Optional.ofNullable(jpaQueryFactory
			.select(account.balance)
			.from(account)
			.where(account.accountNum.eq(accountNum))
			.fetchOne()
		);
	}

}
