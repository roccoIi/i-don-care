package com.idoncare.user.application.relation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idoncare.user.domain.mapper.RelationMapper;
import com.idoncare.user.domain.relation.Relation;
import com.idoncare.user.entity.RelationEntity;
import com.idoncare.user.global.common.exception.ErrorCode;
import com.idoncare.user.global.exception.NotFoundException;
import com.idoncare.user.repository.RelationRepository;

@Service
public class RelationService {

	@Autowired
	private RelationRepository relationRepository;

	public Relation findParentOfChild(Long childId) {
		RelationEntity relation = relationRepository.findRelationByChildUserId(childId);
		return RelationMapper.toDomain(relation);
	}

	public List<Relation> findChildrenOfParent(Long parentId) {
		List<RelationEntity> relations = relationRepository.findRelationsByParentUserId(parentId);
		return relations.stream()
			.map(RelationMapper::toDomain)
			.collect(Collectors.toList());
	}

	public Relation registerRelation(Relation relation) {
		return RelationMapper.toDomain(relationRepository.save(RelationMapper.toEntity(relation)));
	}

	public boolean isExistRelationOfUser(long userId) {
		return relationRepository.existsByParentUserIdOrChildUserId(userId);
	}

	public void deleteRelation(Long relationId) {
		relationRepository.deleteById(relationId);
	}

	public boolean isExistRelationBetweenUsers(Long childId, Long parentId) {
		return relationRepository.existsByParentUserIdAndChildUserId(childId, parentId);
	}

	public Relation findParentAndChild(Long relationId) {
		RelationEntity relationEntity = relationRepository.findById(relationId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.U012));

		return RelationMapper.toDomain(relationEntity);
	}
}
