package biz.thaicom.eBudgeting.controllers.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
}
