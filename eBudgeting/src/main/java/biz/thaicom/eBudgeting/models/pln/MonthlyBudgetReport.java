package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import biz.thaicom.eBudgeting.models.hrx.Organization;

@Entity
@Table(name="PLN_MONTHLYBGTREPORT")
@SequenceGenerator(name="PLN_MONTHLYBGTREPORT_SEQ", sequenceName="PLN_MONTHLYBGTREPORT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class MonthlyBudgetReport implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -79483315443070231L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_MONTHLYBGTREPORT_SEQ")
	private Long id;
	
	/**
	 * กิจกรรม
	 */
	@ManyToOne
	@JoinColumn(name="PERFORMANCE_PLN_ACTPER_ID")
	private ActivityPerformance activityPerformance;
	
	/**
	 * เป้าหมายของเดือน fiscalMonth
	 */
	@Basic
	private Long budgetPlan;
	
	/**
	 * ผลการดำเนินงานของเดือน fiscalMonth
	 */
	@Basic
	private Long budgetResult;
	
	/**
	 * มีค่าตั้งแต่ 0 - 11 (0 = เดือนตุลาคม, 11 = เดือนกันยายน)
	 */
	@Basic
	private Integer fiscalMonth;
	
	/**
	 * หน่วยงานผู้รับผิดชอบ
	 */
	@ManyToOne
	@JoinColumn(name="OWNER_HRX_ORGANIZATION_ID")
	private Organization owner;
	
	/**
	 * หมายเหตุประกอบรายงาน
	 */
	@Basic
	@Column(length=1024)
	private String remark;

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

	public Long getBudgetPlan() {
		return budgetPlan;
	}

	public void setBudgetPlan(Long budgetPlan) {
		this.budgetPlan = budgetPlan;
	}

	public Long getBudgetResult() {
		return budgetResult;
	}

	public void setBudgetResult(Long budgetResult) {
		this.budgetResult = budgetResult;
	}

	public Integer getFiscalMonth() {
		return fiscalMonth;
	}

	public void setFiscalMonth(Integer fiscalMonth) {
		this.fiscalMonth = fiscalMonth;
	}

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
