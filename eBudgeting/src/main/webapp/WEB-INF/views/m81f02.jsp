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
	<h4>การบันทึกผลการดำเนินงานงบลงทุน</h4> 
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

		<div id="assignAssetPlanModal" class="modal wideModal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<a href="#" class="btn btn-primary" id="saveAssetPlanBtn">บันทึกข้อมูล</a>  
				<a href="#" class="btn" id="cancelBtn">กลับหน้าหลัก</a>
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
<table class="table table-bordered table-striped" id="assetAllocationTbl">
	<thead>
		<tr>
			<td style="width:30px;"></td>
			<td>ชื่อรายการ</td>
			<td style="width:50px;">หน่วยงานเจ้าของ</td>
			<td style="width:50px;">หน่วยงานเผู้จัดซื้อ</td>
			<td style="width:50px;">จำนวน</td>
			<td style="width:100px;">งบต่อหน่วย (บาท)</td>
			<td style="width:100px;">รวมจัดสรร (บาท)</td>

		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>
<script id="mainTblTbodyAssetAllocationTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{id}}">
	<td></td>
	<td><a href="#" class="assetPlanLnk">{{assetBudget.name}}</a></td>
	<td style="text-align:center">{{owner.abbr}}</td>
	<td style="text-align:center">{{operator.abbr}}</td>
	<td style="text-align:center">{{formatNumber quantity}}</td>
	<td style="text-align:right; padding-right:10px;">{{formatNumber unitBudget}}</td>
	<td style="text-align:right; padding-right:10px;">{{formatTotalBudget this}}</td>
</tr>
{{/each}}
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
	<div>Loading <img src="<c:url value='/resources/graphics/spinner_bar.gif'/>"/></div>
</script>



<script id="mainCtrTemplate" type="text/x-handler-template">
<div id="mainSelection">
</div>
<div id="mainTbl">
</div>
</script>


<script id="assignAssetPlanModalTemplate" type="text/x-handler-template">
<div id="header">
	<div><strong>รายการ: </strong>{{this.assetBudget.name}}</div>
	<div><strong>จัดสรรให้: </strong>{{this.owner.name}}</div>
	<div><strong>วงเงินงบประมาณ: </strong> {{formatTotalBudget this}} บาท   (<strong>จำนวน: </strong> {{this.quantity}} หน่วย)</div>
{{#if assetMethod}}
	<div><strong>วิธีการจัดซื้อจัดจ้าง: </strong> {{assetMethod.name}}</div>
{{else}}
	<div><strong>วิธีการจัดซื้อจัดจ้าง: </strong> ยังไม่ได้บันทึกแผนการจัดซื้อจัดจ้าง</div>
{{/if}}

	<div><strong>ราคากลาง : </strong> {{#if estimatedCost}}{{formatNumber estimatedCost}} บาท{{else}} ยังไม่ได้ระบุ {{/if}}</div>

</div>
<div id="inputAll">
	<form class="form-horizontal">
{{#if assetMethod}}
		<div id="contractDiv" class="span10" style="margin-bottom: 10px">
			ระบุเลขที่สัญญา : <input type="text" value="{{contractNo}}" placeholder="..." data-field="contractNo" class="span2 assetBudgetAllocationTxt" id="contractNo">
			<a href="#" class="btn btn-deafult" id="findAmountByContractPoBTN">ค้นหาจำนวนเงินตามสัญญา</a><span id="poSearchWaitSpn"></span>
			<br/><div id="resultPODiv"></div><br/>
			จำนวนเงินตามสัญญา: 
				แผน {{formatNumber contractedBudgetPlan}} บาท
				/
				ผล 
				<div class="input-append">
					<input type="text" value="{{contractedBudgetActual}}" placeholder="..." data-field="contractedBudgetActual" class="span2 assetBudgetAllocationTxt" id="contractedBudgetActual" readonly="readonly"><span class="add-on">บาท</span>
				</div> 

		</div>
{{/if}}
		<div id="assetMethodStepDiv" class="span10">
    		<ul class="nav nav-tabs" id="assetTab">
    			<li class="active"><a href="#assetDateTab" data-toggle="tab">ผลการดำเนินการ</a></li>
    			<li><a href="#assetBudgetTab" data-toggle="tab">ผลการเบิกจ่ายงบประมาณ</a></li>
    		</ul>
			<div class="tab-content">
				<div class="tab-pane active" id="assetDateTab"></div>
				<div class="tab-pane" id="assetBudgetTab"></div>
			</div>
		</div>
	</form>
</div>

</script>
<script id="resultPODivTemplate" type="text/x-handler-template">
<table class="table table-condensed">
	<thead>
		<tr>
			<th style="text-align:left;width: 20px;"></th>
			<th style="text-align:left;width: 100px;">เลขที่สัญญา</th>
			<th style="text-align:left;">ชื่อรายการที่จัดซื้อ</th>
			<th style="text-align:left;width: 200px;">จำนวนเงินที่จัดซื้อ</th>
		</tr>
	</thead>
	<tbody>
		{{#each items}}
		<tr>
			<td><input type="checkbox" data-amount="{{assetAmount}}" class="poItem"></td>
			<td>{{poContract}}</td>
			<td>{{faCodeName}} ({{receiveOrgName}})</td>
			<td>{{formatNumber assetAmount}}</td>
		</tr>
		{{/each}}
	</tbody>
</table>
</script>
<script id="budgetPlanTemplate" type="text/x-handler-template">
	<div>
		<strong>จำนวนงวดการเบิกจ่าย : {{length}} งวด</strong>
	</div>
	<div id="budgetPlanDiv" style="margin-top: 14px;">
	</div>
</script>

<script id="budgetPlanTblTemplate" type="text/x-handler-template">
<table class="table table-condensed">
	<thead>
		<tr>
			<td>งวดที่</td>
			<td>วันที่ส่งมอบ/<br/>วันที่เบิกจ่าย (แผน)</td>
			<td>วันที่ส่งมอบ/<br/>วันที่เบิกจ่าย (ผล)</td>
			<td>จำนวนเบิกจ่าย (แผน)</td>
			<td>จำนวนเบิกจ่าย (ผล)</td>
		</tr>
	</thead>
	<tbody>
{{#each this}}
	<tr data-index={{@index}}>
		<td style="text-align:center;padding-top: 14px; width:60px;">{{indexHuman @index}}</td>
		<td style="text-align:center;padding-top: 14px;"> {{formatDate planInstallmentDate}} <br/> {{formatDate planDate}} </td>
		<td style="text-align:center">

			<div class="control-group" style="margin: 0px;">
				<div class="input-append">
					<input type="text" value="{{formatDateDB actualInstallmentDate}}" data-stepId="{{id}}" data-field="actualInstallmentDate" id="actualInstallmentDate_{{@index}}" placeholder="..." class="span2 datepickerTxt assetBudgetPlanTxt"><span class="add-on"><i class="icon-calendar"></i></span>
				</div>
			</div>
			<div class="control-group" style="margin: 0px;">
				<div class="input-append">
					<input type="text" value="{{formatDateDB actualDate}}" data-stepId="{{id}}" data-field="actualDate" id="actualDate_{{@index}}" placeholder="..." class="span2 datepickerTxt assetBudgetPlanTxt"><span class="add-on"><i class="icon-calendar"></i></span>
				</div>
			</div>
		</td>
		<td style="text-align:right;padding-right: 14px;padding-top: 14px;">{{formatNumber planAmount}} บาท <br/>หมายเหตุ:</td>
		<td style="text-align:center">
			<div class="control-group" style="margin: 0px;">
				<div class="input-append">
					<input type="text" value="{{actualAmount}}" placeholder="..." data-field="actualAmount" class="span2 assetBudgetPlanTxt" id="actualAmount_{{@index}}"><span class="add-on">บาท</span>
				</div>
			</div>
			<div class="control-group" style="margin: 0px;">
				<div>
					<input type="text" value="{{remark}}" placeholder="..." data-field="remark" class="assetBudgetPlanTxt" style="width:162px;" id="remark_{{@index}}">
				</div>
			</div>
		</td>
	</tr>
{{/each}}
	</tbody>
</table>
</script>

<script id="stepInputTemplate" type="text/x-handler-template">
<div>
<table class="table table-condensed">
	<thead>
		<tr>
			<td width="100">กิจกรรม</td>
			<td style=""><strong>วันที่เริ่ม (แผน)</strong></td>
			<td style="padding-right:50px;"><strong>วันที่เริ่ม (ผล)</strong></td>
			<td style=""><strong>วันที่สิ้นสุด (แผน)</strong></td>
			<td style="padding-right:50px;"><strong>วันที่สิ้นสุด (ผล)</strong></td>
		</tr>
	</thead>
	<tbody>
{{#each this}}
		<tr data-id="{{id}}" data-index="{{@index}}">
			<td style="border-top: none; padding-top:14px; text-align: right;"><strong>{{name}}</strong></td>
			<td style="border-top: none; padding-top:14px; text-align: center;">{{formatDate planBegin}}</td>
			<td style="border-top: none;">
				<div class="control-group" style="margin: 0px;">
				<div class="input-append date datepicker" data-date-format="dd/mm/yyyy" data-date="">
					<input type="text" value="{{formatDateDB actualBegin}}" data-stepId="{{id}}" data-field="actualBegin" id="actualBegin_{{@index}}" placeholder="..." class="span2 datepickerTxt"><span class="add-on"><i class="icon-calendar"></i></span>
				</div>
				</div>
			</td>
			<td style="border-top: none; padding-top:14px; text-align: center;">{{formatDate planEnd}}</td>
			<td style="border-top: none;">

				<div class="control-group" style="margin: 0px;">
				<div class="input-append date datepicker" data-date-format="dd/mm/yyyy" data-date="">
					<input type="text" value="{{formatDateDB actualEnd}}" data-stepId="{{id}}" data-field="actualEnd" id="actualEnd_{{@index}}" placeholder="..." class="span2 datepickerTxt"><span class="add-on"><i class="icon-calendar"></i></span>
				</div>
				</div>
			</td>
		</tr>
{{/each}}
	</tbody>
</table>
</div>
</script>


<script id="assetMethodOptionTemplate" type="text/x-handler-template">
{{#each this}}
	<option value="{{id}}" {{#if selected}}selected="selected"{{/if}}>{{name}}</option>
{{/each}}
</script>


<script src="<c:url value='/resources/js/pages/m81f02.js'/>"></script>

<script type="text/javascript">
	var fiscalYear = parseInt("${fiscalYear}");
	
	var currentOrganizationId = "${workAtId}";
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
	
	Handlebars.registerHelper("formatTotalBudget", function(assetAllocation) {
		if(assetAllocation != null) {
			return addCommas(assetAllocation.quantity * assetAllocation.unitBudget);
		} 
		return "0";
	});
	
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
		organizationId: currentOrganizationId
	});
	var mainTblView = new MainTblView();

	$(document).ready(function() {
		
		budgetProposalSelectionView.render();
		
	});
</script>
