package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="GLA_ASSETKIND")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=AssetKind.class)
public class AssetKind implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7409434423843639873L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="ASSETKINDCODE")
	private String code;
	
	@Basic
	@Column(name="NAME")
	private String name;
	
	@Basic
	@Column(name="DESCRIPTION")
	private String description;
	
	@ManyToOne
	@JoinColumn(name="TYPEID")
	private AssetType type;
	
	@ManyToOne
	@JoinColumn(name="GROUPID")
	private AssetGroup group;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}

	public AssetGroup getGroup() {
		return group;
	}

	public void setGroup(AssetGroup group) {
		this.group = group;
	}
	
	
}
