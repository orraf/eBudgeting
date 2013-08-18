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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="BGT_ASSETBUDGET")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@SequenceGenerator(name="BGT_ASSETBUDGET_SEQ", sequenceName="BGT_ASSETBUDGET_SEQ", allocationSize=1)

public class AssetBudget implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2097324404902862513L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETBUDGET_SEQ")
	private Long id;

	@Basic
	private String name;
	
	@Basic
	private String code;
	
	@Basic
	private String description;
	
	@ManyToOne
	@JoinColumn(name="ASSETKIND_ID")
	private AssetKind kind;

	@ManyToOne
	@JoinColumn(name="ASSETCATEGORY_ID")
	private AssetCategory category;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AssetKind getKind() {
		return kind;
	}

	public void setKind(AssetKind kind) {
		this.kind = kind;
	}

	public AssetCategory getCategory() {
		return category;
	}

	public void setCategory(AssetCategory category) {
		this.category = category;
	}

	
}
