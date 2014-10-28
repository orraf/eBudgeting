package biz.thaicom.eBudgeting.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

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
import org.apache.poi.ss.util.WorkbookUtil;

import biz.thaicom.eBudgeting.models.bgt.BudgetProposal;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Objective;

public class M82R03XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy", new Locale("th", "TH"));
	
	@Override
	protected Workbook createWorkbook() {
		return new HSSFWorkbook();
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, CellStyle> styles = createStyles(workbook);

		@SuppressWarnings("unchecked")
		List<Objective> ผลผลิต  = (List<Objective>)model.get("ผลผลิต");
		
		@SuppressWarnings("unchecked")
		HashMap<String, Double> ผลจัดสรรMap  = (HashMap<String, Double>)model.get("ผลจัดสรรMap");
		
		Integer fiscalYear = (Integer) model.get("fiscalYear");
		
		@SuppressWarnings("unchecked")
		String sheetName = WorkbookUtil.createSafeSheetName("รายงาน1");
		
		Sheet sheet = workbook.createSheet(sheetName);
		int rowNum = 0;
		int colNum=0;
		Row row;
		Cell cell;
		sheet.setColumnWidth(0, 2500);
		sheet.setColumnWidth(1, 15000);
		sheet.setColumnWidth(2, 2500);
		sheet.setColumnWidth(3, 2500);
		sheet.setColumnWidth(4, 2500);
		sheet.setColumnWidth(5, 2500);
		sheet.setColumnWidth(6, 2500);
		
		Row firstRow = sheet.createRow(rowNum++);
		Cell cell0 = firstRow.createCell(0);
		
		cell0.setCellValue("วันที่พิมพ์รายงาน: " +  printTimeFormat.format(new Date()) );
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		
		cell.setCellValue("รายงานสรุปการผลการจัดสรรงบประมาณในระดับฝ่ายและจังหวัด ปีงบประมาณ " + fiscalYear);
		
		for(Objective o_ผลผลิต: ผลผลิต) {
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue(o_ผลผลิต.getName());	
			for(Objective o_กิจกรรมหลัก : o_ผลผลิต.getChildren()) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("    " +o_กิจกรรมหลัก.getName());	
				
				for(Objective o_แผนปฏิบัติงาน : o_กิจกรรมหลัก.getChildren()) {
					row = sheet.createRow(rowNum++);
					cell = row.createCell(0);
					cell.setCellValue("        " +o_แผนปฏิบัติงาน.getName());	
					
					String listOfOk = "";
					String listOfNg = "";
					
					for(BudgetProposal proposal : o_แผนปฏิบัติงาน.getProposals()) {
						String key = o_แผนปฏิบัติงาน.getId() + "-" + proposal.getOwner().getId();
						Double ผลจัดสรร = ผลจัดสรรMap.get(key);
						if(ผลจัดสรร != null && ผลจัดสรร.equals(proposal.getAmountAllocated()) ) {
							// listOfOk += ", " + proposal.getOwner().getAbbr() + "(" + proposal.getAmountAllocated()+ "/"+ ผลจัดสรร+ ")";
							listOfOk += ", " + proposal.getOwner().getAbbr();
						} else {
							//Double print = ผลจัดสรร!=null?ผลจัดสรร:0.0;
							//listOfNg += ", " + proposal.getOwner().getAbbr() + "(" + proposal.getAmountAllocated()+ "/"+ print+ ")";
							listOfNg += ", " + proposal.getOwner().getAbbr();
						}
						
					}
					row = sheet.createRow(rowNum++);
					cell = row.createCell(0);
					cell = row.createCell(1);
					cell.setCellStyle(styles.get("cellLightGreenColor"));
					cell.setCellValue("หน่วยงานที่ทำการจัดสรรงบประมาณแล้ว");
					if(listOfOk.length() > 0) {
						listOfOk = listOfOk.substring(2);
					}
					cell = row.createCell(2);
					cell.setCellValue(listOfOk);

					
					row = sheet.createRow(rowNum++);
					cell = row.createCell(0);
					cell = row.createCell(1);
					
					cell.setCellStyle(styles.get("cellLightOrgangeColor"));
					cell.setCellValue("หน่วยงานที่ยังไม่ได้ทำการจัดสรรงบประมาณหรือยังจัดสรรไม่เสร็จ");
					if(listOfNg.length() > 0) {
						listOfNg = listOfNg.substring(2);
					}
					cell = row.createCell(2);
					cell.setCellValue(listOfNg);
					
				}
			}
		}
		
	}
	
	
    private String formatDate(Date date) {
    	
    	if(date == null) {
    		return "ไม่ระบุ";
    	}
    	
		return df.format(date);
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
        styles.put("header", style);

        Font groupFont = wb.createFont();
        groupFont.setFontName("Tahoma");
        groupFont.setFontHeightInPoints((short)11);
        groupFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        
        Font cellFont = wb.createFont();
        cellFont.setFontName("Tahoma");
        cellFont.setFontHeightInPoints((short)11);
        
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(groupFont);
        styles.put("groupleft", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFont(cellFont);
        styles.put("cellleft", style);

       
        style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFont(cellFont);
        styles.put("cellLightOrgangeColor", style);

        style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFont(cellFont);
        styles.put("cellLightGreenColor", style);
        
        return styles;
    }

}
