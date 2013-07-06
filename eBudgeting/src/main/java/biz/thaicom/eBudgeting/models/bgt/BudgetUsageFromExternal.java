package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="V_GL")
public class BudgetUsageFromExternal implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3372651430598472747L;

	@EmbeddedId
	private BudgetUsageId id;
	
	@Embeddable
	public class BudgetUsageId implements Serializable{
		
		
		/**
		 * 
		 */
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

	}
	
	@Basic
	@Column(name="NAME")
	private String activityName;
	
	@Basic
	@Column(name="AMT")
	private Double amount;

	public Long getOrganizationId() {
		return id.organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.id.organizationId = organizationId;
	}

	public Date getDate() {
		return id.date;
	}

	public void setDate(Date date) {
		this.id.date = date;
	}

	public String getPlanCode() {
		return this.id.planCode;
	}

	public void setPlanCode(String planCode) {
		this.id.planCode = planCode;
	}

	public String getActivityCode() {
		return this.id.activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.id.activityCode = activityCode;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	} 
	
	
	
}
