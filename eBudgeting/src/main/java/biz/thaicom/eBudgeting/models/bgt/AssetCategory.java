package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="BGT_ASSETCATEGORY")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@SequenceGenerator(name="BGT_ASSETCATEGORY_SEQ", sequenceName="BGT_ASSETCATEGORY_SEQ", allocationSize=1)
public class AssetCategory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8449880664701142017L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETCATEGORY_SEQ")
	private Long id;
	
	private String name;
	
	private String code;

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
	
	

}
