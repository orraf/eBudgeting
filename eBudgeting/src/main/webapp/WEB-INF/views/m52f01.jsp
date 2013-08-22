<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="hero-unit white">
<div id="headLine">
	<h4>ทะเบียนรายการงบลงทุน</h4>
</div>

<div class="row">
	<div class="span11">

		<div id="modal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body">
				
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" id="closeBtn">ปิดหน้าต่าง</a>
				<a href="#" class="btn" id="saveBtn">บันทึกรายละเอียด</a>  
			</div>
		</div>


		<div class="control-group" id="mainCtr">
			
			
			<div id="mainSlt">
			</div>	
			<div id="mainTbl">
			</div>
		</div>
	</div>
</div>
</div>


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

<script id="assetKindDisabledSelectionTemplate" type="text/x-handler-template">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">ชนิด :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>
</script>

<script id="categorySltTemplate" type="text/x-handlebars-template">
{{#each this}}
<option value="{{id}}" {{#if isSelected}}selected="selected"{{/if}}>{{name}}</option>
{{/each}}
</script>

<script id="modalBodyTemplate" type="text/x-handlebars-template">
<form>
	<label>ประเภทรายการ</label>
	<select id="categorySlt">
		<option value="0">กรุณาเลือกประเภทรายการ</option>
	</select>
	
	<label>ระบุชื่อรายการ</label>
	<textarea rows="2" class="span5" id="nameTxt">{{name}}</textarea>

	<label>ระบุรหัส</label>
	<input type="text" id="codeTxt" value="{{code}}"></input>

	<label>ระบุคำอธิบายรายการ</label>
	<textarea row="3" class="span5" id="descriptionTxt">{{description}}</textarea>

</form>
</script>


<script id="assetGroupSelectionTemplate" type="text/x-handler-template">
<form class="form-horizontal">
<div class="control-group" style="margin-bottom:5px;">
	<label class="control-label">หมวด :</label> 
	<div class="controls">
		<select id="assetGroupSlt" class="span5">
			<option>กรุณาเลือก...</option>
			{{#each this}}<option value={{id}}>[{{code}}] {{name}}</option>{{/each}}
		</select>
	</div>
</div>
	<div id="assetTypeSltDiv">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">ประเภท :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>	
	</div>
	<div id="assetKindSltDiv">
		<div class="control-group"  style="margin-bottom:5px;">
			<label class="control-label">ชนิด :</label>
			<div class="controls">
				<select class="span5" disabled="disabled">
					<option>กรุณาเลือก...</option>
				</select>
			</div> 
		</div>

	</div>
</form>
</script>

<script  id="mainTblTemplate" type="text/x-handler-template">
<div class="controls" style="margin-bottom: 15px;">
	<div class="pull-left" style="margin-bottom: 15px;"> 
		<a href="#" class="btn btn-info menuNew"><i class="icon icon-file icon-white"></i> เพิ่มชื่อทะเบียน</a>
	</div>
</div>
<div class="clearfix"></div>
<table class="table table-bordered">
	<thead>
		<tr>
			<td style="width:30px;"></td>
			<td style="width:100px;">รหัสรายการ</td>
			<td>ชื่อรายการ</td>
			<td>คำอธิบาย</td>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>

<script id="assetBudgetRowTemplate" type="text/x-handelbars-template">
<td id="td-{{id}}"><a href="#td-{{id}}" class="editObjective menuEdit"><i class="icon-edit icon-blue"></i></a>				
	<a href="#td-{{id}}" class="deleteObjective menuDelete"><i class="icon-trash icon-red"></i></a> </td>
<td> {{code}} </td>
<td> {{name}} </td>
<td> {{description}} </td>
</script>

<script id="tbodyTemplate" type="text/x-handlebars-template">
{{#each this}}
<tr data-catid="{{id}}" class="{{class}}">
	<td colspan="3">{{name}}<td>
</tr>
{{#each assets}}
<tr data-id="{{id}}">
</tr>
{{/each}}
{{/each}}
</script>



<script src="<c:url value='/resources/js/pages/m52f01.js'/>"></script>

<script type="text/javascript">
<!--
var categories = new AssetCategoryCollection();

var mainTblView;
var assetSltView;

$(document).ready(function() {
	mainTblView = new MainTblView(categories);
	assetSltView = new AssetSelectionView();
	assetSltView.render();
	
	
});

//-->
</script>