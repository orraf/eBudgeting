package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="V_ASSET_AMOUNTDATE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=AssetPOExternal.class)
public class AssetPOExternal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3900199502570564570L;

	@Id
	@Column(name="FA_CODE")
	private String faCode;
	
	@Column(name="V_PO_CONTRACT")
	private String poContract;
	
	@Column(name="V_ASSET_AMOUNT")
	private Double assetAmount;

	public String getFaCode() {
		return faCode;
	}

	public void setFaCode(String faCode) {
		this.faCode = faCode;
	}

	public String getPoContract() {
		return poContract;
	}

	public void setPoContract(String poContract) {
		this.poContract = poContract;
	}

	public Double getAssetAmount() {
		return assetAmount;
	}

	public void setAssetAmount(Double assetAmount) {
		this.assetAmount = assetAmount;
	}
	
}
