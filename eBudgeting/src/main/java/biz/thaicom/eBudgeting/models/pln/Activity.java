package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
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
import javax.persistence.Transient;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Objective.Comparators;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PLN_ACTIVITY")
@SequenceGenerator(name="PLN_ACTIVITY_SEQ", sequenceName="PLN_ACTIVITY_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=Activity.class)
public class Activity implements Serializable, Comparable<Activity> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 100963496530458293L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_ACTIVITY_SEQ")
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="UNIT_PLN_TARGETUNIT_ID")
	private TargetUnit unit;
	
	@Basic
	private Long targetValue;
	
	@Basic
	private String name;
	
	@Basic
	private String code;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OBJ_PLN_OBJECTIVE_ID", nullable=false)
	private Objective forObjective;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OWNER_HRX_ORGANIZATION")
	private Organization owner;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REGULATOR_HRX_ORGANIZATION")
	private Organization regulator;
	
	@Basic
	private Integer idx;
	
	@Basic
	private Integer activityLevel;
	
	@OneToMany(mappedBy="activity", fetch=FetchType.LAZY)
	private List<ActivityTarget> targets;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PARENT_PLN_ACTIVITY_ID")
	private Activity parent;
	
	@OneToMany(mappedBy="parent")
	@OrderBy("code asc")
	private List<Activity> children;

	@Transient
	private List<ActivityTarget> filterTargets;

	@Transient
	private Boolean showInTree = false;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TargetUnit getUnit() {
		return unit;
	}

	public void setUnit(TargetUnit unit) {
		this.unit = unit;
	}

	public Long getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(Long targetValue) {
		this.targetValue = targetValue;
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

	public Objective getForObjective() {
		return forObjective;
	}

	public void setForObjective(Objective forObjective) {
		this.forObjective = forObjective;
	}

	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public Integer getIdx() {
		return idx;
	}

	public void setIdx(Integer idx) {
		this.idx = idx;
	}

	public List<ActivityTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<ActivityTarget> targets) {
		this.targets = targets;
	}

	public Activity getParent() {
		return parent;
	}

	public void setParent(Activity parent) {
		this.parent = parent;
	}

	public List<Activity> getChildren() {
		return children;
	}

	public void setChildren(List<Activity> children) {
		this.children = children;
	}

	public List<ActivityTarget> getFilterTargets() {
		return filterTargets;
	}

	public void setFilterTargets(List<ActivityTarget> filterTargets) {
		this.filterTargets = filterTargets;
	}

	public Organization getRegulator() {
		return regulator;
	}

	public void setRegulator(Organization regulator) {
		this.regulator = regulator;
	}

	public Integer getActivityLevel() {
		return activityLevel;
	}

	public void setActivityLevel(Integer activityLevel) {
		this.activityLevel = activityLevel;
	}
	
	public void sumChildrenTarget() {
		Map <TargetUnit, ActivityTarget> grandChildTargets = new HashMap<TargetUnit, ActivityTarget>();
		for(Activity child : this.getChildren()) {
			child.getTargets().size();
			
			child.getOwner().getId();
			child.getRegulator().getId();
			
			for(ActivityTarget target : child.getTargets()) {
				if(grandChildTargets.get(target.getUnit()) == null) {
					ActivityTarget newTarget = new ActivityTarget();
					newTarget.setUnit(target.getUnit());
					newTarget.setTargetValue(target.getTargetValue());
					grandChildTargets.put(target.getUnit(), newTarget);
				} else {
					ActivityTarget grandChildTarget = grandChildTargets.get(target.getUnit());
					grandChildTarget.setTargetValue(grandChildTarget.getTargetValue() + target.getTargetValue());
				}
			}
		}
		this.setTargets(new ArrayList<ActivityTarget>(grandChildTargets.values()));
		
	}
	
	public Boolean getShowInTree() {
		return showInTree;
	}

	public void setShowInTree(Boolean showInTree) {
		this.showInTree = showInTree;
		
		Activity act = this.getParent();
		while (act != null) {
			act.setShowInTree(showInTree);
			act = act.getParent();
		}
		
		this.getForObjective().setShowInTree(showInTree);
		
	}
	
	
	// Basic compare
		@Override
		public int compareTo(Activity o) {
			return Comparators.CODE.compare(this, o);
		}
		
		public static class Comparators {
			public static Comparator<Activity> NAME = new Comparator<Activity>() {
				@Override
				public int compare(Activity o1, Activity o2) {
					return o1.name.compareTo(o2.name);
				}
			};
			
			public static Comparator<Activity> CODE = new Comparator<Activity>() {
				@Override
				public int compare(Activity o1, Activity o2) {
					return o1.code.compareTo(o2.code);
				}
			};
			
			public static Comparator<Activity> CODE_DESC = new Comparator<Activity>() {
				@Override
				public int compare(Activity o1, Activity o2) {
					return o2.code.compareTo(o1.code);
				}
			};
		}
}
