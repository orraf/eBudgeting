package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R02XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
	
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

        
		List<Objective> objectiveList = (List<Objective>) model.get("objectiveList");
		Integer fiscalYear = (Integer) model.get("fiscalYear");
		Sheet sheet = workbook.createSheet("sheet1");
		Integer oldYear = fiscalYear - 1;

		Row firstRow = sheet.createRow(0);
		Cell cell11 = firstRow.createCell(0);
		cell11.setCellValue("แผนปฏิบัติการประจำปีงบประมาณ " + fiscalYear);
		cell11.setCellStyle(styles.get("title"));

/*		Row subFirstRow = sheet.createRow(1);
		Cell subCell11 = subFirstRow.createCell(0);
		subCell11.setCellValue("ผู้จัดทำรายงาน " + 
				currentUser.getPerson().getFirstName() + " " +	currentUser.getPerson().getLastName() + 
				" เวลาที่จัดทำรายงาน " +  sdf.format(new Date()) + "น.");
*/		
		Row secondRow = sheet.createRow(1);
		Cell cell21 = secondRow.createCell(0);
		cell21.setCellValue("หน่วยงาน " + currentUser.getWorkAt().getName());
		cell21.setCellStyle(styles.get("title"));
		
		
		Row thirdRow = sheet.createRow(3);
		Cell cell301 = thirdRow.createCell(0);
		cell301.setCellValue("แผนงาน/ผลผลิต/โครงการ/กิจกรรม");
		cell301.setCellStyle(styles.get("header"));
		Cell cell302 = thirdRow.createCell(1);
		cell302.setCellValue("แผน/ผลการดำเนินงาน");
		cell302.setCellStyle(styles.get("header"));
		Cell cell303 = thirdRow.createCell(2);
		cell303.setCellValue("ตค." + oldYear.toString().substring(2, 4) );
		cell303.setCellStyle(styles.get("header"));
		Cell cell304 = thirdRow.createCell(3);
		cell304.setCellValue("พย." + oldYear.toString().substring(2, 4) );
		cell304.setCellStyle(styles.get("header"));
		Cell cell305 = thirdRow.createCell(4);
		cell305.setCellValue("ธค." + oldYear.toString().substring(2, 4) );
		cell305.setCellStyle(styles.get("header"));
		Cell cell306 = thirdRow.createCell(5);
		cell306.setCellValue("มค." + fiscalYear.toString().substring(2, 4) );
		cell306.setCellStyle(styles.get("header"));
		Cell cell307 = thirdRow.createCell(6);
		cell307.setCellValue("กพ." + fiscalYear.toString().substring(2, 4) );
		cell307.setCellStyle(styles.get("header"));
		Cell cell308 = thirdRow.createCell(7);
		cell308.setCellValue("มีค." + fiscalYear.toString().substring(2, 4) );
		cell308.setCellStyle(styles.get("header"));
		Cell cell309 = thirdRow.createCell(8);
		cell309.setCellValue("เมย." + fiscalYear.toString().substring(2, 4) );
		cell309.setCellStyle(styles.get("header"));
		Cell cell310 = thirdRow.createCell(9);
		cell310.setCellValue("พค." + fiscalYear.toString().substring(2, 4) );
		cell310.setCellStyle(styles.get("header"));
		Cell cell311 = thirdRow.createCell(10);
		cell311.setCellValue("มิย." + fiscalYear.toString().substring(2, 4) );
		cell311.setCellStyle(styles.get("header"));
		Cell cell312 = thirdRow.createCell(11);
		cell312.setCellValue("กค." + fiscalYear.toString().substring(2, 4) );
		cell312.setCellStyle(styles.get("header"));
		Cell cell313 = thirdRow.createCell(12);
		cell313.setCellValue("สค." + fiscalYear.toString().substring(2, 4) );
		cell313.setCellStyle(styles.get("header"));
		Cell cell314 = thirdRow.createCell(13);
		cell314.setCellValue("กย." + fiscalYear.toString().substring(2, 4) );
		cell314.setCellStyle(styles.get("header"));
		Cell cell315 = thirdRow.createCell(14);
		cell315.setCellValue("รวม");
		cell315.setCellStyle(styles.get("header"));
		sheet.createFreezePane( 1, 4 );

		Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@www.innova.or.th:1521:xe", "afrpmt", "afrpmt");
		PreparedStatement ps = null;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("select lpad(' ',(level-4)*5)||m.name name, m.isleaf, m.id, nvl(lpad(' ',(level-3)*5), '     ') space " +
									   "from pln_objective m where m.id <> 21 and exists " +
									   "(select 1 from pln_activitytargetreport t4, pln_activitytarget t5, pln_activity t1, pln_objective t2, " +
					                       "(select id from hrx_organization " +
					                           "connect by prior id = parent_hrx_organization_id " +
					                           "start with id = (select dept_id from s_user where login = '" + currentUser.getUsername() + "')) t3 " +
					                        "where t4.target_pln_acttarget_id = t5.id " +
					                        "and t5.activity_pln_activity_id = t1.id " +
					                        "and t1.obj_pln_objective_id = t2.id " +
					                        "and t4.owner_hrx_organization_id = t3.id " + 
					                        "and '.'||t2.id||t2.parentpath like '%.'||m.id||'.%' " +
					                        "and t2.fiscalyear = " + fiscalYear + ") " +
									   "connect by prior m.id = m.parent_pln_objective_id " +
					                   "start with m.id = 21 ");

		int i = 4;
		int j = 0;
		int s1 = 0;
		int s2 = 0;
		while (rs.next()) {
			Row rows = sheet.createRow(i);
			
			Cell rsc0 = rows.createCell(0);
			rsc0.setCellValue(rs.getString(1));
			rsc0.setCellStyle(styles.get("cellleft"));
			
			if (rs.getInt(2) == 1) {
				for (j=1;j<15;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellleft"));

				}
				i = i+1;
				Statement st1 = connection.createStatement();
				ResultSet rs1 = st1.executeQuery("select distinct t1.code, t1.name, t1.id, t5.owner_hrx_organization_id, '1' type, t3.id target_id, '   (เป้าหมาย '|| t5.targetvalue||' '||t4.name||')' target " +
												 "from pln_activitytargetreport t5, pln_activity t1, pln_activitytarget t3, pln_targetunit t4, s_user t2 " +
						 						 "where t5.target_pln_acttarget_id = t3.id " +
												 "and t5.owner_hrx_organization_id = t2.dept_id " +
												 "and t1.id = t3.activity_pln_activity_id " +
												 "and t3.unit_pln_targetunit_id = t4.id " +
												 "and t1.obj_pln_objective_id = " + rs.getInt(3) +
												 "and t2.login = '" + currentUser.getUsername() + "' " +
												 "union all " +
												 "select t1.code, t1.name, t1.id, t3.owner_hrx_organization_id, '2', null, '   (จัดสรรเงิน '||nvl(to_char(sum(budgetallocated)), '...')||' บาท)' " +
												 "from pln_activityperformance t3, pln_activity t1, s_user t2 " +
												 "where t3.owner_hrx_organization_id = t2.dept_id  " +
												 "and t1.id = t3.activity_pln_activity_id " +
												 "and t1.obj_pln_objective_id = " + rs.getInt(3) +
												 "and t2.login = '" + currentUser.getUsername() + "' " +
												 "group by t1.code, t1.name, t1.id, t3.owner_hrx_organization_id " +
												 "order by 3, 5 ");
				int actId = 0;
				while (rs1.next()) {
					Row rows1 = sheet.createRow(i);
					Cell rsc11 = rows1.createCell(0);
					if (rs1.getInt(3)!=actId) {
						rsc11.setCellValue(rs.getString(4)+rs1.getString(2));
						actId = rs1.getInt(3);
					}
					rsc11.setCellStyle(styles.get("cellleft"));
					Cell rsc12 = rows1.createCell(1);
					if (rs1.getString(5).equals("1")) rsc12.setCellValue("แผนงาน");
					else rsc12.setCellValue("แผนการใช้เงิน");
					rsc12.setCellStyle(styles.get("cellcenter"));
					for (j=2;j<15;j++) {
						Cell rscj = rows1.createCell(j);
						rscj.setCellStyle(styles.get("cellcenter"));

					}

					Row rows2 = sheet.createRow(i+1);
					Cell rsc21 = rows2.createCell(0);
					rsc21.setCellValue(rs.getString(4)+rs1.getString(7));
					rsc21.setCellStyle(styles.get("cellleft"));
					Cell rsc22 = rows2.createCell(1);
					if (rs1.getString(5).equals("1")) rsc22.setCellValue("ผลงาน");
					else rsc22.setCellValue("ผลการใช้เงิน");
					rsc22.setCellStyle(styles.get("cellcenter"));
					for (j=2;j<15;j++) {
						Cell rscj = rows2.createCell(j);
						rscj.setCellStyle(styles.get("cellcenter"));

					}
					
					Statement st2 = connection.createStatement();
					ResultSet rs2;
					j = 2;
					s1 = 0;
					s2 = 0;
					if (rs1.getString(5).equals("1")) {
						rs2 = st2.executeQuery("select t1.fiscalmonth, sum(t1.activityplan), sum(t1.activityresult) " +
								 "from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3, " +
							     "(select id from hrx_organization " +
							        "connect by prior id = parent_hrx_organization_id " +
							        "start with id = (select dept_id from s_user where login = '" + currentUser.getUsername() + "')) t4 " +
								 "where t1.report_pln_acttargetreport_id = t2.id " +
							     "and t2.target_pln_acttarget_id = t3.id " +
								 "and t1.owner_hrx_organization_id = t4.id " +
								 "and t3.activity_pln_activity_id = " + rs1.getInt(3) + 
								 " and t3.id = " + rs1.getInt(6) +
								 " group by t1.fiscalmonth order by t1.fiscalmonth ");
						
					}
					else {
						rs2 = st2.executeQuery("select t1.fiscalmonth, sum(t1.budgetplan), sum(t1.budgetresult) " +
								 "from pln_monthlybgtreport t1, pln_activityperformance t2, " +
							         "(select id from hrx_organization " +
							            "connect by prior id = parent_hrx_organization_id " +
							            "start with id = (select dept_id from s_user where login = '" + currentUser.getUsername() + "')) t3 " +
								 "where t1.performance_pln_actper_id = t2.id " +
								 "and t1.owner_hrx_organization_id = t3.id " +
								 "and t2.activity_pln_activity_id = " + rs1.getInt(3) + 
								 " group by t1.fiscalmonth order by t1.fiscalmonth ");
					}

					while (rs2.next()) {
						Cell rscj1 = rows1.getCell(j);
						rscj1.setCellValue(rs2.getInt(2));
						Cell rscj2 = rows2.getCell(j);
						rscj2.setCellValue(rs2.getInt(3));
						s1 = s1 + rs2.getInt(2);
						s2 = s2 + rs2.getInt(3);
						j = j+1;
					}
					rs2.close();
					Cell rscs1 = rows1.getCell(j);
					rscs1.setCellValue(s1);
					Cell rscs2 = rows2.getCell(j);
					rscs2.setCellValue(s2);
					
					i = i+2;
				}
				rs1.close();
				
			}
			else {
				for (j=1;j<15;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellleft"));

				}
				i = i+1;
			}
		}
		rs.close();
		connection.close();

		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 3000);
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

        return styles;
    }

}
