package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.pln.ObjectiveOwnerRelation;

public interface ObjectiveOwnerRelationRepository extends
	JpaSpecificationExecutor<ObjectiveOwnerRelation>, 
	PagingAndSortingRepository<ObjectiveOwnerRelation, Long> {

	ObjectiveOwnerRelation findByObjective_Id(Long id);

}
