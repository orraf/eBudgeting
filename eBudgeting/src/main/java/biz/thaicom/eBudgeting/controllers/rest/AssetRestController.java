package biz.thaicom.eBudgeting.controllers.rest;

import java.util.List;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.AssetBudget;
import biz.thaicom.eBudgeting.models.bgt.AssetCategory;
import biz.thaicom.eBudgeting.models.bgt.AssetGroup;
import biz.thaicom.eBudgeting.models.bgt.AssetKind;
import biz.thaicom.eBudgeting.models.bgt.AssetMethod;
import biz.thaicom.eBudgeting.models.bgt.AssetStepReport;
import biz.thaicom.eBudgeting.models.bgt.AssetType;
import biz.thaicom.eBudgeting.models.pln.TargetUnit;
import biz.thaicom.eBudgeting.models.webui.PageUI;
import biz.thaicom.eBudgeting.repositories.AssetAllocationRepository;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

@Controller
public class AssetRestController {
	private static final Logger logger = LoggerFactory.getLogger(AssetRestController.class);
	
	@Autowired
	private EntityService entityService;

	@RequestMapping("/AssetGroup/{id}") 
	public @ResponseBody AssetGroup findAssetGroupById(
			@PathVariable Long id) {
		return entityService.findAssetGroupById(id);
	}
	
	@RequestMapping("/AssetGroup/")
	public @ResponseBody List<AssetGroup> findAssetGroupByAll() {
		return entityService.findAssetGroupAll();
	}
	
	@RequestMapping("/AssetType/byGroupId/{groupId}")
	public @ResponseBody List<AssetType> findAssetTypeByGroupId(
			@PathVariable Long groupId) {
		return entityService.findAssetTypeByAssetGroupId(groupId);
	}
	
	@RequestMapping("/AssetKind/byTypeId/{typeId}")
	public @ResponseBody List<AssetKind> findAssetKindByTypeId(
			@PathVariable Long typeId) {
		return entityService.findAssetKindByAssetTypeId(typeId);
	}
	
	@RequestMapping("/AssetBudget/byKindId/{kindId}")
	public @ResponseBody List<AssetBudget> findAssetBudgetByKindId(
			@PathVariable Long kindId) {
		return entityService.findAssetBudgetByKindId(kindId);
	}
	
	@RequestMapping(value="/AssetBudget/{id}", method=RequestMethod.GET)
	public @ResponseBody AssetBudget findOneAssetBudget(
			@PathVariable Long id) {
		return entityService.findOneAssetBudget(id);
	}
	
	@RequestMapping(value="/AssetBudget/{id}", method=RequestMethod.PUT)
	public @ResponseBody JsonNode updateAssetBudget(
			@PathVariable Long id,
			@RequestBody JsonNode node) {
		AssetBudget assetBudget =  entityService.updateAssetBudget(node);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode(); // will be of type ObjectNode
		((ObjectNode) rootNode).put("id", assetBudget.getId());
		
		return rootNode;

	}
	
	@RequestMapping(value="/AssetBudget/", method=RequestMethod.POST)
	public @ResponseBody JsonNode saveAssetBudget(
			@RequestBody JsonNode node) {
		AssetBudget assetBudget = entityService.saveAssetBudget(node);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode(); // will be of type ObjectNode
		((ObjectNode) rootNode).put("id", assetBudget.getId());
		
		return rootNode;
	}
	
	@RequestMapping(value="/AssetBudget/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteAssetBudget(
			@PathVariable Long id) {
		entityService.deleteAssetBudget(id);
		return "OK";
	}

	@RequestMapping(value="/AssetAllocation/{id}", method=RequestMethod.GET)
	public @ResponseBody AssetAllocation findAssetAllocationById(
			@PathVariable Long id) {
		return entityService.findAssetAllocationById(id);
	}
	
	@RequestMapping(value="/AssetAllocation/", method=RequestMethod.POST)
	public @ResponseBody JsonNode saveAssetAllocation(
			@RequestBody JsonNode node) {
		AssetAllocation assetAllocation = entityService.saveAssetAllocation(node);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.createObjectNode(); // will be of type ObjectNode
		((ObjectNode) rootNode).put("id", assetAllocation.getId());
		JsonNode proposalNode = mapper.createObjectNode();
		((ObjectNode) proposalNode).put("id", assetAllocation.getProposal().getId());
		((ObjectNode) proposalNode).put("amountAllocated", assetAllocation.getProposal().getAmountAllocated());
		((ObjectNode) rootNode).put("proposal", proposalNode);
		
		
		return rootNode;
	}
	
	@RequestMapping(value="/AssetAllocation/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteAssetAllocation(
			@PathVariable Long id) {
		entityService.deleteAssetAllocation(id);
		return "OK";
	}

	@RequestMapping(value="/AssetAllocation/findByParentOwner/{parentOwnerId}/forObjective/{forObjectiveId}/budgetType/{budgetTypeId}")
	public @ResponseBody List<AssetAllocation> findByParentOwnerAndForObjectiveAndBudgetType(
			@PathVariable Long parentOwnerId,
			@PathVariable Long forObjectiveId,
			@PathVariable Long budgetTypeId) {
		return entityService.findAssetAllocationByParentOwnerAndForObjectiveAndBudgetType(parentOwnerId, forObjectiveId, budgetTypeId);
	}
	
	@RequestMapping(value="/AssetAllocation/saveCollection", method=RequestMethod.PUT)
	public @ResponseBody String saveCollection(
			@RequestBody JsonNode node) {
		entityService.saveAssetAllocationCollection(node);
		return "OK";
	}
	
	@RequestMapping(value="/AssetAllocation/forObjective/{objectiveId}", method=RequestMethod.GET) 
	public @ResponseBody List<AssetAllocation> findByForObjectiveId(
			@PathVariable Long objectiveId){
		return entityService.findAssetAllocationByForObjectiveId(objectiveId);
	}
	
	@RequestMapping(value="/AssetAllocation/currentUser/forObjective/{objectiveId}", method=RequestMethod.GET) 
	public @ResponseBody List<AssetAllocation> findByForObjectiveIdAndCurrentUser(
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser){
		return entityService.findAssetAllocationByForObjectiveIdAndOperator(objectiveId,currentUser.getWorkAt());
	}
 
	@RequestMapping(value="/AssetMethod/all") 
	public @ResponseBody List<AssetMethod> findAssetMethodAll() {
		return entityService.findAssetMethodAll();
	}
	
	@RequestMapping(value="/AssetAllocation/saveAssetPlan/{id}", method=RequestMethod.POST)
	public @ResponseBody String saveAssetAllocationPlan(
			@RequestBody JsonNode node) {
		return entityService.saveAssetAllocationPlan(node, false);
	}
	
	@RequestMapping(value="/AssetAllocation/saveAssetResult/{id}", method=RequestMethod.POST)
	public @ResponseBody String saveAssetAllocationResult(
			@RequestBody JsonNode node) {
		return entityService.saveAssetAllocationPlan(node, true);
	}
	
	@RequestMapping(value="/AssetStepReport/allByAssetAllocation/{assetAllocationId}")
	public @ResponseBody List<AssetStepReport> findAssetStepReportByAssetAlloationId(
			@PathVariable Long assetAllocationId){
		return entityService.findAssetStepReportByAssetAllocationId(assetAllocationId);
	}
	
	
	@RequestMapping(value="/AssetCategory/all") 
	public @ResponseBody List<AssetCategory> findAssetCategoryAll() {
		return entityService.findAssetCategoryAll();
	}
	
	@RequestMapping(value="/AssetCategory/page/{targetPage}", method=RequestMethod.GET)
	public @ResponseBody Page<AssetCategory> findAllAssetCategory(@PathVariable Integer targetPage) {
		
		PageRequest pageRequest =
	            new PageRequest(targetPage - 1, PageUI.PAGE_SIZE , Sort.Direction.ASC, "code");
		
		return entityService.findAllAssetCategories(pageRequest);
	}

	@RequestMapping(value="/AssetCategory/page/{targetPage}", method=RequestMethod.POST)
	public @ResponseBody Page<AssetCategory> findAllAssetCategory(@PathVariable Integer targetPage, 
			@RequestParam String query) {
		
		PageRequest pageRequest =
	            new PageRequest(targetPage - 1, PageUI.PAGE_SIZE , Sort.Direction.ASC, "code");
		
		logger.debug(query);
		
		return entityService.findAllAssetCategories(pageRequest, query);
	}
	
	@RequestMapping(value="/AssetCategory/{id}", method=RequestMethod.GET)
	public @ResponseBody AssetCategory findOneAssetCategory(
			@PathVariable Long id) {
		return entityService.findOneAssetCategory(id);
	}
	
	@RequestMapping(value="/AssetCategory/{id}", method=RequestMethod.PUT)
	public @ResponseBody AssetCategory updateAssetCategory(
			@PathVariable Long id,
			@RequestBody JsonNode node) {
		return entityService.updateAssetCategory(node);
	}
	
	@RequestMapping(value="/AssetCategory/", method=RequestMethod.POST)
	public @ResponseBody AssetCategory saveAssetCategory(
			@RequestBody JsonNode node) {
		return entityService.saveAssetCategory(node);
	}
	
	@RequestMapping(value="/AssetCategory/{id}", method=RequestMethod.DELETE)
	public @ResponseBody AssetCategory deleteAssetCategory(
			@PathVariable Long id) {
		return entityService.deleteAssetCategory(id);
	}
	
	@ExceptionHandler(value=Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody String handleException(final Exception e, final HttpServletRequest request) {
		logger.error(e.toString());
		e.printStackTrace();
		return "failed: " + e.toString();
		
	}
	
}
