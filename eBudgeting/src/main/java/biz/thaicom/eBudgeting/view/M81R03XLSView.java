package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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

import biz.thaicom.eBudgeting.controllers.ExcelReportsController;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R03XLSView extends AbstractPOIExcelView {

	private static final Logger logger = LoggerFactory.getLogger(M81R03XLSView.class);
	
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

        Objective root = (Objective) model.get("root");
        
		//List<Objective> objectiveList = (List<Objective>) model.get("objectiveList");
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

/*		Row subFirstRow = sheet.createRow(1);
		Cell subCell11 = subFirstRow.createCell(0);
		subCell11.setCellValue("ผู้จัดทำรายงาน " + 
				currentUser.getPerson().getFirstName() + " " +	currentUser.getPerson().getLastName() + 
				" เวลาที่จัดทำรายงาน " +  sdf.format(new Date()) + "น.");
*/		
		Row secondRow = sheet.createRow(2);
		Cell cell21 = secondRow.createCell(0);
		cell21.setCellValue("หน่วยงาน  สำนักงานกองทุนสงเคราะห์การทำสวนยาง");
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
		Cell cell317 = thirdRow.createCell(16);
		cell317.setCellValue("ผู้รับผิดชอบ");
		cell317.setCellStyle(styles.get("header"));
		
		Connection connection = dataSource.getConnection();
				
		PreparedStatement ps = null;
		Statement st = connection.createStatement();
		String rsSql = "select lpad(' ',(level-4)*5)||m.name name, m.isleaf, m.id, nvl(lpad(' ',(level-3)*5), '     ') space, m.code " +
                "from pln_objective m " +
                "where m.id <> " + root.getId() + " " +
				   "and m.fiscalyear = " + fiscalYear + " " +
				   "connect by prior m.id = m.parent_pln_objective_id " +
				   "start with m.id = " + root.getId() + " " +
				   " order siblings by m.code asc ";
		ResultSet rs = st.executeQuery(rsSql) ;

		logger.debug("rsSQL >>>>> ");
		logger.debug(rsSql);
		logger.debug("rsSQL >>>>> ");
		
		int i = 4;
		int j = 0;
		double s1 = 0.0;
		int s2 = 0;
		int s3 = 0;
		int s4 = 0;
		while (rs.next()) {
			Row rows = sheet.createRow(i);
			
			Cell rsc0 = rows.createCell(0);
			rsc0.setCellValue(rs.getString(1));
			rsc0.setCellStyle(styles.get("cellleft"));
			
			if (rs.getInt(2) == 1) {
				Statement st0 = connection.createStatement();
				ResultSet rs0 = st0.executeQuery("select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(budgetallocated),'999,999,999,999')), '...')||' บาท)' " +
												 "from pln_activity t1, pln_activityperformance t2 " +
												 "where t1.id = t2.activity_pln_activity_id " +
												 "and t1.obj_pln_objective_id = " + rs.getInt(3) + " ");
				
				Cell rsc1 = rows.createCell(1);
				if (rs0.next()) {
					rsc1.setCellValue(rs0.getString(1));
				}
				rsc1.setCellStyle(styles.get("cellcenter"));
				rs0.close();
				st0.close();
				
				Cell rsc2 = rows.createCell(2);
				rsc2.setCellValue("แผนการใช้เงิน");
				rsc2.setCellStyle(styles.get("cellcenter"));
				
				for (j=3;j<17;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellcenternumber1"));
				}

				Statement st3 = connection.createStatement();
				ResultSet rs3 = st3.executeQuery("select t1.fiscalmonth, sum(t1.budgetplan), ltrim(to_char(sum(t1.budgetplan),'999,999,999,999')) " +
												 "from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activity t3 " +
											  	 "where t1.performance_pln_actper_id = t2.id " +
												 "and t2.activity_pln_activity_id = t3.id " +
												 "and t3.obj_pln_objective_id = " + rs.getInt(3) + 
												 " group by t1.fiscalmonth " +
												 "order by t1.fiscalmonth ");

				j = 3;
				s1 = 0;
				while (rs3.next()) {
					Cell rscj = rows.getCell(j);
					rscj.setCellValue(rs3.getString(3));
					s1 = s1 + rs3.getInt(2);
					j = j+1;
				}
				rs3.close();
				st3.close();
				Cell rsc3 = rows.getCell(15);
				rsc3.setCellValue(s1);

				rows = sheet.createRow(i+1);

				rsc1 = rows.createCell(1);
				rsc1.setCellStyle(styles.get("cellcenter"));
				
				rsc2 = rows.createCell(2);
				rsc2.setCellValue("ผลการใช้เงิน (G)");
				rsc2.setCellStyle(styles.get("cellcenter"));
				
				for (j=3;j<17;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellcenternumber2"));
				}

				Statement st4 = connection.createStatement();
				
				String objCodeStr = rs.getString(5);
				// now if we can't convert to integer just forget it
				Long objCodeLong = 0L;
				try {
					objCodeLong = Long.parseLong(objCodeStr);
				} catch (NumberFormatException e) {
					
				}
				
				
				ResultSet rs4 = st4.executeQuery("select date2fmonth(gl_trans_docdate) mon, nvl(sum(amt),0), ltrim(to_char(sum(amt),'999,999,999,990.99')) amt " +
									   "from v_gl " +
									   "where fiscal_year = " + fiscalYear + " " +
									   "and activitycode = '" + objCodeLong + "' " +
									   "group by date2fmonth(gl_trans_docdate) " +
									   "order by 1 ");

				s1 = 0.0;
				while (rs4.next()) {
					Cell rscj = rows.getCell(rs4.getInt(1)+2);
					rscj.setCellValue(rs4.getString(3));
					s1 = s1 + rs4.getDouble(2);
				}
				rs4.close();
				st4.close();
				rsc3 = rows.getCell(15);
				rsc3.setCellValue(s1);

				i = i+2;
				Statement st1 = connection.createStatement();
				ResultSet rs1 = st1.executeQuery("select t1.code, t1.name, t1.id, '1' type, t3.id target_id, '  (เป้าหมาย '|| ltrim(to_char(sum(t3.targetvalue),'999,999,999,999')) ||' '||t4.name||')' target, t4.id unit_id " +
												 "from pln_activity t1, pln_activitytarget t3, pln_targetunit t4 " +
												 "where t1.id = t3.activity_pln_activity_id " +
												 "and t3.unit_pln_targetunit_id = t4.id " +
												 "and t1.obj_pln_objective_id = " + rs.getInt(3) + " " +
												 "group by t1.code, t1.name, t1.id, t3.id, t4.name, t4.id " +
												 "order by 1,4 ");
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
					rsc12.setCellValue(rs1.getString(6));
					rsc12.setCellStyle(styles.get("cellcenter"));

					Cell rsc13 = rows1.createCell(2);
					rsc13.setCellValue("แผนงาน");
					rsc13.setCellStyle(styles.get("cellcenter"));
					
					for (j=3;j<17;j++) {
						Cell rscj = rows1.createCell(j);
						if (rs1.getInt(7)==2 || rs1.getInt(7)==3 || rs1.getInt(7)==9) {
							rscj.setCellStyle(styles.get("cellcenternumber2"));
						} else {
							rscj.setCellStyle(styles.get("cellcenternumber1"));
						}

					}

					Row rows2 = sheet.createRow(i+1);
					Cell rsc21 = rows2.createCell(0);
					rsc21.setCellStyle(styles.get("cellleft"));
					Cell rsc22 = rows2.createCell(1);
					rsc22.setCellStyle(styles.get("cellcenter"));
					Cell rsc23 = rows2.createCell(2);
					rsc23.setCellValue("ผลงาน");
					rsc23.setCellStyle(styles.get("cellcenter"));
					
					for (j=3;j<17;j++) {
						Cell rscj = rows2.createCell(j);
						if (rs1.getInt(7)==2 || rs1.getInt(7)==3 || rs1.getInt(7)==9) {
							rscj.setCellStyle(styles.get("cellcenternumber2"));
						} else {
							rscj.setCellStyle(styles.get("cellcenternumber1"));
						}

					}
					
					Statement st2 = connection.createStatement();
					ResultSet rs2;
					rs2 = st2.executeQuery("select t1.fiscalmonth, sum(t1.activityplan), sum(t1.activityresult) " +
										   "from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3 " +
										   "where t1.report_pln_acttargetreport_id = t2.id " +
										   "and t2.target_pln_acttarget_id = t3.id " +
										   "and t3.activity_pln_activity_id = " + rs1.getInt(3) + 
										   " and t3.id = " + rs1.getInt(5) +
										   " group by t1.fiscalmonth order by t1.fiscalmonth ");

					j = 3;
					s1 = 0;
					s2 = 0;
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
					st2.close();
					Cell rscs1 = rows1.getCell(15);
					rscs1.setCellValue(s1);
					Cell rscs2 = rows2.getCell(15);
					rscs2.setCellValue(s2);

					i = i+2;
				}
				rs1.close();
				st1.close();
			}
			else {
				for (j=1;j<17;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellleft"));

				}
				
				// แสดงผู้รับผิดชอบ
				Long objId = rs.getLong(3);
				
				Statement st5 = connection.createStatement();
				ResultSet rs5;
				String sql5 = ""
						+ "SELECT  org.abbr "
						+ "FROM pln_objectiveownerrelation rel, "
						+ "		pln_objective_owner_join owner_join, "
						+ "		hrx_organization org "
						+ "WHERE rel.id = owner_join.pln_objectiveownerrelation_id "
						+ "		and owner_join.owners_id = org.id"
						+ "		and rel.obj_pln_objective_id=" + objId; 
  
				rs5 = st5.executeQuery(sql5);
				String owner = "";
				while (rs5.next()) {
					owner += rs5.getString(1);
				}
				Cell cell = rows.getCell(16);
				cell.setCellValue(owner);
				
				
				
				i = i+1;
			}
		}

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
		sheet.setColumnWidth(15, 3500);
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
        style.setAlignment(CellStyle.ALIGN_CENTER);
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
        styles.put("cellcenternumber1", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0.00"));
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellcenternumber2", style);

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
