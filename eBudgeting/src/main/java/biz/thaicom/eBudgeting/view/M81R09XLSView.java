package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
import org.apache.poi.ss.util.CellRangeAddress;

import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R09XLSView extends AbstractPOIExcelView {

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
		Sheet sheet = workbook.createSheet("sheet1");


		Row firstRow = sheet.createRow(0);
		Cell cell10 = firstRow.createCell(0);
		cell10.setCellValue("ครุภัณฑ์ ประจำปี " + fiscalYear);
		cell10.setCellStyle(styles.get("title"));
		Cell cell11 = firstRow.createCell(1);
		Cell cell12 = firstRow.createCell(2);
		Cell cell13 = firstRow.createCell(3);
		Cell cell14 = firstRow.createCell(4);
		Cell cell15 = firstRow.createCell(5);
		Cell cell16 = firstRow.createCell(6);
		Cell cell17 = firstRow.createCell(7);
		Cell cell18 = firstRow.createCell(8);
		Cell cell19 = firstRow.createCell(9);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

		Row secondRow = sheet.createRow(1);
		Cell cell20 = secondRow.createCell(0);
		cell20.setCellValue("หน่วยงาน");
		cell20.setCellStyle(styles.get("header"));
		Cell cell21 = secondRow.createCell(1);
		cell21.setCellValue("ลำดับ ที่");
		cell21.setCellStyle(styles.get("header"));
		Cell cell22 = secondRow.createCell(2);
		cell22.setCellValue("รหัส");
		cell22.setCellStyle(styles.get("header"));
		Cell cell23 = secondRow.createCell(3);
		cell23.setCellValue("รายการ");
		cell23.setCellStyle(styles.get("header"));
		Cell cell24 = secondRow.createCell(4);
		cell24.setCellValue("จำนวน หน่วย");
		cell24.setCellStyle(styles.get("header"));
		Cell cell25 = secondRow.createCell(5);
		cell25.setCellValue("ราคาต่อหน่วย");
		cell25.setCellStyle(styles.get("header"));
		Cell cell26 = secondRow.createCell(6);
		cell26.setCellValue("รวมเงิน (บาท)");
		cell26.setCellStyle(styles.get("header"));
		Cell cell27 = secondRow.createCell(7);
		cell27.setCellValue("รหัสครุภัณฑ์");
		cell27.setCellStyle(styles.get("header"));
		Cell cell28 = secondRow.createCell(8);
		Cell cell29 = secondRow.createCell(9);
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 9));

		Row thirdRow = sheet.createRow(2);
		Cell cell30 = thirdRow.createCell(0);
		cell30.setCellStyle(styles.get("header"));
		Cell cell31 = thirdRow.createCell(1);
		cell31.setCellStyle(styles.get("header"));
		Cell cell32 = thirdRow.createCell(2);
		cell32.setCellStyle(styles.get("header"));
		Cell cell33 = thirdRow.createCell(3);
		cell33.setCellStyle(styles.get("header"));
		Cell cell34 = thirdRow.createCell(4);
		cell34.setCellStyle(styles.get("header"));
		Cell cell35 = thirdRow.createCell(5);
		cell35.setCellStyle(styles.get("header"));
		Cell cell36 = thirdRow.createCell(6);
		cell36.setCellStyle(styles.get("header"));
		Cell cell37 = thirdRow.createCell(7);
		cell37.setCellValue("หมวด");
		cell37.setCellStyle(styles.get("header"));
		Cell cell38 = thirdRow.createCell(8);
		cell38.setCellValue("ประเภท");
		cell38.setCellStyle(styles.get("header"));
		Cell cell39 = thirdRow.createCell(9);
		cell39.setCellValue("ชนิด");
		cell39.setCellStyle(styles.get("header"));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
		
		Connection connection = dataSource.getConnection();
				
		PreparedStatement ps = null;
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery("select t6.code, t6.abbr, ' ' seq, t2.code, t2.description, t1.quantity, t1.unitbudget, t1.quantity * t1.unitbudget total, t5.assetgroupcode, t4.assettypecode, t3.assetkindcode " +
									   "from bgt_assetallocation t1, bgt_assetbudget t2, gla_assetkind t3, gla_assettype t4, gla_assetgroup t5, hrx_organization t6 " +
									   "where t1.bgt_assetbudget_id = t2.id " +
									   "and t2.assetkind_id = t3.id " +
									   "and t3.typeid = t4.id " +
									   "and t3.groupid = t5.id " +
									   "and t1.hrx_owner_id = t6.id " +
									   "and t1.fiscalyear = " + fiscalYear + " " +
									   "order by t6.code, t2.code ");

		int i = 3;
		String code = " ";
		while (rs.next()) {
			Row rows = sheet.createRow(i);
			Cell rsc0 = rows.createCell(0);
			if (code.equals(rs.getString(1))) rsc0.setCellValue(" ");
			else {
				rsc0.setCellValue(rs.getString(2));
				code = rs.getString(1);
			}
			rsc0.setCellStyle(styles.get("cellleft"));
			Cell rsc1 = rows.createCell(1);
			rsc1.setCellValue(rs.getString(3));
			rsc1.setCellStyle(styles.get("cellcenter"));
			Cell rsc2 = rows.createCell(2);
			rsc2.setCellValue(rs.getString(4));
			rsc2.setCellStyle(styles.get("cellcenter"));
			Cell rsc3 = rows.createCell(3);
			rsc3.setCellValue(rs.getString(5));
			rsc3.setCellStyle(styles.get("cellleft"));
			Cell rsc4 = rows.createCell(4);
			rsc4.setCellValue(rs.getInt(6));
			rsc4.setCellStyle(styles.get("cellcenter"));
			Cell rsc5 = rows.createCell(5);
			rsc5.setCellValue(rs.getInt(7));
			rsc5.setCellStyle(styles.get("cellnumber"));
			Cell rsc6 = rows.createCell(6);
			rsc6.setCellValue(rs.getInt(8));
			rsc6.setCellStyle(styles.get("cellnumber"));
			Cell rsc7 = rows.createCell(7);
			rsc7.setCellValue(rs.getString(9));
			rsc7.setCellStyle(styles.get("cellcenter"));
			Cell rsc8 = rows.createCell(8);
			rsc8.setCellValue(rs.getString(10));
			rsc8.setCellStyle(styles.get("cellcenter"));
			Cell rsc9 = rows.createCell(9);
			rsc9.setCellValue(rs.getString(11));
			rsc9.setCellStyle(styles.get("cellcenter"));
			
			i = i + 1;
		}

		rs.close();
		st.close();
		connection.close();

		sheet.setColumnWidth(0, 5000);
		sheet.setColumnWidth(1, 2000);
		sheet.setColumnWidth(2, 3500);
		sheet.setColumnWidth(3, 14000);
		sheet.setColumnWidth(4, 2000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 3000);
		sheet.createFreezePane( 0, 3 );
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
        style.setAlignment(CellStyle.ALIGN_CENTER);
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
        style.setDataFormat(format.getFormat("#,##0.00"));
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
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellleft", style);

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
        style.setDataFormat(format.getFormat("#,##0.00"));
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

        Font summaryFont = wb.createFont();
        summaryFont.setFontName("Arial");
        summaryFont.setFontHeightInPoints((short)10);
        summaryFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0.00"));
        style.setFont(summaryFont);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("summarynumber", style);

        return styles;
    }
}
