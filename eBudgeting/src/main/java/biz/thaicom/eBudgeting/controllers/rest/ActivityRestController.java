package biz.thaicom.eBudgeting.controllers.rest;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityPerformance;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetResult;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

@Controller
public class ActivityRestController {
	private static final Logger logger = LoggerFactory.getLogger(ActivityRestController.class);
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping(value="/Activity/currentOwner/forObjective/{objectiveId}")
	public @ResponseBody List<Activity> findActivityFromCurrentOnwerandObjective(
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser) {
		
		return entityService.findActivityByOwnerAndForObjective(currentUser.getWorkAt(), objectiveId);
	}
	
	@RequestMapping(value="/Activity/ownerId/{ownerId}/forObjective/{objectiveId}")
	public @ResponseBody List<Activity> findActivityFromOnwerandObjective(
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser,
			@PathVariable Long ownerId) {
		
		Organization org = entityService.findOrganizationById(ownerId);
		
		return entityService.findActivityByOwnerAndForObjective(org, objectiveId);
	}
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.GET)
	public @ResponseBody Activity findOneActivity(
			@PathVariable Long id) {
		return entityService.findOneActivity(id);
	}
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.PUT)
	public @ResponseBody Long updateActivity(
			@PathVariable Long id,
			@RequestBody JsonNode node) {
		Activity activity = entityService.updateActivity(node);
		
		// we're not updating the model!
		return activity.getId();
	}
	
	@RequestMapping(value="/Activity/", method=RequestMethod.POST)
	public @ResponseBody Long saveActivity(
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentUser) {
		
		Organization parent = entityService.findOrganizationParentOf(currentUser.getWorkAt());
		Activity activity;
		if(parent.getId() == 0L) {
			activity = entityService.saveActivity(node, currentUser.getWorkAt());	
		} else {
			activity = entityService.saveActivity(node, parent);
		}
		
		
		
		// we're not updating the model!
		return activity.getId();
	}
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.DELETE)
	public @ResponseBody Long deleteActivity(
			@PathVariable Long id) {
		return entityService.deleteActivity(id).getId();
	}
	

	
	@RequestMapping(value="/Activity/currentRegulator/forObjective/{objectiveId}", method=RequestMethod.GET) 
	public @ResponseBody List<Activity> findActivityByRegulatorAndObjectiveId(
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser) {
		
		return entityService.findActivityByRegularAndObjectiveId(currentUser.getWorkAt(), objectiveId);
	}
	
	@RequestMapping(value="/ActivityPerformance/currentOwner/forObjective/{objectiveId}", method=RequestMethod.GET) 
	public @ResponseBody List<ActivityPerformance> findActivityPerformancesByOwnerAndObjectiveId(
			@PathVariable Long objectiveId,
			@Activeuser ThaicomUserDetail currentUser) {
		
		return entityService.findActivityPerformancesByOwnerAndObjectiveId(currentUser.getWorkAt(), objectiveId);
	}

	@RequestMapping(value="/ActivityTargetReport/saveReportPlan/{id}", method=RequestMethod.PUT) 
	public @ResponseBody ActivityTargetReport saveActivityTargetReport(
			@PathVariable Long id, 
			@RequestBody JsonNode node){
		return entityService.saveActivityTargetReportPlan(id, node);
	}
	
	@RequestMapping(value="/ActivityTargetReport/{id}", method=RequestMethod.GET) 
	public @ResponseBody ActivityTargetReport findActivityTargetReportById(
			@PathVariable Long id){
		return entityService.findActivityTargetReportById(id);
	}
	
	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}", method=RequestMethod.GET)
	public @ResponseBody List<ActivityTargetReport> findActivityTargetReportByTargetId(
			@PathVariable Long targetId,
			@Activeuser ThaicomUserDetail currentUser) {
		// find top level report (ส่วนจังหวัด/ส่วน)
		return entityService.findActivityTargetReportByTargetIdAndReportLevel(targetId, 1);
	}

	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}/province/{orgId}", method=RequestMethod.GET)
	public @ResponseBody List<ActivityTargetReport> findActivityTargetReportByTargetIdAndParentOrgId(
			@PathVariable Long targetId,
			@PathVariable Long orgId) {
		return entityService.findActivityTargetReportByTarget_IdAndParentOrgId(targetId, orgId);
	}

	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}/province/{parentOrgId}", method=RequestMethod.POST)
	public @ResponseBody List<ActivityTargetReport> saveActivityTargetReportByTargetIdAndParentOrgId(
			@PathVariable Long targetId,
			@PathVariable Long parentOrgId,
			@RequestBody JsonNode node) {
		
		Organization org = entityService.findOrganizationById(parentOrgId);
		Long provinceId = OrganizationType.getProvinceId(org);
		
		return entityService.saveActivityTargetReportByTargetId(targetId, node, provinceId);
	}

	
	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}", method=RequestMethod.POST)
	public @ResponseBody List<ActivityTargetReport> saveActivityTargetReportByTargetId(
			@PathVariable Long targetId,
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentuser) {
		return entityService.saveActivityTargetReportByTargetId(targetId, node, null);
	}
	
	@RequestMapping(value="/ActivityTargetReports/Target/{targetId}", method=RequestMethod.POST)
	public @ResponseBody List<ActivityTargetReport> saveActivityTargetReportsByTargetId(
			@PathVariable Long targetId,
			@RequestBody JsonNode node) {
		return entityService.saveActivityTargetReportByTargetId(targetId, node, null);
	}
	
	
	@RequestMapping(value="/ActivityTargetResult/findBgtResultByReport/{targetReportId}/fiscalMonth/{fiscalMonth}", method=RequestMethod.GET)
	public @ResponseBody ActivityTargetResult findActivityTargetResultBgtByTargetReportAndFiscalMonth(
			@PathVariable Long targetReportId,
			@PathVariable Integer fiscalMonth,
			@Activeuser ThaicomUserDetail currentUser) {
		
		return entityService.findActivityTargetResultByReportAndFiscalMonthAndBgtResult(targetReportId, fiscalMonth);
		
	}
	
	@RequestMapping(value="/ActivityTargetResult/findResultByReport/{targetReportId}/fiscalMonth/{fiscalMonth}", method=RequestMethod.GET)
	public @ResponseBody List<ActivityTargetResult> findActivityTargetResultByTargetReportAndFiscalMonth(
			@PathVariable Long targetReportId,
			@PathVariable Integer fiscalMonth,
			@Activeuser ThaicomUserDetail currentUser) {
		logger.debug("fiscalMonth: "+ fiscalMonth);
		return entityService.findActivityTargetResultByReportAndFiscalMonth(targetReportId, fiscalMonth);
		
	}
	
	@RequestMapping(value="/ActivityTargetResult/{id}", method=RequestMethod.GET) 
	public @ResponseBody ActivityTargetResult findActivityTargetResultById(
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser){
		return entityService.findActivityTargetResultById(id);
	}
	
	@RequestMapping(value="/ActivityTargetResult/", method=RequestMethod.POST) 
	public @ResponseBody Long saveActivityTargetResult(
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentUser){
		ActivityTargetResult result = entityService.saveActivityTargetResult(node, currentUser);
		return result.getId();
	}
	
	@RequestMapping(value="/ActivityTargetResult/{id}", method=RequestMethod.PUT) 
	public @ResponseBody Long updateActivityTargetResult(
			@RequestBody JsonNode node,
			@PathVariable Long id,
			@Activeuser ThaicomUserDetail currentUser){
		logger.debug("calling PUT");
		ActivityTargetResult atr = entityService.saveActivityTargetResult(node, currentUser);
		return atr.getId();
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
