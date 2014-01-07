<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<h4>รายงานทะเบียนแผนปฏิบัติการและกิจกรรม</h4> 
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

	<div id="mainCtr">
		
		<div>
		<select id="orgSLT">
			<option value="0"> เลือกทุกฝ่าย </option>
		<c:forEach items="${orgList}" var="org">
			<option value="${org.id}"> ${org.name} </option>
		</c:forEach>
		</select>
		</div>
		
		<button id="reportBTN" class="btn btn-info"> แสดงรายงาน </button>
	</div>

	</div>
</div>
</div>


<script id="loadingTemplate" type="text/x-handler-template">
	<div>Loading <img src="<c:url value='/resources/graphics/spinner_bar.gif'/>"/></div>
</script>

<script type="text/javascript">
	var fiscalYear = "${fiscalYear}";

	$(document).ready(function() {		

		$("#reportBTN").on("click", function() {
			var orgSelected = $("#orgSLT").val();
			
			// now call upon report
			loadReport(appUrl("/m81r05.xls/"+fiscalYear+ "/" +  orgSelected +  "/file/m81r05.xls"));
			
		});
	});
</script>
