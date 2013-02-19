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

import biz.thaicom.eBudgeting.models.pln.Activity;
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
		return entityService.updateActivity(node);
	}
	
	@RequestMapping(value="/Activity/", method=RequestMethod.POST)
	public @ResponseBody Activity saveActivity(
			@RequestBody JsonNode node) {
		return entityService.saveActivity(node);
	}
	
	@RequestMapping(value="/Activity/{id}", method=RequestMethod.DELETE)
	public @ResponseBody Activity deleteActivity(
			@PathVariable Long id) {
		return entityService.deleteActivity(id);
	}
	

	
	
	

}
