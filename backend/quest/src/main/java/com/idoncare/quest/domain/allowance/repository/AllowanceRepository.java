package com.idoncare.quest.domain.allowance.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idoncare.quest.domain.allowance.dto.AllowanceDto;
import com.idoncare.quest.domain.allowance.entity.AllowanceEntity;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public interface AllowanceRepository extends JpaRepository<AllowanceEntity, Long> {

	@Query(value = "SELECT * "
		+ "FROM allowance "
		+ "WHERE relation_id = :relationId", nativeQuery = true)
	Optional<AllowanceDto> detailAllowance(@Param("relationId") Long relationId);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO allowance(relation_id, type, day, amount, delete_yn, created_at) "
		+ "VALUES(:relationId, :type, :day, :amount, :deleteYn, :createdAt)", nativeQuery = true)
	void registAllowance(@Param("relationId") Long relationId,
		@Param("type") Long type,
		@Param("day") Long day,
		@Param("amount") Long amount,
		@Param("deleteYn") boolean deleteYn,
		@Param("createdAt") LocalDateTime createdAt);

	@Query(value = "SELECT * "
		+ "FROM allowance "
		+ "WHERE (type = 1 and day = DAYOFWEEK(CURDATE())) or (type = 2 and day = DAY(CURDATE()))", nativeQuery = true)
	List<AllowanceDto> dailyUpdate();

	@Transactional
	@Modifying
	@Query(value = "UPDATE allowance "
		+ "SET type = :type, day = :day, amount = :amount "
		+ "WHERE relation_id = :relationId", nativeQuery = true)
	void modifyAllowance(@Param("relationId") Long relationId,
		@Param("type") Long type,
		@Param("day") Long day,
		@Param("amount") Long amount);

	@Transactional
	@Modifying
	@Query(value = "UPDATE allowance "
		+ "SET delete_yn = true "
		+ "WHERE allowance_id = :allowanceId", nativeQuery = true)
	void deleteAllowance(@Param("allowanceId") Long allowanceId);
}
