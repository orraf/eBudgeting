package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetResult;

public interface ActivityTargetResultRepository extends JpaRepository<ActivityTargetResult, Long> {

	@Query("" +
			"SELECT result " +
			"FROM ActivityTargetResult result " +
			"WHERE result.report = ?1 " +
			"	AND result.timestamp = " +
			"		(SELECT max(result1.timestamp) FROM ActivityTargetResult result1 WHERE  result1.report = ?1) ")
	ActivityTargetResult findByLatestTimeStamp(ActivityTargetReport report);

	@Query(""
			+ "SELECT result "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report.id = ?1 AND "
			+ "		result.budgetFiscalMonth = ?2 AND "
			+ " 	result.resultBudgetType = TRUE ")
	ActivityTargetResult findByReportIdAndFiscalMonthAndBgtResult(
			Long targetReportId, Integer fiscalMonth);

	@Query(""
			+ "SELECT result "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report.id = ?1 AND "
			+ "		MONTH(result.reportedResultDate) = ?2 AND "
			+ " 	result.resultBudgetType = FALSE ")
	List<ActivityTargetResult> findByReportIdAndFiscalMonthAndNotBgtResult(
			Long targetReportId, Integer fiscalMonth);

}
