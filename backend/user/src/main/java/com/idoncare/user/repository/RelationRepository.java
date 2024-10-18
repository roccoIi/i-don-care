package com.idoncare.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.idoncare.user.entity.RelationEntity;

public interface RelationRepository extends JpaRepository<RelationEntity, Long> {

	@Query("SELECT r FROM RelationEntity r WHERE r.child.userId = :childId")
	RelationEntity findRelationByChildUserId(@Param("childId") Long childId);

	@Query("SELECT r FROM RelationEntity r WHERE r.parent.userId = :parentId")
	List<RelationEntity> findRelationsByParentUserId(@Param("parentId") Long parentId);

	@Query("SELECT COUNT(r) > 0 FROM RelationEntity r WHERE r.parent.userId = :userId OR r.child.userId = :userId")
	boolean existsByParentUserIdOrChildUserId(@Param("userId") Long userId);

	@Query("SELECT COUNT(r) > 0 FROM RelationEntity r WHERE r.parent.userId = :parentId AND r.child.userId = :childId")
	boolean existsByParentUserIdAndChildUserId(@Param("childId") Long childId, @Param("parentId") Long parentId);

}
