package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.BudgetProposal;

public interface AssetAllocationRepository extends JpaRepository<AssetAllocation, Long> {

	List<AssetAllocation> findAllByParentOwner_IdAndForObjective_IdAndBudgetType_Id(
			Long parentOwnerId, Long forObjectiveId, Long budgetTyeId);

	@Query("" +
			"SELECT sum(assetAllocation.quantity * assetAllocation.unitBudget) " +
			"FROM AssetAllocation assetAllocation " +
			"WHERE assetAllocation.proposal = ?1 ")
	Long findSumBudgetOfPropsoal(BudgetProposal proposal);

	@Query("" +
			"SELECT assetAllocation " +
			"FROM AssetAllocation assetAllocation " +
			"	LEFT JOIN FETCH assetAllocation.owner " +
			"	INNER JOIN FETCH assetAllocation.budgetType " +
			"	INNER JOIN FETCH assetAllocation.assetBudget " +
			"WHERE assetAllocation.forObjective.id = ?1 ")
	List<AssetAllocation> findAllByForObjectiveId(Long objectiveId);

}
