package biz.thaicom.eBudgeting.models.hrx;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.thaicom.eBudgeting.services.EntityServiceJPA;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="HRX_ORGANIZATION")
@SequenceGenerator(name="HRX_ORGANIZATION_SEQ", sequenceName="HRX_ORGANIZATION_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Organization.class)
public class Organization implements Serializable {
	
	private static final Logger logger = LoggerFactory.getLogger(Organization.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7959577438132453361L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="HRX_ORGANIZATION_SEQ")
	private Long id;
	
	@Basic
	private String name;
	
	@Basic
	private String abbr;
	
	@Basic
	@Column(name="IDX")
	private Integer index;
	
	@Basic
	private String code;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_HRX_ORGANIZATION_ID")
	private Organization parent;
	
	@OneToMany(mappedBy="parent", fetch=FetchType.LAZY)
	@OrderBy("id asc")
	private List<Organization> children;

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

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public List<Organization> getChildren() {
		return children;
	}

	public void setChildren(List<Organization> children) {
		this.children = children;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public OrganizationType getType() {
		return OrganizationType.getType(this);
	}
	
	// ถ้าเป็นระดับแผนก return true;
	public Boolean isSubSection() {
		OrganizationType myType =  this.getType();
		if(myType == OrganizationType.ส่วนในจังหวัด || 
				myType == OrganizationType.แผนก ||
				myType == OrganizationType.แผนกในจังหวัด ||
				myType == OrganizationType.แผนกในอำเภอ)
			return true;
	
//		if(this.code != null) {
//			
//			if(this.code.substring(this.code.length()-2, this.code.length()).equals("00")) {
//				return false;
//			} else {
//				return true;
//			}
//		}
//		
		return false;
	}
	
	public Boolean getIsSubSection() {
		return this.isSubSection();
	}
}
