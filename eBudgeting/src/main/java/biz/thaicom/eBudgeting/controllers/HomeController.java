package biz.thaicom.eBudgeting.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.webui.Breadcrumb;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.security.controllers.CurrentUserHandlerMethodArgumentResolver;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private EntityService entityService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static final SimpleDateFormat buddhistYearSDF = new SimpleDateFormat("yyyy", new Locale("th","TH"));
	private static final SimpleDateFormat monthSDF = new SimpleDateFormat("MM", new Locale("th","TH"));
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model,
			HttpSession session, @RequestParam(required=false) String menuCode,  
			@RequestParam(required=false) Integer menuLevel,  
			@Activeuser ThaicomUserDetail currentUser) {
		
		List<Objective> root = entityService.findRootFiscalYear();
		Integer currentFY;
		
		if(root!= null && root.size() > 0) {
			session.setAttribute("rootFY", root);
		}
		
		if(session.getAttribute("currentRootFY") == null) {
			Integer year = Integer.parseInt(buddhistYearSDF.format(new Date()));
			Integer month = Integer.parseInt(monthSDF.format(new Date()));
			if(month > 9) {
				year = year+1;
			}
			
			logger.debug("current Year: " + year);
			Boolean foundFiscalYear = false;
			// now find current fyYear
			for(int i=0; i<root.size(); i++) {
				Objective o = root.get(i);
				if(o.getFiscalYear().equals(year)) {
					session.setAttribute("currentRootFY", o);
					logger.debug("year = "  + year);
					foundFiscalYear = true;
					break;
				}
			}
			
			if(!foundFiscalYear) {
				Objective o = entityService.findRootMaxFiscalYear();
				session.setAttribute("currentRootFY", o);
				year = o.getFiscalYear();
			}
			
			currentFY = year;
		} else {
			currentFY = ((Objective) session.getAttribute("currentRootFY")).getFiscalYear();
		}
		
		if(session.getAttribute("navbarBreadcrumb")!= null) {
			@SuppressWarnings("unchecked")
			List<Breadcrumb> navbarBreadcrumb = (List<Breadcrumb>) session.getAttribute("navbarBreadcrumb");
			
			if(menuLevel == null) {
				//empty all session variable
				for(Breadcrumb b : navbarBreadcrumb) {
					b.setUrl(null);
					b.setValue(null);
				}
			} else if(menuLevel == 0) {
				//empty the last Breadcrumb
				navbarBreadcrumb.get(1).setUrl(null);
				navbarBreadcrumb.get(1).setValue(null);
				
				model.addAttribute("menuLevel", menuLevel);
				model.addAttribute("menuCode", menuCode);
				
			} else {
				model.addAttribute("menuLevel", menuLevel);
				model.addAttribute("menuCode", menuCode);
				
			}
			
			
			session.setAttribute("navbarBreadcrumb", navbarBreadcrumb);
		}
		
		model.addAttribute("fySltEnable", true);
		
		List<Objective> noReportObjectives = 
				entityService.findObjectiveByActivityTargetReportOfOrganizationAndFiscalYearNoReportCurrentMonth(currentUser.getWorkAt(), currentFY);
		
		model.addAttribute("noReportObjectives", noReportObjectives);
		
		String userGroups = "";
	    String delim = "";
	 	for(GrantedAuthority a :  currentUser.getAuthorities()) {
		    userGroups += delim + a.getAuthority();
		    delim = ",";
		}
		
		model.addAttribute("userGroups", userGroups);
		
		return "dashboard";
	}
	
}
