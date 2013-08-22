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
@Table(name="BGT_ASSETSTEPREPORT")
@SequenceGenerator(name="BGT_ASSETSTEPREPORT_SEQ", sequenceName="BGT_ASSETSTEPREPORT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AssetStepReport implements Serializable {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7300709996184509691L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETSTEPREPORT_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="BGT_ASSETALLOCATION_ID")
	private AssetAllocation assetAllocation;
	
	@ManyToOne
	@JoinColumn(name="BGT_ASSETMETHODSTEP_ID")
	private AssetMethodStep step;
	
	@Temporal(TemporalType.DATE)
	private Date planBegin;

	@Temporal(TemporalType.DATE)
	private Date planEnd;
	
	@Temporal(TemporalType.DATE)
	private Date actualBegin;
	
	@Temporal(TemporalType.DATE)
	private Date actualEnd;

	private Integer stepOrder;
	
	
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

	public AssetMethodStep getStep() {
		return step;
	}

	public void setStep(AssetMethodStep step) {
		this.step = step;
	}

	public Date getPlanBegin() {
		return planBegin;
	}

	public void setPlanBegin(Date planBegin) {
		this.planBegin = planBegin;
	}

	public Date getPlanEnd() {
		return planEnd;
	}

	public void setPlanEnd(Date planEnd) {
		this.planEnd = planEnd;
	}

	public Date getActualBegin() {
		return actualBegin;
	}

	public void setActualBegin(Date actualBegin) {
		this.actualBegin = actualBegin;
	}

	public Date getActualEnd() {
		return actualEnd;
	}

	public void setActualEnd(Date actualEnd) {
		this.actualEnd = actualEnd;
	}

	public Integer getStepOrder() {
		return stepOrder;
	}

	public void setStepOrder(Integer stepOrder) {
		this.stepOrder = stepOrder;
	}
	
}
