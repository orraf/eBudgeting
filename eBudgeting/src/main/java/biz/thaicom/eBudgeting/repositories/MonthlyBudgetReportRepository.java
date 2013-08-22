package biz.thaicom.eBudgeting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import biz.thaicom.eBudgeting.models.pln.MonthlyBudgetReport;

public interface MonthlyBudgetReportRepository extends JpaRepository<MonthlyBudgetReport, Long> {

}
