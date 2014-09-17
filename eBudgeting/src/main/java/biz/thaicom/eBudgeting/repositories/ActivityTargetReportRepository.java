package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;

@Repository
public interface ActivityTargetReportRepository extends
		PagingAndSortingRepository<ActivityTargetReport, Long>, JpaSpecificationExecutor<ActivityTargetReport> {

		
	public List<ActivityTargetReport> findAllByTarget_id(Long targetId);

	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"	LEFT JOIN FETCH report.monthlyReports monthlyReports " +
			"WHERE report.target.id = ?1 and report.owner.id = ?2 ")
	public List<ActivityTargetReport> findAllByTarget_idAndOwner_Id(
			Long targetId, Long ownerId);

	
	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"	LEFT JOIN FETCH report.monthlyReports monthlyReports " +
			"	INNER JOIN FETCH report.activityPerformance performance " +
			"WHERE report.id = ?1  ")
	public ActivityTargetReport findOneAndFetchReportById(Long id);
	
	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"	LEFT JOIN FETCH report.monthlyReports monthlyReports " +
			"	INNER JOIN FETCH report.activityPerformance performance " +
			"	INNER JOIN FETCH report.target target " +
			"	INNER JOIN FETCH target.activity activity " +
			"	INNER JOIN FETCH activity.forObjective objective " +
			"WHERE report.owner.id = ?1 " +
			" 	AND report.target.activity.forObjective.fiscalYear = ?2 ")
	public List<ActivityTargetReport> findAllByOwner_idAndFiscalYear(Long ownerId, Integer fiscalYear);

	@Query("" +
			"SELECT report.id, objective.id " +
			"FROM ActivityTargetReport report, "
			+ "	ActivityTarget target,  "
			+ " Activity activity, "
			+ "	Objective objective " +
			//"	LEFT JOIN FETCH report.monthlyReports monthlyReports " +
			//"	INNER JOIN FETCH report.activityPerformance performance " +
			
			//"	INNER JOIN FETCH report.owner owner " +
//			"	INNER JOIN FETCH report.target target " +
//			"	INNER JOIN FETCH target.activity activity " +
//			"	INNER JOIN FETCH activity.forObjective objective " +
			"WHERE report.target.id = target.id "
			+ " AND target.activity.id = activity.id "
			+ "	AND activity.forObjective.id = objective.id "
			+ " AND report.owner.id = ?1 " +
			" 	AND objective.fiscalYear = ?2 ")
	public List<Object[]> findAllByOwner_idAndFiscalYearNoFetch(Long ownerId, Integer fiscalYear);
	
	
	public List<ActivityTargetReport> findAllByTarget_idAndReportLevel(
			Long targetId, int reportLevel);

	public List<ActivityTargetReport> findAllByTarget_IdAndOwner_Parent_id(
			Long activityTargetId, Long parentOrgId);

	
	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"WHERE report.owner.id = ?2 " +
			"	AND report.target.activity.forObjective.parentPath like ?1 " +
			"ORDER BY report.target.activity.code asc" +
			"" )
	public List<ActivityTargetReport> findAllByParentObjectiveIdAndOwnerId(
			String objectiveIdLike, Long ownerId);
	
	
	@Query(""
			+ "SELECT report "
			+ "FROM ActivityTargetReport report "
			+ "		INNER JOIN FETCH report.target target "
			+ "		INNER JOIN FETCH report.activityPerformance performance"
			+ " 	INNER JOIN FETCH target.activity activity  "
			+ "		INNER JOIN FETCH activity.forObjective obj1 "
			+ "		INNER JOIN FETCH obj1.parent obj2 "
			+ "		INNER JOIN FETCH obj2.parent obj3 "
			+ "		INNER JOIN FETCH obj3.parent obj4 "
			+ "		INNER JOIN FETCH obj4.parent obj5 "
			+ "		INNER JOIN FETCH obj5.parent obj6 "
			+ "WHERE ( report.owner.code like ?1 "
			+ "	) "
			+ "		AND activity.forObjective.fiscalYear = ?2 "
			+ "		AND report.reportLevel = 2 "
			+ "ORDER BY obj6.code asc, obj5.code asc, obj4.code asc, obj3.code asc, obj2.code asc, obj1.code asc, activity.code asc,"
			+ "		target.id asc ")
	public List<ActivityTargetReport> findAllByActivityOwnerOrRegulatorAndFiscalYear(
			String org, Integer fiscalYear);

}
