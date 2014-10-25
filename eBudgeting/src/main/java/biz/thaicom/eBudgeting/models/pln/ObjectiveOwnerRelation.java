package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import biz.thaicom.eBudgeting.models.hrx.Organization;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PLN_OBJECTIVEOWNERRELATION")
@SequenceGenerator(name="PLN_OBJOWNERRELATION_SEQ", sequenceName="PLN_OBJOWNERRELATION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope = ObjectiveOwnerRelation.class)
public class ObjectiveOwnerRelation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1403738148508363790L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_OBJOWNERRELATION_SEQ")
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OBJ_PLN_OBJECTIVE_ID", nullable=false)
	private Objective objective;
	
	@OneToMany
	@JoinTable(name="PLN_OBJECTIVE_OWNER_JOIN")
	private List<Organization> owners;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public List<Organization> getOwners() {
		return owners;
	}

	public void setOwners(List<Organization> owners) {
		this.owners = owners;
	}

}
