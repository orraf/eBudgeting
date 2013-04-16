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
@Table(name="GLA_ASSETTYPE")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AssetType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7214318102674762178L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="ASSETTYPECODE")
	private String code;
	
	@Basic
	@Column(name="NAME")
	private String name;
	
	@Basic
	@Column(name="DESCRIPTION")
	private String description;
		
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

	public AssetGroup getGroup() {
		return group;
	}

	public void setGroup(AssetGroup group) {
		this.group = group;
	}

	
}
