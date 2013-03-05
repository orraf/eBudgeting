package biz.thaicom.eBudgeting.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;

@Repository
public interface ActivityTargetReportRepository extends
		PagingAndSortingRepository<ActivityTargetReport, Long>, JpaSpecificationExecutor<ActivityTargetReport> {

		
	List<ActivityTargetReport> findAllByTarget_id(Long targetId);

}
