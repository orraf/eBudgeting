<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<h4>การบันทึกการจัดสรรงบประมาณ ลงสู่ระดับกิจกรรมย่อย</h4> 
</div>
<div class="row">
	<div class="span11">
		
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

<script id="budgetProposalSelectionTemplate" type="text/x-handler-template">
		<div class="btn-group">
    		<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
    			เลือกแผนปฏิบัติการ
    			<span class="caret"></span>
    		</a>
    		<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
    			{{#each this}}
					<li data-id={{id}}>
						<a href="javascript:;" class="budgetProposalSelect">
							[{{forObjective.code}}] {{forObjective.name}} 
							<br/> <span style="padding-left:50px;"> - {{budgetType.name}}  ({{formatNumber amountAllocated}} บาท)</span>
						</a>
					</li>
				{{/each}}
    		</ul>
    	</div>
</script>

<script id="mainTblTemplate" type="text/x-handler-template">
<h4>{{forObjective.name}} / {{budgetType.name}} - {{formatNumber amountAllocated}} บาท</h4>
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
<tr data-id="{{id}}">
	<td></td>
	<td>{{code}}</td>
	<td><span style="padding-left:{{padding}}px;">{{name}}</span></td>
	<td><ul style="list-style-type: none;margin:0px;padding: 0px; text-align:center;">
		{{#each filterTargets}}
			<li data-id="{{filterReport.id}}"><a href="javascript:;" class="assignTargetValueLnk">{{formatNumber filterReport.targetValue}}</a></li>
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
				<a href="javascript:;" class="assignTargetValueLnk">	 
					<span id="target_{{id}}-budgetAllocated">{{formatNumber filterReport.activityPerformance.budgetAllocated}}</span> บาท
				
				</a>
			</li>
		{{/each}}
		</ul>
	</td>
</tr>
</script>
<script id="assignTargetValueModalTemplate" type="text/x-handler-template">
<div id="inputAll">
	<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
    	<strong> ค่าเป้าหมายรวม: </strong>
	</div>
    <div style="height:35px; float:left" id="totalInputForm">
		<div class="input-append"><input disabled type="text" id="totalInputTxt" style="width:120px;" value="{{formatNumber targetValue}}"><span class="add-on">{{target.unit.name}}</span></div>
	</div>

	<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
    	<strong> งบประมาณจัดสรรรวม: </strong>
	</div>
    <div style="height:35px; float:left" id="totalInputForm">
		<div class="input-append"><input disabled type="text" id="totalBudgetInputTxt" style="width:120px;" value=""><span class="add-on">บาท</span></div>
	</div>

	<div class="clearfix"></div>
        
        <div style="padding-bottom:12px;">
           <strong><u>การจัดสรรให้หน่วยงาน</u></strong>
        </div>
        <div class="row">

    	    <div class="span6" style="height:290px; border: 1px solid #cccccc">
        	    <div>
            	    <table class="table table-bordered" style="margin-bottom:0px">
                	<thead>
                    	<tr>
                        	<td>หน่วยงาน</td>
                        	<td style="width:100px;">รวม: <span id="sumTotalAllocated"></span> {{target.unit.name}}</td>
							<td style="width:160px;"><span id="sumTotalBudgetAllocated"></span> บาท</td>
                    	</tr>
                	</thead>
                	</table>
            	</div>

	            <div style="height:252px;overflow:auto;">
    	            <table style="margin-bottom:0px;" class="table table-bordered" id="organizationProposalTbl">
        	        	<tbody>
            			</tbody>
					</table>
             	</div>
         	</div>


			<div class="span3" style="height:290px; width:270px;">
			    <div class="pull-right">
    				<form class="form-search" style="margin-bottom:10px;" id="organizationSearchForm">
			    		<div class="input-append">
    						<input type="text" class="span2 search-query" id="oraganizationQueryTxt">
    						<button class="btn" type="submit" id="organizationSearchBtn">Search</button>
    					</div>
    				</form>
	    		</div>
    	 		<div class="clearfix"></div>
    			<div style="border:1px solid #cccccc">
		    		<div>
        		 		<table style="margin-bottom:0px" class="table table-bordered">
                		<thead>
                    		<tr>
                        		<td style="width:200px;">หน่วยงาน</td>
                    		</tr>
                		</thead>
                		</table>
            		</div>
            		<div style="height:214px;overflow:auto;">
                		<table style="margin-bottom:0px;" class="table table-bordered" id="organizationSearchTbl">
                			<tbody>
                			</tbody>
						</table>
            		</div>
    			</div>
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
<tr data-id="{{owner.id}}">
	<td><a href="#" class="removeOrganizationTarget"><i class="icon icon-trash"></i></a> {{owner.name}}</td>
	<td style="width:100px;">
			<input type="text" class="proposalAllocated" id="amountAllocated-{{id}}" style="width:82px; text-align:right;" value="{{targetValue}}"/>
	</td>
	<td style="width:160px;">
			<input type="text" class="budgetAllocated" id="budgetAllocated-{{id}}" style="width:140px; text-align:right;" value="{{activityPerformance.budgetAllocated}}"/>
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


<script src="<c:url value='/resources/js/pages/m73f03.js'/>"></script>

<script type="text/javascript">
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