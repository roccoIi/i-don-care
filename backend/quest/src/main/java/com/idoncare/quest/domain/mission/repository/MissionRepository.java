package com.idoncare.quest.domain.mission.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idoncare.quest.domain.mission.dto.MissionDto;
import com.idoncare.quest.domain.mission.entity.MissionEntity;

import jakarta.transaction.Transactional;
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO mission(state, title, content, amount, input_date, delete_yn, created_at, relation_id) "
		+ "VALUES(:state, :title, :content, :amount, :inputDate, :deleteYn, :createdAt, :relationId)", nativeQuery = true)
	void registMission(@Param("relationId") Long relationId,
		@Param("state") String state,
		@Param("title") String title,
		@Param("content") String content,
		@Param("amount") Long amount,
		@Param("inputDate") LocalDateTime inputDate,
		@Param("deleteYn") boolean deleteYn,
		@Param("createdAt") LocalDateTime createdAt
	);

	@Query(value = "SELECT * "
		+ "FROM mission "
		+ "WHERE relation_id = :relationId "
		+ "AND state LIKE 'IN PROGRESS' "
		+ "LIMIT 2", nativeQuery = true)
	Optional<List<MissionDto>> getMissionList(@Param("relationId") Long relationId);

	@Query(value = "SELECT * "
		+ "FROM mission "
		+ "WHERE relation_id = :relationId "
		+ "AND state LIKE 'IN PROGRESS'", nativeQuery = true)
	Optional<List<MissionDto>> getMissionInProgessList(@Param("relationId") Long relationId);

	@Query(value = "SELECT * "
		+ "FROM mission "
		+ "WHERE relation_id = :relationId "
		+ "AND state LIKE 'COMPLETED'", nativeQuery = true)
	Optional<List<MissionDto>> getMissionCompleteList(@Param("relationId") Long relationId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE mission "
		+ "SET title = :title, "
		+ "content = :content, "
		+ "amount = :amount "
		+ "WHERE mission_id = :missionId", nativeQuery = true)
	void modifyMission(@Param("missionId") Long missionId, @Param("title") String title, @Param("content") String content, @Param("amount") Long amount);

	@Transactional
	@Modifying
	@Query(value = "UPDATE mission "
		+ "SET state = 'CANCELED' "
		+ "WHERE mission_id = :missionId", nativeQuery = true)
	void deleteMission(@Param("missionId") Long missionId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE mission "
		+ "SET proof_picture_url = :url "
		+ "WHERE mission_id = :missionId", nativeQuery = true)
	void reviewMission(@Param("url") String url, @Param("missionId") long missionId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE mission "
		+ "SET state = 'COMPLETED' "
		+ "WHERE mission_id = :missionId", nativeQuery = true)
	void checkMission(@Param("missionId") Long missionId);

	@Query(value = "SELECT * "
		+ "FROM mission "
		+ "WHERE mission_id = :missionId", nativeQuery = true)
	MissionDto getMission(@Param("missionId") Long missionId);
}
