<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<h4>บันทึกผลการดำเนินงาน</h4> 
</div>

<div class="row">
	<div class="span11">
		
		<div id="modal" class="modal wideModal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<a href="#" class="btn" id="cancelBtn">กลับหน้าหลัก</a> 
			</div>
		</div>

		<div id="mainCtr">

		</div>

	</div>
</div>

</div>


<script id="resultInputTemplate" type="text/x-handler-template">
<div id="inputAll">
	<form class="form-horizontal">
    	<div class="control-group">
    		<label class="control-label" for="reportedResultDate">วันที่รายงาน</label>
    		<div class="controls">
    			<div id="reportedResultDateDiv" class="input-append date datepicker" data-date-format="dd/mm/yyyy" data-date=""><input type="text" id="reportedResultDate" placeholder="..."><span class="add-on"><i class="icon-calendar"></i></span></div>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="result">ค่ารายงาน</label>
    		<div class="controls">
				<div class="input-append"><input type="text" id="result" placeholder="..."><span class="add-on">{{unit.name}}</span></div>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="remark">หมายเหตุ</label>
    		<div class="controls">
    			<input type="text" id="remark" placeholder="...">
    		</div>
    	</div>
		<div class="control-group">
			<div class="controls">
				<button id="saveResultBtn" type="submit" class="btn">บันทึกการรายงานผล</button>
				<button id="backToModalBtn" class="btn">กลับหน้ารายงาน</button>
			</div>
		</div>
    </form>
</div>
</script>

<script id="resultBudgetInputTemplate" type="text/x-handler-template">
<div id="inputAll">
	<form class="form-horizontal">
    	<div class="control-group">
    		<label class="control-label" for="reportedResultDate">เดือนที่รายงาน</label>
    		<div class="controls">
    			<select id="budgetFiscalMonth">
					{{#each month}}
						<option value="{{fiscalMonth}}" {{#if current}}selected="selected"{{/if}}>{{name}}</option>
					{{/each}}
				</select>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="result">เบิกจ่ายได้ทั้งเดือน</label>
    		<div class="controls">
				<div class="input-append"><input type="text" id="budgetResult" placeholder="..."><span class="add-on">บาท</span></div>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="remark">หมายเหตุ</label>
    		<div class="controls">
    			<input type="text" id="remark" placeholder="...">
    		</div>
    	</div>
		<div class="control-group">
			<div class="controls">
				<button id="saveResultBtn" type="submit" class="btn">บันทึกการรายงานผล</button>
				<button id="backToModalBtn" class="btn">กลับหน้ารายงาน</button>
			</div>
		</div>
    </form>
</div>
</script>

<script id="targerReportModalTemplate" type="text/x-handler-template">
<div id="inputAll">
	<div>
		<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
 	 	  	<strong> เป้าหมาย: แผน </strong>
		</div>
	    <div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input disabled type="text" id="totalPlanTxt" style="width:120px;" value="{{formatNumber targetValue}}"><span class="add-on">{{target.unit.name}}</span></div>
		</div>
		<div style="padding-top:7px; margin-left: 35px; padding-right: 20px;height:35px; float:left">
	    	<strong> ผล </strong>
		</div>
	    <div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input disabled type="text" id="totalResultTxt" style="width:120px;" value=""><span class="add-on">{{target.unit.name}}</span></div>
		</div>
		<div style="margin-left: 35px; padding-right: 20px;height:35px; float:left">
 	 	  	<button id="resultInputBtn" class="btn btn-primary"> บันทึกผลเป้าหมาย </button>
		</div>
	</div>
	<div class="clearfix"></div>
	<div>
		<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
	   	 	<strong> งบประมาณ:  ได้รับ </strong>
		</div>
   	 <div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input disabled type="text" id="totalPlanTxt" style="width:120px;" value="{{formatNumber activityPerformance.budgetAllocated}}"><span class="add-on">บาท</span></div>
		</div>
		<div style="padding-top:7px; margin-left: 35px; padding-right: 20px;height:35px; float:left">
   		 	<strong> เบิกจ่ายแล้ว </strong>
		</div>
    	<div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input disabled type="text" id="totalBudgetResultTxt" style="width:120px;" value=""><span class="add-on">บาท</span></div>
		</div>
		<div style="margin-left: 35px; padding-right: 20px;height:35px; float:left">
    		<button id="resultBudgetInputBtn" class="btn btn-primary"> บันทึกผลงบประมาณ </button>
		</div>
	</div>
	<div class="clearfix"></div>
    
	<div class="row">
		<div class="span5">
		<table class="table table-bordered table-condensed smallTxt" id="targetReportTbl">
			<thead>
				<tr>
					<td rowspan="2">เดือน</td>
					<td colspan="2">เป้าหมาย</td>
					<td colspan="2">งบประมาณ</td>
				</tr>
				<tr>
					<td style="width:40px;">แผน</td>
					<td style="width:40px;">ผล</td>
					<td style="width:100px;">แผน</td>
					<td style="width:100px;">ผล</td>
				</tr>
			</thead>
			<tbody>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาส1<strong></td>
					<td class="rightAlign" id="Q1Plan"></td>
					<td class="rightAlign" id="Q1Result"></td>
					<td class="rightAlign" id="Q1BudgetPlan"></td>
					<td class="rightAlign" id="Q1BudgetResult"></td>
				</tr>
				<tr>
					<td>ตุลาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.0.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.0.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.0.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.0.budgetResult}}</td>
				</tr>
				<tr>
					<td>พฤศจิกายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.1.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.1.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.1.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.1.budgetResult}}</td>
				</tr>
				<tr>
					<td>ธันวาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.2.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.2.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.2.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.2.budgetResult}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาส2</strong></td>
					<td class="rightAlign" id="Q2Plan"></td>
					<td class="rightAlign" id="Q2Result"></td>
					<td class="rightAlign" id="Q2BudgetPlan"></td>
					<td class="rightAlign" id="Q2BudgetResult"></td>
				</tr>
				<tr>
					<td>มกราคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.3.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.3.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.3.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.3.budgetResult}}</td>
				</tr>
				<tr>
					<td>กุมภาพันธ์</td>
					<td class="rightAlign">{{formatNumber monthlyReports.4.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.4.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.4.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.4.budgetResult}}</td>
				</tr>
				<tr>
					<td>มีนาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.5.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.5.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.5.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.5.budgetResult}}</td>
				</tr>

			</tbody>
		</table>
		</div>
		<div class="span5">
		<table class="table table-bordered table-condensed smallTxt" id="targetReportTbl">
			<thead>
				<tr>
					<td rowspan="2">เดือน</td>
					<td colspan="2">เป้าหมาย</td>
					<td colspan="2">งบประมาณ</td>
				</tr>
				<tr>
					<td style="width:40px;">แผน</td>
					<td style="width:40px;">ผล</td>
					<td style="width:100px;">แผน</td>
					<td style="width:100px;">ผล</td>
				</tr>

			</thead>
			<tbody>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาส3</strong></td>
					<td class="rightAlign" id="Q3Plan"></td>
					<td class="rightAlign" id="Q3Result"></td>
					<td class="rightAlign" id="Q3BudgetPlan"></td>
					<td class="rightAlign" id="Q3BudgetResult"></td>
				</tr>
				<tr>
					<td>เมษายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.6.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.6.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.6.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.6.budgetResult}}</td>
				</tr>
				<tr>
					<td>พฤษภาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.7.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.7.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.7.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.7.budgetResult}}</td>
				</tr>
				<tr>
					<td>มิถุนายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.8.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.8.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.8.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.8.budgetResult}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาส4</strong></td>
					<td class="rightAlign" id="Q4Plan"></td>
					<td class="rightAlign" id="Q4Result"></td>
					<td class="rightAlign" id="Q4BudgetPlan"></td>
					<td class="rightAlign" id="Q4BudgetResult"></td>
				</tr>
				<tr>
					<td>กรกฎาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.9.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.9.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.9.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.9.budgetResult}}</td>
				</tr>
				<tr>
					<td>สิงหาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.10.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.10.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.10.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.10.budgetResult}}</td>
				</tr>
				<tr>
					<td>กันยายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.11.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.11.activityResult}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.11.budgetPlan}}</td>
					<td class="rightAlign">{{formatNumber activityPerformance.monthlyBudgetReports.11.budgetResult}}</td>
				</tr>

			</tbody>
		</table>
		</div>

	</div>

    
 </div>

</script>

<script id="mainCtrTemplate" type="text/x-handler-template">
<table class="table table-bordered" id="headerTbl" style="margin-bottom:0px; width:785px; table-layout:fixed;">
	<thead>
		<tr>
			<td>กิจกรรม</td>
			<td style="width:60px;">แผน</td>
			<td style="width:60px;">ผล</td>
			<td style="width:100px;">แผน</td>
			<td style="width:100px;">ผล</td>
			<td style="width:100px;">รายงานครั้งล่าสุด</td>
		</tr>
	</thead>
	<tbody>
		{{#each this}}
			<tr>
				<td>{{name}}</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>		
			{{#each this.filterActivities}}
				{{#each this.filterTargets}}
			<tr data-id={{filterReport.id}}>
				{{#if firstRow}}
					<td rowspan="{{targetsLength}}" style="padding-left: 40px;">{{../../name}}</td>
				{{/if}}
					<td style="text-align:center;"><a href="#" class="showReport">{{formatNumber filterReport.targetValue}} {{unit.name}}</a></td>
					<td style="text-align:center;"><a href="#" class="showReport">{{sumResult filterReport.monthlyReports}} {{unit.name}}</a></td>
					<td style="text-align:right;"><a href="#" class="showReport">{{formatNumber filterReport.activityPerformance.budgetAllocated}} บาท</a></td>
					<td style="text-align:right;"><a href="#" class="showReport">{{sumBudgetResult filterReport.activityPerformance.monthlyBudgetReports}} บาท</a></td>
					<td>{{filterReport.lastSaveTxt}}
					</td>
				</tr>
			{{/each}}

			{{/each}}
		{{/each}}
	</tbody>
</table>
</script>

<script src="<c:url value='/resources/js/pages/m81f01.js'/>"></script>

<script type="text/javascript">
Handlebars.registerHelper("sumResult", function(montlyReports) {
	var sum = 0;
	_.forEach(montlyReports, function(report) {
		if(!isNaN(report.activityResult) && report.activityResult != null) {
			sum += parseInt(report.activityResult);
		}
	});
	return addCommas(sum);
});
Handlebars.registerHelper("sumBudgetResult", function(montlyReports) {
	var sum = 0.0;
	_.forEach(montlyReports, function(report) {
		if(report.budgetResult != null) {
			sum += parseFloat(report.budgetResult);
		}
	});
	return addCommas(sum);
});

	var fiscalYear = parseInt("${fiscalYear}");
	
	var mainCtrView = null;
	var objectiveCollection = new ObjectiveCollection();
	var e1;
	var e2;
	
	$(document).ready(function() {
		
		mainCtrView = new MainCtrView();
		
		objectiveCollection.url = appUrl('/Objective/fiscalYear/' + fiscalYear +'/findByActivityTargetReportOfCurrentUser');
		objectiveCollection.fetch( {
			success: function() {
				mainCtrView.setCollection(objectiveCollection);
				mainCtrView.render();
			}
		});
	});
</script>
