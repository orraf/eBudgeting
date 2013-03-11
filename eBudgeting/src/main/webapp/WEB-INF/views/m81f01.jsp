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

<script id="targerReportModalTemplate" type="text/x-handler-template">
<div id="inputAll">
	<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
    	<strong> แผนรวม: </strong>
	</div>
    <div style="height:35px; float:left" id="totalInputForm">
		<div class="input-append"><input disabled type="text" id="totalPlanTxt" style="width:120px;" value="{{formatNumber targetValue}}"><span class="add-on">{{target.unit.name}}</span></div>
	</div>
	<div style="padding-top:7px; margin-left: 35px; padding-right: 20px;height:35px; float:left">
    	<strong> ผลรวม: </strong>
	</div>
    <div style="height:35px; float:left" id="totalInputForm">
		<div class="input-append"><input disabled type="text" id="totalResultTxt" style="width:120px;" value=""><span class="add-on">{{target.unit.name}}</span></div>
	</div>
	<div style="margin-left: 35px; padding-right: 20px;height:35px; float:left">
    	<button id="resultInputBtn" class="btn btn-primary"> บันทึกผลการดำเนินงาน </button>
	</div>

	<div class="clearfix"></div>
    
	<div class="row">
		<div class="span5">
		<table class="table table-bordered" id="targetReportTbl">
			<thead>
				<tr>
					<td>เดือน</td>
					<td style="width:100px;">แผน</td>
					<td style="width:100px;">ผล</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>ตุลาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.0.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.0.activityResult}}</td>
				</tr>
				<tr>
					<td>พฤศจิกายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.1.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.1.activityResult}}</td>
				</tr>
				<tr>
					<td>ธันวาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.2.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.2.activityResult}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาสที่ 1<strong></td>
					<td class="rightAlign" id="Q1Plan"></td>
					<td class="rightAlign" id="Q1Result"></td>
				</tr>
				<tr>
					<td>มกราคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.3.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.3.activityResult}}</td>
				</tr>
				<tr>
					<td>กุมภาพันธ์</td>
					<td class="rightAlign">{{formatNumber monthlyReports.4.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.4.activityResult}}</td>
				</tr>
				<tr>
					<td>มีนาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.5.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.5.activityResult}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาสที่ 2</strong></td>
					<td class="rightAlign" id="Q2Plan"></td>
					<td class="rightAlign" id="Q2Result"></td>
				</tr>
			</tbody>
		</table>
		</div>
		<div class="span5">
		<table class="table table-bordered" id="targetReportTbl">
			<thead>
				<tr>
					<td>เดือน</td>
					<td style="width:100px;">แผน</td>
					<td style="width:100px;">ผล</td>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>เมษายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.6.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.6.activityResult}}</td>
				</tr>
				<tr>
					<td>พฤษภาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.7.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.7.activityResult}}</td>
				</tr>
				<tr>
					<td>มิถุนายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.8.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.8.activityResult}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาสที่ 3</strong></td>
					<td class="rightAlign" id="Q3Plan"></td>
					<td class="rightAlign" id="Q3Result"></td>
				</tr>
				<tr>
					<td>กรกฎาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.9.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.9.activityResult}}</td>
				</tr>
				<tr>
					<td>สิงหาคม</td>
					<td class="rightAlign">{{formatNumber monthlyReports.10.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.10.activityResult}}</td>
				</tr>
				<tr>
					<td>กันยายน</td>
					<td class="rightAlign">{{formatNumber monthlyReports.11.activityPlan}}</td>
					<td class="rightAlign">{{formatNumber monthlyReports.11.activityResult}}</td>
				</tr>
				<tr class="info">
					<td class="rightAlign"><strong>ไตรมาสที่ 4<strong></td>
					<td class="rightAlign" id="Q4Plan"></td>
					<td class="rightAlign" id="Q4Result"></td>
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
			<td style="width:400px;">กิจกรรม</td>
			<td style="width:100px;">แผน</td>
			<td style="width:275px;">รายงานครั้งล่าสุด</td>
		</tr>
	</thead>
	<tbody>
		{{#each this}}
			<tr>
				<td>{{name}}</td>
				<td></td>
				<td></td>
			</tr>		
			{{#each this.filterActivities}}
			<tr>
				<td style="padding-left: 40px;">{{name}}</td>
				<td><ul>
					{{#each this.filterTargets}}
						<li data-id="{{filterReport.id}}"><a href="#" class="showReport">{{formatNumber filterReport.targetValue}} {{unit.name}}</a></li>
					{{/each}}
					</ul>
				</td>
				<td>
					<ul>
					{{#each this.filterTargets}}
						<li>{{filterReport.lastSaveTxt}}</li>
					{{/each}}
					</ul>
				</td>
			</tr>
			{{/each}}
		{{/each}}
	</tbody>
</table>
</script>

<script src="<c:url value='/resources/js/pages/m81f01.js'/>"></script>

<script type="text/javascript">
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
