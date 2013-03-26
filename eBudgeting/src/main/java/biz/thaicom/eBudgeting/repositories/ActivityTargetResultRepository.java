package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetResult;

public interface ActivityTargetResultRepository extends JpaRepository<ActivityTargetResult, Long> {

	@Query("" +
			"SELECT result " +
			"FROM ActivityTargetResult result " +
			"WHERE result.report = ?1 " +
			"	AND result.timestamp = " +
			"		(SELECT max(result1.timestamp) FROM ActivityTargetResult result1 WHERE  result1.report = ?1) ")
	ActivityTargetResult findByLatestTimeStamp(ActivityTargetReport report);

}
