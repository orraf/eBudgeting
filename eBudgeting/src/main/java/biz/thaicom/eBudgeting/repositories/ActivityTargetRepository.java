package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityTarget;

public interface ActivityTargetRepository extends JpaSpecificationExecutor<ActivityTarget>,
		PagingAndSortingRepository<ActivityTarget, Long> {

	
	@Query(""
			+ "SELECT target.id, sum(COALESCE(monthly.activityPlan,0)), sum(COALESCE(monthly.activityResult,0)) "
			+ "FROM ActivityTarget target "
			+ "		INNER JOIN target.reports report "
			+ "		LEFT JOIN report.monthlyReports monthly "
			+ "WHERE (monthly.fiscalMonth is null OR "
			+ "			(monthly.fiscalMonth >= ?1 	AND monthly.fiscalMonth <= ?2) ) "
			+ "		AND target.activity in ( ?3 ) "
			+ "		AND report.reportLevel = 1 "
			+ "GROUP BY target.id ")
	List<Object[]> findSumTargetByMonthAndActivities(int start, int end,
			List<Activity> activities);
	
	
	@Query(""
			+ "SELECT target.id, sum(COALESCE(monthly.activityPlan,0)), sum(COALESCE(monthly.activityResult,0)) "
			+ "FROM ActivityTarget target "
			+ "		INNER JOIN target.reports report "
			+ "		LEFT JOIN report.monthlyReports monthly "
			+ "WHERE (monthly.fiscalMonth is null OR "
			+ "			(monthly.fiscalMonth >= ?1 	AND monthly.fiscalMonth <= ?2) ) "
			+ "		AND target.activity in ( ?3 ) "
			+ " 	AND (report.owner is null OR report.owner in ( ?4 ) ) "
			+ "		AND report.reportLevel = 2 "
			+ "GROUP BY target.id ")
	List<Object[]> findSumTargetByMonthAndActivitiesAndOrganization(int start, int end,
			List<Activity> activities, List<Organization> organizations);

}
