package biz.thaicom.eBudgeting.services;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.bull.javamelody.MonitoredWithSpring;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import biz.thaicom.eBudgeting.models.bgt.AllocationRecord;
import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.AssetBudget;
import biz.thaicom.eBudgeting.models.bgt.AssetCategory;
import biz.thaicom.eBudgeting.models.bgt.AssetGroup;
import biz.thaicom.eBudgeting.models.bgt.AssetKind;
import biz.thaicom.eBudgeting.models.bgt.AssetMethod;
import biz.thaicom.eBudgeting.models.bgt.AssetStepReport;
import biz.thaicom.eBudgeting.models.bgt.AssetType;
import biz.thaicom.eBudgeting.models.bgt.BudgetCommonType;
import biz.thaicom.eBudgeting.models.bgt.BudgetProposal;
import biz.thaicom.eBudgeting.models.bgt.BudgetSignOff;
import biz.thaicom.eBudgeting.models.bgt.BudgetType;
import biz.thaicom.eBudgeting.models.bgt.FiscalBudgetType;
import biz.thaicom.eBudgeting.models.bgt.FormulaColumn;
import biz.thaicom.eBudgeting.models.bgt.FormulaStrategy;
import biz.thaicom.eBudgeting.models.bgt.ObjectiveBudgetProposal;
import biz.thaicom.eBudgeting.models.bgt.ProposalStrategy;
import biz.thaicom.eBudgeting.models.bgt.RequestColumn;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityPerformance;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetResult;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.ObjectiveDetail;
import biz.thaicom.eBudgeting.models.pln.ObjectiveName;
import biz.thaicom.eBudgeting.models.pln.ObjectiveRelations;
import biz.thaicom.eBudgeting.models.pln.ObjectiveTarget;
import biz.thaicom.eBudgeting.models.pln.ObjectiveType;
import biz.thaicom.eBudgeting.models.pln.TargetUnit;
import biz.thaicom.eBudgeting.models.pln.TargetValue;
import biz.thaicom.eBudgeting.models.pln.TargetValueAllocationRecord;
import biz.thaicom.eBudgeting.models.webui.Breadcrumb;
import biz.thaicom.eBudgeting.repositories.ObjectiveRelationsRepository;
import biz.thaicom.security.models.ThaicomUserDetail;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

@MonitoredWithSpring
public interface EntityService {
	
	//ObjectiveType
	public ObjectiveType findObjectiveTypeById(Long id);
	public Set<ObjectiveType> findChildrenObjectiveType(ObjectiveType type);
	public ObjectiveType findParentObjectiveType(ObjectiveType type);
	public List<Integer> findObjectiveTypeRootFiscalYear();
	public List<ObjectiveType> findObjectiveTypeByFiscalYearEager(Integer fiscalYear, Long parentId);
	public String findObjectiveTypeChildrenNameOf(Long id);
	public ObjectiveType findDeepObjectiveTypeById(Long id);
	
	public String findObjectiveChildrenTypeName(Long id);

	
	//Objective
	public List<Objective> findObjectivesOf(ObjectiveType type);
	public List<Objective> findObjectiveChildren(Objective objective);
	public Objective findParentObjective(Objective objective);
	public Objective findOjectiveById(Long id);
	public List<Objective> findObjectiveChildrenByObjectiveId(Long id);
	public List<Objective> findAvailableObjectiveChildrenByObjectiveId(Long id);
	public List<Objective> findAllObjectiveChildren(Integer fiscalYear, Long id);
	
	
	public List<Objective> findObjectiveByOwnerAndFiscalYear(
			Organization workAt, Integer fiscalYear);
	
	public List<Objective> findObjectiveByActivityOwnerAndFiscalYear(
			Organization workAt, Integer fiscalYear);
	
	public List<Objective> findObjectiveByActivityRegulatorAndFiscalYear(
			Organization workAt, Integer fiscalYear);
	
	public List<Objective> findObjectiveChildrenByActivityRegulatorAndParentId(
			Organization workAt, Long id);

	public List<Objective> findObjectiveChildrenByActivityOwnerAndParentId(
			Organization workAt, Long id);
	
	public List<Objective> findRootObjectiveByFiscalyear(Integer fiscalYear, Boolean eagerLoad);
	public Objective findOneRootObjectiveByFiscalyear(Integer fiscalYear);
	
	
	public List<Objective> findRootFiscalYear();
	public List<Breadcrumb> createBreadCrumbObjective(String string,
			Integer fiscalYear, Objective objective);
	public Objective objectiveDoEagerLoad(Long ObjectiveId);
	public Objective updateObjective(Objective objective);
	
	public List<Objective> findChildrenObjectivewithBudgetProposal(Integer fiscalYear, Long ownerId, Long objectiveId, Boolean isChildrenTraversal);
	public List<Objective> findChildrenObjectivewithObjectiveBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId, Boolean isChildrenTraversal);
	
	
	public List<Objective> findFlatChildrenObjectivewithBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId);

	List<Objective> findFlatChildrenObjectivewithObjectiveBudgetProposal(
			Integer fiscalYear, Long ownerId, Long objectiveId);
	
	public List<Objective> findFlatChildrenObjectivewithBudgetProposalAndAllocation(
			Integer fiscalYear, Long objectiveId, Boolean isFindObjectiveBudget);

	public Objective addBudgetTypeToObjective(Long id, Long budgetTypeId);
	public Objective removeBudgetTypeToObjective(Long id, Long budgetTypeId);
	public Objective updateObjectiveFields(Long id, String name, String code);
	public Objective saveObjective(JsonNode objective);
	public Objective newObjectiveWithParam(String name, String code, Long parentId,
			Long typeId, String parentPath, Integer fiscalYear);
	public Objective deleteObjective(Long id, Boolean nameCascade);
	public void addTargetToObjective(Long id, Long targetId);
	
	public List<Objective> findObjectivesByFiscalyearAndTypeId(
			Integer fiscalYear, Long typeId);
	public Objective updateObjectiveParent(Long id, Long parentId);
	public Objective objectiveAddReplaceUnit(Long id, Long unitId);
	
	public List<ObjectiveRelationsRepository> findObjectiveRelationsByFiscalYearAndChildTypeRelation(
			Integer fiscalYear, Long childTypeId);
	
	public String initFiscalYear(Integer fiscalYear);
	public Page<Objective> findObjectivesByFiscalyearAndTypeId(
			Integer fiscalYear, Long typeId, Pageable pageable);
	public Page<Objective> findObjectivesByFiscalyearAndTypeId(
			Integer fiscalYear, Long typeId,
			String query, Pageable pageable);
	
	
	public ObjectiveTarget addUnitToObjective(Long objectiveId, Long unitId, Integer isSumable);
	public String removeUnitFromObjective(Long objectiveId, Long targetId);
	public Objective objectiveAddChildObjectiveName(Long parentId, Long nameId);
	public List<List<Objective>> findObjectivesByFiscalyearAndTypeIdAndInitBudgetProposal(
			Integer fiscalYear, long l, Organization organization);
	
	public List<Objective> findChildrenObjectivewithAllocationRecords(
			Long objectiveId);
	
	public List<Objective> findObjectiveByActivityTargetReportOfOrganizationAndFiscalYear(
			Organization workAt, Integer fiscalYear);
	public List<Objective> findObjectiveByActivityTargetReportOfOrganizationAndFiscalYearNoReportCurrentMonth(
			Organization workAt, Integer fiscalYear);
	public List<Objective> findObjectiveAllByFiscalYear(Integer fiscalYear);
	public List<Objective> findObjectiveHasBudgetAssetByFiscalYear(
			Integer fiscalYear);
	public List<ActivityTargetResult> findActivityTargetResultByReportAndFiscalMonth(
			Long targetReportId, Integer fiscalMonth);
	
	// Specific for Excel Report!
	
	public List<Objective> findObjectivesByFiscalyearAndTypeIdForM81R05Report(Integer fiscalYear);
	public Objective findObjectivesByFiscalyearAndTypeIdForM82R01Report(
			Integer fiscalYear);

	
	//BudgetType
	public List<BudgetType> findRootBudgetType();
	public BudgetType findBudgetTypeById(Long id);
	public BudgetType findBudgetTypeEagerLoadById(Long id, Boolean isLoadParent);
	public List<Integer> findFiscalYearBudgetType();
	public List<Breadcrumb> createBreadCrumbBudgetType(String prefix,
			Integer fiscalYear, BudgetType budgetType);
	public List<BudgetType> findAllMainBudgetTypeByFiscalYear(Integer fiscalYear);
	public Page<BudgetType> findBudgetTypeByLevelAndMainType(Integer fiscalYear, Integer level,
			Long typeId, String query, Pageable pageable);
	public List<BudgetType> findBudgetTypeByLevel(Integer fiscalYear, Integer level);
	public BudgetType saveBudgetType(JsonNode node);
	public BudgetType updateBudgetType(JsonNode node);
	public void deleteBudgetType(Long id);
	
	
	//FiscalBudgetType
	public void initFiscalBudgetType(Integer fiscalYear);
	
	//FormulaStrategy
	public List<FormulaStrategy> findFormulaStrategyByfiscalYearAndTypeId(
			Integer fiscalYear, Long budgetTypeId);
	public FormulaStrategy saveFormulaStrategy(JsonNode strategy);
	public void deleteFormulaStrategy(Long id);
	public FormulaStrategy updateFormulaStrategy(JsonNode strategy);
	public List<FormulaStrategy> findAllFormulaStrategyByfiscalYearAndBudgetType_ParentPathLike(
			Integer fiscalYear, String parentPath);
	public List<FormulaStrategy> findAllFormulaStrategyByfiscalYearAndIsStandardItemAndBudgetType_ParentPathLike(
			Integer fiscalYear, Boolean isStandardItem, Long budgetTypeId, String parentPath);


	
	
	//FormulaColumn
	public void deleteFormulaColumn(Long id);
	public FormulaColumn saveFormulaColumn(FormulaColumn formulaColumn);
	public FormulaColumn updateFormulaColumn(FormulaColumn formulaColumn);
	
	//BudgetProposal
	public BudgetProposal findBudgetProposalById(Long budgetProposalId);
	public BudgetProposal saveBudgetProposal(JsonNode proposal, ThaicomUserDetail currentUser);
	public List<BudgetProposal> findBudgetProposalByObjectiveIdAndBudgetTypeId(Long objectiveId, Long budgetTypeId);
	public BudgetProposal deleteBudgetProposal(Long id);
	public List<BudgetProposal> findBudgetProposalByFiscalYearAndOwner_Id(
			Integer fiscalYear, Long ownerId);
	
	
	//ProposalStrategy
	public List<ProposalStrategy> findProposalStrategyByBudgetProposal(
			Long budgetProposalId);
	public List<ProposalStrategy> findProposalStrategyByFiscalyearAndObjective(
			Integer fiscalYear, Long ownerId, Long objectiveId);
	public List<ProposalStrategy> findAllProposalStrategyByFiscalyearAndObjective(
			Integer fiscalYear, Long objectiveId);
	public ProposalStrategy deleteProposalStrategy(Long id);
	public ProposalStrategy updateProposalStrategy(Long id,
			JsonNode rootNode) throws JsonParseException, JsonMappingException, IOException;

	
	//RequestColumn
	public RequestColumn saveRequestColumn(RequestColumn requestColumn);
	
	//AllocationRecord
	public String initAllocationRecord(Integer fiscalYear, Integer round);
	public AllocationRecord updateAllocationRecord(Long id, JsonNode data);
	public AllocationRecord saveAllocationRecord(JsonNode data);
	public List<AllocationRecord> findAllocationRecordByObjective(Objective o);
	public AllocationRecord saveAllocationRecordWithProposals(
			JsonNode allocationRecord, JsonNode proposals);
	
	
	//BudgetReserved
	public String initReservedBudget(Integer fiscalYear);
	
	public Boolean updateBudgetProposalAndReservedBudget(JsonNode data);
	
	
	//ObjectiveName
	public ObjectiveName saveObjectiveName(JsonNode node);
	public ObjectiveName updateObjectiveName(JsonNode node);
	Page<ObjectiveName> findAllObjectiveNameByFiscalYearAndTypeId(
			Integer fiscalYear, Long typeId, PageRequest pageRequest);
	public ObjectiveName findOneObjectiveName(Long id);
	public ObjectiveName deleteObjectiveName(Long id);
	
	public List<ObjectiveName> findAvailableObjectiveNameChildrenByObejective(Long id, String searchQuery);
	public ObjectiveTarget addUnitToObjectiveName(Long id, Long unitId,
			Integer isSumable);
	public String removeUnitFromObjectiveName(Long id, Long targetId);
	
	
	
	
	//ObjectiveTarget
	public List<ObjectiveTarget> findAllObjectiveTargets();
	public List<ObjectiveTarget> findAllObjectiveTargetsByFiscalyear(Integer fiscalYear);
	public ObjectiveTarget saveObjectiveTarget(ObjectiveTarget objectiveTarget);
	public ObjectiveTarget updateObjectiveTarget(ObjectiveTarget objectiveTarget);
	public ObjectiveTarget deleteObjectiveTarget(ObjectiveTarget objectiveTarget);
	public ObjectiveTarget findOneObjectiveTarget(Long id);
	public ObjectiveTarget updateObjectiveTarget(JsonNode node);
	public ObjectiveTarget saveObjectiveTarget(JsonNode node);
	public ObjectiveTarget deleteObjectiveTarget(Long id);

	//TargetUnit
	public List<TargetUnit> findAllTargetUnits();
	public Page<TargetUnit> findAllTargetUnits(PageRequest pageRequest);
	public TargetUnit saveTargetUnits(TargetUnit targetUnit);
	public TargetUnit updateTargetUnit(TargetUnit targetUnit);
	public TargetUnit deleteTargetUnit(TargetUnit targetUnit);
	public TargetUnit findOneTargetUnit(Long id);
	public TargetUnit updateTargetUnit(JsonNode node);
	public TargetUnit saveTargetUnit(JsonNode node);
	public TargetUnit deleteTargetUnit(Long id);
	
	//TargetValue
	public TargetValue saveTargetValue(JsonNode node, Organization workAt) throws 	Exception;
	public void saveLotsTargetValue(JsonNode node);
	
	//TargetValueAllocationRecord
	public TargetValueAllocationRecord saveTargetValueAllocationRecord(JsonNode node,
			Organization workAt);
	
	//ObjectiveRelations
	public ObjectiveRelations saveObjectiveRelations(JsonNode relation);
	public ObjectiveRelations updateObjectiveRelations(Long id,
			JsonNode relation);
	public List<ObjectiveRelationsRepository> findObjectiveRelationsByFiscalYearAndChildTypeRelationWithObjectiveIds(
			Integer fiscalYear, Long parentTypeId, List<Long> ids);
	public String mappedUnit();
	
	//BudgetCommonType
	public List<BudgetCommonType> findAllBudgetCommonTypes(Integer fiscalYear);
	public BudgetCommonType findOneBudgetCommonType(Long id);
	public BudgetCommonType updateBudgetCommonType(JsonNode node);
	public BudgetCommonType saveBudgetCommonType(JsonNode node);
	public BudgetCommonType deleteBudgetCommonType(Long id);
	
	
	//ObjectiveBudgetProposal
	public List<ObjectiveBudgetProposal> findObjectiveBudgetproposalByObjectiveIdAndOwnerId(
			Long objectiveId, Long id);
	public ObjectiveBudgetProposal saveObjectiveBudgetProposal(
			Organization workAt, JsonNode node);
	public ObjectiveBudgetProposal deleteObjectiveBudgetProposal(Long id);

	public List<List<Objective>> findObjectivesByFiscalyearAndTypeIdAndInitObjectiveBudgetProposal(
			Integer fiscalYear, long typeid, Organization workAt);

	
	
	
	//FiscalBudgetType
	public List<FiscalBudgetType> findAllFiscalBudgetTypeByFiscalYear(
			Integer fiscalYear);
	public String updateFiscalBudgetTypeIsMainBudget(Integer fiscalYear, List<Long> idList);
	public List<FiscalBudgetType> findAllFiscalBudgetTypeByFiscalYearUpToLevel(
			Integer fiscalYear, Integer level);
	
	
	//BudgetSignOff
	public BudgetSignOff findBudgetSignOffByFiscalYearAndOrganization(
			Integer fiscalYear, Organization workAt);
	public Long findSumTotalBudgetProposalOfOwner(Integer fiscalYear,
			Organization workAt);
	public Long findSumTotalObjectiveBudgetProposalOfOwner(Integer fiscalYear,
			Organization workAt);
	public BudgetSignOff updateBudgetSignOff(Integer fiscalYear, ThaicomUserDetail currentUser,
			String command);
	
	
	//ObjectiveDetail
	public ObjectiveDetail findOneObjectiveDetail(Long id);
	public ObjectiveDetail updateObjectiveDetail(JsonNode node, Organization owner);
	public ObjectiveDetail saveObjectiveDetail(JsonNode node, Organization owner);
	public ObjectiveDetail deleteObjectiveDetail(Long id);
	public ObjectiveDetail findOneObjectiveDetailByObjectiveIdAndOwner(Long objectiveId,
			ThaicomUserDetail currentUser);
	
	//Organization
	public List<Organization> findOrganizationTopLevelByName(String query);
	public List<Organization> findOrganizationByNameAndCode(String query, String code);
	public List<Organization> findOrganizationByObjectiveOwner(Long objectiveId);
	public List<Organization> saveObjectiveOwners(Long id, Long[] ownerIds);
	public List<Organization> findOrganizationByNameAndParent_Id(String query,
			Long parentId);
	public List<Organization> findOrganizationByProvinces(String query);
	public Organization findOrganizationById(Long id);
	public List<Organization> findOrganizationChildrenOrSiblingOf(
			Organization workAt);
	public Organization findOrganizationParentOf(Organization org);
	public List<Organization> findOrganizationByNameAndParent_IdWithProcuremnt(
			String query, Long parentId);
	
	public List<Organization> findOrganizationProvinces();

	public List<Organization> findAllOrganization();
	public Organization findOrganizationRoot();


	
	
	// Activity
	public List<Activity> findActivityByOwnerAndForObjective(
			Organization workAt, Long objectiveId);
	public Activity findOneActivity(Long id);
	public Activity updateActivity(JsonNode node);
	public Activity saveActivity(JsonNode node, Organization owner);
	public Activity deleteActivity(Long id);
	public List<ActivityPerformance> findActivityPerformancesByOwnerAndObjectiveId(
			Organization workAt, Long objectiveId);
	public List<Activity> findActivityByRegularAndObjectiveId(
			Organization workAt, Long objectiveId);
	
	//ActivityTargetReport
	public List<ActivityTargetReport> findActivityTargetReportByTargetId(
			Long targetId);
	public List<ActivityTargetReport> saveActivityTargetReportByTargetId(
			Long targetId, JsonNode node, Long parentOrgId);
	public List<ActivityTargetReport> findActivityTargetReportByTargetIdAndOwnerId(
			Long targetId, Long ownerId);
	public List<ActivityTargetReport> findActivityTargetReportByTarget_IdAndParentOrgId(
			Long activityTargetId, Long parentOrgId);
	public ActivityTargetReport findActivityTargetReportById(Long id);
	public List<ActivityTargetReport> findActivityTargetReportByTargetIdAndReportLevel(
			Long targetId, int reportLevel);
	public ActivityTargetReport saveActivityTargetReportPlan(Long id, JsonNode node);
	
	
	public List<Objective> findObjectiveLoadActivityByParentObjectiveIdAndReportLevel(
			Long objectiveId, Long ownerId);

	
	//ActivityTargetResult
	public ActivityTargetResult saveActivityTargetResult(JsonNode node,
			ThaicomUserDetail currentUser);
	public ActivityTargetResult findActivityTargetResultById(Long id);
	public ActivityTargetResult findActivityTargetResultByReportAndFiscalMonthAndBgtResult(
			Long targetReportId, Integer fiscalMonth);

	
	
	// Asset
	public AssetGroup findAssetGroupById(Long id);
	public List<AssetGroup> findAssetGroupAll();
	
	public List<AssetType> findAssetTypeByAssetGroupId(Long groupId);
	public List<AssetKind> findAssetKindByAssetTypeId(Long typeId);

	// AssetBudget
	public List<AssetBudget> findAssetBudgetByKindId(Long kindId);
	public AssetBudget saveAssetBudget(JsonNode node);
	public AssetBudget deleteAssetBudget(Long id);
	public AssetBudget updateAssetBudget(JsonNode node);
	public AssetBudget findOneAssetBudget(Long id);
	
	// AssetAllocation
	public AssetAllocation findAssetAllocationById(Long id);
	public AssetAllocation saveAssetAllocation(JsonNode node);
	public String saveAssetAllocationPlan(JsonNode node, Boolean saveResult);
	public AssetAllocation updateAssetAllocation(JsonNode node);
	public List<AssetAllocation> findAssetAllocationByParentOwnerAndForObjectiveAndBudgetType(
			Long parentOwnerId, Long forObjectiveId, Long budgetTyeId);
	public AssetAllocation deleteAssetAllocation(Long id);
	public void saveAssetAllocationCollection(JsonNode node);
	public List<AssetAllocation> findAssetAllocationByForObjectiveId(
			Long objectiveId);
	public List<AssetStepReport> findAssetStepReportByAssetAllocationId(
			Long assetAllocationId);
	public List<AssetAllocation> findAssetAllocationByForObjectiveIdAndOperator(
			Long objectiveId, Organization workAt);
	public List<AssetAllocation> findAssetAllocationForReportM81r08(
			Integer fiscalYear);
	
	
	// AssetMethod
	public List<AssetMethod> findAssetMethodAll();
	
	
	public Page<AssetCategory> findAllAssetCategories(PageRequest pageRequest, String query);
	public Page<AssetCategory> findAllAssetCategories(PageRequest pageRequest);
	public AssetCategory findOneAssetCategory(Long id);
	public AssetCategory updateAssetCategory(JsonNode node);
	public AssetCategory saveAssetCategory(JsonNode node);
	public AssetCategory deleteAssetCategory(Long id);
	public List<AssetCategory> findAssetCategoryAll();


	










	
	
	


	
	
	










	





	

	











	









	


	


	

	
	
}
