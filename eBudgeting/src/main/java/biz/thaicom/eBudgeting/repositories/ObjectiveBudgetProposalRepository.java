package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.bgt.ObjectiveBudgetProposal;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Objective;

public interface ObjectiveBudgetProposalRepository extends
		JpaSpecificationExecutor<ObjectiveBudgetProposal>, PagingAndSortingRepository<ObjectiveBudgetProposal, Long> {

	@Query("" +
			"SELECT obp " +
			"FROM ObjectiveBudgetProposal obp " +
			"	INNER JOIN FETCH obp.forObjective objective " +
			"	INNER JOIN FETCH obp.budgetType type " +
			"WHERE objective.id = ?1 and obp.owner.id = ?2 ")
	List<ObjectiveBudgetProposal> findAllByForObjective_IdAndOwner_Id(
			Long objectiveId, Long ownerId);


}
