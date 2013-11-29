package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import biz.thaicom.eBudgeting.models.hrx.Organization;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PLN_ACTIVITYTARGETREPORT")
@SequenceGenerator(name="PLN_ACTIVITYTARGETREPORT_SEQ", sequenceName="PLN_ACTIVITYTARGETREPORT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ActivityTargetReport implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3308743343845452601L;
	
	public static ActivityTargetReport createEmptyReport(
			ActivityTarget target) {
		ActivityTargetReport report = new ActivityTargetReport();
		
		report.setTarget(target);
		report.setId(null);
		report.setLatestResult(null);
		report.setOwner(null);
		report.setReportLevel(null);
		report.setTargetValue(0.0);
		
		// now init monthly activity
		List<MonthlyActivityReport> monthlyReports = new ArrayList<MonthlyActivityReport>();
		for(int i=0; i<12; i++) {
			MonthlyActivityReport mar = new MonthlyActivityReport();
			mar.setId(null);
			mar.setActivityPlan(0.0);
			mar.setActivityResult(0.0);
			mar.setFiscalMonth(i);
			mar.setOwner(null);
			mar.setRemark("");
			mar.setReport(report);
			monthlyReports.add(mar);
		}
		report.setMonthlyReports(monthlyReports);
		
		// now init performance
		ActivityPerformance performance = new ActivityPerformance();
		performance.setActivity(target.getActivity());
		performance.setBudgetAllocated(0.0);
		performance.setId(null);
		performance.setOwner(null);
		performance.setTargetReport(report);
		List<MonthlyBudgetReport> monthlyBudgetReports = new ArrayList<MonthlyBudgetReport>();
		for(int i=0; i<12; i++) {
			MonthlyBudgetReport mbr = new MonthlyBudgetReport();
			mbr.setId(null);
			mbr.setBudgetPlan(0.0);
			mbr.setBudgetResult(0.0);
			mbr.setFiscalMonth(i);
			mbr.setOwner(null);
			mbr.setRemark("");
			mbr.setActivityPerformance(performance);;
			monthlyBudgetReports.add(mbr);
		}
		performance.setMonthlyBudgetReports(monthlyBudgetReports);
		report.setActivityPerformance(performance);
		
		return report;
	}

	public static ActivityTargetReport createSumReport(
			ActivityTarget target, 
			List<MonthlyBudgetReport> monthlyBudgetReports,
			List<MonthlyActivityReport> monthlyActivityReports) {
		ActivityTargetReport report = ActivityTargetReport.createEmptyReport(target);
		
		for(MonthlyActivityReport mar : monthlyActivityReports) {
			if(mar.getFiscalMonth()!=null && mar.getFiscalMonth() >= 0 && mar.getFiscalMonth() <= 11) {
				MonthlyActivityReport ownReport =report.getFiscalReportOn(mar.getFiscalMonth()); 
				if(mar.getActivityPlan() != null) {
					ownReport.setActivityPlan(ownReport.getActivityPlan() 
							+ mar.getActivityPlan());
				}
				if(mar.getActivityResult() != null) {
					ownReport.setActivityResult(ownReport.getActivityResult()
							+ mar.getActivityResult()); 
				}
			}
		}
		
		for(MonthlyBudgetReport mbr : monthlyBudgetReports) {
			if(mbr.getFiscalMonth()!=null && mbr.getFiscalMonth() >= 0 && mbr.getFiscalMonth() <= 11) {
				MonthlyBudgetReport ownReport = report.getFiscalBudgetReportOn(mbr.getFiscalMonth());
				if(mbr.getBudgetPlan() != null) {
					ownReport.setBudgetPlan(ownReport.getBudgetPlan() 
							+ mbr.getBudgetPlan());
				}
				if(mbr.getBudgetResult() != null) {
					ownReport.setBudgetResult(ownReport.getBudgetResult()
							+ mbr.getBudgetResult());
				}
			}
		}
		
		return report;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_ACTIVITYTARGETREPORT_SEQ")
	private Long id;
	
	/**
	 * รายงาน performance
	 */
	@OneToOne
	@JoinColumn(name="PERFORMANCE_PLN_ACTPER_ID")
	private ActivityPerformance activityPerformance; 
	
	/**
	 * เป้าหมายกิจกรรมที่อ้างถึง
	 */
	@ManyToOne
	@JoinColumn(name="TARGET_PLN_ACTTARGET_ID")
	private ActivityTarget target;
	
	
	/**
	 * แผน/ผลของกิจกรรมรายเดือน
	 */
	@OneToMany(mappedBy="report")
	@OrderBy("fiscalMonth asc")
	private List<MonthlyActivityReport>  monthlyReports;
	
	/**
	 * เป้าหมายในภาพรวม
	 */
	@Basic
	private Double targetValue;

	/**
	 * ผู้รับผิดชอบเป้าหมาย
	 */
	@ManyToOne
	@JoinColumn(name="OWNER_HRX_ORGANIZATION_ID")
	private Organization owner;

	/**
	 * ระดับของรายงาน 0 = จังหวัด/ส่วน, 1 = อำเภอ
	 */
	@Basic
	private Integer reportLevel;
	
	@Transient
	private ActivityTargetResult latestResult;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityPerformance getActivityPerformance() {
		return activityPerformance;
	}

	public void setActivityPerformance(ActivityPerformance activityPerformance) {
		this.activityPerformance = activityPerformance;
	}

	public ActivityTarget getTarget() {
		return target;
	}

	public void setTarget(ActivityTarget target) {
		this.target = target;
	}

	public List<MonthlyActivityReport> getMonthlyReports() {
		return monthlyReports;
	}

	public void setMonthlyReports(List<MonthlyActivityReport> monthlyReports) {
		this.monthlyReports = monthlyReports;
	}

	public Double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Double targetValue) {
		this.targetValue = targetValue;
	}

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public ActivityTargetResult getLatestResult() {
		return latestResult;
	}

	public void setLatestResult(ActivityTargetResult latestResult) {
		this.latestResult = latestResult;
	}

	public Integer getReportLevel() {
		return reportLevel;
	}

	public void setReportLevel(Integer reportLevel) {
		this.reportLevel = reportLevel;
	}

	public MonthlyActivityReport getFiscalReportOn(Integer fiscalMonth) {
		if(this.monthlyReports == null || this.monthlyReports.size() != 12) { 
			return null;
		}
		
		for(int i=0; i<12; i++) {
			if(this.monthlyReports.get(i).getFiscalMonth().equals(fiscalMonth)) {
				return this.monthlyReports.get(i);
			}
		}
		
		return null;
	}
	
	public MonthlyBudgetReport getFiscalBudgetReportOn(Integer fiscalMonth) {
		if(this.activityPerformance == null) {
			return null;
		}
		 return this.activityPerformance.getFiscalReportOn(fiscalMonth);
	}
	
	public void sumReports(
			List<MonthlyBudgetReport> monthlyBudgetReports,
			List<MonthlyActivityReport> monthlyActivityReports) {
	
		for(MonthlyActivityReport mar : monthlyActivityReports) {
			if(mar.getFiscalMonth()!=null && mar.getFiscalMonth() >= 0 && mar.getFiscalMonth() <= 11) {
				MonthlyActivityReport ownReport =this.getFiscalReportOn(mar.getFiscalMonth()); 
				if(mar.getActivityPlan() != null) {
					ownReport.setActivityPlan(ownReport.getActivityPlan() 
							+ mar.getActivityPlan());
				}
				if(mar.getActivityResult() != null) {
					ownReport.setActivityResult(ownReport.getActivityResult()
							+ mar.getActivityResult()); 
				}
			}
		}
		
		for(MonthlyBudgetReport mbr : monthlyBudgetReports) {
			if(mbr.getFiscalMonth()!=null && mbr.getFiscalMonth() >= 0 && mbr.getFiscalMonth() <= 11) {
				MonthlyBudgetReport ownReport = this.getFiscalBudgetReportOn(mbr.getFiscalMonth());
				if(mbr.getBudgetPlan() != null) {
					ownReport.setBudgetPlan(ownReport.getBudgetPlan() 
							+ mbr.getBudgetPlan());
				}
				if(mbr.getBudgetResult() != null) {
					ownReport.setBudgetResult(ownReport.getBudgetResult()
							+ mbr.getBudgetResult());
				}
			}
		}
	}
	

}
