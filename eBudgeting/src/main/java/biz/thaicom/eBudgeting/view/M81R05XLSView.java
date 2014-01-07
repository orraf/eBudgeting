package biz.thaicom.eBudgeting.view;

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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Activity;
import biz.thaicom.eBudgeting.models.pln.ActivityTarget;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.eBudgeting.models.pln.TargetUnit;

public class M81R05XLSView extends AbstractPOIExcelView {

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
		List<Objective> objectives = (List<Objective>) model.get("objectives");

		Organization org = (Organization) model.get("organization");
		Integer fiscalYear = (Integer) model.get("fiscalYear"); 
		
		
		
		Row row = null;
		Cell cell = null;
		
		for(Objective ผลผลิต: objectives) {
			String sheetName = WorkbookUtil.createSafeSheetName("ยุทธ์ " + ผลผลิต.getParent().getCode() + " - ผลผลิต " + ผลผลิต.getCode());
			
			Sheet sheet = workbook.createSheet(sheetName);
			sheet.setColumnWidth(0, 7500);
			sheet.setColumnWidth(1, 15000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setDefaultColumnStyle(0, styles.get("cellleft"));
			sheet.setDefaultColumnStyle(1, styles.get("cellleft"));
			sheet.setDefaultColumnStyle(2, styles.get("numbercenter"));
			sheet.setDefaultColumnStyle(3, styles.get("cellcenter"));
			sheet.setDefaultColumnStyle(4, styles.get("numbercenter"));
			sheet.setDefaultColumnStyle(5, styles.get("cellcenter"));
			
			
			int rowNum = 0;
			
			Row firstRow = sheet.createRow(rowNum++);
			Cell cell0 = firstRow.createCell(0);
			cell0.setCellValue("วันที่พิมพ์รายงาน: " +  printTimeFormat.format(new Date()) );
			if(org!=null) {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("รายงานทะเบียนแผนปฏิบัติการและกิจกรรมสำหรับ" + org.getName() + " ประจำปีงบประมาณ " + fiscalYear );
					
			} else {
				row = sheet.createRow(rowNum++);
				cell = row.createCell(0);
				cell.setCellValue("รายงานทะเบียนแผนปฏิบัติการและกิจกรรม ประจำปีงบประมาณ " + fiscalYear);
				
			}
			
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue("ยุทธ์ศาสตร์ที่ " + ผลผลิต.getParent().getCode() + " - " + ผลผลิต.getName());
			
			row = sheet.createRow(rowNum++);
			cell = row.createCell(0);
			cell.setCellValue("ชื่อแผน/กิจกรรม");
			cell = row.createCell(1);
			
			cell = row.createCell(2);
			cell.setCellValue("เป้าหมาย");
			cell = row.createCell(3);
			cell.setCellValue("หน่วยนับ");
			cell = row.createCell(4);
			cell.setCellValue("เป้าหมายที่จัดสรรแล้ว");
			cell = row.createCell(5);
			cell.setCellValue("หน่วยนับ");
			
			for(Objective กิจกรรมหลัก : ผลผลิต.getChildren()) {
				if(กิจกรรมหลัก.getShowInTree()) {
					row = sheet.createRow(rowNum++);
					
					cell = row.createCell(0);
					cell.setCellValue("["+กิจกรรมหลัก.getCode()+"] " + กิจกรรมหลัก.getName());
					
					for(Objective แผนปฏิบัติการ : กิจกรรมหลัก.getChildren()) {
						if(แผนปฏิบัติการ.getShowInTree()) {
							sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
							row = sheet.createRow(rowNum++);
							
							cell = row.createCell(0);
							cell.setCellValue("        ["+แผนปฏิบัติการ.getCode()+"] " + แผนปฏิบัติการ.getName());
							cell.setCellStyle(styles.get("groupleft"));
							
							if(แผนปฏิบัติการ.getOwner() != null) {
								cell = row.createCell(2);
								String ownerString = "";
								
								for(Organization owner : แผนปฏิบัติการ.getOwner()) {
									if(ownerString.length() == 0 ) {
										ownerString += owner.getAbbr();	
									} else {
										ownerString += " / " + owner.getAbbr();
									}
									
								}
								cell.setCellValue(ownerString);
								cell.setCellStyle(styles.get("groupleft"));
							}
							
							
							
							for(Objective กิจกรรมรอง : แผนปฏิบัติการ.getChildren()) {
								sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
								row = sheet.createRow(rowNum++);
								
								cell = row.createCell(0);
								cell.setCellValue("                ["+กิจกรรมรอง.getCode()+"] " + กิจกรรมรอง.getName());
								
								if(กิจกรรมรอง.getFilterActivities() != null ) {
								
									if(กิจกรรมรอง.getFilterActivities().size() == 0) {
										row = sheet.createRow(rowNum++);
										
										cell = row.createCell(0);
										cell = row.createCell(1);
										cell.setCellValue("ยังไม่ได้สร้างกิจกรรมย่อย");
										cell.setCellStyle(styles.get("cellRoseColor"));
									} else {
									
										for(Activity กิจกรรมย่อย : กิจกรรมรอง.getFilterActivities() ) {
											row = sheet.createRow(rowNum++);
											
											cell = row.createCell(0);
											cell = row.createCell(1);
											cell.setCellValue("[" + กิจกรรมย่อย.getCode()+"]" + กิจกรรมย่อย.getName());
											
											
											if(กิจกรรมย่อย.getTargets().size() > 0) {
												int i = 0;
												for(ActivityTarget target: กิจกรรมย่อย.getTargets()) {
													if( i>0 ) {
														row = sheet.createRow(rowNum++);
													}
													cell = row.createCell(2);
													cell.setCellValue(target.getTargetValue());
													
													cell = row.createCell(3);
													cell.setCellValue(target.getUnit().getName());
													
													// now จัดสรร แล้ว
													
													cell = row.createCell(4);
													cell.setCellValue(target.getAllocatedTargetValue());
													
													cell = row.createCell(5);
													cell.setCellValue(target.getUnit().getName());
													
													
													
													i++;
													
												}
												
											}
											
											
											for(Activity กิจกรรมเสริม : กิจกรรมย่อย.getChildren()) {
												row = sheet.createRow(rowNum++);
												
												cell = row.createCell(0);
												cell = row.createCell(1);
												cell.setCellValue("          [" + กิจกรรมเสริม.getCode()+"]" + กิจกรรมเสริม.getName());
												
												
												if(กิจกรรมเสริม.getTargets().size() > 0) {
													int i = 0;
													for(ActivityTarget target: กิจกรรมเสริม.getTargets()) {
														if( i>0 ) {
															row = sheet.createRow(rowNum++);
														}
														cell = row.createCell(2);
														cell.setCellValue(target.getTargetValue());
														
														cell = row.createCell(3);
														cell.setCellValue(target.getUnit().getName());
														
														// now จัดสรร แล้ว
														
														cell = row.createCell(4);
														cell.setCellValue(target.getAllocatedTargetValue());
														
														cell = row.createCell(5);
														cell.setCellValue(target.getUnit().getName());
														
														i++;
														
													}
												}
													
												
												for(Activity กิจกรรมสนับสนุน : กิจกรรมเสริม.getChildren()) {
													row = sheet.createRow(rowNum++);
													
													cell = row.createCell(0);
													cell = row.createCell(1);
													cell.setCellValue("          [" + กิจกรรมสนับสนุน.getCode()+"]" + กิจกรรมสนับสนุน.getName());
													
													
													if(กิจกรรมสนับสนุน.getTargets().size() > 0) {
														int i = 0;
														for(ActivityTarget target: กิจกรรมเสริม.getTargets()) {
															if( i>0 ) {
																row = sheet.createRow(rowNum++);
															}
															cell = row.createCell(2);
															cell.setCellValue(target.getTargetValue());
															
															cell = row.createCell(3);
															cell.setCellValue(target.getUnit().getName());
															
															// now จัดสรร แล้ว
															
															cell = row.createCell(4);
															cell.setCellValue(target.getAllocatedTargetValue());
															
															cell = row.createCell(5);
															cell.setCellValue(target.getUnit().getName());
															
															i++;
															
														}
													}
													
												}
												
											}
											
										}
									}
								}
								
							}
						}
					}
				}
			} // forloop กิจกรรมหลัก
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
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setDataFormat(format.getFormat("#,##0"));
        style.setFont(cellFont);
        styles.put("number", style);
        
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setDataFormat(format.getFormat("#,##0"));
        style.setFont(cellFont);
        styles.put("numbercenter", style);
        
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFont(cellFont);
        styles.put("cellcenter", style);
        
       
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
