package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityPerformance;

@Repository
public interface ActivityPerformanceRepository extends
		PagingAndSortingRepository<ActivityPerformance, Long>, JpaSpecificationExecutor<ActivityPerformance> {

	public ActivityPerformance findOneByActivityAndOwner(Activity activity, Organization owner);
	
}
