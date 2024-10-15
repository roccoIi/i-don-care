package com.idoncare.quest.domain.savings.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idoncare.quest.domain.savings.dto.CoinboxDto;
import com.idoncare.quest.domain.savings.entitiy.CoinboxEntity;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public interface SavingsRepository extends JpaRepository<CoinboxEntity, Long> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO coinbox(goal_title, goal_amount, amount, interest_amount, relation_id, delete_yn, created_at) "
		+ "VALUES(:goalTitle, :goalAmount, :amount, :interestAmount, :relationId, :deleteYn, :createdAt)", nativeQuery = true)
	void registCoinbox(@Param("goalTitle") String goalTitle,
		@Param("goalAmount") Long goalAmount,
		@Param("amount") Long amount,
		@Param("interestAmount") Long interestAmount,
		@Param("relationId") Long relationId,
		@Param("deleteYn") boolean deleteYn,
		@Param("createdAt") LocalDateTime createdAt);

	@Query(value = "SELECT * "
		+ "FROM coinbox "
		+ "WHERE relation_id = :relationId "
		+ "AND delete_yn = false "
		+ "ORDER BY created_at DESC "
		+ "LIMIT 1", nativeQuery = true)
	Optional<CoinboxDto> getChildDetail(@Param("relationId") Long relationId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE coinbox "
		+ "SET delete_yn = true "
		+ "WHERE coinbox_id = :coinboxId", nativeQuery = true)
	void cancelCoinbox(@Param("coinboxId") Long coinboxId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE coinbox "
		+ "SET amount = amount + :addAmount "
		+ "WHERE coinbox_id = :coinboxId", nativeQuery = true)
	void addAmount(@Param("coinboxId") Long coinboxId, @Param("addAmount") Long addAmount);

	@Transactional
	@Modifying
	@Query(value = "UPDATE coinbox "
		+ "SET interest_amount = interest_amount + :addAmount "
		+ "WHERE coinbox_id = :coinboxId", nativeQuery = true)
	void addInterest(@Param("coinboxId") Long coinboxId, @Param("addAmount") Long addAmount);

	@Query(value = "SELECT * "
		+ "FROM coinbox "
		+ "WHERE coinbox_id = :coinboxId", nativeQuery = true)
	CoinboxDto getCoinbox(@Param("coinboxId") Long coinboxId);
}
