package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.pln.ActivityTarget;

public interface ActivityTargetRepository extends JpaSpecificationExecutor<ActivityTarget>,
		PagingAndSortingRepository<ActivityTarget, Long> {

}
