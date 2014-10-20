package biz.thaicom.eBudgeting.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import biz.thaicom.eBudgeting.models.bgt.BudgetSignOff;
import biz.thaicom.eBudgeting.models.bgt.BudgetType;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.ObjectiveTypeId;
import biz.thaicom.eBudgeting.models.pln.TargetUnit;
import biz.thaicom.eBudgeting.models.webui.Breadcrumb;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

@Controller
public class GenericViewController {

	public static Logger logger = LoggerFactory.getLogger(GenericViewController.class);
	
	private static final String webAppcontext = "eBudgeting";
	
	@Autowired
	private EntityService entityService;
	
	@RequestMapping("/jsp/{jspName}")
	public String renderJsp(@PathVariable String jspName) {
		
		return jspName;
	}
	
	@RequestMapping("/page/m2f13/")
	public String render_m2f13_fiscalYear(Model model) {
		prepareRootPage(model);
		
		return "m2f13";
	}
	
	private void prepareRootPage(Model model) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		
	}

	@RequestMapping("/page/m2f13/{fiscalYear}/{budgetTypeId}")
	public String render_m2f13(Model model, HttpServletRequest request,
			@PathVariable Integer fiscalYear,
			@PathVariable Long budgetTypeId) { 
		
		logger.debug("fiscalYear = {}, budgetTypeId = {}", fiscalYear, budgetTypeId);
		
		if(budgetTypeId == null) 
			budgetTypeId = 0L;
		
		// now we just get the hold of this budgetType
		BudgetType budgetType = entityService.findBudgetTypeById(budgetTypeId);
		
		if(budgetType != null) {
			logger.debug("BudgetType found!");
			
			model.addAttribute("budgetType", budgetType);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbBudgetType("/page/m2f13", fiscalYear, budgetType); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("budgetType", budgetType);
			
		} else {
			logger.debug("BudgetType NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m2f13/";
		}
		
		return "m2f13";
	}
	

	
//	@RequestMapping("/page/m2f06/**")
//	public String render_m2f06(Model model, HttpServletRequest request) {
//		String pattern = (String)
//		        request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE); 
//		
//		String searchTerm = new AntPathMatcher().extractPathWithinPattern(pattern, 
//		        request.getServletPath());
//
//		String url = webAppcontext +  "/page/m2f06/";
//		List<Map<String,String>> breadcrumb = new ArrayList<Map<String,String>>();
//		
//		logger.debug(searchTerm);
//		if(searchTerm == null || searchTerm.length()==0) {
//			model.addAttribute("url", "/eBudgeting/Objective/root");
//			model.addAttribute("ROOT", true);
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("url", url);
//			map.put("value", "ROOT");
//			breadcrumb.add(map);
//			model.addAttribute("breadcrumb", breadcrumb);
//		} else {
//			// now tokenized the string
//			StringTokenizer token = new StringTokenizer(searchTerm,"/");
//			List<String> items = new ArrayList<String>();
//			while(token.hasMoreTokens()){
//				items.add(token.nextToken());
//			}
//			
//			if(items.size() == 1) {
//				// first part is year
//				model.addAttribute("url", "/eBudgeting/Objective/root/"+items.get(0));
//			} else {
//				model.addAttribute("url", "/eBudgeting/Objective/"+items.get(items.size()-1)+"/children");
//				model.addAttribute("lastObjectiveId", items.get(items.size()-1));
//				// now we need all parents of this object
//				
//			}
//			
//			Objective objective = null;
//			
//			// here we recontruct the breadcrumb
//			for(int i=0; i<items.size(); i++) {
//				
//				HashMap<String, String> map = new HashMap<String, String>();
//				
//				if(i > 0) {
//					url = url  + items.get(i) + "/";
//					map.put("url", url);
//					int index = objective.getIndex()+1;
//					map.put("value", objective.getType().getName() + "ที่  " + index);
//					breadcrumb.add(map);
//					
//				} else {
//					map.put("url", url);
//					map.put("value", "ROOT");
//					breadcrumb.add(map);
//					
//					map = new HashMap<String, String>();
//					url = url + items.get(i) + "/";
//					map.put("url", url);
//					map.put("value", items.get(i));
//					breadcrumb.add(map);
//
//				}
//				
//				if(i+1 < items.size()) {
//					// do this if it's not the last one
//					Long nextId = null;
//					try {
//						nextId = Long.parseLong(items.get(i+1));
//					} catch (NumberFormatException e) {
//						// we should just failed here! 
//					}
//					
//					objective = entityService.findOjectiveById(nextId);
//				}
//				
//				
//			}
//			model.addAttribute("breadcrumb", breadcrumb);
//		}
//		
//		model.addAttribute("currentPath", url);
//		return "m2f06";
//	}
	
	
	@RequestMapping("/page/m2f11/")
	public String runder_m2f11(
			Model model, HttpServletRequest request) {
		
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		
		return "m2f11";
	}
	
	@RequestMapping("/page/m2f11/{fiscalYear}/{objectiveId}")
	public String runder_m2f11OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m2f11", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m2f11/";
		}
		
		return "m2f11";

	}
	
	@RequestMapping("/page/m2f12/")
	public String runder_m2f12(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m2f12";
	}
	
	@RequestMapping("/page/m2f12/{fiscalYear}/{objectiveId}")
	public String render_m2f12OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request,
			@Activeuser ThaicomUserDetail currentUser) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m2f12", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m2f12/";
		}
		
		return "m2f12";
	}
	
	
	@RequestMapping("/page/m3f01/")
	public String runder_m3f01(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m3f01";
	}
	
	@RequestMapping("/page/m3f01/{fiscalYear}/{objectiveId}")
	public String render_m3f01OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m2f06", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m3f01/";
		}
		
		return "m3f01";
	}
	
	@RequestMapping("/page/m3f02/")
	public String render_m3f02(
			Model model, HttpServletRequest request) {
		
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		logger.debug("htt");
		return "m3f02";
	}
	
	@RequestMapping("/page/m3f02/{fiscalYear}/{objectiveId}")
	public String render_m3f02OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m3f02", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m3f02/";
		}
		
		return "m3f02";
	}
	
	@RequestMapping("/page/m3f03/")
	public String render_m3f03(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m3f03";
	}
	
	@RequestMapping("/page/m3f03/{fiscalYear}/{objectiveId}")
	public String render_m3f03OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m3f03", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m3f01/";
		}
		
		return "m3f03";
	}
	
	
	@RequestMapping("/page/m2f06/")
	public String runder_m2f06(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m2f06";
	}
	
	@RequestMapping("/page/m2f06/{fiscalYear}/{objectiveId}")
	public String render_m2f06OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m2f06", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m2f06/";
		}
		
		return "m2f06";
	}
	
	@RequestMapping("/page/m1f05/")
	public String runder_m1f05(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m1f05";
	}

	@RequestMapping("/page/m3f04/")
	public String runder_m3f04(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m3f04";
	}
	
	@RequestMapping("/page/m3f05/")
	public String runder_m3f05(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m3f05";
	}
	
	@RequestMapping("/page/m3f06/")
	public String runder_m3f06(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m3f06";
	}
	
	@RequestMapping("/page/m4f01/")
	public String runder_m4f01(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m4f01";
	}
	
	@RequestMapping("/page/m4f02/")
	public String runder_m4f02(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m4f02";
	}
	
	@RequestMapping("/page/m4f02/{fiscalYear}/{objectiveId}")
	public String render_m4f02OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m4f02", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m4f02/";
		}
		
		return "m4f02";
	}
	

	
	@RequestMapping("/page/m2f14/")
	public String render_m2f14(
			Model model, HttpServletRequest request) {
		model.addAttribute("rootPage", true);
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m2f14";
	}
	
	@RequestMapping("/page/m2f14/{fiscalYear}/")
	public String render_m2f14WithFiscalYear(
			@PathVariable Integer fiscalYear,
			Model model, HttpServletRequest request) {
		model.addAttribute("rootPage", false);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m2f14";
	}
	
	
	private Integer setFiscalYearFromSession(Model model, HttpSession session) {
		if(session.getAttribute("currentRootFY") != null) {
			Objective rootFy = (Objective) session.getAttribute("currentRootFY");
			model.addAttribute("fiscalYear", rootFy.getFiscalYear());
			return rootFy.getFiscalYear();
		}
		return null;
	}


	private Integer getCurrentFiscalYearFromSession(HttpSession session) {
		Objective rootFy = (Objective) session.getAttribute("currentRootFY");
		return rootFy.getFiscalYear();
	}



	// --------------------------------------------------------------m51f01: ทะเบียนแผนงาน
	@RequestMapping("/page/m51f01/")
	public String render_m51f01(
			Model model, HttpServletRequest request, HttpSession session) {
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		
		model.addAttribute("typeId", 101);
		
		String relatedTypeString = "";
		model.addAttribute("relatedTypeString", relatedTypeString);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", "");

		model.addAttribute("hasUnit", false);
		
		
		return "objectiveRegister";
	}
	
	// --------------------------------------------------------------m51f02: ทะเบียนยุทธศาสตร์
	
	@RequestMapping("/page/m51f02/")
	public String render_m51f02(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		
		setFiscalYearFromSession(model, session);
		
		
		model.addAttribute("typeId", 102);
		
		String relatedTypeString = "";
		model.addAttribute("relatedTypeString", relatedTypeString);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasUnit", false);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.แผนงาน.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.แผนงาน.getValue());
		
		
		return "objectiveRegister";
	}
	
	// --------------------------------------------------------------m51f03: ทะเบียนเป้าหมายผลผลิต
	@RequestMapping("/page/m51f03/")
	public String render_m51f03(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		model.addAttribute("typeId", 103);
		
		model.addAttribute("hasUnit", false);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.ยุทธศาสตร์.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.ยุทธศาสตร์.getValue());
		
		return "objectiveRegister";
	}
	
	
	// --------------------------------------------------------------m51f04: ทะเบียนกิจกรรมหลัก
	@RequestMapping("/page/m51f04/")
	public String render_m51f04(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		model.addAttribute("typeId", 104);
		
		model.addAttribute("hasUnit", false);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.ผลผลิตโครงการ.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.ผลผลิตโครงการ.getValue());
		
		return "objectiveRegister";
	}
	
	// --------------------------------------------------------------m51f05: ทะเบียนแผนปฏิบัติการ
	@RequestMapping("/page/m51f05/")
	public String render_m51f05(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		model.addAttribute("typeId", 105);
		
		model.addAttribute("hasUnit", false);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.กิจกรรมหลัก.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.กิจกรรมหลัก.getValue());
		
		return "objectiveRegister";
	}

	// --------------------------------------------------------------m51f06: ทะเบียนกิจกรรมรอง
	@RequestMapping("/page/m51f06/")
	public String render_m51f06(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		model.addAttribute("typeId", 106);
		
		model.addAttribute("hasUnit", false);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.แผนปฏิบัติการ.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.แผนปฏิบัติการ.getValue());
		
		return "objectiveRegister";
	}
	
	
	// --------------------------------------------------------------m51f07: ทะเบียนกิจกรรมย่อย
	@RequestMapping("/page/m51f07/")
	public String render_m51f07(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		model.addAttribute("typeId", 107);
		
		model.addAttribute("hasUnit", false);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.กิจกรรมรอง.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.กิจกรรมรอง.getValue());
		
		
		return "objectiveRegister";
	}

	// --------------------------------------------------------------m51f08: ทะเบียนกิจกรรมเสริม
	@RequestMapping("/page/m51f08/")
	public String render_m51f08(
			Model model,
			HttpServletRequest request, HttpSession session) {

		setFiscalYearFromSession(model, session);
		model.addAttribute("typeId", 108);
		
		model.addAttribute("hasUnit", false);
		
		String relatedTypeNameString = "";
		model.addAttribute("relatedTypeNameString", relatedTypeNameString);
		
		model.addAttribute("hasParent", true);
		model.addAttribute("parentTypeName", ObjectiveTypeId.กิจกรรมย่อย.getName());
		model.addAttribute("parentTypeId", ObjectiveTypeId.กิจกรรมย่อย.getValue());
		
		
		return "objectiveRegister";
	}

	
	
	
	
	// --------------------------------------------------------------m51f14: ทะเบียนรายการและระดับรายการ
	@RequestMapping("/page/m51f14/")
	public String render_m51f14(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		return "m51f14";
	}
	
	// --------------------------------------------------------------m51f15: ทะเบียนรายการกลาง
	@RequestMapping("/page/m51f15/")
	public String render_m51f15_fiscal(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		
		return "m51f15";
	}
	
	// --------------------------------------------------------------	m51f16: ทะเบียนรายการหลักสำหรับบันทึกงบประมาณกิจกรรม
	@RequestMapping("/page/m51f16/")
	public String render_m51f16_fiscal(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		return "m51f16";
	}
	
	
	// --------------------------------------------------------------	m51f17: การเชื่อมโยงกิจกรรม
	@RequestMapping("/page/m51f17/")
	public String render_m51f17_fiscal(
			Model model,
			HttpServletRequest request, HttpSession session) {

				
		model.addAttribute("rootPage", false);
		setFiscalYearFromSession(model, session);
		return "m51f17";
	}
	
	
	// --------------------------------------------------- m51f18: ข้อมูลหน่วยนับเป้าหมาย
	@RequestMapping("/page/m51f18/")
	public String render_m51f18(
			Model model, HttpServletRequest request, HttpSession session) {
		List<TargetUnit> targetUnits = entityService.findAllTargetUnits();		
		model.addAttribute("rootPage", true);
		model.addAttribute("targetUnits", targetUnits);
		return "m51f18";
	}
	
	// --------------------------------------------------- m52f01: ทะเบียนรายการงบลงทุน
		@RequestMapping("/page/m52f01/")
		public String render_m52f01(
				Model model, HttpServletRequest request, HttpSession session) {
			return "m52f01";
		}

		
		// --------------------------------------------------- m52f01: ทะเบียนรายการงบลงทุน
		@RequestMapping("/page/m52f02/")
		public String render_m52f02(
				Model model, HttpServletRequest request, HttpSession session) {
			return "m52f02";
		}

	
	// --------------------------------------------------------------m61f03: การบันทึกงบประมาณ ระดับกิจกรรมหลัก
	@RequestMapping("/page/m61f03_1/")
	public String render_m61f03(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();
		Integer fy = setFiscalYearFromSession(model, session);
		model.addAttribute("rootPage", false);
		model.addAttribute("fiscalYears", fiscalYears);
		
		//check the budgetSignOff
		BudgetSignOff bso = entityService.findBudgetSignOffByFiscalYearAndOrganization(
				fy, currentUser.getWorkAt());
		
		if(bso.getLock1Person() != null) {
			// should not be able to edit!
			model.addAttribute("readOnly", true);
		}
		
		return "m61f03_1";
	}
	
	
	// --------------------------------------------------------------m61f04: การบันทึกงบประมาณ ระดับรายการ
//	@RequestMapping("/page/m61f04/")
//	public String render_m61f04(
//			Model model, HttpServletRequest request, HttpSession session,
//			@Activeuser ThaicomUserDetail currentUser) {
//			
//		model.addAttribute("rootPage", false);
//		
//		setFiscalYearFromSession(model, session);
//		
//		Integer fy = getCurrentFiscalYearFromSession(session);
//		Objective rootObjective = entityService.findOneRootObjectiveByFiscalyear(fy);
//		
//		model.addAttribute("objectiveId", rootObjective.getId());
//
//		//check the budgetSignOff
//		BudgetSignOff bso = entityService.findBudgetSignOffByFiscalYearAndOrganization(
//				fy, currentUser.getWorkAt());
//		
//		if(bso.getLock1Person() != null) {
//			// should not be able to edit!
//			model.addAttribute("readOnly", true);
//		}
//		
//		return "m61f04";
//	}

	// --------------------------------------------------------------m61f05: การบันทึก SignOff/Release
	@RequestMapping("/page/m61f05/")
	public String render_m61f05(
			Model model, HttpServletRequest request, HttpSession session) {
			
		model.addAttribute("rootPage", false);
		
		setFiscalYearFromSession(model, session);
		
		Integer fy = getCurrentFiscalYearFromSession(session);
		Objective rootObjective = entityService.findOneRootObjectiveByFiscalyear(fy);
		
		model.addAttribute("objectiveId", rootObjective.getId());
		
		
		return "m61f05";
	}
	
	
	// --------------------------------------------------------------m61f04: การบันทึกงบประมาณ ระดับรายการ
	@RequestMapping("/page/m61f04_1/")
	public String render_m61f04_1(
			Model model, HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
			
		model.addAttribute("rootPage", false);
		
		setFiscalYearFromSession(model, session);
		
		Integer fy = getCurrentFiscalYearFromSession(session);
		Objective rootObjective = entityService.findOneRootObjectiveByFiscalyear(fy);
		
		model.addAttribute("objectiveId", rootObjective.getId());
		
		//check the budgetSignOff
		BudgetSignOff bso = entityService.findBudgetSignOffByFiscalYearAndOrganization(
				fy, currentUser.getWorkAt());
		
		if(bso.getLock1Person() != null) {
			// should not be able to edit!
			model.addAttribute("readOnly", true);
		}

		
		return "m61f04_1";
	}

	
	// --------------------------------------------------------------m62f01: การประมวลผลการกระทบยอดเงินงบประมาณจากระดับรายการมาที่ระดับกิจกรรม 
	@RequestMapping("/page/m62f01/")
	public String render_m62f01(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m62f01";
	}
	
	@RequestMapping("/page/m62f01/{fiscalYear}/{objectiveId}")
	public String render_m62f01OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request,
			@Activeuser ThaicomUserDetail currentUser) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m61f03", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m61f03/";
		}
		
		return "m62f01";
	}
	
	
	// --------------------------------------------------------------m62f02: การประมวลผลการกระทบยอดเงินงบประมาณจากระดับรายการมาที่ระดับกิจกรรม 
	@RequestMapping("/page/m62f02/")
	public String render_m62f02(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m62f01";
	}
	
	@RequestMapping("/page/m63f02/")
	public String runder_m63f02(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m63f02";
	}
	
	@RequestMapping("/page/m63f02/{fiscalYear}/{objectiveId}")
	public String render_m63f02OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m63f02", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m63f02/";
		}
		
		return "m63f02";
	}
	
	@RequestMapping("/page/m64f02/")
	public String render_m64f02(
			Model model, HttpServletRequest request) {
		
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		logger.debug("htt");
		return "m64f02";
	}
	
	@RequestMapping("/page/m64f02/{fiscalYear}/{objectiveId}")
	public String render_m64f02OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m64f02", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m64f02/";
		}
		
		return "m64f02";
	}
	
	@RequestMapping("/page/m65f02/")
	public String render_m65f02(
			Model model, HttpServletRequest request) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();		
		model.addAttribute("rootPage", true);
		model.addAttribute("fiscalYears", fiscalYears);
		return "m65f02";
	}
	
	@RequestMapping("/page/m65f02/{fiscalYear}/{objectiveId}")
	public String render_m65f02OfYear(
			@PathVariable Integer fiscalYear,
			@PathVariable Long objectiveId,
			Model model, HttpServletRequest request) {
		
		logger.debug("fiscalYear = {}, objectiveId = {}", fiscalYear, objectiveId);
		
		// now find the one we're looking for
		Objective objective = entityService.findOjectiveById(objectiveId);
		if(objective != null ) {
			logger.debug("Objective found!");
			
			model.addAttribute("objective", objective);
			// now construct breadcrumb?
			
			List<Breadcrumb> breadcrumb = entityService.createBreadCrumbObjective("/page/m65f02", fiscalYear, objective); 
			
			model.addAttribute("breadcrumb", breadcrumb.listIterator());
			model.addAttribute("rootPage", false);
			model.addAttribute("objective", objective);
			
		} else {
			logger.debug("Objective NOT found! redirect to fiscal year selection");
			// go to the root one!
			return "redirect:/page/m65f02/";
		}
		
		return "m65f02";
	}
	
	
	// --------------------------------------------------------------m71f01: การจัดสรรงบประมาณ
	@RequestMapping("/page/m71f01/")
	public String render_m71f01(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();
		Integer fy = setFiscalYearFromSession(model, session);
		model.addAttribute("rootPage", false);
		model.addAttribute("fiscalYears", fiscalYears);
		
		//check the budgetSignOff
		BudgetSignOff bso = entityService.findBudgetSignOffByFiscalYearAndOrganization(
				fy, currentUser.getWorkAt());
		
		if(bso != null && bso.getLock1Person() != null) {
			// should not be able to edit!
			model.addAttribute("readOnly", true);
		}
		
		return "m71f01";
	}
	
	
	

	
	// --------------------------------------------------------------m73f01: การบันทึกกิจกรรมย่อย
	@RequestMapping("/page/m73f01/")
	public String render_m73f01(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();
		Organization parentOrg =  entityService.findOrganizationParentOf(currentUser.getWorkAt());
		Integer fy = setFiscalYearFromSession(model, session);
		model.addAttribute("rootPage", false);
		
		logger.debug("fiscalYear :" + session.getAttribute("currentRootFY"));
		
		model.addAttribute("currentOrganizationId", currentUser.getWorkAt().getId());
		model.addAttribute("parentCurrentOrganizationId", parentOrg.getId());
		
		//check the budgetSignOff
		//BudgetSignOff bso = entityService.findBudgetSignOffByFiscalYearAndOrganization(
		//		fy, currentUser.getWorkAt());
		
		//if(bso != null && bso.getLock1Person() != null) {
			// should not be able to edit!
		//	model.addAttribute("readOnly", true);
		//	}
		
		return "m73f01";
	}

	// --------------------------------------------------------------m73f02: การบันทึกกิจกรรมย่อย ระดับส่วนงาน
	@RequestMapping("/page/m73f02/")
	public String render_m73f02(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();
		Integer fy = setFiscalYearFromSession(model, session);
		model.addAttribute("rootPage", false);
		model.addAttribute("fiscalYears", fiscalYears);
		model.addAttribute("workAtId", currentUser.getWorkAt().getId());
		
		//check the budgetSignOff
//		BudgetSignOff bso = entityService.findBudgetSignOffByFiscalYearAndOrganization(
//				fy, currentUser.getWorkAt());
//		
//		if(bso != null && bso.getLock1Person() != null) {
			// should not be able to edit!
//			model.addAttribute("readOnly", true);
//		}
		
		return "m73f02";
	}
	// --------------------------------------------------------------m73f03: การบันทึกกิจกรรมย่อย ระดับจังหวัด
	@RequestMapping("/page/m73f03/")
	public String render_m73f03(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		model.addAttribute("organizationId", currentUser.getWorkAt().getId());
		
		return "m73f03";
	}
	// --------------------------------------------------------------m74f01: การบันทึกแผนงาน
	@RequestMapping("/page/m74f01/")
	public String render_m74f01(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		model.addAttribute("organizationId", currentUser.getWorkAt().getId());
		
		return "m74f01";
	}

	// --------------------------------------------------------------m74f01: การบันทึกแผนงบลงทุน
	@RequestMapping("/page/m74f02/")
	public String render_m74f02(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		model.addAttribute("workAtId", currentUser.getWorkAt().getId());
		
		return "m74f02";
	}
	// --------------------------------------------------------------m81f01: การบันทึกผลการดำเนินงาน
	@RequestMapping("/page/m81f01/")
	public String render_m81f01(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		List<Objective> fiscalYears = entityService.findRootFiscalYear();
		setFiscalYearFromSession(model, session);
		model.addAttribute("rootPage", false);
		model.addAttribute("fiscalYears", fiscalYears);
		model.addAttribute("workAtId", currentUser.getWorkAt().getId());
		
		return "m81f01";
	}
	// --------------------------------------------------------------m81f02: การบันทึกผลการดำเนินงานงบลงทุน
	@RequestMapping("/page/m81f02/")
	public String render_m81f02(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		model.addAttribute("workAtId", currentUser.getWorkAt().getId());
		
		return "m81f02";
	}

	// --------------------------------------------------------------m81r02: รายงานแผนปฏิบัติการสำหรับส่วนงาน/สกยจ./สกยอ.
	@RequestMapping("/page/m81r02/")
	public String render_m81r02(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		Integer currentfy = setFiscalYearFromSession(model, session);
		logger.debug("currentOrganizationId:" + currentUser.getWorkAt().getId());
		model.addAttribute("currentOrganizationId", currentUser.getWorkAt().getId());

		String userGroups = "";
	    String delim = "";
	    Boolean isPlan = false;
	 	for(GrantedAuthority a :  currentUser.getAuthorities()) {
		    userGroups += delim + a.getAuthority();
		    delim = ",";
		    
		    if(a.getAuthority().toString().equals("PMS_PLAN") || a.getAuthority().toString().equals("PMS_ADMIN")){
		    	isPlan = true;
		    }
		    
		}
		
		model.addAttribute("userGroups", userGroups);
		
		if(isPlan) {
			return "m81r02";
		} else {
			return "redirect:/m81r02.xls/"+ currentfy + "/"+ currentUser.getWorkAt().getId() + "/file/m81r02.xls";
		}
		
	}
	
	@RequestMapping("/page/m82r01/")
	public String render_m82r01(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		model.addAttribute("organizationId", currentUser.getWorkAt().getId());
		
		return "m82r01";
	}
	
	@RequestMapping("/page/m82r02/")
	public String render_m82r02(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		model.addAttribute("organizationId", currentUser.getWorkAt().getId());
		
		return "m82r02";
	}
	
	@RequestMapping("/page/m81r04/")
	public String render_m81r04(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		logger.debug("currentOrganizationId:" + currentUser.getWorkAt().getId());
		if(currentUser.getWorkAt().getType() == OrganizationType.แผนก) {
			Organization searchOrg = entityService.findOrganizationParentOf(currentUser.getWorkAt());
			
			
			model.addAttribute("currentOrganizationId", searchOrg.getId());
			model.addAttribute("userOrgType", searchOrg.getType());			
		} else {
			model.addAttribute("currentOrganizationId", currentUser.getWorkAt().getId());
			model.addAttribute("userOrgType", currentUser.getWorkAt().getType());
		}
		
		return "m81r04";
	}
	
	@RequestMapping("/page/m81r05/")
	public String render_m81r05(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		logger.debug("currentOrganizationId:" + currentUser.getWorkAt().getId());
		
		List<Organization> orgList = entityService.findOrganization_ฝ่าย();
		
		model.addAttribute("orgList",orgList);
		
		return "m81r05";
	}
	

	@RequestMapping("/page/m81r06/")
	public String render_m81r06(
			Model model,
			HttpServletRequest request, HttpSession session,
			@Activeuser ThaicomUserDetail currentUser) {
		model.addAttribute("rootPage", true);
		setFiscalYearFromSession(model, session);
		logger.debug("currentOrganizationId:" + currentUser.getWorkAt().getId());
		model.addAttribute("currentOrganizationId", currentUser.getWorkAt().getId());
		
		String userGroups = "";
	    String delim = "";
	 	for(GrantedAuthority a :  currentUser.getAuthorities()) {
		    userGroups += delim + a.getAuthority();
		    delim = ",";
		}
		
		model.addAttribute("userGroups", userGroups);
		
		return "m81r06";
	}
	
}


