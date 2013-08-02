package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="BGT_ASSETBUDGETPLAN")
@SequenceGenerator(name="BGT_ASSETBUDGETPLAN_SEQ", sequenceName="BGT_ASSETBUDGETPLAN_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AssetBudgetPlan implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1279581555306542029L;

	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETBUDGETPLAN_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="BGT_ASSETALLOCATION_ID")
	private AssetAllocation assetAllocation;
	
	
	private Integer budgetOrder;
	
	private Double planAmount;
	private Double actualAmount;
	
	private String remark;
	
	@Temporal(TemporalType.DATE)
	private Date planDate;
	
	@Temporal(TemporalType.DATE)
	private Date actualDate;
	
	@Temporal(TemporalType.DATE)
	private Date planInstallmentDate;
	
	@Temporal(TemporalType.DATE)
	private Date actualInstallmentDate;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AssetAllocation getAssetAllocation() {
		return assetAllocation;
	}
	public void setAssetAllocation(AssetAllocation assetAllocation) {
		this.assetAllocation = assetAllocation;
	}
	public Integer getBudgetOrder() {
		return budgetOrder;
	}
	public void setBudgetOrder(Integer budgetOrder) {
		this.budgetOrder = budgetOrder;
	}
	public Double getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(Double planAmount) {
		this.planAmount = planAmount;
	}
	public Double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(Double actualAmount) {
		this.actualAmount = actualAmount;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public Date getActualDate() {
		return actualDate;
	}
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getPlanInstallmentDate() {
		return planInstallmentDate;
	}
	public void setPlanInstallmentDate(Date planInstallmentDate) {
		this.planInstallmentDate = planInstallmentDate;
	}
	public Date getActualInstallmentDate() {
		return actualInstallmentDate;
	}
	public void setActualInstallmentDate(Date actualInstallmentDate) {
		this.actualInstallmentDate = actualInstallmentDate;
	}
	
	
}
