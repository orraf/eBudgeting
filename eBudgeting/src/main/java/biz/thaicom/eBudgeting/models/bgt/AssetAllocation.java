package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.Objective;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="BGT_ASSETALLOCATION")
@SequenceGenerator(name="BGT_ASSETALLOCATION_SEQ", sequenceName="BGT_ASSETALLOCATION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=AssetAllocation.class)
public class AssetAllocation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5388537347402476513L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETALLOCATION_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="BGT_ASSETBUDGET_ID")
	private AssetBudget assetBudget;
	
	@Basic
	private Integer fiscalYear;
	
	@Basic
	private Integer quantity;
	
	@Basic
	private Long unitBudget;
	
	@Basic
	private String contractNo;
	
	@Basic
	private Double contractBudgetSigned;
	
	
	/**
	 * วันที่เริ่มสัญญา (แผน)
	 */
	@Basic 
	private Double contractedBudgetPlan;

	/**
	 * วันที่สิ้นสุดสัญญา (ผล)
	 */
	@Basic 
	private Double contractedBudgetActual;
	
	/**
	 * ราคากลาง
	 */
	@Basic
	private Double estimatedCost;
	
	@ManyToOne
	@JoinColumn(name="HRX_OWNER_ID")
	private Organization owner;

	@ManyToOne
	@JoinColumn(name="HRX_PARENTOWNER_ID")
	private Organization parentOwner;

	@ManyToOne
	@JoinColumn(name="HRX_OPERATOR_ID")
	private Organization operator;
	
	@ManyToOne
	@JoinColumn(name="PLN_OBJECTIVE_ID")
	private Objective forObjective;
	
	@ManyToOne
	@JoinColumn(name="BGT_PROPOSAL_ID")
	private BudgetProposal proposal;
	
	@ManyToOne
	@JoinColumn(name="PLN_ACTIVITY_ID")
	private Activity forActitvity;
	
	@ManyToOne
	@JoinColumn(name="BGT_BUDGETTYPE_ID")
	private BudgetType budgetType;
	
	@ManyToOne
	@JoinColumn(name="BGT_ASSETMETHOD_ID")
	private AssetMethod assetMethod;

	@ManyToOne
	@JoinColumn(name="BGT_ASSETCATEGORY_ID")
	private AssetCategory category;
	
	@OneToMany(mappedBy="assetAllocation")
	@OrderColumn(name="STEPORDER")
	private List<AssetStepReport> assetStepReports;

	@OneToMany(mappedBy="assetAllocation", cascade = CascadeType.ALL,  orphanRemoval=true)
	@OrderColumn(name="BUDGETORDER")
	private List<AssetBudgetPlan> assetBudgetPlans;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AssetBudget getAssetBudget() {
		return assetBudget;
	}

	public void setAssetBudget(AssetBudget assetBudget) {
		this.assetBudget = assetBudget;
	}

	public Integer getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(Integer fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getUnitBudget() {
		return unitBudget;
	}

	public void setUnitBudget(Long unitBudget) {
		this.unitBudget = unitBudget;
	}

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public Objective getForObjective() {
		return forObjective;
	}

	public void setForObjective(Objective forObjective) {
		this.forObjective = forObjective;
	}

	public Activity getForActitvity() {
		return forActitvity;
	}

	public void setForActitvity(Activity forActitvity) {
		this.forActitvity = forActitvity;
	}

	public BudgetType getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(BudgetType budgetType) {
		this.budgetType = budgetType;
	}

	public Organization getParentOwner() {
		return parentOwner;
	}

	public void setParentOwner(Organization parentOwner) {
		this.parentOwner = parentOwner;
	}

	public BudgetProposal getProposal() {
		return proposal;
	}

	public void setProposal(BudgetProposal proposal) {
		this.proposal = proposal;
	}

	public AssetMethod getAssetMethod() {
		return assetMethod;
	}

	public void setAssetMethod(AssetMethod assetMethod) {
		this.assetMethod = assetMethod;
	}

	public List<AssetStepReport> getAssetStepReports() {
		return assetStepReports;
	}

	public void setAssetStepReports(List<AssetStepReport> assetStepReports) {
		this.assetStepReports = assetStepReports;
	}

	public Organization getOperator() {
		return operator;
	}

	public void setOperator(Organization operator) {
		this.operator = operator;
	}

	public List<AssetBudgetPlan> getAssetBudgetPlans() {
		return assetBudgetPlans;
	}

	public void setAssetBudgetPlans(List<AssetBudgetPlan> assetBudgetPlans) {
		this.assetBudgetPlans = assetBudgetPlans;
	}

	public Double getContractedBudgetPlan() {
		return contractedBudgetPlan;
	}

	public void setContractedBudgetPlan(Double contractedBudgetPlan) {
		this.contractedBudgetPlan = contractedBudgetPlan;
	}

	public Double getContractedBudgetActual() {
		return contractedBudgetActual;
	}

	public void setContractedBudgetActual(Double contractedBudgetActual) {
		this.contractedBudgetActual = contractedBudgetActual;
	}

	public Double getEstimatedCost() {
		return estimatedCost;
	}

	public void setEstimatedCost(Double estimatedCost) {
		this.estimatedCost = estimatedCost;
	}

	public AssetCategory getCategory() {
		return category;
	}

	public void setCategory(AssetCategory category) {
		this.category = category;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public Double getContractBudgetSigned() {
		return contractBudgetSigned;
	}

	public void setContractBudgetSigned(Double contractBudgetSigned) {
		this.contractBudgetSigned = contractBudgetSigned;
	}
	
	
	
	
}
