package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.pln.ActivityTarget;
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
			"WHERE report.owner.id = ?1 " +
			" 	AND report.target.activity.forObjective.fiscalYear = ?2 ")
	public List<ActivityTargetReport> findAllByOwner_idAndFiscalYear(Long ownerId, Integer fiscalYear);
	
	
	public List<ActivityTargetReport> findAllByTarget_idAndReportLevel(
			Long targetId, int reportLevel);

	public List<ActivityTargetReport> findAllByTarget_IdAndOwner_Parent_id(
			Long activityTargetId, Long parentOrgId);

	
	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"WHERE report.owner.id = ?2 " +
			"	AND report.target.activity.forObjective.parentPath like ?1 " )
	public List<ActivityTargetReport> findAllByParentObjectiveIdAndOwnerId(
			String objectiveIdLike, Long ownerId);

}
