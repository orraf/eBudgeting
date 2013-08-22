package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.bgt.BudgetUsageFromExternal;
import biz.thaicom.eBudgeting.models.bgt.BudgetUsageId;

public interface BudgetUsageFromExternalRepository extends PagingAndSortingRepository<BudgetUsageFromExternal, BudgetUsageId> {

	@Query("" +
			"SELECT sum(bgt.amount) " +
			"FROM BudgetUsageFromExternal bgt " +
			"WHERE bgt.id.activityCode = ?1 " +
			"	AND bgt.id.organizationId = ?2 ")
	public Double findBudgetUsageSummaryByCodeAndOwner(String code, Long ownerId);
	
}
