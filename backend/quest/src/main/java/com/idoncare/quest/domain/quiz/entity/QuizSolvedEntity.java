package com.idoncare.quest.domain.quiz.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(
	name = "quiz_solved",
	uniqueConstraints = @UniqueConstraint(columnNames = {"relation_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE account SET delete_yn = true WHERE solved_id = ?")
@SQLRestriction("delete_yn = false")
public class QuizSolvedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "solved_id")
	private Long solvedId;

	@Column(name = "is_solved")
	private Boolean isSolved;

	@Column(name = "quiz_rating")
	private Long quizRating;

	@Column(name = "previous_quiz_rating")
	private Long previousQuizRating;

	@Column(name = "relation_id")
	private Long relationId;

	@Column(name = "delete_yn")
	private Boolean deleteYn;

	@Column(name = "create_at")
	private LocalDateTime createAt;
}
