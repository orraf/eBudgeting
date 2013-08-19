<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<div class="hero-unit white">

<div class="row">
	<div class="span11" id="messageBox">

	</div>


	<div class="span3 menu" id="main1" data-id="1">
		
	</div>
	
	<div class="span3 menu" id="main2" data-id="2">
		
	</div>
	
	<div class="span4 menu" id="main3" data-id="3">
		
	</div>
</div>
</div>
<div id="result"> 

</div>

<script id="alertNoReportTemplate" type="text/x-handlebars-template">
<div class="alert alert-block">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <h4 style="padding-bottom: 10px;">เดือนนี้ยังมีกิจกรรมที่ยังไม่ได้รายงานผล!</h4>
    	<ul>
			{{#each this}}
				<li> {{name}} </li>
			{{/each}}
		<ul>
</div>
</script>

<script id="menuTemplate" type="text/x-handlebars-template">
{{#each this}}
<div style="margin-bottom: 25px;">
	<h3 style="margin-bottom: 0px;">{{name}}</h3>
	<ul style="padding-top:0px;">
		{{#each menus}}
			<li>{{name}}
				<ul>
					{{#each menus}}
					<li>
					{{#if disabled}} 
						{{name}}
					{{else}}
						<a href="{{link}}" >{{name}}</a>
					{{/if}}
					</li>
					{{/each}}
				</ul>
			</li>
		{{/each}}
	</ul>
</div>
{{/each}}
</script>

<script id="subMenuTemplate" type="text/x-handlebars-template">
{{#each this}}
<ul style="padding-top:0px;">
		<li>
					{{#if disabled}} 
						{{name}}
					{{else}}
						<a id="{{id}}" data-type="{{type}}" {{#if link}}href="{{link}}"{{else}}href="#"{{/if}}>{{name}}</a>
					{{/if}}

			</li>
		
	</ul>	
{{/each}}
</script>

<sec:authorize access="hasRole('ROLE_USER_PLAN')">
<script type="text/javascript">
var ROLE_USER_PLAN = true;
</script>
</sec:authorize>


<script type="text/javascript">
var menuCode='${menuCode}';
var menuLevel='${menuLevel}';
var fiscalYear = parseInt("${currentRootFY.fiscalYear}");

var menuJson = [{
	name: "ข้อมูลพื้นฐานหน่วยงาน (m1)",
	code: "m1",
	menus: [{name: "ข้อมูลพื้นฐานงบประมาณ(m11)", code: "m11", menus: [
				{name: "m1f05: เพิ่มข้อมูลเริ่มต้นปีงบประมาณ", code:"m1f05", link: "page/m1f05/"}
	        	]}
	        	]
},{
	name: "ระบบทะเบียน (m5)", code: "m5",
	menus: [  
	         {name: "ทะเบียนสายยุทธศาสตร์การจัดสรรงบประมาณ (m51)", code: "m51", menus: 
	        	 [
	        	  //{name: "m51f01:(1) ทะเบียนยุทธศาสตร์การจัดสรร", code: "m51f01", link: "page/m51f01/"},
	 	         {name: "m51f01: (1) ทะเบียนแผนงาน", code: "m51f01", link: "page/m51f01/"},
		        // {name: "m51f03: ทะเบียนเป้าหมายเชิงยุทธศาสตร์", code: "m51f03", link: "page/m51f03/"},
		        // {name: "m51f04: ทะเบียนเป้าหมายบริการกระทรวง", code: "m51f04", link: "page/m51f04/"},
		         {name: "m51f02: (2) ทะเบียนยุทธศาสตร์", code: "m51f02", link: "page/m51f02/"},
		        // {name: "m51f05: ทะเบียนเป้าหมายบริการหน่วยงาน", code: "m51f06", link: "page/m51f05/"},
		         {name: "m51f03: (3) ทะเบียนผลผลิต/โครงการ", code: "m51f03", link: "page/m51f03/"},
		         {name: "m51f04: (4) ทะเบียนกิจกรรมหลัก", code: "m51f04", link: "page/m51f04/"},
		         {name: "m51f05: (5) ทะเบียนแผนปฏิบัติการ", code: "m51f05", link: "page/m51f05/"},
		         {name: "m51f06: (6) ทะเบียนกิจกรรมรอง", code: "m51f06", link: "page/m51f06/"},
		        // {name: "m51f10: (7) ทะเบียนกิจกรรมย่อย", code: "m51f10", link: "page/m51f10/"},
		        // {name: "m51f11: (8) ทะเบียนกิจกรรมเสริม", code: "m51f11", link: "page/m51f11/"},
		        // {name: "m51f12: (9) ทะเบียนกิจกรรมสนับสนุน", code: "m51f12", link: "page/m51f12/"},
		        // {name: "m51f13: (10) ทะเบียนกิจกรรมรายละเอียด", code: "m51f13", link: "page/m51f13/"},
		        //{name: "m51f14: ทะเบียนรายการและรายการย่อย", code: "m51f14", link: "page/m51f14/"},
		        // {name: "m51f15: ทะเบียนรายการกลาง", code: "m51f15", link: "page/m51f15/"},
		        // {name: "m51f16: ทะเบียนรายการหลักสำหรับบันทึกงบประมาณกิจกรรม", code: "m51f16", link: "page/m51f16/"},
		         {name: "m51f18: ทะเบียนหน่วยนับ", code: "m51f18", link: "page/m51f18/"}
		         ]},{
		        	 name: "ทะเบียนรายการงบลงทุน (m52)", code: "m52", menus: 
			        	 [{name: "m52f01: ทะเบียนรายการงบลงทุน", code: "m52f01", link: "page/m52f01/"},
			        	  {name: "m52f02: ทะเบียนประเภทงบลงทุน", code: "m52f02", link: "page/m52f02/"}]
		         }
	         
	        // ,
		         
	        // {name: "ทะเบียนตามแผนปฏิบัติราชการ (m52)", code: "m52",  menus: 
	        //	 [ {name: "m52f01: ทะเบียนเป้าประสงค์ (เป้าหมาย) เชิงนโยบนาย", code: "m52f01",  link: "page/m52f01/"}]},
	        	 
	       //  {name: "ทะเบียนสายยุทธศาสตร์กระทรวง-หน่วยงาน (m53)", code: "m53",  menus: 
	       // 	 [{name: "m53f01: ทะเบียนยุทธศาสตร์กระทรวง", code: "m53f01",  link: "page/m53f01/"},
     	    //     {name: "m53f02: ทะเบียนกลยุทธ์หน่วยงาน",code: "m53f02",  link: "page/m53f02/"},
     	     //    {name: "m53f03: ทะเบียนกลยุทธ์/วิธีการกรมฯ", code: "m53f03",  link: "page/m53f03/"}
              //   ]},
                 
	        // {name: "ทะเบียนสายกลยุทธ์หลัก (m54)", code: "m54",  menus: 
	        //	 [{name: "m54f01: ทะเบียนแนวทางการจัดสรรงบประมาณ (กลยุทธ์หลัก)", code: "m54f01",  link: "page/m54f01/"}]},
	        // 
	         //{name: "ทะเบียนวิสัยทัศน์-พันธกิจ (m55)",code: "m55",  menus: 
	        //	 [{name: "m55f01: ทะเบียนวิสัยทัศน์หน่วยงาน", code: "m55f01", link: "page/m55f01/"},
	        //	  {name: "m55f02: ทะเบียนพันธกิจหน่วยงาน", code: "m55f02", link: "page/m55f02/"}]}
	         
	         ]
},{
	name: "ระบบการจัดสรรงบประมาณ (m7)", code: "m7", 
	menus: [
		 {name: "การจัดสรรงบประมาณ (m71)",code: "m71",  menus: 
		    [ //{name: "m3f04: การประมวลผลก่อนการจัดสรรงบประมาณ", code: "m3f04", link: "page/m4f01/"},
		     {name: "m71f01: บันทึกการจัดสรรงบประมาณ", code: "m71f01", link: "page/m71f01/"} ]  
		 }, 
		 {name: "การบันทึกกิจกรรมย่อย (m73)",code: "m73",  menus: 
			    [ //{name: "m3f04: การประมวลผลก่อนการจัดสรรงบประมาณ", code: "m3f04", link: "page/m4f01/"},
			     {name: "m73f01: บันทึกกิจกรรมย่อย", code: "m73f01", link: "page/m73f01/"},
			     {name: "m73f02: บันทึกกิจกรรมย่อย (ระดับจังหวัด)", code: "m73f02", link: "page/m73f02/"},
			     {name: "m73f03: บันทึกแผนการดำเนินงานกิจกรรม", code: "m73f03", link: "page/m73f03/"}]  
		 }]
},{
	name: "ระบบรายงาน (r)", code: "m8", 
	menus: [{
		name: "รายงานทะเบียน (m81r)", code: "m81", menus: 
		    [{
		    	name: "m81r01: รายงานแผนปฏิบัติการสำหรับฝ่าย", link: "m81r01.xls/"+fiscalYear+"/file/m81r01.xls", type: "download"
		    },{
		    	name: "m81r02: รายงานแผนปฏิบัติการสำหรับส่วนงาน/สกยจ./สกยอ.", link: "m81r02.xls/"+fiscalYear+"/file/m81r02.xls", type: "download"
		    },{
		    	name: "m81r03: รายงานภาพรวมแผนปฏิบัติการประจำปีของ สกย.", link: "m81r03.xls/"+fiscalYear+"/file/m81r03.xls", type: "download"
		    }, {
		    	name: "m81r04: รายงานตามแผนปฏิบัติการรายกิจกรรม", link: "page/m81r04/"
		    }, {
		    	name: "m81r05: รายงานทะเบียนแผนปฏิบัติการและกิจกรรม", link: "m81r05.xls/" + fiscalYear + "/file/m81r05.xls", type: "download"
		    }, {
		    	name: "m81r06: รายงานแผนปฏิบัติการรายเดือน - แยกตามยุทธศาสตร์", link: "page/m81r06/"
		    }, {
		    	name: "m81r07: รายงานสรุปการใช้งบประมาณ", link: "m81r07.xls/" + fiscalYear + "/file/m81r07.xls", type: "download"
		    }, {
		    	name: "m81r08: รายงานการสรุปการเบิกจ่ายงบลงทุน", link: "m81r08.xls/"+fiscalYear+"/file/m81r08.xls", id: 'm81r08', type: "download"
		    }, {
		    	name: "m81r09: รายงานการสรุปรายการครุภัณฑ์", link: "m81r09.xls/"+fiscalYear+"/file/m81r09.xls", id: 'm81r09', type: "download"
		    }]
	}, {
		name: "รายงานการตรวจสอบ (m82r)", code: "m82", menus : [{
			name: "m82r01: รายงานการตรวจสอบความเชื่อมโยง", code: "m82r01", link: "page/m82r01/"
		}, {
			name: "m82r02: รายงานผังองค์กร", code: "m82r02", link: "page/m82r02/"
		}]
	}]
}];

var menuUserJson = [
//{
//	name: "ระบบการบันทึกเงินคำของบประมาณ (m6)",code: "m6", 
//	menus: [{
//		name: "การจัดทำคำของบประมาณ  (m61)", code: "m61", menus: 
//	         [{name: "m61f03: การบันทึกงบประมาณ ระดับกิจกรรมหลัก",code: "m61f03",  link: "page/m61f03_1/"},
//		      {name: "m61f04: การบันทึกงบประมาณ ระดับรายการ", code: "m61f04", link: "page/m61f04_1/"},
//	          {name: "m61f05: การนำส่งคำของบประมาณ (Sign off) / ถอนนำส่ง (Release)", code: "m61f05", link: "page/m61f05/"}
//	        ]}],
//	       
//	         
//},
{
	name: "ระบบการจัดสรรงบประมาณ (m7)", code: "m7", 
	menus: [{
		name: "การบันทึกกิจกรรมย่อย (m73)",code: "m73",  menus: 
		    [ //{name: "m3f04: การประมวลผลก่อนการจัดสรรงบประมาณ", code: "m3f04", link: "page/m4f01/"},
		     {name: "m73f01: บันทึกข้อมูลกิจกรรมย่อย", code: "m73f01", link: "page/m73f01/"},
		     {name: "m73f02: บันทึกกิจกรรมย่อย (ระดับส่วนงาน)", code: "m73f02", link: "page/m73f02/"},
		     {name: "m73f03: บันทึกและถ่ายค่าเป้าหมายและงบประมาณ (ระดับจังหวัด)", code: "m73f03", link: "page/m73f03/"}]  
	}, {
			name: "การบันทึกแผนการดำเนินงานกิจกรรมย่อย (m74)",code: "m74",  menus: 
			    [ //{name: "m3f04: การประมวลผลก่อนการจัดสรรงบประมาณ", code: "m3f04", link: "page/m4f01/"},
			     {name: "m74f01: บันทึกแผนการดำเนินงานกิจกรรม", code: "m74f01", link: "page/m74f01/"},
			     {name: "m74f02: บันทึกแผนงบลงทุน", code: "m74f02", link : "page/m74f02/"}]
		}]
		   
		   
},{
	name: "ระบบรายงานผลการดำเนินงาน (m8)", code: "m8", 
	menus: [{
		name: "การบันทึกผลการดำเนินงาน (m81)",code: "m81",  menus: [{
		    	name: "m81f01: บันทึกผลการดำเนินงาน", code: "m81f01", link: "page/m81f01/"
		},{
			name: "m81f02: บันทึกผลการดำเนินงานงบลงทุน", code: "m81f02", link: "page/m81f02/"
		}]  
	}]
},{
	name: "ระบบรายงาน (r)", code: "m8", 
	menus: [{
		name: "รายงานทะเบียน (m81r)", code: "m81", menus: 
		    [{
		    	name: "m81r01: รายงานสำหรับส่วนฝ่าย", link: "m81r01.xls/"+fiscalYear+"/file/m81r01.xls", type: "download"
		    },{
		    	name: "m81r02: รายงานแผนปฏิบัติการสำหรับส่วนงาน/สกยจ./สกยอ.", link: "m81r02.xls/"+fiscalYear+"/file/m81r02.xls", type: "download"
		    }, {
		    	name: "m81r04: รายงานตามแผนปฏิบัติการรายกิจกรรม", link: "page/m81r04/"
		    }, {
		    	name: "m81r06: รายงานแผนปฏิบัติการรายเดือน - แยกตามยุทธศาสตร์", link: "page/m81r06/"
		    }, {
		    	name: "m81r08: รายงานการสรุปการเบิกจ่ายงบลงทุน", link: "m81r08.xls/"+fiscalYear+"/file/m81r08.xls", id: "m81r08", type:"download"
		    }, {
		    	name: "m81r09: รายงานการสรุปรายการครุภัณฑ์", link: "m81r09.xls/"+fiscalYear+"/file/m81r09.xls", id: "m81r09", type:"download"
		    }]
	}, {
		name: "รายงานการตรวจสอบ (m82r)", code: "m82", menus : [{
			name: "m82r01: รายงานการตรวจสอบความเชื่อมโยง", code: "m82r01", link: "page/m82r01/"
		}, {
			name: "m82r02: รายงานผังองค์กร", code: "m82r02", link: "page/m82r02/"
		}]
	}]
}];

var menuTemplate = Handlebars.compile($("#menuTemplate").html());
var alertNoReportTemplate = Handlebars.compile($("#alertNoReportTemplate").html());
var mainview;
var e1;


$(document).ready(function() {
	
	
	var MainView = Backbone.View.extend({
		initialize: function(options){
	    	
		},
		subMenuTemplate: Handlebars.compile($("#subMenuTemplate").html()),
		el: "body",
		render: function() {
			$("#main1").html(this.subMenuTemplate(this.menu));
		},
		
		renderWith: function(m) {
			this.menu = m;
			this.render();
		},
		events: {
			"click a" : "menuClick"
		},
		
		menuClick: function(e) {
			if ( $(e.target).attr('data-type') !=  "download" ) {
				
				e1 = e;
				
				var parentDiv = $(e.target).parents('div.menu');
				var li = $(e.target).parent();
				
				//e1=parentDiv;
				var level = parentDiv.attr('data-id');
				
				if(level == 1) {
					// get this index
					var i = parentDiv.find('li').index(li);
					
					this.currentFirstLevelIndex  = i;
					$("#main3").empty();
					
					$("#main2").html(this.subMenuTemplate(this.menu[i].menus));
					
					$("#navbarBreadcrumb").empty();
					$("#navbarBreadcrumb").html("<li><a href='#'>" + $(e.target).html() +"</a></li>");
					
					$.ajax({
						type: 'POST',
						data: {
							level: 0,
							value: this.menu[i].name,
							code: this.menu[i].code
						},
						url: appUrl('/Session/updateNavbarBreadcrumb')
					});
					
					
				} else if (level==2) {
					// get this index
					var i = parentDiv.find('li').index(li);
					
					$("#main3").html(this.subMenuTemplate(this.menu[this.currentFirstLevelIndex].menus[i].menus));
					
					
					$("#navbarBreadcrumb").empty();
					$("#navbarBreadcrumb").html("<li><a href='#'>" + this.menu[this.currentFirstLevelIndex].name +"</a> <span class='divider'>/</span></li>");
					$("#navbarBreadcrumb").append("<li><a href='#'>" + $(e.target).html() +"</a></li>");
					
					$.ajax({
						type: 'POST',
						data: {
							level: 1,
							value: this.menu[this.currentFirstLevelIndex].menus[i].name,
							code: this.menu[this.currentFirstLevelIndex].menus[i].code
						},
						url: appUrl('/Session/updateNavbarBreadcrumb')
					});
					
				} else if (level==3) {
					
				}
			} else {
				loadReport($(e.target).attr('href'));
				return false;
			}
		}
	});
	
	mainView = new MainView();
	
	if (typeof ROLE_USER_PLAN != "undefined"){
		mainView.renderWith(menuJson);
	 	//$("#menuDiv").html(menuTemplate(menuJson));
	 	
	 	if(menuLevel == '0') {
	 		var firstMenu = _.where(menuJson, {code: menuCode})[0];
	 		$("#main2").html(mainView.subMenuTemplate(firstMenu.menus));
	 		mainView.currentFirstLevelIndex = _.indexOf(menuJson, firstMenu);
	 	} else if(menuLevel == '1') {
	 		var firstMenu = _.find(menuJson, function(menu) { return _.where(menu.menus, {code: menuCode}).length > 0; });
			mainView.currentFirstLevelIndex = _.indexOf(menuJson, firstMenu);
			$("#main2").html(mainView.subMenuTemplate(firstMenu.menus));
			
			var secondMenu = _.where(firstMenu.menus, {code: menuCode})[0];
			$("#main3").html(mainView.subMenuTemplate(secondMenu.menus));
	 	}
	 	
	} else {
		mainView.renderWith(menuUserJson);
		//$("#menuDiv").html(menuTemplate(menuUserJson));
		
	}
	
	// now find objective which hasn't report
	var noReports = new ObjectiveCollection();
	noReports.fetch({
		url: appUrl('/Objective/fiscalYear/' + fiscalYear + '/findByActivityTargetReportOfCurrentUser/NoReportCurrentMonth'),
		success: function() {
			if(noReports.length > 0) {
				var json = noReports.toJSON();
				var html= alertNoReportTemplate(json);
				
				$("#messageBox").html(html);
			}
		}, error: function(model, xhr, options) {
			alert("Error Status Code: " + xhr.status + " " + xhr.statusText + "\n" + xhr.responseText);
		}
	});
	
	
	
});

</script>