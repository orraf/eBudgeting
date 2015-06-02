package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R04XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
	
	public static Logger logger = LoggerFactory.getLogger(M81R04XLSView.class);
	
	@Override
	protected Workbook createWorkbook() {
		return new HSSFWorkbook();
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ThaicomUserDetail currentUser = (ThaicomUserDetail) model.get("currentUser");
		
        Map<String, CellStyle> styles = createStyles(workbook);

		Integer fiscalYear = (Integer) model.get("fiscalYear");
		Activity activity = (Activity) model.get("activity");
				
		Sheet sheet = workbook.createSheet("sheet1");
		Integer oldYear = fiscalYear - 1;

		Row firstRow = sheet.createRow(0);
		Cell cell0 = firstRow.createCell(0);
		cell0.setCellValue("วันที่พิมพ์รายงาน: " +  printTimeFormat.format(new Date()) );
		
		firstRow = sheet.createRow(1);
		Cell cell11 = firstRow.createCell(0);
		cell11.setCellValue("ตรวจสอบแผนปฏิบัติการของ" + activity.getName() +" ประจำปีงบประมาณ " + fiscalYear);
		cell11.setCellStyle(styles.get("title"));

/*		Row subFirstRow = sheet.createRow(1);
		Cell subCell11 = subFirstRow.createCell(0);
		subCell11.setCellValue("ผู้จัดทำรายงาน " + 
				currentUser.getPerson().getFirstName() + " " +	currentUser.getPerson().getLastName() + 
				" เวลาที่จัดทำรายงาน " +  sdf.format(new Date()) + "น.");
		Row secondRow = sheet.createRow(1);
		Cell cell21 = secondRow.createCell(0);
		cell21.setCellValue("หน่วยงาน  " + currentUser.getWorkAt().getName());
		cell21.setCellStyle(styles.get("title"));
*/		
		
		
		Row thirdRow = sheet.createRow(2);
		Cell cell301 = thirdRow.createCell(0);
		cell301.setCellValue("หน่วยงาน");
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

		
		Connection connection = dataSource.getConnection();
		Organization searchOrg;
		logger.debug(currentUser.getWorkAt().getId() + " : " + activity.getOwner().getId());
		if(currentUser.getWorkAt().getId().equals(activity.getOwner().getId()) || 
				currentUser.getWorkAt().getId().equals(activity.getRegulator().getId())){
			searchOrg = new Organization();
			searchOrg.setId(0L);
		} else {
			searchOrg = currentUser.getWorkAt();
			if(searchOrg.getType() == OrganizationType.แผนก) {
				searchOrg = searchOrg.getParent();
			}
		}
		
		int i = 3;
		int j = 0;
		int s1 = 0;
		int s2 = 0;
		int orgId = 0;

		PreparedStatement ps = null;
		Statement st = connection.createStatement();
		String sql = "select t4.id, t4.name, '1' type, t3.id target_id, '   (เป้าหมาย '|| ltrim(to_char(t1.targetvalue,'999,999,999,999'))||' '||t3.name||')' target " +
				   "from pln_activitytargetreport t1, pln_activitytarget t2, pln_targetunit t3, hrx_organization t4 " +
				   "where t1.target_pln_acttarget_id = t2.id " +
					"and t1.owner_hrx_organization_id = t4.id " +
					"and t2.unit_pln_targetunit_id = t3.id " +
					"and t2.activity_pln_activity_id = " + activity.getId() +
					" and t4.parent_hrx_organization_id =  "  + searchOrg.getId() +
					" order by 1, 4 ";
		
		ResultSet rs = st.executeQuery(sql);

		logger.debug(sql);
		
										
/*										"union all " +
										"select t6.id, t6.name, '2' type, null, '   (จัดสรรเงิน '||nvl(ltrim(to_char(budgetallocated,'999,999,999,999')), '...')||' บาท)' " +
										"from pln_activityperformance t5, hrx_organization t6 " +
										"where t5.owner_hrx_organization_id = t6.id " +
										"and t5.activity_pln_activity_id = " + activity.getId() +
										" and t6.parent_hrx_organization_id = 0 " +
*/
		while (rs.next()) {
			Row rows = sheet.createRow(i);
			
			Cell rsc10 = rows.createCell(0);
			rsc10.setCellStyle(styles.get("cellleft"));
			if (rs.getInt(1)!=orgId) {
				rsc10.setCellValue(rs.getString(2));
				orgId = rs.getInt(1);
			}

			Cell rsc11 = rows.createCell(1);
			rsc11.setCellStyle(styles.get("cellcenter"));

			Cell rsc12 = rows.createCell(2);
			rsc12.setCellValue("แผนการใช้เงิน");
			rsc12.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Row rows2 = sheet.createRow(i+1);
			Cell rsc21 = rows2.createCell(0);
			rsc21.setCellStyle(styles.get("cellleft"));
			Cell rsc22 = rows2.createCell(1);
			rsc22.setCellStyle(styles.get("cellcenter"));
			Cell rsc23 = rows2.createCell(2);
			rsc23.setCellValue("ผลการใช้เงิน");
			rsc23.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows2.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Statement st2 = connection.createStatement();
			ResultSet rs2;
			String rs2SQL = "select t1.fiscalmonth, nvl(sum(t1.budgetplan),0), nvl(sum(t1.budgetresult),0) " +
						   "from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activitytargetreport t3, pln_activitytarget t4 " +
					  	   "where t1.performance_pln_actper_id = t2.id " +
					  	   "and t2.id = t3.performance_pln_actper_id " +
					  	   "and t3.target_pln_acttarget_id = t4.id " +	
						   "and t2.activity_pln_activity_id = " + activity.getId() + 
						   "and t4.unit_pln_targetunit_id = " + rs.getInt(4) +
						   " and t2.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = " + rs.getInt(1) + ") " +
						   " group by t1.fiscalmonth order by t1.fiscalmonth ";
			rs2 = st2.executeQuery(rs2SQL);
				
			j = 3;
			s1 = 0;
			s2 = 0;
			while (rs2.next()) {
				Cell rscj1 = rows.getCell(j);
				rscj1.setCellValue(rs2.getInt(2));
				Cell rscj2 = rows2.getCell(j);
				rscj2.setCellValue(rs2.getInt(3));
				s1 = s1 + rs2.getInt(2);
				s2 = s2 + rs2.getInt(3);
				j = j+1;
			}
			rs2.close();
			Cell rscs1 = rows.getCell(15);
			rscs1.setCellValue(s1);
			Cell rscs2 = rows2.getCell(15);
			rscs2.setCellValue(s2);
			
			Row rows3 = sheet.createRow(i+2);
			Cell rsc31 = rows3.createCell(0);
			rsc31.setCellStyle(styles.get("cellleft"));
			Cell rsc32 = rows3.createCell(1);
			rsc32.setCellValue(rs.getString(5));
			rsc32.setCellStyle(styles.get("cellcenter"));
			Cell rsc33 = rows3.createCell(2);
			rsc33.setCellValue("แผนงาน");
			rsc33.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows3.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Row rows4 = sheet.createRow(i+3);
			Cell rsc41 = rows4.createCell(0);
			rsc41.setCellStyle(styles.get("cellleft"));
			Cell rsc42 = rows4.createCell(1);
			rsc42.setCellStyle(styles.get("cellcenter"));
			Cell rsc43 = rows4.createCell(2);
			rsc43.setCellValue("ผลงาน");
			rsc43.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows4.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Statement st3 = connection.createStatement();
			ResultSet rs3;
			String rs3SQL = "select t1.fiscalmonth, sum(t1.activityplan), sum(t1.activityresult) " +
					"from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3, " +
					"(select id from hrx_organization " +
					   "connect by prior id = parent_hrx_organization_id " +
					   "start with id = " + rs.getInt(1) + ") t4 " +
					"where t1.report_pln_acttargetreport_id = t2.id " +
					"and t2.target_pln_acttarget_id = t3.id " +
					"and t2.owner_hrx_organization_id = t4.id " + 
					"and t3.activity_pln_activity_id = " + activity.getId() +
					" and t3.unit_pln_targetunit_id = " + rs.getInt(4) +
					" group by t1.fiscalmonth order by t1.fiscalmonth ";
			rs3 = st3.executeQuery(rs3SQL);
		
			j = 3;
			s1 = 0;
			s2 = 0;
			while (rs3.next()) {
				Cell rscj1 = rows3.getCell(j);
				rscj1.setCellValue(rs3.getInt(2));
				Cell rscj2 = rows4.getCell(j);
				rscj2.setCellValue(rs3.getInt(3));
				s1 = s1 + rs3.getInt(2);
				s2 = s2 + rs3.getInt(3);
				j = j+1;
			}
			rs3.close();
			Cell rscs3 = rows3.getCell(15);
			rscs3.setCellValue(s1);
			Cell rscs4 = rows4.getCell(15);
			rscs4.setCellValue(s2);

			i = i+4;
			
		}

		Statement st4 = connection.createStatement();
		ResultSet rs4;
		String rs4SQL = "select t3.id target_id, '   (เป้าหมาย '|| ltrim(to_char(sum(t1.targetvalue),'999,999,999,999'))||' '||t3.name||')' target " +
					    "from pln_activitytargetreport t1, pln_activitytarget t2, pln_targetunit t3, hrx_organization t4 " +
					    "where t1.target_pln_acttarget_id = t2.id " +
						"and t1.owner_hrx_organization_id = t4.id " +
						"and t2.unit_pln_targetunit_id = t3.id " +
						"and t2.activity_pln_activity_id = " + activity.getId() +
						" and t4.parent_hrx_organization_id =  "  + searchOrg.getId() +
						"group by t3.id, t3.name ";
		
		rs4 = st4.executeQuery(rs4SQL);
		while (rs4.next()) {
			Row rows5 = sheet.createRow(i);
			Cell rsc50 = rows5.createCell(0);
			if (rs4.isFirst()) {
				rsc50.setCellValue("ยอดรวม");
				rsc50.setCellStyle(styles.get("celltop"));

			} else {
				rsc50.setCellStyle(styles.get("cellleft"));
			}

			Cell rsc52 = rows5.createCell(2);
			rsc52.setCellValue("แผนการใช้เงิน");
			rsc52.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows5.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Row rows6 = sheet.createRow(i+1);
			Cell rsc61 = rows6.createCell(0);
			rsc61.setCellStyle(styles.get("cellleft"));
			Cell rsc62 = rows6.createCell(1);
			rsc62.setCellStyle(styles.get("cellcenter"));
			Cell rsc63 = rows6.createCell(2);
			rsc63.setCellValue("ผลการใช้เงิน");
			rsc63.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows6.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Statement st5 = connection.createStatement();
			ResultSet rs5;
			String rs5SQL = "select t1.fiscalmonth, nvl(sum(t1.budgetplan),0), nvl(sum(t1.budgetresult),0) " +
						    "from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activitytargetreport t3, pln_activitytarget t4 " +
					  	    "where t1.performance_pln_actper_id = t2.id " +
					  	    "and t2.id = t3.performance_pln_actper_id " +
					  	    "and t3.target_pln_acttarget_id = t4.id " +	
						    "and t2.activity_pln_activity_id = " + activity.getId() + 
						    "and t4.unit_pln_targetunit_id = " + rs4.getInt(1) +
						    " group by t1.fiscalmonth order by t1.fiscalmonth ";
			rs5 = st5.executeQuery(rs5SQL);
				
			j = 3;
			s1 = 0;
			s2 = 0;
			while (rs5.next()) {
				Cell rscj1 = rows5.getCell(j);
				rscj1.setCellValue(rs5.getInt(2));
				Cell rscj2 = rows6.getCell(j);
				rscj2.setCellValue(rs5.getInt(3));
				s1 = s1 + rs5.getInt(2);
				s2 = s2 + rs5.getInt(3);
				j = j+1;
			}
			rs5.close();
			Cell rscs1 = rows5.getCell(15);
			rscs1.setCellValue(s1);
			Cell rscs2 = rows6.getCell(15);
			rscs2.setCellValue(s2);

			Row rows3 = sheet.createRow(i+2);
			Cell rsc51 = rows5.createCell(1);
			rsc51.setCellStyle(styles.get("cellcenter"));

			Cell rsc31 = rows3.createCell(0);
			rsc31.setCellStyle(styles.get("cellleft"));
			Cell rsc32 = rows3.createCell(1);
			rsc32.setCellValue(rs4.getString(2));
			rsc32.setCellStyle(styles.get("cellcenter"));
			Cell rsc33 = rows3.createCell(2);
			rsc33.setCellValue("แผนงาน");
			rsc33.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows3.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Row rows4 = sheet.createRow(i+3);
			Cell rsc41 = rows4.createCell(0);
			rsc41.setCellStyle(styles.get("cellleft"));
			Cell rsc42 = rows4.createCell(1);
			rsc42.setCellStyle(styles.get("cellcenter"));
			Cell rsc43 = rows4.createCell(2);
			rsc43.setCellValue("ผลงาน");
			rsc43.setCellStyle(styles.get("cellcenter"));

			for (j=3;j<16;j++) {
				Cell rscj = rows4.createCell(j);
				rscj.setCellStyle(styles.get("cellnumber"));

			}

			Statement st3 = connection.createStatement();
			ResultSet rs3;
			String rs3SQL = "select t1.fiscalmonth, sum(t1.activityplan), sum(t1.activityresult) " +
					"from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3 " +
					"where t1.report_pln_acttargetreport_id = t2.id " +
					"and t2.target_pln_acttarget_id = t3.id " +
					"and t3.activity_pln_activity_id = " + activity.getId() +
					" and t3.unit_pln_targetunit_id = " + rs4.getInt(1) +
					" group by t1.fiscalmonth order by t1.fiscalmonth ";
			rs3 = st3.executeQuery(rs3SQL);
		
			j = 3;
			s1 = 0;
			s2 = 0;
			while (rs3.next()) {
				Cell rscj1 = rows3.getCell(j);
				rscj1.setCellValue(rs3.getInt(2));
				Cell rscj2 = rows4.getCell(j);
				rscj2.setCellValue(rs3.getInt(3));
				s1 = s1 + rs3.getInt(2);
				s2 = s2 + rs3.getInt(3);
				j = j+1;
			}
			rs3.close();
			Cell rscs3 = rows3.getCell(15);
			rscs3.setCellValue(s1);
			Cell rscs4 = rows4.getCell(15);
			rscs4.setCellValue(s2);

			i = i+4;
		}
		rs4.close();
		
		Row rowE = sheet.createRow(i);
		Cell re = rowE.createCell(0);
		re.setCellStyle(styles.get("celltop"));
		
		rs.close();
		connection.close();

		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 8000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 3500);
		sheet.setColumnWidth(4, 3500);
		sheet.setColumnWidth(5, 3500);
		sheet.setColumnWidth(6, 3500);
		sheet.setColumnWidth(7, 3500);
		sheet.setColumnWidth(8, 3500);
		sheet.setColumnWidth(9, 3500);
		sheet.setColumnWidth(10, 3500);
		sheet.setColumnWidth(11, 3500);
		sheet.setColumnWidth(12, 3500);
		sheet.setColumnWidth(13, 3500);
		sheet.setColumnWidth(14, 3500);
		sheet.setColumnWidth(14, 3500);
		sheet.setColumnWidth(15, 3500);
		sheet.createFreezePane( 3, 3 );
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
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
      /*  style.setFont(groupFont);  */
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("groupcenter", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0"));
      /*  style.setFont(groupFont); */
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
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("celltop", style);

        return styles;
    }

}
