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
	
	@Basic
	@Column(name="NAME")
	private String activityName;
	
	@Basic
	@Column(name="AMT")
	private Double amount;

	public Long getOrganizationId() {
		return id.getOrganizationId();
	}

	public Date getDate() {
		return id.getDate();
	}

	public String getPlanCode() {
		return this.id.getPlanCode();
	}

	public String getActivityCode() {
		return this.id.getActivityCode();
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
