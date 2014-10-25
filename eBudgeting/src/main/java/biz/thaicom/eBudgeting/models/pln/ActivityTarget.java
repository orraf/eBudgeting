package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PLN_ACTIVITYTARGET")
@SequenceGenerator(name="PLN_ACTIVITYTARGET_SEQ", sequenceName="PLN_ACTIVITYTARGET_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=ActivityTarget.class)
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
	private Double targetValue;
	
	@Basic
	private Double budgetAllocated;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_PLN_TARGETUNIT_ID", nullable=false)
	private TargetUnit unit;

	@Basic
	private Boolean provincialTarget;

	@Transient
	private ActivityTargetReport filterReport;
	
	@OneToMany(mappedBy="target", fetch=FetchType.LAZY)
	private Set<ActivityTargetReport>  reports;
	
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

	public Double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Double targetValue) {
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

	public Double getBudgetAllocated() {
		return budgetAllocated;
	}

	public void setBudgetAllocated(Double budgetAllocated) {
		this.budgetAllocated = budgetAllocated;
	}

	public Boolean getProvincialTarget() {
		return provincialTarget;
	}

	public void setProvincialTarget(Boolean provincialTarget) {
		this.provincialTarget = provincialTarget;
	}

	public Set<ActivityTargetReport> getReports() {
		Set<ActivityTargetReport> level0Reports = new HashSet<ActivityTargetReport>();
		if(reports != null && reports.size() > 0 ) {
			for(ActivityTargetReport report : reports) {
				if(report.getReportLevel() == 1) {
					level0Reports.add(report);
				}
			}
		}
		return level0Reports;
	}
	
	public Double getAllocatedTargetValue() {
		Double level0AllocatedTargetValue = 0.0;
		if(reports != null && reports.size() > 0 ) {
			for(ActivityTargetReport report : reports) {
				if(report.getReportLevel() == 1) {
					level0AllocatedTargetValue += report.getTargetValue();
				}
			}
		}
		return level0AllocatedTargetValue;
	}
	
	@JsonIgnore
	public Set<ActivityTargetReport> getAllReports() {
		return reports;
	}

	public void setReports(Set<ActivityTargetReport> reports) {
		this.reports = reports;
	}
	
}
