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
	<h4>การจัดสรรงบประมาณ</h4> 
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

		<div id="targetValueModal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body"></div>
			<div class="modal-footer">
				<a href="#" class="btn" id="saveBtn">บันทึกข้อมูล</a>  
				<a href="#" class="btn" id="cancelBtn">ยกเลิก</a>
			</div>
		</div>

		<div id="mainCtr">
			<c:choose>
				<c:when test="${rootPage}">
					<table class="table table-bordered" id="mainTbl">
						<thead>
							<tr>
								<td>เลือกปีงบประมาณ</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${fiscalYears}" var="fiscalYear">
							<tr>
								
									<td><a href="./${fiscalYear.fiscalYear}/${fiscalYear.id}/"
										class="nextChildrenLnk">${fiscalYear.fiscalYear} <i
											class="icon icon-chevron-right nextChildrenLnk"></i>
									</a></td>
							
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
			</c:choose>
		</div>

	</div>
</div>
</div>


<script id="mainSelectionTemplate" type="text/x-handler-template">
<form class="form-horizontal">
<div class="control-group" style="margin-bottom:5px;">
	<label class="control-label">แผนงาน :</label> 
	<div class="controls">
		<select id="type101Slt" class="span5">
			<option>กรุณาเลือก...</option>
			{{#each this}}<option value={{id}}>[{{code}}] {{name}}</option>{{/each}}
		</select>
	</div>
</div>
	<div id="type102Div">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">ยุทธศาสตร์ :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>	
	</div>
	<div id="type103Div">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">ผลผลิต/โครงการ :</label>
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
			<label class="control-label">ยุทธศาสตร์ :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>
</script>


<script id="type103DisabledSelection" type="text/x-handler-template">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">ผลผลิต/โครงการ :</label>
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

<script id="budgetInputSelectionTemplate" type="text/x-handler-template">
<select id="budgetType_{{this.id}}" multiple="multiple" style="height: 100px;" class="span3">
	{{#if this}}
	{{#each this.children}}
		<option value="{{this.id}}" {{#if this.selected}}selected='selected'{{/if}}>{{this.name}}</option>
	{{/each}}
{{else}} {{/if}}
</select>
</script>

<script id="strategySelectionTemplate" type="text/x-handler-template">
<select id="strategySlt" multiple="multiple" style="height: 100px;" class="span2" >
	{{#each this}}
	<option value="{{id}}" {{#if selected}}selected='selected'{{/if}}>{{name}}</option>
	{{/each}}
</select>
</script>

<script id="mainCtrTemplate" type="text/x-handler-template">
<div id="mainSelection">
</div>
<div id="mainTbl">
</div>
</script>
<script id="mainTblTemplate" type="text/x-handler-template">
<table class="table table-bordered" id="headerTbl" style="margin-bottom:0px; width:875px; table-layout:fixed;">
	<thead>
		<tr>
			<td style="width:20px;">#</td>
			<td style="width:246px;"><strong>แผนงาน/กิจกรรม ประจำปี {{this.0.fiscalYear}}</strong></td>
			<td style="width:60px;">เป้าหมาย</td>
			<td style="width:60px;">หน่วยนับ</td>
			<td style="width:80px;">งบประมาณปี  {{this.0.fiscalYear}}</td>
			<td style="width:80px;">ปี  {{next this.0.fiscalYear 1}}</td>
			<td style="width:80px;">ปี  {{next this.0.fiscalYear 2}}</td>
			<td style="width:80px;">ปี  {{next this.0.fiscalYear 3}}</td>
			<td style="width:15px;padding:0px;">&nbsp;</td>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td></td>
			<td><strong>รวม{{objective.type.name}}{{objective.name}}</strong></td>
			<td><ul  style="list-style:none; margin: 0px;">
				{{#each objective.targets}}
					<li style="list-style:none; padding: 0px;">{{sumTargetValue unit.id ../filterObjectiveBudgetProposals}}</li>
				{{/each}}
				</ul>
			</td>
			<td><ul  style="list-style:none; margin: 0px;">{{#each objective.targets}}<li style="list-style:none; padding: 0px;">{{unit.name}} ({{#if isSumable}}นับ{{else}}ไม่นับ{{/if}})</li>{{/each}}</ul></td>
			<td class="rightAlign"><strong>{{sumAllocationRecords allRecords}}</td>
			<td class="rightAlign"><strong>{{sumProposalNext1Year allProposal}}</strong></td>
			<td class="rightAlign"><strong>{{sumProposalNext2Year allProposal}}</strong></td>
			<td class="rightAlign"><strong>{{sumProposalNext3Year allProposal}}</strong></td>
			<td style="width:15px;padding:0px;">&nbsp;</td>
		</tr>
	</tbody>
</table>
<div class="inRow" style="height: 600px;overflow-y: scroll; width:860px; border-left:1px solid #DDDDDD;">
<table class="table table-bordered" id="mainTbl" style="width:720px; table-layout:fixed; margin: 0px; border-radius: 0px;">
	<tbody>
			{{{childrenNodeTpl this 0}}}
	</tbody>
</table>
</div>
<table class="table table-bordered" id="headerTbl" style="margin-bottom:0px; width:875px; table-layout:fixed;">
	<thead>
		<tr>
			<td>รายการ</td>
		</tr>
	</thead>
</table>

</script>
<script id="childrenNodeTemplate" type="text/x-handler-template">
	<tr data-level="{{this.level}}" data-id="{{this.id}}" class="type-{{type.id}}" showChildren="true" parentPath="{{this.parentPath}}">
		<td style="width:20px;"></td>
		<td style="width:246px;" class="{{#if this.children}}disable{{/if}}">
			<div class="pull-left" style="margin-left:{{this.padding}}px; width:18px;">
					{{#if this.children}}
					<input class="checkbox_tree bullet" type="checkbox" id="bullet_{{this.id}}"/>
					<label class="expand" for="bullet_{{this.id}}"><icon class="label-caret icon-caret-down"></icon></label>
					{{else}}		
						<label class="expand">			
							<icon class="icon-file-alt"></icon>
						<label>
					{{/if}}					
			</div>
			<div class="pull-left" style="width:{{nameWidth}}px;">
					<input class="checkbox_tree" type="checkbox" id="item_{{this.id}}"/>
					<label class="main" for="item_{{this.id}}">
						{{#if this._planBudgetLevel}}<a href="#" class="detail">{{/if}}
						<b>{{this.type.name}}</b> [{{this.code}}] {{this.name}}
						{{#if this._planBudgetLevel}}</a>{{/if}}
					</label>
			</div>
{{#unless this.children}}
			<div class="clearfix">	{{{listProposals this.filterProposals}}}</div>
{{/unless}}
			
		</td>
		<td  style="width:60px;" class="{{#if this.children}}disable{{/if}} centerAlign">
			<span>
				<ul  style="list-style:none; margin: 0px;">
					{{#each filterTargetValues}}
							<li style="list-style:none; padding: 0px;">{{sumTargetValue target.unit.id ../filterObjectiveBudgetProposals}}</li>
					{{/each}}
				</ul>
			</span>
		</td>
		<td  style="width:60px;" class="{{#if this.children}}disable{{/if}} centerAlign">
			<span>
				<ul  style="list-style:none; margin: 0px;">{{#each filterTargetValues}}<li style="list-style:none; padding: 0px;">{{target.unit.name}} ({{#if target.isSumable}}นับ{{else}}ไม่นับ{{/if}})</li>{{/each}}</ul>
			</span>
		</td>
		<td style="width:80px;" class="{{#if this.children}}disable{{/if}} rightAlign">
				<span>{{#if this.allocationRecords}}{{{sumAllocationRecords this.allocationRecords}}}{{else}}-{{/if}}</span>
		</td>

		<td style="width:80px;" class="{{#if this.children}}disable{{/if}} rightAlign">
				<span>{{#if this.filterObjectiveBudgetProposals}}{{{sumProposalNext1Year this.filterObjectiveBudgetProposals}}}{{else}}-{{/if}}</span>
		</td>
		<td style="width:80px;" class="{{#if this.children}}disable{{/if}} rightAlign">
				<span>{{#if this.filterObjectiveBudgetProposals}}{{{sumProposalNext2Year this.filterObjectiveBudgetProposals}}}{{else}}-{{/if}}</span>				
		</td>
		<td style="width:80px;" class="{{#if this.children}}disable{{/if}} rightAlign">
				<span>{{#if this.filterObjectiveBudgetProposals}}{{{sumProposalNext3Year this.filterObjectiveBudgetProposals}}}{{else}}-{{/if}}</span>
		</td>
	</tr>
	{{{childrenNodeTpl this.children this.level}}}  
</script>


<script id="allocationRecordCellTemplate" type="text/x-handler-template">
<div data-id={{id}}>{{formatNumber amountAllocated}} บาท <div class="pull-right"><a href="#" class="editAllocationRecord"><i class="icon-edit"></i></a></div>
</script>


<script id="modalTemplate" type="text/x-handler-template">
<div>
<table class="table table-bordered">
		<thead>
			<tr>
				<td rowspan="2" style="width:120px;">ประเภทงบ</td>
				<td style="width:200px;" colspan="2">เงินสงเคราะห์ (sess)</td>
				<td style="width:200px;" colspan="2">เงินงบประมาณแผ่นดิน</td>
			</tr>
			<tr>
				<td style="width:100px;">งบทำการ</td>
				<td style="width:100px;">งบลงทุน</td>
				<td style="width:100px;">งบทำการ</td>
				<td style="width:100px;">งบลงทุน</td>
			</tr>
		</thead>
		<tbody>
            <tr>
				<td>งบสงเคราะห์</td>
				<td data-budgetTypeId="9" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
				<td data-budgetTypeId="10" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
				<td data-budgetTypeId="13" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
				<td data-budgetTypeId="14" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
			</tr>
        	<tr>
				<td>งบบริหาร</td>
				<td data-budgetTypeId="8" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
				<td data-budgetTypeId="7" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
				<td data-budgetTypeId="11" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
				<td data-budgetTypeId="12" style="text-align:center"><div class="pull-right"><a href="#" class="addAllocationRecord"><i class="icon-plus-sign"></i></a></div></td>
			</tr>
			<tr>
				<td style="text-align:right"><strong>รวม</strong></td>
				<td id="sum1" style="text-align:center;font-weight: bold;text-decoration: underline;"></td>
				<td id="sum2" style="text-align:center;font-weight: bold;text-decoration: underline;"></td>
				<td id="sum3" style="text-align:center;font-weight: bold;text-decoration: underline;"></td>
				<td id="sum4" style="text-align:center;font-weight: bold;text-decoration: underline;"></td>
			</tr>
                
		</tbody>
	</table>
</div>
</script>



<script id="inputAllocationRecordTemplate" type="text/x-handler-template">
<div id="inputAll">
	<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
    	<strong>{{budgetType.name}}</strong> จำนวนจัดสรร:
	</div>
    <div style="height:35px; float:left" id="totalInputForm">
		<div class="input-append"><input type="text" id="totalInputTxt" style="width:120px;" value="{{amountAllocated}}"><span class="add-on">บาท</span></div>
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
                        	<td style="width:237px;">หน่วยงาน</td>
                        	<td>รวม: <span id="sumTotalAllocated"></span> บาท</td>
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

<div style="padding-top:5px;">
<button class="btn btn-mini btn-primary saveProposal">บันทึก</button> <button class="btn btn-mini backToProposal">ย้อนกลับ</button>
</div>

</script>
<script id="organizationSearchTbodyTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{id}}"><td>
	{{#if this._inProposalList}}
		<span class="label label-warning">เลือกแล้ว</span>
	{{else}}
		<a href="#" class="addOrgazation"><i class="icon icon-plus-sign"></i></a>
	{{/if}}

 	{{name}}</td>
</tr>
{{/each}}
</script>

<script id="organizationProposalTbodyTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{owner.id}}"><td><a href="#" class="removeOrganizationProposal"><i class="icon icon-trash"></i></a> {{owner.name}}</td>
	<td  style="width:188px">
		<div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input type="text" class="proposalAllocated" id="amountAllocated-{{id}}" style="width:120px; text-align:right;" value="{{amountAllocated}}"><span class="add-on">บาท</span></div>
		</div>
	</td>
</tr>
{{/each}}
</script>

<script id="inputAllDivTemplate" type="text/x-handler-template">
<div id="inputAll">
	<div class="row">
		<div class="span3" id="budgetTypeSelectionDivL1">แหล่งของเงิน<div></div></div>
		<div class="span3" id="budgetTypeSelectionDivL2">ประเภทงบ<div></div></div>
		<div class="span3" id="budgetTypeSelectionDivL3">หมวดงบ<div></div></div>
	</div>
	<div id="inputDiv" class="span10">
		<form id="input-form" style="margin-bottom:0px;" data-id="{{id}}">
			
		</form>
	</div>
</div>
<button class="btn btn-mini btn-primary saveProposal">บันทึก</button> <button class="btn btn-mini backToProposal">ย้อนกลับ</button>
</script>

<script id="inputFormTemplate" type="text/x-handler-template">
<div id="formulaBox">
				<div>
					<div style="height:35px;">
						จำนวนจัดสรร:
					</div>
				</div>
				<div>
					<div style="height:35px;" id="totalInputForm">
						<div class="input-append"><input type="text" id="totalInputTxt" style="width:120px;" value="{{amountAllocated}}"></input><span class="add-on">บาท</span></div>
					</div>
				</div>
			</div>
	
			<div class="clearfix"></div>
{{#each targets}}
			<div id="formulaBox">
				<div>
					<div style="margin-top:0px;"> ระบุเป้าหมาย </div>
				</div>
				<div style="margin: 0px 8px;">
					<div class="input-append"><input style="width:120px;" type="text" id="targetValue{{unit.id}}" value="{{targetValue}}" data-id={{id}}/><span class="add-on">{{unit.name}}</span></div>
				</div>
			</div>
			<div class="clearfix"></div>
{{/each}}
</script>

<script id="objectiveBudgetProposalListTemplate" type="text/x-handler-template">
<ul>
{{#each this}}
	<li>{{budgetType.name}}: {{formatNumber amountRequest}} บาท</li>
{{/each}}
</ul>
</script>

<script src="<c:url value='/resources/js/pages/m71f01.js'/>"></script>

<script type="text/javascript">
	var fiscalYear = parseInt("${fiscalYear}");
	
	var mainCtrView = null;
	var objectiveCollection = null;
	var budgetTypeSelectionView = null;
	var rootCollection;
	var mainBudgetTypeCollection = null;
	var topBudgetList = ["งบบุคลากร","งบดำเนินงาน","งบลงทุน","งบอุดหนุน","งบรายจ่ายอื่น"];
	var l = null;
	var e1;
	var e2;
	
	var readOnly = "${readOnly}";

	var proposalListTemplate = Handlebars.compile($('#proposalListTemplate').html());
	
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
	
	Handlebars.registerHelper("listProposals", function(proposals) {
		if(proposals == null || proposals.length == 0) return "";
		
		var budgetTypeList = [];
		
		for(var i=0; i< proposals.length; i++) {
 			if(budgetTypeList[proposals[i].budgetType.topParentName] == null) budgetTypeList[proposals[i].budgetType.topParentName] = 0;

 			budgetTypeList[proposals[i].budgetType.topParentName] += proposals[i].amountRequest;
 		}
 		
 		var json=[];
 		for(var i=0; i< topBudgetList.length; i++) {
 			if(budgetTypeList[topBudgetList[i]] != null && budgetTypeList[topBudgetList[i]] > 0) {
 				json.push({name: topBudgetList[i], total: budgetTypeList[topBudgetList[i]]});
 			}
 		}
 		 		
 		return proposalListTemplate(json);
		
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

	$(document).ready(function() {

		mainBudgetTypeCollection = new BudgetTypeCollection();
		mainBudgetTypeCollection.url=appUrl("/BudgetType/fiscalYear/" + fiscalYear +"/mainType");
		mainBudgetTypeCollection.fetch();
		
		mainCtrView = new MainCtrView();
		mainCtrView.render();
	});
</script>
