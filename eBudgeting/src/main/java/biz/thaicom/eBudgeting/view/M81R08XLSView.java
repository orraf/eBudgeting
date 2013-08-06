package biz.thaicom.eBudgeting.view;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;

import biz.thaicom.eBudgeting.models.bgt.AssetAllocation;
import biz.thaicom.eBudgeting.models.bgt.AssetMethod;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.Objective;

public class M81R08XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
	
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
		List<AssetAllocation> noMethodAllocs  = (List<AssetAllocation>)model.get("noMethodAllocs");
		
		@SuppressWarnings("unchecked")
		HashMap<AssetMethod, List<AssetAllocation>> assetMap = (HashMap<AssetMethod, List<AssetAllocation>>)model.get("assetMap");
		
		String sheetName = WorkbookUtil.createSafeSheetName("งบลงทุนที่ยังไม่มีการบันทึกวิธีการจัดหา");
		
		Sheet sheet = workbook.createSheet(sheetName);
		int rowNum = 0;
		Row row;
		Cell cell;
		for(AssetAllocation alloc : noMethodAllocs) {
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue(alloc.getAssetBudget().getName());
			cell = row.createCell(1);
			if(alloc.getOwner() == null) {
				cell.setCellValue("ยังไม่ได้ระบุ");
			} else {
				cell.setCellValue(alloc.getOwner().getAbbr());
			}
			
			cell = row.createCell(2);
			
			if(alloc.getOperator() == null) {
				cell.setCellValue("ยังไม่ได้ระบุ");
			} else {
				cell.setCellValue(alloc.getOperator().getAbbr());
			}
		}
		
		for(AssetMethod method: assetMap.keySet()){
			 sheet = workbook.createSheet(method.getName());
			 rowNum = 0;
			 
			 for(AssetAllocation alloc : assetMap.get(method)) {
					row = sheet.createRow(rowNum++);
					cell = row.createCell(0);
					cell.setCellValue(alloc.getAssetBudget().getName());
					cell = row.createCell(1);
					if(alloc.getOwner() == null) {
						cell.setCellValue("ยังไม่ได้ระบุ");
					} else {
						cell.setCellValue(alloc.getOwner().getAbbr());
					}
					
					cell = row.createCell(2);
					
					if(alloc.getOperator() == null) {
						cell.setCellValue("ยังไม่ได้ระบุ");
					} else {
						cell.setCellValue(alloc.getOperator().getAbbr());
					}
				}
			
		}
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
        style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFont(cellFont);
        styles.put("cellRoseColor", style);

        
        return styles;
    }

}
