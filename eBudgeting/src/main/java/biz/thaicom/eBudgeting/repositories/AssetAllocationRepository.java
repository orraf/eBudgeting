package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.BudgetProposal;
import biz.thaicom.eBudgeting.models.hrx.Organization;

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
			"	LEFT JOIN FETCH assetAllocation.operator " +
			"	INNER JOIN FETCH assetAllocation.budgetType " +
			"	INNER JOIN FETCH assetAllocation.assetBudget " +
			"WHERE assetAllocation.forObjective.id = ?1 ")
	List<AssetAllocation> findAllByForObjectiveId(Long objectiveId);

	@Query("" +
			"SELECT assetAllocation " +
			"FROM AssetAllocation assetAllocation " +
			"	LEFT JOIN FETCH assetAllocation.owner " +
			"	LEFT JOIN FETCH assetAllocation.operator " +
			"	INNER JOIN FETCH assetAllocation.budgetType " +
			"	INNER JOIN FETCH assetAllocation.assetBudget " +
			"WHERE assetAllocation.forObjective.id = ?1 "
			+ "	AND (assetAllocation.operator = ?2 or assetAllocation.owner = ?2 or assetAllocation.operator=?3) "
			+ "ORDER BY assetAllocation.owner.id asc, assetAllocation.operator.id asc, assetAllocation.id asc ")
	List<AssetAllocation> findAllByForObjectiveIdAndOperator(Long objectiveId,
			Organization operator, Organization searchOrg);

	@Query("" +
			"SELECT assetAllocation " +
			"FROM AssetAllocation assetAllocation " +
			"	LEFT JOIN FETCH assetAllocation.owner " +
			"	LEFT JOIN FETCH assetAllocation.operator " +
			"	INNER JOIN FETCH assetAllocation.budgetType " +
			"	INNER JOIN FETCH assetAllocation.assetBudget " +
			"WHERE assetAllocation.fiscalYear = ?1 ")
	List<AssetAllocation> findAlByFiscalyear(Integer fiscalYear);


	@Query("" +
			"SELECT assetAllocation " +
			"FROM AssetAllocation assetAllocation " +
			"	LEFT JOIN FETCH assetAllocation.owner " +
			"	LEFT JOIN FETCH assetAllocation.operator " +
			"	INNER JOIN FETCH assetAllocation.budgetType " +
			"	INNER JOIN FETCH assetAllocation.assetBudget " +
			"WHERE assetAllocation.fiscalYear = ?1 "
			+ "AND (assetAllocation.owner = ?2  or assetAllocation.owner.parent = ?2 or assetAllocation.operator = ?2)")
	List<AssetAllocation> findAlByFiscalyearAndOwnerOrOperator(Integer fiscalYear,
			Organization workAt);

}
