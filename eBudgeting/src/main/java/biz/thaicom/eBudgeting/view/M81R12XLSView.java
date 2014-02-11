package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityTargetReport;
import biz.thaicom.eBudgeting.models.pln.MonthlyActivityReport;
import biz.thaicom.eBudgeting.models.pln.MonthlyBudgetReport;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R12XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
	
	public static Logger logger = LoggerFactory.getLogger(M81R12XLSView.class);
	
	@Override
	protected Workbook createWorkbook() {
		return new HSSFWorkbook();
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		logger.debug("xxxx");
		
		ThaicomUserDetail currentUser = (ThaicomUserDetail) model.get("currentUser");
		
		@SuppressWarnings("unchecked")
		List<ActivityTargetReport> reports = (List<ActivityTargetReport>) model.get("reports");
		
		
		
		Organization searchOrg = (Organization) model.get("searchOrg");
		
		
        Map<String, CellStyle> styles = createStyles(workbook);

        Objective root = (Objective) model.get("root");
		Integer fiscalYear = (Integer) model.get("fiscalYear");
		Sheet sheet = workbook.createSheet("sheet1");
		Integer oldYear = fiscalYear - 1;

		Row firstRow = sheet.createRow(0);
		Cell cell0 = firstRow.createCell(0);
		cell0.setCellValue("วันที่พิมพ์รายงาน: " +  printTimeFormat.format(new Date()) );
		
		firstRow = sheet.createRow(1);
		Cell cell11 = firstRow.createCell(0);
		cell11.setCellValue("แผนปฏิบัติการประจำปีงบประมาณ " + fiscalYear);
		cell11.setCellStyle(styles.get("title"));

		Row secondRow = sheet.createRow(2);
		Cell cell21 = secondRow.createCell(0);
		if(searchOrg.isSubSection()) {
			cell21.setCellValue("หน่วยงาน " + searchOrg.getName() + " " + searchOrg.getParent().getName());
		} else {
			cell21.setCellValue("หน่วยงาน " + searchOrg.getName());
		}
		
		cell21.setCellStyle(styles.get("title"));
		
		
		Row thirdRow = sheet.createRow(3);
		Cell cell301 = thirdRow.createCell(0);
		cell301.setCellValue("แผนงาน/ผลผลิต/โครงการ/กิจกรรม");
		cell301.setCellStyle(styles.get("header"));
		Cell cell302 = thirdRow.createCell(1);
		cell302.setCellValue("เป้าหมาย");
		cell302.setCellStyle(styles.get("header"));
		Cell cell303 = thirdRow.createCell(2);
		cell303.setCellValue("แผน/ผล");
		cell303.setCellStyle(styles.get("header"));
		Cell cell304 = thirdRow.createCell(3);
		cell304.setCellValue("ตค." + oldYear.toString().substring(2, 4) );
		cell304.setCellStyle(styles.get("header"));
		Cell cell305 = thirdRow.createCell(4);
		cell305.setCellValue("พย." + oldYear.toString().substring(2, 4) );
		cell305.setCellStyle(styles.get("header"));
		Cell cell306 = thirdRow.createCell(5);
		cell306.setCellValue("ธค." + oldYear.toString().substring(2, 4) );
		cell306.setCellStyle(styles.get("header"));
		Cell cell307 = thirdRow.createCell(6);
		cell307.setCellValue("มค." + fiscalYear.toString().substring(2, 4) );
		cell307.setCellStyle(styles.get("header"));
		Cell cell308 = thirdRow.createCell(7);
		cell308.setCellValue("กพ." + fiscalYear.toString().substring(2, 4) );
		cell308.setCellStyle(styles.get("header"));
		Cell cell309 = thirdRow.createCell(8);
		cell309.setCellValue("มีค." + fiscalYear.toString().substring(2, 4) );
		cell309.setCellStyle(styles.get("header"));
		Cell cell310 = thirdRow.createCell(9);
		cell310.setCellValue("เมย." + fiscalYear.toString().substring(2, 4) );
		cell310.setCellStyle(styles.get("header"));
		Cell cell311 = thirdRow.createCell(10);
		cell311.setCellValue("พค." + fiscalYear.toString().substring(2, 4) );
		cell311.setCellStyle(styles.get("header"));
		Cell cell312 = thirdRow.createCell(11);
		cell312.setCellValue("มิย." + fiscalYear.toString().substring(2, 4) );
		cell312.setCellStyle(styles.get("header"));
		Cell cell313 = thirdRow.createCell(12);
		cell313.setCellValue("กค." + fiscalYear.toString().substring(2, 4) );
		cell313.setCellStyle(styles.get("header"));
		Cell cell314 = thirdRow.createCell(13);
		cell314.setCellValue("สค." + fiscalYear.toString().substring(2, 4) );
		cell314.setCellStyle(styles.get("header"));
		Cell cell315 = thirdRow.createCell(14);
		cell315.setCellValue("กย." + fiscalYear.toString().substring(2, 4) );
		cell315.setCellStyle(styles.get("header"));
		Cell cell316 = thirdRow.createCell(15);
		cell316.setCellValue("รวม");
		cell316.setCellStyle(styles.get("header"));

		int rowNum = 4;
		int i=0;
		ActivityTargetReport prevReport = null;
		ActivityTargetReport prevSum = null;
		List<ActivityTargetReport> sumReports = new ArrayList<ActivityTargetReport>();
		if(reports.size() > 0) {
			prevReport = reports.get(0);
			prevSum = ActivityTargetReport.createEmptyReport(prevReport.getTarget());
			sumReports.add(prevSum);
			logger.debug("first Target with id: " + prevReport.getTarget().getId());
		}
		for(ActivityTargetReport report : reports) {
			if(! prevReport.getTarget().getId().equals(report.getTarget().getId())) {
				logger.debug("new Target with id: " + report.getTarget().getId());
				// new Target
				prevSum =  ActivityTargetReport.createEmptyReport(report.getTarget());
				sumReports.add(prevSum);
				prevReport = report;
			}
			
			prevSum.setTargetValue(prevSum.getTargetValue() 
					+ report.getTargetValue() );
			
			prevSum.getActivityPerformance().setBudgetAllocated(
					prevSum.getActivityPerformance().getBudgetAllocated() 
					+ report.getActivityPerformance().getBudgetAllocated());
			
			prevSum.sumReports(report.getActivityPerformance().getMonthlyBudgetReports(),
					report.getMonthlyReports());
		}
		
		Objective กิจกรรม = null;
		Objective แผนปฏิบัติการ = null;
		Objective กิจกรรมหลัก = null;
		Objective ผลผลิต = null;
		Objective ยุทธศาสตร์ = null;
		Objective แผนงาน = null;
		
		Activity กิจกรรมย่อย = null;
		Activity กิจกรรมเสริม = null;
		Activity กิจกรรมสนับสนุน = null;
		
		
		
		if(sumReports.size() > 0) {
			ActivityTargetReport report = sumReports.get(0);
			กิจกรรม = report.getTarget().getActivity().getForObjective();
			แผนปฏิบัติการ = กิจกรรม.getParent();
			กิจกรรมหลัก = แผนปฏิบัติการ.getParent();
			ผลผลิต = กิจกรรมหลัก.getParent();
			ยุทธศาสตร์ = ผลผลิต.getParent();
			แผนงาน = ยุทธศาสตร์.getParent();
			
			Activity currentActivity = report.getTarget().getActivity();
			if(currentActivity.getParent() == null) {
				กิจกรรมย่อย  = currentActivity;
				กิจกรรมเสริม = null;
				กิจกรรมสนับสนุน = null;
			} else if(currentActivity.getParent().getParent() == null){
				กิจกรรมย่อย = currentActivity.getParent();
				กิจกรรมเสริม = currentActivity;
				กิจกรรมสนับสนุน = null;
			} else {
				กิจกรรมย่อย = currentActivity.getParent().getParent();
				กิจกรรมเสริม = currentActivity.getParent();
				กิจกรรมสนับสนุน = currentActivity;
			}
			
			Row obj1Row = sheet.createRow(rowNum++);
			Cell obj1Name = obj1Row.createCell(0);
			obj1Name.setCellValue(แผนงาน.getName());
			
			obj1Row = sheet.createRow(rowNum++);
			obj1Name = obj1Row.createCell(0);
			obj1Name.setCellValue(ยุทธศาสตร์.getName());
			
			obj1Row = sheet.createRow(rowNum++);
			obj1Name = obj1Row.createCell(0);
			obj1Name.setCellValue(ผลผลิต.getName());
			
			obj1Row = sheet.createRow(rowNum++);
			obj1Name = obj1Row.createCell(0);
			obj1Name.setCellValue(getIndent(1) + กิจกรรมหลัก.getName());
			
			obj1Row = sheet.createRow(rowNum++);
			obj1Name = obj1Row.createCell(0);
			obj1Name.setCellValue(getIndent(2) + แผนปฏิบัติการ.getName());
			
			obj1Row = sheet.createRow(rowNum++);
			obj1Name = obj1Row.createCell(0);
			obj1Name.setCellValue(getIndent(3) + กิจกรรม.getName());
			
			
//			obj1Row = sheet.createRow(rowNum++);
//			obj1Name = obj1Row.createCell(0);
//			obj1Name.setCellValue(getIndent(4) + กิจกรรมย่อย.getName());	
//			
//			if(กิจกรรมเสริม != null) {
//				obj1Row = sheet.createRow(rowNum++);
//				obj1Name = obj1Row.createCell(0);
//				obj1Name.setCellValue(getIndent(5) + กิจกรรมเสริม.getName());
//			}
//			
//			if(กิจกรรมสนับสนุน != null ) {
//				obj1Row = sheet.createRow(rowNum++);
//				obj1Name = obj1Row.createCell(0);
//				obj1Name.setCellValue(getIndent(6) + กิจกรรมสนับสนุน.getName());
//			}
			
		}
		
				
		
		
		for(ActivityTargetReport report : sumReports) {
			
			Activity currentกิจกรรมย่อย;
			Activity currentกิจกรรมเสริม;
			Activity currentกิจกรรมสนับสนุน;
			Integer indentLevel = 4;
			
			Activity currentActivity = report.getTarget().getActivity();
			if(currentActivity.getParent() == null) {
				currentกิจกรรมย่อย  = currentActivity;
				currentกิจกรรมเสริม = null;
				currentกิจกรรมสนับสนุน = null;
				indentLevel = 4;
			} else if(currentActivity.getParent().getParent() == null){
				currentกิจกรรมย่อย = currentActivity.getParent();
				currentกิจกรรมเสริม = currentActivity;
				currentกิจกรรมสนับสนุน = null;
				indentLevel = 5;
			} else {
				currentกิจกรรมย่อย = currentActivity.getParent().getParent();
				currentกิจกรรมเสริม = currentActivity.getParent();
				currentกิจกรรมสนับสนุน = currentActivity;
				indentLevel = 6;
			}
			
			
			Objective currentกิจกรรม = report.getTarget().getActivity().getForObjective();
			Objective currentแผนปฏิบัติการ = currentกิจกรรม.getParent();
			Objective currentกิจกรรมหลัก = currentแผนปฏิบัติการ.getParent();
			Objective currentผลผลิต = currentกิจกรรมหลัก.getParent();
			Objective currentยุทธศาสตร์ = currentผลผลิต.getParent();
			Objective currentแผนงาน = currentยุทธศาสตร์.getParent();

			Row obj1Row;
			Cell obj1Name;
			if(!currentแผนงาน.getId().equals(แผนงาน.getId())) {
				แผนงาน = currentแผนงาน;
				obj1Row= sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(แผนงาน.getName());
			}
			
			if(!currentยุทธศาสตร์.getId().equals(ยุทธศาสตร์.getId())) {
				ยุทธศาสตร์ = currentยุทธศาสตร์;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(ยุทธศาสตร์.getName());
			}
			
			if(!currentผลผลิต.getId().equals(ผลผลิต.getId())) {
				ผลผลิต = currentผลผลิต;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(ผลผลิต.getName());
			}
			
			if(!currentกิจกรรมหลัก.getId().equals(กิจกรรมหลัก.getId())) {
				กิจกรรมหลัก = currentกิจกรรมหลัก;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(getIndent(1) + กิจกรรมหลัก.getName());
			}
		
			if(!currentแผนปฏิบัติการ.getId().equals(แผนปฏิบัติการ.getId())) {
				แผนปฏิบัติการ = currentแผนปฏิบัติการ;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(getIndent(2) + แผนปฏิบัติการ.getName());
			}

			if(!currentกิจกรรม.getId().equals(กิจกรรม.getId())) {
				กิจกรรม = currentกิจกรรม;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(getIndent(3) + กิจกรรม.getName());
			}
			
			if(!currentกิจกรรมย่อย.getId().equals(กิจกรรมย่อย.getId())) {
				กิจกรรมย่อย = currentกิจกรรมย่อย;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(getIndent(4) + กิจกรรมย่อย.getName());
			}
			
			if(currentกิจกรรมเสริม != null && 
					(กิจกรรมเสริม == null || 
						!currentกิจกรรมเสริม.getId().equals(กิจกรรมเสริม.getId()) )) {
				กิจกรรมเสริม = currentกิจกรรมเสริม;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(getIndent(5) + กิจกรรมเสริม.getName());
			}
			
			if(currentกิจกรรมสนับสนุน != null && 
					( กิจกรรมสนับสนุน == null || 
						!currentกิจกรรมสนับสนุน.getId().equals(กิจกรรมสนับสนุน.getId()))) {
				กิจกรรมสนับสนุน = currentกิจกรรมสนับสนุน;
				obj1Row = sheet.createRow(rowNum++);
				obj1Name = obj1Row.createCell(0);
				obj1Name.setCellValue(getIndent(6) + กิจกรรมสนับสนุน.getName());
			}
			
			
			Row dataRow1 = sheet.createRow(rowNum-1);
			Row dataRow2 = sheet.createRow(rowNum);
			Row dataRow3 = sheet.createRow(rowNum+1);
			Row dataRow4 = sheet.createRow(rowNum+2);
			rowNum = rowNum+3;
				
			
			Cell nameCell = dataRow1.createCell(0);
			nameCell.setCellValue(getIndent(indentLevel) + report.getTarget().getActivity().getName());
			
			Cell targetCell = dataRow1.createCell(1);
			targetCell.setCellValue("จำนวน " + decimalFormat.format(report.getTargetValue()) + "  " + report.getTarget().getUnit().getName());
			
			
			Cell budgetCell = dataRow3.createCell(1);
			budgetCell.setCellValue("จัดสรร " + decimalFormat.format(report.getActivityPerformance().getBudgetAllocated()) + "  บาท");
			
			Cell labelCell;
			labelCell = dataRow1.createCell(2);
			labelCell.setCellValue("แผนงาน");
			labelCell = dataRow2.createCell(2);
			labelCell.setCellValue("ผลงาน");
			
			labelCell = dataRow3.createCell(2);
			labelCell.setCellValue("แผนเงิน");
			labelCell = dataRow4.createCell(2);
			labelCell.setCellValue("ผลเงิน");
			
			Double sumActPlan = 0.0;
			Double sumActResult = 0.0;
			Double sumBgtPlan = 0.0;
			Double sumBgtResult = 0.0;
			for (int j=3;j<16;j++) {
				Cell cell1 = dataRow1.createCell(j);
				Cell cell2 = dataRow2.createCell(j);
				Cell cell3 = dataRow3.createCell(j);
				Cell cell4 = dataRow4.createCell(j);
				cell1.setCellStyle(styles.get("cellnumbercenter"));
				cell2.setCellStyle(styles.get("cellnumbercenter"));
				cell3.setCellStyle(styles.get("cellnumbercenter"));
				cell4.setCellStyle(styles.get("cellnumbercenter"));
				
				if(j != 15) {
					MonthlyActivityReport actReport = report.getFiscalReportOn(j-3);
					if(actReport != null) {
						cell1.setCellValue(actReport.getActivityPlan());
						sumActPlan += actReport.getActivityPlan();
						cell2.setCellValue(actReport.getActivityResult());
						sumActResult += actReport.getActivityResult();
					}
					
					MonthlyBudgetReport bgtReport = report.getFiscalBudgetReportOn(j-3);
					if(bgtReport != null) {
						cell3.setCellValue(bgtReport.getBudgetPlan());
						sumBgtPlan += bgtReport.getBudgetPlan();
						cell4.setCellValue(bgtReport.getBudgetResult());
						sumBgtResult += bgtReport.getBudgetResult();
					}
				} else {
					cell1.setCellValue(sumActPlan);
					cell2.setCellValue(sumActResult);
					cell3.setCellValue(sumBgtPlan);
					cell4.setCellValue(sumBgtResult);
				}
			}
			
			
			
			
		}
		
//		
//		Statement st = connection.createStatement();
//		String st01 = "select lpad(' ',(level-4)*5)||m.name name, m.isleaf, m.id, nvl(lpad(' ',(level-3)*5), '     ') space, m.code " +
//				   "from pln_objective m where m.id <> " + root.getId() + " and exists " +
//				   "(select 1 from pln_activitytargetreport t4, pln_activitytarget t5, pln_activity t1, pln_objective t2, " +
//                    " hrx_organization  t3 " +
//                     "where t4.target_pln_acttarget_id = t5.id " +
//                     "and t5.activity_pln_activity_id = t1.id " +
//                     "and t1.obj_pln_objective_id = t2.id " +
//                     "and (t1.OWNER_HRX_ORGANIZATION = t3.id or t1.REGULATOR_HRX_ORGANIZATION = t3.id) " + 
//                     "and '.'||t2.id||t2.parentpath like '%.'||m.id||'.%' " +
//                     "and t2.fiscalyear = " + fiscalYear + ") " +
//				   "connect by prior m.id = m.parent_pln_objective_id " +
//                " start with m.id = " + rootObjectiveId 
//                + " order siblings by m.code asc";
//		ResultSet rs = st.executeQuery(st01);
//
//		int i = 4;
//		int j = 0;
//		int s1 = 0;
//		int s2 = 0;
//		logger.debug("ST01: ");
//		logger.debug(st01);
//		
//		while (rs.next()) {
//			Row rows = sheet.createRow(i);
//			
//			Cell rsc0 = rows.createCell(0);
//			rsc0.setCellValue(rs.getString(1));
//			rsc0.setCellStyle(styles.get("cellleft"));
//			
//			if (rs.getString(5).length() == 7) {
//				Statement st0 = connection.createStatement();
//				ResultSet rs0 = st0.executeQuery("select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(amountallocated),'999,999,999,999')), '...')||' บาท)' " +
//												 "from bgt_budgetproposal " +
//												 "where objective_id = " + rs.getInt(3) + " " +
//												 "and organization_id = " + searchOrg.getId() + " ");
//				
//				Cell rsc1 = rows.createCell(1);
//				if (rs0.next()) {
//					rsc1.setCellValue(rs0.getString(1));
//				}
//				rsc1.setCellStyle(styles.get("cellcenter"));
//				rs0.close();
//				st0.close();
//				
//				Cell rsc2 = rows.createCell(2);
//				rsc2.setCellValue("แผนการใช้เงิน");
//				rsc2.setCellStyle(styles.get("cellcenter"));
//				
//				for (j=3;j<16;j++) {
//					Cell rscj = rows.createCell(j);
//					rscj.setCellStyle(styles.get("cellnumbercenter"));
//				}
//
//				Statement st3 = connection.createStatement();
//				String st03 = "select t1.fiscalmonth, sum(t1.budgetplan) " +
//						 "from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activity t3 " +
//					  	 "where t1.performance_pln_actper_id = t2.id " +
//						 "and t2.activity_pln_activity_id = t3.id " +
//					  	 "and t1.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") " +
//						 "and t3.obj_pln_objective_id in (select id from pln_objective connect by prior id = parent_pln_objective_id start with id = " + rs.getInt(3) + ") " +
//						 "group by t1.fiscalmonth " +
//						 "order by t1.fiscalmonth ";
//				ResultSet rs3 = st3.executeQuery(st03);
//
//				logger.debug(st03);
//				
//				j = 3;
//				s1 = 0;
//				while (rs3.next()) {
//					Cell rscj = rows.getCell(j);
//					rscj.setCellValue(rs3.getInt(2));
//					s1 = s1 + rs3.getInt(2);
//					j = j+1;
//				}
//				rs3.close();
//				st3.close();
//				Cell rsc3 = rows.getCell(15);
//				rsc3.setCellValue(s1);
//
//				rows = sheet.createRow(i+1);
//
//				rsc1 = rows.createCell(1);
//				rsc1.setCellStyle(styles.get("cellcenter"));
//				
//				rsc2 = rows.createCell(2);
//				rsc2.setCellValue("ผลการใช้เงิน");
//				rsc2.setCellStyle(styles.get("cellcenter"));
//				
//				for (j=3;j<16;j++) {
//					Cell rscj = rows.createCell(j);
//					rscj.setCellStyle(styles.get("cellnumbercenter"));
//				}
//
//				Statement st4 = connection.createStatement();
//				ResultSet rs4 = st4.executeQuery("select date2fmonth(gl_trans_docdate) mon, nvl(sum(amt),0) amt " +
//									   "from v_gl " +
//									   "where org_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") " +
//									   "and fiscal_year = " + fiscalYear + " " +
//									   "and gl_trans_plan = '" + rs.getString(5) + "' " +
//									   "group by date2fmonth(gl_trans_docdate) " +
//									   "order by 1 ");
//
//				s1 = 0;
//				while (rs4.next()) {
//					Cell rscj = rows.getCell(rs4.getInt(1)+2);
//					rscj.setCellValue(rs4.getInt(2));
//					s1 = s1 + rs4.getInt(2);
//				}
//				rs4.close();
//				st4.close();
//				rsc3 = rows.getCell(15);
//				rsc3.setCellValue(s1);
//
//				i = i+2;
//				
//			} else {
//				if (rs.getInt(2) == 1) {
//					Statement st0 = connection.createStatement();
//					String st02 = "select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(budgetallocated),'999,999,999,999')), '...')||' บาท)' " +
//							 "from pln_activity t1, pln_activityperformance t3 " +
//							 "where t1.id = t3.activity_pln_activity_id " +
//							 "and t1.id in (select id from PLN_ACTIVITY connect by prior id = PARENT_PLN_ACTIVITY_ID start with OBJ_PLN_OBJECTIVE_ID =" + rs.getInt(3) + ")" +
//							 "and t3.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with parent_hrx_organization_id = " + searchOrg.getId() + ") ";							
//					
//					logger.debug(st02);
//					
//					ResultSet rs0 = st0.executeQuery(st02);
//					
//					
//					
//					Cell rsc1 = rows.createCell(1);
//					if (rs0.next()) {
//						rsc1.setCellValue(rs0.getString(1));
//					}
//					rsc1.setCellStyle(styles.get("cellcenter"));
//					rs0.close();
//					st0.close();
//					
//					Cell rsc2 = rows.createCell(2);
//					rsc2.setCellValue("แผนการใช้เงิน");
//					rsc2.setCellStyle(styles.get("cellcenter"));
//					
//					for (j=3;j<16;j++) {
//						Cell rscj = rows.createCell(j);
//						rscj.setCellStyle(styles.get("cellnumbercenter"));
//					}
//
//					Statement st3 = connection.createStatement();
//					
//					/**
//					 * 
//select t1.fiscalmonth, sum(t1.budgetplan), ltrim(to_char(sum(t1.budgetplan),'999,999,999,999')) 
//from pln_monthlybgtreport t1, pln_activityperformance t2, PLN_ACTIVITY t3
//where t1.performance_pln_actper_id = t2.id 
//  and t2.ACTIVITY_PLN_ACTIVITY_ID = t3.id
//  and t3.id in (select id from PLN_ACTIVITY connect by prior id = PARENT_PLN_ACTIVITY_ID start with OBJ_PLN_OBJECTIVE_ID = 86)
//  and t2.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = 132010000)
//group by t1.fiscalmonth order by t1.fiscalmonth;
//					 * 
//					 * 
//					 */
//					
//					
//					String st03 = "" +
//							"select t1.fiscalmonth, sum(t1.budgetplan), ltrim(to_char(sum(t1.budgetplan),'999,999,999,999')) " +
//							"from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activity t3 " +
//						  	"where t1.performance_pln_actper_id = t2.id " +
//						  	"	and t2.ACTIVITY_PLN_ACTIVITY_ID = t3.id " +
//							"	and t3.id in (select id from PLN_ACTIVITY connect by prior id = PARENT_PLN_ACTIVITY_ID start with OBJ_PLN_OBJECTIVE_ID = " + rs.getInt(3) + ") " +
//							"	and t3.obj_pln_objective_id = " + rs.getInt(3) + 
//							"	and t2.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ")" +
//							"group by t1.fiscalmonth " +
//							"order by t1.fiscalmonth ";
//					logger.debug("XXXXXXX");
//					logger.debug(st03);
//					
//					ResultSet rs3 = st3.executeQuery(st03);
//
//					j = 3;
//					s1 = 0;
//					while (rs3.next()) {
//						Cell rscj = rows.getCell(j);
//						rscj.setCellValue(rs3.getInt(2));
//						s1 = s1 + rs3.getInt(2);
//						j = j+1;
//					}
//					rs3.close();
//					st3.close();
//					Cell rsc3 = rows.getCell(15);
//					rsc3.setCellValue(s1);
//
//					rows = sheet.createRow(i+1);
//
//					rsc1 = rows.createCell(1);
//					rsc1.setCellStyle(styles.get("cellcenter"));
//					
//					rsc2 = rows.createCell(2);
//					rsc2.setCellValue("ผลการใช้เงิน");
//					rsc2.setCellStyle(styles.get("cellcenter"));
//					
//					for (j=3;j<16;j++) {
//						Cell rscj = rows.createCell(j);
//						rscj.setCellStyle(styles.get("cellnumbercenter"));
//					}
//
//					Statement st4 = connection.createStatement();
//					ResultSet rs4 = st4.executeQuery("select date2fmonth(gl_trans_docdate) mon, nvl(sum(amt),0) amt " +
//										   "from v_gl " +
//										   "where org_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = " + currentUser.getWorkAt().getId() + ") " +
//										   "and fiscal_year = " + fiscalYear + " " +
//										   "and activitycode = '" + rs.getInt(5) + "' " +
//										   "group by date2fmonth(gl_trans_docdate) " +
//										   "order by 1 ");
//
//					s1 = 0;
//					while (rs4.next()) {
//						Cell rscj = rows.getCell(rs4.getInt(1)+2);
//						rscj.setCellValue(rs4.getInt(2));
//						s1 = s1 + rs4.getInt(2);
//					}
//					rs4.close();
//					st4.close();
//					rsc3 = rows.getCell(15);
//					rsc3.setCellValue(s1);
//
//					i = i+2;
//					
//					
//					
//					
//					
//					Statement st1 = connection.createStatement();
//					String st1Sql = "select distinct t1.code, t1.name, t1.id, t5.owner_hrx_organization_id, '1' type, t3.id target_id, '   (เป้าหมาย '|| ltrim(to_char(t5.targetvalue,'999,999,999,999'))||' '||t4.name||')' target " +
//							 "from pln_activitytargetreport t5, pln_activity t1, pln_activitytarget t3, pln_targetunit t4, s_user t2 " +
//	 						 "where t5.target_pln_acttarget_id = t3.id " +
//							 "	and t5.owner_hrx_organization_id = t2.dept_id " +
//							 "	and t1.id = t3.activity_pln_activity_id " +
//							 "	and t3.unit_pln_targetunit_id = t4.id " +
//							 "	and t1.obj_pln_objective_id = " + rs.getInt(3) +
//							 "	and t2.login = '" + currentUser.getUsername() + "' " +
//							 "order by 3, 5 ";
//					logger.debug("YYYYYYY");
//					logger.debug(st1Sql);
//					
//					ResultSet rs1 = st1.executeQuery(st1Sql);
//					
//					
//					int actId = 0;
//					while (rs1.next()) {
//						Long targetId=rs1.getLong("target_id");
//						
//						Row rows1 = sheet.createRow(i);
//						Cell rsc11 = rows1.createCell(0);
//						if (rs1.getInt(3)!=actId) {
//							rsc11.setCellValue(rs.getString(4)+rs1.getString(2));
//							actId = rs1.getInt(3);
//							rsc11.setCellStyle(styles.get("cellleft"));
//						}
//						
//						// here we have to do แผนการใช้เงิน/ผลการใช้เงิน
//						// now แผน/ผลการใช้เงินของกิจกรรม
//							
//						Cell rsc013 = rows1.createCell(2);
//						rsc013.setCellValue("แผนการใช้เงิน");
//						rsc013.setCellStyle(styles.get("cellcenter"));
//						
//						for (j=3;j<16;j++) {
//							Cell rscj01 = rows1.createCell(j);
//							rscj01.setCellStyle(styles.get("cellnumbercenter"));
//
//						}
//
//						Row rows02 = sheet.createRow(i+1);
//						Cell rsc021 = rows02.createCell(0);
//						rsc021.setCellStyle(styles.get("cellleft"));
//						
//						Cell rsc022 = rows02.createCell(1);
//						rsc022.setCellStyle(styles.get("cellcenter"));
//						
//						Cell rsc023 = rows02.createCell(2);
//						rsc023.setCellValue("ผลการใช้เงิน");
//						rsc023.setCellStyle(styles.get("cellcenter"));
//						
//						for (j=3;j<16;j++) {
//							Cell rscj02 = rows02.createCell(j);
//							rscj02.setCellStyle(styles.get("cellnumbercenter"));
//
//						}
//						
//						String st05 = "select t1.fiscalmonth, nvl(sum(t1.budgetplan),0), nvl(sum(t1.BUDGETRESULT),0) " 
//								+ "from pln_monthlybgtreport t1, pln_activityperformance t2, PLN_ACTIVITYTARGETREPORT t3," +
//								"		pln_activitytarget t4 "
//								+ "where t1.performance_pln_actper_id = t2.id "
//								+ " 	and t2.id = t3.performance_pln_actper_id " 
//								+ "		and t3.target_pln_acttarget_id = t4.id "	
//								+ "  	and t4.id = " + targetId
//								+ " 	and t2.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = "+ searchOrg.getId() +") " 
//								+ "group by t1.fiscalmonth order by t1.fiscalmonth";
//						Statement st5 = connection.createStatement();
//						ResultSet rs5 = st5.executeQuery(st05);
//						j = 3;
//						s1 = 0;
//						s2 = 0;
//						while(rs5.next()) {
//							Cell rscj1 = rows1.getCell(j);
//							rscj1.setCellValue(rs5.getInt(2));
//							Cell rscj2 = rows02.getCell(j);
//							rscj2.setCellValue(rs5.getInt(3));
//							
//							s1 = s1 + rs5.getInt(2);
//							s2 = s2 + rs5.getInt(3);
//							j = j+1;
//						}
//						Cell rscs1 = rows1.getCell(15);
//						rscs1.setCellValue(s1);
//						Cell rscs2 = rows02.getCell(15);
//						rscs2.setCellValue(s2);
//						
//						i = i+2;
//						
//						rows1 = sheet.createRow(i);
//						
//						
//						Cell rsc12 = rows1.createCell(1);
//						rsc12.setCellValue(rs1.getString(7));
//						rsc12.setCellStyle(styles.get("cellcenter"));
//
//						Cell rsc13 = rows1.createCell(2);
//						rsc13.setCellValue("แผนงาน");
//						rsc13.setCellStyle(styles.get("cellcenter"));
//						
//						for (j=3;j<16;j++) {
//							Cell rscj = rows1.createCell(j);
//							rscj.setCellStyle(styles.get("cellnumbercenter"));
//
//						}
//
//						Row rows2 = sheet.createRow(i+1);
//						Cell rsc21 = rows2.createCell(0);
//						rsc21.setCellStyle(styles.get("cellleft"));
//						
//						Cell rsc22 = rows2.createCell(1);
//						rsc22.setCellStyle(styles.get("cellcenter"));
//						
//						Cell rsc23 = rows2.createCell(2);
//						rsc23.setCellValue("ผลงาน");
//						rsc23.setCellStyle(styles.get("cellcenter"));
//						
//						for (j=3;j<16;j++) {
//							Cell rscj = rows2.createCell(j);
//							rscj.setCellStyle(styles.get("cellnumbercenter"));
//
//						}
//						
//						Statement st2 = connection.createStatement();
//						ResultSet rs2;
//						rs2 = st2.executeQuery("select t1.fiscalmonth, sum(t1.activityplan), sum(t1.activityresult) " +
//
//								 "from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3, " +
//								     "(select id from hrx_organization " +
//								        "connect by prior id = parent_hrx_organization_id " +
//								        "start with id = (select dept_id from s_user where login = '" + currentUser.getUsername() + "')) t4 " +
//								 "where t1.report_pln_acttargetreport_id = t2.id " +
//							     "and t2.target_pln_acttarget_id = t3.id " +
//								 "and t1.owner_hrx_organization_id = t4.id " +
//								 "and t3.activity_pln_activity_id = " + rs1.getInt(3) + 
//								 " and t3.id = " + rs1.getInt(6) +
//								 " group by t1.fiscalmonth order by t1.fiscalmonth ");
//
//						
//						
//						j = 3;
//						s1 = 0;
//						s2 = 0;
//						while (rs2.next()) {
//							Cell rscj1 = rows1.getCell(j);
//							rscj1.setCellValue(rs2.getInt(2));
//							Cell rscj2 = rows2.getCell(j);
//							rscj2.setCellValue(rs2.getInt(3));
//							s1 = s1 + rs2.getInt(2);
//							s2 = s2 + rs2.getInt(3);
//							j = j+1;
//						}
//						rs2.close();
//						st2.close();
//						Cell rscs11 = rows1.getCell(15);
//						rscs11.setCellValue(s1);
//						Cell rscs22 = rows2.getCell(15);
//						rscs22.setCellValue(s2);
//						
//						i = i+2;
//					}
//					rs1.close();
//					st1.close();
//				}
//				else {
//					for (j=1;j<16;j++) {
//						Cell rscj = rows.createCell(j);
//						rscj.setCellStyle(styles.get("cellleft"));
//
//					}
//					i = i+1;
//				}
//			}
//		}
//		
//		Row rowE = sheet.createRow(i);
//		Cell re = rowE.createCell(0);
//		re.setCellStyle(styles.get("celltop"));
//		
//		rs.close();
//		st.close();
//		connection.close();

		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 3000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 3000);
		sheet.setColumnWidth(10, 3000);
		sheet.setColumnWidth(11, 3000);
		sheet.setColumnWidth(12, 3000);
		sheet.setColumnWidth(13, 3000);
		sheet.setColumnWidth(14, 3000);
		sheet.setColumnWidth(15, 3000);
		sheet.createFreezePane( 3, 4 );
	}
	
	
    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        short borderColor = IndexedColors.GREY_50_PERCENT.getIndex();

        CellStyle style;
        DataFormat format = wb.createDataFormat();
        Font titleFont = wb.createFont();
        titleFont.setFontName("Tahoma");
        titleFont.setFontHeightInPoints((short)12);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font headFont = wb.createFont();
        headFont.setFontName("Tahoma");
        headFont.setFontHeightInPoints((short)11);
        headFont.setColor(IndexedColors.WHITE.getIndex());
        headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headFont);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(true);
        styles.put("header", style);

        Font groupFont = wb.createFont();
        groupFont.setFontName("Tahoma");
        groupFont.setFontHeightInPoints((short)11);
        groupFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setFont(groupFont);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("groupleft", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setFont(groupFont);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("groupright", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0"));
        style.setFont(groupFont);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("groupnumber", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
/*        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
*/        styles.put("cellleft", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellcenter", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellright", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0"));
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellnumber", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0"));
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellnumbercenter", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("celltop", style);

        return styles;
    }

}
