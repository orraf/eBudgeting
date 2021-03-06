package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityPerformance;
import biz.thaicom.eBudgeting.models.pln.ActivityTarget;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;

@Repository
public interface ActivityPerformanceRepository extends
		PagingAndSortingRepository<ActivityPerformance, Long>, JpaSpecificationExecutor<ActivityPerformance> {

	@Query("" +
			"SELECT activityPerformance " +
			"FROM ActivityPerformance activityPerformance " +
			"	INNER JOIN FETCH activityPerformance.activity activity " +
			"	INNER JOIN FETCH activity.forObjective objective " +
			"	INNER JOIN FETCH activityPerformance.targetReport report " +
			"	INNER JOIN FETCH report.target target " +
			"	INNER JOIN FETCH target.unit unit " +
			"WHERE " +
			"	activityPerformance.owner = ?1 AND" +
			"	objective.id = ?2 " +
			"ORDER BY activity.code ASC")
	public List<ActivityPerformance> findByOwnerAndObjectiveId(
			Organization workAt, Long objectiveId);

	public List<ActivityPerformance> findAllByActivityAndOwner(
			Activity activity, Organization owner);

	@Query("" +
			"SELECT report.activityPerformance " +
			"FROM ActivityTargetReport report " +
			"WHERE report = ?1")
	public ActivityPerformance findOneBytargetReport(ActivityTargetReport report);

	
	@Query("" +
			"SELECT performance " +
			"FROM ActivityPerformance performance " +
			"WHERE performance.activity = ?1 " +
			"	AND performance.targetReport.target = ?2 " +
			"	AND performance.owner.id = ?3 ")	
	public ActivityPerformance findOneByActivityAndTargetAndOwner_id(
			Activity activity,
			ActivityTarget target,
			Long parentOrgId);
	
	
	@Query(""
			+ "SELECT activity.forObjective.id, sum(performance.budgetAllocated) "
			+ "FROM ActivityPerformance performance "
			+ "	 	INNER JOIN performance.activity activity "
			+ "WHERE activity.forObjective.fiscalYear = ?1 "
			+ "		and performance.targetReport.reportLevel = 1 "
			+ "GROUP BY activity.forObjective.id "
			+ "")
	public Iterable<Object[]> findSumBudgetAllocatedByFiscalYear(Integer fiscalYear);
	
	@Query(""
			+ "SELECT activity.forObjective.parent.id, SUBSTRING(performance.owner.code, 0,4), sum(performance.budgetAllocated) "
			+ "FROM ActivityPerformance performance "
			+ "	 	INNER JOIN performance.activity activity "
			+ "WHERE activity.forObjective.fiscalYear = ?1 "
			+ "		and performance.targetReport.reportLevel = 1 "
			+ "GROUP BY activity.forObjective.parent.id, SUBSTRING(performance.owner.code, 0,4) "
			+ "")
	public Iterable<Object[]> findSumBudgetAllocatedByFiscalYearAndOwnerId(Integer fiscalYear);
	
	
	@Query(""
			+ "SELECT activity.forObjective.parent.id, sum(performance.budgetAllocated) "
			+ "FROM ActivityPerformance performance "
			+ "	 	INNER JOIN performance.activity activity "
			+ "WHERE activity.forObjective.fiscalYear = ?1 "
			+ "		and performance.targetReport.owner.id = ?2 "
			+ "GROUP BY activity.forObjective.parent.id "
			+ "")
	public Iterable<Object[]> findSumBudgetAllocatedByFiscalYearByOwnerId(Integer fiscalYear, Long OwnerId);

	
	@Query(""
			+ "SELECT performance "
			+ "FROM ActivityPerformance performance "
			+ "WHERE performance.activity = ?1 ")
	public List<ActivityPerformance> findAllByActivity(Activity activity);
	
}
