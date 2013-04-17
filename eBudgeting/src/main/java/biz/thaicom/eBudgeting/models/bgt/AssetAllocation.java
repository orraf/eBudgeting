package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AssetAllocation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5388537347402476513L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETALLOCATION_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="ASSETKIND_ID")
	private AssetKind kind;
	
	@Basic
	private Integer fiscalYear;
	
	@Basic
	private Integer quantity;
	
	@Basic
	private Long unitBudget;
	
	@ManyToOne
	private Organization owner;
	
	@ManyToOne
	@JoinColumn(name="PLN_OBJECTIVE_ID")
	private Objective forObjective;
	
	@ManyToOne
	@JoinColumn(name="PLN_ACTIVITY_ID")
	private Activity forActitvity;
	
	@ManyToOne
	@JoinColumn(name="BGT_BUDGETTYPE_ID")
	private BudgetType budgetType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AssetKind getKind() {
		return kind;
	}

	public void setKind(AssetKind kind) {
		this.kind = kind;
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
	
	
}
