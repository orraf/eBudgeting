package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityPerformance;

@Repository
public interface ActivityPerformanceRepository extends
		PagingAndSortingRepository<ActivityPerformance, Long>, JpaSpecificationExecutor<ActivityPerformance> {

	public ActivityPerformance findOneByActivityAndOwner(Activity activity, Organization owner);

	@Query("" +
			"SELECT activityPerformance " +
			"FROM ActivityPerformance activityPerformance " +
			"	INNER JOIN FETCH activityPerformance.activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"	INNER JOIN FETCH activityPerformance.targetReports reports" +
			"	INNER JOIN FETCH reports.target target " +
			"	INNER JOIN FETCH target.unit unit " +
			"WHERE " +
			"	activityPerformance.owner = ?1 AND" +
			"	objective.id = ?2 ")
	public List<ActivityPerformance> findByOwnerAndObjectiveId(
			Organization workAt, Long objectiveId);
	
}
