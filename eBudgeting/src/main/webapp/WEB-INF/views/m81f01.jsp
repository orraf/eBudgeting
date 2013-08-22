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
		
		<div id="historyModal" class="modal wideModal hide fade">
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

<script id="targetReportByMonthTemplate" type="text/x-handler-template">
<div>
	<table class="table table-bordered">
		<thead>
		<tr>
			<td width="120">วันที่รายงาน</td>
			<td width="100">ค่าที่รายงาน</td>
			<td>หมายเหตุ</td>

		</tr>
		</thead>
		<tbody>
		{{#each this}}
			<tr data-id={{id}}>
				<td>{{formatDate reportedResultDate}}</td>
				
				<td style="text-align: right; padding-right: 8px;">
					{{#if person}}
						<a href="#" class="editResultLnk">{{formatNumber result}}</a>
					{{else}}
						{{formatNumber result}}
					{{/if}}

				</td>
	
				<td>{{remark}}</td>
			</tr>
		{{/each}}			
		</tbody>
	</table>
	<button id="backToModalBtn" class="btn">กลับหน้ารายงาน</button>
</div>
</script>

<script id="resultInputTemplate" type="text/x-handler-template">
<div id="inputAll">
	<form class="form-horizontal">
    	<div class="control-group">
    		<label class="control-label" for="reportedResultDate">วันที่รายงาน</label>
    		<div class="controls">
				{{#if result}}
					<div id="reportedResultDateDiv">
						<input type="hidden" id="reportedResultDate" value="{{formatDateDB result.reportedResultDate}}"/>
						<input type="text" class="uneditable-input" id="reportedResultDateTxt" value="{{formatDate result.reportedResultDate}}">
					</div>
				{{else}}
    				<div id="reportedResultDateDiv" class="input-append date datepicker" data-date-format="dd/mm/yyyy" data-date=""><input type="text" id="reportedResultDate" placeholder="..." value="{{result.reportedResultDate}}"><span class="add-on"><i class="icon-calendar"></i></span></div>
				{{/if}}
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="result">ค่ารายงาน</label>
    		<div class="controls">
				<div class="input-append"><input type="text" id="result" placeholder="..." value="{{result.result}}"><span class="add-on">{{unit.name}}</span></div>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="remark">หมายเหตุ</label>
    		<div class="controls">
    			<input type="text" id="remark" placeholder="..." value="{{result.remark}}">
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
		{{#if activityResultId}}
		<input type="hidden" name="activityResultId" value="{{activityResultId}}"/> 
		{{/if}}
    	<div class="control-group">
    		<label class="control-label" for="reportedResultDate">เดือนที่รายงาน</label>
    		<div class="controls">
    			<select id="budgetFiscalMonth">
					{{#each month}}
						{{#unless disabled}}
							<option value="{{fiscalMonth}}" {{#if current}}selected="selected"{{/if}}>{{name}}</option>
						{{/unless}}
					{{/each}}
				</select>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="result">เบิกจ่ายได้ทั้งเดือน</label>
    		<div class="controls">
				<div class="input-append"><input type="text" id="budgetResult" placeholder="..." value="{{result}}"><span class="add-on">บาท</span></div>
    		</div>
    	</div>
    	<div class="control-group">
    		<label class="control-label" for="remark">หมายเหตุ</label>
    		<div class="controls">
    			<input type="text" id="remark" placeholder="..." value="{{remark}}">
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



<script id="targetReportModalTemplate" type="text/x-handler-template">
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
					<td style="width:40px;">แผนงาน</td>
					<td style="width:40px;">ผลงาน</td>
					<td style="width:100px;">แผนเงิน</td>
					<td style="width:100px;">ผลเงิน</td>
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
				<tr data-fiscalMonth="0">
					<td>ตุลาคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.0.id}}">{{formatNumber monthlyReports.0.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.0.id}}">{{{formatNumberActLink monthlyReports.0.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.0.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.0.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.0.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.0.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="1">
					<td>พฤศจิกายน</td>
					<td class="rightAlign" data-id="{{monthlyReports.1.id}}">{{formatNumber monthlyReports.1.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.1.id}}">{{{formatNumberActLink monthlyReports.1.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.1.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.1.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.1.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.1.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="2">
					<td>ธันวาคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.2.id}}">{{formatNumber monthlyReports.2.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.2.id}}">{{{formatNumberActLink monthlyReports.2.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.2.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.2.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.2.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.2.budgetResult}}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาส2</strong></td>
					<td class="rightAlign" id="Q2Plan"></td>
					<td class="rightAlign" id="Q2Result"></td>
					<td class="rightAlign" id="Q2BudgetPlan"></td>
					<td class="rightAlign" id="Q2BudgetResult"></td>
				</tr>
				<tr data-fiscalMonth="3">
					<td>มกราคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.3.id}}">{{formatNumber monthlyReports.3.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.3.id}}">{{{formatNumberActLink monthlyReports.3.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.3.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.3.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.3.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.3.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="4">
					<td>กุมภาพันธ์</td>
					<td class="rightAlign" data-id="{{monthlyReports.4.id}}">{{formatNumber monthlyReports.4.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.4.id}}">{{{formatNumberActLink monthlyReports.4.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.4.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.4.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.4.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.4.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="5">
					<td>มีนาคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.5.id}}">{{formatNumber monthlyReports.5.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.5.id}}">{{{formatNumberActLink monthlyReports.5.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.5.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.5.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.5.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.5.budgetResult}}}</td>
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
					<td style="width:40px;">แผนงาน</td>
					<td style="width:40px;">ผลงาน</td>
					<td style="width:100px;">แผนเงิน</td>
					<td style="width:100px;">ผลเงิน</td>
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
				<tr data-fiscalMonth="6">
					<td>เมษายน</td>
					<td class="rightAlign" data-id="{{monthlyReports.6.id}}">{{formatNumber monthlyReports.6.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.6.id}}">{{{formatNumberActLink monthlyReports.6.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.6.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.6.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.6.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.6.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="7">
					<td>พฤษภาคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.7.id}}">{{formatNumber monthlyReports.7.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.7.id}}">{{{formatNumberActLink monthlyReports.7.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.7.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.7.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.7.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.7.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="8">
					<td>มิถุนายน</td>
					<td class="rightAlign" data-id="{{monthlyReports.8.id}}">{{formatNumber monthlyReports.8.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.8.id}}">{{{formatNumberActLink monthlyReports.8.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.8.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.8.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.8.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.8.budgetResult}}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาส4</strong></td>
					<td class="rightAlign" id="Q4Plan"></td>
					<td class="rightAlign" id="Q4Result"></td>
					<td class="rightAlign" id="Q4BudgetPlan"></td>
					<td class="rightAlign" id="Q4BudgetResult"></td>
				</tr>
				<tr data-fiscalMonth="9">
					<td>กรกฎาคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.9.id}}">{{formatNumber monthlyReports.9.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.9.id}}">{{{formatNumberActLink monthlyReports.9.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.9.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.9.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.9.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.9.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="10">
					<td>สิงหาคม</td>
					<td class="rightAlign" data-id="{{monthlyReports.10.id}}">{{formatNumber monthlyReports.10.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.10.id}}">{{{formatNumberActLink monthlyReports.10.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.10.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.10.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.10.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.10.budgetResult}}}</td>
				</tr>
				<tr data-fiscalMonth="11">
					<td>กันยายน</td>
					<td class="rightAlign" data-id="{{monthlyReports.11.id}}">{{formatNumber monthlyReports.11.activityPlan}}</td>
					<td class="rightAlign" data-id="{{monthlyReports.11.id}}">{{{formatNumberActLink monthlyReports.11.activityResult}}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.11.id}}">{{formatNumber activityPerformance.monthlyBudgetReports.11.budgetPlan}}</td>
					<td class="rightAlign" data-id="{{activityPerformance.monthlyBudgetReports.11.id}}">{{{formatNumberBgtLink activityPerformance.monthlyBudgetReports.11.budgetResult}}}</td>
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
			<td style="width:60px;">แผนงาน</td>
			<td style="width:60px;">ผลงาน</td>
			<td style="width:100px;">แผนเงิน</td>
			<td style="width:100px;">ผลเงิน</td>
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
				<td  style="text-align:right;">{{formatNumber filterProposals.0.amountUsed}} บาท</td>
				<td>จากระบบ G</td>
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
					<td>{{filterReport.lastSaveTxt}} <a href="javascript:;" class="historyLookup"><i class="icon-search"></i></a>
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
Handlebars.registerHelper("formatNumberActLink", function(result) {
	if(result == null || isNaN(result)) {
	 	return "-";
	} 
	
	var formatedStr = addCommas(result);
	
	return "<a href='#' class='activityResultLnk'>" + formatedStr + "</a>";
	
});
Handlebars.registerHelper("formatNumberBgtLink", function(result) {
	if(result == null || isNaN(result)) {
	 	return "-";
	} 
	
	var formatedStr = addCommas(result);
	
	return "<a href='#' class='budgetResultLnk'>" + formatedStr + "</a>";
	
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
