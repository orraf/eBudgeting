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
import org.springframework.transaction.annotation.Transactional;

import biz.thaicom.eBudgeting.models.hrx.Organization;
import biz.thaicom.eBudgeting.models.hrx.OrganizationType;
import biz.thaicom.eBudgeting.models.pln.Objective;
import biz.thaicom.security.models.ThaicomUserDetail;

public class M81R12XLSView extends AbstractPOIExcelView {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
	
	public static Logger logger = LoggerFactory.getLogger(M81R02XLSView.class);
	
	@Override
	protected Workbook createWorkbook() {
		return new HSSFWorkbook();
	}

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ThaicomUserDetail currentUser = (ThaicomUserDetail) model.get("currentUser");
		
		Organization searchOrg = (Organization) model.get("searchOrg");
		
		
        Map<String, CellStyle> styles = createStyles(workbook);

        
		@SuppressWarnings("unchecked")
		Objective root = (Objective) model.get("root");
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

		if(OrganizationType.getType(searchOrg) == OrganizationType.ส่วนในจังหวัด ||
				OrganizationType.getType(searchOrg) == OrganizationType.แผนกในจังหวัด || 
				OrganizationType.getType(searchOrg) == OrganizationType.แผนกในอำเภอ || 
				OrganizationType.getType(searchOrg) == OrganizationType.แผนก) {
			cell21.setCellValue("หน่วยงาน " + searchOrg.getName() + " " + searchOrg.getParent().getName());
			
			if(OrganizationType.getType(searchOrg) != OrganizationType.ส่วนในจังหวัด) {
				searchOrg = searchOrg.getParent();
			}
		} else {
			cell21.setCellValue("หน่วยงาน " + searchOrg.getName());
		}
		
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

		//Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		//Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@www.innova.or.th:1521:xe", "afrpmt", "afrpmt");
		Connection connection = dataSource.getConnection();
		
		PreparedStatement ps = null;
		
		Statement rootStmt = connection.createStatement();
		String st0Sql = "select m.id " +
				"from pln_objective m " +
				"where m.parent_pln_objective_id is null " +
				"	and m.type_pln_objectivetype_id = 100 " +
				"	and m.fiscalYear = " + fiscalYear; 
		ResultSet rootRs = rootStmt.executeQuery(st0Sql);
		Long rootObjectiveId = null;
		while(rootRs.next()) {
			rootObjectiveId = rootRs.getLong("id");
		}
		
		Statement st = connection.createStatement();
		String st01 = "select lpad(' ',(level-4)*5)||m.name name, m.isleaf, m.id, nvl(lpad(' ',(level-3)*5), '     ') space, m.code " +
				   "from pln_objective m where m.id <> " + root.getId() + " and exists " +
				   "(select 1 from pln_activitytargetreport t4, pln_activitytarget t5, pln_activity t1, pln_objective t2, " +
                    "(select id from hrx_organization " +
                        "connect by prior id = parent_hrx_organization_id " +
                        "start with id = "+ searchOrg.getId() +") t3 " +
                     "where t4.target_pln_acttarget_id = t5.id " +
                     "and t5.activity_pln_activity_id = t1.id " +
                     "and t1.obj_pln_objective_id = t2.id " +
                     "and t4.owner_hrx_organization_id = t3.id " + 
                     "and '.'||t2.id||t2.parentpath like '%.'||m.id||'.%' " +
                     "and t2.fiscalyear = " + fiscalYear + ") " +
				   "connect by prior m.id = m.parent_pln_objective_id " +
                " start with m.id = " + rootObjectiveId 
                + " order siblings by m.code asc";
		ResultSet rs = st.executeQuery(st01);

		int i = 4;
		int j = 0;
		double s1 = 0.0;
		double s2 = 0.0;
		double d2 = 0.0;
		
		logger.debug(st01);
		
		while (rs.next()) {
			Row rows = sheet.createRow(i);
			
			Cell rsc0 = rows.createCell(0);
			rsc0.setCellValue(rs.getString(1));
			rsc0.setCellStyle(styles.get("cellleft"));
			
			if (rs.getString(5).length() == 7) {
				Statement st0 = connection.createStatement();
				String stmt;
				if (searchOrg.getId().toString().substring(5, 9).equals("0000") ) {
					stmt = "select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(amountallocated),'999,999,999,999')), '...')||' บาท)' " +
													 "from bgt_budgetproposal " +
													 "where objective_id = " + rs.getInt(3) + " " +
													 "and organization_id = " + searchOrg.getId() + " ";
					
				} else {
					stmt = "select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(budgetallocated),'999,999,999,999')), '...')||' บาท)' " +
													 "from pln_activity t1, pln_activityperformance t3 " +
													 "where t1.id = t3.activity_pln_activity_id " +
													 "and t1.id in (select id from PLN_ACTIVITY where obj_pln_objective_id in (select id from pln_objective connect by prior id = parent_pln_objective_id start with id = " + rs.getInt(3) + "))" +
													 "and t3.owner_hrx_organization_id = " + searchOrg.getId() + " ";
					
				}
				
				ResultSet rs0 = st0.executeQuery(stmt);
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
				
				for (j=3;j<16;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellnumber2"));
				}

				Statement st3 = connection.createStatement();
				String st03 = "select t1.fiscalmonth, sum(t1.budgetplan) " +
						 "from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activity t3 " +
					  	 "where t1.performance_pln_actper_id = t2.id " +
						 "and t2.activity_pln_activity_id = t3.id " +
					  	 "and t1.owner_hrx_organization_id in (select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") " +
						 "and t3.obj_pln_objective_id in (select id from pln_objective connect by prior id = parent_pln_objective_id start with id = " + rs.getInt(3) + ") " +
						 "group by t1.fiscalmonth " +
						 "order by t1.fiscalmonth ";
				ResultSet rs3 = st3.executeQuery(st03);

				logger.debug(st03);
				
				j = 3;
				s1 = 0;
				while (rs3.next()) {
					Cell rscj = rows.getCell(j);
					rscj.setCellValue(rs3.getInt(2));
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
				
				for (j=3;j<16;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("cellnumber2"));
				}

				Statement st4 = connection.createStatement();
/*				ResultSet rs4 = st4.executeQuery("select date2fmonth(gl_trans_docdate) mon, nvl(sum(amt),0) amt " +
									   "from v_gl " +
									   "where org_id in (select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") " +
									   "and fiscal_year = " + fiscalYear + " " +
									   "and gl_trans_plan = '" + rs.getString(5) + "' " +
									   "group by date2fmonth(gl_trans_docdate) " +
									   "order by 1 ");
*/
				ResultSet rs4 = st4.executeQuery("select date2fmonth(gl_trans_docdate) mon, nvl(sum(amt),0) amt " +
												 "from v_gl " +
												 "where org_id in (select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") " +
												 "and fiscal_year = " + fiscalYear + " " +
												 "and activitycode in (select code from pln_objective " +
												 						"where id <> " + rs.getInt(3) + " " +
												 						"connect by prior id = parent_pln_objective_id " +
												 						"start with id = " + rs.getInt(3) + ") " +
												 "group by date2fmonth(gl_trans_docdate) " +
												 "order by 1 ");

				Double d1 = 0.0;
				while (rs4.next()) {
					Cell rscj = rows.getCell(rs4.getInt(1)+2);
					rscj.setCellValue(rs4.getDouble(2));
					d1 = d1 + rs4.getDouble(2);
				}
				rs4.close();
				st4.close();
				rsc3 = rows.getCell(15);
				rsc3.setCellValue(d1);

				/**  เพิ่มผลรวมการใช้เงิน  **/
				rows = sheet.createRow(i+2);

				rsc1 = rows.createCell(1);
				rsc1.setCellStyle(styles.get("cellcenter"));
				
				rsc2 = rows.createCell(2);
				rsc2.setCellValue("รวมผลการใช้เงิน");
				rsc2.setCellStyle(styles.get("groupcenter"));
				
				for (j=3;j<16;j++) {
					Cell rscj = rows.createCell(j);
					rscj.setCellStyle(styles.get("groupnumber2"));
				}

				st4 = connection.createStatement();
				rs4 = st4.executeQuery("select t2.fiscalmonth, nvl(sum(t2.budgetresult),0) " +
									   "from pln_activity t1, (select d2.activity_pln_activity_id, d1.owner_hrx_organization_id, d4.fiscalmonth, d4.budgetresult " +
								                                "from pln_activitytargetreport d1, pln_activitytarget d2, pln_activityperformance d3, pln_monthlybgtreport d4, " +
								                                    "(select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") d5 " +
								                                "where d1.target_pln_acttarget_id = d2.id " + 
								                                "and d1.performance_pln_actper_id = d3.id " + 
								                                "and d3.id = d4.performance_pln_actper_id " +
								                                "and d1.owner_hrx_organization_id = d5.id) t2 " +
									   "where t1.id = t2.activity_pln_activity_id (+) " +
									   "and t1.obj_pln_objective_id in (select id from pln_objective " +
																	   "connect by prior id = parent_pln_objective_id " +
																	   "start with id = " + rs.getInt(3) + ") " +
									   "connect by prior t1.id = t1.parent_pln_activity_id " + 
									   "start with t1.parent_pln_activity_id is null " +
									   "group by t2.fiscalmonth order by t2.fiscalmonth ");

				s1 = 0.0;
				while (rs4.next()) {
					Cell rscj = rows.getCell(rs4.getInt(1)+3);
					rscj.setCellValue(rs4.getDouble(2));
					s1 = s1 + rs4.getDouble(2);
				}
				rs4.close();
				st4.close();
				rsc3 = rows.getCell(15);
				rsc3.setCellValue(s1);

				i = i+3;
				
			} else {
				if (rs.getInt(2) == 1) {
					Statement st0 = connection.createStatement();
/*
					String st02 = "select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(budgetallocated),'999,999,999,999')), '...')||' บาท)' " +
							 "from pln_activity t1, pln_activityperformance t3 " +
							 "where t1.id = t3.activity_pln_activity_id " +
							 "and t1.id in (select id from PLN_ACTIVITY connect by prior id = PARENT_PLN_ACTIVITY_ID start with OBJ_PLN_OBJECTIVE_ID =" + rs.getInt(3) + ")" +
							 "and t3.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with parent_hrx_organization_id = " + searchOrg.getId() + ") ";							
*/					
					String st02 = "select '   (จัดสรรเงิน '||nvl(ltrim(to_char(sum(budgetallocated),'999,999,999,999')), '...')||' บาท)' " +
							 "from pln_activity t1, pln_activityperformance t3 " +
							 "where t1.id = t3.activity_pln_activity_id " +
							 "and t1.id in (select id from PLN_ACTIVITY where OBJ_PLN_OBJECTIVE_ID =" + rs.getInt(3) + ")" +
							 "and t3.owner_hrx_organization_id = " + searchOrg.getId() + " ";							
					
					logger.debug(st02);
					
					ResultSet rs0 = st0.executeQuery(st02);
					
					
					
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
					
					for (j=3;j<16;j++) {
						Cell rscj = rows.createCell(j);
						rscj.setCellStyle(styles.get("cellnumber2"));
					}

					Statement st3 = connection.createStatement();
					
					/**
					 * 
select t1.fiscalmonth, sum(t1.budgetplan), ltrim(to_char(sum(t1.budgetplan),'999,999,999,999')) 
from pln_monthlybgtreport t1, pln_activityperformance t2, PLN_ACTIVITY t3
where t1.performance_pln_actper_id = t2.id 
  and t2.ACTIVITY_PLN_ACTIVITY_ID = t3.id
  and t3.id in (select id from PLN_ACTIVITY connect by prior id = PARENT_PLN_ACTIVITY_ID start with OBJ_PLN_OBJECTIVE_ID = 86)
  and t2.owner_hrx_organization_id in (select id from hrx_organization connect by prior id = parent_hrx_organization_id start with id = 132010000)
group by t1.fiscalmonth order by t1.fiscalmonth;
					 * 
					 * 
					 */
					
					
					String st03 = "" +
							"select t1.fiscalmonth, sum(t1.budgetplan), ltrim(to_char(sum(t1.budgetplan),'999,999,999,999')) " +
							"from pln_monthlybgtreport t1, pln_activityperformance t2, pln_activity t3 " +
						  	"where t1.performance_pln_actper_id = t2.id " +
						  	"	and t2.ACTIVITY_PLN_ACTIVITY_ID = t3.id " +
							"	and t3.id in (select id from PLN_ACTIVITY connect by prior id = PARENT_PLN_ACTIVITY_ID start with OBJ_PLN_OBJECTIVE_ID = " + rs.getInt(3) + ") " +
							"	and t3.obj_pln_objective_id = " + rs.getInt(3) + 
							"	and t2.owner_hrx_organization_id in (select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ")" +
							"group by t1.fiscalmonth " +
							"order by t1.fiscalmonth ";
					logger.debug("XXXXXXX");
					logger.debug(st03);
					
					ResultSet rs3 = st3.executeQuery(st03);

					j = 3;
					s1 = 0.0;
					while (rs3.next()) {
						Cell rscj = rows.getCell(j);
						rscj.setCellValue(rs3.getDouble(2));
						s1 = s1 + rs3.getDouble(2);
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
					
					for (j=3;j<16;j++) {
						Cell rscj = rows.createCell(j);
						rscj.setCellStyle(styles.get("cellnumber2"));
					}

					Statement st4 = connection.createStatement();
					ResultSet rs4 = st4.executeQuery("select date2fmonth(gl_trans_docdate) mon, nvl(sum(amt),0) amt " +
										   "from v_gl " +
										   "where org_id in (select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") " +
										   "and fiscal_year = " + fiscalYear + " " +
										   "and activitycode like '" + rs.getString(5) + "' " +
										   "group by date2fmonth(gl_trans_docdate) " +
										   "order by 1 ");

					s1 = 0.0;
					while (rs4.next()) {
						Cell rscj = rows.getCell(rs4.getInt(1)+2);
						rscj.setCellValue(rs4.getDouble(2));
						s1 = s1 + rs4.getDouble(2);
					}
					rs4.close();
					st4.close();
					rsc3 = rows.getCell(15);
					rsc3.setCellValue(s1);

					/**  เพิ่มผลรวมการใช้เงิน  **/
					rows = sheet.createRow(i+2);

					rsc1 = rows.createCell(1);
					rsc1.setCellStyle(styles.get("cellcenter"));
					
					rsc2 = rows.createCell(2);
					rsc2.setCellValue("รวมผลการใช้เงิน");
					rsc2.setCellStyle(styles.get("groupcenter"));
					
					for (j=3;j<16;j++) {
						Cell rscj = rows.createCell(j);
						rscj.setCellStyle(styles.get("groupnumber2"));
					}

					st4 = connection.createStatement();
					rs4 = st4.executeQuery("select t2.fiscalmonth, nvl(sum(t2.budgetresult),0) " +
										   "from pln_activity t1, (select d2.activity_pln_activity_id, d1.owner_hrx_organization_id, d4.fiscalmonth, d4.budgetresult " +
									                                "from pln_activitytargetreport d1, pln_activitytarget d2, pln_activityperformance d3, pln_monthlybgtreport d4, " +
									                                    "(select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = " + searchOrg.getId() + ") d5 " +
									                                "where d1.target_pln_acttarget_id = d2.id " + 
									                                "and d1.performance_pln_actper_id = d3.id " + 
									                                "and d3.id = d4.performance_pln_actper_id " +
									                                "and d1.owner_hrx_organization_id = d5.id) t2 " +
										   "where t1.id = t2.activity_pln_activity_id (+) " +
										   "and t1.obj_pln_objective_id = " + rs.getInt(3) + " " +
										   "connect by prior t1.id = t1.parent_pln_activity_id " + 
										   "start with t1.parent_pln_activity_id is null " +
										   "group by t2.fiscalmonth order by t2.fiscalmonth ");

					s1 = 0.0;
					while (rs4.next()) {
						Cell rscj = rows.getCell(rs4.getInt(1)+3);
						rscj.setCellValue(rs4.getDouble(2));
						s1 = s1 + rs4.getDouble(2);
					}
					rs4.close();
					st4.close();
					rsc3 = rows.getCell(15);
					rsc3.setCellValue(s1);

					i = i+3;
					
					
					
					Statement st1 = connection.createStatement();
/* แก้ไขการแสดงกิจกรรม ให้แสดงกิจกรรมทุกระดับเป็น Tree Walk
					String st1Sql = "select distinct t1.code, t1.name, t1.id, t5.owner_hrx_organization_id, '1' type, t3.id target_id, '   (เป้าหมาย '|| ltrim(decode(t4.id, 2, to_char(t5.targetvalue,'999,999,999,999.99'), 3, to_char(t5.targetvalue,'999,999,999,999.99'), 9, to_char(t5.targetvalue,'999,999,999,999.99'), to_char(t5.targetvalue,'999,999,999,999')))||' '||t4.name||')' target, t4.id unit_id " +
							 "from pln_activitytargetreport t5, pln_activity t1, pln_activitytarget t3, pln_targetunit t4 " +
	 						 "where t5.target_pln_acttarget_id = t3.id " +
							 "	and t5.owner_hrx_organization_id = " + searchOrg.getId() +
							 "	and t1.id = t3.activity_pln_activity_id " +
							 "	and t3.unit_pln_targetunit_id = t4.id " +
							 "	and t1.obj_pln_objective_id = " + rs.getInt(3) + " " +
							 "order by 3, 5 ";
*/
					
					String st1Sql = "select code, lpad(' ', (level-1) * 5, ' ')||t1.name name, id, t2.owner_hrx_organization_id, nvl(t2.own,0) own, t2.target_id, '   (เป้าหมาย '|| ltrim(decode(t2.unit_id, 2, to_char(t2.targetvalue,'999,999,999,999.99'), 3, to_char(t2.targetvalue,'999,999,999,999.99'), 9, to_char(t2.targetvalue,'999,999,999,999.99'), to_char(t2.targetvalue,'999,999,999,999')))||' '||t2.name||')' target, t2.unit_id, nvl((select distinct 0 from pln_activity where parent_pln_activity_id = t1.id), 1) leaf " +
									 "from pln_activity t1, (select d2.activity_pln_activity_id, d1.owner_hrx_organization_id, d2.id target_id, d1.targetvalue, d3.id unit_id, d3.name, 1 own " +
									 						"from pln_activitytargetreport d1, pln_activitytarget d2, pln_targetunit d3 " +
									 						"where d1.target_pln_acttarget_id = d2.id " +
									 						"and d2.unit_pln_targetunit_id = d3.id " +
									 						"and d1.owner_hrx_organization_id = " + searchOrg.getId() + ") t2 " +
			 						 "where t1.id = t2.activity_pln_activity_id (+) " +
									 "and t1.obj_pln_objective_id = " + rs.getInt(3) + " " +
									 "connect by prior t1.id = t1.parent_pln_activity_id " +
									 "start with t1.parent_pln_activity_id is null " +
									 "order siblings by t1.code ";
					logger.debug("YYYYYYY");
					logger.debug(st1Sql);
					
					ResultSet rs1 = st1.executeQuery(st1Sql);
					
					
					int actId = 0;
					while (rs1.next()) {
						Long targetId=rs1.getLong("target_id");
						
						if (rs1.getInt(5)==1 || rs1.getInt(9)==0) {
							Row rows1 = sheet.createRow(i);
							Cell rsc11 = rows1.createCell(0);
							if (rs1.getInt(3)!=actId) {
								rsc11.setCellValue(rs.getString(4)+rs1.getString(2));
								actId = rs1.getInt(3);
							}
							rsc11.setCellStyle(styles.get("cellleft"));
							Cell rsc112 = rows1.createCell(1);
							rsc112.setCellStyle(styles.get("cellcenter"));
							
							// here we have to do แผนการใช้เงิน/ผลการใช้เงิน
							// now แผน/ผลการใช้เงินของกิจกรรม
							if (rs1.getInt(9)==1) {
								Cell rsc013 = rows1.createCell(2);
								rsc013.setCellValue("แผนการใช้เงิน");
								rsc013.setCellStyle(styles.get("cellcenter"));
								
								for (j=3;j<16;j++) {
									Cell rscj01 = rows1.createCell(j);
									rscj01.setCellStyle(styles.get("cellnumber2"));

								}

								Row rows02 = sheet.createRow(i+1);
								Cell rsc021 = rows02.createCell(0);
								rsc021.setCellStyle(styles.get("cellleft"));
								
								Cell rsc022 = rows02.createCell(1);
								rsc022.setCellStyle(styles.get("cellcenter"));
								
								Cell rsc023 = rows02.createCell(2);
								rsc023.setCellValue("ผลการใช้เงิน");
								rsc023.setCellStyle(styles.get("cellcenter"));
								
								for (j=3;j<16;j++) {
									Cell rscj02 = rows02.createCell(j);
									rscj02.setCellStyle(styles.get("cellnumber2"));

								}
								
								String st05 = "select t1.fiscalmonth, nvl(sum(t1.budgetplan),0), nvl(sum(t1.BUDGETRESULT),0) " 
										+ "from pln_monthlybgtreport t1, pln_activityperformance t2, PLN_ACTIVITYTARGETREPORT t3," +
										"		pln_activitytarget t4 "
										+ "where t1.performance_pln_actper_id = t2.id "
										+ " 	and t2.id = t3.performance_pln_actper_id " 
										+ "		and t3.target_pln_acttarget_id = t4.id "	
										+ "  	and t4.id = " + targetId
										+ " 	and t2.owner_hrx_organization_id in (select id from hrx_organization where substr(code,5,1) = '0' connect by prior id = parent_hrx_organization_id start with id = "+ searchOrg.getId() +") " 
										+ "group by t1.fiscalmonth order by t1.fiscalmonth";
								Statement st5 = connection.createStatement();
								ResultSet rs5 = st5.executeQuery(st05);
								logger.debug(st05);
								j = 3;
								s1 = 0.0;
								s2 = 0.0;
								d2 = 0.0;
								while(rs5.next()) {
									Cell rscj1 = rows1.getCell(j);
									rscj1.setCellValue(rs5.getInt(2));
									Cell rscj2 = rows02.getCell(j);
									rscj2.setCellValue(rs5.getDouble(3));
									
									s1 = s1 + rs5.getInt(2);
									d2 = d2 + rs5.getDouble(3);
									j = j+1;
								}
								Cell rscs1 = rows1.getCell(15);
								rscs1.setCellValue(s1);
								Cell rscs2 = rows02.getCell(15);
								rscs2.setCellValue(d2);
								
								i = i+2;
								
								rows1 = sheet.createRow(i);
								
								
								Cell rsc12 = rows1.createCell(1);
								rsc12.setCellValue(rs1.getString(7));
								rsc12.setCellStyle(styles.get("cellcenter"));

								Cell rsc13 = rows1.createCell(2);
								rsc13.setCellValue("แผนงาน");
								rsc13.setCellStyle(styles.get("cellcenter"));
								
								for (j=3;j<16;j++) {
									Cell rscj = rows1.createCell(j);
									if (rs1.getInt(8)==2 || rs1.getInt(8)==3 || rs1.getInt(8)==9) {
										rscj.setCellStyle(styles.get("cellnumber2"));
									} else {
										rscj.setCellStyle(styles.get("cellnumber"));
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
								
								for (j=3;j<16;j++) {
									Cell rscj = rows2.createCell(j);
									if (rs1.getInt(8)==2 || rs1.getInt(8)==3 || rs1.getInt(8)==9) {
										rscj.setCellStyle(styles.get("cellnumber2"));
									} else {
										rscj.setCellStyle(styles.get("cellnumber"));
									}

								}
								
								Statement st2 = connection.createStatement();
								ResultSet rs2;
								String rs2SQL ="select t1.fiscalmonth, sum(t1.activityplan), sum(t1.activityresult) " +

										 "from pln_monthlyactreport t1, pln_activitytargetreport t2, pln_activitytarget t3, " +
										     "(select id from hrx_organization where substr(code,5,1) = '0' " +
										        "connect by prior id = parent_hrx_organization_id " +
										        "start with id = "+ searchOrg.getId() +") t4 " +
										 "where t1.report_pln_acttargetreport_id = t2.id " +
									     "and t2.target_pln_acttarget_id = t3.id " +
										 "and t1.owner_hrx_organization_id = t4.id " +
										 "and t3.activity_pln_activity_id = " + rs1.getInt(3) + 
										 " and t3.id = " + rs1.getInt(6) +
										 " group by t1.fiscalmonth order by t1.fiscalmonth ";
								rs2 = st2.executeQuery(rs2SQL);

								
								
								
								j = 3;
								s1 = 0.0;
								s2 = 0.0;
								if(rs1.getInt(3) == 2900 ) {
									logger.debug(">>>>>>>>>>: rs2:");
									logger.debug(rs2SQL);
								}
								while (rs2.next()) {
									Cell rscj1 = rows1.getCell(j);
									rscj1.setCellValue(rs2.getDouble(2));
									Cell rscj2 = rows2.getCell(j);
									rscj2.setCellValue(rs2.getDouble(3));
									s1 = s1 + rs2.getDouble(2);
									s2 = s2 + rs2.getDouble(3);
									j = j+1;
								}
								rs2.close();
								st2.close();
								Cell rscs11 = rows1.getCell(15);
								rscs11.setCellValue(s1);
								Cell rscs22 = rows2.getCell(15);
								rscs22.setCellValue(s2);
								
								i = i+2;
							} else {
								Statement st6 = connection.createStatement();
								ResultSet rs6;
								String rs6SQL = "select count(*) " +
												"from pln_activitytargetreport s1, pln_activitytarget s2, (select id from pln_activity " +
																											"connect by prior id = parent_pln_activity_id " +
																											"start with id = " + rs1.getInt(3) + ") s3 " +
												"where s1.target_pln_acttarget_id = s2.id " +
												"and s2.activity_pln_activity_id = s3.id " +
												"and s1.owner_hrx_organization_id = " + searchOrg.getId() + " ";
								rs6 = st6.executeQuery(rs6SQL);

								while (rs6.next()) {
									if (rs6.getInt(1) > 0) {
										for (j=1;j<16;j++) {
											Cell rscj = rows1.createCell(j);
											rscj.setCellStyle(styles.get("cellleft"));

										}
										i = i+1;								
									} else {
										rsc11.setCellValue("");
									}
								}

							}
							
						}
						
					}
					rs1.close();
					st1.close();
				}
				else {
					for (j=1;j<16;j++) {
						Cell rscj = rows.createCell(j);
						rscj.setCellStyle(styles.get("cellleft"));

					}
					i = i+1;
				}
			}
		}
		
		Row rowE = sheet.createRow(i);
		Cell re = rowE.createCell(0);
		re.setCellStyle(styles.get("celltop"));
		
		rs.close();
		st.close();
		connection.close();

		sheet.setColumnWidth(0, 15000);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 5000);
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
		sheet.setColumnWidth(15, 3000);
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
        style.setAlignment(CellStyle.ALIGN_CENTER );
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
      /*  style.setFont(groupFont);  */
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
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex() );
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("#,##0.00"));
      /*  style.setFont(groupFont); */
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("groupnumber2", style);

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
        styles.put("cellnumber2", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("General"));
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellnumbercenter", style);
        
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setDataFormat(format.getFormat("General"));
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cellnumber2Ditgitcenter", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setWrapText(true);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("celltop", style);

        return styles;
    }

}
