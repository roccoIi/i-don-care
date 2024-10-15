package com.idoncare.quest.domain.quiz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idoncare.quest.domain.quiz.dto.QuizDto;
import com.idoncare.quest.domain.quiz.dto.QuizSolvedDto;
import com.idoncare.quest.domain.quiz.entity.QuizEntity;

import jakarta.transaction.Transactional;

public interface QuizRepository extends JpaRepository<QuizEntity, Long> {

	@Transactional
	@Modifying
	@Query(value = "UPDATE quiz_solved "
		+ "SET previous_quiz_rating = quiz_rating, "
		+ "is_solved = false", nativeQuery = true)
	void updateRating();

	@Query(value = "SELECT * "
		+ "FROM quiz "
		+ "WHERE DATE (`date`) = CURDATE()", nativeQuery = true)
	List<QuizDto> dailyUpdate();

	@Query(value = "SELECT * "
		+ "FROM quiz_solved "
		+ "WHERE relation_id = :relationId", nativeQuery = true)
	Optional<QuizSolvedDto> getUserInfo(@Param("relationId") Long relationId);

	@Transactional
	@Modifying
	@Query(value = "UPDATE quiz_solved "
		+ "SET is_solved = true, "
		+ "quiz_rating = quiz_rating + :rate "
		+ "WHERE relation_id = :relationId", nativeQuery = true)
	void reviewQuiz(@Param("relationId") Long relationId, @Param("rate") Long rate);
}
