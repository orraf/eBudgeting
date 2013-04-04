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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import biz.thaicom.eBudgeting.models.hrx.Organization;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PLN_ACTIVITYTARGET")
@SequenceGenerator(name="PLN_ACTIVITYTARGET_SEQ", sequenceName="PLN_ACTIVITYTARGET_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ActivityTarget implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6063504979075831731L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_ACTIVITYTARGET_SEQ")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ACTIVITY_PLN_ACTIVITY_ID", nullable=false)
	private Activity activity;
	
	@Basic
	private Long targetValue;
	
	@Basic
	private Long budgetAllocated;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_PLN_TARGETUNIT_ID", nullable=false)
	private TargetUnit unit;

	@Transient
	private ActivityTargetReport filterReport;
	
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

	public Long getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Long targetValue) {
		this.targetValue = targetValue;
	}

	public TargetUnit getUnit() {
		return unit;
	}

	public void setUnit(TargetUnit unit) {
		this.unit = unit;
	}

	public ActivityTargetReport getFilterReport() {
		return filterReport;
	}

	public void setFilterReport(ActivityTargetReport filterReport) {
		this.filterReport = filterReport;
	}

	public Long getBudgetAllocated() {
		return budgetAllocated;
	}

	public void setBudgetAllocated(Long budgetAllocated) {
		this.budgetAllocated = budgetAllocated;
	}
	
	
	
}
