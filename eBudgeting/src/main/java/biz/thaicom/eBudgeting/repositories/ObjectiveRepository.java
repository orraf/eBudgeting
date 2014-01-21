package biz.thaicom.eBudgeting.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.ObjectiveName;
import biz.thaicom.eBudgeting.models.pln.ObjectiveType;

public interface ObjectiveRepository extends PagingAndSortingRepository<Objective, Long>, JpaSpecificationExecutor<Objective>{
	public List<Objective> findByTypeId(Long id);

	public List<Objective> findByParentIdAndFiscalYearAndParent_Name(Long id, Integer fiscalYear, String parentName);

	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE objective.name = 'ROOT' " +
			"")
	public List<Objective> findRootFiscalYear();
	
	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE objective.name='ROOT' and fiscalYear=?1 " +
			"")
	public Objective findRootOfFiscalYear(Integer fiscalYear);
	
	
	
//	@Query("" +  
//			"SELECT objective, proposal " +
//			"FROM BudgetProposal proposal " +
//			"	RIGHT OUTER JOIN proposal.owner owner with owner.id = ?2 " +
//			"	RIGHT OUTER JOIN proposal.forObjective objective with objective.fiscalYear = ?1 " +
//			"WHERE " +
//			"	" +
//			"	 objective.parent.id = ?3 ")
	@Query("" +  
			"SELECT objective " +
			"FROM Objective objective " +
			"	LEFT OUTER JOIN objective.proposals proposal with proposal.owner.id = ?2 " +
			"WHERE objective.parent.id = ?3 and objective.fiscalYear = ?1 " +
			"ORDER BY objective.id asc ")
	public List<Objective> findByObjectiveBudgetProposal(Integer fiscalYear, Long onwerId, Long objectiveId);

	
	
	@Query("" +  
			"SELECT distinct objective " +
			"FROM Objective objective" +
			"	INNER JOIN FETCH objective.parent parent " +
			"	INNER JOIN FETCH objective.type type " +
			"	LEFT OUTER JOIN FETCH objective.budgetTypes budgetTypes " +
			"	LEFT OUTER JOIN objective.proposals proposal with proposal.owner.id = ?2 " +
			"WHERE objective.fiscalYear = ?1 AND (objective.parentPath like ?3 OR objective.parentPath is null) " +
			"ORDER BY objective.id asc ")
	public List<Objective> findFlatByObjectiveBudgetProposal(
			Integer fiscalYear, Long ownerId, String parentPathLikeString);
	
	
	@Query("" +  
			"SELECT distinct objective " +
			"FROM Objective objective" +
			"	INNER JOIN FETCH objective.parent parent " +
			"	INNER JOIN FETCH objective.type type " +
			"	LEFT OUTER JOIN FETCH objective.budgetTypes budgetTypes " +
			"	LEFT OUTER JOIN objective.objectiveProposals proposal with proposal.owner.id = ?2 " +
			"WHERE objective.fiscalYear = ?1 AND (objective.parentPath like ?3 OR objective.parentPath is null) " +
			"ORDER BY objective.id asc ")	
	public List<Objective> findFlatByObjectiveObjectiveBudgetProposal(
			Integer fiscalYear, Long ownerId, String parentPathLikeString);
	
	
	@Query("" +  
			"SELECT distinct objective " +
			"FROM Objective objective" +
			"	LEFT OUTER JOIN FETCH objective.parent parent " +
			"	INNER JOIN FETCH objective.type type " +
			"	LEFT OUTER JOIN FETCH objective.budgetTypes budgetTypes " +
			"	LEFT OUTER JOIN objective.proposals proposal " +
			"WHERE objective.fiscalYear = ?1 AND (objective.parentPath like ?2 OR objective.parentPath is null) " +
			"ORDER BY objective.id asc ")
	public List<Objective> findFlatByObjectiveBudgetProposal(
			Integer fiscalYear, String parentPathLikeString);
	

	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE objective.parentPath like ?1")
	public List<Objective> findAllDescendantOf(String parentPathLikeString);
	
	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE objective.id in (?1) " +
			"ORDER BY objective.parentPath DESC ")
	public List<Objective> findAllObjectiveByIds(List<Long> ids);
	
	
	@Query("" +  
			"SELECT objective " +
			"FROM Objective objective" +
			"	INNER JOIN FETCH objective.type type " +
			"	LEFT OUTER JOIN FETCH objective.budgetTypes budgetTypes " +
			"WHERE objective.parent.id = ?1  " +
			"ORDER BY objective.code asc ")
	public List<Objective> findChildrenWithParentAndTypeAndBudgetType(
			Long id);

	@Modifying
	@Query("update Objective objective " +
			"set index = index-1 " +
			"where index > ?1 and objective.parent = ?2 ")
	public int reIndex(Integer deleteIndex, Objective parent);

	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"	INNER JOIN FETCH objective.type type " +
			"	LEFT OUTER JOIN FETCH objective.parent parent " +
			"	LEFT OUTER JOIN FETCH objective.units unit " +
			"WHERE objective.fiscalYear=?1 " +
			"	AND objective.type.id=?2 " +
			"ORDER BY objective.code asc ")
	public List<Objective> findAllByFiscalYearAndType_id(Integer fiscalYear,
			Long typeId);
	
	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE objective.fiscalYear=?1 " +
			"	AND objective.type.id=?2 " )
	public Page<Objective> findPageByFiscalYearAndType_id(Integer fiscalYear,
			Long typeId, Pageable pageable);
	
	@Query("" +
			"SELECT max(o.code) " +
			"FROM Objective o " +
			"WHERE o.type=? AND o.fiscalYear=?2 ")
	public String findMaxCodeOfTypeAndFiscalYear(ObjectiveType type,
			Integer fiscalYear);
	
	@Query("" +
			"SELECT max(o.lineNumber) " +
			"FROM Objective o " +
			"WHERE o.fiscalYear=?1 ")
	public Integer findMaxLineNumberFiscalYear(Integer fiscalYear);
	
	@Modifying
	@Query("update Objective objective " +
			"set lineNumber = lineNumber + ?3  " +
			"where fiscalYear =?1 AND lineNumber >= ?2 ")
	public Integer insertFiscalyearLineNumberAt(Integer fiscalYear, Integer lineNumer, Integer amount);

	@Modifying
	@Query("update Objective objective " +
			"set lineNumber = lineNumber - ?3  " +
			"where fiscalYear =?1 AND lineNumber > ?2 ")
	public Integer removeFiscalyearLineNumberAt(Integer fiscalYear, Integer lineNumer, Integer amount);
	
	@Query("" +
			"SELECT max(o.lineNumber) " +
			"FROM Objective o " +
			"WHERE o.parent = ?1  ")
	public Integer findMaxLineNumberChildrenOf(Objective parent);
	
	
	
	public List<Objective> findAllByFiscalYearAndParentPathLike(Integer fiscalYear, String parentPath);

	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE objective.type in (?1) and objective.parent is null ")
		public List<Objective> findAvailableChildrenOfObjectiveType(
			Set<ObjectiveType> childrenSet);

	public Objective findOneByFiscalYearAndName(Integer fiscalYear,
			String string);

	public List<Objective> findAllByObjectiveName(ObjectiveName on);

	
	@Query("" +
			"SELECT objective " +
			"FROM Objective objective" +
			"	INNER JOIN FETCH objective.proposals proposal " +
			"	INNER JOIN FETCH proposal.owner owner " +
			"	INNER JOIN FETCH proposal.budgetType budgetType " +
			"WHERE objective.fiscalYear = ?1 AND objective.type.id = ?2 ")
	public List<Objective> findAllByTypeIdAndFiscalYearInitBudgetProposal(
			Integer fiscalYear, long typeId);

	
	@Query("" +  
			"SELECT objective " +
			"FROM Objective objective " +
			"	LEFT OUTER JOIN FETCH objective.allocationRecords " +
			"WHERE objective.parentPath like ?1 " + 
			"ORDER BY objective.id asc ")
	public List<Objective> findByParent_IdLoadAllocationRecords(String parentIdPath);

	
	@Query("" +
			"SELECT objective " +
			"FROM Objective objective " +
			"WHERE fiscalYear = ?1 and type.id = ?2 and " +
			"	(name like ?3 or code like ?3) ")
	public Page<Objective> findByFiscalYearAndType_Id(
			Integer fiscalYear, Long typeId, String query, Pageable pageable);

	
	@Query("" +
			"SELECT owr.objective " +
			"FROM ObjectiveOwnerRelation owr " +
			"	INNER JOIN owr.owners owner " +
			"WHERE " +
			"	owner = ?1 AND " +
			"	owr.objective.fiscalYear = ?2 " +
			"ORDER BY " +
			"	owr.objective.code asc ")
	public List<Objective> findAllByOwnerAndfiscalYear(Organization workAt,
			Integer fiscalYear);

	
	
	@Query("" +
			"SELECT parentObjective " +
			"FROM ActivityPerformance activityPerformance " +
			"	INNER JOIN activityPerformance.activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"	INNER JOIN objective.parent parentObjective " +
			"WHERE " +
			"	activityPerformance.owner  = ?1 AND " +
			"	objective.fiscalYear = ?2")
	public List<Objective> findAllByActivityOwnerAndFiscalYear(
			Organization workAt, Integer fiscalYear);

	@Query("" +
			"SELECT parentObjective " +
			"FROM Activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"	INNER JOIN objective.parent parentObjective " +
			"WHERE " +
			"	activity.regulator  = ?1 AND " +
			"	objective.fiscalYear = ?2")
	public List<Objective> findAllByActivityRegulatorAndFiscalYear(
			Organization workAt, Integer fiscalYear);


	
	
	@Query("" +
			"SELECT objective " +
			"FROM ActivityPerformance activityPerformance " +
			"	INNER JOIN activityPerformance.activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"WHERE " +
			"	activityPerformance.owner  = ?1 AND " +
			"	objective.parent.id = ?2 " +
			"ORDER BY objective.code asc ")
	public List<Objective> findAllChildrenByActivityOwnerAndPanrentId(
			Organization workAt, Long id);


	@Query("" +
			"SELECT objective " +
			"FROM ActivityPerformance activityPerformance " +
			"	INNER JOIN activityPerformance.activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"WHERE " +
			"	activityPerformance.owner  = ?1 AND " +
			"	objective.parent.id = ?2 " +
			"ORDER BY objective.code asc ")
	public List<Objective> findObjectiveChildrenByActivityTargetOwnerAndObjectiveParentId(
			Organization workAt, Long id);
	
	@Query("" +
			"SELECT objective " +
			"FROM Activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"WHERE " +
			"	activity.regulator  = ?1 AND " +
			"	objective.parent.id = ?2 " +
			"ORDER BY objective.code asc ")	
	public List<Objective> findAllChildrenByActivityRegulatorAndParentId(
			Organization workAt, Long id);
	
	
	@Query("" +
			"SELECT objective " +
			"FROM ActivityTargetReport activityTargetReport " +
			"	INNER JOIN activityTargetReport.target target " +
			"	INNER JOIN target.activity activity " +
			"	INNER JOIN activity.forObjective objective " +
			"WHERE activityTargetReport.owner = ?1 ")
	public List<Objective> findByActivityTargetReportOfOrganization(
			Organization workAt);

	@Query("" +
			"SELECT obj " +
			"FROM Objective obj " +
			"	LEFT JOIN FETCH obj.children " +
			"	INNER JOIN FETCH obj.type " +
			"WHERE obj.fiscalYear = ?1 ")
	public List<Objective> findAllLeftJoinChildrenByFiscalYear(Integer fiscalYear);

	@Query("" +
			"SELECT assetAllocation.forObjective " +
			"FROM AssetAllocation assetAllocation " +
			"WHERE	assetAllocation.fiscalYear = ?1 AND "
			+ " (assetAllocation.owner like ?2 OR assetAllocation.operator like ?2) ")
	public List<Objective> findAllHasBudgetAssetByFiscalYear(Integer fiscalYear, Organization organization);

	@Query(""
			+ "SELECT max(obj.fiscalYear) "
			+ "FROM Objective obj "
			+ "WHERE obj.parent is null ")
	public Integer findMaxFiscalYear();

	@Query("" +
			"SELECT obj "
			+ "FROM Objective obj " 
			+ "	LEFT JOIN FETCH obj.children "
			+ "	INNER JOIN FETCH obj.type "
			+ "	INNER JOIN FETCH obj.objectiveName "
			+ "WHERE obj.fiscalYear = ?1 ")
	public List<Objective> findAllLeftJoinChildrenAndFetchBudgetProposalByFiscalYear(
			Integer fiscalYear);

	
	@Query(""
			+ "SELECT distinct obj "
			+ "FROM Objective obj "
			+ "	LEFT JOIN FETCH obj.children "
			+ "	INNER JOIN FETCH obj.type " 
			+ "	INNER JOIN FETCH obj.parent "
			+ "WHERE obj.parentPath like ?2 or obj.id = ?1 ")
	public List<Objective> findAllLeftJoinChildrenByParentIdLike(Long ObjId, String pranenId);


}
