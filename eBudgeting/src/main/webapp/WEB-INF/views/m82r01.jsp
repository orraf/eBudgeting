<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<h4>การตรวจสอบความเชื่อมโยง</h4> 
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
				<a href="#" class="btn btn-primary" id="saveAssignTargetBtn">บันทึกข้อมูล</a>  
				<a href="#" class="btn" id="cancelBtn">ยกเลิก</a>
			</div>
		</div>

		<div id="mainCtr">
			<div id="mainTbl"></div>
		</div>

	</div>
</div>
</div>

<script id="objectiveTblTemplate" type="text/x-handler-template">
<table class="table table-bordered table-striped" id="objectiveTbl">
	<thead>
		<tr>
			<td>ชื่อกิจกรรม</td>
			<td style="width:120px;">รหัสของ parent</td>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
</script>

<script id="objectiveTbodyTemplate" type="text/x-handler-template">
<tr>
	<td><div style="padding-left:{{padding}}px">[({{id}})/{{code}}] {{name}}</div></td>
	<td style="text-align: right">{{parentPath}}</td>
</tr>
</script>

<script src="<c:url value='/resources/js/pages/m82r01.js'/>"></script>

<script type="text/javascript">
var organizationId = "${organizationId}";
var fiscalYear = "${fiscalYear}";

var mainTblView = new MainTblView();

$(document).ready(function() {
	
	mainTblView.render();
	
});
</script>