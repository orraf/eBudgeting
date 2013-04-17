package biz.thaicom.eBudgeting.controllers.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import biz.thaicom.eBudgeting.models.bgt.AssetBudget;
import biz.thaicom.eBudgeting.models.bgt.AssetGroup;
import biz.thaicom.eBudgeting.models.bgt.AssetKind;
import biz.thaicom.eBudgeting.models.bgt.AssetType;
import biz.thaicom.eBudgeting.services.EntityService;

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
		return "OK";
	}
	

	
}
