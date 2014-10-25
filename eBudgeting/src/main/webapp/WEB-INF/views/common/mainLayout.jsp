<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>

<html lang="th">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="<c:url value='/resources/jslibs/html5shiv.js'/>"></script>
      <script src="<c:url value='/resources/jslibs/html5shiv-printshiv.js'/>"></script>
    <![endif]-->	

	
	<link href="<c:url value='/resources/jslibs/jqueryui-1.9.2/css/redmond/jquery-ui-1.9.2.custom.css'/>" rel="stylesheet">
	<link href="<c:url value='/resources/jslibs/bootstrap-2.3.1/css/bootstrap.css'/>" rel="stylesheet">
	<link href="<c:url value='/resources/jslibs/bootstrap-2.3.1/css/font-awesome-3.0.min.css'/>" rel="stylesheet">
	
	
	<link href="<c:url value='/resources/css/slickmap.css'/>" rel="stylesheet">
	<link href="<c:url value='/resources/css/app.css'/>" rel="stylesheet">
	
	<script src="<c:url value='/resources/jslibs/modernizr.custom.16652.js'/>"></script>
	
	<script src="<c:url value='/resources/jslibs/jquery-1.11.1.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/jqueryui-1.9.2/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/jquery.ui.datepicker-th.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/jquery.ui.datepicker.ext.be.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/jquery.fileDownload-1.4.2.js'/>"></script>
	
	<script src="<c:url value='/resources/jslibs/lodash-1.3.1.min.js'/>"></script>
	<!-- 
	<script src="<c:url value='/resources/jslibs/underscore-1.5.1.min.js'/>"></script>
	 -->

	<script src="<c:url value='/resources/jslibs/backbone-1.0.0.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/backbone-relational-0.8.6.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/handlebars-1.0.0.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/moment-2.1/moment-2.1.0.min.js'/>"></script>
	<script src="<c:url value='/resources/jslibs/moment-2.0/lang/th.js'/>"></script>		
	
	<script src="<c:url value='/resources/jslibs/bootstrap-2.3.1/js/bootstrap.js'/>"></script>
		
	<script src="<c:url value='/resources/js/appUtils.js'/>"></script>
	<script src="<c:url value='/resources/js/eBudgeting-app.js'/>"></script>

	
	<title>${title}</title>
</head>

<body>
	<div id="errorModal" class="modal wideModal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body" style="max-height: 800px;">
				
			</div>
			</div>
		</div>
	</div>		

	<c:choose>
		<c:when test="${adminPage == true}">
			<div id="container-header" class="container">
				<tiles:insertAttribute name="header-admin"/>		
			</div>
		</c:when>
		<c:otherwise>
			<div id="container-header" class="container">
				<tiles:insertAttribute name="header"/>		
			</div>	
		</c:otherwise>
	</c:choose>


	<div id="container-body" class="container">
		<tiles:insertAttribute name="body" />
	</div>


	<div id="container-footer" class="container">
		<tiles:insertAttribute name="footer" />
	</div>
	
	<script id="errorModalBodyTemplate" type="text/x-handlebars-template">
		<div>
			<b>Error Timestamp : </b> {{timestamp}}
		</div>
		<div>
			<b>Error Message : </b> {{message}}
		</div>
		<div>
			<b>Stack Trace : </b>
			<pre class="pre-scrollable" style="font-size:0.75em;">{{stackTrace}}</pre>
		</div>
	</script>
	
<script type="text/javascript">
$(document).ready(function() {
   // register Global error handling here .. if there isn't one
   
	$(document).ajaxError( function(event, jqXHR, ajaxSettings, thrownError){
		if(jqXHR.status == 403) {
			alert('Session Timeout,  Please Log in again');
			window.location.reload();
		} else {
			$('#errorModal').find('.modal-header span').html("<h3>Error Status Code: " + jqXHR.status + " " + jqXHR.statusText +"</h3>");
			
			var json =jqXHR.responseJSON;
			
			console.log(jqXHR);
			
			var errorTemplate = Handlebars.compile($("#errorModalBodyTemplate").html());
			
			$('#errorModal').find('.modal-body').html(errorTemplate(json));
			
			$('#errorModal').modal({show: true, backdrop: 'static', keyboard: false});	
		}
		
	});
 });
</script>

</body>
</html>
