package biz.thaicom.eBudgeting.controllers.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Throwables;

import biz.thaicom.eBudgeting.controllers.error.RESTError;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.repositories.OrganizationRepository;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.eBudgeting.services.EntityServiceJPA;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

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

	@RequestMapping(value="/Organization/findTopLevelByName", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findOrganizationTopLevelByName(
			@RequestParam String query) {
		logger.debug("query: " + query);
		List<Organization> list =entityService.findOrganizationTopLevelByName(query);

		return  list;
	}

		
	@RequestMapping(value="/Organization/findAllProvinces", method=RequestMethod.GET)
	public @ResponseBody List<Organization> findAllProvinces(
			@Activeuser ThaicomUserDetail currentUser 
			) {
		List<Organization> list = entityService.findOrganizationProvinces();
		
		return list;
	}
	
	@RequestMapping(value="/Organization/findRoot", method=RequestMethod.GET)
	public @ResponseBody Organization findOrganizationRoot() {
		
		Organization root = entityService.findOrganizationRoot();
		
		return root;
	}
	
	@RequestMapping(value="/Organization/{id}/children", method=RequestMethod.GET)
	public @ResponseBody List<Organization> findChildrenByParentId(
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser 
			) {
		Organization org = entityService.findOrganizationById(id);
		List<Organization> children = new ArrayList<Organization>();
		for(Organization child : org.getChildren()) {
			if(child.getInActive().equals('N')) {
				children.add(child);
			}
		}
		
		return children;
	}
	
	
	@RequestMapping(value="/Organization/findAllProvincesAndSelf", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findAllProvincesAndSelf(
			@Activeuser ThaicomUserDetail currentUser, 
			@RequestParam String query
			) {
		List<Organization> list = new ArrayList<Organization>();

		Organization org= entityService.findOrganizationById(currentUser.getWorkAt().getId());
		
		list.add(org);
		
		for(Organization child : org.getChildren()) {
			if(child.getInActive().equals('N')) {
				list.add(child);
			}
		}
		
		//list.addAll(entityService.findOrganizationByTop(query));
		list.addAll(entityService.findOrganizationByProvinces(query));
		
		
		return list;
	}

	@RequestMapping(value="/Organization/childrenOfProvince/{orgId}/findByName", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findOrganizationChildrenOfProvinceAndName(
			@RequestParam String query,
			@PathVariable Long orgId) {
		logger.debug("query: " + query);
		
		Organization org = entityService.findOrganizationById(orgId);
		if(org == null) return null;
		
		Long searchId = OrganizationType.getProvinceId(org);
		
//		if(org.getType() == OrganizationType.ส่วนในจังหวัด) {
//			searchId = org.getParent().getId();
//		}
		
		List<Organization> list =entityService.findOrganizationByNameAndParent_Id(query, searchId);

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
	
	@RequestMapping(value="/Organization/parentId/{parentId}/findByNameWithProcurement", method=RequestMethod.POST)
	public @ResponseBody List<Organization> findOrganizationByParentIdAndNameWithProcurement(
			@RequestParam String query,
			@PathVariable Long parentId) {
		logger.debug("query: " + query);
		List<Organization> list =entityService.findOrganizationByNameAndParent_IdWithProcuremnt(query, parentId);

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
	
	@RequestMapping(value="/Organization/currentSession/children")
	public @ResponseBody List<Organization> findOneByCurrentSession(
			@Activeuser ThaicomUserDetail currentUser) {
		
		return entityService.findOrganizationChildrenOrSiblingOf(currentUser.getWorkAt());
	}
	
	@RequestMapping(value="/Organization/{id}", method=RequestMethod.GET)
	public @ResponseBody Organization findOrganizationById(
			@PathVariable Long id) {
		return entityService.findOrganizationById(id);
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
