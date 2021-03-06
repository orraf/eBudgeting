package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import biz.thaicom.eBudgeting.models.hrx.Organization;

@Entity
@Table(name="PLN_ACTIVITYPERFORMANCE")
@SequenceGenerator(name="PLN_ACTIVITYPERFORMANCE_SEQ", sequenceName="PLN_ACTIVITYPERFORMANCE_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=ActivityPerformance.class)
public class ActivityPerformance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4357047336355187478L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_ACTIVITYPERFORMANCE_SEQ")
	private Long id;
	
	/**
	 * กิจกรรม
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ACTIVITY_PLN_ACTIVITY_ID")
	private Activity activity;
	
	/**
	 * แต่ละกิจกรรมมีหลายเป้าหมาย targetReports เก็บค่าแผน/ผลของแต่ละเป้าหมาย
	 */
	@OneToOne(mappedBy="activityPerformance")
	private ActivityTargetReport targetReport;
	
	/**
	 * เก็บข้อมูลแผน/ผลการใช้จ่ายงบประมาณ
	 */
	@OneToMany(mappedBy="activityPerformance")
	@OrderBy("fiscalMonth asc")
	private List<MonthlyBudgetReport> monthlyBudgetReports;
	
	/**
	 * งบประมาณที่ได้รับการจัดสรร
	 */
	@Basic
	private Double budgetAllocated;
	
	/**
	 * ผู้รับผิดชอบกิจกรรม
	 */
	@ManyToOne
	@JoinColumn(name="OWNER_HRX_ORGANIZATION_ID")
	private Organization owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ActivityTargetReport getTargetReport() {
		return targetReport;
	}

	public void setTargetReport(ActivityTargetReport targetReport) {
		this.targetReport = targetReport;
	}

	public List<MonthlyBudgetReport> getMonthlyBudgetReports() {
		return monthlyBudgetReports;
	}

	public void setMonthlyBudgetReports(
			List<MonthlyBudgetReport> monthlyBudgetReports) {
		this.monthlyBudgetReports = monthlyBudgetReports;
	}

	public Double getBudgetAllocated() {
		return budgetAllocated;
	}

	public void setBudgetAllocated(Double budgetAllocated) {
		this.budgetAllocated = budgetAllocated;
	}

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public MonthlyBudgetReport getFiscalReportOn(Integer budgetFiscalMonth) {
		if(this.monthlyBudgetReports == null || this.monthlyBudgetReports.size() != 12) { 
			return null;
		}
		
		for(int i=0; i<12; i++) {
			if(this.monthlyBudgetReports.get(i).getFiscalMonth().equals(budgetFiscalMonth)) {
				return this.monthlyBudgetReports.get(i);
			}
		}
		
		return null;
	}
	
	
	
	
}
