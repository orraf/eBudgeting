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
	<h4>การจัดทำทะเบียนกิจกรรมย่อย</h4> 
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
			<td style="width:30px;">รหัส</td>
			<td>ชื่อกิจกรรมย่อย</td>
			<td style="width:100px;">เป้าหมาย</td>
			<td style="width:100px;">หน่วยนับ</td>
			<td style="width:100px;">งบประมาณที่ได้รับจัดสรร (บาท)</td>

		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>
<script id="mainTblTbodyTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id="{{id}}">
	<td>{{code}}</td>
	<td {{#if parent}}style="padding-left:48px;"{{/if}}>
		{{name}} </div>
	</td>
	<td><ul style="list-style-type: none;margin:0px;padding: 0px; text-align:center;">
		{{#each targets}}
			<li data-id="{{id}}"><a href="#" class="assignTargetLnk">{{formatNumber targetValue}}</a></li>
		{{/each}}
		</ul>
	</td>
	<td><ul style="list-style-type: none;margin:0px;padding: 0px; text-align:center;">
		{{#each targets}}
			<li>{{unit.name}}</li>
		{{/each}}
		</ul>
	</td>
	<td><ul style="list-style-type: none;margin:0px;padding-right:10px; text-align:right;"">
		{{#each targets}}
			<li>{{formatNumber budgetAllocated}}</li>
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

<script id="modalTemplate" type="text/x-handler-template">
<div>
<form>
	<label>ระบุรหัส</label>
	<input type="text" class="model" id="code" value="{{code}}" data-modelName="code"></input>

	<label>ระบุชื่อกิจกรรม</label>
	<textarea rows="2" class="span5 model" id="nameTxt" data-modelName="name">{{name}}</textarea>
	
	<div id="activityTarget">
		
	</div>
</form>

</div>
</script>

<script id="assignTargetValueModalTemplate" type="text/x-handler-template">
<div id="inputAll">
	<div style="padding-top:7px; padding-right: 20px;height:35px; float:left">
    	<strong> ค่าเป้าหมายรวม: </strong>
	</div>
    <div style="height:35px; float:left" id="totalInputForm">
		<div class="input-append"><input disabled type="text" id="totalInputTxt" style="width:120px;" value="{{formatNumber targetValue}}"><span class="add-on">{{unit.name}}</span></div>
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
                        	<td>รวม: <span id="sumTotalAllocated"></span> {{unit.name}}</td>
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
<tr data-id="{{owner.id}}"><td><a href="#" class="removeOrganizationTarget"><i class="icon icon-trash"></i></a> {{owner.name}}</td>
	<td  style="width:188px">
		<div style="height:35px; float:left" id="totalInputForm">
			<div class="input-append"><input type="text" class="proposalAllocated" id="amountAllocated-{{id}}" style="width:120px; text-align:right;" value="{{targetValue}}"><span class="add-on">{{target.unit.name}}</span></div>
		</div>
	</td>
</tr>
{{/each}}
</script>


<script src="<c:url value='/resources/js/pages/m73f02.js'/>"></script>

<script type="text/javascript">
	var fiscalYear = parseInt("${fiscalYear}");
	
	var currentOrganizationId = "${workAtId}";
	var currentOrganization = new Organization({id: currentOrganizationId});
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
		mainCtrView = new MainCtrView();
		mainCtrView.render();
		
		currentOrganization.fetch();
	});
</script>
