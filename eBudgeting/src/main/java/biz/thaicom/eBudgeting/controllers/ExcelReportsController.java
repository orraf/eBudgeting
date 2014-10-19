package biz.thaicom.eBudgeting.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;







import org.springframework.web.bind.annotation.ResponseBody;

import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.AssetMethod;
import biz.thaicom.eBudgeting.models.bgt.BudgetType;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.ObjectiveType;
import biz.thaicom.eBudgeting.models.pln.TargetUnit;
import biz.thaicom.eBudgeting.services.EntityService;
import biz.thaicom.security.models.Activeuser;
import biz.thaicom.security.models.ThaicomUserDetail;

@Controller
public class ExcelReportsController {

	private static final Logger logger = LoggerFactory.getLogger(ExcelReportsController.class);
	
	// กำหนด entityService ไว้ใช้ในการดึง database
	@Autowired
	public EntityService entityService;
	
	
	@RequestMapping("/admin/excel/sample.xls")
	public String excelSample(Model model) {
		model.addAttribute("fiscalYear", 2556);
		
		return "sample.xls";
	}

	@RequestMapping("/m51r01.xls/{fiscalYear}/file/m51r01.xls")
	public String excelM51R01(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 109);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 109);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r01.xls";
	}

	@RequestMapping("/m51r02.xls/{fiscalYear}/file/m51r02.xls")
	public String excelM51R02(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 121);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 121);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r02.xls";
	}

	@RequestMapping("/m51r03.xls/{fiscalYear}/file/m51r03.xls")
	public String excelM51R03(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 110);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 110);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r03.xls";
	}

	@RequestMapping("/m51r04.xls/{fiscalYear}/file/m51r04.xls")
	public String excelM51R04(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 111);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 111);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r04.xls";
	}

	@RequestMapping("/m51r05.xls/{fiscalYear}/file/m51r05.xls")
	public String excelM51R05(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 112);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 112);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r05.xls";
	}

	@RequestMapping("/m51r06.xls/{fiscalYear}/file/m51r06.xls")
	public String excelM51R06(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 101);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 101);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r06.xls";
	}

	@RequestMapping("/m51r07.xls/{fiscalYear}/file/m51r07.xls")
	public String excelM51R07(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 102);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 102);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r07.xls";
	}

	@RequestMapping("/m51r08.xls/{fiscalYear}/file/m51r08.xls")
	public String excelM51R08(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 103);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 103);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r08.xls";
	}

	@RequestMapping("/m51r09.xls/{fiscalYear}/file/m51r09.xls")
	public String excelM51R09(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 104);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 104);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r09.xls";
	}

	@RequestMapping("/m51r10.xls/{fiscalYear}/file/m51r10.xls")
	public String excelM51R10(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 105);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 105);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r10.xls";
	}

	@RequestMapping("/m51r11.xls/{fiscalYear}/file/m51r11.xls")
	public String excelM51R11(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 106);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 106);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r11.xls";
	}

	@RequestMapping("/m51r12.xls/{fiscalYear}/file/m51r12.xls")
	public String excelM51R12(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 107);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 107);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r12.xls";
	}

	@RequestMapping("/m51r13.xls/{fiscalYear}/file/m51r13.xls")
	public String excelM51R13(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 108);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 108);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r13.xls";
	}

	@RequestMapping("/m51r14.xls/{fiscalYear}/file/m51r14.xls")
	public String excelM51R14(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 113);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 113);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r14.xls";
	}

	@RequestMapping("/m51r15.xls/{fiscalYear}/file/m51r15.xls")
	public String excelM51R15(@PathVariable Integer fiscalYear, Model model) {
		
		List<BudgetType> type = entityService.findBudgetTypeByLevel(fiscalYear, 3);
		
		model.addAttribute("type", type);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r15.xls";
	}

	@RequestMapping("/m51r16.xls/{fiscalYear}/file/m51r16.xls")
	public String excelM51R16(@PathVariable Integer fiscalYear, Model model) {
		
		List<Objective> objectiveList = entityService.findAllObjectiveChildren(fiscalYear,(long) 101);
		
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r16.xls";
	}

	@RequestMapping("/m51r17.xls/{fiscalYear}/file/m51r17.xls")
	public String excelM51R17(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findDeepObjectiveTypeById((long) 114);
		List<Objective> objectiveList = entityService.findAllObjectiveChildren(fiscalYear,(long) 114);

		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r17.xls";
	}

	@RequestMapping("/m51r18.xls/{fiscalYear}/file/m51r18.xls")
	public String excelM51R18(@PathVariable Integer fiscalYear, Model model) {
		
		List<TargetUnit> unit = entityService.findAllTargetUnits();
		
		model.addAttribute("unit", unit);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r18.xls";
	}

	@RequestMapping("/m51r19.xls/{fiscalYear}/file/m51r19.xls")
	public String excelM51R19(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 114);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 114);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r19.xls";
	}

	@RequestMapping("/m51r20.xls/{fiscalYear}/file/m51r20.xls")
	public String excelM51R20(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 115);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 115);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r20.xls";
	}

	@RequestMapping("/m51r21.xls/{fiscalYear}/file/m51r21.xls")
	public String excelM51R21(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 116);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 116);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r21.xls";
	}

	@RequestMapping("/m51r22.xls/{fiscalYear}/file/m51r22.xls")
	public String excelM51R22(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 118);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 118);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r22.xls";
	}

	@RequestMapping("/m51r23.xls/{fiscalYear}/file/m51r23.xls")
	public String excelM51R23(@PathVariable Integer fiscalYear, Model model) {
		
		List<Objective> objectiveList1 = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 119);
		List<Objective> objectiveList2 = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 120);
		
		model.addAttribute("objectiveList1", objectiveList1);
		model.addAttribute("objectiveList2", objectiveList2);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m51r23.xls";
	}

	@RequestMapping("/m52r01.xls/{fiscalYear}/file/m52r01.xls")
	public String excelM52R01(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 113);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 113);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m52r01.xls";
	}

	@RequestMapping("/m52r01_1.xls/{fiscalYear}/file/m52r01_1.xls")
	public String excelM52R01_1(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser) {
		
		List<List<Objective>> returnList = entityService.findObjectivesByFiscalyearAndTypeIdAndInitObjectiveBudgetProposal(fiscalYear, (long) 101, currentUser.getWorkAt());
		
		
		
		
		List<Objective> objList = new ArrayList<Objective>();
 		List<Objective> allList = returnList.get(0);
		HashMap<Long, Objective> objMap = new HashMap<Long, Objective>();		
		for(Objective o : allList) {
			o.setChildren(new ArrayList<Objective>());
			objMap.put(o.getId(), o);
			logger.debug("put " + o.getFilterObjectiveBudgetProposals().size());
		}
		// now connect the children
		for(Objective obj : allList) {
			Objective parent = objMap.get(obj.getParent().getId());
			if(parent != null) {
				parent.getChildren().add(obj);
			} else {
				if(obj.getType().getId() == 101L) 
					objList.add(obj);
			}
			
		}
		
		model.addAttribute("objectiveList", objList);
		
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
		return "m52r01_1.xls";
	}
	
	@RequestMapping("/m52r02_1.xls/{fiscalYear}/file/m52r02_1.xls")
	public String excelM52R02_1(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser) {
		
		List<List<Objective>> returnList = entityService.findObjectivesByFiscalyearAndTypeIdAndInitBudgetProposal(fiscalYear, (long) 101, currentUser.getWorkAt());
		
		
		
		
		List<Objective> objList = new ArrayList<Objective>();
 		List<Objective> allList = returnList.get(0);
		HashMap<Long, Objective> objMap = new HashMap<Long, Objective>();		
		for(Objective o : allList) {
			o.setChildren(new ArrayList<Objective>());
			objMap.put(o.getId(), o);
			
		}
		// now connect the children
		for(Objective obj : allList) {
			Objective parent = objMap.get(obj.getParent().getId());
			if(parent != null) {
				parent.getChildren().add(obj);
			} else {
				if(obj.getType().getId() == 101L) 
					objList.add(obj);
			}
			
		}
		
		model.addAttribute("objectiveList", objList);
		
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
		return "m52r02_1.xls";
	}

	@RequestMapping("/m52r28.xls/{fiscalYear}/file/m52r28.xls")
	public String excelM52R28(@PathVariable Integer fiscalYear, Model model) {
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 102);
		List<BudgetType> budgetTypeList = entityService.findBudgetTypeByLevel(fiscalYear, 1);
		
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("budgetTypeList", budgetTypeList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m52r28.xls";
	}

	@RequestMapping("/m53r01.xls/{fiscalYear}/file/m53r01.xls")
	public String excelM53R01(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 114);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 114);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m53r01.xls";
	}

	@RequestMapping("/m53r02.xls/{fiscalYear}/file/m53r02.xls")
	public String excelM53R02(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 115);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 115);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m53r02.xls";
	}

	@RequestMapping("/m53r03.xls/{fiscalYear}/file/m53r03.xls")
	public String excelM53R03(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 116);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 116);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m53r03.xls";
	}

	@RequestMapping("/m54r01.xls/{fiscalYear}/file/m54r01.xls")
	public String excelM54R01(@PathVariable Integer fiscalYear, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById((long) 118);
		
		List<Objective> objectiveList = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 118);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m54r01.xls";
	}

	@RequestMapping("/m55r01.xls/{fiscalYear}/file/m55r01.xls")
	public String excelM55R01(@PathVariable Integer fiscalYear, Model model) {
		
		List<Objective> objectiveList1 = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 119);
		List<Objective> objectiveList2 = entityService.findObjectivesByFiscalyearAndTypeId(fiscalYear, (long) 120);
		
		model.addAttribute("objectiveList1", objectiveList1);
		model.addAttribute("objectiveList2", objectiveList2);
		model.addAttribute("fiscalYear", fiscalYear);
		
		return "m55r01.xls";
	}

	@RequestMapping("/m81r01.xls/{fiscalYear}/file/m81r01.xls")
	public String excelM81R01(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		Objective root = entityService.findOneRootObjectiveByFiscalyear(fiscalYear);
		
		model.addAttribute("root", root);
		
		
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		
		response.addCookie(cookie);
		
		return "m81r01.xls";
	}
	

	@RequestMapping("/m81r12.xls/{fiscalYear}/file/m81r12.xls")
	public String excelM81R12(@PathVariable Integer fiscalYear, Model model,
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		logger.debug("m81r12.xls controller : ");
		
		Objective root = entityService.findOneRootObjectiveByFiscalyear(fiscalYear);
		
		model.addAttribute("root", root);		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
//		Organization searchOrg = entityService.findOrganizationById(searchOrgId);
//		
//		Organization parent = entityService.findOrganizationParentOf(searchOrg);
//		if(parent != null) {
//			searchOrg.setParent(parent);
//		}
		
		model.addAttribute("searchOrg", currentUser.getWorkAt());
		
		List<ActivityTargetReport> reports = entityService.findActivityTargetReportByOwnerOrRegulator(
				currentUser.getWorkAt(), fiscalYear);
		
		List<Object[]> sumFromG = entityService.findSumMonthlyFromGByFiscalYearAndOwnerLike(fiscalYear, currentUser.getWorkAt());
		
		model.addAttribute("sumFromG", sumFromG);
		
		model.addAttribute("reports", reports);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		
		response.addCookie(cookie);
		
		return "m81r12.xls";
	}
	
	@RequestMapping("/m81r02.xls/{fiscalYear}/{searchOrgId}/file/m81r02.xls")
	public String excelM81R02(@PathVariable Integer fiscalYear, Model model,
			@PathVariable Long searchOrgId,
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
//		List<List<Objective>> returnList = entityService.findObjectivesByFiscalyearAndTypeIdAndInitObjectiveBudgetProposal(fiscalYear, (long) 101, currentUser.getWorkAt());
//		
//		List<Objective> objList = new ArrayList<Objective>();
// 		List<Objective> allList = returnList.get(0);
//		HashMap<Long, Objective> objMap = new HashMap<Long, Objective>();		
//		for(Objective o : allList) {
//			o.setChildren(new ArrayList<Objective>());
//			objMap.put(o.getId(), o);
//			logger.debug("put " + o.getFilterObjectiveBudgetProposals().size());
//		}
//		// now connect the children
//		for(Objective obj : allList) {
//			Objective parent = objMap.get(obj.getParent().getId());
//			if(parent != null) {
//				parent.getChildren().add(obj);
//			} else {
//				if(obj.getType().getId() == 101L) 
//					objList.add(obj);
//			}
//			
//		}
//		
		Objective root = entityService.findOneRootObjectiveByFiscalyear(fiscalYear);
		
		model.addAttribute("root", root);
		
		//model.addAttribute("objectiveList", objList);
		
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
		Organization searchOrg = entityService.findOrganizationById(searchOrgId);
		
		if(searchOrg.getType() == OrganizationType.แผนก) {
			searchOrg = entityService.findOrganizationParentOf(searchOrg);
		}
		
		Organization parent = entityService.findOrganizationParentOf(searchOrg);
		if(parent != null) {
			searchOrg.setParent(parent);
		}
		
		model.addAttribute("searchOrg", searchOrg);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		
		response.addCookie(cookie);
		
		return "m81r02.xls";
	}
	
	@RequestMapping("/m81r03.xls/{fiscalYear}/file/m81r03.xls")
	public String excelM81R03(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
//		List<List<Objective>> returnList = entityService.findObjectivesByFiscalyearAndTypeIdAndInitObjectiveBudgetProposal(fiscalYear, (long) 101, currentUser.getWorkAt());
//		
//		List<Objective> objList = new ArrayList<Objective>();
// 		List<Objective> allList = returnList.get(0);
//		HashMap<Long, Objective> objMap = new HashMap<Long, Objective>();		
//		for(Objective o : allList) {
//			o.setChildren(new ArrayList<Objective>());
//			objMap.put(o.getId(), o);
//			logger.debug("put " + o.getFilterObjectiveBudgetProposals().size());
//		}
//		// now connect the children
//		for(Objective obj : allList) {
//			Objective parent = objMap.get(obj.getParent().getId());
//			if(parent != null) {
//				parent.getChildren().add(obj);
//			} else {
//				if(obj.getType().getId() == 101L) 
//					objList.add(obj);
//			}
//			
//		}
		
		Objective root = entityService.findOneRootObjectiveByFiscalyear(fiscalYear);
		
				
		model.addAttribute("root", root);
		
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		response.addCookie(cookie);
		
		return "m81r03.xls";
	}
	
	@RequestMapping("/m81r04.xls/{fiscalYear}/{id}/file/m81r04.xls")
	public String excelM81R04(@PathVariable Integer fiscalYear, @PathVariable Integer id, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		Activity activity = entityService.findOneActivity((long) id);
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("activity", activity);
		model.addAttribute("currentUser", currentUser);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		
		return "m81r04.xls";
	}
	
	@RequestMapping("/m81r05.xls/{fiscalYear}/{orgId}/file/m81r05.xls")
	public String excelM81R05(@PathVariable Integer fiscalYear,
			@PathVariable Long orgId,
			Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		List<Objective> objectives = entityService.findObjectivesByFiscalyearAndTypeIdForM81R05Report(fiscalYear, orgId);
		
		if(orgId > 0L) {
			model.addAttribute("organization", entityService.findOrganizationById(orgId));
		}
		
		model.addAttribute("objectives", objectives);
		
		model.addAttribute("fiscalYear", fiscalYear);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		return "m81r05.xls";
	}
	
	@RequestMapping("/m82r01.xls/{fiscalYear}/file/m82r01.xls")
	public String excelM82R01(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		
		
		Objective root = entityService.findObjectivesByFiscalyearAndTypeIdForM82R01Report(fiscalYear);
		
		//model.addAttribute("objectives", objectives);
		
		model.addAttribute("root", root);
		
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		return "m82r01.xls";
	}
	
	@RequestMapping("/m81r06.xls/{fiscalYear}/{startMonth}/{endMonth}/{objId}/{orgId}/file/m81r06.xls")
	public String excelM81R06(@PathVariable Integer fiscalYear, @PathVariable Integer startMonth, @PathVariable Integer endMonth, @PathVariable Long objId,
			
			@PathVariable Long orgId,
			Model model,
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		
		//Organization organization = entityService.findOrganizationById((long) currentUser.getWorkAt().getId());
		Organization organization = null;
		
		
		organization = entityService.findOrganizationById(orgId);
		
		Object[] returnObjs = entityService.findObjectiveForM81R06Report(objId, orgId, startMonth, endMonth);
		
		
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("startMonth", startMonth);
		model.addAttribute("endMonth", endMonth);
		model.addAttribute("objective", returnObjs[0]);
		model.addAttribute("targetValues", returnObjs[1]);
		
		model.addAttribute("organization", organization);
		model.addAttribute("currentUser", currentUser);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		

		return "m81r06.xls";
	}

	@RequestMapping("/m81r08.xls/{fiscalYear}/file/m81r08.xls")
	public String excelM81R08(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		List<AssetAllocation> assetAllocations = entityService.findAssetAllocationForReportM81r08(fiscalYear);
		List<AssetAllocation> noMethodAllocs = new ArrayList<AssetAllocation>();
		
		
		HashMap<AssetMethod, List<AssetAllocation>> assetMap = new HashMap<AssetMethod, List<AssetAllocation>>();
		
		
		List<AssetMethod> assetMethods = entityService.findAssetMethodAll();
		for(AssetMethod method : assetMethods) {
			assetMap.put(method, new ArrayList<AssetAllocation>());
		}
		
		for(AssetAllocation alloc: assetAllocations) {
			if(alloc.getAssetMethod() != null && assetMap.get(alloc.getAssetMethod()) != null) {
				
				assetMap.get(alloc.getAssetMethod()).add(alloc);
			} else {
				noMethodAllocs.add(alloc);
			}
		}
		
		model.addAttribute("assetMap", assetMap);
		model.addAttribute("noMethodAllocs", noMethodAllocs);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		
		response.addCookie(cookie);
		
		
		return "m81r08.xls";
	}
	
	@RequestMapping("/m81r11.xls/{fiscalYear}/file/m81r11.xls")
	public String excelM81R11(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		
		
		List<AssetAllocation> assetAllocations = entityService.findAssetAllocationForReportM81r11(fiscalYear, currentUser);
		List<AssetAllocation> noMethodAllocs = new ArrayList<AssetAllocation>();
		
		
		HashMap<AssetMethod, List<AssetAllocation>> assetMap = new HashMap<AssetMethod, List<AssetAllocation>>();
		
		
		List<AssetMethod> assetMethods = entityService.findAssetMethodAll();
		for(AssetMethod method : assetMethods) {
			assetMap.put(method, new ArrayList<AssetAllocation>());
		}
		
		for(AssetAllocation alloc: assetAllocations) {
			if(alloc.getAssetMethod() != null && assetMap.get(alloc.getAssetMethod()) != null) {
				
				assetMap.get(alloc.getAssetMethod()).add(alloc);
			} else {
				noMethodAllocs.add(alloc);
			}
		}
		
		model.addAttribute("assetMap", assetMap);
		model.addAttribute("noMethodAllocs", noMethodAllocs);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		
		
		response.addCookie(cookie);
		
		
		return "m81r08.xls";
	}
	
	
	@RequestMapping("/m81r09.xls/{fiscalYear}/file/m81r09.xls")
	public String excelM81R09(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		
		
		return "m81r09.xls";
	}
	
	
	@RequestMapping("/m81r10.xls/{fiscalYear}/file/m81r10.xls")
	public String excelM81R10(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		model.addAttribute("fiscalYear", fiscalYear);
		model.addAttribute("currentUser", currentUser);
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		
		
		return "m81r10.xls";
	}
	
	@RequestMapping("/admin/excel/report1.xls/{id}")
	public String excelReport1(@PathVariable Long id, Model model) {
		
		ObjectiveType type = entityService.findObjectiveTypeById(id);
		
		List<Objective> objectiveList = entityService.findObjectivesOf(type);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		
		return "report1.xls";
	}

	@RequestMapping("/admin/excel/report2.xls")
	public String excelReport2 (
		@RequestParam(required=false) String year,
		Model model) {
		model.addAttribute("fiscalYear", year);
		
		return "report2.xls";
	}

	@RequestMapping("/admin/excel/report11.xls/{id}")
	public String excelReport11 (
		@PathVariable Long id, 
		Model model) {
		ObjectiveType type = entityService.findObjectiveTypeById(id);
		List<Objective> objectiveList = entityService.findObjectivesOf(type);
		
		model.addAttribute("type", type);
		model.addAttribute("objectiveList", objectiveList);
		
		return "report11.xls";
	}
	
	@RequestMapping("/admin/excel/track1.xls")
	public String excelTrack1(Model model) {
		model.addAttribute("fiscalYear", 2556);
		
		return "track1.xls";
	}

	@RequestMapping("/m81r07.xls/{fiscalYear}/file/m81r07.xls")
	public String excelM81R07(@PathVariable Integer fiscalYear, Model model, 
			@Activeuser ThaicomUserDetail currentUser, HttpServletResponse response) {
		
		model.addAttribute("fiscalYear", fiscalYear);
		
		Objective root = entityService.findObjectivesByFiscalyearAndTypeIdForM81R07Report(fiscalYear);
		model.addAttribute("rootObjective", root);
		
		Iterable<Object[]> sumBudgetPlans = entityService.findAllSumBudgetPlanByFiscalYear(fiscalYear);
		model.addAttribute("sumBudgetPlans", sumBudgetPlans);
		
		Iterable<Object[]> sumBudgetUseds = entityService.findAllSumBudgetUsedByFiscalYear(fiscalYear);
		model.addAttribute("sumBudgetUseds", sumBudgetUseds);
		
		logger.debug("Calling findAllSumMonthlyBudgetByFiscalYear");
		Iterable<Object[]> sumMonthlyBudgets = entityService.findAllSumMonthlyBudgetByFiscalYear(fiscalYear);
		model.addAttribute("sumMonthlyBudgets", sumMonthlyBudgets);
		
		
		Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		
		
		return "m81r07_new.xls";
		
		//return "m81r07.xls";
	}

}
