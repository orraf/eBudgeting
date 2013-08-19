<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="hero-unit white">
<div id="headLine">
	<h4>ทะเบียนประเภทงบลงทุน</h4> 
</div>

<div class="row">
	<div class="span11">
		
		<div id="modal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body">
				
			</div>
			<div class="modal-footer">
				
				<a href="#"	class="btn btn-primary" id="saveBtn">บันทึกข้อมูล</a>
				<a href="#" class="btn" id="cancelBtn">ยกเลิก</a> 
			</div>
		</div>
	
		<div id="mainCtr">

		</div>
	
	</div>
</div>
</div>

<script id="modalTemplate" type="text/x-handler-template">
<form>
	<label>รหัส</label>
	<input type="text" id="codeTxt" value="{{code}}">
	<label>ชื่อประเภทงบลงทุน</label>
	<input type="text" id="nameTxt" value="{{name}}">
</form>
</script>

<script id="mainCtrTemplate" type="text/x-handler-template">
<div class="controls" style="margin-bottom: 15px;">
	<a href="#" class="btn btn-info menuNew"><i class="icon icon-file icon-white"></i> เพิ่มประเภท</a>
	<a href="#" class="btn btn-primary menuEdit"><i class="icon icon-edit icon-white"></i> แก้ไข</a>
	<a href="#" class="btn btn-danger menuDelete"><i class="icon icon-trash icon-white"></i> ลบ</a> 
</div>

	{{#if pageParams}}
	{{#with pageParams}}
    <div class="pagination">
        <span style="border: 1px;">พบทั้งสิ้น {{totalElements}} รายการ </span> <ul>
		{{#each page}}
	    <li {{#if isActive}}class="active"{{/if}}><a href="#" class="pageLink" data-id="{{pageNumber}}">
				{{#if isPrev}}&laquo;{{/if}} 
				{{#if isNext}}&raquo;{{/if}}
				{{#if showPageNumber}} {{pageNumber}} {{/if}}

			</a>
		</li>
	    {{/each}}
    </div>
	{{/with}}
	{{/if}}

<table class="table table-bordered" id="mainTbl">
	<thead>
		<tr>
			<td width="20"></td>
			<td>รหัส</td>
			<td>ประเภทงบลงทุน</td>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>

<script id="newRowTemplate" type="text/x-handlebars-template">
	<td>  </td>
	<td>
		 <form class="form-inline">
			<div class="control-group">
				<label class="control-label" for="codeTxt"> <b>รหัส: </b> </label>
				<div class="controls">
					<input id="codeTxt" type='text' placeholder='...' class='span7' value="{{code}}" ></input> <br/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="nameTxt"> <b>ชื่อประเภท: </b> </label>
				<div class="controls">
					<input id="nameTxt" type='text' placeholder='...' class='span7' value="{{name}}" ></input> <br/>
				</div>
			</div>

		</form>

		<button class='btn btn-mini btn-info lineSave'>บันทึก</button>
		<button class='btn btn-mini btn-danger cancelLineSave'>ยกเลิก</button>
	</td>

</script>
<script id="rowTemplate" type="text/x-handler-template">
{{#each this}}
<tr data-id={{id}}>
	<td><input type="radio" name="rowRdo" id="rdo_{{id}}" value="{{id}}"/></td>
	<td>{{code}}</td>
	<td>{{name}}</td>
</tr>
{{/each}}
</script>
	


<script type="text/javascript">
//Global variable
	var assetCategories = new AssetCategoryPagableCollection([], {
		 targetPage: 1
	});
</script>


<script src="<c:url value='/resources/js/pages/m52f02.js'/>"></script>

	
<script type="text/javascript">

$(document).ready(function() {
	
	mainCtrView = new MainCtrView();
	
	assetCategories.fetch({
		success: function() {
			assetCategories.trigger('reset');
		}
	});
	
});
</script>