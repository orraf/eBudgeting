package biz.thaicom.eBudgeting.services;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.swing.SortOrder;

import net.bull.javamelody.MonitoredWithSpring;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import biz.thaicom.eBudgeting.controllers.WordReportsController;
import biz.thaicom.eBudgeting.models.bgt.AllocationRecord;
import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.AssetBudget;
import biz.thaicom.eBudgeting.models.bgt.AssetBudgetPlan;
import biz.thaicom.eBudgeting.models.bgt.AssetCategory;
import biz.thaicom.eBudgeting.models.bgt.AssetGroup;
import biz.thaicom.eBudgeting.models.bgt.AssetKind;
import biz.thaicom.eBudgeting.models.bgt.AssetMethod;
import biz.thaicom.eBudgeting.models.bgt.AssetStepReport;
import biz.thaicom.eBudgeting.models.bgt.AssetType;
import biz.thaicom.eBudgeting.models.bgt.BudgetCommonType;
import biz.thaicom.eBudgeting.models.bgt.BudgetLevel;
import biz.thaicom.eBudgeting.models.bgt.BudgetProposal;
import biz.thaicom.eBudgeting.models.bgt.BudgetSignOff;
import biz.thaicom.eBudgeting.models.bgt.BudgetType;
import biz.thaicom.eBudgeting.models.bgt.FiscalBudgetType;
import biz.thaicom.eBudgeting.models.bgt.FormulaColumn;
import biz.thaicom.eBudgeting.models.bgt.FormulaStrategy;
import biz.thaicom.eBudgeting.models.bgt.ObjectiveBudgetProposal;
import biz.thaicom.eBudgeting.models.bgt.ObjectiveBudgetProposalTarget;
import biz.thaicom.eBudgeting.models.bgt.ProposalStrategy;
import biz.thaicom.eBudgeting.models.bgt.RequestColumn;
import biz.thaicom.eBudgeting.models.bgt.ReservedBudget;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityPerformance;
import biz.thaicom.eBudgeting.models.pln.ActivityTarget;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetResult;
import biz.thaicom.eBudgeting.models.pln.MonthlyActivityReport;
import biz.thaicom.eBudgeting.models.pln.MonthlyBudgetReport;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.ObjectiveDetail;
import biz.thaicom.eBudgeting.models.pln.ObjectiveName;
import biz.thaicom.eBudgeting.models.pln.ObjectiveOwnerRelation;
import biz.thaicom.eBudgeting.models.pln.ObjectiveRelations;
import biz.thaicom.eBudgeting.models.pln.ObjectiveTarget;
import biz.thaicom.eBudgeting.models.pln.ObjectiveType;
import biz.thaicom.eBudgeting.models.pln.ObjectiveTypeId;
import biz.thaicom.eBudgeting.models.pln.TargetUnit;
import biz.thaicom.eBudgeting.models.pln.TargetValue;
import biz.thaicom.eBudgeting.models.pln.TargetValueAllocationRecord;
import biz.thaicom.eBudgeting.models.webui.Breadcrumb;
import biz.thaicom.eBudgeting.repositories.ActivityPerformanceRepository;
import biz.thaicom.eBudgeting.repositories.ActivityRepository;
import biz.thaicom.eBudgeting.repositories.ActivityTargetReportRepository;
import biz.thaicom.eBudgeting.repositories.ActivityTargetRepository;
import biz.thaicom.eBudgeting.repositories.ActivityTargetResultRepository;
import biz.thaicom.eBudgeting.repositories.AllocationRecordRepository;
import biz.thaicom.eBudgeting.repositories.AssetAllocationRepository;
import biz.thaicom.eBudgeting.repositories.AssetBudgetRepository;
import biz.thaicom.eBudgeting.repositories.AssetCategoryRepository;
import biz.thaicom.eBudgeting.repositories.AssetGroupRepository;
import biz.thaicom.eBudgeting.repositories.AssetKindRepository;
import biz.thaicom.eBudgeting.repositories.AssetMethodRepository;
import biz.thaicom.eBudgeting.repositories.AssetStepReportRepository;
import biz.thaicom.eBudgeting.repositories.AssetTypeRepository;
import biz.thaicom.eBudgeting.repositories.BudgetCommonTypeRepository;
import biz.thaicom.eBudgeting.repositories.BudgetProposalRepository;
import biz.thaicom.eBudgeting.repositories.BudgetSignOffRepository;
import biz.thaicom.eBudgeting.repositories.BudgetTypeRepository;
import biz.thaicom.eBudgeting.repositories.BudgetUsageFromExternalRepository;
import biz.thaicom.eBudgeting.repositories.FiscalBudgetTypeRepository;
import biz.thaicom.eBudgeting.repositories.FormulaColumnRepository;
import biz.thaicom.eBudgeting.repositories.FormulaStrategyRepository;
import biz.thaicom.eBudgeting.repositories.MonthlyActivityReportRepository;
import biz.thaicom.eBudgeting.repositories.MonthlyBudgetReportRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveBudgetProposalRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveDetailRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveNameRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveOwnerRelationRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveRelationsRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveTargetRepository;
import biz.thaicom.eBudgeting.repositories.ObjectiveTypeRepository;
import biz.thaicom.eBudgeting.repositories.OrganizationRepository;
import biz.thaicom.eBudgeting.repositories.ProposalStrategyRepository;
import biz.thaicom.eBudgeting.repositories.RequestColumnRepositories;
import biz.thaicom.eBudgeting.repositories.ReservedBudgetRepository;
import biz.thaicom.eBudgeting.repositories.TargetUnitRepository;
import biz.thaicom.eBudgeting.repositories.TargetValueAllocationRecordRepository;
import biz.thaicom.eBudgeting.repositories.TargetValueRepository;
import biz.thaicom.security.models.ThaicomUserDetail;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
@Transactional
@MonitoredWithSpring
public class EntityServiceJPA implements EntityService {
	private static final Logger logger = LoggerFactory.getLogger(EntityServiceJPA.class);
	
	@Autowired
	private ObjectiveRepository objectiveRepository;
	
	@Autowired
	private ObjectiveTypeRepository objectiveTypeRepository;
	
	@Autowired
	private BudgetTypeRepository budgetTypeRepository;
	
	@Autowired
	private FormulaStrategyRepository formulaStrategyRepository;
	
	@Autowired
	private FormulaColumnRepository formulaColumnRepository;
	
	@Autowired
	private BudgetProposalRepository budgetProposalRepository;
	
	@Autowired
	private RequestColumnRepositories requestColumnRepositories;
	
	@Autowired 
	private ProposalStrategyRepository proposalStrategyRepository;
	
	@Autowired 
	private AllocationRecordRepository allocationRecordRepository;
	
	@Autowired
	private ReservedBudgetRepository reservedBudgetRepository;
	
	@Autowired
	private ObjectiveTargetRepository objectiveTargetRepository;
	
	@Autowired
	private TargetValueRepository targetValueRepository;
	
	@Autowired
	private TargetUnitRepository targetUnitRepository;
	
	@Autowired
	private TargetValueAllocationRecordRepository targetValueAllocationRecordRepository;
	
	@Autowired
	private ObjectiveRelationsRepository objectiveRelationsRepository;
	
	@Autowired
	private BudgetCommonTypeRepository budgetCommonTypeRepository;
	
	@Autowired
	private ObjectiveBudgetProposalRepository objectiveBudgetProposalRepository; 
	
	@Autowired
	private FiscalBudgetTypeRepository fiscalBudgetTypeRepository;
	
	@Autowired
	private ObjectiveNameRepository objectiveNameRepository;
	
	@Autowired
	private BudgetSignOffRepository budgetSignOffRepository;
	
	@Autowired
	private ObjectiveDetailRepository objectiveDetailRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private ObjectiveOwnerRelationRepository objectiveOwnerRelationRepository;
	
	@Autowired
	private ActivityTargetRepository activityTargetRepository;
	
	@Autowired
	private ActivityPerformanceRepository activityPerformanceRepository;
	
	@Autowired
	private ActivityRepository activityRepository;
	
	@Autowired
	private ActivityTargetReportRepository activityTargetReportRepository;
	
	@Autowired
	private ActivityTargetResultRepository activityTargetResultRepository;
	
	
	@Autowired
	private MonthlyActivityReportRepository monthlyActivityReportRepository;
	
	@Autowired
	private MonthlyBudgetReportRepository monthlyBudgetReportRepository;
	
	@Autowired
	private AssetGroupRepository assetGroupRepository;
	
	@Autowired
	private AssetTypeRepository assetTypeRepository;
	
	@Autowired
	private AssetKindRepository assetKindRepository;
	
	@Autowired
	private AssetBudgetRepository assetBudgetRepository;
	
	@Autowired
	private AssetAllocationRepository assetAllocationRepository;
	
	@Autowired
	private AssetMethodRepository assetMethodRepository;

	@Autowired
	private AssetStepReportRepository assetStepReportRepository;
	
	@Autowired
	private BudgetUsageFromExternalRepository budgetUsageFromExternalRepository;
	
	@Autowired
	private AssetCategoryRepository assetCategoryRepository;
	
	@Autowired
	private ObjectMapper mapper;
	


	@Override
	public ObjectiveType findObjectiveTypeById(Long id) {
		ObjectiveType type = objectiveTypeRepository.findOne(id);
		if(type.getParent() !=null) {
			type.getParent().getName();
		}
		type.getChildren().size();
		return type;
	}

	@Override
	public Set<ObjectiveType> findChildrenObjectiveType(ObjectiveType type) {
		ObjectiveType self = objectiveTypeRepository.findOne(type.getId());
		return self.getChildren();
	}

	@Override
	public ObjectiveType findParentObjectiveType(ObjectiveType type) {
		ObjectiveType self = objectiveTypeRepository.findOne(type.getId());
		return self.getParent();
	}

	@Override
	public List<Objective> findObjectivesOf(ObjectiveType type) {
		return objectiveRepository.findByTypeId(type.getId());
	}

	@Override
	public List<Objective> findObjectiveChildren(Objective objective) {
		return findObjectiveChildrenByObjectiveId(objective.getId());
	}

	@Override
	public List<Objective> findAllObjectiveChildren(Integer fiscalYear, Long id) {
		List<Objective> obj = objectiveRepository.findAllByFiscalYearAndType_id(fiscalYear, id);
		
		for (Objective o : obj) {
			deepInitObjective(o);
		}
		
		return obj;
	}

	private void deepInitObjective(Objective obj) {
		if(obj == null || obj.getChildren() == null || obj.getChildren().size() == 0) {
			return;
		} else {
			obj.getChildren().size();
			for(Objective o : obj.getChildren()) {
				deepInitObjective(o);
			}
		}
	}

	@Override
	public Objective findParentObjective(Objective objective) {
		Objective self = objectiveRepository.findOne(objective.getId());
		
		return self.getParent();
	}

	@Override
	public Objective findOjectiveById(Long id) {
		Objective objective = objectiveRepository.findOne(id);
//		if(objective != null) {
//			objective.doBasicLazyLoad();
//		}
		
		
		return objective;
	}

	@Override
	public List<Objective> findObjectiveChildrenByObjectiveId(Long id) {
//		Objective self = objectiveRepository.findOne(id);
//		if(self != null) {
//			logger.debug("--id: " + self.getId());
//			logger.debug("children.getSize() = " + self.getChildren().size());
//			self.getChildren().size();
//			for(Objective objective: self.getChildren()) {
//				if(objective != null) {
//					logger.debug(" child.id --> " + objective.getId());
//					objective.doBasicLazyLoad();
//				}
//			}
//		}
//		return self.getChildren();
		
		List<Objective> objs =  objectiveRepository.findChildrenWithParentAndTypeAndBudgetType(id);
		
		for(Objective obj : objs){
			obj.getTargets().size();
		}
		
		return objs;
	}
	
	@Override
	public List<Objective> findObjectiveAllByFiscalYear(Integer fiscalYear) {
		List<Objective> allObjectives = objectiveRepository
				.findAllLeftJoinChildrenByFiscalYear(fiscalYear);
		
		List<Objective> rootObjectives = new ArrayList<Objective> ();
		
		for(Objective obj:  allObjectives) {
			if(obj.getParent() == null) {
				rootObjectives.add(obj);
			}
		}
			
		return rootObjectives;
	}

	@Override
	public List<Objective> findRootObjectiveByFiscalyear(Integer fiscalYear, Boolean eagerLoad) {
		
		List<Objective> list = objectiveRepository.findByParentIdAndFiscalYearAndParent_Name(null, fiscalYear, "ROOT");
		if(eagerLoad == true) {
			for(Objective objective: list) {
				objective.doEagerLoad();
			}
		} else {
			for(Objective objective : list) {
				objective.doBasicLazyLoad();
			}
		}
		return list;
	}
	
	@Override
	public Objective findOneRootObjectiveByFiscalyear(Integer fiscalYear) {
		return objectiveRepository.findRootOfFiscalYear(fiscalYear);
	}

	@Override
	public List<Objective> findAvailableObjectiveChildrenByObjectiveId(Long id) {
		Objective o = objectiveRepository.findOne(id);
		Set<ObjectiveType> childrenSet = o.getType().getChildren();
		
		logger.debug("++++++++++++++++++++++++++++" + o.getType().getId());
		
		
		if(childrenSet != null && childrenSet.size() >0 ) {
			for(ObjectiveType ot : childrenSet) {
				logger.debug(ot.getName());
			}
			
			return objectiveRepository.findAvailableChildrenOfObjectiveType(childrenSet);
		} else {
			return null;
		}
		
	}

	@Override
	public List<Objective> findRootFiscalYear() {
		return objectiveRepository.findRootFiscalYear();
	}

	@Override
	public List<Integer> findObjectiveTypeRootFiscalYear() {
		return objectiveTypeRepository.findRootFiscalYear();
	}
	
	

	@Override
	public List<Objective> findObjectiveHasBudgetAssetByFiscalYear(
			Integer fiscalYear, Organization organization) {
		return objectiveRepository.findAllHasBudgetAssetByFiscalYear(fiscalYear, organization);
	}

	@Override
	public List<ObjectiveType> findObjectiveTypeByFiscalYearEager(
			Integer fiscalYear, Long parentId) {
		List<ObjectiveType>  list = objectiveTypeRepository.findByFiscalYearAndParentId(fiscalYear, parentId);
		
		// now we'll have to just fill 'em up
		for(ObjectiveType type : list) {
			deepInitObjectiveType(type);
		}
		
		return list;
		
	}

	@Override
	public ObjectiveType findDeepObjectiveTypeById(Long id) {
		ObjectiveType  type = (ObjectiveType) objectiveTypeRepository.findOne(id);
		
		// now we'll have to just fill 'em up
			deepInitObjectiveType(type);
		
		return type;
		
	}

	private void deepInitObjectiveType(ObjectiveType type) {
		if(type == null || type.getChildren() == null || type.getChildren().size() == 0) {
			return;
		} else {
			type.getChildren().size();
			for(ObjectiveType t : type.getChildren()) {
				deepInitObjectiveType(t);
			}
		}
	}

	@Override
	public List<BudgetType> findRootBudgetType() {
		return budgetTypeRepository.findRootBudgetType();
	}
	
	@Override
	public BudgetType findBudgetTypeById(Long id) {
		BudgetType b = budgetTypeRepository.findOne(id);
		if(b!=null) {
			b.doBasicLazyLoad();
			
			b.getChildren().size();
			//for each children get
		}
		return b;
	}
	
	@Override
	public Page<BudgetType> findBudgetTypeByLevelAndMainType(Integer fiscalYear, Integer level,
			Long typeId, String query, Pageable pageable) {
		String mainTypePath = "%." + typeId.toString() + ".%";
		logger.debug(query);
		Page<BudgetType> p = budgetTypeRepository.findAllByParentLevelAndParentPathLike(level,mainTypePath, query,pageable);
		
		// now we load the necceessary 
		for(BudgetType b : p ) {
			b.getLevel().getId();
			if(b.getCommonType() != null) {
				b.getCommonType().getId();
			}
			if(b.getUnit() != null) {
				b.getUnit().getId();
			}
			b.setStrategies(formulaStrategyRepository.findOnlyNonStandardByfiscalYearAndType_id(fiscalYear, b.getId()));
			b.setStandardStrategy(formulaStrategyRepository.findOnlyStandardByfiscalYearAndType_id(fiscalYear, b.getId()));
			b.setCurrentFiscalYear(fiscalYear);
		}
		
		return p;
		
	}
	
	@Override
	public List<BudgetType> findBudgetTypeByLevel(Integer fiscalYear, Integer level) {
		List<BudgetType> p = budgetTypeRepository.findAllByParentLevel(level);
		
		// now we load the necceessary 
		for(BudgetType b : p ) {
			b.getLevel().getId();
			if(b.getCommonType() != null) {
				b.getCommonType().getId();
			}
			if(b.getUnit() != null) {
				b.getUnit().getId();
			}
			
			b.getParent().getId();
			b.getChildren().size();
			
			logger.debug("children size: " + b.getChildren().size());
			
			b.setStrategies(formulaStrategyRepository.findOnlyNonStandardByfiscalYearAndType_id(fiscalYear, b.getId()));
			b.setStandardStrategy(formulaStrategyRepository.findOnlyStandardByfiscalYearAndType_id(fiscalYear, b.getId()));
			b.setCurrentFiscalYear(fiscalYear);
		}
		
		return p;
		
	}
	
	@Override
	public BudgetType saveBudgetType(JsonNode node) {
		BudgetType budgetType;
		if(node.get("id") == null) {
			budgetType = new BudgetType();
			Long parentTypeId = node.get("parent").get("id").asLong();
			
			BudgetType parent = budgetTypeRepository.findOne(parentTypeId);
			if(parent != null) {
				logger.debug("parentId: " + parent.getId());
				budgetType.setParent(parent);
				budgetType.setParentPath("." + parent.getId() + parent.getParentPath());
				budgetType.setParentLevel(node.get("parentLevel").asInt());
			}
			
			BudgetLevel level = budgetTypeRepository.findBudgetLevelNumber(budgetType.getParentLevel());
			budgetType.setLevel(level);
			
			Integer prevLineNumber = null;
			if(parent.getChildren().size() > 0) {
				BudgetType lastIndexType = parent.getChildren().get(parent.getChildren().size()-1);
				budgetType.setIndex(lastIndexType.getIndex()+1);
				
				prevLineNumber = lastIndexType.getLineNumber();
				
			} else {
				budgetType.setIndex(0);
				
				prevLineNumber = parent.getLineNumber();
			}
			
			budgetType.setLineNumber(prevLineNumber+1);
			
			// now the code
			logger.debug("level " + level.getId());
			Integer maxCode = budgetTypeRepository.findMaxCodeAtLevel(level);
			logger.debug("maxCode" +maxCode);
			budgetType.setCode(maxCode+1);
			
			
			// and the last piece will be lineNumber 
			budgetTypeRepository.incrementLineNumber(prevLineNumber);
		} else {
			budgetType = budgetTypeRepository.findOne(node.get("id").asLong());
		}
		
		
		budgetType.setName(node.get("name").asText());
		budgetType.setParentLevel(node.get("parentLevel").asInt());
		
		
		
		
		// then the commontype
		if(getJsonNodeId(node.get("commonType"))!=null) {
			BudgetCommonType bct = budgetCommonTypeRepository.findOne(getJsonNodeId(node.get("commonType")));
			budgetType.setCommonType(bct);
		}
		
		// lastly unit
		if(getJsonNodeId(node.get("unit"))!=null) {
			TargetUnit unit = targetUnitRepository.findOne(getJsonNodeId(node.get("unit")));
			budgetType.setUnit(unit);
		}
		
		budgetTypeRepository.save(budgetType);
		
		return budgetType;
	}

	@Override
	public BudgetType updateBudgetType(JsonNode node) {
		BudgetType type = budgetTypeRepository.findOne(node.get("id").asLong());
		if(type != null) {
			type.setName(node.get("name").asText());
			
			budgetTypeRepository.save(type);
		}
		
		return type;
	}

	@Override
	public void deleteBudgetType(Long id) {
		BudgetType type = budgetTypeRepository.findOne(id);
		if(type == null) {
			return ;
		}
		
		budgetTypeRepository.delete(type);
		
	}

	@Override
	public BudgetType findBudgetTypeEagerLoadById(Long id, Boolean isLoadParent) {
		BudgetType b = findBudgetTypeById(id);
		b.doEagerLoad();
		
		if(isLoadParent) {
			b.doLoadParent();
		}
		
		return b;
	}

	@Override
	public List<BudgetType> findAllMainBudgetTypeByFiscalYear(Integer fiscalYear) {
		return fiscalBudgetTypeRepository.findAllMainBudgetTypeByFiscalYear(fiscalYear);
	}

	
	@Override
	public List<Integer> findFiscalYearBudgetType() {
		List<Integer> fiscalYears = budgetTypeRepository.findFiscalYears();
		
		return fiscalYears;
	}
	
	

	@Override
	public void initFiscalBudgetType(Integer fiscalYear) {
		Iterable<BudgetType> types = budgetTypeRepository.findAll();
		
		for(BudgetType type : types) {
			// check first if we already have this one
			FiscalBudgetType fbt = fiscalBudgetTypeRepository.findOneByBudgetTypeAndFiscalYear(type, fiscalYear); 
			logger.debug("fbt: " + fbt);
			if(fbt == null) {
				// we have to add this one
				FiscalBudgetType newFbt = new FiscalBudgetType();
				newFbt.setFiscalYear(fiscalYear);
				newFbt.setBudgetType(type);
				
				// set the main type to be the same as last year!?
				FiscalBudgetType lastYearFbt = fiscalBudgetTypeRepository.findOneByBudgetTypeAndFiscalYear(type, fiscalYear-1);
				if(lastYearFbt == null) {
					newFbt.setIsMainType(false);
				} else {
					newFbt.setIsMainType(lastYearFbt.getIsMainType());
				}
				
				//now save!?
				fiscalBudgetTypeRepository.save(newFbt);
			}
		}
		
	}
	
	@Override
	@Transactional (propagation = Propagation.REQUIRED, readOnly = true)
	public List<Breadcrumb> createBreadCrumbBudgetType(String prefix,
			Integer fiscalYear, BudgetType budgetType) {
		if(budgetType == null) {
			return null;
		}
		
		BudgetType current = budgetTypeRepository.findOne(budgetType.getId());
		
		Stack<Breadcrumb> stack = new Stack<Breadcrumb>();
		
		while(current != null) {
			Breadcrumb b = new Breadcrumb();
			if(current.getParent() == null) {
				// this is the root!
				b.setUrl(prefix + "/" + fiscalYear + "/" + current.getId() + "/");
				b.setValue("ปี " + fiscalYear);
				stack.push(b);
				
				b = new Breadcrumb();
				b.setUrl(prefix+ "/" );
				b.setValue("ROOT");
				stack.push(b);
				
			} else {
			
				
				b.setUrl(prefix + "/" + + fiscalYear + "/" + current.getId() + "/");
				b.setValue(current.getName());
				stack.push(b);
			}
			
			current = current.getParent();
		}
		
		List<Breadcrumb> list = new ArrayList<Breadcrumb>();
		
		while (stack.size() > 0) {
			list.add(stack.pop());
		}
		
		return list;
	}

	@Override
	public List<FormulaStrategy> findFormulaStrategyByfiscalYearAndTypeId(
			Integer fiscalYear, Long budgetTypeId) {
		List<FormulaStrategy> list = formulaStrategyRepository.findByfiscalYearAndType_id(fiscalYear, budgetTypeId);
		for(FormulaStrategy strategy : list) {
			strategy.getFormulaColumns().size();
			logger.debug("-----" + strategy.getType().getName());
			if(strategy.getType().getParent()!=null) {
				strategy.getType().getParent().getName();
			}
			//logger.debug("-----" + strategy.getType().getParent().getName());
		}
		
		return list;
	}
	
	@Override
	public List<FormulaStrategy> findAllFormulaStrategyByfiscalYearAndBudgetType_ParentPathLike(
			Integer fiscalYear, String parentPath) {
		logger.debug("parentPath: {} ", parentPath);
		List<FormulaStrategy> list = formulaStrategyRepository.findAllByfiscalYearAndType_ParentPathLike(fiscalYear, parentPath);
		for(FormulaStrategy strategy : list) {
			strategy.getFormulaColumns().size();
		}
		
		return list;
	}
	
	@Override
	public List<FormulaStrategy> findAllFormulaStrategyByfiscalYearAndIsStandardItemAndBudgetType_ParentPathLike(
			Integer fiscalYear, Boolean isStandardItem, Long budgetTypeId, String parentPath) {
		List<FormulaStrategy> list = formulaStrategyRepository.findAllByfiscalYearAndIsStandardItemAndType_ParentPathLike(fiscalYear, isStandardItem, budgetTypeId, parentPath);
		for(FormulaStrategy strategy : list) {
			strategy.getFormulaColumns().size();
			strategy.getType().getParent().getName();
			if(strategy.getCommonType() !=null) {
				strategy.getCommonType().getName();
			}
			if(strategy.getUnit() !=null) {
				strategy.getUnit().getName();
			}
		}
		
		return list;
	}


	@Override
	public FormulaStrategy saveFormulaStrategy(JsonNode strategy) {
		FormulaStrategy fs;
		if(strategy.get("id") == null) {
			fs=new FormulaStrategy();
		} else {
			fs = formulaStrategyRepository.findOne(strategy.get("id").asLong());
			
		}
		
		fs.setName(strategy.get("name").asText());
		fs.setFiscalYear(strategy.get("fiscalYear").asInt());
		fs.setIsStandardItem(strategy.get("isStandardItem").asBoolean());
		
		if(strategy.get("standardPrice") == null) {
			fs.setStandardPrice(0);
		} else {
			
			try {
				fs.setStandardPrice(strategy.get("standardPrice").asInt());
			} catch (NumberFormatException e) {
				fs.setStandardPrice(0);
			}
		}
		
		if(strategy.get("isStandardItem") != null) {
			fs.setIsStandardItem(strategy.get("isStandardItem").asBoolean());
		} else {
			fs.setIsStandardItem(false);
		}
		
		// now get the budgetType
		if(strategy.get("type") != null) {
			Long btId = strategy.get("type").get("id").asLong();
			BudgetType budgetType = budgetTypeRepository.findOne(btId);
			fs.setType(budgetType);
		}
		
		fs.getType().getParent().getChildren().size();
		
		
		// now save the commonType
		if(strategy.get("commonType") != null && strategy.get("commonType").get("id") != null) {
			
			Long cid = strategy.get("commonType").get("id").asLong();
			BudgetCommonType bct = budgetCommonTypeRepository.findOne(cid);
			fs.setCommonType(bct);
		}
		
		if(strategy.get("unit") != null && strategy.get("unit").get("id") != null) {
			Long unitId = strategy.get("unit").get("id").asLong();
			TargetUnit unit = targetUnitRepository.findOne(unitId);
			fs.setUnit(unit);
		}
		
		List<FormulaColumn> newFcList = new ArrayList<FormulaColumn>();
		List<FormulaColumn> oldFcList = fs.getFormulaColumns();
		// check if formulaColumn is exists!
		for(JsonNode fcNode : strategy.get("formulaColumns")) {
			if(fcNode.get("id") != null) {
				FormulaColumn fc = formulaColumnRepository.findOne(fcNode.get("id").asLong());
				if(fc!=null) {
					//remove from old Fc
					oldFcList.remove(fc);
					
					//update fc
					fc.setUnitName(fcNode.get("unitName").asText());
					fc.setIndex(fcNode.get("index").asInt());
					if(fcNode.get("isFixed") == null) {
						fc.setIsFixed(true);
					} else {
						fc.setIsFixed(fcNode.get("isFixed").asBoolean());
					}
					
					newFcList.add(fc);
					formulaColumnRepository.save(fc);
					logger.debug("fc.unitName: " + fc.getUnitName());
				}
			} else {
				FormulaColumn fc = new FormulaColumn();
				fc.setUnitName(fcNode.get("unitName").asText());
				fc.setIndex(fcNode.get("index").asInt());
				if(fcNode.get("isFixed") == null) {
					fc.setIsFixed(true);
				} else {
					fc.setIsFixed(fcNode.get("isFixed").asBoolean());
				}
				fc.setStrategy(fs);
				
				newFcList.add(fc);
				formulaColumnRepository.save(fc);
				logger.debug("fc.unitName: " + fc.getUnitName());
			}
		}
		
		fs.setFormulaColumns(newFcList);
		
		// we should destroy all fc in old list!
		if(oldFcList!=null) {
			for(FormulaColumn fc : oldFcList) {
				formulaColumnRepository.delete(fc);
			}
		}
		
		
		FormulaStrategy saveFs = formulaStrategyRepository.save(fs);
		
		saveFs.getType().setStrategies(formulaStrategyRepository.findOnlyNonStandardByfiscalYearAndType_id(fs.getFiscalYear(), fs.getType().getId()));
		saveFs.getType().setStandardStrategy(formulaStrategyRepository.findOnlyStandardByfiscalYearAndType_id(fs.getFiscalYear(), fs.getType().getId()));
		saveFs.getType().setCurrentFiscalYear(fs.getFiscalYear());
		
		logger.debug("about to return fs!");
		
		for(FormulaColumn fc : saveFs.getFormulaColumns()) {
			logger.debug(fc.getUnitName());
		}
		
		return saveFs;
		
	}

	@Override
	public FormulaStrategy updateFormulaStrategy(JsonNode strategy) {
		return saveFormulaStrategy(strategy);
	}

	
	@Override
	public void deleteFormulaStrategy(Long id) {
		// we'll have to update the rest of index !
		// so get the one we want to delete first 
		FormulaStrategy strategy = formulaStrategyRepository.findOne(id);
		
		formulaStrategyRepository.delete(id);
		formulaStrategyRepository.reIndex(strategy.getIndex(), 
				strategy.getFiscalYear(), strategy.getType());
	}

	@Override
	public void deleteFormulaColumn(Long id) {
		// get this one first
		FormulaColumn column = formulaColumnRepository.findOne(id);
		formulaColumnRepository.delete(id);
		formulaColumnRepository.reIndex(column.getIndex(), column.getStrategy());
	}

	@Override
	public FormulaColumn saveFormulaColumn(
			FormulaColumn formulaColumn) {
		return formulaColumnRepository.save(formulaColumn);
	}

	@Override
	public FormulaColumn updateFormulaColumn(
			FormulaColumn formulaColumn) {
		// so we'll get FormulaColumn First
		FormulaColumn columnFromJpa = formulaColumnRepository.findOne(
				formulaColumn.getId());
		
		// now update this columnFromJpa
		columnFromJpa.setColumnName(formulaColumn.getColumnName());
		columnFromJpa.setIsFixed(formulaColumn.getIsFixed());
		columnFromJpa.setUnitName(formulaColumn.getUnitName());
		columnFromJpa.setValue(formulaColumn.getValue());
		
		
		// and we can save now
		formulaColumnRepository.save(columnFromJpa);
		
		// and happily return 
		return columnFromJpa;
	}

	@Override
	public List<Breadcrumb> createBreadCrumbObjective(String prefix,
			Integer fiscalYear, Objective objective) {
		if(objective == null) {
			List<Breadcrumb> list = new ArrayList<Breadcrumb>();
			Breadcrumb b = new Breadcrumb();
			b.setUrl(prefix+ "/" );
			b.setValue("ROOT");
			
			list.add(b);
			
			b = new Breadcrumb();
			b.setUrl(prefix + "/" + fiscalYear + "/");
			b.setValue("ทะเบียนปี " + fiscalYear);
			list.add(b);
			
			return list;
		}
		
		Objective current = objectiveRepository.findOne(objective.getId());
		
		Stack<Breadcrumb> stack = new Stack<Breadcrumb>();
		
		while(current != null) {
			Breadcrumb b = new Breadcrumb();
			String code = current.getCode();
			if(code==null) {
				code= "";
			}
			
			if(current.getParent() == null || current.getType().getId() == 103) {
				// this is the root!
				
						
				b.setUrl(prefix + "/" + fiscalYear + "/");
				b.setValue(current.getType().getName() + " "+ code + ". <br/>" + current.getName());
				stack.push(b);
				
				
				b = new Breadcrumb();
				b.setUrl(prefix + "/" + fiscalYear + "/");
				b.setValue("ทะเบียนปี " + fiscalYear);
				stack.push(b);
				
				
				
				b = new Breadcrumb();
				b.setUrl(prefix+ "/" );
				b.setValue("ROOT");
				stack.push(b);
				
				break;
				
			} else {
				b.setUrl(prefix + "/" + + fiscalYear + "/" + current.getId() + "/");
				
				b.setValue(current.getType().getName() + " " + code + ". <br/>" + current.getName());
				stack.push(b);
			}
			
			current = current.getParent();
		}
		
		List<Breadcrumb> list = new ArrayList<Breadcrumb>();
		
		while (stack.size() > 0) {
			list.add(stack.pop());
		}
		
		return list;
	}

	@Override
	public Objective objectiveDoEagerLoad(Long objectiveId) {
		Objective objective = objectiveRepository.findOne(objectiveId);
		objective.doEagerLoad();
		
		return objective;
	}

	@Override
	public Objective updateObjective(Objective objective) {
		// now get Objective form our DB first
		Objective objectiveFromJpa = objectiveRepository.findOne(objective.getId());
		
		if(objectiveFromJpa != null) {
			// OK go through the supposed model
			objectiveFromJpa.setName(objective.getName());
			objectiveFromJpa.setFiscalYear(objective.getFiscalYear());
			
//			if(objective.getBudgetType() != null && objective.getBudgetType().getId() != null) {
//				objectiveFromJpa.setBudgetType(objective.getBudgetType());
//			} 
			
			if(objective.getParent() != null && objective.getParent().getId() != null) {
				objectiveFromJpa.setParent(objective.getParent());
			}
			
			if(objective.getType() != null && objective.getType().getId() != null) {
				objectiveFromJpa.setType(objective.getType());
			}
			
			// we don't do anything for children
			
			objectiveRepository.save(objectiveFromJpa);
		}
		
		return objectiveFromJpa;
		
	}

	
	
	/**
	 * ค้นหาลูกของ Objective พร้อม Load AllocationRecords
	 * 
	 * @param objectiveId id ของ parent Objective
	 */
	@Override
	public List<Objective> findChildrenObjectivewithAllocationRecords(
			Long objectiveId) {
		
		List<Objective> objectives = objectiveRepository.findByParent_IdLoadAllocationRecords("%."+objectiveId+".%");
		return objectives;
	}

	@Override
	public List<Objective> findChildrenObjectivewithBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId, Boolean isChildrenTraversal) {
		logger.debug("ownerId: " + ownerId);
		List<Objective> objectives = objectiveRepository.findByObjectiveBudgetProposal(fiscalYear, ownerId, objectiveId);
		
		for(Objective objective : objectives) {
//			logger.debug("** " + objective.getBudgetType().getName());
			objective.doEagerLoadWithBudgetProposal(isChildrenTraversal);
		}
		return objectives;
	}

	@Override
	public List<Objective> findChildrenObjectivewithObjectiveBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId,
			Boolean isChildrenTraversal) {
		List<Objective> objectives = objectiveRepository.findByObjectiveBudgetProposal(fiscalYear, ownerId, objectiveId);
		
		for(Objective objective : objectives) {
//			logger.debug("** " + objective.getBudgetType().getName());
			objective.doEagerLoadWithBudgetProposal(isChildrenTraversal);
		}
		return objectives;
	}
	
	
	@Override
	public BudgetProposal findBudgetProposalById(Long budgetProposalId) {
		return budgetProposalRepository.findOne(budgetProposalId);
	}

	@Override
	public String initReservedBudget(Integer fiscalYear) {
		Objective root = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		String parentPathLikeString = "%."+root.getId()+"%";		
		List<Objective> list = objectiveRepository.findFlatByObjectiveBudgetProposal(fiscalYear,parentPathLikeString);
		
		// we will copy from the last round (index = 2)
					List<AllocationRecord> allocationRecordList = allocationRecordRepository
							.findAllByForObjective_fiscalYearAndIndex(fiscalYear, 2);
					
		// go through this one
		for(AllocationRecord record: allocationRecordList) {
			ReservedBudget reservedBudget = reservedBudgetRepository.findOneByBudgetTypeAndObjective(record.getBudgetType(), record.getForObjective());
			
			if(reservedBudget == null) {
				reservedBudget = new ReservedBudget();
			}
			reservedBudget.setAmountReserved(0L);
			reservedBudget.setBudgetType(record.getBudgetType());
			reservedBudget.setForObjective(record.getForObjective());

			
			reservedBudgetRepository.save(reservedBudget);
		}
		
//		List<RequestColumn> requestColumns = requestColumnRepositories.findAllByFiscalYear(fiscalYear);
//		for(RequestColumn rc : requestColumns) {
//			rc.setAllocatedAmount(rc.getAmount());
//			requestColumnRepositories.save(rc);
//		}
//	
//		List<FormulaColumn> formulaColumns = formulaColumnRepository.findAllByFiscalYear(fiscalYear);
//		for(FormulaColumn fc : formulaColumns) {
//			fc.setAllocatedValue(fc.getValue());
//			formulaColumnRepository.save(fc);
//		}
		

		return "success";
	

	}

	
	@Override
	public String initAllocationRecord(Integer fiscalYear, Integer round) {
		Objective root = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		String parentPathLikeString = "%."+root.getId()+"%";		
		List<Objective> list = objectiveRepository.findFlatByObjectiveBudgetProposal(fiscalYear,parentPathLikeString);
		
		List<BudgetProposal> proposalList = budgetProposalRepository
				.findBudgetProposalByFiscalYearAndParentPath(fiscalYear, parentPathLikeString);
		
		if(round == 1) {
			//loop through proposalList
			for(BudgetProposal proposal : proposalList) {
				Integer index = list.indexOf(proposal.getForObjective());
				Objective o = list.get(index);
				o.getProposals().size();
				
				o.addToSumBudgetTypeProposals(proposal);
				
				logger.debug("AAding proposal {} to objective: {}", proposal.getId(), o.getId());
				
				
				//o.getProposals().add(proposal);
				logger.debug("proposal size is " + o.getProposals().size());
				
			}
			
			
			
			// now loop through the Objective
			for(Objective o : list) {
				if(o.getSumBudgetTypeProposals() != null) {
					for(BudgetProposal b : o.getSumBudgetTypeProposals()) {
						// now get this on
						AllocationRecord allocationRecord = allocationRecordRepository.findOneByBudgetTypeAndObjectiveAndIndex(b.getBudgetType(), o ,round-1);  
						if(allocationRecord != null) {
							allocationRecord.setAmountAllocated(b.getAmountRequest().doubleValue());
						} else {
							
							
							allocationRecord = new AllocationRecord();
							allocationRecord.setAmountAllocated(b.getAmountRequest().doubleValue());
							allocationRecord.setBudgetType(b.getBudgetType());
							allocationRecord.setForObjective(o);
							allocationRecord.setIndex(round-1);
							
							allocationRecordRepository.save(allocationRecord);
						}
					}
				}
				
				// now the targetValue
				for(ObjectiveTarget target: o.getTargets()) {
					List<TargetValue> targetvalues = targetValueRepository.findAllByTargetIdAndObjectiveId(target.getId(), o.getId());
					
					logger.debug("----------------------------- {} / {} ", o.getId(), target.getId());
					
					Long sum = 0L;
					for(TargetValue tv : targetvalues) {
						sum += tv.getRequestedValue();
					}
					
					// now we can add 
					TargetValueAllocationRecord tvar = targetValueAllocationRecordRepository.findOneByIndexAndForObjectiveAndTarget(round-1, o, target);
					if(tvar == null) {
						logger.debug("+++++++++++++++++++++++++++++++++++++++++ {} / {} ", o.getId(), target.getId());
						tvar = new TargetValueAllocationRecord();
						tvar.setIndex(round-1);
						tvar.setForObjective(o);
						tvar.setTarget(target);
					} 
					
					tvar.setAmountAllocated(sum);
					
					targetValueAllocationRecordRepository.save(tvar);
					
				}
				
			}
		} else {
			// we will copy from the previous round...
			List<AllocationRecord> allocationRecordList = allocationRecordRepository
					.findAllByForObjective_fiscalYearAndIndex(fiscalYear, round-2);
			
			// go through this one
			for(AllocationRecord record: allocationRecordList) {
				AllocationRecord dbRecord = allocationRecordRepository.findOneByBudgetTypeAndObjectiveAndIndex(record.getBudgetType(), record.getForObjective(), round-1);
				
				if(dbRecord == null) {
					dbRecord = new AllocationRecord();
				}
				dbRecord.setAmountAllocated(record.getAmountAllocated());
				dbRecord.setBudgetType(record.getBudgetType());
				dbRecord.setForObjective(record.getForObjective());
				dbRecord.setIndex(round-1);
				
				allocationRecordRepository.save(dbRecord);
			}
			
			List<TargetValueAllocationRecord> tvarList = targetValueAllocationRecordRepository
					.findAllByForObjective_FiscalYearAndIndex(fiscalYear, round-2);
			for(TargetValueAllocationRecord rvar : tvarList) {
				TargetValueAllocationRecord dbRecord = targetValueAllocationRecordRepository
						.findOneByTargetAndForObjectiveAndIndex(rvar.getTarget(), rvar.getForObjective(), round-1);
				
				logger.debug("objectiveid: {}", rvar.getForObjective().getId());
				
				if(dbRecord == null) {
					dbRecord = new TargetValueAllocationRecord();
					dbRecord.setIndex(round-1);
					dbRecord.setForObjective(rvar.getForObjective());
					dbRecord.setTarget(rvar.getTarget());
				}
				
				dbRecord.setAmountAllocated(rvar.getAmountAllocated());
				targetValueAllocationRecordRepository.save(dbRecord);
			}
		}
		return "success";
		
	}

	
	@Override
	public List<Objective> findFlatChildrenObjectivewithBudgetProposalAndAllocation(
			Integer fiscalYear, Long objectiveId, Boolean isFindObjectiveBudget) {
		String parentPathLikeString = "%."+objectiveId.toString()+"%";
		List<Objective> list = objectiveRepository.findFlatByObjectiveBudgetProposal(fiscalYear, parentPathLikeString);
		
		List<BudgetProposal> proposalList = budgetProposalRepository
				.findBudgetProposalByFiscalYearAndParentPath(fiscalYear, parentPathLikeString);
		
		//loop through proposalList
		for(BudgetProposal proposal : proposalList) {
			Integer index = list.indexOf(proposal.getForObjective());
			Objective o = list.get(index);
			o.getProposals().size();
			
			o.addToSumBudgetTypeProposals(proposal);
			
			logger.debug("AAding proposal {} to objective: {}", proposal.getId(), o.getId());
			
			//o.getProposals().add(proposal);
			logger.debug("proposal size is " + o.getProposals().size());
		}
		
		//now loop through allocationRecord
		List<AllocationRecord> recordList = allocationRecordRepository
				.findBudgetProposalByFiscalYearAndOwnerAndParentPath(fiscalYear, parentPathLikeString);
		for(AllocationRecord record : recordList) {
			Integer index = list.indexOf(record.getForObjective());
			Objective o = list.get(index);
			logger.debug("AAding Allocation {} to objective: {}", record.getId(), o.getId());
			
			if(o.getAllocationRecords()==null) {
				o.setAllocationRecords(new ArrayList<AllocationRecord>());
			}
			
			//o.getProposals().add(record);
			logger.debug("proposal size is " + o.getAllocationRecords().size());
		}
		
		// And lastly loop through reservedBudget
		List<ReservedBudget> reservedBudgets = reservedBudgetRepository.findAllByFiscalYearAndParentPathLike(fiscalYear, parentPathLikeString);
		for(ReservedBudget rb : reservedBudgets) {
			logger.debug("reservedBuget: {} ", rb.getForObjective().getId());
			Integer index = list.indexOf(rb.getForObjective());
			Objective o = list.get(index);
			
			o.getReservedBudgets().size();
			
			
		}
		
		// oh not yet!
		for(Objective o : list) {
			o.getTargetValueAllocationRecords().size();
			o.getTargetValues().size();
			for(TargetValue tv : o.getTargetValues()) {
				tv.getOwner().getId();
			}
			
			
		}
		
		return list;
	}
	
	
	@Override
	public List<Objective> findFlatChildrenObjectivewithBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId) {
		String parentPathLikeString = "%."+objectiveId.toString()+"%";
		List<Objective> list = objectiveRepository.findFlatByObjectiveBudgetProposal(fiscalYear, ownerId, parentPathLikeString);
		
		List<BudgetProposal> proposalList = budgetProposalRepository
				.findBudgetProposalByFiscalYearAndOwnerAndParentPath(fiscalYear, ownerId, parentPathLikeString);
		
		//loop through proposalList
		for(BudgetProposal proposal : proposalList) {
			Integer index = list.indexOf(proposal.getForObjective());
			Objective o = list.get(index);
			logger.debug("AAding proposal {} to objective: {}", proposal.getId(), o.getId());
			
			o.addfilterProposal(proposal);
			//logger.debug("proposal size is " + o.getProposals().size());
			
		}
		
		// get List of targetValue
		Map<String, TargetValue> targetValueMap = new HashMap<String, TargetValue>();
		List<TargetValue> targetValues = targetValueRepository.findAllByOnwerIdAndObjectiveParentPathLike(ownerId, parentPathLikeString);
		for(TargetValue tv : targetValues) {
			targetValueMap.put(tv.getForObjective().getId()+ "," + tv.getTarget().getId(), tv);
				
		}
		
		// get List of ObjectiveTarget?
		List<ObjectiveTarget> targets = objectiveTargetRepository.findAllByObjectiveParentPathLike(parentPathLikeString);
		
		for(ObjectiveTarget target : targets) {
			target.getForObjectives().size();
			
			for(Objective o : target.getForObjectives()) {
				logger.debug("Adding objective target to list");
				Integer index = list.indexOf(o);
				Objective objInlist = list.get(index);
				logger.debug("objInList target size = " + objInlist.getTargets().size());
				
				TargetValue tv = targetValueMap.get(objInlist.getId() + "," + target.getId());
				if(tv==null) {
					tv = new TargetValue();
					tv.setTarget(target);
					tv.setForObjective(objInlist);
					
				}
				objInlist.addfilterTargetValue(tv);
				
			}
						
		}
		
		return list;
	}
	
	@Override
	public List<Objective> findFlatChildrenObjectivewithObjectiveBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId) {
		String parentPathLikeString = "%."+objectiveId.toString()+"%";
		List<Objective> list = objectiveRepository.findFlatByObjectiveObjectiveBudgetProposal(fiscalYear, ownerId, parentPathLikeString);
		
		for(Objective o : list) {
		//get List of ObjectiveBudgetProposal
			
			logger.debug("finding objective budget proposal of objective code: " + o.getCode() + " ");
			List<ObjectiveBudgetProposal> obpList = objectiveBudgetProposalRepository.findAllByForObjective_IdAndOwner_Id(o.getId(), ownerId);
			
			logger.debug("found " + obpList.size() + " proposals" );
			o.setFilterObjectiveBudgetProposals(obpList);
		}
		
		// get List of targetValue
		Map<String, TargetValue> targetValueMap = new HashMap<String, TargetValue>();
		List<TargetValue> targetValues = targetValueRepository.findAllByOnwerIdAndObjectiveParentPathLike(ownerId, parentPathLikeString);
		for(TargetValue tv : targetValues) {
			targetValueMap.put(tv.getForObjective().getId()+ "," + tv.getTarget().getId(), tv);
				
		}
		
		// get List of ObjectiveTarget?
		List<ObjectiveTarget> targets = objectiveTargetRepository.findAllByObjectiveParentPathLike(parentPathLikeString);
		
		for(ObjectiveTarget target : targets) {
			target.getForObjectives().size();
			
			for(Objective o : target.getForObjectives()) {
				//logger.debug("Adding objective target to list");
				Integer index = list.indexOf(o);
				Objective objInlist = list.get(index);
				//logger.debug("objInList target size = " + objInlist.getTargets().size());
				
				TargetValue tv = targetValueMap.get(objInlist.getId() + "," + target.getId());
				if(tv==null) {
					tv = new TargetValue();
					tv.setTarget(target);
					tv.setForObjective(objInlist);
					
				}
				objInlist.addfilterTargetValue(tv);
				
			}
						
		}
		
		return list;
	}

	@Override
	public ProposalStrategy deleteProposalStrategy(Long id) {
		ProposalStrategy proposalStrategy = proposalStrategyRepository.findOne(id);
		
		Long amountToBeReduced = proposalStrategy.getTotalCalculatedAmount();
		Long amountRequestNext1Year = proposalStrategy.getAmountRequestNext1Year();
		Long amountRequestNext2Year = proposalStrategy.getAmountRequestNext2Year();
		Long amountRequestNext3Year = proposalStrategy.getAmountRequestNext3Year();
		
		BudgetProposal b = proposalStrategy.getProposal();
		b.addAmountRequest(-amountToBeReduced);
		b.addAmountRequestNext1Year(-amountRequestNext1Year);
		b.addAmountRequestNext2Year(-amountRequestNext2Year);
		b.addAmountRequestNext3Year(-amountRequestNext3Year);
		budgetProposalRepository.save(b);
		
		Organization owner = b.getOwner();
		
		// now walk up ward
		BudgetProposal temp = b;
		// OK we'll go through the amount of this one and it's parent!?
		while (temp.getForObjective().getParent() != null) {
			// now we'll get all proposal
			Objective parent = temp.getForObjective().getParent();
			temp = budgetProposalRepository.findByForObjectiveAndOwnerAndBudgetType(parent,owner,b.getBudgetType());
			
			if(temp!=null) {
				temp.addAmountRequest(-amountToBeReduced);
				temp.addAmountRequestNext1Year(-amountRequestNext1Year);
				temp.addAmountRequestNext2Year(-amountRequestNext2Year);
				temp.addAmountRequestNext3Year(-amountRequestNext3Year);
				
			} 
			budgetProposalRepository.save(temp);
		}
		
		proposalStrategyRepository.delete(proposalStrategy);
		
		return proposalStrategy;
	}
	
	
	private ProposalStrategy saveProposalStrategy(ProposalStrategy strategy, ProposalStrategy oldStrategy, Long budgetProposalId, Long formulaStrategyId) {
		
		FormulaStrategy formulaStrategy=null;
		if(formulaStrategyId != null) {
		 formulaStrategy = formulaStrategyRepository.findOne(formulaStrategyId);
		}
		
		strategy.setFormulaStrategy(formulaStrategy);
		
		// 
		BudgetProposal b = budgetProposalRepository.findOne(budgetProposalId);
		
		b.addAmountRequest(strategy.getTotalCalculatedAmount()-oldStrategy.getTotalCalculatedAmount());
		b.addAmountRequestNext1Year(strategy.getAmountRequestNext1Year()-oldStrategy.getAmountRequestNext1Year());
		b.addAmountRequestNext2Year(strategy.getAmountRequestNext2Year()-oldStrategy.getAmountRequestNext2Year());
		b.addAmountRequestNext3Year(strategy.getAmountRequestNext2Year()-oldStrategy.getAmountRequestNext3Year());
		
		budgetProposalRepository.save(b);
		
		strategy.setProposal(b);
		
		Organization owner = b.getOwner();
		BudgetType budgetType = b.getBudgetType();
		
		BudgetProposal temp = b;
		// OK we'll go through the amount of this one and it's parent!?
		while (temp.getForObjective().getParent() != null) {
			// now we'll get all proposal
			Objective parent = temp.getForObjective().getParent();
			temp = budgetProposalRepository.findByForObjectiveAndOwnerAndBudgetType(parent,owner, budgetType);
			
			if(temp!=null) {
				temp.addAmountRequest(strategy.getTotalCalculatedAmount()-oldStrategy.getTotalCalculatedAmount());
				temp.setBudgetType(budgetType);
				temp.addAmountRequestNext1Year(strategy.getAmountRequestNext1Year()-oldStrategy.getAmountRequestNext1Year());
				temp.addAmountRequestNext2Year(strategy.getAmountRequestNext2Year()-oldStrategy.getAmountRequestNext2Year());
				temp.addAmountRequestNext3Year(strategy.getAmountRequestNext3Year()-oldStrategy.getAmountRequestNext3Year());
			} else {
				temp = new BudgetProposal();
				temp.setForObjective(parent);
				temp.setOwner(owner);
				temp.setBudgetType(budgetType);
				temp.setAmountRequest(strategy.getTotalCalculatedAmount());
				temp.setAmountRequestNext1Year(strategy.getAmountRequestNext1Year());
				temp.setAmountRequestNext2Year(strategy.getAmountRequestNext2Year());
				temp.setAmountRequestNext3Year(strategy.getAmountRequestNext3Year());
			}

			budgetProposalRepository.save(temp);
		}
		
		// now deal with target
		if(strategy.getTargetValue() != null && strategy.getTargetValue() > 0) {
			Objective obj = strategy.getProposal().getForObjective();
			
			while(obj.getParent()!=null) {
				List<TargetValue> tvList = targetValueRepository.findAllByOnwerIdAndTargetUnitIdAndObjectiveId(owner.getId(), strategy.getTargetUnit().getId(), obj.getId());
				TargetValue tv = null;
				if(tvList.size() == 0) {
					
					//find a matching Target
					ObjectiveTarget matchingTarget = objectiveTargetRepository.findOneByForObjectivesAndUnit(obj, strategy.getTargetUnit());
					
					//crate a new TargetValue
					if(matchingTarget != null) {
					
						tv = new TargetValue();	
						tv.setTarget(matchingTarget);
						tv.setForObjective(obj);
						tv.setOwner(owner);
						tv.setRequestedValue(strategy.getTargetValue());
					} else {
						break;
					}
					
				} else {
				
					for(TargetValue tvInList: tvList) {
						if(tvInList.getTarget().getIsSumable()) {
							tv = tvInList;
							
							tv.adjustRequestedValue(oldStrategy.getTargetValue()-strategy.getTargetValue());
						} else {
							break;
						}
					}
				}
				
				if(tv != null) targetValueRepository.save(tv);
				
				obj = obj.getParent();
				
			}
			
		}
		
		ProposalStrategy strategyJpa =  proposalStrategyRepository.save(strategy);
		
		if(strategy.getRequestColumns() != null) {
			// we have to save these columns first
			
			for(RequestColumn rc : strategy.getRequestColumns()) {
				rc.setProposalStrategy(strategyJpa);
				requestColumnRepositories.save(rc);
			}
		}
		
		return strategyJpa;
	}

	private Long getJsonNodeId(JsonNode node) {
		if(node == null) {
			return null;
		}
		
		if(node.get("id") != null) {
			
			return node.get("id").asLong();
		} else {
			if(node.asLong() == 0) {
				return null;
			} else {
				return node.asLong();
			}
		}
		
	}
	
	private  ProposalStrategy createProposalStrategy(JsonNode psNode) {
		ProposalStrategy ps;
		if(getJsonNodeId(psNode) != null) {
			ps = proposalStrategyRepository.findOne(getJsonNodeId(psNode));
		} else {
			ps = new ProposalStrategy();
		}
		
		// the fs suppose to be there or either null?
		FormulaStrategy fs = null;
		if(psNode != null && getJsonNodeId(psNode.get("formulaStrategy")) != null) {
			fs = formulaStrategyRepository.findOne(getJsonNodeId(psNode.get("formulaStrategy")));
		}
		
		
		ps.setFormulaStrategy(fs);
		
		if(psNode != null && psNode.get("name") != null)
			ps.setName(psNode.get("name").asText());
		else 
			ps.setName("");
		
		if(psNode != null) {
			ps.setTotalCalculatedAmount(psNode.get("totalCalculatedAmount").asLong());
			ps.setAmountRequestNext1Year(psNode.get("amountRequestNext1Year").asLong());
			ps.setAmountRequestNext2Year(psNode.get("amountRequestNext2Year").asLong());
			ps.setAmountRequestNext3Year(psNode.get("amountRequestNext3Year").asLong());
		}
		
		
		// now look at the formulaColumns
		if(psNode!= null && psNode.get("formulaStrategy") != null) {
			
			logger.debug(">> formulaStrategy: "+ psNode.get("formulaStrategy").toString());
			
			List<RequestColumn> rcList = new ArrayList<RequestColumn>();
			ps.setRequestColumns(rcList);
			
			logger.debug(">> requestColumns: "+ psNode.get("requestColumns").toString());
			Iterator<JsonNode> rcNodeIter = psNode.get("requestColumns").iterator();
			while (rcNodeIter.hasNext()) {
			
				JsonNode rcNode  = rcNodeIter.next();
				RequestColumn rc;
				if(getJsonNodeId(rcNode) != null) {
					rc = requestColumnRepositories.findOne(getJsonNodeId(rcNode));
				} else {
					rc = new RequestColumn();
				}
				
				FormulaColumn fc = formulaColumnRepository.findOne(getJsonNodeId(rcNode.get("column")));
				
				rc.setAmount(rcNode.get("amount").asInt());
				rc.setProposalStrategy(ps);
				rc.setColumn(fc);
				
				rcList.add(rc);
			}
		}
		// lastly do the targetValue
		if( psNode != null && getJsonNodeId(psNode.get("targetUnit")) != null ) {
			TargetUnit unit = targetUnitRepository.findOne(getJsonNodeId(psNode.get("targetUnit")));
			ps.setTargetUnit(unit);
			if(psNode.get("targetValue") != null) {
				ps.setTargetValue(psNode.get("targetValue").asLong());
			} else {
				ps.setTargetValue(0L);
			}
		}
		
		
		return ps;
	}
	
	private BudgetProposal saveBudgetProposal(JsonNode proposalNode, Organization owner) {
		
		logger.debug(proposalNode.toString());
		
		// we only deal with new proposal here
		if(proposalNode.get("id") != null) {
			return null;
		}
		
		BudgetProposal proposal = new BudgetProposal();
		// now wire up all the dressing?
		
		proposal.setOwner(owner);
		
		Long objectiveId = getJsonNodeId(proposalNode.get("forObjective"));
		
		Objective forObjective = objectiveRepository.findOne(objectiveId);
		proposal.setForObjective(forObjective);

		
		
		JsonNode a = proposalNode.get("budgetType");
		getJsonNodeId(a);
		
		
		BudgetType budgetType = budgetTypeRepository.findOne(getJsonNodeId(proposalNode.get("budgetType")));
		proposal.setBudgetType(budgetType);
		
		budgetProposalRepository.save(proposal);
		
		ProposalStrategy oldps = null;
		if(proposalNode.get("proposalStrategies") != null && proposalNode.get("proposalStrategies").get(0) != null 
				&& proposalNode.get("proposalStrategies").get(0).get("id")!= null) { 
			 oldps = proposalStrategyRepository.findOne(proposalNode.get("proposalStrategies").get(0).get("id").asLong());
		}
		
		ProposalStrategy oldStrategy = ProposalStrategy.copyLongValue(oldps);
		
		
		ProposalStrategy ps = createProposalStrategy(proposalNode.get("proposalStrategies").get(0));
	
		saveProposalStrategy(ps,  oldStrategy, proposal.getId(),
				ps.getFormulaStrategy() != null ? ps.getFormulaStrategy().getId() : null );
		
		if(proposal.getProposalStrategies() == null) {
			List<ProposalStrategy> psList = new ArrayList<ProposalStrategy> (); 
			psList.add(ps);
			
			proposal.setProposalStrategies(psList);
		}
		
		
		return proposal;
	
	}
	
	@Override
	public BudgetProposal saveBudgetProposal(JsonNode proposalNode, ThaicomUserDetail currentUser) {
		return saveBudgetProposal(proposalNode, currentUser.getWorkAt());
	}

	@Override
	public RequestColumn saveRequestColumn(RequestColumn requestColumn) {
		return requestColumnRepositories.save(requestColumn);
	}

	@Override
	public Objective addBudgetTypeToObjective(Long id, Long budgetTypeId) {
		// Ok so we get one objective
		Objective obj = objectiveRepository.findOne(id);
		
		if(obj!= null) {
			//now find the budgetType
			BudgetType b = budgetTypeRepository.findOne(budgetTypeId);
			
			//now we're just ready to add to obj
			obj.getBudgetTypes().add(b);
			
			obj.getTargets().size();
			objectiveRepository.save(obj);
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public Objective removeBudgetTypeToObjective(Long id, Long budgetTypeId) {
		// Ok so we get one objective
		Objective obj = objectiveRepository.findOne(id);
		
		if(obj!= null) {
			//now find the budgetType
			BudgetType b = budgetTypeRepository.findOne(budgetTypeId);
			
			//now we're just ready to add to obj
			obj.getBudgetTypes().remove(b);
			objectiveRepository.save(obj);
			
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public Objective updateObjectiveFields(Long id, String name, String code) {
		/// Ok so we get one objective
		Objective obj = objectiveRepository.findOne(id);
		
		obj.setName(name);
		obj.setCode(code);
		
		objectiveRepository.save(obj);
		return obj;
	}

	@Override
	public Objective saveObjective(JsonNode objectiveJsonNode) {
		Objective objective;
		
		//first check objective id
		if(objectiveJsonNode.get("id") == null) { 
			objective = new Objective();
			objective.setObjectiveName(new ObjectiveName());
		} else {
			objective = objectiveRepository.findOne(objectiveJsonNode.get("id").asLong());
		}
		
		objective.setName(objectiveJsonNode.get("name").asText());
		objective.setCode(objectiveJsonNode.get("code").asText());
		
		// doing away with name?
		if(objectiveJsonNode.get("objectiveName") != null) {
			String nameTxt = objectiveJsonNode.get("objectiveName").get("name").asText();
			objective.getObjectiveName().setName(nameTxt);
			
		}
		
		objective.setFiscalYear(objectiveJsonNode.get("fiscalYear").asInt());
		objective.getObjectiveName().setFiscalYear(objectiveJsonNode.get("fiscalYear").asInt());

		if(objectiveJsonNode.get("type") != null) {
			Long objectiveTypeId = objectiveJsonNode.get("type").get("id").asLong();
			ObjectiveType ot = objectiveTypeRepository.findOne(objectiveTypeId);
			
			objective.setType(ot);
			objective.getObjectiveName().setType(ot);
			
		}
		
		//lastly the unit
		if(objectiveJsonNode.get("units") != null ) {
			objective.setUnits(new ArrayList<TargetUnit>());
			if(objective.getTargets() != null) {
				List<ObjectiveTarget> tList = new ArrayList<ObjectiveTarget>();
				for(ObjectiveTarget t : objective.getTargets()) {
					tList.add(t);
				}
				
				// now delete t here?
				while(tList.isEmpty() == false) {
					ObjectiveTarget t = tList.remove(0);
					objective.getTargets().remove(t);
					objectiveTargetRepository.delete(t);
				}
				
			}
			
			for(JsonNode unit : objectiveJsonNode.get("units")) {
				TargetUnit unitJpa = targetUnitRepository.findOne(unit.get("id").asLong());
				objective.addUnit(unitJpa);
				
				ObjectiveTarget t = new ObjectiveTarget();
				t.setFiscalYear(objective.getFiscalYear());
				t.setUnit(unitJpa);
				
				objectiveTargetRepository.save(t);
				
				objective.addTarget(t);
					
			}
			
			
		}
		 
		
		logger.debug("1. {} " , objectiveJsonNode.get("parent"));
		Objective oldParent = objective.getParent();
		String parentPathLikeString = "." + objective.getId() + ".";
		List<Objective> allDescendant = objectiveRepository.findAllDescendantOf(parentPathLikeString);
		
		if(objectiveJsonNode.get("parent") != null &&  objectiveJsonNode.get("parent").get("id") != null  ) {
			Long parentId = objectiveJsonNode.get("parent").get("id").asLong();
			
			logger.debug("fetching parent : " + parentId);
			Objective parent = objectiveRepository.findOne(parentId);
			logger.debug("111111......parent.getChildren.size() = " + parent.getChildren().size());
			
			if(parent.getParentPath() == null || parent.getParentPath().length() == 0) {
			
				objective.setParentPath("."+parentId+".");
				objective.setParentLevel(parent.getParentLevel()+1);
				
			} else {
				objective.setParentPath("."+parentId+parent.getParentPath());
				objective.setParentLevel(parent.getParentLevel()+1);
				
			} 
			
			if(parent.getLineNumber() != null) {
				
				if(objective.getLineNumber() != null) {
					objectiveRepository.removeFiscalyearLineNumberAt(objective.getFiscalYear(), objective.getLineNumber(), allDescendant.size()+1);
				}
				
				Integer maxLineNumber = null;
				
				if(parent.getChildren().size() == 0) {
					logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>parent LineNumber Before: " + parent.getLineNumber());
					parent = objectiveRepository.findOne(parentId);
					logger.debug("parent LineNumber After: " + parent.getLineNumber());
					maxLineNumber = parent.getLineNumber();
				} else {
					maxLineNumber = objectiveRepository.findMaxLineNumberChildrenOf(parent);
					logger.debug("++++ objectiveRepository.findMaxLineNumberChildrenOf: " + maxLineNumber);
				}
				
				objectiveRepository.insertFiscalyearLineNumberAt(objective.getFiscalYear(), maxLineNumber+allDescendant.size()+1, maxLineNumber+allDescendant.size()+1);
				objective.setLineNumber(maxLineNumber+1);
				
				// now we should set the Line number of all descendant
				objective.calculateAndSetLineNumberForChildren();
				
				// now save all descendant 
				for(Objective descendant : allDescendant) {
					objectiveRepository.save(descendant);
				}
				
			}
			
			
			objective.setParent(parent);
			parent.setIsLeaf(false);
			objectiveRepository.save(parent);
			
		} else if(objective.getType().getId() == ObjectiveTypeId.แผนงาน.getValue()) {
			// this parent  must be root!
			Objective root = objectiveRepository.findRootOfFiscalYear(objective.getFiscalYear());
			objective.setParent(root);
			
			objective.setParentLevel(2);
			objective.setParentPath("." + root.getId().toString() + ".");
			if(objective.getLineNumber() == null) {
				// we'll have to find out this line number  
				Integer maxLineNumber = objectiveRepository.findMaxLineNumberFiscalYear(objective.getFiscalYear());
				if(maxLineNumber != null) {
					objective.setLineNumber(maxLineNumber+1);
				} else {
					objective.setLineNumber(1);
				}
			}
			
		} else{
			// parent is null 
			objective.setParent(null);
			objective.setParentPath(".");
			objective.setParentLevel(1);

			if(objective.getLineNumber() != null) {
				objectiveRepository.removeFiscalyearLineNumberAt(objective.getFiscalYear(), objective.getLineNumber(), allDescendant.size()+1);
				objective.setLineNumber(null);
			}
			
			// now save all descendant 
			for(Objective descendant : allDescendant) {
				descendant.setLineNumber(null);
				objectiveRepository.save(descendant);
			}
			
			
		}
		
		// now reset the isLeaf on Old parent 
		if(oldParent != null) {
			if(oldParent.getChildren().size() == 0) {
				oldParent.setIsLeaf(true);
				objectiveRepository.save(oldParent);
			}
		}
		// and on itself
		if(objective.getChildren() != null) {
			if(objective.getChildren().size() == 0) {
				objective.setIsLeaf(true);
			} else {
				objective.setIsLeaf(false);
			}
		} else {
			objective.setIsLeaf(true);
		}
		
		
		
		//will have to find the maxone and put the increment here!
		if(objective.getCode() == null || objective.getCode().length() == 0) {
			String maxCode = objectiveNameRepository.findMaxCodeOfTypeAndFiscalYear(objective.getType(), objective.getFiscalYear());
			
			Integer nextCode = 0;
			if(maxCode == null) {
				nextCode=10;
			} else {
				nextCode = Integer.parseInt(maxCode) + 1;
			}
		
		
			objective.setCode(nextCode.toString());
			objective.getObjectiveName().setCode(nextCode.toString());
			objective.setIndex(nextCode);
			
		}
		
		
		
		objectiveRepository.save(objective);
		
		// we have to assume to save only the parameter!
		
		// now deal with changes in relations!
		for(JsonNode relation : objectiveJsonNode.get("relations")) {
			// if(relation.getId == null 
			
			if(relation.get("id") == null) {
				if(relation.get("parent") != null) {
					logger.debug("{} ", relation.get("parent"));
					
					if(relation.get("parent").get("id") != null) {
						Long parentId = relation.get("parent").get("id").asLong();
						Objective parent = objectiveRepository.findOne(parentId);
						
						ObjectiveRelations relationJpa = new ObjectiveRelations();
						relationJpa.setObjective(objective);
						relationJpa.setChildType(objective.getType());
						relationJpa.setFiscalYear(objective.getFiscalYear());
						relationJpa.setParent(parent);
						relationJpa.setParentType(parent.getType());
						
						objectiveRelationsRepository.save(relationJpa);
					}
				}
				
			} else {
				ObjectiveRelations relationJpa = objectiveRelationsRepository.findOne(relation.get("id").asLong());
				if(relation.get("parent").get("id") != null) {
					Long parentId = relation.get("parent").get("id").asLong();
					Objective parent = objectiveRepository.findOne(parentId);
					
					relationJpa.setParent(parent);
				//now this will have to only change parent!
				} else {
					logger.debug("*************************************parent is null");
					relationJpa.setParent(null);
				}
				
				objectiveRelationsRepository.save(relationJpa);
			}
			
			
		}
		
		
		return objective;
	}

	@Override
	public Objective newObjectiveWithParam(String name, String code, Long parentId,
			Long typeId, String parentPath, Integer fiscalYear) {
		Objective obj = new Objective();
		obj.setName(name);
		obj.setCode(code);
		obj.setParentPath(parentPath);
		obj.setFiscalYear(fiscalYear);
		
		if(parentId != null) {
			Objective parent = objectiveRepository.findOne(parentId);
			obj.setParent(parent);
			obj.setIndex(parent.getChildren().size());
			
			// now the parent will not be leaf node anymore
			parent.setIsLeaf(false);
			objectiveRepository.save(parent);
		}
		
		ObjectiveType type = objectiveTypeRepository.findOne(typeId);
		
		
		obj.setType(type);
		
		obj.setIsLeaf(true);
		
		return objectiveRepository.save(obj);
	}

	@Override
	public Objective deleteObjective(Long id, Boolean nameCascade) {
		// ok we'll have to get this one first
		Objective obj = objectiveRepository.findOne(id);
		
		//then get its parent
		Objective parent = obj.getParent();
		
		if(parent != null) {
			parent.getChildren().remove(obj);
		
		
			if(parent.getChildren() != null && parent.getChildren().size() == 0) {
				parent.setIsLeaf(true);
				objectiveRepository.save(parent);
			} 
		}
		
		objectiveRelationsRepository.deleteAllObjective(obj);
		
		
		if(nameCascade == true) {
			ObjectiveName name= obj.getObjectiveName();
			obj.setObjectiveName(null);
			objectiveNameRepository.delete(name);
		}
		
		objectiveRepository.delete(obj);
		
		return obj;
	}

	@Override
	public List<ProposalStrategy> findProposalStrategyByBudgetProposal(
			Long budgetProposalId) {
		BudgetProposal budgetProposal = budgetProposalRepository.findOne(budgetProposalId);
		return proposalStrategyRepository.findByProposal(budgetProposal);
	}

	@Override
	public List<ProposalStrategy> findProposalStrategyByFiscalyearAndObjective(
			Integer fiscalYear, Long ownerId, Long objectiveId) {
		List<ProposalStrategy> psList = proposalStrategyRepository.findByObjectiveIdAndfiscalYearAndOwnerId(fiscalYear, ownerId, objectiveId);
		for(ProposalStrategy ps : psList) {
			if(ps.getFormulaStrategy()!=null) {
				ps.getFormulaStrategy().getFormulaColumns().size();
				ps.getRequestColumns().size();
			}
		}
		return psList;
		
	}
	
	@Override
	public List<ProposalStrategy> findAllProposalStrategyByFiscalyearAndObjective(
			Integer fiscalYear, Long objectiveId) {
		return proposalStrategyRepository.findAllByObjectiveIdAndfiscalYearAndOwnerId(fiscalYear, objectiveId);
	}


	
	@Override
	public ProposalStrategy updateProposalStrategy(Long id,
			JsonNode rootNode) throws JsonParseException, JsonMappingException, IOException {

		ProposalStrategy oldPs = proposalStrategyRepository.findOne(id);
		
		ProposalStrategy oldStrategy = ProposalStrategy.copyLongValue(oldPs);
		
		ProposalStrategy ps = createProposalStrategy(rootNode);
		
		
		
		saveProposalStrategy(ps, oldStrategy, ps.getProposal().getId(),
				ps.getFormulaStrategy() == null?null:ps.getFormulaStrategy().getId());
		
		
		return ps;
		
//		ProposalStrategy strategy = proposalStrategyRepository.findOne(id);
//		
//		if(strategy != null) {
//			// now get information from JSON string?
//			
//			Long adjustedAmount = strategy.getTotalCalculatedAmount() - rootNode.get("totalCalculatedAmount").asLong();
//			Long adjustedAmountRequestNext1Year = strategy.getAmountRequestNext1Year()==null?0:strategy.getAmountRequestNext1Year() - rootNode.get("amountRequestNext1Year").asLong();
//			Long adjustedAmountRequestNext2Year = strategy.getAmountRequestNext2Year()==null?0:strategy.getAmountRequestNext2Year() - rootNode.get("amountRequestNext2Year").asLong();
//			Long adjustedAmountRequestNext3Year = strategy.getAmountRequestNext3Year()==null?0:strategy.getAmountRequestNext3Year() - rootNode.get("amountRequestNext3Year").asLong();
//			Long adjustedTargetValue = strategy.getTargetValue()==null?0:strategy.getTargetValue() - rootNode.get("targetValue").asLong();
//			
//			strategy.adjustTotalCalculatedAmount(adjustedAmount);
//			
//			strategy.adjustAmountRequestNext1Year(adjustedAmountRequestNext1Year);
//			strategy.adjustAmountRequestNext2Year(adjustedAmountRequestNext2Year);
//			strategy.adjustAmountRequestNext3Year(adjustedAmountRequestNext3Year);
//			
//			strategy.setTargetValue(rootNode.get("targetValue").asLong());
//			
//			// now looping through the RequestColumns
//			JsonNode requestColumnsArray = rootNode.get("requestColumns");
//			
//			List<RequestColumn> rcList = strategy.getRequestColumns();
//			for(RequestColumn rc : rcList) {
//				Long rcId = rc.getId();
//				// now find this in
//				for(JsonNode rcNode : requestColumnsArray) {
//					if( rcId == rcNode.get("id").asLong()) {
//						//we can just update this one ?
//						rc.setAmount(rcNode.get("amount").asInt());
//						break;
//					}
//				}
//				
//			}
//			
//			proposalStrategyRepository.save(strategy);
//			
//			// now save this budgetProposal
//			BudgetProposal b = strategy.getProposal();
//			b.adjustAmountRequest(adjustedAmount);
//			b.adjustAmountRequestNext1Year(adjustedAmountRequestNext1Year);
//			b.adjustAmountRequestNext2Year(adjustedAmountRequestNext2Year);
//			b.adjustAmountRequestNext3Year(adjustedAmountRequestNext3Year);
//			
//			budgetProposalRepository.save(b);
//			
//			
//			
//			Organization owner = strategy.getProposal().getOwner();
//			
//			BudgetProposal temp = b;
//			// OK we'll go through the amount of this one and it's parent!?
//			while (temp.getForObjective().getParent() != null) {
//				// now we'll get all proposal
//				Objective parent = temp.getForObjective().getParent();
//				temp = budgetProposalRepository.findByForObjectiveAndOwnerAndBudgetType(parent,owner,b.getBudgetType());
//				
//				if(temp!=null) {
//					temp.adjustAmountRequest(adjustedAmount);
//					temp.adjustAmountRequestNext1Year(adjustedAmountRequestNext1Year);
//					temp.adjustAmountRequestNext2Year(adjustedAmountRequestNext2Year);
//					temp.adjustAmountRequestNext3Year(adjustedAmountRequestNext3Year);
//				} else {
//					temp = new BudgetProposal();
//					temp.setForObjective(parent);
//					temp.setOwner(owner);
////					temp.setBudgetType(parent.getBudgetType());
//					temp.setAmountRequest(strategy.getTotalCalculatedAmount());
//					temp.setAmountRequestNext1Year(strategy.getAmountRequestNext1Year());
//					temp.setAmountRequestNext2Year(strategy.getAmountRequestNext2Year());
//					temp.setAmountRequestNext3Year(strategy.getAmountRequestNext3Year());
//				}
//				budgetProposalRepository.save(temp);
//			}
//			
//			
//			
//			
//			
//			return strategy;
//		} else {
//			return null;
//		}
//		
//		
	}

	
	
	/**
	 * ค้นหาข้อมูลการจัดสรรงบประมาณโดยระบุ objective
	 * 
	 * @param objective objective ที่ต้องการค้นหาข้อมูลการจัดสรร
	 * @return	ข้อมูลการจัดสรรงบประมาณทั้งหมด
	 */
	@Override
	public List<AllocationRecord> findAllocationRecordByObjective(Objective objective) {
		List<AllocationRecord> allocList =  allocationRecordRepository.findAlByForObjective(objective);
		
		for(AllocationRecord alloc : allocList) {
			alloc.getForObjective().getId();
			alloc.getBudgetType().getId();
		}
		
		return allocList;
	}
	

	/**
	 * บันทึกข้อมูลการจัดสรรงบประมาณและการจัดสรรให้หน่วยงาน
	 * 
	 * @param allocationRecord ข้อมูลการจัดสรร โดยเป็น JSON ที่ประกอบไปด้วย field
	 * 			{amountAllocated, forObjectiveId, budgetTypeId}
	 * @param proposal ข้อมูลการจัดดสรรงบลงหน่วยงาน เป็น JSON Array ที่ในแต่ละอัน
	 * 			ประกอบไปด้วย {amountAllocated, organizationId}
	 * @return allocationRecord ข้อมูลการจัดสรรของหน่วยงานที่บันทึกลง database แล้ว
	 */
	@Override
	public AllocationRecord saveAllocationRecordWithProposals(
			JsonNode allocationRecord, JsonNode proposals) {
		
		Objective objective = objectiveRepository.findOne(allocationRecord.get("forObjectiveId").asLong());
		BudgetType budgetType = budgetTypeRepository.findOne(allocationRecord.get("budgetTypeId").asLong());
		
		AllocationRecord record;
		
		if(getJsonNodeId(allocationRecord) == null) {
			record = new AllocationRecord();
			record.setBudgetType(budgetType);
			record.setForObjective(objective);
			record.setIndex(0);
			record.setAmountAllocated(0.0);
		} else {
			record = allocationRecordRepository.findOne(getJsonNodeId(allocationRecord));
		}
		
		// The method updateAllocationRecord will set this to the correct amount and save again!
		allocationRecordRepository.save(record);
		
		updateAllocationRecord(record.getId(), allocationRecord);
		
		// now we'll deal with proposals
		
		List<BudgetProposal> proposalsJpa = findBudgetProposalByObjectiveIdAndBudgetTypeId(
				record.getForObjective().getId(), record.getBudgetType().getId());
		
		List<BudgetProposal> toSaveProposal = new ArrayList<BudgetProposal>();
		
		// now we'll do the saving for proposal
		for(JsonNode proposal : proposals) {
			Long orgId = proposal.get("organizationId").asLong();
			Organization owner = organizationRepository.findOne(orgId);
			Boolean found = false;
			
			// now loop through the list 
			for(BudgetProposal proposalJpa : proposalsJpa) {
				Long orgJpaId = proposalJpa.getOwner().getId();
				if(orgJpaId == orgId) {
					// we update this value
					proposalJpa.setAmountAllocated(proposal.get("amountAllocated").asDouble());
					found = true;
					
					toSaveProposal.add(proposalJpa);
					
					break;
				} 
			}
			
			if(found==false) {
				BudgetProposal newProposal = new  BudgetProposal();
				newProposal.setAmountAllocated(proposal.get("amountAllocated").asDouble());
				newProposal.setForObjective(objective);
				newProposal.setBudgetType(budgetType);
				newProposal.setOwner(owner);
				
				toSaveProposal.add(newProposal);
			} 
		}
		
		proposalsJpa.removeAll(toSaveProposal);
		
		budgetProposalRepository.save(toSaveProposal);
		budgetProposalRepository.delete(proposalsJpa);
		
		return record;
	}

	/**
	 * บันทึกข้อมูลการจัดสรรงบประมาณลง Database 
	 * 
	 * @param  data ข้อมูลในรูปแบบ JSON ประกอบไปด้วย 
	 * 				{ 
	 * 				  objective.id,    // Objective 
	 * 				  budgetType.id,   // หมวดงบประมาณ
	 * 	              amountAllocated, // จำนวนเงินที่จัดสรร
	 * 				  round	// รอบของการจัดสรร 
	 * 				}
	 * @return      ข้อมูลการจัดสรรงบประมาณที่บันทึกลง database แล้ว กรณี objective.id == null
	 * 				หรือ budgetType.id == null จะ return null
	 * @see         AllocationRecord
	 */
	@Override
	public AllocationRecord saveAllocationRecord(JsonNode data) {
	
		// now get objective.id
		Long objectiveId = getJsonNodeId(data.get("forObjective"));
		if(objectiveId == null) return null;
		
		// now get budgetType.id
		Long budgetTypeId = getJsonNodeId(data.get("budgetType"));
		if(budgetTypeId == null) return null;
		
		Objective objective = objectiveRepository.findOne(objectiveId);
		BudgetType budgetType = budgetTypeRepository.findOne(budgetTypeId);
		
		
		AllocationRecord record = new AllocationRecord();
		record.setBudgetType(budgetType);
		record.setForObjective(objective);
		record.setAmountAllocated(0.0);
		record.setIndex(data.get("index").asInt());
		
		allocationRecordRepository.save(record);
		
		AllocationRecord returnRecord = updateAllocationRecord(record.getId(), data);
		
		objective.setAllocationRecords(findAllocationRecordByObjective(objective));
		
		return returnRecord;
		
		
		
	}

	/**
	 * ปรับปรุงข้อมูลการจัดสรรงบประมาณลง Database โดย method จะทำการปรับปรุงข้อมูลการจัดสรร
	 * งบประมาณให้กับ parent objective ด้วย 
	 * 
	 * @param  id primary key ของ การจัดสรรงบประมาณที่มีอยู่แล้วใน database
	 * @param  data ข้อมูลในรูปแบบ JSON ประกอบไปด้วย {amountAllocated}
	 * @return      ข้อมูลการจัดสรรงบประมาณที่บันทึกลง database แล้ว กรณี id == null 
	 * 				จะ return null
	 * @see         AllocationRecord
	 */
	@Override
	public AllocationRecord updateAllocationRecord(Long id, JsonNode data) {
		AllocationRecord record = allocationRecordRepository.findOne(id);
		
		// now update the value
		Double amountUpdate = data.get("amountAllocated").asDouble();
		Double oldAmount = record.getAmountAllocated();
		if(oldAmount == null) {
			oldAmount = 0.0;
		}
		Double adjustedAmount = oldAmount - amountUpdate;
		
		Integer index = record.getIndex();
		BudgetType budgetType = record.getBudgetType();
		Objective objective = record.getForObjective();
		
		
		record.setAmountAllocated(amountUpdate);
		allocationRecordRepository.save(record);
		
		objective.setAllocationRecords(findAllocationRecordByObjective(objective));
		
		// now looking back
		Objective parent = objective.getParent();
		while(parent.getParent() != null) {
			logger.debug("parent.id: {}", parent.getId());
			AllocationRecord temp = allocationRecordRepository.findOneByBudgetTypeAndObjectiveAndIndex(budgetType, parent, index);
			
			if(temp == null) {
				temp = new AllocationRecord();
				temp.setAmountAllocated(0.0);
				temp.setBudgetType(budgetType);
				temp.setForObjective(parent);
				temp.setIndex(record.getIndex());
			}
			
			temp.adjustAmountAllocated(adjustedAmount);
			
			allocationRecordRepository.save(temp);
			
			parent = parent.getParent();
			logger.debug("parent.id--: {}", parent.getId());
		}
		
		return record;
	}

	@Override
	public List<BudgetProposal> findBudgetProposalByObjectiveIdAndBudgetTypeId(Long objectiveId, Long budgetTypeId) {
		
		return budgetProposalRepository.findByForObjective_idAndBudgetType_id(objectiveId, budgetTypeId);
	}

	@Override
	public Boolean updateBudgetProposalAndReservedBudget(JsonNode data) {
		//deal with BudgetReseved first
		JsonNode reservedBudgetJson =  data.get("reservedBudget");
		
		Long rbId = reservedBudgetJson.get("id").asLong();
		Long objectiveId = reservedBudgetJson.get("forObjective").get("id").asLong();
		Long budgetTypeId = reservedBudgetJson.get("budgetType").get("id").asLong();
		
		//get Objective first
		Objective currentObj = objectiveRepository.findOne(objectiveId);
		//then BudgetType
		BudgetType currentBudgetType = budgetTypeRepository.findOne(budgetTypeId);
		
		//now find the one
		ReservedBudget rb = reservedBudgetRepository.findOne(rbId);
		
		//get Old value 
		Long oldAmountReserved = rb.getAmountReserved();
		if(oldAmountReserved == null) {
			oldAmountReserved = 0L;
		}
		Long newAmountReserved = reservedBudgetJson.get("amountReserved").asLong();
		Long adjustedAmountReserved = oldAmountReserved - newAmountReserved;
		
		rb.setAmountReserved(newAmountReserved);
		
		//should be OK to save here
		reservedBudgetRepository.save(rb);
		
		List<Long> parentIds = currentObj.getParentIds();
		
		// We are ready to update the parent...
		
		List<ReservedBudget> parentReservedBudgets = reservedBudgetRepository.findAllByObjetiveIds(parentIds, currentBudgetType);
		for(ReservedBudget parentRB : parentReservedBudgets) {
			Long parentOldAmountReserved = parentRB.getAmountReserved();
			
			parentRB.setAmountReserved(parentOldAmountReserved - adjustedAmountReserved);
			// and we can save 'em
			reservedBudgetRepository.save(parentRB);
		}
		
		// now we're updating proposals
		// first get the budgetProposal into Hash
		Map<Long, JsonNode> budgetProposalMap = new HashMap<Long, JsonNode>();
		Map<Long, Double> ownerBudgetProposalAdjustedAllocationMap = new HashMap<Long, Double>();
		Map<Long, JsonNode> requestColumnMap = new HashMap<Long, JsonNode>();
		Map<Long, JsonNode> formulaColumnMap = new HashMap<Long, JsonNode>();
		Map<Long, JsonNode> proposalStrategyMap = new HashMap<Long, JsonNode>();
		for(JsonNode node : data.get("proposals")){
			budgetProposalMap.put(node.get("id").asLong(), node);
			
			for(JsonNode proposalStrategyNode : node.get("proposalStrategies")) {
				proposalStrategyMap.put(proposalStrategyNode.get("id").asLong(), proposalStrategyNode);
				
				for(JsonNode reqeustColumnNode : proposalStrategyNode.get("requestColumns")) {
					requestColumnMap.put(reqeustColumnNode.get("id").asLong(), reqeustColumnNode);
				}
				
				for(JsonNode formulaColumnNode : proposalStrategyNode.get("formulaStrategy").get("formulaColumns")) {
					
					if(!formulaColumnMap.containsKey(formulaColumnNode.get("id").asLong())) {
						formulaColumnMap.put(formulaColumnNode.get("id").asLong(), formulaColumnNode);
					}
				}
			}
			
		}
		
		List<BudgetProposal> proposals = budgetProposalRepository.findAllByForObjectiveAndBudgetType(currentObj, currentBudgetType);
		// ready to loop through and set the owner..
		for(BudgetProposal proposal : proposals) {
			JsonNode node = budgetProposalMap.get(proposal.getId());
			Double oldAmount = proposal.getAmountAllocated() == null ? 0.0: proposal.getAmountAllocated();
			Double newAmount = node.get("amountAllocated").asDouble();
			proposal.setAmountAllocated(newAmount);
			
			ownerBudgetProposalAdjustedAllocationMap.put(proposal.getOwner().getId(), oldAmount-newAmount);
			logger.debug("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++adjusted BudgetProposal: {} " , oldAmount - newAmount);
			budgetProposalRepository.save(proposal);
			
		}
		
		//now update the parents
		List<BudgetProposal> parentProposals = budgetProposalRepository.findAllByForObjectiveIdsAndBudgetType(parentIds, currentBudgetType);
		for(BudgetProposal parentProposal:  parentProposals) {
			Double adjustedAmount = ownerBudgetProposalAdjustedAllocationMap.get(parentProposal.getOwner().getId());
			
			if(parentProposal.getAmountAllocated() != null ) {
				parentProposal.setAmountAllocated(parentProposal.getAmountAllocated() - adjustedAmount);
			} else {
				parentProposal.setAmountAllocated(0.0 - adjustedAmount);
			}
			
			budgetProposalRepository.save(parentProposal);
		}
		
		//last thing is to update formularStrategy & RequestColumns!
		
		// let's get the easy one first, request columns
		
		for(JsonNode rcNode : requestColumnMap.values()) {
			Long rcid = rcNode.get("id").asLong();
			RequestColumn rc = requestColumnRepositories.findOne(rcid);
			rc.setAllocatedAmount(rcNode.get("allocatedAmount").asInt());
			
			
			logger.debug("saving... rc.id {} with allocatedAmount {}", rc.getId(), rcNode.get("allocatedAmount").asInt());
			requestColumnRepositories.save(rc);
		}
		
		for(JsonNode fcNode : formulaColumnMap.values()) {
			Long fcid = fcNode.get("id").asLong();
			FormulaColumn fc = formulaColumnRepository.findOne(fcid);
			fc.setAllocatedValue(fcNode.get("allocatedValue").asLong());
			
			formulaColumnRepository.save(fc);
		}
		
		for(JsonNode psNode : proposalStrategyMap.values()) {
			Long psid = psNode.get("id").asLong();
			ProposalStrategy ps = proposalStrategyRepository.findOne(psid);
			ps.setTotalCalculatedAllocatedAmount(psNode.get("totalCalculatedAllocatedAmount").asLong());
			
			proposalStrategyRepository.save(ps);					
		}
		
		return true;
	}

	@Override
	public List<TargetUnit> findAllTargetUnits() {
		return (List<TargetUnit>) targetUnitRepository.findAllSortedByName();
	}

	@Override
	public Page<TargetUnit> findAllTargetUnits(PageRequest pageRequest) {
		return (Page<TargetUnit>) targetUnitRepository.findAll(pageRequest);
	}

	@Override
	public List<ObjectiveTarget> findAllObjectiveTargets() {
		return (List<ObjectiveTarget>) objectiveTargetRepository.findAll();
	}

	@Override
	public TargetUnit saveTargetUnits(TargetUnit targetUnit) {
		return targetUnitRepository.save(targetUnit);
	}

	@Override
	public TargetUnit updateTargetUnit(TargetUnit targetUnit) {
		TargetUnit targetUnitJPA = targetUnitRepository.findOne(targetUnit.getId());
		if(targetUnitJPA != null) {
			targetUnitJPA.setName(targetUnit.getName());
			
			targetUnitRepository.save(targetUnitJPA);
			return targetUnitJPA;
		}
		return null;
	}

	@Override
	public TargetUnit deleteTargetUnit(TargetUnit targetUnit) {
		TargetUnit targetUnitJPA = targetUnitRepository.findOne(targetUnit.getId());
		if(targetUnitJPA != null) {
			targetUnitRepository.delete(targetUnitJPA);
			
			return targetUnitJPA;
		}
		return null;	}

	@Override
	public ObjectiveTarget saveObjectiveTarget(ObjectiveTarget objectiveTarget) {
		return objectiveTargetRepository.save(objectiveTarget);
	}

	@Override
	public ObjectiveTarget updateObjectiveTarget(
			ObjectiveTarget objectiveTarget) {
		ObjectiveTarget objectiveTargetJPA = objectiveTargetRepository.findOne(objectiveTarget.getId());
		if(objectiveTargetJPA != null) {
			objectiveTargetJPA.setName(objectiveTarget.getName());
			
			objectiveTargetRepository.save(objectiveTargetJPA);
			return objectiveTargetJPA;
		}
		return null;
	}

	@Override
	public ObjectiveTarget deleteObjectiveTarget(
			ObjectiveTarget objectiveTarget) {
		ObjectiveTarget objectiveTargetJPA = objectiveTargetRepository.findOne(objectiveTarget.getId());
		if(objectiveTargetJPA != null) {
			
			objectiveTargetRepository.delete(objectiveTargetJPA);
			return objectiveTargetJPA;
		}
		return null;
	}

	@Override
	public TargetUnit findOneTargetUnit(Long id) {
		return targetUnitRepository.findOne(id);
	}

	@Override
	public ObjectiveTarget findOneObjectiveTarget(Long id) {
		return objectiveTargetRepository.findOne(id);
	}

	@Override
	public ObjectiveTarget updateObjectiveTarget(JsonNode node) {
		ObjectiveTarget ot = findOneObjectiveTarget(node.get("id").asLong());
		// now filling in only what we need!
		if(ot!=null) {
			ot.setName(node.get("name").asText());
			ot.setIsSumable(node.get("isSumable").asBoolean());
			
			TargetUnit tu = findOneTargetUnit(node.get("unit").get("id").asLong());
			if(tu != null) {
				ot.setUnit(tu);
			}
			
			return saveObjectiveTarget(ot);
		} 
		 return null;
	}

	@Override
	public ObjectiveTarget saveObjectiveTarget(JsonNode node) {
		ObjectiveTarget ot = new ObjectiveTarget();
		// now filling in only what we need!

		ot.setName(node.get("name").asText());
		ot.setIsSumable(node.get("isSumable").asBoolean());
		ot.setFiscalYear(node.get("fiscalYear").asInt());
		
		TargetUnit tu = findOneTargetUnit(node.get("unit").get("id").asLong());
		if(tu != null) {
			ot.setUnit(tu);
		}
		
		return saveObjectiveTarget(ot);
	}

	@Override
	public ObjectiveTarget deleteObjectiveTarget(Long id) {
		ObjectiveTarget objectiveTarget = objectiveTargetRepository.findOne(id);
		return deleteObjectiveTarget(objectiveTarget);
	}

	@Override
	public TargetUnit updateTargetUnit(JsonNode node) {
		TargetUnit tu = findOneTargetUnit(node.get("id").asLong());
		// now filling in only what we need!
		if(tu!=null) {
			tu.setName(node.get("name").asText());
	
			
			return saveTargetUnits(tu);
		} 
		 return null;
	}

	@Override
	public TargetUnit saveTargetUnit(JsonNode node) {
		TargetUnit tu = new TargetUnit();
		tu.setName(node.get("name").asText());
		return saveTargetUnits(tu);
	}

	@Override
	public TargetUnit deleteTargetUnit(Long id) {
		TargetUnit targetUnit = findOneTargetUnit(id);
		
		return deleteTargetUnit(targetUnit);
	}

	@Override
	public List<ObjectiveTarget> findAllObjectiveTargetsByFiscalyear(Integer fiscalYear) {
		return objectiveTargetRepository.findAllByFiscalYear(fiscalYear);
	}

	@Override
	public void addTargetToObjective(Long id, Long targetId) {
		Objective o = objectiveRepository.findOne(id);
		ObjectiveTarget ot = objectiveTargetRepository.findOne(targetId);
		
		if(o.getTargets().lastIndexOf(ot) >= 0) {
			// we should be save to return here?
			return;
		} else {
			
			ObjectiveTarget otJPA = null;
			if(o.getTargets().size() > 0) {
				otJPA = o.getTargets().get(0);
			}

			// remove the one we have first
			if(otJPA != null) {
				o.getTargets().remove(otJPA);
			} 
			
			o.addTarget(ot);
			
			// and we should be save this one
			objectiveRepository.save(o);
			
			// now go on to its parents;
			List<Objective> parents = objectiveRepository.findAllObjectiveByIds(
					o.getParentIds());
			
			for(Objective parent: parents) {
				
				// we'll have to somehow take out the old one out too
				if(otJPA != null) {
					
					
					List<ObjectiveTarget> otList = findObjectiveTargetForChildrenObjective(parent.getId(), otJPA.getId());
					if(otList.size() == 0) {
						// now we can take the old out
						parent.getTargets().remove(otJPA);
					}
				}					

				if(parent.addTarget(ot)) {
					objectiveRepository.save(parent);
				}

				
			}
		}
		
	}

	private List<ObjectiveTarget> findObjectiveTargetForChildrenObjective(
			Long objectiveId, Long targetId) {
		
		logger.debug("targetId: {} ",  targetId);
		logger.debug("objectiveIdLike: {}", "%."+objectiveId+"%");
		
		return objectiveTargetRepository.findAllByIdAndChildrenOfObjectiveId(targetId, "%."+objectiveId+"%");
	}

	@Override
	public TargetValue saveTargetValue(JsonNode node, Organization workAt) throws Exception {
		Long targetValueId = null;
		if(node.get("id") != null) {
			targetValueId = node.get("id").asLong();
		}
		
		Long forObjectiveId = node.get("forObjective").get("id").asLong();
		Objective obj = objectiveRepository.findOne(forObjectiveId);

		Long objectiveTargetId = node.get("target").get("id").asLong();
		ObjectiveTarget target = objectiveTargetRepository.findOne(objectiveTargetId);
		
		Long adjustedRequestedValue = 0L;
		Long requestedValue = node.get("requestedValue").asLong();
		
		TargetValue tv;
		if(targetValueId == null) {
			tv = new TargetValue();
			tv.setOwner(workAt);
			tv.setForObjective(obj);
			tv.setTarget(target);
			
			
		} else {
			tv = targetValueRepository.findOne(targetValueId);
			tv.setOwner(workAt);
			adjustedRequestedValue = tv.getRequestedValue();
			
		}
		
		tv.setRequestedValue(node.get("requestedValue").asLong());
		adjustedRequestedValue -= requestedValue;
		targetValueRepository.save(tv);
		
		if(target.getIsSumable() == true ) {
		
			logger.debug("---------------------------------parents : " + obj.getParentIds());
			
			// now loop for parent
			Objective parent = obj.getParent();
			while(parent!=null) {
				
				// now get the matching target
				ObjectiveTarget matchingTarget = null;
				// now find the matching unit
				for(ObjectiveTarget t : parent.getTargets()) {
					if(t.getUnit().getId() == target.getUnit().getId()) {
						matchingTarget = t;
					}
				}
				
				if(matchingTarget == null) {
					break;
				}
		
				
				// now we'll find the matching value
				List<TargetValue> tvs = targetValueRepository.findAllByOnwerIdAndTargetUnitIdAndObjectiveId(
						workAt.getId(), matchingTarget.getUnit().getId(), parent.getId());
				
				if(tvs.size() == 0) {
					//crate a new TargetValue
					TargetValue newTv = new TargetValue();
					newTv.setTarget(matchingTarget);
					newTv.setForObjective(parent);
					newTv.setOwner(workAt);
					newTv.setRequestedValue(requestedValue);
					
					logger.debug("---------adding new tv with target.id: {}, requestedValue : {}",  matchingTarget.getId(), requestedValue);
					targetValueRepository.save(newTv);
					
				} else if (tvs.size() == 1) {
					TargetValue matchTv = tvs.get(0);
					logger.debug("---------updating tv with adjustedReqeust : {}",  adjustedRequestedValue);
					logger.debug("----------oldone is {}", matchTv.getRequestedValue());
					matchTv.adjustRequestedValue(adjustedRequestedValue);
					logger.debug("----------newone is {}", matchTv.getRequestedValue());
					
					targetValueRepository.save(matchTv);
				}
				
				logger.debug("-------------------------------parents: " + parent.getId());
				
				logger.debug("matchingTarget == null {} ", matchingTarget == null);
				
				logger.debug("matchingTarget.getIsSumable == null {} ", matchingTarget.getIsSumable() == null);
				logger.debug("matchingTarget.getIsSumable {} ", matchingTarget.getIsSumable());
				
				
				if(matchingTarget == null || matchingTarget.getIsSumable() == null || matchingTarget.getIsSumable() == false) {
					logger.debug("******not found");
					break;
				}
				parent = parent.getParent();
			}
		}
		return tv;
	}

	@Override
	public TargetValueAllocationRecord saveTargetValueAllocationRecord(JsonNode node,
			Organization workAt) {
		Long tvarId = null;
		if(node.get("id") != null) {
			tvarId = node.get("id").asLong();
		}
		
		Long forObjectiveId = node.get("forObjective").get("id").asLong();
		Objective obj = objectiveRepository.findOne(forObjectiveId);

		Long objectiveTargetId = node.get("target").get("id").asLong();
		ObjectiveTarget target = objectiveTargetRepository.findOne(objectiveTargetId);
		
		Long adjustedRequestedValue = 0L;
		Long requestedValue = node.get("amountAllocated").asLong();
		
		TargetValueAllocationRecord tvar;
		tvar = targetValueAllocationRecordRepository.findOne(tvarId);
		
		adjustedRequestedValue = tvar.getAmountAllocated();
			
	
		
		tvar.setAmountAllocated(requestedValue);
		adjustedRequestedValue -= requestedValue;
		targetValueAllocationRecordRepository.save(tvar);
		
		
		for(Objective parent : objectiveRepository.findAllObjectiveByIds(obj.getParentIds())) {
			TargetValueAllocationRecord parentTvar = targetValueAllocationRecordRepository.findOneByIndexAndForObjectiveAndTarget(tvar.getIndex(), parent, tvar.getTarget());
			
			
			parentTvar.adjustAmountAllocated(adjustedRequestedValue);
			
			targetValueAllocationRecordRepository.save(parentTvar);			
			
		}
		
		return tvar;
	}

	@Override
	public void saveLotsTargetValue(JsonNode node) {
		for(JsonNode n: node) {
			
			
			Long id = n.get("id").asLong();
			logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++ {} ", id);
			
			
			
			TargetValue tv = targetValueRepository.findOne(id);
			
			Long oldAmount = tv.getAllocatedValue();
			if(oldAmount == null) {
				oldAmount = 0L;
			}
			
			tv.setAllocatedValue(n.get("allocatedValue").asLong());
			
			Long newAmout = tv.getAllocatedValue();
			Long adjustedRequestedValue = oldAmount-newAmout;
			
			
			targetValueRepository.save(tv);
			
			List<TargetValue> tvs = targetValueRepository
				.findAllByOnwerIdAndObjectiveIdIn(
						tv.getOwner().getId(), tv.getTarget().getId(),  tv.getForObjective().getParentIds());

			
			for(TargetValue parentTv: tvs) {
				parentTv.adjustAllocatedValue(adjustedRequestedValue);
				
				targetValueRepository.save(parentTv);
			}
			
			//now ineach tv has to go get the parents?
		}

		
	}
	
	
	/**
	 * ค้นหา root objective ของปีงบประมาณล่าสุด
	 */
	@Override
	public Objective findRootMaxFiscalYear() {
		// TODO Auto-generated method stub
		Integer fy = objectiveRepository.findMaxFiscalYear();
		Objective o = objectiveRepository.findRootOfFiscalYear(fy);
		
		return o;
	}

	/** ค้นหา Objective ด้วยผู้รับผิดชอบและปีงบประมาณ
	 * 
	 */
	public List<Objective> findObjectiveByOwnerAndFiscalYear(
			Organization workAt, Integer fiscalYear){
		Organization org = organizationRepository.findOne(workAt.getId());
		
		logger.debug(org.getAbbr());
		
		List<Objective> returnList = new ArrayList<Objective>();
		
		
		if(org.getType() == OrganizationType.ฝ่าย  || org.getType() == OrganizationType.จังหวัด ) {
		
			List<Objective> list = objectiveRepository.findAllByOwnerAndfiscalYear(org, fiscalYear);
			List<Objective> list2 = objectiveRepository.findAllByActivityOwnerAndFiscalYear(org, fiscalYear);
	
			for(Objective obj : list) {
				if(!returnList.contains(obj)) {
					 returnList.add(obj);
				}
			}
	
			for(Objective obj : list2) {
				if(!returnList.contains(obj)) {
					 returnList.add(obj);
				}
			}

		} else if(org.getType() == OrganizationType.ส่วน) {
			List<Objective> list = objectiveRepository.findAllByActivityRegulatorAndFiscalYear(org, fiscalYear);
			for(Objective obj : list) {
				if(!returnList.contains(obj)) {
					returnList.add(obj);
				}
			}
			
		}
		
		
		
		Collections.sort(returnList, new Comparator<Objective>() {

			@Override
			public int compare(Objective o1, Objective o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
			
		});
		
		Organization searhOrg = org;
		if(org.getType() == OrganizationType.ส่วน) {
			searhOrg = org.getParent();
		}
		
		for(Objective obj: returnList) {
			
			List<BudgetProposal> proposals = 
					budgetProposalRepository.findByForObjectiveAndOwner(obj, searhOrg);
			
			obj.setFilterProposals(proposals);
		}
		
		return returnList;
		
	}
	
	
	
	@Override
	public List<Objective> findObjectiveByActivityOwnerAndFiscalYear(
			Organization workAt, Integer fiscalYear) {
		
		Organization searchOrg = workAt;
		if(workAt.getType() == OrganizationType.แผนกในจังหวัด) {
			searchOrg = workAt.getParent();
		}
		
		// now if workAt is at แผนก 
//		if(workAt.isSubSection()) {
//			searchOrg = workAt.getParent();
//		}
		
		return objectiveRepository.findAllByActivityOwnerAndFiscalYear(searchOrg, fiscalYear);
	}
	
	@Override
	public List<Objective> findObjectiveByActivityRegulatorAndFiscalYear(
			Organization workAt, Integer fiscalYear) {
		return objectiveRepository.findAllByActivityRegulatorAndFiscalYear(workAt, fiscalYear);
	}

	@Override
	public List<Objective> findObjectiveChildrenByActivityOwnerAndParentId(
			Organization workAt, Long id) {
		return objectiveRepository.findAllChildrenByActivityOwnerAndPanrentId(workAt,id);
	}

	@Override
	public List<Objective> findObjectiveChildrenByActivityTargetOwnerAndObjectiveParentId(
			Organization workAt, Long id) {
		
		return objectiveRepository.findAllChildrenByActivityOwnerAndPanrentId(workAt, id);
	}
	
	
	
	@Override
	public List<Objective> findObjectiveChildrenByActivityRegulatorAndParentId(
			Organization workAt, Long id) {
		return objectiveRepository.findAllChildrenByActivityRegulatorAndParentId(workAt, id);
	}

	/**
	 * ค้นหา  Objective ด้วยปีงบประมาณ และ ชนิด โดยกำหนด PageRequest และผ่านค่า 
	 * คำที่ต้องการค้นหาเบื้องต้น
	 * 
	 *  
	 */
	@Override
	public Page<Objective> findObjectivesByFiscalyearAndTypeId(
			Integer fiscalYear, Long typeId,
			String query,   Pageable pageable) {
		
		logger.debug("++++ query: " + query);
		
		Page<Objective> page = objectiveRepository.findByFiscalYearAndType_Id(fiscalYear, typeId, query, pageable);
		for(Objective obj : page.getContent()) {
			obj.getTargets().size();
			if(obj.getType().getParent() != null) {
				obj.getType().getParent().getName();
				if(obj.getParent() != null) {
					obj.getParent().getName();
					logger.debug(" +++++++++++++++++++++++++++++++++++++++++  {} ",obj.getParent().getName() );
				}
			}
			obj.getUnits().size();
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + obj.getTargets().size());
			obj.getTargets().size();
		}
		return page;
	}

	@Override
	public List<Objective> findObjectivesByFiscalyearAndTypeId(
			Integer fiscalYear, Long typeId) {
		List<Objective> objs = objectiveRepository.findAllByFiscalYearAndType_id(fiscalYear, typeId);
		for(Objective obj : objs){
			obj.getTargets().size();
			if(obj.getType().getParent() != null) {
				obj.getType().getParent().getName();
			}
		}
		
		return objs;
	}
	
	@Override
	public Page<Objective> findObjectivesByFiscalyearAndTypeId(
			Integer fiscalYear, Long typeId, Pageable pageable) {
		
		Page<Objective> page = objectiveRepository.findPageByFiscalYearAndType_id(fiscalYear, typeId, pageable);
		for(Objective obj : page.getContent()) {
			obj.getTargets().size();
			if(obj.getType().getParent() != null) {
				obj.getType().getParent().getName();
				if(obj.getParent() != null) {
					obj.getParent().getName();
					logger.debug(" +++++++++++++++++++++++++++++++++++++++++  {} ",obj.getParent().getName() );
				}
			}
			obj.getUnits().size();
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + obj.getTargets().size());
			obj.getTargets().size();
		}
		
		return page;
	}
	

	@Override
	public Objective updateObjectiveParent(Long id, Long parentId) {
		Objective o = objectiveRepository.findOne(id);
		String oldParentPath = "." +  o.getId().toString() +  o.getParentPath();
		
		Objective parent = objectiveRepository.findOne(parentId);
		
		if(o != null && parent != null) {
			o.setParent(parent);
			if(parent.getParentPath()==null) {
				o.setParentPath("."+parent.getId()+".");
			} else {
				o.setParentPath("."+parent.getId()+parent.getParentPath());
			}
			objectiveRepository.save(o);
			
			
			// now every child with old o parentpath will have to be updated
			logger.debug(oldParentPath);
			
			List<Objective> children = objectiveRepository
					.findAllByFiscalYearAndParentPathLike(o.getFiscalYear(), "%"+oldParentPath);
			
			for(Objective child : children) {
				logger.debug("-------" + child.getId().toString() + " old ParentPath : " + child.getParentPath());
				String oldString = child.getParentPath();
				child.setParentPath(oldString.replace(oldParentPath, "." +  o.getId().toString() +o.getParentPath()));
				logger.debug("-------" + child.getId().toString() + " new ParentPath : " + child.getParentPath());
				objectiveRepository.save(child);
			}
			
			
			return o;
		}
		return null;
	}

	@Override
	public Objective objectiveAddReplaceUnit(Long id, Long unitId) {
		Objective o = objectiveRepository.findOne(id);
		TargetUnit unit = targetUnitRepository.findOne(unitId);
		
		if(o != null && unit != null ) {
			o.addReplaceUnit(unit);
		}
		objectiveRepository.save(o);
		return o;
	}

	@Override
	public List<ObjectiveRelationsRepository> findObjectiveRelationsByFiscalYearAndChildTypeRelation(
			Integer fiscalYear, Long childTypeId) {
		
		ObjectiveType childType = objectiveTypeRepository.findOne(childTypeId);
		
		return objectiveRelationsRepository.findAllByFiscalYearAndChildType(fiscalYear, childType);
	}

	@Override
	public List<ObjectiveRelationsRepository> findObjectiveRelationsByFiscalYearAndChildTypeRelationWithObjectiveIds(
			Integer fiscalYear, Long childTypeId, List<Long> ids) {
		
		ObjectiveType childType = objectiveTypeRepository.findOne(childTypeId);
		return objectiveRelationsRepository.findAllByFiscalYearAndChildTypeWithIds(fiscalYear, childType, ids);
	}
	
	@Override
	public ObjectiveRelations saveObjectiveRelations(JsonNode relation) {
		
		Long objectiveId = relation.get("objective").get("id").asLong();
		Long parentId = relation.get("parent").get("id").asLong();
		Integer fiscalYear = relation.get("fiscalYear").asInt();
		
		Objective objective = objectiveRepository.findOne(objectiveId);
		if(objective.getParent() != null) {
			objective.getParent().getName();
		}
		Objective parent = objectiveRepository.findOne(parentId);
		
		ObjectiveRelations relationJpa = new ObjectiveRelations();
		
		relationJpa.setFiscalYear(fiscalYear);
		relationJpa.setObjective(objective);
		relationJpa.setChildType(objective.getType());
		
		relationJpa.setParent(parent);
		relationJpa.setParentType(parent.getType());
		relationJpa.getObjective().getUnits().size();
		
		objectiveRelationsRepository.save(relationJpa);
		
		return relationJpa;
	}

	@Override
	public ObjectiveRelations updateObjectiveRelations(Long id,
			JsonNode relation) {
		ObjectiveRelations relationJpa = objectiveRelationsRepository.findOne(id);
		Long parentId = relation.get("parent").get("id").asLong();
		Objective parent = objectiveRepository.findOne(parentId);
		
		relationJpa.setParent(parent);
		relationJpa.setParentType(parent.getType());
		
		relationJpa.getObjective().getParent();
		
		if(relationJpa.getObjective().getParent() != null) {
			relationJpa.getObjective().getParent().getName();
		}
		
		relationJpa.getObjective().getUnits().size();
		
		objectiveRelationsRepository.save(relationJpa);
		
		return relationJpa;
	}

	@Override
	public String initFiscalYear(Integer fiscalYear) {
		
		
		Objective obj = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		
		if(obj == null) {
			ObjectiveType rootType = objectiveTypeRepository.findOne(ObjectiveTypeId.ROOT.getValue());
			
			ObjectiveName objName = new ObjectiveName();
			objName.setName("ROOT");
			objName.setFiscalYear(fiscalYear);
			objName.setType(rootType);
			
			obj = new Objective();
			obj.setName("ROOT");
			obj.setParent(null);
			obj.setParentPath(".");
			obj.setParentLevel(1);
			obj.setFiscalYear(fiscalYear);
			obj.setObjectiveName(objName);
			
			
			obj.setType(rootType);
			
			objectiveNameRepository.save(objName);
			objectiveRepository.save(obj);
			
			
		}
		
		// now init fiscalBudgetType
		logger.debug("initfiscalBudgetType");
		initFiscalBudgetType(fiscalYear);
		
		return "success";
	}

	@Override
	public String mappedUnit() {
		Iterable<Objective> list = objectiveRepository.findAll();
		for(Objective o : list) {
			if(o.getUnits() != null) {
				for(TargetUnit u : o.getUnits()) {
					ObjectiveTarget t = new ObjectiveTarget();
					t.setUnit(u);
					t.setFiscalYear(o.getFiscalYear());
					
					objectiveTargetRepository.save(t);
					
					o.addTarget(t);
					
					objectiveRepository.save(o);
				}
				
				
			}
		}
		
		return "success";
	}

	@Override
	public ObjectiveTarget addUnitToObjective(Long objectiveId, Long unitId,
			Integer isSumable) {
		Objective o = objectiveRepository.findOne(objectiveId);
		TargetUnit u = targetUnitRepository.findOne(unitId);
		
		ObjectiveTarget t = new ObjectiveTarget();
		t.setUnit(u);
		
		if(isSumable == 1 ) {
			t.setIsSumable(true);
		} else { 
			t.setIsSumable(false);
		}
		t.setFiscalYear(o.getFiscalYear());
		
		objectiveTargetRepository.save(t);
		
		// now save t
		o.addTarget(t);
		
		objectiveRepository.save(o);
		
		
		return t;
	}

	@Override
	public String removeUnitFromObjective(Long objectiveId,
			Long targetId) {
		Objective o = objectiveRepository.findOne(objectiveId);
		ObjectiveTarget t= objectiveTargetRepository.findOne(targetId);
		

		
		o.getTargets().remove(t);
		o.getObjectiveName().getTargets().remove(t);
		
		t.setUnit(null);
		
		
		objectiveTargetRepository.delete(t);
				
		
		return "success";
	}
	
	@Override
	public ObjectiveTarget addUnitToObjectiveName(Long id, Long unitId,
			Integer isSumable) {
		ObjectiveName o = objectiveNameRepository.findOne(id);
		TargetUnit u = targetUnitRepository.findOne(unitId);
		
		ObjectiveTarget t = new ObjectiveTarget();
		t.setUnit(u);
		
		if(isSumable == 1 ) {
			t.setIsSumable(true);
		} else { 
			t.setIsSumable(false);
		}
		t.setFiscalYear(o.getFiscalYear());
		
		objectiveTargetRepository.save(t);
		
		// now save t
		o.addTarget(t);
		
		objectiveNameRepository.save(o);
		
		
		return t;
	}

	@Override
	public String removeUnitFromObjectiveName(Long id, Long targetId) {
		ObjectiveName o = objectiveNameRepository.findOne(id);
		ObjectiveTarget t= objectiveTargetRepository.findOne(targetId);
		
		o.getTargets().remove(t);
		
		t.setUnit(null);
				
		objectiveTargetRepository.delete(t);
		return "success";
	}

	@Override
	public List<BudgetCommonType> findAllBudgetCommonTypes(Integer fiscalYear) {
		
		return budgetCommonTypeRepository.findAllByFiscalYear(fiscalYear);
	}

	@Override
	public BudgetCommonType findOneBudgetCommonType(Long id) {

		return budgetCommonTypeRepository.findOne(id);
	}

	@Override
	public BudgetCommonType updateBudgetCommonType(JsonNode node) {
		Long id = null;
		if(node.get("id") == null) {
			return null;
		}
		
		id = node.get("id").asLong();
		BudgetCommonType bct = budgetCommonTypeRepository.findOne(id);
		
		bct.setName(node.get("name").asText());
		
		return budgetCommonTypeRepository.save(bct);
	}

	@Override
	public BudgetCommonType saveBudgetCommonType(JsonNode node) {
		BudgetCommonType bct = new BudgetCommonType();
		
		// now set 
		if(node.get("code") != null)  {
			bct.setCode(node.get("code").asText());
		}
		bct.setFiscalYear(node.get("fiscalYear").asInt());
		bct.setName(node.get("name").asText());
		
		budgetCommonTypeRepository.save(bct);
		
		bct.setCode(bct.getId().toString());
		
		budgetCommonTypeRepository.save(bct);
		
		return bct;
		
	}

	@Override
	public BudgetCommonType deleteBudgetCommonType(Long id) {
		BudgetCommonType bct = budgetCommonTypeRepository.findOne(id);
		
		if(bct != null) {
			budgetCommonTypeRepository.delete(bct);
		}
		
		return bct;
	}

	@Override
	public List<ObjectiveBudgetProposal> findObjectiveBudgetproposalByObjectiveIdAndOwnerId(
			Long objectiveId, Long ownerId) {
		
		return objectiveBudgetProposalRepository.findAllByForObjective_IdAndOwner_Id(objectiveId, ownerId);
	}

	@Override
	public List<FiscalBudgetType> findAllFiscalBudgetTypeByFiscalYear(
			Integer fiscalYear) {
		return fiscalBudgetTypeRepository.findAllByFiscalYear(fiscalYear);
	}
	
	@Override
	public List<FiscalBudgetType> findAllFiscalBudgetTypeByFiscalYearUpToLevel(
			Integer fiscalYear, Integer level) {
		// TODO Auto-generated method stub
		return fiscalBudgetTypeRepository.findAllByFiscalYearUpToLevel(fiscalYear, level);
	}


	@Override
	public String updateFiscalBudgetTypeIsMainBudget(Integer fiscalYear, List<Long> idList) {
		fiscalBudgetTypeRepository.setALLIsMainBudgetToFALSE(fiscalYear);
		
		if(idList.size() > 0) {
			fiscalBudgetTypeRepository.setIsMainBudget(fiscalYear, idList);
		}
		
		return "success";
	}

	@Override
	public ObjectiveBudgetProposal saveObjectiveBudgetProposal(
			Organization workAt, JsonNode node) {

		ObjectiveBudgetProposal obp;
		
		if(getJsonNodeId(node) != null) {
			obp = objectiveBudgetProposalRepository.findOne(getJsonNodeId(node));
			
		} else {
			obp = new ObjectiveBudgetProposal();
			// now for Each targetValue we'll have to init one here
			if(node.get("targets") != null) {
				
				obp.setTargets(new ArrayList<ObjectiveBudgetProposalTarget>());
				for(JsonNode target : node.get("targets")){
					ObjectiveBudgetProposalTarget targetJPA = new ObjectiveBudgetProposalTarget();
					targetJPA.setUnit(targetUnitRepository.findOne(getJsonNodeId(target.get("unit"))));
					targetJPA.setObjectiveBudgetProposal(obp);
					obp.getTargets().add(targetJPA);
					
				}
				
			}
		}
		ObjectiveBudgetProposal obpOldValue = new ObjectiveBudgetProposal();
		obpOldValue.copyValue(obp);
		
		if(node.get("targets") != null) {
			for(JsonNode target : node.get("targets")){
				logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>JSON_NODE_UNIT_ID" + getJsonNodeId(node.get("unit")));
				for(ObjectiveBudgetProposalTarget targetJpa : obp.getTargets()) {
					logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<TARGETJPA_UNIT_ID" + targetJpa.getUnit().getId());
					if(targetJpa.getUnit().getId() == getJsonNodeId(target.get("unit"))) {
						targetJpa.setTargetValue(target.get("targetValue").asLong());
						break;
					}
				}
			}	
		}
		
		obp.setOwner(workAt);
		
		BudgetType type = null;
		
		if(node.get("budgetType") !=null ) {
			if(node.get("budgetType").get("id") != null) {
				type = budgetTypeRepository.findOne(node.get("budgetType").get("id").asLong());
			}
		}
		
		Objective objective= null;
		if(node.get("forObjective") !=null ) {
			if(node.get("forObjective").get("id") != null) {
				objective = objectiveRepository.findOne(node.get("forObjective").get("id").asLong());
			}
		}
		
		if(type == null) {
			return null;
		}
		
		if(objective == null) {
			return null;
		}
		
		obp.setBudgetType(type);
		obp.setForObjective(objective);
		
		if(node.get("amountRequest")!=null ) {
			obp.setAmountRequest(node.get("amountRequest").asLong());
		}
		
		if(node.get("amountRequestNext1Year")!=null ) {
			obp.setAmountRequestNext1Year(node.get("amountRequestNext1Year").asLong());
		}
		
		if(node.get("amountRequestNext2Year")!=null ) {
			obp.setAmountRequestNext2Year(node.get("amountRequestNext2Year").asLong());
		}
		
		if(node.get("amountRequestNext3Year")!=null ) {
			obp.setAmountRequestNext3Year(node.get("amountRequestNext3Year").asLong());
		}
		
		objectiveBudgetProposalRepository.save(obp);
		List<ObjectiveBudgetProposal> obpList = objectiveBudgetProposalRepository.findAllByForObjective_IdAndOwner_Id(obp.getForObjective().getId(), workAt.getId());
		obp.getForObjective().setFilterObjectiveBudgetProposals(obpList);
		
		//now before return back we'll update the parents
		Objective o = obp.getForObjective().getParent();
		BudgetType budgetType = obp.getBudgetType();
		while(o != null) {
			ObjectiveBudgetProposal obpParent = objectiveBudgetProposalRepository.findByForObjectiveAndOwnerAndBudgetType(o, workAt, budgetType);
			if(obpParent == null) {
				obpParent= new ObjectiveBudgetProposal();
				obpParent.setForObjective(o);
				obpParent.setBudgetType(budgetType);
				obpParent.setOwner(workAt);
			}
			
			// now we set the obp to this one!
			obpParent.adjustAmount(obp, obpOldValue);
			
			objectiveBudgetProposalRepository.save(obpParent);
			
			for(ObjectiveBudgetProposalTarget tt: obpParent.getTargets()) {
				logger.debug("-------------->>>>>>>>>>>> " + tt.getTargetValue());
			}
			
			o = o.getParent();
		}
		
		obp.getForObjective().getTargets().size();
		
		return obp;
	}

	@Override
	public ObjectiveBudgetProposal deleteObjectiveBudgetProposal(Long id) {
		
		ObjectiveBudgetProposal obp = objectiveBudgetProposalRepository.findOne(id);
		if(obp == null) {
			return null;
		}

		obp.getForObjective().getTargets().size();
		
		//now before return back we'll update the parents
		Objective o = obp.getForObjective().getParent();
		BudgetType budgetType = obp.getBudgetType();
		Organization workAt = obp.getOwner();
		ObjectiveBudgetProposal zeroObp = new ObjectiveBudgetProposal();
		zeroObp.copyValue(obp);
		// now reset all to zero
		zeroObp.resetToZeroValue();
		
		while(o != null) {
			ObjectiveBudgetProposal obpParent = objectiveBudgetProposalRepository.findByForObjectiveAndOwnerAndBudgetType(o, workAt, budgetType);
			if(obpParent == null) {
				obpParent= new ObjectiveBudgetProposal();
				obpParent.setForObjective(o);
				obpParent.setBudgetType(budgetType);
				obpParent.setOwner(workAt);
			}
			
			// now we set the obp to this one!
			obpParent.adjustAmount(zeroObp, obp);
			
			objectiveBudgetProposalRepository.save(obpParent);
			
			o = o.getParent();
		}
		
		objectiveBudgetProposalRepository.delete(obp);
		
		return obp;
		
	}

	@Override
	public String findObjectiveTypeChildrenNameOf(Long id) {
		ObjectiveType t = objectiveTypeRepository.findOne(id);
		if(t.getChildren() != null && t.getChildren().size() > 0 ) {
			return t.getChildren().iterator().next().getName();
		} else {
			return "";
		}
		
	}

	@Override
	public String findObjectiveChildrenTypeName(Long id) {
		Objective o = objectiveRepository.findOne(id);
		if(o.getType() != null ) {
			if(o.getType().getChildren() != null && o.getType().getChildren().size() > 0 ) {
				return o.getType().getChildren().iterator().next().getName();
			}
		}
		return null;
	}

	
	
	@Override
	public ObjectiveName saveObjectiveName(JsonNode node) {
		ObjectiveName on = new ObjectiveName();
		
		if(node.get("name") != null) {
			on.setName(node.get("name").asText());
		}
		
		if(node.get("type") != null) {
			ObjectiveType type = objectiveTypeRepository.findOne(
					node.get("type").get("id").asLong());
			on.setType(type);
		}
		
		if(node.get("fiscalYear") != null ) {
			on.setFiscalYear(node.get("fiscalYear").asInt());
		}
		
		// now find the maximum number in this type
		Integer maxIndex = objectiveNameRepository.findMaxIndexOfTypeAndFiscalYear(
				on.getType(), on.getFiscalYear());
		
		if(maxIndex == null) {
			on.setIndex(11);
		} else {
			on.setIndex(maxIndex+1);
		}
		on.setCode(on.getIndex().toString());
		
		objectiveNameRepository.save(on);
		return on;
	}

	@Override
	public ObjectiveName updateObjectiveName(JsonNode node) {
		ObjectiveName on = objectiveNameRepository.findOne(node.get("id").asLong());
		
		if(node.get("name") != null) {
			on.setName(node.get("name").asText());
		}
		
		if(node.get("type") != null) {
			ObjectiveType type = objectiveTypeRepository.findOne(
					node.get("type").get("id").asLong());
			on.setType(type);
		}
		
		if(node.get("fiscalYear") != null ) {
			on.setFiscalYear(node.get("fiscalYear").asInt());
		}
		
		objectiveNameRepository.save(on);
		return on;
	}

	@Override
	public Page<ObjectiveName> findAllObjectiveNameByFiscalYearAndTypeId(
			Integer fiscalYear, Long typeId, PageRequest pageRequest) {
		Page<ObjectiveName> page =  objectiveNameRepository.findAllObjectiveNameByFiscalYearAndTypeId(fiscalYear, typeId, pageRequest);
		
		for(ObjectiveName n : page) {
			n.getType().getId();
			n.getTargets().size();
		}
		
		return page;
	}

	@Override
	public ObjectiveName findOneObjectiveName(Long id) {
		return objectiveNameRepository.findOne(id);
	}

	@Override
	public ObjectiveName deleteObjectiveName(Long id) {
		ObjectiveName on = objectiveNameRepository.findOne(id);
		if(on!=null) {
			List<Objective> oList = objectiveRepository.findAllByObjectiveName(on);
			
			// we must delete all Objective before 
			for(Objective o : oList) {
				// now delete all relation that have o
				
				
				objectiveRepository.delete(o);
			}
			
			
			objectiveNameRepository.delete(on);
		}
		return on;
	}

	@Override
	public List<ObjectiveName> findAvailableObjectiveNameChildrenByObejective(Long id, String searchQuery) {
		Objective objective = objectiveRepository.findOne(id);
		
		logger.debug(searchQuery);
		
		if(searchQuery == null || searchQuery.length() == 0) {
		
			return objectiveNameRepository.findAllChildrenTypeObjectiveNameByFiscalYearAndTypeId(objective.getFiscalYear(), objective.getType().getId()) ;
		} else {
			return objectiveNameRepository.findAllChildrenTypeObjectiveNameByFiscalYearAndTypeId(objective.getFiscalYear(), objective.getType().getId(), "%"+searchQuery+"%") ;
		}
	}

	@Override
	public Objective objectiveAddChildObjectiveName(Long parentId, Long nameId) {
		ObjectiveName oName = objectiveNameRepository.findOne(nameId);
		
		Objective o = new Objective();
		o.setName(oName.getName());
		o.setCode(oName.getCode());
		o.setFiscalYear(oName.getFiscalYear());
		o.setType(oName.getType());
		
		o.getType().getName();
		
		o.setIndex(Integer.parseInt(oName.getCode()));
		o.setIsLeaf(true);
		o.setObjectiveName(oName);
		
		Objective parent = objectiveRepository.findOne(parentId);
		parent.getType().getName();
		
		o.setParent(parent);
		o.setParentLevel(parent.getParentLevel()+1);
		o.setParentPath("." + parentId + parent.getParentPath());
		
		// now set the target
		for(ObjectiveTarget ot : oName.getTargets()) {
			o.setTargets(new ArrayList<ObjectiveTarget>());
			o.getTargets().add(ot);
		}
		
		// now save O
		objectiveRepository.save(o);
		
		return o;
	}

	@Override
	public BudgetSignOff findBudgetSignOffByFiscalYearAndOrganization(
			Integer fiscalYear, Organization workAt) {
		BudgetSignOff budgetSignOff = budgetSignOffRepository.findOneByFiscalYearAndOwner(fiscalYear, workAt);
		
		if(budgetSignOff == null) {
			budgetSignOff = new BudgetSignOff();
			budgetSignOff.setFiscalYear(fiscalYear);
			budgetSignOff.setOwner(workAt);
			budgetSignOffRepository.save(budgetSignOff);
		}
		
		return budgetSignOff;
	}

	@Override
	public Long findSumTotalBudgetProposalOfOwner(Integer fiscalYear,
			Organization workAt) {
		return budgetProposalRepository.findSumTotalOfOwner(fiscalYear,workAt);
	}

	@Override
	public Long findSumTotalObjectiveBudgetProposalOfOwner(Integer fiscalYear,
			Organization workAt) {
		return objectiveBudgetProposalRepository.findSumTotalOfOwner(fiscalYear,workAt);
	}

	@Override
	public BudgetSignOff updateBudgetSignOff(Integer fiscalYear,ThaicomUserDetail currentUser,
			String command) {
		
		
		BudgetSignOff bso = findBudgetSignOffByFiscalYearAndOrganization(fiscalYear, currentUser.getWorkAt());
		if(command.equals("lock1")) {
			bso.setLock1Person(currentUser.getPerson());
			bso.setLock1TimeStamp(new Date());
			
			bso.setUnLock1Person(null);
			bso.setUnLock1TimeStamp(null);
		} else if(command.equals("lock2")) {
			bso.setLock2Person(currentUser.getPerson());
			bso.setLock2TimeStamp(new Date());
			
			bso.setUnLock2Person(null);
			bso.setUnLock2TimeStamp(null);
			
		} else if(command.equals("unLock1")) {
			bso.setUnLock1Person(currentUser.getPerson());
			bso.setUnLock1TimeStamp(new Date());
			
			bso.setLock1Person(null);
			bso.setLock1TimeStamp(null);
			
		} else if(command.equals("unLock2")) {
			bso.setUnLock2Person(currentUser.getPerson());
			bso.setUnLock2TimeStamp(new Date());
			
			bso.setLock2Person(null);
			bso.setLock2TimeStamp(null);
			
		}
		
		
		budgetSignOffRepository.save(bso);
		
		return bso;
	}
	
	public List<List<Objective>> findObjectivesByFiscalyearAndTypeIdAndInitBudgetProposal(
			Integer fiscalYear, long typeId, Organization workAt) {
		Objective root = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		List<Objective> allList = new ArrayList<Objective>();
		
		
		allList = findFlatChildrenObjectivewithBudgetProposal(
					fiscalYear, workAt.getId(), root.getId());
		
		
		List<List<Objective>> returnList = new ArrayList<List<Objective>>();
		returnList.add(allList);
		return returnList;
	}

	@Override
	public List<List<Objective>> findObjectivesByFiscalyearAndTypeIdAndInitObjectiveBudgetProposal(
			Integer fiscalYear, long typeId, Organization workAt) {
		Objective root = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		List<Objective> allList = new ArrayList<Objective>();
		
		
		allList = findFlatChildrenObjectivewithObjectiveBudgetProposal(
					fiscalYear, workAt.getId(), root.getId());
		
		
		List<List<Objective>> returnList = new ArrayList<List<Objective>>();
		returnList.add(allList);
		return returnList;
	}

	@Override
	public ObjectiveDetail findOneObjectiveDetail(Long id) {
		ObjectiveDetail detail = objectiveDetailRepository.findOne(id);
		detail.getForObjective().getId();
		return detail;
	}

	@Override
	public ObjectiveDetail updateObjectiveDetail(JsonNode node, Organization owner) {
		return saveObjectiveDetail(node, owner);
	}

	@Override
	public ObjectiveDetail saveObjectiveDetail(JsonNode node, Organization owner) {
		ObjectiveDetail detail;
		if(getJsonNodeId(node) != null) {
			detail = objectiveDetailRepository.findOne(getJsonNodeId(node));
		} else {
			detail = new ObjectiveDetail();
		}
		
		// now will do the dull mapping 
		
		if(getJsonNodeId(node.get("forObjective")) != null) {
			Objective forObjective = objectiveRepository.findOne(getJsonNodeId(node.get("forObjective")));
			detail.setForObjective(forObjective);
		}
		
		detail.updateField("officerInCharge", node.get("officerInCharge"));
		detail.updateField("phoneNumber", node.get("phoneNumber"));
		detail.updateField("email", node.get("email"));
		
		detail.updateField("reason", node.get("reason"));
		detail.updateField("projectObjective", node.get("projectObjective"));
		
		detail.updateField("methodology1", node.get("methodology1"));
		detail.updateField("methodology2", node.get("methodology2"));
		detail.updateField("methodology3", node.get("methodology3"));
		
		detail.updateField("location", node.get("location"));
		detail.updateField("timeframe", node.get("timeframe"));
		detail.updateField("targetDescription", node.get("targetDescription"));
		
		detail.updateField("outcome", node.get("outcome"));
		
		detail.updateField("output", node.get("output"));
		
		detail.updateField("targetArea", node.get("targetArea"));
		
		/**
		if(node.get("officerInCharge")!=null) detail.setOfficerInCharge(node.get("officerInCharge").asText());
		if(node.get("phoneNumber") != null) detail.setPhoneNumber(node.get("phoneNumber").asText());
		if(node.get("email") != null) detail.setEmail(node.get("email").asText());
		
		if(node.get("reason") != null) detail.setReason(node.get("reason").asText());
		if(node.get("projectObjective") != null) detail.setProjectObjective(node.get("projectObjective").asText());
		
		if(node.get("methodology1") != null) detail.setMethodology1(node.get("methodology1").asText());
		if(node.get("methodology2") != null) detail.setMethodology1(node.get("methodology2").asText());
		if(node.get("methodology3") != null) detail.setMethodology1(node.get("methodology3").asText());
		
		if(node.get("location") != null) detail.setLocation(node.get("location").asText());
		if(node.get("timeframe") != null) detail.setTimeframe(node.get("timeframe").asText());
		if(node.get("targetDescription") != null) detail.setTargetDescription(node.get("targetDescription").asText());
		
		if(node.get("outcome") != null) detail.setOutcome(node.get("outcome").asText());
		
		if(node.get("output") != null) detail.setOutput(node.get("output").asText());
		
		if(node.get("targetArea") != null) detail.setTargetArea(node.get("targetArea").asText());
		**/
		
		
		// lastly 
		detail.setOwner(owner);
		
		
		objectiveDetailRepository.save(detail);
		
		
		return detail;
	}

	@Override
	public ObjectiveDetail deleteObjectiveDetail(Long id) {
		ObjectiveDetail detail = objectiveDetailRepository.findOne(id);
		if(detail != null ) {
			objectiveDetailRepository.delete(detail);
		}
		return detail;
	}

	@Override
	public ObjectiveDetail findOneObjectiveDetailByObjectiveIdAndOwner(Long objectiveId,
			ThaicomUserDetail currentUser) {
		return objectiveDetailRepository.findByForObjective_IdAndOwner(objectiveId, currentUser.getWorkAt());
	}

	
	
	
	@Override
	public List<Organization> findOrganizationTopLevelByName(
			String query) {
		if(query == null) {
			query = "%";
		} else {
			query = "%" + query + "%";
		}
		return organizationRepository.findAllTopLevelByNameLike(query);
	}

	/**
	 * ค้นหาหน่วยงานจากชื่อ
	 * 
	 * @param query 	ชื่อที่ต้องการค้นหา โดยจะค้นจาก name หรือ abbr และใส่
	 * 					% + query + %
	 * @param code		รหัสของหน่วยงานที่ต้องการค้นหา
	 */
	@Override
	public List<Organization> findOrganizationByNameAndCode(String query, String code) {
		
		if(code == null) {
			code = "";
		}
		
		return organizationRepository.findAllByNameLikeAndCodeLikeOrderByNameAsc("%"+query+ "%", "%"+code+ "%");
	}

	
	
	@Override
	public List<Organization> findOrganizationByNameAndParent_Id(String query,
			Long parentId) {
		// TODO Auto-generated method stub
		return organizationRepository.findAllByNameLikeAndParent_IdOrderByNameAsc("%"+query+"%", parentId);
	}
	
	@Override
	public List<Organization> findOrganizationByNameAndParent_IdWithProcuremnt(
			String query, Long parentId) {
		List<Organization> returnList = new ArrayList<Organization>();
		Organization procurment = organizationRepository.findOne(101170100L);
		
		returnList.add(procurment);
		
		if(parentId >= 110000000L){
			List<Organization> l = organizationRepository.findAllByNameLikeAndParent_IdOrderByNameAsc("%"+query+"%", parentId);
			returnList.addAll(l);
		}
		
		return returnList;
	}

	@Override
	public List<Organization> findOrganizationChildrenOrSiblingOf(
			Organization workAt) {
		List<Organization> returnList = new ArrayList<Organization>();
		Organization org = organizationRepository.findOne(workAt.getId());
		returnList.add(org);
		if(org.getParent().getId() == 0L ) {
			returnList.addAll(org.getChildren());
			returnList.size();
		} else {
			Organization parent = org.getParent();
			returnList.addAll(parent.getChildren());
			returnList.size();
		}
		return returnList;
	}

	
	
	
	
	@Override
	public List<Organization> findOrganization_ฝ่าย() {
		// TODO Auto-generated method stub
		return organizationRepository.findAll_ฝ่าย();
	}

	@Override
	public List<Organization> findOrganizationProvinces() {
		
		return organizationRepository.findAllProvinces();
	}

	@Override
	public List<Organization> findOrganizationByProvinces(String query) {
		query = "%" + query + "%";
		return organizationRepository.findAllByProvinces(query);
	}
	
	@Override
	public Organization findOrganizationById(Long id) {
		logger.debug("id" + id.toString());
		
		Organization org = organizationRepository.findOneById(id);
		if(org!=null && org.getChildren() != null) {
			org.getChildren().size();
		} else {
			logger.debug("org is null");
		}
		return org;
	}
	
	


	
	@Override
	public Organization findOrganizationParentOf(Organization org) {
		Organization orgJpa = organizationRepository.findOneById(org.getId());
		orgJpa.getParent().getId();
		return orgJpa.getParent();
	}

	/**
	 * ค้นหาหน่วยงานจาก Objective ที่ได้รับผิดชอบ
	 * @param objectiveId id ของ {@link Objective} ที่รับผิดชอบ
	 * 
	 */
	@Override
	public List<Organization> findOrganizationByObjectiveOwner(Long objectiveId) {
		return organizationRepository.findAllByOwningObjective(objectiveId);
	}

	/**
	 * บันทึกข้อมูลหน่วยงานผู้รับผิดชอบ
	 * @param id objective id ที่ต้องการบันทึก
	 * @param ownerIds array ของ organization ids
	 */
	@Override
	public List<Organization> saveObjectiveOwners(Long id, Long[] ownerIds) {
		if(ownerIds == null || ownerIds.length == 0) {
			// nothing to do here
			return null;
		}
		
		ObjectiveOwnerRelation owr = objectiveOwnerRelationRepository.findByObjective_Id(id);
		if(owr == null) {
			owr = new ObjectiveOwnerRelation();
			owr.setObjective(objectiveRepository.findOne(id));
			owr.setOwners(new ArrayList<Organization>());
		}
		
		List<Organization> paramList = new ArrayList<Organization>();
		for(Long ownerId : ownerIds) {
			Organization o = organizationRepository.findOne(ownerId);
			paramList.add(o);

		}
	
		owr.setOwners(paramList);
		
		// we can save here
		objectiveOwnerRelationRepository.save(owr);
		
		return owr.getOwners();
	}

	@Override
	public List<Activity> findActivityByOwnerAndForObjective(
			Organization workAt, Long objectiveId) {
		
		Organization org = organizationRepository.findOne(workAt.getId());
		if(org.getParent().getId() != 0) {
			org = org.getParent();
		}
		
		logger.debug("org: " + org.getId() +  " : " + org.getName());
		
		List<Activity> list1 =  activityRepository.findAllByOwnerAndForObjective_Id(org, objectiveId);
		List<Activity> list2 = activityRepository.findAllByRegulatorAndForObejctive_Id(org, objectiveId);
		List<Activity> list3 = activityRepository.findAllByActivityTargetReportOwner(org, objectiveId);
		
		List<Activity> list = new ArrayList<Activity>();
		list.addAll(list1);
		for(Activity act : list2) {
			if(!list.contains(act)) {
				list.add(act);
			}
		}
		for(Activity act : list3) {
			if(!list.contains(act)) {
				list.add(act);
			}
		}
		
		for(Activity activity : list) {
			activity.getForObjective().getId();
			activity.getTargets().size();
			activity.getChildren().size();
			activity.getOwner().getId();
			if(activity.getRegulator() != null) {
				activity.getRegulator().getId();
			}
			if(activity.getChildren().size() > 0) {
				for(Activity child : activity.getChildren()) {
					child.getTargets().size();
					if(child.getChildren() != null && child.getChildren().size() > 0 ) {
						for(Activity grandChild : child.getChildren()) {
							grandChild.getTargets().size();
						}
					}
				}
			}
			
		}
		
		logger.debug("list size: " + list1.size());
		logger.debug("list size: " + list2.size());
		logger.debug("list size: " + list3.size());
		logger.debug("list size: " + list.size());
		
		Collections.sort(list, new Comparator<Activity>() {

			@Override
			public int compare(Activity o1, Activity o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});
		
		return list;
	}

	@Override
	public Activity findOneActivity(Long id) {
		Activity activity =  activityRepository.findOne(id);
		activity.getTargets().size();
		activity.getOwner().getId();
		activity.getRegulator().getId();
		return activity;
	}

	@Override
	public Activity updateActivity(JsonNode node) {
		Activity activity = activityRepository.findOne(getJsonNodeId(node));
		
		activity.setCode(node.get("code").asText());
		activity.setName(node.get("name").asText());
		
		Organization regulator = organizationRepository.findOne(getJsonNodeId(node.get("regulator")));
		activity.setRegulator(regulator);
		
		List<ActivityTarget> oldTargets = activity.getTargets();
		
		activity.setTargets(new ArrayList<ActivityTarget> ());
		
		for(JsonNode targetNode : node.get("targets")) {
			ActivityTarget target = new ActivityTarget();
			
			if(getJsonNodeId(targetNode) != null) {
				target = activityTargetRepository.findOne(getJsonNodeId(targetNode));
				oldTargets.remove(target);
			}
			
			target.setActivity(activity);
			target.setProvincialTarget(targetNode.get("provincialTarget").asBoolean());
			
			target.setTargetValue(targetNode.get("targetValue").asDouble());
			if(targetNode.get("budgetAllocated") != null) {
				if(!target.getProvincialTarget()) {
					target.setBudgetAllocated(targetNode.get("budgetAllocated").asDouble());
					if(target.getReports() != null) {
						for(ActivityTargetReport report : target.getReports()) {
							report.getActivityPerformance().setBudgetAllocated(target.getBudgetAllocated());
							activityPerformanceRepository.save(report.getActivityPerformance());
						}
					}
				} else {
					target.setBudgetAllocated(0.0);
					// we should remove all report here?
				}
			}
				
			
			TargetUnit unit = targetUnitRepository.findOne(getJsonNodeId(targetNode.get("unit")));
			target.setUnit(unit);
			
			logger.debug("setting unit!");

			activityTargetRepository.save(target);
			
			logger.debug("save target?!");
			
			activity.getTargets().add(target);
		}
		
		// we'll delete each of the left over
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>" + oldTargets.size());
		
		List<ActivityTargetReport> reports =  new ArrayList<ActivityTargetReport>();
		
		for(ActivityTarget t : oldTargets) {
			reports.addAll(t.getReports());
		}
		
		activityTargetReportRepository.delete(reports);
		
		activityTargetRepository.delete(oldTargets);
		
		activityRepository.save(activity);
		
		
		
		return activity;
	}

	@Override
	public Activity saveActivity(JsonNode node, Organization owner) {
		Activity activity = new Activity();
		activity.setOwner(owner);
		
		Objective obj = objectiveRepository.findOne(getJsonNodeId(node.get("forObjective")));
		activity.setForObjective(obj);
		
		activity.setCode(node.get("code").asText());
		activity.setName(node.get("name").asText());
		activity.setActivityLevel(node.get("activityLevel").asInt());
		
		Organization regulator = organizationRepository.findOne(getJsonNodeId(node.get("regulator")));
		activity.setRegulator(regulator);
		
		activity.setTargets(new ArrayList<ActivityTarget> ());
		activityRepository.save(activity);
		
		for(JsonNode targetNode : node.get("targets")) {
			ActivityTarget target = new ActivityTarget();
			target.setActivity(activity);
			target.setProvincialTarget(targetNode.get("provincialTarget").asBoolean());

			target.setTargetValue(targetNode.get("targetValue").asDouble());
			if(targetNode.get("budgetAllocated") != null) {
//				if(!target.getProvincialTarget()) {
				if(targetNode.get("provincialTarget") != null  && targetNode.get("provincialTarget").asBoolean() == false ) {
					target.setBudgetAllocated(targetNode.get("budgetAllocated").asDouble());
				} else {
					target.setBudgetAllocated(0.0);
				}
			}
			TargetUnit unit = targetUnitRepository.findOne(getJsonNodeId(targetNode.get("unit")));
			target.setUnit(unit);
			
			activityTargetRepository.save(target);
			
			activity.getTargets().add(target);
		}
		
		// now if there is a parent
		if(getJsonNodeId(node.get("parent")) != null) {
			Activity parent = activityRepository.findOne(getJsonNodeId(node.get("parent")));
			activity.setParent(parent);
		}
		
		
		activityRepository.save(activity);
		
		return activity;
	}

	@Override
	public Activity deleteActivity(Long id) {
		Activity activity = activityRepository.findOne(id);
		
			// now find all children
		if(activity.getChildren() != null && activity.getChildren().size() > 0) {
			return null; 
			
		} else {
			if(activity.getTargets() != null) {
				for(ActivityTarget target : activity.getTargets()) {
					List<ActivityTargetReport> atrList = activityTargetReportRepository.findAllByTarget_id(target.getId());
					
					for(ActivityTargetReport atr : atrList) {
						ActivityPerformance ap =  atr.getActivityPerformance();
						ap.getMonthlyBudgetReports();
						atr.getMonthlyReports();
						
						monthlyActivityReportRepository.delete(atr.getMonthlyReports());
						monthlyBudgetReportRepository.delete(ap.getMonthlyBudgetReports());
						
						activityPerformanceRepository.delete(ap);
						
					}
					
					activityTargetReportRepository.delete(atrList);
					
				}
				
				activityTargetRepository.delete(activity.getTargets());
			}
			
			activityRepository.delete(activity);
		}
		 
		return activity;
	}

	@Override
	public List<ActivityTargetReport> findActivityTargetReportByTargetId(
			Long targetId) {
		return activityTargetReportRepository.findAllByTarget_id(targetId);
	}

	@Override
	public List<ActivityTargetReport> saveActivityTargetReportByTargetId(
			Long targetId, JsonNode node, Long parentOrgId) {
		// we get the current List
		
		logger.debug("parentOrgId : " + parentOrgId);
		List<ActivityTargetReport> oldList;
		if(parentOrgId == null ) {
			oldList = findActivityTargetReportByTargetId(targetId);
		} else {
			oldList = findActivityTargetReportByTarget_IdAndParentOrgId(targetId, parentOrgId);
		}
		
		logger.debug("oldList.size() =" + oldList.size() + " :");
		for(ActivityTargetReport r: oldList) {
			logger.debug("   - id: " + r.getId()); 
					
		}
		
		
		List<ActivityTargetReport> newList = new ArrayList<ActivityTargetReport>();
		
		ActivityTarget target = activityTargetRepository.findOne(targetId);
		Double sumBudget = 0.0;
		//now for each node 
		for(JsonNode reportNode : node) {
			// we'll go through the oldList
			Long reportId = getJsonNodeId(reportNode);
			logger.debug("-----------------------report ID : " + reportId);
			logger.debug("=======================owner ID  : " + getJsonNodeId(reportNode.get("owner")));
			ActivityTargetReport report;
			ActivityPerformance performance;
			if (reportId != null) {
				// should be in the oldList
				report = activityTargetReportRepository.findOne(reportId);
				performance = report.getActivityPerformance();
				oldList.remove(report);
				
				// now we'll have to go to oldList again to remove the level2 report that 
				// this report is parent?
				List<ActivityTargetReport> level2Report = new ArrayList<ActivityTargetReport>();
				for(ActivityTargetReport r : oldList) {
					if(r.getOwner().getParent().getId() == report.getOwner().getId()) {
						level2Report.add(r);
					}
				}
				
				oldList.removeAll(level2Report);
				
			} else {
				report = new ActivityTargetReport();
				Organization owner = organizationRepository.findOne(getJsonNodeId(reportNode.get("owner")));
				report.setOwner(owner);
				report.setTarget(target);
				
				performance = new ActivityPerformance();
				performance.setActivity(target.getActivity());
				performance.setOwner(report.getOwner());
			
				report.setActivityPerformance(performance);
				
			}
			
			if(reportNode.get("reportLevel") != null) {
				report.setReportLevel(reportNode.get("reportLevel").asInt());
			}
			
			logger.debug(">>>>>>" + target.getActivity().getId() );
			logger.debug(">>>>>" + report.getOwner().getId() );
			
			// activityTargetReportRepository.save(report);
			
			if(reportNode.get("activityPerformance") != null && 
					reportNode.get("activityPerformance").get("budgetAllocated") != null) {
				logger.debug("reportNode.get(\"activityPerformance\").get(\"budgetAllocated\") ==" + reportNode.get("activityPerformance").get("budgetAllocated").asDouble());
				performance.setBudgetAllocated(reportNode.get("activityPerformance").get("budgetAllocated").asDouble());
			} else if(report.getTarget().getProvincialTarget() == false) {
				logger.debug("report.getTarget().getProvincialTarget() ==" + report.getTarget().getProvincialTarget());
				
				Double targetBudgetAllocated = 0.0;
				if(report.getTarget().getBudgetAllocated()!=null) {
					targetBudgetAllocated = report.getTarget().getBudgetAllocated();
				}
				
				logger.debug("report.getTarget().getBudgetAllocated() ==" + targetBudgetAllocated);
				performance.setBudgetAllocated(targetBudgetAllocated); 
			} else {
				logger.debug("seting BudgetAllocated == 0.0" );
				performance.setBudgetAllocated(0.0);
			}
			
			activityPerformanceRepository.save(performance);
			
			report.setActivityPerformance(performance);
			report.setTargetValue(reportNode.get("targetValue").asDouble());
			
			sumBudget += performance.getBudgetAllocated();
			
			
			newList.add(report);
		}
		
		logger.debug("target.getActivity().getId() : " + target.getActivity().getId());
		logger.debug("parentOrgId: " + parentOrgId);
		// here we'll update the parent!
		ActivityPerformance performance = 
				activityPerformanceRepository.findOneByActivityAndTargetAndOwner_id(target.getActivity(), target, parentOrgId);
		
		if(performance != null) {
			// if it is null there is no child!
			performance.setBudgetAllocated(sumBudget.doubleValue());
			activityPerformanceRepository.save(performance);
		}
		
		// we should be able to delete oldList and save newList
		
		activityTargetReportRepository.save(newList);
		
		logger.debug("oldList.size() now =" + oldList.size());
		
		// now in each of the left old list 
		// we iterate to see if we should delete performance
		List<ActivityPerformance> toDeletePerformance = new ArrayList<ActivityPerformance>();
 		for(ActivityTargetReport oldReport: oldList) {
 			
 			
 			
			ActivityPerformance oldPerformance = oldReport.getActivityPerformance();
			toDeletePerformance.add(oldPerformance);
			
			monthlyActivityReportRepository.delete(oldReport.getMonthlyReports());
			monthlyBudgetReportRepository.delete(oldPerformance.getMonthlyBudgetReports());
		}
		
 		logger.debug("report oldList Id: " );
 		List<ActivityTargetResult> results = new ArrayList<ActivityTargetResult>();
 		for(ActivityTargetReport deleteReport : oldList) {
 			logger.debug("  id: "+ deleteReport.getId() );
 			results.addAll(activityTargetResultRepository.findByReport(deleteReport));
 			
 		}
 		
 		activityTargetResultRepository.delete(results);
 		
		activityTargetReportRepository.delete(oldList);
		activityPerformanceRepository.delete(toDeletePerformance);
		
		return newList;
	}

	@Override
	public List<ActivityTargetReport> findActivityTargetReportByTarget_IdAndParentOrgId(
			Long activityTargetId, Long parentOrgId) {
		
			Long searchOrgId = parentOrgId;
			
			Organization org = organizationRepository.findOne(parentOrgId);
			searchOrgId = OrganizationType.getProvinceId(org);
			if(searchOrgId == null) return null;
			
		
			return activityTargetReportRepository.findAllByTarget_IdAndOwner_Parent_id(activityTargetId, searchOrgId);
		
	}
	
	@Override
	public List<Objective> findObjectiveLoadActivityByParentObjectiveIdAndReportLevel(
			Long objectiveId, Long ownerId, Boolean provinceLevel) {
		Organization searchOrg = organizationRepository.findOne(ownerId);
		
		if(provinceLevel == true) {
			logger.debug("province Id  = " + OrganizationType.getProvinceId(searchOrg));
			searchOrg = organizationRepository.findOne(OrganizationType.getProvinceId(searchOrg));
			if(searchOrg == null) return null;
		}
		
		
		String objectiveIdLike = "%."+objectiveId + ".%";
		
		List<Objective> childrenObjective = new ArrayList<Objective>();
		
		List<ActivityTargetReport> targetReports = activityTargetReportRepository
				.findAllByParentObjectiveIdAndOwnerId(objectiveIdLike, searchOrg.getId());
		
		logger.debug("targetReports: " + targetReports.size());
		
		for(ActivityTargetReport report : targetReports) {
			Objective child = report.getTarget().getActivity().getForObjective();
			
			// we're just displaying not save...
			// so rid of stuff
			report.getTarget().setReports(null);
			
			
			report.getTarget().setFilterReport(report);
			report.getActivityPerformance().getId();
			//now sum all performance 
			//report.getActivityPerformance().setBudgetAllocated(budgetAllocated);
			
			
			Activity act = report.getTarget().getActivity();
			logger.debug("activity id: " + act.getId());
			
			// first put the target
			if(act.getFilterTargets()==null) {
				act.setFilterTargets(new ArrayList<ActivityTarget>());
			}
			if(!act.getFilterTargets().contains(report.getTarget())) {
				//lazily init TargetUnit here 
				report.getTarget().getUnit().getId();
				act.getFilterTargets().add(report.getTarget());
			} 
			
			//then check if there is anyparent
			while(act.getParent() != null) {
				if(act.getParent().getChildren() == null){
					act.getParent().setChildren(new ArrayList<Activity>());
				}
				
				act.getParent().getChildren().add(act);
				act = act.getParent();
				logger.debug("adding children to act.getParent: " + act.getChildren().size());
			}
			
			// then add to กิจกรรมรอง
			if(child.getFilterActivities()==null) {
				child.setFilterActivities(new ArrayList<Activity>());
			}
			if(!child.getFilterActivities().contains(act)) {
				logger.debug("adding act.id: " + act.getId());
				child.getFilterActivities().add(act);
				
//				Collections.sort(child.getFilterActivities(), new Comparator<Activity>() {
//
//					@Override
//					public int compare(Activity arg0, Activity arg1) {
//						// TODO Auto-generated method stub
//						return arg0.getCode().compareTo(arg1.getCode());
//					}
//					
//				});
				
			}
			
			if(!childrenObjective.contains(child)) {
				childrenObjective.add(child);
			}
			
		}
		//now sort childrenObjective
		 Collections.sort(childrenObjective,new Comparator<Objective>() {
			@Override
			public int compare(Objective arg0, Objective arg1) {
				return arg0.getCode().compareTo(arg1.getCode());
			}
	           
	 	});
		
		logger.debug("returning...");
		return childrenObjective;
		
	}

	@Override
	public List<ActivityTargetReport> findActivityTargetReportByTargetIdAndReportLevel(
			Long targetId, int reportLevel) {
		return activityTargetReportRepository.findAllByTarget_idAndReportLevel(targetId, reportLevel);
	}

	@Override
	public List<ActivityPerformance> findActivityPerformancesByOwnerAndObjectiveId(
			Organization workAt, Long objectiveId) {
		return activityPerformanceRepository.findByOwnerAndObjectiveId(workAt, objectiveId);
	}

	
	
	@Override
	public List<Activity> findActivityByRegularAndObjectiveId(
			Organization workAt, Long objectiveId) {
		List<Activity> parentList = activityRepository.findAllByRegulatorAndForObejctive_Id(workAt, objectiveId);
		
		List<Activity> flatList = new ArrayList<Activity>();
		
		
		for(Activity parent : parentList) {
			flatList.add(parent);
			if(parent.getChildren() != null && parent.getChildren().size() > 0 ) {
				for(Activity child : parent.getChildren()) {
					child.getTargets().size();
					child.getOwner().getId();
					child.getRegulator().getId();
					flatList.add(child);
					if(child.getChildren() != null && child.getChildren().size() >0) {
						for(Activity grandChild : child.getChildren()) {
							grandChild.getTargets().size();
							
							grandChild.getOwner().getId();
							grandChild.getRegulator().getId();
							flatList.add(grandChild);
						}
						child.sumChildrenTarget();
					}
 				}
				parent.sumChildrenTarget();
			}
		}
		
		
		return flatList;
		
	}

	@Override
	public List<ActivityTargetReport> findActivityTargetReportByTargetIdAndOwnerId(
			Long targetId, Long ownerId) {
		return activityTargetReportRepository.findAllByTarget_idAndOwner_Id(targetId, ownerId);
	}

	@Override
	public ActivityTargetReport findActivityTargetReportById(Long id) {
		ActivityTargetReport atr = activityTargetReportRepository.findOneAndFetchReportById(id);
		atr.getActivityPerformance().getMonthlyBudgetReports().size();
		atr.setLatestResult(activityTargetResultRepository.findByLatestTimeStamp(atr));
		return atr;
	}

	@Override
	public ActivityTargetReport saveActivityTargetReportPlan(Long id, JsonNode node) {
		ActivityTargetReport report = activityTargetReportRepository.findOne(id);
		if(report.getMonthlyReports() == null || report.getMonthlyReports().size() != 12) {
			// remove the the old one!
			if(report.getMonthlyReports() != null) {
				monthlyActivityReportRepository.delete(report.getMonthlyReports());
			}
			
			// make new Report first!
			logger.debug("create new Report!");
			report.setMonthlyReports(new ArrayList<MonthlyActivityReport>());
			List<MonthlyActivityReport> monthlyReports = report.getMonthlyReports();
			for(Integer i=0; i<12; i++) {
				MonthlyActivityReport monthly = new MonthlyActivityReport();
				monthly.setFiscalMonth(i);
				monthly.setOwner(report.getOwner());
				monthly.setReport(report);
				
				monthlyReports.add(monthly);
			}
		}
		
		for(JsonNode monthlyNode : node.get("monthlyReports")) {
			Integer month = monthlyNode.get("fiscalMonth").asInt();
			
			
			MonthlyActivityReport monthly = report.getMonthlyReports().get(month);
			
			Double monthlyActivityPlan = 0.0;
			if(monthlyNode.get("activityPlan") != null) {
				monthlyActivityPlan = monthlyNode.get("activityPlan").asDouble();
			}
			logger.debug("get report of fiscalMonth: " + month + " old Value: " + monthly.getActivityPlan() 
					+ " set to new Value: " + monthlyActivityPlan);
			
			if(monthlyNode.get("activityPlan")!=null) {
				monthly.setActivityPlan(monthlyNode.get("activityPlan").asDouble());
			} else {
				monthly.setActivityPlan(0.0);
			}
			
		}
		
		// now do this with the performance
		if(report.getActivityPerformance().getMonthlyBudgetReports() == null ||
				report.getActivityPerformance().getMonthlyBudgetReports().size() != 12) {
			report.getActivityPerformance().setMonthlyBudgetReports(new ArrayList<MonthlyBudgetReport>());
			
			List<MonthlyBudgetReport> monthlyReports = report.getActivityPerformance().getMonthlyBudgetReports();
			for(Integer i=0; i<12; i++) {
				MonthlyBudgetReport monthly = new MonthlyBudgetReport();
				monthly.setFiscalMonth(i);
				monthly.setOwner(report.getOwner());
				monthly.setActivityPerformance(report.getActivityPerformance());
				
				monthlyReports.add(monthly);
			}
		}
		
		for(JsonNode monthlyNode : node.get("activityPerformance").get("monthlyBudgetReports")) {
			Integer month = monthlyNode.get("fiscalMonth").asInt();
			MonthlyBudgetReport monthly = report.getActivityPerformance().getMonthlyBudgetReports().get(month);
			if(monthlyNode.get("budgetPlan")!=null) {
				monthly.setBudgetPlan(monthlyNode.get("budgetPlan").asDouble());
			} else {
				monthly.setBudgetPlan(0.0);
			}
			
		}
		
		monthlyActivityReportRepository.save(report.getMonthlyReports());
		monthlyBudgetReportRepository.save(report.getActivityPerformance().getMonthlyBudgetReports());
		
		
		return report;
	}
	
	

	@Override
	public List<Objective> findObjectiveByActivityTargetReportOfOrganizationAndFiscalYearNoReportCurrentMonth(
			Organization workAt, Integer fiscalYear) {
		
		// getCurrent FiscalMonth
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		Integer fiscalMonth = ( calendar.get(Calendar.MONTH) + 3 ) % 12;
		
		List<ActivityTargetReport> noReports = new ArrayList<ActivityTargetReport> ();
		
		List<ActivityTargetReport> reports = activityTargetReportRepository.findAllByOwner_idAndFiscalYear(workAt.getId(), fiscalYear);
		for(ActivityTargetReport report: reports) {
			// if this has no result in this fiscalBudget 
			Long numResult = activityTargetResultRepository.countResultByReportIdAndFiscalMonthAndBgtResult(report, fiscalMonth);
			
			if(numResult == 0) {
				noReports.add(report);
			}
		}
		
		List<Objective> objectives = new ArrayList<Objective>();
		for(ActivityTargetReport report: noReports){
			Objective obj = report.getTarget().getActivity().getForObjective();
			if(!objectives.contains(obj)) {
				objectives.add(obj);
			}
			
			if(obj.getFilterActivities() == null) {
				obj.setFilterActivities(new ArrayList<Activity> ());
			}
			
			if(!obj.getFilterActivities().contains(report.getTarget().getActivity())) {
				obj.getFilterActivities().add(report.getTarget().getActivity());
			}
			
			if(report.getTarget().getActivity().getFilterTargets() == null) {
				report.getTarget().getActivity().setFilterTargets(new ArrayList<ActivityTarget>());
			}
			
			report.getTarget().setFilterReport(report);
			report.getTarget().getUnit().getId();
			
			report.getTarget().getActivity().getFilterTargets().add(report.getTarget());
		}
		
		return objectives;
	}

	@Override
	public List<Objective> findObjectiveByActivityTargetReportOfOrganizationAndFiscalYear(
			Organization workAt, Integer fiscalYear) {
		
		Organization searchOrg = organizationRepository.findOne(workAt.getId());
//		if(searchOrg.isSubSection()) {
//			searchOrg = searchOrg.getParent();
//		}
		
		
		List<ActivityTargetReport> reports = activityTargetReportRepository.findAllByOwner_idAndFiscalYear(searchOrg.getId(), fiscalYear);
		
		List<Objective> objectives = new ArrayList<Objective>();
		
		
		// now forEach Report
		for(ActivityTargetReport report: reports) {
			report.getActivityPerformance().getMonthlyBudgetReports().size();
			Objective obj = report.getTarget().getActivity().getForObjective();
			
			if(!objectives.contains(obj)) {
				objectives.add(obj);
			}
			
			if(obj.getFilterActivities() == null) {
				obj.setFilterActivities(new ArrayList<Activity> ());
			}
			
			if(!obj.getFilterActivities().contains(report.getTarget().getActivity())) {
				obj.getFilterActivities().add(report.getTarget().getActivity());
			}
			
			if(report.getTarget().getActivity().getFilterTargets() == null) {
				report.getTarget().getActivity().setFilterTargets(new ArrayList<ActivityTarget>());
			}
			
			report.getTarget().setFilterReport(report);
			report.getTarget().getUnit().getId();
			
			report.getTarget().getActivity().getFilterTargets().add(report.getTarget());
			
			logger.debug("reportId : " + report.getId());
			
			report.setLatestResult(activityTargetResultRepository.findByLatestTimeStamp(report));
			
			
			if(report.getLatestResult() == null ) {
				 logger.debug("LatestResult is null");
			} else {
				logger.debug("LatestResult: " + report.getLatestResult().getId() + " : " + report.getLatestResult().getTimestamp() );
			}
		}
		
		for(Objective obj: objectives) {
			// now we'll find out the current budget usages!
			Double sumUsage = budgetUsageFromExternalRepository.findBudgetUsageSummaryByCodeAndOwner(
					obj.getCode(), searchOrg.getId());
			
			List<BudgetProposal> proposals = budgetProposalRepository.findByForObjectiveAndOwner(obj, searchOrg);
			
			
			if(proposals == null || proposals.size() == 0) {
				proposals = new ArrayList<BudgetProposal>();
				BudgetProposal p = new BudgetProposal();
				p.setAmountUsed(sumUsage);
				p.setOwner(searchOrg);
				p.setForObjective(obj);
				
				logger.debug("adding new proposal");
				
				proposals.add(p);
			} 
			
			obj.setFilterProposals(proposals);
			
			logger.debug("xxxxx :  " + obj.getName() + " (" + obj.getCode() + ") : " + sumUsage );
		}
		
		return objectives;
		
		//return objectiveRepository.findByActivityTargetReportOfOrganization(workAt);
	}

	@Override
	public ActivityTargetResult saveActivityTargetResult(JsonNode node,
			ThaicomUserDetail currentUser) {
		
		ActivityTargetResult result;
		if(getJsonNodeId(node) != null) {
			result = activityTargetResultRepository.findOne(getJsonNodeId(node));
			logger.debug("found result : " + result.getId());
		} else {
			result = new ActivityTargetResult();
			logger.debug("create new result : ");
		}
		
		ActivityTargetReport report = activityTargetReportRepository.findOne(getJsonNodeId(node.get("report")));
		result.setReport(report);
		result.setTimestamp(new Date());
		result.setPerson(currentUser.getPerson());
		result.setRemark(node.get("remark").asText());
		result.setResultBudgetType(node.get("resultBudgetType").asBoolean());
		if(result.getResultBudgetType() == true) {
			result.setBudgetResult(node.get("budgetResult").asDouble());
			result.setBudgetFiscalMonth(node.get("budgetFiscalMonth").asInt());

			logger.debug("about to save...");
			activityTargetResultRepository.save(result);

			
			MonthlyBudgetReport monthlyReport = report
					.getActivityPerformance()
					.getFiscalReportOn(result.getBudgetFiscalMonth());
			 
			monthlyReport.setBudgetResult(result.getBudgetResult());
			monthlyBudgetReportRepository.save(monthlyReport);

			
		} else {
			result.setResult(node.get("result").asDouble());
			SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy", new Locale("TH", "TH"));
			
			try {
				
				result.setReportedResultDate(df.parse(node.get("reportedResultDate").asText()));
				
				logger.debug("saving date:" + result.getReportedResultDate());
				
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(result.getReportedResultDate());
			Integer fiscalMonth = ( calendar.get(Calendar.MONTH) + 3 ) % 12;
			
			result.setBudgetFiscalMonth(fiscalMonth);
			
			logger.debug("calendar.get(Calendar.MONTH) = " + calendar.get(Calendar.MONTH));
			logger.debug("fiscalMonth : " + fiscalMonth); 
			
			logger.debug("about to save...");
			activityTargetResultRepository.save(result);

			
			 MonthlyActivityReport monthlyReport = report.getFiscalReportOn(fiscalMonth);
			 
			 logger.debug("monthlyReport.getFiscalMonth(): " + monthlyReport.getFiscalMonth());
			 
			 Double sum = activityTargetResultRepository.findSumResultByReportAndFiscalMonth(report, fiscalMonth);
			 monthlyReport.setActivityResult(sum);
			 
			
			//report.getMonthlyReports().get(fiscalMonth).setActivityResult(monthlyResult);		
			monthlyActivityReportRepository.save(monthlyReport);
		}
		
				// now we'll have to update the result month
		
		result.getReport();
		
		
		result.getReport().getMonthlyReports().size();
		result.getReport().getActivityPerformance().getMonthlyBudgetReports().size();
		
		return result;
	}

	
	
	
	@Override
	public List<ActivityTargetResult> findActivityTargetResultByReportAndFiscalMonth(
			Long targetReportId, Integer fiscalMonth) {
		fiscalMonth = (fiscalMonth + 10) % 12; 
		
		List<ActivityTargetResult> atrList =activityTargetResultRepository.findByReportIdAndFiscalMonthAndNotBgtResult(targetReportId,fiscalMonth);
		
		for(ActivityTargetResult atr : atrList) {
			atr.getReport().getActivityPerformance().getMonthlyBudgetReports().size();
			atr.getReport().getMonthlyReports().size();
		}
		
		return atrList;
	}

	@Override
	public ActivityTargetResult findActivityTargetResultByReportAndFiscalMonthAndBgtResult(
			Long targetReportId, Integer fiscalMonth) {
		
		logger.debug("targetReportId:  " + targetReportId );
		logger.debug("fiscalMonth:  " + fiscalMonth );
		
		
		ActivityTargetResult atr =activityTargetResultRepository.findByReportIdAndFiscalMonthAndBgtResult(targetReportId,fiscalMonth);
		
		if(atr != null) {
			logger.debug(" atr.getId(): " + atr.getId());
		
			atr.getReport().getActivityPerformance().getMonthlyBudgetReports().size();
			atr.getReport().getMonthlyReports().size();
		} else {
			return null;
		}
		
		
		return atr;
	}

	@Override
	public ActivityTargetResult findActivityTargetResultById(Long id) {
		
		return activityTargetResultRepository.findOne(id);
	}

	@Override
	public List<AssetGroup> findAssetGroupAll() {
		return assetGroupRepository.findAllOrderByIdAsc();
	}

	@Override
	public AssetGroup findAssetGroupById(Long id) {
		return assetGroupRepository.findOne(id);
	}

	@Override
	public List<AssetType> findAssetTypeByAssetGroupId(Long groupId) {
		return assetTypeRepository.findAllByGroup_IdOrderByIdAsc(groupId);
	}

	@Override
	public List<AssetKind> findAssetKindByAssetTypeId(Long typeId) {
		return assetKindRepository.findAllByType_idOrderByIdAsc(typeId);
	}

	@Override
	public List<AssetBudget> findAssetBudgetByKindId(Long kindId) {
		return assetBudgetRepository.findAllByKind_IdOrderByIdAsc(kindId);
	}

	@Override
	public AssetBudget saveAssetBudget(JsonNode node) {
		AssetBudget assetBudget = new AssetBudget();
		if(node.get("code") != null) {
			assetBudget.setCode(node.get("code").asText());
		}
		if(node.get("name") != null) {
			assetBudget.setName(node.get("name").asText());
		}
		if(node.get("description") != null) {
			assetBudget.setDescription(node.get("description").asText());
		}
		if(getJsonNodeId(node.get("kind")) != null) {
			AssetKind kind = assetKindRepository.findOne(getJsonNodeId(node.get("kind")));
			assetBudget.setKind(kind);
		}
		
		if(getJsonNodeId(node.get("category")) != null) {
			AssetCategory category = assetCategoryRepository.findOne(getJsonNodeId(node.get("category")));
			assetBudget.setCategory(category);
		}
		
		assetBudgetRepository.save(assetBudget);
		
		return assetBudget;
	}

	@Override
	public AssetBudget deleteAssetBudget(Long id) {
		AssetBudget assetBudget = assetBudgetRepository.findOne(id);

		assetBudgetRepository.delete(assetBudget);		
		return assetBudget;
	}

	@Override
	public AssetBudget updateAssetBudget(JsonNode node) {
		AssetBudget assetBudget = assetBudgetRepository.findOne(getJsonNodeId(node));
		if(node.get("code") != null) {
			assetBudget.setCode(node.get("code").asText());
		}
		if(node.get("name") != null) {
			assetBudget.setName(node.get("name").asText());
		}
		if(node.get("description") != null) {
			assetBudget.setDescription(node.get("description").asText());
		}
		if(getJsonNodeId(node.get("kind")) != null) {
			AssetKind kind = assetKindRepository.findOne(getJsonNodeId(node.get("kind")));
			assetBudget.setKind(kind);
		}
		
		if(getJsonNodeId(node.get("category")) != null) {
			AssetCategory category = assetCategoryRepository.findOne(getJsonNodeId(node.get("category")));
			assetBudget.setCategory(category);
		}
		assetBudgetRepository.save(assetBudget);
		return assetBudget;
	}

	@Override
	public AssetBudget findOneAssetBudget(Long id) {
		return assetBudgetRepository.findOne(id);
	}

	@Override
	public AssetAllocation saveAssetAllocation(JsonNode node) {
		AssetAllocation assetAllocation = new AssetAllocation();
		
		copyAssetAllocationFromNode(node, assetAllocation);
		
		assetAllocationRepository.save(assetAllocation);
		
		return assetAllocation;
	}

	private AssetAllocation copyAssetAllocationFromNode(JsonNode node, AssetAllocation assetAllocation){
		if(node.get("fiscalYear") != null) {
			assetAllocation.setFiscalYear(node.get("fiscalYear").asInt());
		}
		
		if(getJsonNodeId(node.get("assetBudget")) != null) {
			AssetBudget assetBudget = assetBudgetRepository.findOne(getJsonNodeId(node.get("assetBudget")));
			assetAllocation.setAssetBudget(assetBudget);
		}
		
		if(getJsonNodeId(node.get("owner")) != null) {
			Organization owner = organizationRepository.findOne(getJsonNodeId(node.get("owner")));
			assetAllocation.setOwner(owner);
		}

		if(getJsonNodeId(node.get("parentOwner")) != null) {
			Organization parentOwner = organizationRepository.findOne(getJsonNodeId(node.get("parentOwner")));
			assetAllocation.setParentOwner(parentOwner);
		}

		
		if(getJsonNodeId(node.get("budgetType")) != null) {
			BudgetType budgetType = budgetTypeRepository.findOne(getJsonNodeId(node.get("budgetType")));
			assetAllocation.setBudgetType(budgetType);
		}
		
		if(getJsonNodeId(node.get("forObjective")) != null) {
			Objective forObjective = objectiveRepository.findOne(getJsonNodeId(node.get("forObjective")));
			assetAllocation.setForObjective(forObjective);
		}
		
		if(getJsonNodeId(node.get("forActivity")) != null) {
			Activity forActivity = activityRepository.findOne(getJsonNodeId(node.get("forActivity")));
			assetAllocation.setForActitvity(forActivity);
		}

		Integer oldQuantity = null;
		Long oldUnitBudget = null;
		Long adjustAmount = null;
		
		if(node.get("quantity") != null ) {
			oldQuantity = assetAllocation.getQuantity();
			assetAllocation.setQuantity(node.get("quantity").asInt());
		} else {
			assetAllocation.setQuantity(0);
		}
		
		if(node.get("unitBudget") != null ) {
			oldUnitBudget = assetAllocation.getUnitBudget();
			assetAllocation.setUnitBudget(node.get("unitBudget").asLong());
		} else {
			assetAllocation.setUnitBudget(0L);
		}
		
		BudgetProposal proposal = null;		
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + node.get("proposal"));
		if(getJsonNodeId(node.get("proposal")) != null) {
			proposal = budgetProposalRepository.findOne(getJsonNodeId(node.get("proposal")));
			assetAllocation.setProposal(proposal);
//			if(assetAllocation.getQuantity() != null && assetAllocation.getUnitBudget() != null) {
//				Long amount = assetAllocation.getQuantity() * assetAllocation.getUnitBudget();
//				if(proposal.getAmountAllocated() == null || proposal.getAmountAllocated() == 0) {
//					proposal.setAmountAllocated(amount);
//				} else {
//					// we update
//					if(oldQuantity != null && oldUnitBudget != null) {
//						Long oldAmount = oldQuantity * oldUnitBudget;
//						adjustAmount = amount - oldAmount;
//						
//						proposal.setAmountAllocated(proposal.getAmountAllocated() + adjustAmount);
//					}
//				}
//				
//				budgetProposalRepository.save(proposal);
//			}
			
		} else if(node.get("proposal") != null) {
			proposal = new BudgetProposal();
			proposal.setBudgetType(assetAllocation.getBudgetType());
			proposal.setOwner(assetAllocation.getParentOwner());
			proposal.setForObjective(assetAllocation.getForObjective());
//			if(assetAllocation.getQuantity() != null && assetAllocation.getUnitBudget() != null) {
//				Long amount = assetAllocation.getQuantity() * assetAllocation.getUnitBudget();
//				proposal.setAmountAllocated(amount);
//			}
			budgetProposalRepository.save(proposal);
			
			assetAllocation.setProposal(proposal);
		}
		
		
		
				

		// we'll update proposal at last
		Double sumAssetAllocation = 
				assetAllocationRepository.findSumBudgetOfPropsoal(proposal) != null ?
						assetAllocationRepository.findSumBudgetOfPropsoal(proposal).doubleValue() 
							: null;
		if(sumAssetAllocation != null) {
			proposal.setAmountAllocated(sumAssetAllocation);
			
		} else {
			proposal.setAmountAllocated(0.0);
		}
		budgetProposalRepository.save(proposal);
		
		// then make sure we'get a new Allocation Record
		AllocationRecord allocationRecord = allocationRecordRepository
				.findOneByBudgetTypeAndObjectiveAndIndex(
						assetAllocation.getBudgetType(),
						assetAllocation.getForObjective(), 0);
		
		if(allocationRecord == null) {
			allocationRecord = new AllocationRecord();
			allocationRecord.setBudgetType(assetAllocation.getBudgetType());
			allocationRecord.setForObjective(assetAllocation.getForObjective());
			allocationRecord.setIndex(0);
			allocationRecord.setAmountAllocated(0.0);
		}
		
		Double sumBudgetProposalAmountAllocated = budgetProposalRepository
				.findSumByBudgetTypeAndForObjective(
						assetAllocation.getBudgetType(),
						assetAllocation.getForObjective());
		if(sumBudgetProposalAmountAllocated != null) { 
			allocationRecord.setAmountAllocated(sumBudgetProposalAmountAllocated);
		} else {
			allocationRecord.setAmountAllocated(0.0);
		}
		allocationRecordRepository.save(allocationRecord);
		
		
		
		return assetAllocation;
	}
	
	@Override
	public List<BudgetProposal> findBudgetProposalByFiscalYearAndOwner_Id(
			Integer fiscalYear, Long ownerId) {
		Organization org = organizationRepository.findOne(ownerId);
		
		if(org == null) return null;
		
		Long searchOrgId = ownerId;
		
		if(org.getType() == OrganizationType.ส่วนในจังหวัด || org.getType() == OrganizationType.แผนกในอำเภอ) {
			searchOrgId = org.getParent().getId();
		}
		
		List<BudgetProposal> proposals = budgetProposalRepository.findBudgetProposalByFiscalYearAndOwner_Id(fiscalYear, searchOrgId);
		
		Collections.sort(proposals, new Comparator<BudgetProposal>() {
			@Override
			public int compare(BudgetProposal o1, BudgetProposal o2) {
				return o1.getForObjective().getCode()
						.compareTo(o2.getForObjective().getCode());
			}
		});
		
		return proposals;
	}
	
	


	@Override
	public BudgetProposal deleteBudgetProposal(Long id) {
		BudgetProposal proposal = budgetProposalRepository.findOne(id);
		if(proposal == null) return null;
		
		// now get the list of assetsallocation
		List<AssetAllocation> assetAllocations = assetAllocationRepository
				.findAllByParentOwner_IdAndForObjective_IdAndBudgetType_Id(
						proposal.getOwner().getId(), 
						proposal.getForObjective().getId(),
						proposal.getBudgetType().getId());
		
		if(assetAllocations != null) {
			assetAllocationRepository.delete(assetAllocations);
		}
		
		budgetProposalRepository.delete(proposal);
		
		// and then allocationRecord
		AllocationRecord allocationRecord = allocationRecordRepository
				.findOneByBudgetTypeAndObjectiveAndIndex(
						proposal.getBudgetType(),
						proposal.getForObjective(), 0);
		
		Double sumBudgetProposalAmountAllocated = budgetProposalRepository
				.findSumByBudgetTypeAndForObjective(
						allocationRecord.getBudgetType(),
						allocationRecord.getForObjective());

		ObjectMapper mapper = new ObjectMapper();
		JsonNode allocNode = mapper.createObjectNode();
				
		if(sumBudgetProposalAmountAllocated != null) { 
			((ObjectNode) allocNode).put("amountAllocated", sumBudgetProposalAmountAllocated);
			//allocationRecord.setAmountAllocated(sumBudgetProposalAmountAllocated);
		} else {
			((ObjectNode) allocNode).put("amountAllocated", 0L);
			//allocationRecord.setAmountAllocated(0L);
		}
		allocationRecordRepository.save(allocationRecord);
		updateAllocationRecord(allocationRecord.getId(), allocNode);

		
		return proposal;
	}

	@Override
	public AssetAllocation deleteAssetAllocation(Long id) {
		AssetAllocation assetAllocation = assetAllocationRepository.findOne(id);
		
		assetAllocationRepository.delete(assetAllocation);
		
		// have to update proposal! 
		BudgetProposal proposal = assetAllocation.getProposal();
		
		Long sum = assetAllocationRepository.findSumBudgetOfPropsoal(proposal);
		Double sumAssetAllocation = 0.0;
		if(sum != null) {
			sumAssetAllocation = sum.doubleValue();
		}
		
		if(sumAssetAllocation != null) {
			proposal.setAmountAllocated(sumAssetAllocation);
			
		} else {
			proposal.setAmountAllocated(0.0);
		}
		budgetProposalRepository.save(proposal);	

		// and then allocationRecord
		AllocationRecord allocationRecord = allocationRecordRepository
				.findOneByBudgetTypeAndObjectiveAndIndex(
						proposal.getBudgetType(),
						proposal.getForObjective(), 0);
		
		Double sumBudgetProposalAmountAllocated = budgetProposalRepository
				.findSumByBudgetTypeAndForObjective(
						allocationRecord.getBudgetType(),
						allocationRecord.getForObjective());

		ObjectMapper mapper = new ObjectMapper();
		JsonNode allocNode = mapper.createObjectNode();
				
		if(sumBudgetProposalAmountAllocated != null) { 
			((ObjectNode) allocNode).put("amountAllocated", sumBudgetProposalAmountAllocated);
			//allocationRecord.setAmountAllocated(sumBudgetProposalAmountAllocated);
		} else {
			((ObjectNode) allocNode).put("amountAllocated", 0L);
			//allocationRecord.setAmountAllocated(0L);
		}
		allocationRecordRepository.save(allocationRecord);
		updateAllocationRecord(allocationRecord.getId(), allocNode);
				
		
		return assetAllocation;
	}

	@Override
	public AssetAllocation updateAssetAllocation(JsonNode node) {
		AssetAllocation assetAllocation = assetAllocationRepository.findOne(getJsonNodeId(node));
		copyAssetAllocationFromNode(node, assetAllocation);
		assetAllocationRepository.save(assetAllocation);
		
		return assetAllocation;
	}

	@Override
	public List<AssetAllocation> findAssetAllocationByParentOwnerAndForObjectiveAndBudgetType(
			Long parentOwnerId, Long forObjectiveId, Long budgetTyeId) {
		return assetAllocationRepository.findAllByParentOwner_IdAndForObjective_IdAndBudgetType_Id(parentOwnerId, forObjectiveId, budgetTyeId);
	}

	@Override
	public void saveAssetAllocationCollection(JsonNode node) {
		Set<BudgetProposal> proposalSet = new HashSet<BudgetProposal>();
		Set<AllocationRecord> allocationRecordSet = new HashSet<AllocationRecord>();
		List<AssetAllocation> allocList = new ArrayList<AssetAllocation>();
		
		for(JsonNode assetAllocNode : node ) {
			AssetAllocation assetAlloc = assetAllocationRepository.findOne(getJsonNodeId(assetAllocNode));
			
			Organization owner = organizationRepository.findOne(getJsonNodeId(assetAllocNode.get("owner")));
			assetAlloc.setOwner(owner);
			
			Organization operator = organizationRepository.findOne(getJsonNodeId(assetAllocNode.get("operator")));
			assetAlloc.setOperator(operator);
			
			if(getJsonNodeId(assetAllocNode.get("category")) != null) {
				AssetCategory category = assetCategoryRepository.findOne(getJsonNodeId(assetAllocNode.get("category")));
				assetAlloc.setCategory(category);
			}
			
			assetAlloc.setQuantity(assetAllocNode.get("quantity").asInt());
			assetAlloc.setUnitBudget(assetAllocNode.get("unitBudget").asLong());
			
			allocList.add(assetAlloc);
			
			BudgetProposal proposal = assetAlloc.getProposal();
			proposalSet.add(proposal);
		}
	
		assetAllocationRepository.save(allocList);
		
		// have to update proposal! 
		for(BudgetProposal proposal : proposalSet) {
			Double sumAssetAllocation = assetAllocationRepository.findSumBudgetOfPropsoal(proposal).doubleValue();
			if(sumAssetAllocation != null) {
				proposal.setAmountAllocated(sumAssetAllocation);
				
			} else {
				proposal.setAmountAllocated(0.0);
			}
			budgetProposalRepository.save(proposal);	
			
			AllocationRecord allocationRecord = allocationRecordRepository
					.findOneByBudgetTypeAndObjectiveAndIndex(
							proposal.getBudgetType(),
							proposal.getForObjective(), 0);
			
			allocationRecordSet.add(allocationRecord);
			
		}
		
		// and allocation record!
		for(AllocationRecord allocationRecord : allocationRecordSet) {
			Double sumBudgetProposalAmountAllocated = budgetProposalRepository
					.findSumByBudgetTypeAndForObjective(
							allocationRecord.getBudgetType(),
							allocationRecord.getForObjective());
			ObjectMapper mapper = new ObjectMapper();
			JsonNode allocNode = mapper.createObjectNode();
			
			if(sumBudgetProposalAmountAllocated != null) { 
				((ObjectNode) allocNode).put("amountAllocated", sumBudgetProposalAmountAllocated);
				//allocationRecord.setAmountAllocated(sumBudgetProposalAmountAllocated);
			} else {
				((ObjectNode) allocNode).put("amountAllocated", 0L);
				//allocationRecord.setAmountAllocated(0L);
			}
			allocationRecordRepository.save(allocationRecord);
			updateAllocationRecord(allocationRecord.getId(), allocNode);
		}
	}

	@Override
	public List<AssetAllocation> findAssetAllocationByForObjectiveId(
			Long objectiveId) {
		
		return assetAllocationRepository.findAllByForObjectiveId(objectiveId);
	}
	
	

	@Override
	public List<AssetAllocation> findAssetAllocationByForObjectiveIdAndOperator(
			Long objectiveId, Organization operator) {
		
		if(operator.isSubSection()) {
			operator = operator.getParent();
		}
		
		return assetAllocationRepository.findAllByForObjectiveIdAndOperator(objectiveId, operator);
	}
	
	

	@Override
	public AssetAllocation findAssetAllocationById(Long id) {
		AssetAllocation assetAlloc = assetAllocationRepository.findOne(id);
		if(assetAlloc != null) {
			// load the collection
			assetAlloc.getAssetBudgetPlans().size();
			assetAlloc.getAssetStepReports().size();
		}
		
		return assetAlloc;
	}

	@Override
	public List<AssetMethod> findAssetMethodAll() {
		
		List<AssetMethod> methods =  assetMethodRepository.findAll();
		
		methods.size();
		for(AssetMethod method: methods) {
			method.getSteps().size();
		}
		
		return methods;
	}

	@Override
	public String saveAssetAllocationPlan(JsonNode node, Boolean saveResult) {
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d", new Locale("en", "US"));
		
		AssetAllocation asset = assetAllocationRepository.findOne(getJsonNodeId(node));
		if(node.get("contractedBudgetActual") != null) {
			
			asset.setContractedBudgetActual(node.get("contractedBudgetActual").asDouble());
		}
		
		 
		
		if(node.get("contractedBudgetPlan") != null) {
			asset.setContractedBudgetPlan(node.get("contractedBudgetPlan").asDouble());
		} 
		
		if(node.get("estimatedCost") != null) {
			asset.setEstimatedCost(node.get("estimatedCost").asDouble());
		}
		
		AssetMethod assetMethod = assetMethodRepository.findOne(getJsonNodeId(node.get("assetMethod")));
		Boolean newMethod;
		if(asset.getAssetMethod() != assetMethod){
			List<AssetStepReport> oldreports = asset.getAssetStepReports();
			asset.setAssetStepReports(new ArrayList<AssetStepReport>());
			
			//we'll have to delete the old one!
			if(oldreports != null && oldreports.size() > 0) {
				assetStepReportRepository.delete(oldreports);
			}
			
			asset.setAssetMethod(assetMethod);
			newMethod=true;
		} else {
			newMethod = false;
		}
		asset.setAssetMethod(assetMethod);
		Integer stepIndex = 0;
		
		for(JsonNode reportNode: node.get("assetStepReports")) {
			AssetStepReport report;
			logger.debug("stepIndex: " + stepIndex);
			logger.debug("newMethod: " + newMethod);
			if(newMethod) {
				report = new AssetStepReport();
				report.setStepOrder(stepIndex);
				report.setStep(assetMethod.getSteps().get(stepIndex++));
				report.setAssetAllocation(asset);
				asset.getAssetStepReports().add(report);
				
			} else {
				report = asset.getAssetStepReports().get(stepIndex);
				report.setStepOrder(stepIndex);
				stepIndex++;
			}
			
			
			if(!saveResult) {
				try {
					logger.debug(">>>>>> "  + report);
					report.setPlanBegin(df.parse(reportNode.get("planBegin").asText()));
					report.setPlanEnd(df.parse(reportNode.get("planEnd").asText()));
					
					logger.debug("saving reportID: " + report.getId() +  " +  PlanBegin:" + report.getPlanBegin() + " and PlanEnd: " + report.getPlanEnd());
					
				} catch (ParseException e) {
					
				}
			}
			
			if(saveResult) {
			
				try {
					logger.debug("saving reportID: " + report.getId() +  " +  ActualBegin:" + reportNode.get("actualBegin").asText() );
					report.setActualBegin(df.parse(reportNode.get("actualBegin").asText()));
					
				} catch (ParseException e) {
					report.setActualBegin(null);
				}
				
				try {
					logger.debug("saving reportID: " + report.getId() +  " +  ActualEnd:" + reportNode.get("actualEnd").asText() );
					report.setActualEnd(df.parse(reportNode.get("actualEnd").asText()));
				} catch (ParseException e) {
					report.setActualEnd(null);
				}
			}
			
			
			

		}
		
		//now 
		assetStepReportRepository.save(asset.getAssetStepReports());
		
		
		// then we'll have to deal with assetBudgetPlan!
		if(node.get("assetBudgetPlans").size() > 0) {
			int i = 0;
			
			if(asset.getAssetBudgetPlans() == null) {
				asset.setAssetBudgetPlans(new ArrayList<AssetBudgetPlan>());
			}
			
			for(JsonNode planNode : node.get("assetBudgetPlans")) {
				AssetBudgetPlan plan = null;
				try{
					if(asset.getAssetBudgetPlans().get(i) == null) {
						
						plan = new AssetBudgetPlan();
						plan.setAssetAllocation(asset);
						plan.setBudgetOrder(i);
						asset.getAssetBudgetPlans().add(plan);
					} else { 
						plan = asset.getAssetBudgetPlans().get(i);
					}
					
					logger.debug("" + plan);
					
				}catch (IndexOutOfBoundsException e) {
					plan = new AssetBudgetPlan();
					plan.setAssetAllocation(asset);
					plan.setBudgetOrder(i);
					asset.getAssetBudgetPlans().add(plan);
					
					
					logger.debug("" + plan);
				} 
				
				logger.debug(planNode.toString());
				
				plan.setPlanAmount(planNode.get("planAmount").asDouble());
				
				
				try {
					plan.setPlanDate(df.parse(planNode.get("planDate").asText()));
				} catch (ParseException e) {
					plan.setPlanDate(null);
				}
				
				try {
					plan.setPlanInstallmentDate(df.parse(planNode.get("planInstallmentDate").asText()));
				} catch (ParseException e) {
					plan.setPlanInstallmentDate(null);
				}
			
				if(saveResult) {
				
					if(planNode.get("actualAmount") != null) {
						plan.setActualAmount(planNode.get("actualAmount").asDouble());
					}
					
					
					try {
						plan.setActualDate(df.parse(planNode.get("actualDate").asText()));
					} catch (ParseException e) {
						plan.setActualDate(null);
					}
					
					try {
						plan.setActualInstallmentDate(df.parse(planNode.get("actualInstallmentDate").asText()));
					} catch (ParseException e) {
						plan.setActualInstallmentDate(null);
					}
					
					if(planNode.get("remark") != null) {
						plan.setRemark(planNode.get("remark").asText());
					}
					
				}
			i++;
			}
			
			Integer planSize = asset.getAssetBudgetPlans().size();
			
			
			logger.debug("i = " + i + " and planSize = " + planSize);
			
			while(i < planSize) {
				// now we must remove the rest
				AssetBudgetPlan removedPlan = asset.getAssetBudgetPlans().remove(i);
				planSize = asset.getAssetBudgetPlans().size();
			}
			
			
			// now we should be able to save 
			assetAllocationRepository.save(asset);
			
		}
		
		return "ok";
	}

	@Override
	public List<AssetStepReport> findAssetStepReportByAssetAllocationId(
			Long assetAllocationId) {
		AssetAllocation asset = assetAllocationRepository.findOne(assetAllocationId);
		asset.getAssetStepReports().size();
		
		
		return asset.getAssetStepReports();
	}

	@Override
	/**
	 * @author dbuaklee
	 * @return return array of Object with Object[0] = root Objective and Object[1] = List of [ActivityTargetId, sumPlan, sumResult]
	 * @param objId	Objective ID 
	 * @param orgId Organization Id
	 * @param startMonth start of fiscal month to search (0 = October, 11 = September)
	 * @param endMonth end of fiscal month to search (0 = October, 11 = September)
	 */
	public Object[] findObjectiveForM81R06Report(Long objId, Long orgId,
			Integer startMonth, Integer endMonth) {
		
		Object[] returnObjs = new Object[2];
		
		Objective objective = objectiveRepository.findOne(objId);
		if(objective == null) 
			return null;
		
		// now we'll load all chidren
		List<Objective> allObjectives = objectiveRepository.findAllLeftJoinChildrenByParentIdLike(objId, "%."+objective.getId()+".%");
		
		String allObjId = "";
		for(Objective obj : allObjectives) {
			allObjId += " " + obj.getId()+",";
		}
		
		logger.debug("ALL ID: " + allObjId);
		
		
		// then we need to get to activitity 
		List<Activity> allActivities = activityRepository.findAllActivityByListOfObjective(allObjectives);
		logger.debug("YYYYYYY " + allActivities.size());
		String allActId = "";
		for(Activity act : allActivities) {
			allActId += " " + act.getId()+",";
		}
		logger.debug("ALL ID: " + allActId);
		
		List<Object[]> targetValue;
		
		if(orgId == 0L) {
			targetValue = activityTargetRepository.findSumTargetByMonthAndActivities(0, 12, allActivities);
		} else {
			
			Organization org = organizationRepository.findOne(orgId);
			String queryOrg = OrganizationType.getChildrenQueryString(org);
			
			logger.debug("org Search = " + queryOrg);
			List<Organization> allWithChildren = organizationRepository.findAllCodeLike(queryOrg);
			
			
			targetValue = activityTargetRepository.findSumTargetByMonthAndActivitiesAndOrganization(0, 12, allActivities, allWithChildren);
		}
		for(Object[] result : targetValue) {
			Long targetId = (Long) result[0];
			ActivityTarget t = activityTargetRepository.findOne(targetId);
			logger.debug("found target:" + t.getId());
			
			if(t!=null) {
				if(t.getActivity().getFilterTargets() == null) {
					t.getActivity().setFilterTargets(new ArrayList<ActivityTarget> ());
				}
				
				t.getActivity().getFilterTargets().add(t);
				t.getActivity().setShowInTree(true);

			}
		}
		
		
		for(Activity activity: allActivities) {
			if(activity.getParent() == null) {
				
								
				if(activity.getForObjective().getFilterActivities() == null) {
					activity.getForObjective().setFilterActivities(new ArrayList<Activity> () );
				}
				
				activity.getForObjective().getFilterActivities().add(activity);
			}
			
			
		}
		
		returnObjs[0] = objective;
		returnObjs[1] = targetValue;
		
		return returnObjs;
	}

	@Override
	public List<Objective> findObjectivesByFiscalyearAndTypeIdForM81R05Report(Integer fiscalYear, Long orgId) {
		
		Organization org = null;
		List<Objective> แผนปฏิบัติการ_list = null;
		List<Objective> กิจกรรมหลัก_list = new ArrayList<Objective>();
		List<Objective> ผลผลิต_list = new ArrayList<Objective>();
		if(orgId > 0) {
			org = organizationRepository.findOneById(orgId);
			แผนปฏิบัติการ_list = objectiveRepository.findAllByOwnerAndfiscalYear(org, fiscalYear); 
		} else {
			// กรณีเลือกทุกหน่วยงาน
			แผนปฏิบัติการ_list = objectiveRepository.findAllByFiscalYearAndType_id(fiscalYear, 105L);
		}
		
		
		
		for(Objective แผนปฏิบัติการ : แผนปฏิบัติการ_list) {
				for(Objective กิจกรรมรอง : แผนปฏิบัติการ.getChildren()) {
				Hibernate.initialize(กิจกรรมรอง);
				
				List<Activity> activities = null;
				if(orgId == 0L) {
					activities = activityRepository.findAllByForObjective(กิจกรรมรอง);
					
					ObjectiveOwnerRelation ownerRelation = objectiveOwnerRelationRepository.findByObjective_Id(แผนปฏิบัติการ.getId());
					if(ownerRelation != null) {
						Hibernate.initialize(ownerRelation.getOwners());
						แผนปฏิบัติการ.setOwner(ownerRelation.getOwners());
					}
					
				} else {
					// about to query
					logger.info("qeurying for activity " + กิจกรรมรอง.getName());
					activities = activityRepository.findAllByForObjectiveAndOwnerOrRegulator(กิจกรรมรอง, orgId);
					logger.info(" -- has " + activities.size()  + " กิจกรรม");
					
				}
				
				กิจกรรมรอง.setFilterActivities(activities);
				
				for(Activity กิจกรรมย่อย : activities ) {
					Hibernate.initialize(กิจกรรมย่อย.getChildren());
					
					for(Activity กิจกรรมเสริม : กิจกรรมย่อย.getChildren()) {
						Hibernate.initialize(กิจกรรมเสริม.getChildren());
					}
					
				}
			}
			
			แผนปฏิบัติการ.setShowInTree(true);
				
				
			if(!กิจกรรมหลัก_list.contains(แผนปฏิบัติการ.getParent()) ) {
				กิจกรรมหลัก_list.add(แผนปฏิบัติการ.getParent());
				
				if(!ผลผลิต_list.contains(แผนปฏิบัติการ.getParent().getParent())) {
					ผลผลิต_list.add(แผนปฏิบัติการ.getParent().getParent());
				
				}
			}
		}
		
		return ผลผลิต_list;
		
		
		
		
		
		
//		List<Objective> ผลผลิตทั้งหมด = objectiveRepository.findAllByFiscalYearAndType_id(fiscalYear, 103L);
//		for(Objective ผลผลิต : ผลผลิตทั้งหมด) {
//			Hibernate.initialize(ผลผลิต.getParent());
//			Hibernate.initialize(ผลผลิต.getParent().getType());
//			
//			for(Objective กิจกรรมหลัก : ผลผลิต.getChildren()) {
//				for(Objective แผนปฏิบัติการ : กิจกรรมหลัก.getChildren()) {
//					ObjectiveOwnerRelation ownerRelation = objectiveOwnerRelationRepository.findByObjective_Id(แผนปฏิบัติการ.getId());
//					
//					if(ownerRelation != null) {
//						Hibernate.initialize(ownerRelation.getOwners());
//						แผนปฏิบัติการ.setOwner(ownerRelation.getOwners());
//					}
//					for(Objective กิจกรรมรอง : แผนปฏิบัติการ.getChildren()) {
//						Hibernate.initialize(กิจกรรมรอง);
//						List<Activity> activities = activityRepository.findAllByForObjective(กิจกรรมรอง);
//						กิจกรรมรอง.setFilterActivities(activities);
//						
//						for(Activity กิจกรรมย่อย : activities ) {
//							Hibernate.initialize(กิจกรรมย่อย.getChildren());
//							
//							for(Activity กิจกรรมเสริม : กิจกรรมย่อย.getChildren()) {
//								Hibernate.initialize(กิจกรรมเสริม.getChildren());
//							}
//							
//						}
//						
//					}
//				}
//			}
//			
//		}
		
//		return ผลผลิตทั้งหมด;
	}

	@Override
	public List<AssetAllocation> findAssetAllocationForReportM81r08(
			Integer fiscalYear) {
		List<AssetAllocation> assetAllocations = assetAllocationRepository.findAlByFiscalyear(fiscalYear);
		
		for(AssetAllocation alloc : assetAllocations) {
			alloc.getAssetBudgetPlans().size();
			alloc.getAssetStepReports().size();
			alloc.getProposal().getForObjective();
		}
		
		return assetAllocations;
	}
	
	@Override
	public List<AssetAllocation> findAssetAllocationForReportM81r11(
			Integer fiscalYear, ThaicomUserDetail currentUser) {
	
		Organization searchOrg = currentUser.getWorkAt();
		if(searchOrg.getType() == OrganizationType.แผนก) {
			searchOrg = searchOrg.getParent();
		}
		
		List<AssetAllocation> assetAllocations = assetAllocationRepository.findAlByFiscalyearAndOwner(fiscalYear, searchOrg);
		
		for(AssetAllocation alloc : assetAllocations) {
			alloc.getAssetBudgetPlans().size();
			alloc.getAssetStepReports().size();
			alloc.getProposal().getForObjective();
		}
		
		return assetAllocations;
	}

	@Override
	public Objective findObjectivesByFiscalyearAndTypeIdForM82R01Report(
			Integer fiscalYear) {
		
		
		// now we'll have to recursively get all children?
		List<Objective> allObjective = objectiveRepository.findAllLeftJoinChildrenByFiscalYear(fiscalYear);
		
		Objective root = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		
		return root;
	}
	
	
	@Override
	public Objective findObjectivesByFiscalyearAndTypeIdForM81R07Report(
			Integer fiscalYear) {
		// now we'll have to recursively get all children?
		List<Objective> allObjective = objectiveRepository.findAllLeftJoinChildrenAndFetchBudgetProposalByFiscalYear(fiscalYear);
	
		Objective root = objectiveRepository.findRootOfFiscalYear(fiscalYear);
		
		return root;
	}

	
	
	@Override
	public Iterable<Object[]> findAllSumBudgetPlanByFiscalYear(
			Integer fiscalYear) {
		return activityPerformanceRepository.findSumBudgetAllocatedByFiscalYear(fiscalYear);
	}

	@Override
	public Iterable<Object[]> findAllSumBudgetUsedByFiscalYear(
			Integer fiscalYear) {
		return budgetUsageFromExternalRepository.findSumBudgetUsedByFiscalYear(fiscalYear);
	}

	
	
	@Override
	public Iterable<Object[]> findAllSumMonthlyBudgetByFiscalYear(
			Integer fiscalYear) {
		return activityTargetResultRepository.findBudgetResultByFiscalYear(fiscalYear);
	}

	@Override
	public List<Organization> findAllOrganization() {
		List<Organization> organizations = organizationRepository.findAllLeftJoinChildren();
		
		return organizations;
		
	}

	@Override
	public Organization findOrganizationRoot() {
		
		List<Organization> organizations = organizationRepository.findAllLeftJoinChildren();
		Organization root = organizationRepository.findRoot();
		
		return root;
	}

	@Override
	public Page<AssetCategory> findAllAssetCategories(PageRequest pageRequest,
			String query) {
		// TODO Auto-generated method stub
		return (Page<AssetCategory>) assetCategoryRepository.findAll(pageRequest);
	}
	
	

	@Override
	public Page<AssetCategory> findAllAssetCategories(PageRequest pageRequest) {
		return this.findAllAssetCategories(pageRequest, "%");
	}

	@Override
	public AssetCategory findOneAssetCategory(Long id) {
		return assetCategoryRepository.findOne(id);
	}

	@Override
	public AssetCategory updateAssetCategory(JsonNode node) {
		AssetCategory category = assetCategoryRepository.findOne(getJsonNodeId(node));
		if(category != null) {
			category.setName(node.get("name").asText());
			category.setCode(node.get("code").asText());
		}
		
		assetCategoryRepository.save(category);
		
		return category;
	}

	@Override
	public AssetCategory saveAssetCategory(JsonNode node) {
		AssetCategory category = new AssetCategory();
		if(category != null) {
			category.setName(node.get("name").asText());
			category.setCode(node.get("code").asText());
		}
		
		assetCategoryRepository.save(category);
		
		return category;	}

	@Override
	public AssetCategory deleteAssetCategory(Long id) {
		AssetCategory category = assetCategoryRepository.findOne(id);
		if(category != null) {
			// now find all asset budget assign to this category 
			// and set them to null
			
			assetBudgetRepository.removeCategory(category);
			
			assetCategoryRepository.delete(category);
		}
		return category;
	}

	@Override
	public List<AssetCategory> findAssetCategoryAll() {

		return (List<AssetCategory>) assetCategoryRepository.findAll(new Sort(Sort.Direction.ASC, "code"));
	}

	@Override
	public List<ActivityTargetReport> findActivityTargetReportByOwnerOrRegulator(
			Organization workAt, Integer fiscalYear) {
		List<ActivityTargetReport> reports = activityTargetReportRepository.findAllByActivityOwnerOrRegulatorAndFiscalYear(workAt, fiscalYear);
		return reports;
	}



	
	
	
	
	

}
