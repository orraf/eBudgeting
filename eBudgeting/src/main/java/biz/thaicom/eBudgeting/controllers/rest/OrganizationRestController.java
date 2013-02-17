package biz.thaicom.eBudgeting.controllers.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
		List<Organization> list =entityService.findOrganizationByName(query);

		return  list;
	}
	
}
