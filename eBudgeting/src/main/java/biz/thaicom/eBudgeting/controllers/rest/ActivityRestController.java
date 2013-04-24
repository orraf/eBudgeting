package biz.thaicom.eBudgeting.controllers.rest;

import java.util.List;

import javax.servlet.ServletRequest;

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
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.GET)
	public @ResponseBody Activity findOneActivity(
			@PathVariable Long id) {
		return entityService.findOneActivity(id);
	}
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.PUT)
	public @ResponseBody Activity updateActivity(
			@PathVariable Long id,
			@RequestBody JsonNode node) {
		entityService.updateActivity(node);
		
		// we're not updating the model!
		return null;
	}
	
	@RequestMapping(value="/Activity/", method=RequestMethod.POST)
	public @ResponseBody Activity saveActivity(
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentUser) {
		entityService.saveActivity(node, currentUser.getWorkAt());
		
		// we're not updating the model!
		return null;
	}
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.DELETE)
	public @ResponseBody Activity deleteActivity(
			@PathVariable Long id) {
		return entityService.deleteActivity(id);
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

	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}/parentOrganization/{parentOrgId}", method=RequestMethod.GET)
	public @ResponseBody List<ActivityTargetReport> findActivityTargetReportByTargetIdAndParentOrgId(
			@PathVariable Long targetId,
			@PathVariable Long parentOrgId) {
		return entityService.findActivityTargetReportByTarget_IdAndParentOrgId(targetId, parentOrgId);
	}

	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}/parentOrganization/{parentOrgId}", method=RequestMethod.POST)
	public @ResponseBody List<ActivityTargetReport> saveActivityTargetReportByTargetIdAndParentOrgId(
			@PathVariable Long targetId,
			@PathVariable Long parentOrgId,
			@RequestBody JsonNode node) {
		return entityService.saveActivityTargetReportByTargetId(targetId, node, parentOrgId);
	}

	
	@RequestMapping(value="/ActivityTargetReport/findByTarget/{targetId}", method=RequestMethod.POST)
	public @ResponseBody List<ActivityTargetReport> saveActivityTargetReportByTargetId(
			@PathVariable Long targetId,
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentuser) {
		return entityService.saveActivityTargetReportByTargetId(targetId, node, null);
	}
	
	@RequestMapping(value="/ActivityTargetResult/", method=RequestMethod.POST) 
	public @ResponseBody ActivityTargetResult saveActivityTargetResult(
			@RequestBody JsonNode node,
			@Activeuser ThaicomUserDetail currentUser){
		return entityService.saveActivityTargetResult(node, currentUser);
	}

}