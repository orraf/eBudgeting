package biz.thaicom.eBudgeting.services;

import java.util.List;
import java.util.Set;

import biz.thaicom.eBudgeting.model.bgt.BudgetType;
import biz.thaicom.eBudgeting.model.pln.Objective;
import biz.thaicom.eBudgeting.model.pln.ObjectiveType;

public interface EntityService {
	
	//ObjectiveType
	public ObjectiveType findObjectiveTypeById(Long id);
	public Set<ObjectiveType> findChildrenObjectiveType(ObjectiveType type);
	public ObjectiveType findParentObjectiveType(ObjectiveType type);
	public List<Integer> findObjectiveTypeRootFiscalYear();
	public List<ObjectiveType> findObjectiveTypeByFiscalYearEager(Integer fiscalYear, Long parentId);

	
	//Objective
	public List<Objective> findObjectivesOf(ObjectiveType type);
	public List<Objective> findObjectiveChildren(Objective objective);
	public Objective findParentObjective(Objective objective);
	public Objective findOjectiveById(Long id);
	public List<Objective> findObjectiveChildrenByObjectiveId(Long id);
	public List<Objective> findRootObjectiveByFiscalyear(Integer fiscalYear, Boolean eagerLoad);
	public List<Integer> findRootFiscalYear();
	
	//BudgetType
	public List<BudgetType> findRootBudgetType();
	public BudgetType findBudgetTyeById(Long id);
	public BudgetType findBudgetTyeEagerLoadById(Long id);
	
	

	
	
}