package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;

@Repository
public interface ActivityRepository extends PagingAndSortingRepository<Activity, Long>,
		JpaSpecificationExecutor<Long> {

	@Query("" +
			"SELECT act " +
			"FROM Activity act " +
			"WHERE owner =?1 AND forObjective.id = ?2 ")
	List<Activity> findAllByOwnerAndForObejctive_Id(Organization org, Long objectiveId);

	
}
