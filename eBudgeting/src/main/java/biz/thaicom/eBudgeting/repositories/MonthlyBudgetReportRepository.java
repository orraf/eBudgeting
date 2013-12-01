package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.pln.MonthlyBudgetReport;

public interface MonthlyBudgetReportRepository extends JpaRepository<MonthlyBudgetReport, Long> {

	@Query(""
			+ "SELECT activity.forObjective.id, sum(coalesce(monthlyBudget.budgetPlan, 0)),"
			+ "		 sum(coalesce(monthlyBudget.budgetResult, 0)) "
			+ "FROM MonthlyBudgetReport monthlyBudget "
			+ "		INNER JOIN monthlyBudget.activityPerformance performance "
			+ "	 	INNER JOIN performance.activity activity "
			+ "WHERE activity.forObjective.fiscalYear = ?1 "
			+ "GROUP BY activity.forObjective.id "
			+ "")
	public Iterable<Object[]> findSumBudgetByFiscalYear(Integer fiscalYear);
	
}
