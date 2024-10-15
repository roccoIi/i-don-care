package com.idoncare.quest.domain.openfeign.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idoncare.quest.domain.quiz.entity.QuizSolvedEntity;

import jakarta.transaction.Transactional;

public interface OpenFeignRepository extends JpaRepository<QuizSolvedEntity, Long> {

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO quiz_solved(is_solved, quiz_rating, relation_id, delete_yn, create_at) "
		+ "VALUES (:isSolved, :quizRating, :relationId, :deleteYn, :createAt)", nativeQuery = true)
	Optional<Integer> addUser(@Param("isSolved") boolean isSolved, @Param("quizRating") Long quizRating, @Param("relationId") Long relationId, @Param("deleteYn") Boolean deleteYn, @Param("createAt") LocalDateTime createAt);
}
