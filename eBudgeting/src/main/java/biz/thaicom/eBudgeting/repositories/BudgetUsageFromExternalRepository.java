package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.bgt.BudgetUsageFromExternal;
import biz.thaicom.eBudgeting.models.bgt.BudgetUsageId;

public interface BudgetUsageFromExternalRepository extends PagingAndSortingRepository<BudgetUsageFromExternal, BudgetUsageId> {

	@Query("" +
			"SELECT sum(bgt.amount) " +
			"FROM BudgetUsageFromExternal bgt " +
			"WHERE bgt.id.activityCode = ?1 " +
			"	AND bgt.id.organizationId = ?2 "
			+ "	AND bgt.fiscalYear = ?3 ")
	public Double findBudgetUsageSummaryByCodeAndOwner(String code, Long ownerId, Integer fiscalYear);
	
	@Query(""
			+ "SELECT bgt.id.activityCode, month(bgt.id.date), sum(bgt.amount) "
			+ "FROM BudgetUsageFromExternal bgt "
			+ "WHERE bgt.fiscalYear = ?1 "
			+ "	AND bgt.id.organizationId in (?2) "
			+ "GROUP BY bgt.id.activityCode, month(bgt.id.date)")
	public List<Object[]> findMonthlyBudgetUsageByfiscalYearAndOrgLike(Integer fiscalYear, List<Long> orgs);
	
	
//	@Query(""
//			+ "SELECT activity.forObjective.id, sum(performance.budgetAllocated) "
//			+ "FROM ActivityPerformance performance "
//			+ "	 	INNER JOIN performance.activity activity "
//			+ "WHERE activity.forObjective.fiscalYear = ?1 "
//			+ "GROUP BY activity.forObjective.id "
//			+ "")
	
	
	@Query(" " +
			"SELECT bgt.id.activityCode, sum(bgt.amount) " +
			"FROM BudgetUsageFromExternal bgt " +
			"WHERE bgt.fiscalYear = ?1 " +
			"GROUP BY bgt.id.activityCode ")
	public Iterable<Object[]> findSumBudgetUsedByFiscalYear(Integer fiscalYear);
	
}
