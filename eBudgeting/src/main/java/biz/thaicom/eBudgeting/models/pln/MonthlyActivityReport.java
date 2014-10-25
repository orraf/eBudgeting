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
@Table(name="PLN_MONTHLYACTREPORT")
@SequenceGenerator(name="PLN_MONTHLYACTREPORT_SEQ", sequenceName="PLN_MONTHLYACTREPORT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=MonthlyActivityReport.class)
public class MonthlyActivityReport implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8837435221100206314L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_MONTHLYACTREPORT_SEQ")
	private Long id;
	
	/**
	 * กิจกรรม
	 */
	@ManyToOne
	@JoinColumn(name="REPORT_PLN_ACTTARGETREPORT_ID")
	private ActivityTargetReport report;
	
	/**
	 * เป้าหมายของเดือน fiscalMonth
	 */
	@Basic
	private Double activityPlan;
	
	/**
	 * ผลการดำเนินงานของเดือน fiscalMonth
	 */
	@Basic
	private Double activityResult;
	
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

	public ActivityTargetReport getReport() {
		return report;
	}

	public void setReport(ActivityTargetReport report) {
		this.report = report;
	}

	public Double getActivityPlan() {
		return activityPlan;
	}

	public void setActivityPlan(Double activityPlan) {
		this.activityPlan = activityPlan;
	}

	public Double getActivityResult() {
		return activityResult;
	}

	public void setActivityResult(Double activityResult) {
		this.activityResult = activityResult;
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
