package biz.thaicom.eBudgeting.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	
	@Query("SELECT result "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.timestamp = "
			+ "	(SELECT max(result1.timestamp) FROM ActivityTargetResult result1 WHERE  result.report = result1.report) "
			+ "	and result.report in (?1) ")
	List<ActivityTargetResult> findLatestTimeStampByReport(List<ActivityTargetReport> reports);
	

	@Query(""
			+ "SELECT result "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report.id = ?1 AND "
			+ "		result.budgetFiscalMonth = ?2 AND "
			+ " 	result.resultBudgetType = TRUE ")
	ActivityTargetResult findByReportIdAndFiscalMonthAndBgtResult(
			Long targetReportId, Integer fiscalMonth);


	@Query(""
			+ "SELECT objective.id, sum(result.budgetResult) "
			+ "FROM ActivityTargetResult result "
			+ "		INNER JOIN result.report report  "
			+ "		INNER JOIN report.target target " 
			+ "		INNER JOIN target.activity activity "
			+ " 	INNER JOIN activity.forObjective objective "
			+ "WHERE  "
			+ " 	result.resultBudgetType = TRUE "
			+ "		AND result.removed = FALSE " 
			+ "		AND objective.fiscalYear = ?1 " 
			+ "GROUP BY objective.id ")
	Iterable<Object[]>  findBudgetResultByFiscalYear(Integer fiscalYear);

	
	@Query(""
			+ "SELECT result "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report.id = ?1 AND "
			+ "		MONTH(result.reportedResultDate) = ?2 AND "
			+ " 	result.resultBudgetType = FALSE ")
	List<ActivityTargetResult> findByReportIdAndFiscalMonthAndNotBgtResult(
			Long targetReportId, Integer fiscalMonth);

	@Query(""
			+ "SELECT sum(result.result) "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report = ?1 AND "
			+ "		result.budgetFiscalMonth = ?2 AND "
			+ " 	result.resultBudgetType = FALSE ")
	Double findSumResultByReportAndFiscalMonth(ActivityTargetReport report,
			Integer fiscalMonth);

	@Query(""
			+ "SELECT count(result) "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report = ?1 AND "
			+ "		result.budgetFiscalMonth = ?2 ")
	Long countResultByReportIdAndFiscalMonthAndBgtResult(ActivityTargetReport report,
			Integer fiscalMonth);

	@Query(""
			+ "SELECT count(result), result.report.id "
			+ "FROM ActivityTargetResult result "
			+ "WHERE result.report.id in (?1) AND "
			+ "		result.budgetFiscalMonth = ?2 "
			+ "GROUP BY result.report.id ")
	List<Object[]> countResultByReportIdAndFiscalMonthAndBgtResult(List<Long> report,
			Integer fiscalMonth);

	
	Collection<? extends ActivityTargetResult> findByReport(
			ActivityTargetReport deleteReport);

}
