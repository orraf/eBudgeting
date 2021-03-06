<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<c:if test='${not empty readOnly}'>
		<div class="alert">
    			<strong>Sign Off แล้ว</strong> สามารถเปิดดูข้อมูลได้อย่างเดียว ไม่สามารถแก้ไขเพิ่มเติมได้ 
    		</div>
	</c:if>
	<h4>การบันทึกแผนการดำเนินงานกิจกรรมย่อย ${organizationLevelAndName}</h4>
	<div class="alert alert-error">กรณีเห็นกิจกรรมไม่ครบ  เป้าหมายไม่ตรง  เงินงบประมาณไม่ตรง ให้ติดต่อกับ สกยจ.</div> 
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
				<a href="#" class="btn btn-primary" id="saveBtn">บันทึกข้อมูล</a>  
				<a href="#" class="btn" id="cancelBtn">กลับหน้าหลัก</a> 
			</div>
		</div>

		<div id="assignTargetValueModal" class="modal wideModal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" id="saveAssignTargetBtn">บันทึกข้อมูล</a>  
				<a href="#" class="btn" id="cancelBtn">ยกเลิก</a>
			</div>
		</div>

		<div id="mainCtr">
			<div id="budgetSlt"></div>
			<div id="mainTbl"></div>
		</div>

	</div>
</div>
</div>

<script id="objectiveSelectionTemplate" type="text/x-handler-template">
		<div class="btn-group">
    		<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
    			เลือกแผนปฏิบัติการ
    			<span class="caret"></span>
    		</a>
    		<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
    			{{#each this}}
					<li data-id={{id}}>
						<a href="javascript:;" class="objectiveSelect">
							[{{code}}] {{name}} 
						</a>
					</li>
				{{/each}}
    		</ul>
    	</div>
</script>

<script id="mainTblTemplate" type="text/x-handler-template">
<h4>{{name}}</h4>
<table class="table table-bordered table-striped" id="mainTbl">
	<thead>
		<tr>
			<td style="width:30px;"></td>
			<td style="width:30px;">รหัส</td>
			<td>ชื่อกิจกรรมย่อย</td>
			<td style="width:100px;">เป้าหมาย</td>
			<td style="width:100px;">หน่วยนับ</td>
			<td style="width:100px;">งบประมาณที่จัดสรร (บาท)</td>

		</tr>
	</thead>
	<tbody>
	</tbody>
</table>

หมายเหตุ: ถ้ามีเครื่องหมาย <i class="icon-check"></i> ท้ายเป้าหมายแสดงว่าเป้าหมายนั้นได้จัดทำแผนแล้ว 
</script>
<script id="mainTblTbodyObjectiveTemplate" type="text/x-handler-template">
<tr data-id="{{id}}">
	<td></td>
	<td>{{code}}</td>
	<td>{{name}}</td>
	<td></td>
	<td></td>
	<td></td>
</tr>
</script>
<script id="mainTblTbodyActivityTemplate" type="text/x-handler-template">
{{#if showInTree}}
<tr data-id="{{id}}">
	<td></td>
	<td>{{code}}</td>
	<td><span style="padding-left:{{padding}}px;">{{name}}</span></td>
	<td><ul style="list-style-type: none;margin:0px;padding: 0px; text-align:center;">
		{{#each filterTargets}}
			<li data-id="{{filterReport.id}}"><a href="javascript:;" class="assignTargetLnk">{{formatNumber filterReport.targetValue}}</a>
				{{#if filterReport.monthlyReports}}<i class="icon-check"></i> {{/if}}
			</li>
		{{/each}}
		</ul>
	</td>
	<td><ul style="list-style-type: none;margin:0px;padding: 0px; text-align:center;">
		{{#each filterTargets}}
			<li>{{unit.name}}</li>
		{{/each}}
		</ul>
	</td>
	<td>
		<ul style="list-style-type: none;margin:0px;padding-right:10px; text-align:right;"">
		{{#each filterTargets}}
			<li data-id="{{filterReport.id}}">
				<a href="javascript:;" class="assignTargetLnk">	 
				
					<span id="target_{{id}}-budgetAllocated">{{formatNumber filterReport.activityPerformance.budgetAllocated}}</span> บาท
				
				</a>
			</li>
		{{/each}}
		</ul>
	</td>
</tr>
{{/if}}
</script>

<script id="mainSelectionTemplate" type="text/x-handler-template">
<form class="form-horizontal">
<div class="control-group" style="margin-bottom:5px;">
	<label class="control-label">แผนปฏิบัติการ :</label> 
	<div class="controls">
		<select id="type101Slt" class="span5">
			<option>กรุณาเลือก...</option>
			{{#each this}}<option value={{id}}>[{{code}}] {{name}}</option>{{/each}}
		</select>
	</div>
</div>
	<div id="type102Div">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">กิจกรรมรอง :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>	
	</div>
</form>
</script>

<script id="selectionTemplate" type="text/x-handler-template">
<div class="control-group"  style="margin-bottom:5px;">
	<label class="control-label">{{type.name}} :</label>
	<div class="controls">
		<select id="type{{type.id}}Slt" class="span5">
			<option>กรุณาเลือก...</option>
			{{#each this}}<option value={{id}}>[{{code}}] {{name}}</option>{{/each}}
		</select>
	</div> 
</div>
</script>

<script id="type102DisabledSelection" type="text/x-handler-template">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">กิจกรรมรอง :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>
</script>


<script id="loadingTemplate" type="text/x-handler-template">
	<div style="margin-top: 30px;"><strong>Loading {{name}} </strong><img src="<c:url value='/resources/graphics/spinner_bar.gif'/>"/></div>
</script>



<script id="mainCtrTemplate" type="text/x-handler-template">
<div id="mainSelection">
</div>
<div id="mainTbl">
</div>
</script>


<script id="mainTblTemplate" type="text/x-handler-template">
<div class="controls" style="margin-bottom: 15px;">
	<div class="pull-left"> 
		
	</div>
	<div class="pull-left" style="margin-left: 20px;">
	<form class="form-search pull-left" style="margin-bottom:10px;" id="activitySearchFrm">
		<div class="input-append">
    		<input type="text" class="span2 search-query" id="activitySearchTxt" value="{{queryTxt}}">
    			<button class="btn" type="submit" id="activitySearchTxt">Search</button>
    	</div>
    </form>
	</div>
	
	<div class="clearfix"></div>
</div>
<table class="table table-bordered table-striped" id="mainTbl">
	<thead>
		<tr>
			<td style="width:30px;"></td>
			<td style="width:80px;">รหัส</td>
			<td>ชื่อกิจกรรมย่อย</td>
			<td style="width:140px;">เป้าหมาย</td>
			<td style="width:100px;">หน่วยนับ</td>

		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>
<script id="mainTblTbodyTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{id}}">
	<td><a href="#td-{{id}}" class="editObjective menuEdit"><i class="icon-edit icon-blue"></i></a>				
	</td>
	<td>{{activity.code}}</td>
	<td {{paddingActivity this}}>
		{{activity.name}}</td>
	<td><ul>
		{{#each targetReports}}
			<li data-id="{{id}}"><a href="#" class="assignTargetLnk">{{formatNumber targetValue}}</a></li>
		{{/each}}
		</ul>
	</td>
	<td><ul>
		{{#each targetReports}}
			<li>{{target.unit.name}}</li>
		{{/each}}
		</ul>
	</td>
</tr>
{{/each}}
</script>

<script id="activityTargetTemplate" type="text/x-handler-template">
<div class="alert alert-info">
	 <h4><u><strong>{{headerString}}</strong></u></h4>
	<div class="pull-left" style="padding-top:10px;">
	    <label>ระบุค่าเป้าหมาย</label>
        <input type="text" class="targetModel" id="targetValue" data-modelname="targetValue" value="{{targetValue}}">
	</div>
	<div class="pull-left" style="padding-top:10px; padding-left: 30px;">
        <label>ระบุหน่วยนับ</label>
        <select class="span2" id="unitSlt" data-modelName="unit" data-modelType="TargetUnit">
                <option value="0">กรุณาเลือก</option>
                {{#each unitSelectionList}}
                        <option value="{{this.id}}" {{#if this.selected}}selected='selected'{{/if}}>{{this.name}}</option>
                {{/each}}
         </select>
	</div>
	<div class="clearfix"></div>
	<div>
		<a href="#" class="btn btn-mini btn-info addActivityTargetBtn"><i class="icon icon-plus-sign icon-white"></i> บันทึกค่าเป้าหมาย</a>
		<a href="#" class="btn btn-mini backToActivity"><i class="icon icon-white"></i> ยกเลิก</a>
	</div>

</div>
</script>

<script  id="activityTargetTableTemplate" type="text/x-handler-template">
<div id="activityTargetMenu">
<a href="#" class="btn btn-info newActivityTargetBtn"><i class="icon icon-file icon-white"></i> เพิ่มเป้าหมาย</a>
</div>
<div id="activityTargetTblCtr">
{{#if targets}}
<table class="table table-bordered table-striped" id="activityTargetTbl" style="width:285px;margin-top:10px;">
<thead>
	<tr>
		<td style="width:45px;"></td>
		<td style="width:140px;">เป้าหมาย</td>
		<td style="width:100px;">หน่วยนับ</td>
	</tr>
</thead>
<tbody>
	{{#each targets}}
	<tr data-idx="{{idx}}" data-id="{{id}}">
	<td><a href="#td-{{id}}" class="editObjective editTarget"><i class="icon-edit icon-blue"></i></a>				
	<a href="#td-{{id}}" class="deleteObjective deleteTarget"><i class="icon-trash icon-red"></i></a></td>
			<td style="text-align:center;">{{formatNumber targetValue}}</td>
			<td style="text-align:center;">{{unit.name}}</td>
	</tr>
	{{/each}}
</tbody>
</table>
{{/if}}
</div>
</script>

<script id="assignTargetValueModalTemplate" type="text/x-handler-template">
<div id="inputAll">
		
		<div style="padding-bottom:12px;">
           <strong><u>แผนการดำเนินงาน</u>: {{formatNumber targetValue}} {{target.unit.name}}  จัดสรรแล้ว <span id="curentSumTarget"></span> {{target.unit.name}}</strong> 
        </div>
        <div class="row">
			<div class="span3">
				ต.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="0" class="monthlyReportPlan" id="monthlyTarget0" style="width:120px;" value="{{monthlyReports.0.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				พ.ย. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="1" class="monthlyReportPlan" id="monthlyTarget1" style="width:120px;" value="{{monthlyReports.1.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				ธ.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="2" class="monthlyReportPlan" id="monthlyTarget2" style="width:120px;" value="{{monthlyReports.2.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
		</div>
        <div class="row">
			<div class="span3">
				ม.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="3" class="monthlyReportPlan" id="monthlyTarget3" style="width:120px;" value="{{monthlyReports.3.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				ก.พ. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="4" class="monthlyReportPlan" id="monthlyTarget4" style="width:120px;" value="{{monthlyReports.4.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				มี.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="5" class="monthlyReportPlan" id="monthlyTarget5" style="width:120px;" value="{{monthlyReports.5.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
		</div>
        <div class="row">
			<div class="span3">
				เม.ย. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="6" class="monthlyReportPlan" id="monthlyTarget6" style="width:120px;" value="{{monthlyReports.6.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				พ.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="7" class="monthlyReportPlan" id="monthlyTarget7" style="width:120px;" value="{{monthlyReports.7.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				มิ.ย. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="8" class="monthlyReportPlan" id="monthlyTarget8" style="width:120px;" value="{{monthlyReports.8.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
		</div>
        <div class="row">
			<div class="span3">
				ก.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="9" class="monthlyReportPlan" id="monthlyTarget9" style="width:120px;" value="{{monthlyReports.9.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				ส.ค. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="10" class="monthlyReportPlan" id="monthlyTarget10" style="width:120px;" value="{{monthlyReports.10.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
			<div class="span3">
				ก.ย. <div class="input-append"><input {{#if disableInput}}disabled="disabled"{{/if}} type="text" data-idx="11" class="monthlyReportPlan" id="monthlyTarget11" style="width:120px;" value="{{monthlyReports.11.activityPlan}}"><span class="add-on">{{target.unit.name}}</span></div>
			</div>
		</div>

	<div class="clearfix"></div>
	
		<div style="padding-bottom:12px;">
           <strong><u>แผนเบิกจ่ายงบประมาณ</u>: {{formatNumber activityPerformance.budgetAllocated}} บาท  จัดสรรแล้ว <span id="curentSumBudget"></span> บาท</strong>
        </div>
        <div class="row">
			<div class="span3">
				ต.ค. <div class="input-append"><input type="text" data-idx="0" class="monthlyBudgetPlan" id="monthlyBudgetTarget0" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.0.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				พ.ย. <div class="input-append"><input type="text" data-idx="1" class="monthlyBudgetPlan" id="monthlyBudgetTarget1" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.1.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				ธ.ค. <div class="input-append"><input type="text" data-idx="2" class="monthlyBudgetPlan" id="monthlyBudgetTarget2" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.2.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
		</div>
        <div class="row">
			<div class="span3">
				ม.ค. <div class="input-append"><input type="text" data-idx="3" class="monthlyBudgetPlan" id="monthlyBudgetTarget3" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.3.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				ก.พ. <div class="input-append"><input type="text" data-idx="4" class="monthlyBudgetPlan" id="monthlyBudgetTarget4" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.4.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				มี.ค. <div class="input-append"><input type="text" data-idx="5" class="monthlyBudgetPlan" id="monthlyBudgetTarget5" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.5.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
		</div>
        <div class="row">
			<div class="span3">
				เม.ย. <div class="input-append"><input type="text" data-idx="6" class="monthlyBudgetPlan" id="monthlyBudgetTarget6" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.6.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				พ.ค. <div class="input-append"><input type="text" data-idx="7" class="monthlyBudgetPlan" id="monthlyBudgetTarget7" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.7.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				มิ.ย. <div class="input-append"><input type="text" data-idx="8" class="monthlyBudgetPlan" id="monthlyBudgetTarget8" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.8.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
		</div>
        <div class="row">
			<div class="span3">
				ก.ค. <div class="input-append"><input type="text" data-idx="9" class="monthlyBudgetPlan" id="monthlyBudgetTarget9" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.9.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				ส.ค. <div class="input-append"><input type="text" data-idx="10" class="monthlyBudgetPlan" id="monthlyBudgetTarget10" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.10.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
			<div class="span3">
				ก.ย. <div class="input-append"><input type="text" data-idx="11" class="monthlyBudgetPlan" id="monthlyBudgetTarget11" style="width:120px;" value="{{activityPerformance.monthlyBudgetReports.11.budgetPlan}}"><span class="add-on">บาท</span></div>
			</div>
		</div>

	</div>
</div>
</script>
<script id="organizationSearchTbodyTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{id}}"><td>
	{{#if this._inProposalList}}
		<span class="label label-warning">เลือกแล้ว</span>
	{{else}}
		<a href="#" class="addOrganization"><i class="icon icon-plus-sign"></i></a>
	{{/if}}

 	{{name}}</td>
</tr>
{{/each}}
</script>

<script id="organizationTargetValueTbodyTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{owner.id}}"><td><a href="#" class="removeOrganizationTarget"><i class="icon icon-trash"></i></a> {{owner.name}}</td>
	<td  style="width:188px">
		<div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input type="text" class="proposalAllocated" id="amountAllocated-{{id}}" style="width:120px; text-align:right;" value="{{targetValue}}"><span class="add-on">{{target.unit.name}}</span></div>
		</div>
	</td>
</tr>
{{/each}}
</script>


<script src="<c:url value='/resources/js/pages/m74f01.js'/>"></script>

<script type="text/javascript">
	var fiscalYear = parseInt("${fiscalYear}");
	
	var currentOrganizationId = "${workAtId}";
	var organizationId = "${organizationId}";
	var mainCtrView = null;
	var objectiveCollection = null;
	var budgetTypeSelectionView = null;
	var rootCollection;
	var topBudgetList = ["งบบุคลากร","งบดำเนินงาน","งบลงทุน","งบอุดหนุน","งบรายจ่ายอื่น"];
	var l = null;
	var e1;
	var e2;
	
	var listTargetUnits = new TargetUnitCollection();
	listTargetUnits.fetch({
		url: appUrl('/TargetUnit/')
	});

	
	var readOnly = "${readOnly}";
	
	Handlebars.registerHelper("sumTargetValue", function(unitId, proposals) {
		// get all targetValue
		sum=0;
		if(proposals == null || proposals.length ==0) {
			return sum;
		}
		for(var i=0; i< proposals.length; i++) {
			if(proposals[i].targets != null) {
				var targets = proposals[i].targets;
				for(var j=0; j < targets.length; j++) {
					if(targets[j].unit.id == unitId) {
						sum += targets[j].targetValue;
					}
				}
			}
		}
		
		return addCommas(sum);
	});
	
	Handlebars.registerHelper("sumProposal", function(proposals) {
		var amount = 0;
		for ( var i = 0; i < proposals.length; i++) {
			amount += proposals[i].amountRequest;
		}
		return addCommas(amount);

	});
	Handlebars.registerHelper("sumAllocationRecords", function(records) {
		var amount = 0;
		for ( var i = 0; i < records.length; i++) {
			amount += records[i].amountAllocated;
		}
		return addCommas(amount);

	});
	
	Handlebars.registerHelper("sumProposalNext1Year", function(proposals) {
		var amount = 0;
		for ( var i = 0; i < proposals.length; i++) {
			amount += proposals[i].amountRequestNext1Year;
		}
		return addCommas(amount);

	});
	Handlebars.registerHelper("sumProposalNext2Year", function(proposals) {
		var amount = 0;
		for ( var i = 0; i < proposals.length; i++) {
			amount += proposals[i].amountRequestNext2Year;
		}
		return addCommas(amount);

	});
	Handlebars.registerHelper("sumProposalNext3Year", function(proposals) {
		var amount = 0;
		for ( var i = 0; i < proposals.length; i++) {
			amount += proposals[i].amountRequestNext3Year;
		}
		return addCommas(amount);

	});

	Handlebars.registerHelper("paddingActivity", function(activity) {
		var padding = 8 + (parseInt(activity.activityLevel) * 48);
		return "padding-left: " + padding + "px;";
	});

	
	Handlebars.registerHelper("formulaLine", function(strategy) {
		console.log(strategy);
		var s = addCommas(strategy.formulaStrategy.standardPrice) + " บาท ";

		if (strategy.formulaStrategy != null) {
			var formulaColumns = strategy.formulaStrategy.formulaColumns;
			for ( var i = 0; i < formulaColumns.length; i++) {
				
				s = s + " &times; ";
				if (formulaColumns[i].isFixed) {
					// now we'll go through requestColumns
					var j;
					for (j = 0; j < strategy.requestColumns.length; j++) {
						if (strategy.requestColumns[j].column.id == formulaColumns[i].id) {
							s = s + addCommas(strategy.requestColumns[j].amount)
								+ " " + formulaColumns[i].unitName;
						}
					}

				} else {
					s = s +  addCommas(formulaColumns[i].value)
						+ " " + formulaColumns[i].unitName;
				}

			}
		}

		return s;
	});

	Handlebars.registerHelper('substract', function(a, b) {
		return a - b;
	});

	Handlebars.registerHelper('childrenNodeTpl', function(children, level) {
		var out = '';
		var childNodeTpl = Handlebars
				.compile($("#childrenNodeTemplate").html());
		var childNormalNodeTpl = Handlebars.compile($(
				"#childrenNormalNodeTemplate").html());
		if (level == undefined)
			level = 0;
		if (children != null && children.length > 0) {

			if (children[0].type.id > 0) {
				children.forEach(function(child) {
					child["level"] = level + 1;
					
					
					child["padding"] = parseInt(level) * 20;
					
					child["nameWidth"] = 246 - 18 - child["padding"];
					out = out + childNodeTpl(child);
				});

			} else {
				children.forEach(function(child) {
					out = out + childNormalNodeTpl(child);
				});
			}
		}

		return out;
	});

	Handlebars.registerHelper('next', function(val, next) {
		return val + next;
	});
	
	var organizationId = "${organizationId}";
	var fiscalYear = "${fiscalYear}";
	var budgetProposalSelectionView = new BudgetProposalSelectionView({
		organizationId: organizationId
	});
	var mainTblView = new MainTblView();

	$(document).ready(function() {
		
		budgetProposalSelectionView.render();
		
	});
</script>
