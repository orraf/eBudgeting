<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<h4>บันทึกผลการดำเนินกงาน</h4> 
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
				<td></td>
				<td></td>
			</tr>
			{{#each this.filterTargets}}
			<tr>
				<td></td>
				<td>{{filterReport.targetValue}}</td>
				<td></td>
			</tr>
			{{/each}}
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
