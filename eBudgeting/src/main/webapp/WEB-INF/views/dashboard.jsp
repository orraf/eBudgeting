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

<sec:authorize access="hasRole('PMS_BGT_ADMIN')">
<script type="text/javascript">
var PMS_BGT_ADMIN = true;
</script>
</sec:authorize>




<script type="text/javascript">
var menuCode='${menuCode}';
var menuLevel='${menuLevel}';
var fiscalYear = parseInt("${currentRootFY.fiscalYear}");

var userGroups = '${userGroups}'.split(",");

var menuJson = [{
	name: "ข้อมูลพื้นฐานหน่วยงาน (m1)", code: "m1",
	menus: [{
		name: "ข้อมูลพื้นฐานงบประมาณ(m11)", code: "m11", parent: 'm1', menus: [{
			name: "m1f05: เพิ่มข้อมูลเริ่มต้นปีงบประมาณ", code:"m1f05", parent: 'm11', link: "page/m1f05/", group: ["PMS_BGT_ADMIN"]
		}]
	}]
},{
	name: "ระบบทะเบียน (m5)", code: "m5",
	menus: [{
		name: "ทะเบียนสายยุทธศาสตร์การจัดสรรงบประมาณ (m51)", parent: 'm5' ,code: "m51", menus: [{
			name: "m51f01: (1) ทะเบียนแผนงาน", code: "m51f01", parent: 'm51' , link: "page/m51f01/", group: ["PMS_BGT_ADMIN"]
		},{
			name: "m51f02: (2) ทะเบียนยุทธศาสตร์", code: "m51f02", parent: 'm51', link: "page/m51f02/", group: ["PMS_BGT_ADMIN"]
		},{
			name: "m51f03: (3) ทะเบียนผลผลิต/โครงการ", code: "m51f03", parent: 'm51', link: "page/m51f03/", group: ["PMS_BGT_ADMIN"]
		},{	
			name: "m51f04: (4) ทะเบียนกิจกรรมหลัก", code: "m51f04", parent: 'm51', link: "page/m51f04/", group: ["PMS_BGT_ADMIN"]
		},{
			name: "m51f05: (5) ทะเบียนแผนปฏิบัติการ", code: "m51f05", parent: 'm51', link: "page/m51f05/", group: ["PMS_BGT_ADMIN"]
		},{
			name: "m51f06: (6) ทะเบียนกิจกรรมรอง", code: "m51f06", parent: 'm51', link: "page/m51f06/", group: ["PMS_BGT_ADMIN"]
		},{
			name: "m51f18: ทะเบียนหน่วยนับ", code: "m51f18", parent: 'm51', link: "page/m51f18/", group: ["PMS_BGT_ADMIN"]
		}
	]},{
		name: "ทะเบียนรายการงบลงทุน (m52)", code: "m52", parent: 'm5', menus: [{
			name: "m52f01: ทะเบียนรายการงบลงทุน", code: "m52f01", parent: 'm52', link: "page/m52f01/", group: ["PMS_BGT_ADMIN"]
		}, {
			name: "m52f02: ทะเบียนประเภทงบลงทุน", code: "m52f02", parent: 'm52', link: "page/m52f02/", group: ["PMS_BGT_ADMIN"]
		}]
	}]
},{
	name: "ระบบการจัดสรรงบประมาณ (m7)", code: "m7", 
	menus: [{
		name: "การจัดสรรงบประมาณ (m71)",code: "m71", parent: 'm7',  menus:[{
			name: "m71f01: บันทึกการจัดสรรงบประมาณ", code: "m71f01", parent: 'm71', link: "page/m71f01/", group: ["PMS_BGT_ADMIN"]
		}]  
	},{
		name: "การบันทึกกิจกรรมย่อย (m73)",code: "m73", parent: 'm7',  menus:[{
			name: "m73f01: บันทึกกิจกรรมย่อย (ระดับฝ่าย/ส่วนงาน)", code: "m73f01", parent: 'm73', link: "page/m73f01/", group: ["PMS_USER"]
		},{
			name: "m73f02: การจัดสรรกิจกรรมย่อย (ระดับส่วนงาน)", code: "m73f02", parent: 'm73', link: "page/m73f02/", group: ["PMS_USER"]
		},{
			name: "m73f03: บันทึกและถ่ายค่าเป้าหมายและงบประมาณ (ระดับจังหวัด)", code: "m73f03", parent: 'm51', link: "page/m73f03/", group: ["PMS_USER"]
		}]  
	}, {
		name: "การบันทึกแผนการดำเนินงานกิจกรรมย่อย (m74)",code: "m74", parent: 'm7',  menus: [{
			name: "m74f01: บันทึกแผนการดำเนินงานกิจกรรม", code: "m74f01", parent: 'm74', link: "page/m74f01/", group: ["PMS_USER"]
		}, {
			name: "m74f02: บันทึกแผนงบลงทุน", code: "m74f02", parent: 'm74', link : "page/m74f02/", group: ["PMS_USER"]
		}]
	}]
},{
	name: "ระบบรายงานผลการดำเนินงาน (m8)", code: "m8", 
	menus: [{
		name: "การบันทึกผลการดำเนินงาน (m81)",code: "m81", parent: 'm8',  menus: [{
		   	name: "m81f01: บันทึกผลการดำเนินงาน", code: "m81f01", parent: 'm81', link: "page/m81f01/", group: ["PMS_USER"]
		},{
			name: "m81f02: บันทึกผลการดำเนินงานงบลงทุน", code: "m81f02", parent: 'm81', link: "page/m81f02/", group: ["PMS_USER"]
		}]  
	}]
},{
	name: "ระบบรายงาน (r)", code: "mr", 
	menus: [{
		name: "รายงานทะเบียน (m81r)", code: "m81r", parent: 'mr', menus: [{
	    	name: "m81r01: รายงานแผนปฏิบัติการสำหรับฝ่าย", code: "m81r01", parent: 'm81r', link: "m81r01.xls/"+fiscalYear+"/file/m81r01.xls", type: "download", group: ["PMS_BGT_ADMIN"]
	    },{
	    	name: "m81r02: รายงานแผนปฏิบัติการสำหรับส่วนงาน/สกยจ./สกยอ.", code: "m81r02", parent: 'm81r', link: "m81r02.xls/"+fiscalYear+"/file/m81r02.xls", type: "download", group: ["PMS_USER"]
	    },{
	    	name: "m81r03: รายงานภาพรวมแผนปฏิบัติการประจำปีของ สกย.", code: "m81r03", parent: 'm81r', link: "m81r03.xls/"+fiscalYear+"/file/m81r03.xls", type: "download", group: ["PMS_BGT_ADMIN"]
	    }, {
	    	name: "m81r04: รายงานตามแผนปฏิบัติการรายกิจกรรม", code: "m81r04", parent: 'm81r', link: "page/m81r04/", group: ["PMS_USER"]
	    }, {
	    	name: "m81r05: รายงานทะเบียนแผนปฏิบัติการและกิจกรรม", code: "m81r05", parent: 'm81r', link: "m81r05.xls/" + fiscalYear + "/file/m81r05.xls", type: "download", group: ["PMS_BGT_ADMIN"]
	    }, {
	    	name: "m81r06: รายงานแผนปฏิบัติการรายเดือน - แยกตามยุทธศาสตร์", code: "m81r06", parent: 'm81r', link: "page/m81r06/", group: ["PMS_USER"]
	    }, {
	    	name: "m81r07: รายงานสรุปการใช้งบประมาณ", code: "m81r07", parent: 'm81r', link: "m81r07.xls/" + fiscalYear + "/file/m81r07.xls", type: "download", group: ["PMS_BGT_ADMIN"]
	    }, {
	    	name: "m81r08: รายงานการสรุปการเบิกจ่ายงบลงทุน (ทั้ง สกย.)", code: "m81r08", parent: 'm81r', link: "m81r08.xls/"+fiscalYear+"/file/m81r08.xls", id: 'm81r08', type: "download", group: ["PMS_USER"]
	    }, {
	    	name: "m81r11: รายงานการสรุปการเบิกจ่ายงบลงทุน (เฉพาะรายการที่รับผิดชอบ)", code: "m81r11", parent: 'm81r', link: "m81r11.xls/"+fiscalYear+"/file/m81r11.xls", id: 'm81r11', type: "download", group: ["PMS_USER"]
	    }, {
	    	name: "m81r09: รายงานการสรุปรายการครุภัณฑ์ (ทั้ง สกย.)", code: "m81r09", parent: 'm81r', link: "m81r09.xls/"+fiscalYear+"/file/m81r09.xls", id: 'm81r09', type: "download", group: ["PMS_USER"]
	    }, {
	    	name: "m81r10: รายงานการสรุปรายการครุภัณฑ์ (เฉพาะรายการที่รับผิดชอบ)", code: "m81r10", parent: 'm81r', link: "m81r10.xls/"+fiscalYear+"/file/m81r10.xls", id: 'm81r10', type: "download", group: ["PMS_USER"]
	    }]
	},{
		name: "รายงานการตรวจสอบ (m82r)", code: "m82", parent: 'mr', menus : [{
			name: "m82r01: รายงานการตรวจสอบความเชื่อมโยง", parent: 'm82r', code: "m82r01", link: "page/m82r01/", group: ["PMS_USER"]
		}, {
			name: "m82r02: รายงานผังองค์กร", parent: 'm82r', code: "m82r02", link: "page/m82r02/", group: ["PMS_USER"]
		}]
	}]
}];

var menuTemplate = Handlebars.compile($("#menuTemplate").html());
var alertNoReportTemplate = Handlebars.compile($("#alertNoReportTemplate").html());
var mainview;
var e1;

//	find only the last menu of our right!
var l2Menu = _.flatten(_.flatten(menuJson, 'menus'), 'menus').filter(function(menu) {
	for(var i=0; i<userGroups.length; i++) {
		if(userGroups[i].indexOf(menu.group) >= 0) {
			return true;
		}
	 }
	return  false;
});

var l2MenuCode = _.pluck(l2Menu,'code');

$(document).ready(function() {
	
	// now prune the menu
	for(var i=0; i<menuJson.length; i++) {
		var m1 = menuJson[i];
		for(var j=0; j<m1.menus.length; j++) {
			var m2 = m1.menus[j];
			
			m2.menus = _.filter(m2.menus, function(menu) {
				return l2MenuCode.lastIndexOf(menu.code) >= 0;
			});
		}
		
		m1.menus = _.filter(m1.menus, function(menu) {
			return menu.menus.length > 0;
		});
	}
	
	menuJson =_.filter(menuJson, function(menu) {
		return menu.menus.length > 0;
	});
	
	
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
	
	
	mainView.renderWith(menuJson);
 	
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