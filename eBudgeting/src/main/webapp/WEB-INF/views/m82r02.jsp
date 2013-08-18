<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="hero-unit white">
<div id="headLine">
	<div class="pull-left">
	<h4>รายงานผังองค์กร</h4>
	</div>
	<div class="clearfix"></div>
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
			<div id="mainTbl">
				
				
			<ul id="primaryNav" class="col4">
				
			</ul>
				
			
			
			</div>
		</div>

	</div>
</div>
</div>

<script id="organiationNodeTemplate" type="text/x-handler-template">
<li {{#if isClearLeft}}style="clear: left"{{/if}} id="{{#if isHome}}home{{else}}{{id}}{{/if}}"><a href="#" title="({{abbr}})">{{name}}</a>
</li>
</script>

<script src="<c:url value='/resources/js/pages/m82r02.js'/>"></script>

<script type="text/javascript">
var organizationId = "${organizationId}";
var fiscalYear = "${fiscalYear}";

var mainTblView = new MainTblView();

$(document).ready(function() {
	
	mainTblView.render();
	
});
</script>