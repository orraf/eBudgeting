package biz.thaicom.eBudgeting.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import biz.thaicom.eBudgeting.models.bgt.AssetMethodStep;
import biz.thaicom.eBudgeting.models.hrx.Organization;

public class M81R08XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat sdf = new SimpleDateFormat("d MMM yy", new Locale("th", "TH"));
	private static SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy", new Locale("th", "TH"));
	
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
		
		Organization org = (Organization)model.get("org");
		
		String sheetName = WorkbookUtil.createSafeSheetName("งบลงทุนที่ยังไม่มีการบันทึกวิธีการจัดหา");
		
		Sheet sheet = workbook.createSheet(sheetName);
		int rowNum = 0;
		int colNum=0;
		Row row;
		Cell cell;
		sheet.setColumnWidth(0, 17500);
		sheet.setColumnWidth(1, 3200);
		sheet.setColumnWidth(2, 3200);
		sheet.setColumnWidth(3, 1800);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		
		Row firstRow = sheet.createRow(rowNum++);
		Cell cell0 = firstRow.createCell(0);
		if(org == null) {
			cell0.setCellValue("รายงานสรุปผลการใช้จ่ายงบลงทุน" );
		} else {
			cell0.setCellValue("รายงานสรุปผลการใช้จ่ายงบลงทุน หน่วยงาน: " + org.getName() );
		}
		
		
		cell0.setCellStyle(styles.get("title"));
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(0);
		cell.setCellValue("วันที่รายงาน: " +  df.format(new Date()));
		cell.setCellStyle(styles.get("title"));
		
		row = sheet.createRow(rowNum++);
		cell = row.createCell(colNum++);
		cell.setCellValue("ชื่อครุภัณฑ์-สิ่งก่อสร้าง");
		cell.setCellStyle(styles.get("header"));
		
		cell = row.createCell(colNum++);
		cell.setCellValue("หน่วยงานเจ้าของ");
		cell.setCellStyle(styles.get("header"));
		
		cell = row.createCell(colNum++);
		cell.setCellValue("หน่วยงานดำเนินการ");
		cell.setCellStyle(styles.get("header"));
		
		cell = row.createCell(colNum++);
		cell.setCellValue("จำนวน");
		cell.setCellStyle(styles.get("header"));
		
		
		cell = row.createCell(colNum++);
		cell.setCellValue("งบต่อหน่วย");
		cell.setCellStyle(styles.get("header")); 
		
		cell = row.createCell(colNum++);
		cell.setCellValue("รวมงบได้รับ");
		cell.setCellStyle(styles.get("header"));
		
		for(AssetAllocation alloc : noMethodAllocs) {

			colNum=0;
			row = sheet.createRow(rowNum++);
			cell = row.createCell(colNum++);
			cell.setCellValue(alloc.getAssetBudget().getName());
			cell.setCellStyle(styles.get("cellleft"));
			
			cell = row.createCell(colNum++);
			if(alloc.getOwner() == null) {
				cell.setCellValue("ยังไม่ได้ระบุ");
			} else {
				cell.setCellValue(alloc.getOwner().getAbbr());
			}
			cell.setCellStyle(styles.get("cellleft"));
			
			cell = row.createCell(colNum++);
			
			if(alloc.getOperator() == null) {
				cell.setCellValue("ยังไม่ได้ระบุ");
			} else {
				cell.setCellValue(alloc.getOperator().getAbbr());
			}
			cell.setCellStyle(styles.get("cellleft"));
			
			cell = row.createCell(colNum++);
			cell.setCellValue(alloc.getQuantity());
			cell.setCellStyle(styles.get("cellcenter"));
			
			cell = row.createCell(colNum++);
			cell.setCellValue(alloc.getUnitBudget());
			cell.setCellStyle(styles.get("cellnumber"));
			
			cell = row.createCell(colNum++);
			cell.setCellValue(alloc.getUnitBudget() * alloc.getQuantity());
			cell.setCellStyle(styles.get("cellnumber"));
			
		}
		
		for(AssetMethod method: assetMap.keySet()){
			if(assetMap.get(method).size() > 0 ) {

				sheet = workbook.createSheet(method.getName());

				
				
				
				sheet.setColumnWidth(0, 17500);
				sheet.setColumnWidth(1, 3200);
				sheet.setColumnWidth(2, 3200);
				sheet.setColumnWidth(3, 1800);
				sheet.setColumnWidth(4, 4000);
				sheet.setColumnWidth(5, 4000);
				sheet.setColumnWidth(6, 4000);
				sheet.setColumnWidth(7, 4000);
				rowNum = 0;
				colNum = 0;
				
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				if(org == null) {
					cell.setCellValue("รายงานสรุปผลการใช้จ่ายงบลงทุน" );
				} else {
					cell.setCellValue("รายงานสรุปผลการใช้จ่ายงบลงทุน หน่วยงาน: " + org.getName() );
				}
				cell.setCellStyle(styles.get("title"));
				
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("วันที่รายงาน: " +  df.format(new Date()));
				cell.setCellStyle(styles.get("title"));
			 
			
				row = sheet.createRow(rowNum++);
				Row row2 = sheet.createRow(rowNum++);
			 
			 cell = row.createCell(colNum++);
			 cell.setCellValue("ชื่อครุภัณฑ์-สิ่งก่อสร้าง");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-1, colNum-1, colNum-1));
			 
			 cell = row.createCell(colNum++);
			 cell.setCellValue("หน่วยงานเจ้าของ");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-1, colNum-1, colNum-1));
			 
			 cell = row.createCell(colNum++);
			 cell.setCellValue("หน่วยงานดำเนินการ");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-1, colNum-1, colNum-1));
			 
			 cell = row.createCell(colNum++);
			 cell.setCellValue("จำนวน");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-1, colNum-1, colNum-1));
			 
			 cell = row.createCell(colNum++);
			 cell.setCellValue("งบต่อหน่วย");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-1, colNum-1, colNum-1));
			 
			 cell = row.createCell(colNum++);
			 cell.setCellValue("รวมงบได้รับ");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-1, colNum-1, colNum-1));
			 
			 cell = row.createCell(colNum);
			 cell.setCellValue("งบที่ทำสัญญา (แผน)");
			 cell.setCellStyle(styles.get("header"));
			 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-2, colNum, colNum+1));
			 
			 cell = row2.createCell(colNum++);
			 cell.setCellValue("แผน");
			 cell.setCellStyle(styles.get("header"));
			 
			 cell = row2.createCell(colNum++);
			 cell.setCellValue("ผล");
			 cell.setCellStyle(styles.get("header"));
			 
			 
			 //logger.debug(method.getName() + " :" + method.getSteps().size());
			 for(AssetMethodStep step : method.getSteps()) {
				 cell = row.createCell(colNum);
				 cell.setCellValue(step.getName() + "เริ่ม");
				 cell.setCellStyle(styles.get("header"));
				 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-2, colNum, colNum+1));
				 
				 cell = row2.createCell(colNum++);
				 cell.setCellValue("แผน");
				 cell.setCellStyle(styles.get("header"));
				 sheet.setColumnWidth(colNum-1, 2500);
				 
				 cell = row2.createCell(colNum++);
				 cell.setCellValue("ผล");
				 cell.setCellStyle(styles.get("header"));
				 sheet.setColumnWidth(colNum-1, 2500);
				 
				 cell = row.createCell(colNum);
				 cell.setCellValue(step.getName() + "สิ้นสุด");
				 cell.setCellStyle(styles.get("header"));
				 sheet.addMergedRegion(new CellRangeAddress(rowNum-2, rowNum-2, colNum, colNum+1));
				 
				 cell = row2.createCell(colNum++);
				 cell.setCellValue("แผน");
				 cell.setCellStyle(styles.get("header"));
				 sheet.setColumnWidth(colNum-1, 2500);
				 
				 cell = row2.createCell(colNum++);
				 cell.setCellValue("ผล");
				 cell.setCellStyle(styles.get("header"));
				 sheet.setColumnWidth(colNum-1, 2500);
			 }
			 
			
			 
			 for(AssetAllocation alloc : assetMap.get(method)) {
				 colNum = 0;
					row = sheet.createRow(rowNum++);
					
					cell = row.createCell(colNum++);
					cell.setCellValue(alloc.getAssetBudget().getName());
					cell.setCellStyle(styles.get("cellleft"));
					
					cell = row.createCell(colNum++);
					if(alloc.getOwner() == null) {
						cell.setCellValue("ยังไม่ได้ระบุ");
					} else {
						cell.setCellValue(alloc.getOwner().getAbbr());
					}
					cell.setCellStyle(styles.get("cellleft"));
					
					cell = row.createCell(colNum++);
					
					if(alloc.getOperator() == null) {
						cell.setCellValue("ยังไม่ได้ระบุ");
					} else {
						cell.setCellValue(alloc.getOperator().getAbbr());
					}
					cell.setCellStyle(styles.get("cellleft"));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(alloc.getQuantity());
					cell.setCellStyle(styles.get("cellcenter"));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(alloc.getUnitBudget());
					cell.setCellStyle(styles.get("cellnumber"));
					
					cell = row.createCell(colNum++);
					cell.setCellValue(alloc.getUnitBudget() * alloc.getQuantity());
					cell.setCellStyle(styles.get("cellnumber"));
					
					cell = row.createCell(colNum++);
					if(alloc.getContractedBudgetPlan() != null) {
						cell.setCellValue(alloc.getContractedBudgetPlan());
						cell.setCellStyle(styles.get("cellnumber"));
					}
					
					cell = row.createCell(colNum++);
					if(alloc.getContractedBudgetActual() != null) {
						cell.setCellValue(alloc.getContractedBudgetActual());
						cell.setCellStyle(styles.get("cellnumber"));
					}
					
					int i = 0;
					for(AssetMethodStep step : method.getSteps()) {
						
						if(alloc.getAssetStepReports().size() <= i) {
						
							cell = row.createCell(colNum++);
							cell.setCellValue("ไม่ระบุ");
							cell.setCellStyle(styles.get("cellcenter"));
							
							cell = row.createCell(colNum++);
							cell.setCellValue("ไม่ระบุ");
							cell.setCellStyle(styles.get("cellcenter"));
							
							cell = row.createCell(colNum++);
							cell.setCellValue("ไม่ระบุ");
							cell.setCellStyle(styles.get("cellcenter"));
							
							cell = row.createCell(colNum++);
							cell.setCellValue("ไม่ระบุ");
							cell.setCellStyle(styles.get("cellcenter"));
							
						} else {
						
							cell = row.createCell(colNum++);
							cell.setCellValue(formatDate(alloc.getAssetStepReports().get(i).getPlanBegin()));
							cell.setCellStyle(styles.get("cellcenter"));
							
							cell = row.createCell(colNum++);
							cell.setCellValue(formatDate(alloc.getAssetStepReports().get(i).getActualBegin()));
							cell.setCellStyle(styles.get("cellcenter"));
							
							cell = row.createCell(colNum++);
							cell.setCellValue(formatDate(alloc.getAssetStepReports().get(i).getPlanEnd()));
							cell.setCellStyle(styles.get("cellcenter"));
							
							cell = row.createCell(colNum++);
							cell.setCellValue(formatDate(alloc.getAssetStepReports().get(i).getActualEnd()));
							cell.setCellStyle(styles.get("cellcenter"));
							
						}
						//logger.debug("i: " + i);
						i++;
					}
					
					
				}
			}
		}
	}
	
	
    private String formatDate(Date date) {
    	
    	if(date == null) {
    		return "ไม่ระบุ";
    	}
    	
		return sdf.format(date);
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
        style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFont(cellFont);
        styles.put("cellRoseColor", style);

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
        
        return styles;
    }

}
