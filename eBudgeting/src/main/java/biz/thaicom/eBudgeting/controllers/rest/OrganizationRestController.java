package biz.thaicom.eBudgeting.controllers.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.services.EntityService;

@Controller
public class OrganizationRestController {
private static final Logger logger = LoggerFactory.getLogger(Organization.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value="/Organization/findByName", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findOrganizationByName(
			@RequestParam String query) {
		logger.debug("query: " + query);
		List<Organization> list =entityService.findOrganizationByNameAndCode(query, null);

		return  list;
	}

	@RequestMapping(value="/Organization/parentId/{parentId}/findByName", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findOrganizationByParentIdAndName(
			@RequestParam String query,
			@PathVariable Long parentId) {
		logger.debug("query: " + query);
		List<Organization> list =entityService.findOrganizationByNameAndParent_Id(query, parentId);

		return  list;
	}

	
	@RequestMapping(value="/Organization/code/{code}/findByName", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findOrganizationByCodeAndName(
			@RequestParam String query,
			@PathVariable String code) {
		logger.debug("query: " + query);
		List<Organization> list =entityService.findOrganizationByNameAndCode(query, code);

		return  list;
	}
	
	
	@RequestMapping(value="/Organization/ownObjective/{objectiveId}")
	public @ResponseBody List<Organization> findOrganizationByObjectOwner(
			@PathVariable Long objectiveId) {
		List<Organization> list = entityService.findOrganizationByObjectiveOwner(objectiveId);
		return list;
	}
	
}
