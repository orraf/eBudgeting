package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import biz.thaicom.eBudgeting.models.converter.BooleanTFConverter;

@Entity
@Table(name="PLN_TARGETUNIT")
@SequenceGenerator(name="PLN_TARGETUNIT_SEQ", sequenceName="PLN_TARGETUNIT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=TargetUnit.class)
public class TargetUnit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7321492793191996623L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_TARGETUNIT_SEQ")
	private Long id;
	
	@Basic
	private String name;
	
	@Convert(converter=BooleanTFConverter.class)
	@Column(name="ISACTIVED", length=1)
	private Boolean isActived;
	
	@OneToMany(mappedBy="unit", fetch=FetchType.LAZY)
	List<ObjectiveTarget> targets;

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

	public List<ObjectiveTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<ObjectiveTarget> targets) {
		this.targets = targets;
	}

	public Boolean getIsActived() {
		return isActived;
	}

	public void setIsActived(Boolean isActived) {
		this.isActived = isActived;
	}
	
	
	
}
