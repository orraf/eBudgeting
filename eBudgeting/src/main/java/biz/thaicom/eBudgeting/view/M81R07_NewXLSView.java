package biz.thaicom.eBudgeting.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.thaicom.eBudgeting.models.bgt.BudgetProposal;
import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R07_NewXLSView extends AbstractPOIExcelView {

	public static Logger logger = LoggerFactory.getLogger(M81R07_NewXLSView.class);

	private static HashMap<String, CellStyle> styles;
	
	private Hashtable<Long, Double> budgetPlanTable = new Hashtable<Long, Double>();
	private Hashtable<Long, Double> budgetResultTable = new Hashtable<Long, Double>();
	private Hashtable<String, Double> budgetUsedTable = new Hashtable<String, Double>();

	private Integer col_ลำดับที่;

	private Integer col_ชื่อ;

	private Integer col_รหัส;

	private Integer col_การจัดสรรงบประมาณ;

	private Integer col_แผนการใช้เงิน;

	private Integer col_ผลการใช้เงิน;
	
	
	@Override
	protected Workbook createWorkbook() {
		return new HSSFWorkbook();
	}

	private Double getSumBudgetPlan(Objective obj) {
		Double sum =0.0;
		if(obj.getChildren() == null || obj.getChildren().size() == 0) {
			if(budgetPlanTable.get(obj.getId()) != null) {
				return budgetPlanTable.get(obj.getId());
			}
		} else {
			for(Objective child: obj.getChildren()) {
				sum += getSumBudgetPlan(child);
			}
		}
		return sum;
	}
	
	private Double getSumBudgetResult(Objective obj) {
		Double sum =0.0;
		if(obj.getChildren() == null || obj.getChildren().size() == 0) {
			if(budgetResultTable.get(obj.getId()) != null) {
				return budgetResultTable.get(obj.getId());
			}
		} else {
			for(Objective child: obj.getChildren()) {
				sum += getSumBudgetResult(child);
			}
		}
		return sum;
	}
	
	private Double getSumBudgetUsed(Objective obj) {
		Double sum =0.0;
		if(obj.getChildren() == null || obj.getChildren().size() == 0) {
			if(budgetUsedTable.get(obj.getCode()) != null) {
				return budgetUsedTable.get(obj.getCode());
			}
		} else {
			for(Objective child: obj.getChildren()) {
				sum += getSumBudgetUsed(child);
			}
		}
		return sum;
	}
	
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ThaicomUserDetail currentUser = (ThaicomUserDetail) model.get("currentUser");
		
		Objective rootObjective = (Objective) model.get("rootObjective");

		@SuppressWarnings("unchecked")
		Iterable<Object[]> sumBudgetPlans = (Iterable<Object[]>) model.get("sumBudgetPlans");
		// now put this in hashtable
		
	
		
		
		for(Object[] sumBudgetPlan : sumBudgetPlans) {
			budgetPlanTable.put((Long) sumBudgetPlan[0],
					(Double) sumBudgetPlan[1]); 
		}
		
		@SuppressWarnings("unchecked")
		Iterable<Object[]> sumMonthlyBudgets = (Iterable<Object[]>) model.get("sumMonthlyBudgets");
		// now put this in hashtable
		for(Object[] sumMonthlyBudget : sumMonthlyBudgets) {
			budgetResultTable.put((Long) sumMonthlyBudget[0],
					(Double) sumMonthlyBudget[1]); 
		}
		
		@SuppressWarnings("unchecked")
		Iterable<Object[]> sumBudgetUseds = (Iterable<Object[]>) model.get("sumBudgetUseds");
		// now put this in hashtable
		for(Object[] sumBudgetUsed : sumBudgetUseds) {
			budgetUsedTable.put((String) sumBudgetUsed[0],
					(Double) sumBudgetUsed[1]); 
		}
		
		
        Map<String, CellStyle> styles = createStyles(workbook);

  		Integer fiscalYear = (Integer) model.get("fiscalYear");
		Sheet sheet = workbook.createSheet("sheet1");

		Integer rowNum = 0;
		Integer colNum = 0;
		
		Row firstRow = sheet.createRow(rowNum++);
		Cell cell0 = firstRow.createCell(0);
		cell0.setCellValue("วันที่พิมพ์รายงาน: " +  printTimeFormat.format(new Date()) );

		firstRow = sheet.createRow(rowNum++);
		
		col_ลำดับที่ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("");
		cell0.setCellStyle(styles.get("header"));
		
		col_ชื่อ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ผลผลิต /โครงการ / แผนปฏิบัติการ/  กิจกรรม");
		cell0.setCellStyle(styles.get("header"));
		
		col_รหัส = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("รหัสงบประมาณ");
		cell0.setCellStyle(styles.get("header"));
		
		col_การจัดสรรงบประมาณ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("การจัดสรรงบประมาณ");
		cell0.setCellStyle(styles.get("header"));
		
		col_แผนการใช้เงิน = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("แผนการใช้เงิน");
		cell0.setCellStyle(styles.get("header"));
		
		col_ผลการใช้เงิน = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ผลการใช้เงิน");
		cell0.setCellStyle(styles.get("header"));
		
		Stack<Objective> objectives = new Stack<Objective>();
		
		Collections.sort(rootObjective.getChildren(), Objective.Comparators.CODE_DESC);
				
		objectives.addAll(rootObjective.getChildren());
		
		List<Objective> obj1 = new ArrayList<Objective>();
		String งบบริหาร_code = fiscalYear.toString().substring(2) + "1";
		List<Objective> obj2 = new ArrayList<Objective>();
		String งบสงเคราะห์_code = fiscalYear.toString().substring(2) + "2";
		
		
		Row row;
		Objective obj;
		
		//Hashtable<Objective, Row> objectiveRowTable = new Hashtable<Objective, Row>();
		
		while(!objectives.isEmpty()) {
			obj = objectives.pop();
			
			if(obj.getType().getId() == 101L) {
				//colNum = col_แผนงาน;
			} else if(obj.getType().getId() == 102L) {
				//colNum = col_ยุทธศาสตร์;
			} else if(obj.getType().getId() == 103L) {
				//colNum = col_ผลผลิต;
			} else if(obj.getType().getId() == 104L) {
				if(obj.getCode().startsWith(งบบริหาร_code)) {
					obj1.add(obj);
				} else {
					obj2.add(obj);
				}
			} else if(obj.getType().getId() == 105L) {
				if(obj.getCode().startsWith(งบบริหาร_code)) {
					obj1.add(obj);
				} else {
					obj2.add(obj);
				}
			} else if(obj.getType().getId() == 106L) {
				if(obj.getCode().startsWith(งบบริหาร_code)) {
					obj1.add(obj);
				} else {
					obj2.add(obj);
				}
			}   
			
			
//			row = sheet.createRow(rowNum++);
//			objectiveRowTable.put(obj, row);
//			if(obj.getType().getId() == 101L) {
//				colNum = col_แผนงาน;
//			} else if(obj.getType().getId() == 102L) {
//				colNum = col_ยุทธศาสตร์;
//			} else if(obj.getType().getId() == 103L) {
//				colNum = col_ผลผลิต;
//			} else if(obj.getType().getId() == 104L) {
//				colNum = col_กิจกรรมหลัก;
//			} else if(obj.getType().getId() == 105L) {
//				colNum = col_แผนปฏิบัติการ;
//			} else if(obj.getType().getId() == 106L) {
//				colNum = col_กิจกรรมรอง;
//			}   
//  
//			
//			if(obj.getType().getId() != 106L) {
//				cell0 = row.createCell(colNum);
//				cell0.setCellValue(obj.getCode() + " " + obj.getName());
//			} else {
//				cell0 = row.createCell(colNum);
//				cell0.setCellValue(obj.getCode());
//				cell0 = row.createCell(col_ชื่อ);
//				cell0.setCellValue(obj.getName());
//			}
//			
//			cell0 = row.createCell(col_การจัดสรรงบประมาณ);
//			cell0.setCellValue(obj.getSumAllocated());
//			cell0.setCellStyle(styles.get("cellnumber"));
//			
//			cell0 = row.createCell(col_แผนการใช้เงิน);
//			cell0.setCellValue(getSumBudgetPlan(obj));
//			cell0.setCellStyle(styles.get("cellnumber"));
//			
//			cell0 = row.createCell(col_ผลการใช้เงิน);
//			cell0.setCellValue(getSumBudgetUsed(obj));
//			cell0.setCellStyle(styles.get("cellnumber"));
			
			if(obj.getChildren() != null) {
				Collections.sort(obj.getChildren(), Objective.Comparators.CODE_DESC);
				objectives.addAll(obj.getChildren());
			}
			
		}
		
		
		// now we can go throgh obj1 and obj2
		row = sheet.createRow(rowNum++);
		cell0 = row.createCell(col_ชื่อ);
		cell0.setCellValue("งบบริหาร");
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_ลำดับที่);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_รหัส);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_การจัดสรรงบประมาณ);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_แผนการใช้เงิน);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_ผลการใช้เงิน);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		Objective ผลผลิต = null;
		for(Objective o : obj1) {
			if(o.getType().getId() == 104L) {
				if(o.getParent() != ผลผลิต) {
					row = sheet.createRow(rowNum++);
					displayInfo_ผลผลิต(o.getParent(), row);
					ผลผลิต = o.getParent();
				}
			}
			row = sheet.createRow(rowNum++);
			displayInfo(o, row);
			
			
		}
		
		row = sheet.createRow(rowNum++);
		cell0 = row.createCell(col_ชื่อ);
		cell0.setCellValue("งบสงเคราะห์");
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_ลำดับที่);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_รหัส);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_การจัดสรรงบประมาณ);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_แผนการใช้เงิน);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		cell0 = row.createCell(col_ผลการใช้เงิน);
		cell0.setCellStyle(styles.get("groupcenter"));
		
		ผลผลิต = null;
		for(Objective o : obj2) {
			if(o.getType().getId() == 104L) {
				if(o.getParent() != ผลผลิต) {
					row = sheet.createRow(rowNum++);
					displayInfo_ผลผลิต(o.getParent(), row);
					ผลผลิต = o.getParent();
				}
			}
			
			row = sheet.createRow(rowNum++);
			displayInfo(o, row);
		}
		
		
		
		sheet.setColumnWidth(col_ลำดับที่, 1000);
//		sheet.setColumnWidth(col_แผนงาน, 3000);
//		sheet.setColumnWidth(col_ยุทธศาสตร์, 3000);
//		sheet.setColumnWidth(col_ผลผลิต, 3000);
//		sheet.setColumnWidth(col_กิจกรรมหลัก, 3000);
//		sheet.setColumnWidth(col_แผนปฏิบัติการ, 3000);
		sheet.setColumnWidth(col_ชื่อ, 20000);
		sheet.setColumnWidth(col_รหัส, 4000);
		
		sheet.setColumnWidth(col_การจัดสรรงบประมาณ, 7000);
		sheet.setColumnWidth(col_แผนการใช้เงิน, 5000);
		sheet.setColumnWidth(col_ผลการใช้เงิน, 5000);
		sheet.createFreezePane( 0, 1 );
	}
	
	
    private void displayInfo_ผลผลิต(Objective o, Row row) {
    	Cell cell0;
    	
    	cell0 = row.createCell(col_ลำดับที่);		
    	cell0.setCellValue(o.getCode());
		cell0.setCellStyle(styles.get("cellleft"));
		
		cell0 = row.createCell(col_รหัส);			
		
		Cell cell1 = row.createCell(col_ชื่อ);
		cell1.setCellValue(o.getName());
		
		// bold
		cell0.setCellStyle(styles.get("cellleftBold"));
		cell1.setCellStyle(styles.get("cellleftBold"));
		
		cell0 = row.createCell(col_การจัดสรรงบประมาณ);
//		cell0.setCellValue(o.getSumAllocated());
		cell0.setCellStyle(styles.get("cellnumber"));
		
		cell0 = row.createCell(col_แผนการใช้เงิน);
//		cell0.setCellValue(getSumBudgetPlan(o));
		cell0.setCellStyle(styles.get("cellnumber"));
		
		cell0 = row.createCell(col_ผลการใช้เงิน);
//		cell0.setCellValue(getSumBudgetUsed(o));
		cell0.setCellStyle(styles.get("cellnumber"));
		
	}

	private void displayInfo(Objective o, Row row) {
    	Cell cell0;
    	
    	cell0 = row.createCell(col_ลำดับที่);			
		cell0.setCellStyle(styles.get("cellleft"));
		
		cell0 = row.createCell(col_รหัส);			
		cell0.setCellValue(o.getCode());
		
		Cell cell1 = row.createCell(col_ชื่อ);
		cell1.setCellValue(o.getName());
		
		if(o.getType().getId() == 106L) {
			// don't bold
			cell0.setCellStyle(styles.get("cellleft"));
			cell1.setCellStyle(styles.get("cellleft"));
		} else {
			// bold
			cell0.setCellStyle(styles.get("cellleftBold"));
			cell1.setCellStyle(styles.get("cellleftBold"));
		}   

		
		
		cell0 = row.createCell(col_การจัดสรรงบประมาณ);
		cell0.setCellValue(o.getSumAllocated());
		cell0.setCellStyle(styles.get("cellnumber"));
		
		cell0 = row.createCell(col_แผนการใช้เงิน);
		cell0.setCellValue(getSumBudgetPlan(o));
		cell0.setCellStyle(styles.get("cellnumber"));
		
		cell0 = row.createCell(col_ผลการใช้เงิน);
		cell0.setCellValue(getSumBudgetUsed(o));
		cell0.setCellStyle(styles.get("cellnumber"));
		
	}

	private static Map<String, CellStyle> createStyles(Workbook wb){
        styles = new HashMap<String, CellStyle>();

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
//        groupFont.setFontHeightInPoints((short)11);
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
        styles.put("groupcenter", style);

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
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
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
        styles.put("cellleftBold", style);
        
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
