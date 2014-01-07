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

	@Query("" 
			+ "SELECT activity " 
			+ "FROM Activity activity " 
			+"	INNER JOIN activity.forObjective objective " 
			+ "	INNER JOIN objective.parent parentObjective "
			+ " LEFT JOIN FETCH activity.targets target "
			+ " LEFT JOIN FETCH target.unit "
			+ "WHERE " 
			+ "	(activity.owner = ?1 OR activity.regulator = ?1) AND " 
			+ "	objective.id = ?2 "
			+ "ORDER BY activity.regulator.id ASC, activity.code ASC ")
	List<Activity> findAllByOwnerAndForObjective_Id(Organization org, Long objectiveId);
	
	
	@Query(""
			+ "SELECT activity "
			+ "FROM ActivityTargetReport report "
			+ "		INNER JOIN report.target target "
			+ "		INNER JOIN target.activity activity "
			+ "WHERE report.owner = ?1 AND activity.forObjective.id = ?2 ")
	List<Activity> findAllByActivityTargetReportOwner(Organization org, Long objectiveId);

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


	@Query(""
			+ "SELECT DISTINCT act "
			+ "FROM Activity act "
			+ "		LEFT JOIN FETCH act.targets target "
			+ " 	LEFT JOIN FETCH target.unit "
			+ "		JOIN act.regulator regulator "
			+ "		JOIN act.owner owner "
			+ "WHERE ( owner.parent.id = ?2 or regulator.parent.id = ?2 ) "
			+ "		AND act.forObjective = ?1 "
			+ "ORDER BY act.code ASC ")
	List<Activity> findAllByForObjectiveAndOwnerOrRegulator(
			Objective objective, Long orgId);

	
}
