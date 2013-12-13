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
	
	private Hashtable<Long, Double> budgetPlanTable = new Hashtable<Long, Double>();
	private Hashtable<Long, Double> budgetResultTable = new Hashtable<Long, Double>();
	private Hashtable<String, Double> budgetUsedTable = new Hashtable<String, Double>();
	
	
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
		
		Integer col_ปีงบประมาณ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ปีงบประมาณ");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_แผนงาน = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("แผนงาน");
		cell0.setCellStyle(styles.get("header"));

		Integer col_ยุทธศาสตร์ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ยุทธศาสตร์");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_ผลผลิต = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ผลผลิต/โครงการ");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_กิจกรรมหลัก = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("กิจกรรมหลัก");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_แผนปฏิบัติการ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("แผนปฏิบัติการ");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_กิจกรรมรอง = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("กิจกรรมรอง");
		cell0.setCellStyle(styles.get("header"));

		Integer col_ชื่อ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ชื่อ");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_การจัดสรรงบประมาณ = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("การจัดสรรงบประมาณ");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_แผนการใช้เงิน = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("แผนการใช้เงิน");
		cell0.setCellStyle(styles.get("header"));
		
		Integer col_ผลการใช้เงิน = colNum;
		cell0 = firstRow.createCell(colNum++);
		cell0.setCellValue("ผลการใช้เงิน");
		cell0.setCellStyle(styles.get("header"));
		
		Stack<Objective> objectives = new Stack<Objective>();
		
		Collections.sort(rootObjective.getChildren(), Objective.Comparators.CODE_DESC);
				
		objectives.addAll(rootObjective.getChildren());
		
		
		
		
		Row row;
		// do the first row!
		row = sheet.createRow(rowNum++);
		cell0 = row.createCell(col_ปีงบประมาณ);
		cell0.setCellValue("ปีงบประมาณ " + fiscalYear);
		cell0 = row.createCell(col_การจัดสรรงบประมาณ);
		cell0.setCellValue(rootObjective.getSumAllocated());
		cell0.setCellStyle(styles.get("cellnumber"));
		
		cell0 = row.createCell(col_แผนการใช้เงิน);
		cell0.setCellValue(getSumBudgetPlan(rootObjective));
		cell0.setCellStyle(styles.get("cellnumber"));
		
		cell0 = row.createCell(col_ผลการใช้เงิน);
		cell0.setCellValue(getSumBudgetUsed(rootObjective));
		cell0.setCellStyle(styles.get("cellnumber"));
		
		
		Objective obj;
		
		Hashtable<Objective, Row> objectiveRowTable = new Hashtable<Objective, Row>();
		
		while(!objectives.isEmpty()) {
			obj = objectives.pop();
			
			row = sheet.createRow(rowNum++);
			objectiveRowTable.put(obj, row);
			if(obj.getType().getId() == 101L) {
				colNum = col_แผนงาน;
			} else if(obj.getType().getId() == 102L) {
				colNum = col_ยุทธศาสตร์;
			} else if(obj.getType().getId() == 103L) {
				colNum = col_ผลผลิต;
			} else if(obj.getType().getId() == 104L) {
				colNum = col_กิจกรรมหลัก;
			} else if(obj.getType().getId() == 105L) {
				colNum = col_แผนปฏิบัติการ;
			} else if(obj.getType().getId() == 106L) {
				colNum = col_กิจกรรมรอง;
			}   
  
			
			if(obj.getType().getId() != 106L) {
				cell0 = row.createCell(colNum);
				cell0.setCellValue(obj.getCode() + " " + obj.getName());
			} else {
				cell0 = row.createCell(colNum);
				cell0.setCellValue(obj.getCode());
				cell0 = row.createCell(col_ชื่อ);
				cell0.setCellValue(obj.getName());
			}
			
			cell0 = row.createCell(col_การจัดสรรงบประมาณ);
			cell0.setCellValue(obj.getSumAllocated());
			cell0.setCellStyle(styles.get("cellnumber"));
			
			cell0 = row.createCell(col_แผนการใช้เงิน);
			cell0.setCellValue(getSumBudgetPlan(obj));
			cell0.setCellStyle(styles.get("cellnumber"));
			
			cell0 = row.createCell(col_ผลการใช้เงิน);
			cell0.setCellValue(getSumBudgetUsed(obj));
			cell0.setCellStyle(styles.get("cellnumber"));
			
			if(obj.getChildren() != null) {
				Collections.sort(obj.getChildren(), Objective.Comparators.CODE_DESC);
				objectives.addAll(obj.getChildren());
			}
			
		}
		
		
		
		
//		Connection connection = dataSource.getConnection();
//				
//		PreparedStatement ps = null;
//		Statement st = connection.createStatement();
//		String rsSQL = "select substr(code,1,2) fyear, substr(code,1,3) fin_type, substr(code,3,1) type, substr(code,1,5) prod_type, substr(code,1,7) plan_code, code, name name, parent_pln_objective_id parentId " +
//				   "from pln_objective " +
//				   "where fiscalyear = " + fiscalYear + " " +
//				  // "and substr(code,1,2) = '" + fiscalYear.toString().substring(2, 4) + "' " +
//				   " and type_pln_objectivetype_id = 106 " + 
//				   // "and length(code) = 9 " +
//				   // "group by code " +
//				   "order by code ";
//		
//		logger.debug(rsSQL);
//		ResultSet rs = st.executeQuery(rsSQL);
//		
//
//		int i = 2;
//		String fYear = " ";
//		String fType = " ";
//		String pType = " ";
//		String plan = " ";
//		String name = "";
		
//		while (rs.next()) {
//			Row rows = sheet.createRow(i);
//			Cell rsc0 = rows.createCell(0);
//			rsc0.setCellStyle(styles.get("cellcenter"));
//			Cell rsc1 = rows.createCell(1);
//			rsc1.setCellStyle(styles.get("cellcenter"));
//			Cell rsc2 = rows.createCell(2);
//			rsc2.setCellStyle(styles.get("cellcenter"));
//			Cell rsc3 = rows.createCell(3);
//			rsc3.setCellStyle(styles.get("cellcenter"));
//			Cell rsc4 = rows.createCell(4);
//			rsc4.setCellStyle(styles.get("cellcenter"));
//			Cell rsc5 = rows.createCell(5);
//			rsc5.setCellStyle(styles.get("cellleft"));
//			Cell rsc6 = rows.createCell(6);
//			rsc6.setCellStyle(styles.get("cellnumber"));
//			Cell rsc7 = rows.createCell(7);
//			rsc7.setCellStyle(styles.get("cellnumber"));
//			Cell rsc8 = rows.createCell(8);
//			rsc8.setCellStyle(styles.get("cellnumber"));
//			
//			if (!fYear.equals(rs.getString(1))) {
//				rsc0.setCellValue(rs.getString(1));
//				rsc5.setCellValue("ปีงบประมาณ " + fiscalYear);
//				rsc5.setCellStyle(styles.get("cellcenter"));
//				fYear = rs.getString(1);
//				i = i + 1;
//				rows = sheet.createRow(i);
//				rsc0 = rows.createCell(0);
//				rsc0.setCellStyle(styles.get("cellcenter"));
//				rsc1 = rows.createCell(1);
//				rsc1.setCellStyle(styles.get("cellcenter"));
//				rsc2 = rows.createCell(2);
//				rsc2.setCellStyle(styles.get("cellcenter"));
//				rsc3 = rows.createCell(3);
//				rsc3.setCellStyle(styles.get("cellcenter"));
//				rsc4 = rows.createCell(4);
//				rsc4.setCellStyle(styles.get("cellcenter"));
//				rsc5 = rows.createCell(5);
//				rsc5.setCellStyle(styles.get("cellleft"));
//				rsc6 = rows.createCell(6);
//				rsc6.setCellStyle(styles.get("cellnumber"));
//				rsc7 = rows.createCell(7);
//				rsc7.setCellStyle(styles.get("cellnumber"));
//				rsc8 = rows.createCell(8);
//				rsc8.setCellStyle(styles.get("cellnumber"));
//			}
//			
//			if (!fType.equals(rs.getString(2))) {
//				rsc1.setCellValue(rs.getString(2));
//				if (rs.getString(3).equals("1")) rsc5.setCellValue("งบบริหาร");
//				else rsc5.setCellValue("งบสงเคราะห์");
//				rsc5.setCellStyle(styles.get("cellcenter"));
//				fType = rs.getString(2);
//				i = i + 1;
//				rows = sheet.createRow(i);
//				rsc0 = rows.createCell(0);
//				rsc0.setCellStyle(styles.get("cellcenter"));
//				rsc1 = rows.createCell(1);
//				rsc1.setCellStyle(styles.get("cellcenter"));
//				rsc2 = rows.createCell(2);
//				rsc2.setCellStyle(styles.get("cellcenter"));
//				rsc3 = rows.createCell(3);
//				rsc3.setCellStyle(styles.get("cellcenter"));
//				rsc4 = rows.createCell(4);
//				rsc4.setCellStyle(styles.get("cellcenter"));
//				rsc5 = rows.createCell(5);
//				rsc5.setCellStyle(styles.get("cellleft"));
//				rsc6 = rows.createCell(6);
//				rsc6.setCellStyle(styles.get("cellnumber"));
//				rsc7 = rows.createCell(7);
//				rsc7.setCellStyle(styles.get("cellnumber"));
//				rsc8 = rows.createCell(8);
//				rsc8.setCellStyle(styles.get("cellnumber"));
//			}
//			
//			if (!pType.equals(rs.getString(4))) {
//				rsc2.setCellValue(rs.getString(4));
//				pType = rs.getString(4);
//
//				Statement st1 = connection.createStatement();
//				String rs1SQL = "select name, id " +
//					     "from pln_objective " +
//					     "where fiscalyear = " + fiscalYear + " " +
//					     "and code = '" + pType + "' and type_pln_objectivetype_id = 103 " +
//					     "order by parentlevel ";
//				
//				logger.debug(rs1SQL);
//				ResultSet rs1 = st1.executeQuery(rs1SQL);
//				
//				Long id = null;
//				ArrayList<Long> idList = new ArrayList<Long>();
//				
//				name = "";
//				while (rs1.next()) {
//					name = name + rs1.getString(1) + "  ";
//					id=rs1.getLong(2);
//					idList.add(id);
//				}
//				
//				String idString = "(";
//				String idLike = " (";
//				
//				if(idList.size() == 0) {
//					idString = "( null )";
//					idLike = "( 1 = 1)";
//				}
//				
//				for(int k=0; k<idList.size(); k++) {
//					idString += idList.get(k);
//					idLike += "t3.parentpath like '%."+ idList.get(k) + ".%' ";
//					if(k != idList.size()-1) {
//						// not the last one
//						idString += ",";
//						idLike += " OR ";
//					} else {
//						idString += ")";
//						idLike += ") ";
//					}
//				}
//				
//				
//				rsc5.setCellValue(name);
//				logger.debug("-------------> rs1SLQ  RESULT: " + name);
//				rs1.close();
//				st1.close();
//
//				Statement st2 = connection.createStatement();
//				String st2SQL = "select sum(amountallocated) " +
//						 "from bgt_allocationrecord t1, pln_objective t2 " +
//						 "where t1.objective_pln_objective_id = t2.id " +
//						 //"and t2.code = '" + pType + "' ";
//						 " and t2.id in " + idString;
//				logger.debug(st2SQL);
//				ResultSet rs2 = st2.executeQuery(st2SQL );
//				
//				
//				rs2.next();
//				rsc6.setCellValue(rs2.getDouble(1));
//				rsc6.setCellStyle(styles.get("summarynumber"));
//				rs2.close();
//				st2.close();
//				
//				Statement st3 = connection.createStatement();
//				/**
//				String st3SQL = ""
//						+ "select sum(t2.budgetallocated) target "
//						+ "from pln_activity t1, pln_activityperformance t2,  "
//						+ "	(select id "
//						+ "		from pln_objective "
//						+ "     connect by prior id  = PARENT_PLN_OBJECTIVE_ID "
//						+ "		start with id in " + idString + ") t3 "
//						+ "where t1.id = t2.activity_pln_activity_id "
//						+ "      and t1.obj_pln_objective_id = t3.id ";
//				**/
//				
//				String st3SQL = ""
//						+ "SELECT sum(p1.sumallocated) FROM ("
//						+ "SELECT t3.id objectiveid, t3.parent_pln_objective_id parent_id, t3.parentpath, t3.name objectivename, sum(t2.budgetallocated) sumallocated "
//						+ "FROM pln_activity t1, pln_activityperformance t2, pln_objective t3 "
//						+ "WHERE t2.activity_pln_activity_id = t1.id " //and t1.parent_pln_activity_id is null "
//						+ "	and t3.id = t1.obj_pln_objective_id and " + idLike 
//						+ "GROUP BY t3.id, t3.name, t3.parent_pln_objective_id, t3.parentpath"
//						+ ") p1 ";
//				logger.debug("--- st3SQL: " );
//				logger.debug(st3SQL);
//				
//				ResultSet rs3 = st3.executeQuery(st3SQL);
//				
//				if(rs3.next()) {
//				
//					logger.debug("st3SQL result: " + rs3.getDouble(1));
//					rsc7.setCellValue(rs3.getDouble(1));
//					rsc7.setCellStyle(styles.get("summarynumber"));
//				} else {
//					rsc7.setCellValue(0.0);
//					rsc7.setCellStyle(styles.get("summarynumber"));
//				}
//				rs3.close();
//				st3.close();
//				
//				Statement st4 = connection.createStatement();
//				ResultSet rs4 = st4.executeQuery("select sum(amt) " +
//											     "from v_gl " +
//											     "where fiscal_year = " + fiscalYear + " " +
//											     "and substr(gl_trans_plan,1,5) = '" + pType + "'");
//				
//				rs4.next();
//				rsc8.setCellValue(rs4.getDouble(1));
//				rsc8.setCellStyle(styles.get("summarynumber"));
//				rs4.close();
//				st4.close();
//
//				i = i + 1;
//				rows = sheet.createRow(i);
//				rsc0 = rows.createCell(0);
//				rsc0.setCellStyle(styles.get("cellcenter"));
//				rsc1 = rows.createCell(1);
//				rsc1.setCellStyle(styles.get("cellcenter"));
//				rsc2 = rows.createCell(2);
//				rsc2.setCellStyle(styles.get("cellcenter"));
//				rsc3 = rows.createCell(3);
//				rsc3.setCellStyle(styles.get("cellcenter"));
//				rsc4 = rows.createCell(4);
//				rsc4.setCellStyle(styles.get("cellcenter"));
//				rsc5 = rows.createCell(5);
//				rsc5.setCellStyle(styles.get("cellleft"));
//				rsc6 = rows.createCell(6);
//				rsc6.setCellStyle(styles.get("cellnumber"));
//				rsc7 = rows.createCell(7);
//				rsc7.setCellStyle(styles.get("cellnumber"));
//				rsc8 = rows.createCell(8);
//				rsc8.setCellStyle(styles.get("cellnumber"));
//			}
//	
//			if (!plan.equals(rs.getString(5))) {
//				rsc3.setCellValue(rs.getString(5));
//				plan = rs.getString(5);
//
//				Statement st1 = connection.createStatement();
//				ResultSet rs1 = st1.executeQuery("select name " +
//											     "from pln_objective " +
//											     "where fiscalyear = " + fiscalYear + " " +
//											     "and code = '" + plan + "' " +
//											     "order by parentlevel ");
//				
//				name = "";
//				while (rs1.next()) {
//					name = name + rs1.getString(1) + "  ";
//				}
//				rsc5.setCellValue(name);
//				rs1.close();
//				st1.close();
//
//				Statement st2 = connection.createStatement();
//				ResultSet rs2 = st2.executeQuery("select sum(amountallocated) " +
//												 "from bgt_allocationrecord t1, pln_objective t2 " +
//												 "where t1.objective_pln_objective_id = t2.id " +
//												 "and t2.code = '" + plan + "' " );
//				
//				rs2.next();
//				rsc6.setCellValue(rs2.getDouble(1));
//				rsc6.setCellStyle(styles.get("summarynumber"));
//				rs2.close();
//				st2.close();
//				
//				Statement st3 = connection.createStatement();
//				ResultSet rs3 = st3.executeQuery("select sum(t2.budgetallocated) target " +
//												 "from pln_activity t1, pln_activityperformance t2,  " +
//													 "(select id from pln_objective " +
//									                     "connect by prior id  = PARENT_PLN_OBJECTIVE_ID " +
//									                     "start with code = '" + plan + "') t3 " +			
//												 "where t1.id = t2.activity_pln_activity_id " +
//												 "and t1.obj_pln_objective_id = t3.id ");
//				
//				rs3.next();
//				rsc7.setCellValue(rs3.getDouble(1));
//				rsc7.setCellStyle(styles.get("summarynumber"));
//				rs3.close();
//				st3.close();
//				
//				Statement st4 = connection.createStatement();
//				String st4SQL = ""
//						+ "select sum(amt) "
//						+ "from v_gl t1, pln_objective t2 "
//						+ "where t1.fiscal_year = " + fiscalYear + " "
//						+ "	and t1.gl_trans_plan = '" + plan + "'" 
//						+ " and t1.activitycode = t2.code ";
//				logger.debug("st4SQL:");
//				logger.debug(st4SQL);
//				ResultSet rs4 = st4.executeQuery(st4SQL);
//				
//				rs4.next();
//				logger.debug("rs4 result: " + rs4.getDouble(1));
//				rsc8.setCellValue(rs4.getDouble(1));
//				rsc8.setCellStyle(styles.get("summarynumber"));
//				rs4.close();
//				st4.close();
//
//				i = i + 1;
//				rows = sheet.createRow(i);
//				rsc0 = rows.createCell(0);
//				rsc0.setCellStyle(styles.get("cellcenter"));
//				rsc1 = rows.createCell(1);
//				rsc1.setCellStyle(styles.get("cellcenter"));
//				rsc2 = rows.createCell(2);
//				rsc2.setCellStyle(styles.get("cellcenter"));
//				rsc3 = rows.createCell(3);
//				rsc3.setCellStyle(styles.get("cellcenter"));
//				rsc4 = rows.createCell(4);
//				rsc4.setCellStyle(styles.get("cellcenter"));
//				rsc5 = rows.createCell(5);
//				rsc5.setCellStyle(styles.get("cellleft"));
//				rsc6 = rows.createCell(6);
//				rsc6.setCellStyle(styles.get("cellnumber"));
//				rsc7 = rows.createCell(7);
//				rsc7.setCellStyle(styles.get("cellnumber"));
//				rsc8 = rows.createCell(8);
//				rsc8.setCellStyle(styles.get("cellnumber"));
//			}
//	
//			rsc4.setCellValue(rs.getString(6));
//			rsc5.setCellValue(rs.getString(7));
//
//			Statement st2 = connection.createStatement();
//			String rs2SQL = "select sum(t2.budgetallocated) target "
//					+ "from pln_activity t1, pln_activityperformance t2,  "
//					+ "		(select id from pln_objective "
//					+ "			connect by prior id  = PARENT_PLN_OBJECTIVE_ID "
//					+ "				start with code = " + rs.getString(6) + ") t3 "
//					+ "where t1.id = t2.activity_pln_activity_id "
//					+ "and t1.obj_pln_objective_id = t3.id ";
//			ResultSet rs2 = st2.executeQuery(rs2SQL);
//			
//			logger.debug("rs2SQL: ");
//			logger.debug(rs2SQL);
//			
//			rs2.next();
//			rsc7.setCellValue(rs2.getDouble(1));
//			rs2.close();
//			st2.close();
//			
//			Statement st3 = connection.createStatement();
//			String st3SQL  = "select sum(amt) " +
//				     "from v_gl " +
//				     "where fiscal_year = " + fiscalYear + " " +
//				     "and activitycode = '" + rs.getString(6) + "'";
//			logger.debug("st3DQL: ");
//			logger.debug(st3SQL);
//			ResultSet rs3 = st3.executeQuery(st3SQL);
//			
//			rs3.next();
//			logger.debug("st3SQL result: " + rs3.getDouble(1));
//			rsc8.setCellValue(rs3.getDouble(1));
//			rs3.close();
//			st3.close();
//			i = i + 1;
//		}
//
//		rs.close();
//		st.close();
//		connection.close();
		sheet.setColumnWidth(col_ปีงบประมาณ, 3000);
		sheet.setColumnWidth(col_แผนงาน, 3000);
		sheet.setColumnWidth(col_ยุทธศาสตร์, 3000);
		sheet.setColumnWidth(col_ผลผลิต, 3000);
		sheet.setColumnWidth(col_กิจกรรมหลัก, 3000);
		sheet.setColumnWidth(col_แผนปฏิบัติการ, 3000);
		sheet.setColumnWidth(col_กิจกรรมรอง, 3000);
		sheet.setColumnWidth(col_ชื่อ, 20000);
		sheet.setColumnWidth(col_การจัดสรรงบประมาณ, 7000);
		sheet.setColumnWidth(col_แผนการใช้เงิน, 5000);
		sheet.setColumnWidth(col_ผลการใช้เงิน, 5000);
		sheet.createFreezePane( 0, 1 );
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
