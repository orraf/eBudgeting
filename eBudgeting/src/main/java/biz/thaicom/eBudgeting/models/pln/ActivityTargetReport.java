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

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_ACTIVITYTARGETREPORT_SEQ")
	private Long id;
	
	/**
	 * รายงาน performance
	 */
	@ManyToOne(fetch=FetchType.LAZY)
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
	private List<MonthlyActivityReport>  monthlyReports;
	
	/**
	 * เป้าหมายในภาพรวม
	 */
	@Basic
	private Long targetValue;

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
	private Integer level;
	
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

	public Long getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Long targetValue) {
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
	
	

}
