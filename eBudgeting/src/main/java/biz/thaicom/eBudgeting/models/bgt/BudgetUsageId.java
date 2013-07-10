package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BudgetUsageId implements Serializable{
	
	private static final long serialVersionUID = -3944413748773930087L;

	@Basic
	@Column(name="ORG_ID")
	private Long organizationId;
	
	@Basic
	@Column(name="GL_TRANS_DOCDATE")
	private Date date;
	
	@Basic
	@Column(name="GL_TRANS_PLAN")
	private String planCode;
	
	@Basic
	@Column(name="ACTIVITYCODE")
	private String activityCode;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	
	
}
