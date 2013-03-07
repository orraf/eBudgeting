package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import biz.thaicom.eBudgeting.models.pln.MonthlyActivityReport;

@Repository
public interface MonthlyActivityReportRepository extends
		PagingAndSortingRepository<MonthlyActivityReport, Long>, JpaSpecificationExecutor<MonthlyActivityReport> {

}
