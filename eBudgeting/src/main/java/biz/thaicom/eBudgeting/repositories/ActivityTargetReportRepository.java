package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;

@Repository
public interface ActivityTargetReportRepository extends
		PagingAndSortingRepository<ActivityTargetReport, Long>, JpaSpecificationExecutor<ActivityTargetReport> {

		
	List<ActivityTargetReport> findAllByTarget_id(Long targetId);

	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"	INNER JOIN FETCH report.monthlyReports monthlyReports " +
			"WHERE report.target.id = ?1 and report.owner.id = ?2 ")
	List<ActivityTargetReport> findAllByTarget_idAndOwner_Id(
			Long targetId, Long ownerId);

	List<ActivityTargetReport> findAllByTarget_idAndOwner_Parent_id(
			Long targetId, Long parentOrgId);

	
	@Query("" +
			"SELECT report " +
			"FROM ActivityTargetReport report " +
			"	INNER JOIN FETCH report.monthlyReports monthlyReports " +
			"WHERE report.id = ?1  ")
	ActivityTargetReport findOneAndFetchReportById(Long id);

}
