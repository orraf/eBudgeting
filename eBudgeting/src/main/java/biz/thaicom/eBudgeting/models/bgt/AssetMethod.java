package biz.thaicom.eBudgeting.models.bgt;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="BGT_ASSETMETHOD")
@SequenceGenerator(name="BGT_ASSETMETHOD_SEQ", sequenceName="BGT_ASSETMETHOD_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AssetMethod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4268259786326782718L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BGT_ASSETMETHOD_SEQ")
	private Long id;
	
	private String name;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@OrderColumn(name="STEPORDER")
	@JoinTable(
        name="BGT_ASSETMETHODSTEP_JOIN",
        joinColumns = @JoinColumn(name="BGT_ASSETMETHOD_ID"),
        inverseJoinColumns = @JoinColumn( name="BGT_ASSETMETHODSTEP_ID")
    )
	private List<AssetMethodStep> steps;

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

	public List<AssetMethodStep> getSteps() {
		return steps;
	}

	public void setSteps(List<AssetMethodStep> steps) {
		this.steps = steps;
	}
	
}
