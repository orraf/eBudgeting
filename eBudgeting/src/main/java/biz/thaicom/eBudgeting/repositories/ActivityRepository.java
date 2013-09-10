package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.Objective;

@Repository
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long>,
		JpaSpecificationExecutor<Long> {

	@Query("" +
			"SELECT activity " +
			"FROM ActivityPerformance activityPerformance " +
			"	INNER JOIN activityPerformance.activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"	INNER JOIN objective.parent parentObjective " +
			"WHERE " +
			"	(activityPerformance.owner  = ?1 OR activity.owner = ?1 ) AND " +
			"	objective.id = ?2")
	List<Activity> findAllByOwnerAndForObejctive_Id(Organization org, Long objectiveId);

	@Query("" +
			"SELECT act " +
			"FROM Activity act " +
			"	LEFT JOIN FETCH act.targets target " +
			"	LEFT JOIN FETCH target.unit " +
			"WHERE act.regulator =?1 AND act.forObjective.id = ?2" +
			"	AND act.activityLevel = 0 " +
			"ORDER BY act.code ASC ")
	List<Activity> findAllByRegulatorAndForObejctive_Id(Organization workAt,
			Long objectiveId);

	List<Activity> findAllByForObjective(Objective objective);

	
}
