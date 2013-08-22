<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="hero-unit white">
<div id="headLine">
	<h4>การประมวลผลก่อนการปรับลดรอบที่ 2</h4> 
</div>
<div class="row">
	<div class="span11">
		<c:if test="${rootPage == false}">
		    <ul class="breadcrumb" id="headNav">
		    	<c:forEach items="${breadcrumb}" var="link" varStatus="status">
		    		<c:choose>
						<c:when test="${status.last}">
							<li class="active">${link.value}</li>
						</c:when>
						<c:otherwise>
							<li><a href="<c:url value='${link.url}'></c:url>">${link.value}</a> <span class="divider">/</span></li>
						</c:otherwise>
					</c:choose>
		    	</c:forEach>
		    </ul>
	    </c:if>
	
		<div id="modal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body">
				
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" id="cancelBtn">Close</a> 
				<a href="#"	class="btn btn-primary" id="saveBtn">Save changes</a>
			</div>
		</div>
	
		<div id="mainCtr">
		<c:choose>
		<c:when test="${rootPage}">
			<table class="table table-bordered" id="mainTbl">
				<thead>
					<tr>
						<td>เลือกปีงบประมาณที่มีข้อมูลแล้ว</td>
					</tr>
				</thead>
				<tbody>
					
					<c:forEach items="${fiscalYears}" var="fiscalYear">
						<tr>
							<td><a href="#" onClick="doBeginBudgetCutR1(${fiscalYear.fiscalYear})">${fiscalYear.fiscalYear}</a></td>
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
	
<script type="text/javascript">
function doBeginBudgetCutR1(fiscalYear) {
	
	$.ajax({
		url: appUrl('/AllocationRecord/' + fiscalYear + '/R2'),
		method: 'GET',
		success: function() {
			alert("ดำเนินการเรียบร้อยแล้ว");	
		}
	});
	
	return false;
}

$(document).ready(function() {
	var a;

});
</script>