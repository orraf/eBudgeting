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
@Table(name="BGT_ASSETMETHODSTEP")
@SequenceGenerator(name="BGT_ASSETMETHODSTEP_SEQ", sequenceName="BGT_ASSETMETHODSTEP_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AssetMethodStep implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5466345046895004399L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETMETHODSTEP_SEQ")
	private Long id;
	
	private String name;

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
	
	
	
	
}
