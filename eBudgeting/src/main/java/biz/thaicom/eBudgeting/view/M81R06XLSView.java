package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R06XLSView extends AbstractPOIExcelView {

	public static Logger logger = LoggerFactory.getLogger(M81R06XLSView.class);
	
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
  		Integer startMonth = (Integer) model.get("startMonth");
  		Integer endMonth = (Integer) model.get("endMonth");
		Objective obj = (Objective) model.get("objective");
		Organization org = (Organization) model.get("organization");
		
		
		Sheet sheet = workbook.createSheet("sheet1");
		Integer oldYear = fiscalYear - 1;
		int i = 7;
		int j = 0;
		DecimalFormat df = new DecimalFormat("###,###,###,###.##");
		DecimalFormat df2 = new DecimalFormat("##0.00");

		Row firstRow = sheet.createRow(0);
		Cell cell0 = firstRow.createCell(0);
		cell0.setCellValue("วันที่พิมพ์รายงาน: " +  printTimeFormat.format(new Date()) );
		
		firstRow = sheet.createRow(1);
		Cell cell11 = firstRow.createCell(0);
		cell11.setCellValue("แผนงาน / โครงการตามแผนวิสาหกิจ  ประจำปีงบประมาณ " + fiscalYear);
		cell11.setCellStyle(styles.get("title"));

/*		Row subFirstRow = sheet.createRow(1);
		Cell subCell11 = subFirstRow.createCell(0);
		subCell11.setCellValue("ผู้จัดทำรายงาน " + 
				currentUser.getPerson().getFirstName() + " " +	currentUser.getPerson().getLastName() + 
				" เวลาที่จัดทำรายงาน " +  sdf.format(new Date()) + "น.");
*/		
		Row secondRow = sheet.createRow(2);
		Cell cell21 = secondRow.createCell(0);
		if(org.getId() != 0L) {
			cell21.setCellValue("หน่วยงาน  " + org.getName());
		}
		
		cell21.setCellStyle(styles.get("title"));
		
		Row thirdRow = sheet.createRow(3);
		Cell cell301 = thirdRow.createCell(0);
		cell301.setCellValue("แผนงาน/โครงการ/กิจกรรม");
		cell301.setCellStyle(styles.get("header"));
		Cell cell302 = thirdRow.createCell(1);
		cell302.setCellValue("เป้าหมาย");
		cell302.setCellStyle(styles.get("header"));
		Cell cell303 = thirdRow.createCell(2);
		cell303.setCellStyle(styles.get("header"));
		Cell cell304 = thirdRow.createCell(3);
		cell304.setCellValue("ผลการดำเนินงาน");
		cell304.setCellStyle(styles.get("header"));
		Cell cell305 = thirdRow.createCell(4);
		cell305.setCellStyle(styles.get("header"));
		Cell cell306 = thirdRow.createCell(5);
		cell306.setCellStyle(styles.get("header"));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 5));

		Row forthRow = sheet.createRow(4);
		Cell cell401 = forthRow.createCell(0);
		cell401.setCellStyle(styles.get("header"));
		Cell cell402 = forthRow.createCell(1);
		cell402.setCellValue("รวมทั้งปี");
		cell402.setCellStyle(styles.get("header"));
		Cell cell403 = forthRow.createCell(2);
		if (startMonth == null || endMonth == null) {
			cell403.setCellValue("ต.ค." + oldYear.toString().substring(2, 4) + " - ก.ย." + fiscalYear.toString().substring(2, 4));	
		}
		else {
			cell403.setCellValue(getMonth(startMonth) + oldYear.toString().substring(2, 4) + " - " + getMonth(endMonth) + fiscalYear.toString().substring(2, 4));
		}
		cell403.setCellStyle(styles.get("header"));
		Cell cell404 = forthRow.createCell(3);
		if (startMonth == null || endMonth == null) {
			cell404.setCellValue("ต.ค." + oldYear.toString().substring(2, 4) + " - ก.ย." + fiscalYear.toString().substring(2, 4));	
		}
		else {
			cell404.setCellValue(getMonth(startMonth) + oldYear.toString().substring(2, 4) + " - " + getMonth(endMonth) + fiscalYear.toString().substring(2, 4));
		}
		cell404.setCellStyle(styles.get("header"));
		Cell cell405 = forthRow.createCell(4);
		cell405.setCellValue("ร้อยละ");
		cell405.setCellStyle(styles.get("header"));
		Cell cell406 = forthRow.createCell(5);
		cell406.setCellStyle(styles.get("header"));
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 4, 5));
		
		Row fifthRow = sheet.createRow(5);
		Cell cell501 = fifthRow.createCell(0);
		cell501.setCellStyle(styles.get("header"));
		Cell cell502 = fifthRow.createCell(1);
		cell502.setCellStyle(styles.get("header"));
		Cell cell503 = fifthRow.createCell(2);
		cell503.setCellStyle(styles.get("header"));
		Cell cell504 = fifthRow.createCell(3);
		cell504.setCellStyle(styles.get("header"));
		Cell cell505 = fifthRow.createCell(4);
		if (startMonth == null || endMonth == null) {
			cell505.setCellValue("ต.ค." + oldYear.toString().substring(2, 4) + " - ก.ย." + fiscalYear.toString().substring(2, 4));	
		}
		else {
			cell505.setCellValue(getMonth(startMonth) + oldYear.toString().substring(2, 4) + " - " + getMonth(endMonth) + fiscalYear.toString().substring(2, 4));
		}
		cell505.setCellStyle(styles.get("header"));
		Cell cell506 = fifthRow.createCell(5);
		cell506.setCellValue("รวมทั้งปี");
		cell506.setCellStyle(styles.get("header"));
		sheet.addMergedRegion(new CellRangeAddress(3, 5, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(4, 5, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(4, 5, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(4, 5, 3, 3));

		Row sixthRow = sheet.createRow(6);
		Cell cell6 = sixthRow.createCell(0);
		cell6.setCellValue(obj.getName());
		cell6.setCellStyle(styles.get("headerleft"));
		for (j=1;j<6;j++) {
			Cell rscj = sixthRow.createCell(j);
			rscj.setCellStyle(styles.get("headerleft"));
		}
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 5));
		
		Connection connection = dataSource.getConnection();
				
		PreparedStatement ps = null;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("select lpad(' ',(level-4)*5)||m.name name, m.isleaf, m.id " +
	               "from pln_objective m " +
	               "where m.id <> " + obj.getId() + " " +
				   "and m.fiscalyear = " + fiscalYear + " " +
				   "connect by prior m.id = m.parent_pln_objective_id " +
				   "start with m.id = " + obj.getId() + " " + 
				   "ORDER BY m.code asc ");

		while (rs.next()) {
			Row rows = sheet.createRow(i);
			
			Cell rsc0 = rows.createCell(0);
			rsc0.setCellValue(rs.getString(1));
			rsc0.setCellStyle(styles.get("cellleft"));
			
			if (rs.getInt(2) == 1) {
				for (j=1;j<6;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellleft"));

				}
				i = i+1;
				Statement st1 = connection.createStatement();
				ResultSet rs1;
				if (org.getId() == 0L) {
					rs1 = st1.executeQuery("select t1.code, t1.name, t1.id, t2.id target_id, sum(t3.targetvalue) target, t4.name unit " +
							 "from pln_activity t1, pln_activitytarget t2, pln_activitytargetreport t3, pln_targetunit t4 " +
							 "where t1.id = t2.activity_pln_activity_id " +
							 "and t2.id = t3.target_pln_acttarget_id " +
							 "and t2.unit_pln_targetunit_id = t4.id " +
							 "and t1.obj_pln_objective_id = " + rs.getInt(3) + " " +
							 "group by t1.code, t1.name, t1.id, t2.id, t4.name " +
							 "order by t1.code");
				}
				else {
					String sql = "select t1.code, t1.name, t1.id, t2.id target_id, sum(t3.targetvalue) target, t4.name unit " +
							 "from pln_activity t1, pln_activitytarget t2, pln_activitytargetreport t3, pln_targetunit t4 " +
							 "where t1.id = t2.activity_pln_activity_id " +
							 "and t2.id = t3.target_pln_acttarget_id " +
							 "and t2.unit_pln_targetunit_id = t4.id " +
							 "and t1.obj_pln_objective_id = " + rs.getInt(3) + " " +
							 "and t3.owner_hrx_organization_id = " + org.getId() +
							 " group by t1.code, t1.name, t1.id, t2.id, t4.name " +
							 "order by t1.code";
					
					rs1 = st1.executeQuery(sql);
				}
				
				int actId = 0;	 
				while (rs1.next()) {
					Row rows1 = sheet.createRow(i);
					Cell rsc11 = rows1.createCell(0);
					rsc11.setCellValue("      - " + rs1.getString(2) + "  (" + rs1.getString(6) + ")");
					rsc11.setCellStyle(styles.get("cellleft"));

					Cell rsc12 = rows1.createCell(1);
					rsc12.setCellValue(df.format(rs1.getInt(5)));
					rsc12.setCellStyle(styles.get("cellright"));

					for (j=2;j<6;j++) {
						Cell rscj = rows1.createCell(j);
						rscj.setCellStyle(styles.get("cellright"));

					}

					Statement st2 = connection.createStatement();
					ResultSet rs2;
					if (org.getId() == 0) {
						rs2 = st2.executeQuery("select sum(t1.activityplan), sum(t1.activityresult) " +
								   "from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3 " +
								   "where t1.report_pln_acttargetreport_id = t2.id " +
								   "and t2.target_pln_acttarget_id = t3.id " +
								   "and t3.activity_pln_activity_id = " + rs1.getInt(3) + 
								   " and t3.id = " + rs1.getInt(4) );
					}
					else {
						String sql = "select sum(t1.activityplan), sum(t1.activityresult) "
									+ "from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3,"
									+ "	hrx_organization o1 "
									+ "where t1.report_pln_acttargetreport_id = t2.id "
									+ " and t1.owner_hrx_organization_id = o1.id "
									+ " and t2.target_pln_acttarget_id = t3.id "
									+ " and (t2.owner_hrx_organization_id = " + org.getId() + "or o1.parent_hrx_organization_id = " + org.getId() + ") " 
									+ " and t3.activity_pln_activity_id = " + rs1.getInt(3)
									+ " and t3.id = " + rs1.getInt(4);
						
						rs2 = st2.executeQuery(sql);
					}

					while (rs2.next()) {
						Cell rscj1 = rows1.getCell(2);
						rscj1.setCellValue(df.format(rs2.getInt(1)));
						Cell rscj2 = rows1.getCell(3);
						rscj2.setCellValue(df.format(rs2.getInt(2)));
						Cell rscj3 = rows1.getCell(4);
						Cell rscj4 = rows1.getCell(5);
						if (rs1.getInt(5) == 0) {
							rscj3.setCellValue(0.00);
							rscj4.setCellValue(0.00);
						}
						else {
							rscj3.setCellValue(df2.format(rs2.getInt(1) / (double) rs1.getInt(5) * 100.0));
							rscj4.setCellValue(df2.format(rs2.getInt(2) / (double) rs1.getInt(5) * 100.0));
						}
					}
					rs2.close();
					st2.close();
					
					i = i+1;
				}
				rs1.close();
				st1.close();
			}
			else {
				for (j=1;j<6;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellleft"));

				}
				i = i+1;
			}
		}

		Row rowE = sheet.createRow(i);
		Cell re = rowE.createCell(0);
		re.setCellStyle(styles.get("celltop"));
		
		rs.close();
		st.close();
		connection.close();

		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 3000);
		sheet.createFreezePane( 1, 7 );
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

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
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
        styles.put("headerleft", style);

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
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("celltop", style);

        return styles;
    }
    
    private String getMonth(Integer m) {
    	String month = null;
		if (m == 0) month = "ต.ค.";
		else if (m == 1) month = "พ.ย.";
		else if (m == 2) month = "ธ.ค.";
		else if (m == 3) month = "ม.ค.";
		else if (m == 4) month = "ก.พ.";
		else if (m == 5) month = "มี.ค.";
		else if (m == 6) month = "เม.ย.";
		else if (m == 7) month = "พ.ค.";
		else if (m == 8) month = "มิ.ย.";
		else if (m == 9) month = "ก.ค.";
		else if (m == 10) month = "ส.ค.";
		else if (m == 11) month = "ก.ย.";
		
		return month;
	}

}
