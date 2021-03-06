package biz.thaicom.eBudgeting.controllers.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.JsonNode;




import com.google.common.base.Throwables;

import biz.thaicom.eBudgeting.controllers.error.RESTError;
import biz.thaicom.eBudgeting.models.bgt.AllocationRecord;
import biz.thaicom.eBudgeting.models.bgt.ObjectiveBudgetProposal;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.ObjectiveDetail;
import biz.thaicom.eBudgeting.models.pln.ObjectiveName;
import biz.thaicom.eBudgeting.models.pln.ObjectiveRelations;
import biz.thaicom.eBudgeting.models.pln.ObjectiveTarget;
import biz.thaicom.eBudgeting.models.webui.PageUI;
import biz.thaicom.eBudgeting.repositories.ObjectiveRelationsRepository;
import biz.thaicom.eBudgeting.repositories.OrganizationRepository;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

@Controller
public class ObjectiveRestController {
	private static final Logger logger = LoggerFactory.getLogger(ObjectiveRestController.class);
	
	@Autowired
	private EntityService entityService;

	@RequestMapping(value="/Objective/root", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getRootFiscalYear() {
		logger.debug("/Objective/root is called ");
		return entityService.findRootFiscalYear();
	}
	
	@RequestMapping(value="/Objective/root/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getRootObjectiveByFiscalYear(
			@PathVariable Integer fiscalYear) {
		
		return entityService.findRootObjectiveByFiscalyear(fiscalYear, false);
	};
	
	@RequestMapping(value="/Objective/ROOT/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody Objective getOneROOTObjectiveByFiscalYear(
			@PathVariable Integer fiscalYear) {
		
		return entityService.findOneRootObjectiveByFiscalyear(fiscalYear);
	}
	
	
	@RequestMapping(value="/Objective/rootEager/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getRootEagerObjectiveByFiscalYear(
			@PathVariable Integer fiscalYear) {
		
		return entityService.findRootObjectiveByFiscalyear(fiscalYear, true);
	};

	
	@RequestMapping(value="/Objective/{id}", method=RequestMethod.GET)
	public @ResponseBody Objective getObjectiveById(@PathVariable Long id) {
		logger.debug("id: " + id);
		return entityService.findOjectiveById(id); 
	}
	
	@RequestMapping(value="/Objective/loadObjectiveBudgetProposal/{id}", method=RequestMethod.GET)
	public @ResponseBody Objective getObjectiveLoadObjectiveBudgetProposalById(
			@PathVariable Long id, 
			@Activeuser ThaicomUserDetail currentUser) {
		Objective o = entityService.findOjectiveById(id);
		if(o!=null) {
			List<ObjectiveBudgetProposal> obpList = entityService.findObjectiveBudgetproposalByObjectiveIdAndOwnerId(id, currentUser.getWorkAt().getId());
			o.setFilterObjectiveBudgetProposals(obpList);
		}
		return o;  
	}

	@RequestMapping(value="/Objective/loadObjectiveAllocationRecord/{id}", method=RequestMethod.GET)
	public @ResponseBody Objective getObjectiveLoadAllocationRecordById(
			@PathVariable Long id, 
			@RequestParam(required = false) boolean onlyCapitalBudget,
			@Activeuser ThaicomUserDetail currentUser) {
		Objective o = entityService.findOjectiveById(id);
		if(o!=null) {
			List<AllocationRecord> allocationRecords = entityService.findAllocationRecordByObjective(o);
			o.setAllocationRecords(allocationRecords);
		}
		return o;  
	}
	
	@RequestMapping(value="/Objective/{id}/children", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getChildrenObjectiveById(@PathVariable Long id,
			@RequestParam(required = false) boolean onlyCapitalBudget) {
		logger.debug("id: " + id);
		List<Objective> list =entityService.findObjectiveChildrenByObjectiveId(id);

		if(onlyCapitalBudget) {
			List<Objective> onlyCapitalList = new ArrayList<Objective>();
			for(Objective o : list) {
				for(AllocationRecord r : o.getAllocationRecords()) {
					if(r.getBudgetType().is_งบลงทุน()) {
						onlyCapitalList.add(o);
					}
				}
			}
			list = onlyCapitalList;
		}
		
		return  list;
	}
	 
	
	// ค้นหากิจกรรม และ Load เป้าหมายและงบประมาณของกิจกรรม ในระดับ เขต
		@RequestMapping(value="/Objective/getChildrenAndloadActivityAndOwnerId/{id}/district/{ownerId}", method=RequestMethod.GET)
		public @ResponseBody List<Objective> getChildrenAndloadActivityDistrict(
				@PathVariable Long id, @PathVariable Long ownerId) {
			
			
			// now load all activity
			List<Objective> list = entityService
					.findObjectiveLoadActivityByParentObjectiveIdAndReportLevel(id, ownerId, ActivityTargetReport.districtLevel);
			
			return  list;
		}
	
	// ค้นหากิจกรรม และ Load เป้าหมายและงบประมาณของกิจกรรม ในระดับ จังหวัด
	@RequestMapping(value="/Objective/getChildrenAndloadActivityAndOwnerId/{id}/province/{ownerId}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getChildrenAndloadActivityProvince(
			@PathVariable Long id, @PathVariable Long ownerId) {
		
		
		// now load all activity
		List<Objective> list = entityService
				.findObjectiveLoadActivityByParentObjectiveIdAndReportLevel(id, ownerId, ActivityTargetReport.provinceLevel);
		
		return  list;
	}
	
	
	// ค้นหากิจกรรม และ Load เป้าหมายและงบประมาณของกิจกรรม ในระดับ ส่วน/อำเภอ
	@RequestMapping(value="/Objective/getChildrenAndloadActivityAndOwnerId/{id}/{ownerId}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getChildrenAndloadActivity(
			@PathVariable Long id, @PathVariable Long ownerId) {
		
		// now load all activity
		List<Objective> list = entityService
				.findObjectiveLoadActivityByParentObjectiveIdAndReportLevel(id, ownerId, ActivityTargetReport.amphurLevel);
		
		return  list;
	}
	
	
	@RequestMapping(value="/Objective/{id}/availableChildren", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getAvailableChildrenObjectiveById(@PathVariable Long id) {
		logger.debug("id: " + id);
		List<Objective> list =entityService.findAvailableObjectiveChildrenByObjectiveId(id);

		return  list;
	}
	
	
//	@RequestMapping(value="/Objective/{id}/availableChildrenName", method=RequestMethod.GET)
//	public @ResponseBody List<ObjectiveName> getAvailableChildrenObjectiveNameById(@PathVariable Long id) {
//		logger.debug("id: " + id);
//		List<ObjectiveName> list =entityService.findAvailableObjectiveNameChildrenByObejective(id);
//
//		return  list;
//	}

	@RequestMapping(value="/Objective/{id}/childrenTypeName", method=RequestMethod.GET)
	public @ResponseBody String getChildrenTypeName(@PathVariable Long id) {
		logger.debug("id: " + id);
		String s = entityService.findObjectiveChildrenTypeName(id);

		return  s;
	}
	
	@RequestMapping(value="/Objective/{fiscalYear}/type/{typeId}") 
	public @ResponseBody List<Objective> getObjectiveByFiscalYearAndType(
			@PathVariable Integer fiscalYear, @PathVariable Long typeId) {
		return entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, typeId);
	}
	
	@RequestMapping(value="/Objective/{fiscalYear}/type/{typeId}/page/{pageNumber}") 
	public @ResponseBody Page<Objective> getPagedObjectiveByFiscalYearAndType(
			@PathVariable Integer fiscalYear, 
			@PathVariable Long typeId,
			@PathVariable Integer pageNumber,
			@RequestParam(required=false) String query) {
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, PageUI.PAGE_SIZE, Sort.Direction.ASC, "code");
		
		if(query != null && query.length() > 0) {
			query = "%" + query + "%";
		} else {
			query = "%";
		}
		
		return entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, typeId, query, pageRequest);
	}
	
	@RequestMapping(value="/Objective/{id}/addUnit", method=RequestMethod.POST) 
	public @ResponseBody ObjectiveTarget addUnit(@PathVariable Long id,
			@RequestParam Long unitId, @RequestParam Integer isSumable) {

		return entityService.addUnitToObjective(id, unitId, isSumable);
	}
	
	@RequestMapping(value="/Objective/{id}/removeUnit", method=RequestMethod.POST) 
	public @ResponseBody String removeUnit(@PathVariable Long id,
			@RequestParam Long targetId) {
		String s =  entityService.removeUnitFromObjective(id, targetId);
		logger.debug("returning: " + s );
		
		return s;
	}
	
	
	@RequestMapping(value="/Objective/{id}/addTarget", method=RequestMethod.POST)
	public @ResponseBody String addTargetToObjective(@PathVariable Long id,
			@RequestParam Long targetId) {
		logger.debug("id: " + id);
		logger.debug("targetId: " + id);
		
		entityService.addTargetToObjective(id, targetId);
		
		return  "success";
	}
	
	@RequestMapping(value="/Objective/fiscalYear/{fiscalYear}/findByActivityTargetReportOfCurrentUser", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findByActivityTargetReportOfCurrentUser(
			@Activeuser ThaicomUserDetail currentUser, @PathVariable Integer fiscalYear) {
		return entityService.findObjectiveByActivityTargetReportOfOrganizationAndFiscalYear(currentUser.getWorkAt(), fiscalYear);
	}

	@RequestMapping(value="/Objective/fiscalYear/{fiscalYear}/{objectiveId}/findByActivityTargetReportOfCurrentUser", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findByActivityTargetReportOfCurrentUserAndObjectiveId(
			@Activeuser ThaicomUserDetail currentUser, @PathVariable Integer fiscalYear, @PathVariable Long objectiveId) {
		return entityService.findObjectiveByActivityTargetReportOfOrganizationAndFiscalYear(currentUser.getWorkAt(), fiscalYear, objectiveId);
	}
	
	@RequestMapping(value="/Objective/fiscalYear/{fiscalYear}/{objectiveId}/findByActivityTargetReportOfOwnerId/{ownerId}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findByActivityTargetReportOfCurrentUserAndObjectiveIdAndOwnerId(
			@Activeuser ThaicomUserDetail currentUser, @PathVariable Integer fiscalYear, @PathVariable Long objectiveId,
			@PathVariable Long ownerId) {
		
		Organization org = entityService.findOrganizationById(ownerId);
		
		return entityService.findObjectiveByActivityTargetReportOfOrganizationAndFiscalYear(org, fiscalYear, objectiveId);
	}
	
	
	
	@RequestMapping(value="/Objective/fiscalYear/{fiscalYear}/findByActivityTargetReportOfCurrentUser/NoReportCurrentMonth", method=RequestMethod.GET)
	public @ResponseBody List<String> findByActivityTargetReportOfCurrentUserNoReportCurrentMonth(
			@Activeuser ThaicomUserDetail currentUser, @PathVariable Integer fiscalYear) {
		List<Objective> objectives = entityService.findObjectiveByActivityTargetReportOfOrganizationAndFiscalYearNoReportCurrentMonth(currentUser.getWorkAt(), fiscalYear);
		
		List<String> objNames = new ArrayList<String>();
		
		if(objectives != null && objectives.size() > 0) {
			for(Objective obj : objectives) {
				objNames.add(obj.getName());
			}
		}
		return objNames;
	}
	
	@RequestMapping(value="/Objective/{id}/saveOwners", method=RequestMethod.POST)
	public @ResponseBody List<Organization> saveObjectiveOwners(
			@PathVariable Long id,
			@RequestParam(required=false, value="ownerIds[]") Long ownerIds[] ){
		return entityService.saveObjectiveOwners(id, ownerIds);
	}

	@RequestMapping(value="/Objective/currentTopOwner/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveByCurrentTopOwner(
			@PathVariable Integer fiscalYear,
			@Activeuser ThaicomUserDetail currentUser
			){
		Organization org = currentUser.getWorkAt();
		if(org.getType() == OrganizationType.ส่วน) {
			org = org.getParent();
		}
		
		return entityService.findObjectiveByOwnerAndFiscalYear(org, fiscalYear);
	}
	
	@RequestMapping(value="/Objective/currentOwner/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveByCurrentOwner(
			@PathVariable Integer fiscalYear,
			@Activeuser ThaicomUserDetail currentUser
			){
		
		return entityService.findObjectiveByOwnerAndFiscalYear(currentUser.getWorkAt(), fiscalYear);
	}
	
	@RequestMapping(value="/Objective/ownerId/{ownerId}/FY/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveByOwnerAndFy(
			@PathVariable Integer fiscalYear,
			@Activeuser ThaicomUserDetail currentUser,
			@PathVariable Long ownerId
			){
		Organization org = entityService.findOrganizationById(ownerId);
		
		return entityService.findObjectiveByOwnerAndFiscalYear(org, fiscalYear);
	}
	
	@RequestMapping(value="/Objective/BudgetAsset/ficalYear/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveHasBudgetAssetByFiscalYear(
			@PathVariable Integer fiscalYear,
			@Activeuser ThaicomUserDetail currentUser
			){
		return entityService.findObjectiveHasBudgetAssetByFiscalYear(fiscalYear, currentUser.getWorkAt());
		
	}
	
	@RequestMapping(value="/Objective/currentActivityOwner/{fiscalYear}/ownerId/{ownerId}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveByCurrentActivityWithOwnerId(
			@PathVariable Integer fiscalYear,
			@PathVariable Long ownerId,
			@Activeuser ThaicomUserDetail currentUser
			){
		
		Organization org = entityService.findOrganizationById(ownerId);
		
		return entityService.findObjectiveByActivityOwnerAndFiscalYear(org, fiscalYear);
	}
	
	
	@RequestMapping(value="/Objective/currentActivityOwner/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveByCurrentActivityOwner(
			@PathVariable Integer fiscalYear,
			@Activeuser ThaicomUserDetail currentUser
			){
		return entityService.findObjectiveByActivityOwnerAndFiscalYear(currentUser.getWorkAt(), fiscalYear);
	}

	@RequestMapping(value="/Objective/currentActivityRegulator/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveByCurrentActivityRegulator(
			@PathVariable Integer fiscalYear,
			@Activeuser ThaicomUserDetail currentUser
			){
		return entityService.findObjectiveByActivityRegulatorAndFiscalYear(currentUser.getWorkAt(), fiscalYear);
	}

	
	@RequestMapping(value="/Objective/{id}/childrenOnlyWithCurrentActivityOwner", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveChildrenByCurrentActivityOwner(
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser
			){
		return entityService.findObjectiveChildrenByActivityOwnerAndParentId(currentUser.getWorkAt(), id);
	}

	@RequestMapping(value="/Objective/{id}/childrenOnlyWithCurrentActivityRegulator", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveChildrenByCurrentActivityRegulator(
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser
			){
		return entityService.findObjectiveChildrenByActivityRegulatorAndParentId(currentUser.getWorkAt(), id);
	}
	
	@RequestMapping(value="/Objective/{id}/childrenOnlyWithCurrentActivityTargetOwner", method=RequestMethod.GET)
	public @ResponseBody List<Objective> findObjectiveChildrenByCurrentActivityTargetOwner(
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser
			){
		return entityService.findObjectiveChildrenByActivityTargetOwnerAndObjectiveParentId(currentUser.getWorkAt(), id);
	}

	
	@RequestMapping(value="/Objective", method=RequestMethod.POST) 
	public @ResponseBody Objective saveObjective(@RequestBody JsonNode node) {
		return entityService.saveObjective(node);
	}
	
	@RequestMapping(value="/Objective/{id}", method=RequestMethod.PUT)
	public @ResponseBody Objective updateObjective(@PathVariable Long id,
			@RequestBody JsonNode node) {
		
		// now we'll have to save this
		Objective objectiveFromJpa = entityService.saveObjective(node);
		
		
		return objectiveFromJpa;
		
	}
	
	@RequestMapping(value="/Objective/{id}/updateToParent/{parentId}", method=RequestMethod.PUT)
	public @ResponseBody Objective updateObjectiveParent(@PathVariable Long id,
			@PathVariable Long parentId) {
		
		
		
		// now we'll have to save this
		Objective objectiveFromJpa = entityService.updateObjectiveParent(id, parentId);
		
		
		return objectiveFromJpa;
		
	}
	
	@RequestMapping(value="/Objective/{id}/addReplaceUnit/{unitId}", method=RequestMethod.PUT)
	public @ResponseBody Objective updateObjectiveUnit(@PathVariable Long id, @PathVariable Long unitId) {
		// now we'll have to save this
		Objective objectiveFromJpa = entityService.objectiveAddReplaceUnit(id, unitId);
				
				
		return objectiveFromJpa;
	}
	
	@RequestMapping(value="/Objective/{id}", method=RequestMethod.DELETE) 
	public @ResponseBody Objective deleteObjective(
			@PathVariable Long id) {

		Boolean cascadeNameDelete = false;
		
		return entityService.deleteObjective(id, cascadeNameDelete);
	}
	
	@RequestMapping(value="/Objective/ListAllInFiscalYear/{fiscalYear}", method=RequestMethod.GET)
	public @ResponseBody List<Objective> listAllinFiscalYear(@PathVariable Integer fiscalYear) {
		Objective root = entityService.findObjectivesByFiscalyearAndTypeIdForM82R01Report(fiscalYear);
		
		List<Objective> objectives = new ArrayList<Objective>();
		
		objectives.add(root);
		
		Stack<Objective> stack = new Stack<Objective>();
		
		for(int i=root.getChildren().size(); i>0; i--) {
			stack.push(root.getChildren().get(i-1));
		}
		
		
		
		while(!stack.empty()) {
			Objective o = stack.pop();
			
			objectives.add(o);
			
			if(o.getChildren() != null && o.getChildren().size() > 0) {
				for(int i=o.getChildren().size(); i>0; i--) {
					stack.push(o.getChildren().get(i-1));
				}
			}
				
		}
		
		// now rid of the children .. to improve performance in js
		logger.debug("objetives.size() : " + objectives.size());
		for(Objective o : objectives) {
			o.setChildren(null);
			o.setParent(null);
		}
		
		return  objectives;
	}
	

	
	@RequestMapping(value="/Objective/newObjectiveWithParam", method=RequestMethod.POST) 
	public @ResponseBody Objective saveObjectiveWithParam(
			@RequestParam String name,
			@RequestParam String code,
			@RequestParam Long parentId,
			@RequestParam String parentPath,
			@RequestParam Long typeId,
			@RequestParam Integer fiscalYear) {
		
		return entityService.newObjectiveWithParam(name,code,parentId,typeId, parentPath, fiscalYear);
	}
	
	
	@RequestMapping(value="/Objective/{id}/addBudgetType", method=RequestMethod.POST)
	public @ResponseBody Objective addBudgetType(@PathVariable Long id,
			@RequestParam Long budgetTypeId){
		return entityService.addBudgetTypeToObjective(id, budgetTypeId);
	}
	
	@RequestMapping(value="/Objective/{id}/updateFields", method=RequestMethod.POST)
	public @ResponseBody Objective updateFileds(@PathVariable Long id,
			@RequestParam(required=false) String name,
			@RequestParam(required=false) String code){
		return entityService.updateObjectiveFields(id, name, code);
	}
	
	@RequestMapping(value="/Objective/{id}/removeBudgetType", method=RequestMethod.POST)
	public @ResponseBody Objective removeBudgetType(@PathVariable Long id,
			@RequestParam Long budgetTypeId){
		return entityService.removeBudgetTypeToObjective(id, budgetTypeId);
	}
	
	
	@RequestMapping(value="/ObjectiveWithBudgetProposal/{fiscalYear}/{ownerId}/{objectiveId}/children", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getChildrenbjectiveWithBudgetPorposalByOwnerId(
			@PathVariable Integer fiscalYear,
			@PathVariable Long ownerId,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		List<Objective> objectives = entityService.findChildrenObjectivewithBudgetProposal(fiscalYear, ownerId, objectiveId, false);
		
		return objectives;
		
	}
	

	@RequestMapping(value="/ObjectiveWithAllocationRecords/{objectiveId}/children", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getChildrenbjectiveWithAllocationRecord(
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser,
			@RequestParam(required = false) boolean onlyCapitalBudget
			) {
		List<Objective> objectives = entityService.findChildrenObjectivewithAllocationRecords(objectiveId);
		
		if(onlyCapitalBudget) {
			List<Objective> onlyCapitalList = new ArrayList<Objective>();
			for(Objective o : objectives) {
				for(AllocationRecord r : o.getAllocationRecords()) {
					if(r.getBudgetType().is_งบลงทุน()) {
						onlyCapitalList.add(o);
					}
				}
			}
			objectives = onlyCapitalList;
		}
		
		
		return objectives;
		
	}
	
	
	
	@RequestMapping(value="/ObjectiveWithBudgetProposal/{fiscalYear}/{objectiveId}/children", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getChildrenbjectiveWithBudgetPorposal(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		List<Objective> objectives = entityService.findChildrenObjectivewithBudgetProposal(fiscalYear, currentUser.getWorkAt().getId(), objectiveId, false);
		
		return objectives;
		
	}
	
	@RequestMapping(value="/ObjectiveWithBudgetProposal/{fiscalYear}/{objectiveId}/flatDescendants", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getFlatDescendantsObjectiveWithBudgetPorposalByOwnerId(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		
		logger.debug("current user workAt.id = {} ",currentUser.getWorkAt().getId());
		
		List<Objective> objectives = entityService.findFlatChildrenObjectivewithBudgetProposal(fiscalYear, currentUser.getWorkAt().getId(), objectiveId);
		
		return objectives;
		
	}
	
	@RequestMapping(value="/ObjectiveWithObjectiveBudgetProposal/{fiscalYear}/{objectiveId}/flatDescendants", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getFlatDescendantsObjectiveWithObjectiveBudgetPorposalByOwnerId(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		
		logger.debug("current user workAt.id = {} ",currentUser.getWorkAt().getId());
		
		List<Objective> objectives = entityService.findFlatChildrenObjectivewithObjectiveBudgetProposal(fiscalYear, currentUser.getWorkAt().getId(), objectiveId);
		
		return objectives;
		
	}
	
	@RequestMapping(value="/ObjectiveWithBudgetProposalAndAllocation/{fiscalYear}/{objectiveId}/flatDescendants", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getFlatDescendantsObjectiveWithBudgetPorposalAndAllocation(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		boolean isFindObjectiveBudget = false;
		List<Objective> objectives = entityService.findFlatChildrenObjectivewithBudgetProposalAndAllocation(fiscalYear, objectiveId, isFindObjectiveBudget);
		
		return objectives;
		
	}
	
	@RequestMapping(value="/ObjectiveWithObjectiveBudgetProposalAndAllocation/{fiscalYear}/{objectiveId}/flatDescendants", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getFlatDescendantsObjectiveWithObjectiveBudgetPorposalAndAllocation(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		
		boolean isFindObjectiveBudget = true;
		List<Objective> objectives = entityService.findFlatChildrenObjectivewithBudgetProposalAndAllocation(fiscalYear, objectiveId, isFindObjectiveBudget);
		
		return objectives;
		
	}
	
	
	
	
	@RequestMapping(value="/ObjectiveWithBudgetProposal/{fiscalYear}/{ownerId}/{objectiveId}/descendants", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getDescendantsbjectiveWithBudgetPorposalByOwnerId(
			@PathVariable Integer fiscalYear,
			@PathVariable Long ownerId,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		List<Objective> objectives = entityService.findChildrenObjectivewithBudgetProposal(fiscalYear, ownerId, objectiveId, true);
		
		return objectives;
		
	}
	
	@RequestMapping(value="/ObjectiveWithBudgetProposal/{fiscalYear}/{objectiveId}/descendants", method=RequestMethod.GET)
	public @ResponseBody List<Objective> getDescendantsbjectiveWithBudgetPorposal(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser
			) {
		List<Objective> objectives = entityService.findChildrenObjectivewithBudgetProposal(fiscalYear, currentUser.getWorkAt().getId(), objectiveId, true);
		
		return objectives;
		
	}
	
	
	@RequestMapping(value="/ObjectiveRelations/{fiscalYear}/relation/{parentTypeId}/all") 
	public @ResponseBody List<ObjectiveRelationsRepository> getObjectiveByFiscalYearAndparentRelation(
			@PathVariable Integer fiscalYear, @PathVariable Long parentTypeId){
		return entityService.findObjectiveRelationsByFiscalYearAndChildTypeRelation(fiscalYear, parentTypeId);
	}
	
	@RequestMapping(value="/ObjectiveRelations/{fiscalYear}/relation/{parentTypeId}", method=RequestMethod.GET) 
	public @ResponseBody List<ObjectiveRelationsRepository> getObjectiveByFiscalYearAndparentRelationWithObjectiveIds(
			@PathVariable Integer fiscalYear, @PathVariable Long parentTypeId,
			@RequestParam(required=false, value="ids[]") String[] ids){
		
		if(ids==null || ids.length == 0) {
			return null;
		}
		
		List<Long> idList = new ArrayList<Long>();
		
		for(String id: ids) {
			idList.add(Long.parseLong(id));
		}
		return entityService.findObjectiveRelationsByFiscalYearAndChildTypeRelationWithObjectiveIds
				(fiscalYear, parentTypeId, idList);
	
	}
	
	@RequestMapping(value="/Objective/mappedUnit", method=RequestMethod.GET)
	public @ResponseBody String mappedUnit() {
		return entityService.mappedUnit();
	}
	
	
	@RequestMapping(value="/ObjectiveRelations", method=RequestMethod.POST)
	public @ResponseBody ObjectiveRelations saveObjectiveRelations( @RequestBody JsonNode relation) {
		return entityService.saveObjectiveRelations(relation);
	}
	
	@RequestMapping(value="/ObjectiveRelations/{id}", method=RequestMethod.PUT)
	public @ResponseBody ObjectiveRelations updateObjectiveRelations(
			@PathVariable Long id, @RequestBody JsonNode relation) {
		return entityService.updateObjectiveRelations(id, relation);
	}
	
	@RequestMapping(value="/Objective/initFiscalYear", method=RequestMethod.POST)
	public @ResponseBody String initFiscalYear(
			@RequestParam Integer fiscalYear){
		logger.debug("initFiscalYear " + fiscalYear);
		return entityService.initFiscalYear(fiscalYear);
		
	}
	
	@RequestMapping(value="/Objective/findAllByFiscalYear/{fiscalYear}") 
	public @ResponseBody List<Objective> findObjectiveAllByFiscalYear(
			@PathVariable Integer fiscalYear) {
		
		
		return entityService.findObjectiveAllByFiscalYear(fiscalYear);
	}
	
	@RequestMapping(value="/Objective/{parentId}/addChildObjectiveName/{nameId}", method=RequestMethod.GET)
	public @ResponseBody Objective addChildObjectiveName( 
			@PathVariable Long parentId, @PathVariable Long nameId) {
		return entityService.objectiveAddChildObjectiveName(parentId, nameId);
	}
	
	
	@RequestMapping(value="/ObjectiveName/fiscalYear/{fiscalYear}/type/{typeId}/page/{pageNumber}", method=RequestMethod.GET)
	public @ResponseBody Page<ObjectiveName> findAllObjectiveNameByFiscalYearAndTypeId(
			@PathVariable Integer fiscalYear, 
			@PathVariable Long typeId,
			@PathVariable Integer pageNumber) {
		
		PageRequest pageRequest =
	            new PageRequest(pageNumber - 1, PageUI.PAGE_SIZE, Sort.Direction.ASC, "index");
		return entityService.findAllObjectiveNameByFiscalYearAndTypeId(fiscalYear, typeId, pageRequest);
		
	}
	
	@RequestMapping(value="/ObjectiveName/{id}", method=RequestMethod.GET)
	public @ResponseBody ObjectiveName findOneObjectiveName(
			@PathVariable Long id) {
		return entityService.findOneObjectiveName(id);
	}
	
	@RequestMapping(value="/ObjectiveName/{id}", method=RequestMethod.PUT)
	public @ResponseBody ObjectiveName updateObjectiveName(
			@PathVariable Long id,
			@RequestBody JsonNode node) {
		return entityService.updateObjectiveName(node);
	}
	
	@RequestMapping(value="/ObjectiveName/", method=RequestMethod.POST)
	public @ResponseBody ObjectiveName saveObjectiveName(
			@RequestBody JsonNode node) {
		return entityService.saveObjectiveName(node);
	}
	
	@RequestMapping(value="/ObjectiveName/{id}", method=RequestMethod.DELETE)
	public @ResponseBody ObjectiveName deleteObjectiveName(
			@PathVariable Long id) {
		return entityService.deleteObjectiveName(id);
	}
	
	@RequestMapping(value="/ObjectiveName/findChildrenNameOfObjective/{id}") 
	public @ResponseBody List<ObjectiveName> findChildrenNameOfObjective (
			@PathVariable Long id, @RequestParam(required=false) String searchQuery ) throws UnsupportedEncodingException {
		if(searchQuery != null) {
			searchQuery = URLDecoder.decode(searchQuery, "UTF-8");
		}
		
		return entityService.findAvailableObjectiveNameChildrenByObejective(id, searchQuery );
	}
	
	@RequestMapping(value="/ObjectiveName/{id}/addUnit", method=RequestMethod.POST) 
	public @ResponseBody ObjectiveTarget addObjectiveNameUnit(@PathVariable Long id,
			@RequestParam Long unitId, @RequestParam Integer isSumable) {

		return entityService.addUnitToObjectiveName(id, unitId, isSumable);
	}
	
	@RequestMapping(value="/ObjectiveName/{id}/removeUnit", method=RequestMethod.POST) 
	public @ResponseBody String removeObjectiveNameUnit(@PathVariable Long id,
			@RequestParam Long targetId) {
		return entityService.removeUnitFromObjectiveName(id, targetId);
	}
	
	
	
	@RequestMapping(value="/ObjectiveDetail/byObjective/{id}/ofCurrentUser", method=RequestMethod.GET)
	public @ResponseBody ObjectiveDetail findOneObjectiveDetailByObjectiveAndOwner(
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser) {
		ObjectiveDetail detail = entityService.findOneObjectiveDetailByObjectiveIdAndOwner(id, currentUser);
		
		//we intentionally put forObjective to be null and let the caller save its own
		if(detail != null) 	detail.setForObjective(null);
		
		return detail;
	}
	
	@RequestMapping(value="/ObjectiveDetail/{id}", method=RequestMethod.GET)
	public @ResponseBody ObjectiveDetail findOneObjectiveDetail(
			@PathVariable Long id) {
		return entityService.findOneObjectiveDetail(id);
	}
	
	@RequestMapping(value="/ObjectiveDetail/{id}", method=RequestMethod.PUT)
	public @ResponseBody ObjectiveDetail updateObjectiveDetail(
			@PathVariable Long id,
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentUser) {
		ObjectiveDetail detail = entityService.updateObjectiveDetail(node, currentUser.getWorkAt());
		
		//we intentionally put forObjective to be null and let the caller save its own
		detail.setForObjective(null);		
		return detail;
	}
	
	@RequestMapping(value="/ObjectiveDetail/", method=RequestMethod.POST)
	public @ResponseBody ObjectiveDetail saveObjectiveDetail(
			@RequestBody JsonNode node, @Activeuser ThaicomUserDetail currentUser) {
		ObjectiveDetail detail =  entityService.saveObjectiveDetail(node, currentUser.getWorkAt());
		
		//we intentionally put forObjective to be null and let the caller save its own
		detail.setForObjective(null);		
		return detail;
	}
	
	@RequestMapping(value="/ObjectiveDetail/{id}", method=RequestMethod.DELETE)
	public @ResponseBody ObjectiveDetail deleteObjectiveDetail(
			@PathVariable Long id) {
		return entityService.deleteObjectiveDetail(id);
	}

	
	
	
	@ExceptionHandler(value=Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody RESTError handleException(final Exception e, final HttpServletRequest request) {
    	RESTError error = new RESTError();
    	error.setMessage(e.getMessage());
    	
    	String trace = Throwables.getStackTraceAsString(e);
        error.setStackTrace(trace);
        
        error.setDate(new Date());
        
        return error;
	}

	
}
