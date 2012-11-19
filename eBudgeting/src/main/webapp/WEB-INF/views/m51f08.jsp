<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="headLine">
	
</div>

<div class="row">
	<div class="span12">

		<div id="modal" class="modal hide fade">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<span style="font-weight: bold;"></span>
			</div>
			<div class="modal-body">
				
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" id="closeBtn">ยกเลิก</a>
				<a href="#" class="btn" id="saveBtn">บันทึกข้อมูล</a>  
			</div>
		</div>


		<div class="control-group" id="mainCtr">
			
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
								<td> <a href="./${fiscalYear.fiscalYear}/" class="nextChildrenLnk">${fiscalYear.fiscalYear}<i class="icon icon-chevron-right nextChildrenLnk"></i> </a></td>
						</tr>
						</c:forEach>
					</tbody>
				</table>			
			</c:when>
			</c:choose>	
		</div>


	</div>
</div>

<script id="mainCtrTemplate" type="text/x-handler-template">
<div class="controls" style="margin-bottom: 15px;">
	<a href="#" class="btn btn-mini btn-info menuNew"><i class="icon icon-file icon-white"></i> เพิ่มรายการ</a>
	<a href="#" class="btn btn-mini btn-primary menuEdit"><i class="icon icon-edit icon-white"></i> แก้ไข</a>
	<a href="#" class="btn btn-mini btn-danger menuDelete"><i class="icon icon-trash icon-white"></i> ลบ</a>

	{{#if pageParams}}
	{{#with pageParams}}
    <div class="pagination">
        <span style="border: 1px;">พบทั้งสิ้น {{totalElements}} รายการ </span> <ul>
		{{#each page}}
	    <li {{#if isActive}}class="active"{{/if}}><a href="#" class="pageLink" data-id="{{pageNumber}}">{{pageNumber}}</a></li>
	    {{/each}}
    </div>
	{{/with}}
	{{/if}}

</div>
<table class="table table-bordered" id="mainTbl">
	<thead>
		<tr>
			<td style="width:20px;"></td>
			<td style="width:40px;">รหัส</td>
			<td>ชื่อ{{name}}</td>

			{{#if hasUnit}}
			<td style="width:100px;">หน่วยนับ</td>
			{{/if}}

			{{#if hasParent}}
			<td style="width:140px;" type-id="102">เชื่อมโยง{{parentTypeName}}</td>
			{{/if}}

			{{#each relatedTypenameList}}
				<td style="width:140px;">เชื่อมโยง{{this}}</td>
			{{/each}}
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<div class="controls" style="margin-bottom: 15px;">
	<a href="#" class="btn btn-mini btn-info menuNew"><i class="icon icon-file icon-white"></i> เพิ่มรายการ</a>
	<a href="#" class="btn btn-mini btn-primary menuEdit"><i class="icon icon-edit icon-white"></i> แก้ไข</a>
	<a href="#" class="btn btn-mini btn-danger menuDelete"><i class="icon icon-trash icon-white"></i> ลบ</a>
</div>
</script>

<script id="objectiveRowTemplate" type="text/x-handelbars-template">
<td><input type="radio" name="rowRdo" id="rdo_{{id}}" value="{id}}"/></td>
	<td> {{code}} </td>
	<td> {{name}} </td>

	{{#if hasUnit}}
	<td class="unitLink"> <ul class="noBullet">{{#each units}}</li>{{name}}<li>{{/each}}</ul></td>
	{{/if}}

	{{#if hasParent}}
	<td class="parentLink"> {{parent.code}} {{parent.name}} </td>
	{{/if}}	

	{{#each relations}}
		<td>{{#if parent}} [{{parent.code}}] {{parent.name}} {{/if}}</td>
	{{/each}}
	
</script>

<script id="tbodyTemplate" type="text/x-handlebars-template">
{{#each this}}
<tr data-id="{{id}}">
</tr>
{{/each}}
</script>


<script id="modalBodyTemplate" type="text/x-handlebars-template">
<form>
	<label>ระบุชื่อ{{type.name}}</label>
	<textarea rows="2" class="span5" id="nameTxt">{{name}}</textarea>
	
	{{#if hasUnit}}
	<label>ระบุหน่วยนับ</label>
	<select class="span2" id="unitSlt">
		<option value="0">กรุณาเลือก</option>
		{{#each unitSelectionList}}
			<option value="{{this.id}}" {{#if this.selected}}selected='selected'{{/if}}>{{this.name}}</option>
		{{/each}}
	</select>
	{{/if}}

	{{#if hasParent}}
	<label>เชื่อมความสัมพันธ์{{parentTypeName}}</label>
	<select class="span5" id="parentSlt">
		<option value="0">ยังไม่ระบุ</option>
		{{#each parentSelectionList}}
			<option value="{{this.id}}" {{#if this.selected}}selected='selected'{{/if}}>{{this.code}} {{this.name}}</option>
		{{/each}}
	</select>
	{{/if}}

	{{#each relations}}
	<label>เชื่อมความสัมพันธ์{{relationInfo.name}}</label>
	<select class="span5 relationSlt" parentId="{{relationInfo.parentId}}" id="relation-{{relationInfo.parentId}}">
		<option value="0">ยังไม่ระบุ</option>
		{{#each selectionList}}
			<option value="{{this.id}}" {{#if this.selected}}selected='selected'{{/if}}>{{this.code}} {{this.name}}</option>
		{{/each}}
		</select>
	{{/each}}

</form>
</script>

<script type="text/javascript">
<!--
var objectiveId = "${objective.id}";
var fiscalYear = "${fiscalYear}";
var typeId = "${typeId}";
var objectiveCollection = new ObjectivePagableCollection([], {
	fiscalYear: fiscalYear, objectiveTypeId: typeId, targetPage: 1
});

var pageUrl = "/page/m51f08/";

var mainTblView;

var e1;

var objectiveType;
var hasParent = "${hasParent}";
var hasUnit = "${hasUnit}";
var relatedTypeString= "${relatedTypeString}";
var relatedTypeList = relatedTypeString.split(' ');

var relatedTypeNameString = "${relatedTypeNameString}";
var relatedTypenameList = relatedTypeNameString.split(' ');

var hasParent = "${hasParent}";
var parentTypeName = "${parentTypeName}";
var parentTypeId = "${parentTypeId}";

var hasunit = "${hasUnit}";


var listTargetUnits = new TargetUnitCollection();
listTargetUnits.fetch({
	url: appUrl('/TargetUnit/')
});

var listObjective = new Array();



$(document).ready(function() {

	var ModalView = Backbone.View.extend({
		modalBodyTemplate: Handlebars.compile($("#modalBodyTemplate").html()),
		
		initialize: function(options) {
			
		},
		
		el: "#modal",
		
		events: {
			"click #closeBtn" : "close",
			"click #saveBtn" : "save"
		},
		
		close : function() {
			this.$el.modal('hide');
		},
		save : function(e) {
			var newObj = this.currentObjective.get('id') == null;
			// now collect data 
			this.currentObjective.set('name', this.$el.find('#nameTxt').val());
			
			// now save unit
			if(this.$el.find('#unitSlt').length > 0) {
				var unit = TargetUnit.findOrCreate(this.$el.find('#unitSlt').val());
				if(unit != null){
					this.currentObjective.get('units').reset();
					this.currentObjective.get('units').add(unit);
					
					
				} else {
					
					this.currentObjective.set('units', null);
				}
			}
			
			// now save parent
			if(this.$el.find('#parentSlt').length > 0) {
				var parent = Objective.findOrCreate(this.$el.find('#parentSlt').val());
				if(parent != null){
					this.currentObjective.set('parent', parent);	
				} else {
					this.currentObjective.set('parent', null);
				}
			}
			
			//then 
			var relationSlt = this.$el.find('.relationSlt');
			var relations = this.currentObjective.get('relations');
			for(var i=0; i<relationSlt.length; i++) {
				if($(relationSlt[i]).val() != 0 ){
					if(relations.at(i).get('objective') == null) {
						
						relations.at(i).set('objective', this.currentObjective);
						relations.at(i).set('parent', Objective.findOrCreate($(relationSlt[i]).val()));
						
					} else {
						// update this relation!
						relations.at(i).set('parent', Objective.findOrCreate($(relationSlt[i]).val()));
						
					}
				} else {
					relations.at(i).set('parent',null);
				}				
			}
			
			// we should be fine to save here?
			this.currentObjective.save(null,{
				success: _.bind(function() {
					if(newObj) {
						var lastPage = objectiveCollection.totalPages;
						if(objectiveCollection.totalElements % objectiveCollection.pageSize == 0 ) {
							lastPage++;
						}
						mainTblView.renderTargetPage(lastPage);						
					}
					
					this.$el.modal('hide');
					
				},this)
			});
			
		},
		render: function() {
			if(this.currentObjective.get('name') == null) {
			
				this.$el.find('.modal-header span').html("เพิ่มรายการทะเบียน" + objectiveType.get('name') + "ใหม่");
			} else {
				this.$el.find('.modal-header span').html(objectiveType.get('name') + " [" + this.currentObjective.get('code') + "]"+ this.currentObjective.get('name'));
			}
			
			var json = this.currentObjective.toJSON();
			
			if( hasUnit.length > 0 ) {
				json.hasUnit = true;
				json.unitSelectionList = listTargetUnits.toJSON();
				
				// loop through the parent
				for(var i=0; i<json.unitSelectionList.length; i++) {
					if(this.currentObjective.get('units') != null) {
						if(json.unitSelectionList[i].id == this.currentObjective.get('units').at(0).get('id')) {
							json.unitSelectionList[i].selected = "selected";
						}
					}
				}
				
			}
			
			
			if(hasParent.length > 0 ) {
				json.hasParent = true;
				json.parentTypeName = parentTypeName;
				json.parentTypeId = parentTypeId;
				json.parentSelectionList = parentObjectiveCollection.toJSON();
			
				// loop through the parent
				for(var i=0; i<json.parentSelectionList.length; i++) {
					if(this.currentObjective.get('parent') != null) {
						if(json.parentSelectionList[i].id == this.currentObjective.get('parent').get('id')) {
							json.parentSelectionList[i].selected = "selected";
						}
					}
				}
			}
			
			
			// now associcate each listOfObjective
			
			for(var i=0; i< relatedTypeList.length; i++) {
				
				json.relations[i].selectionList = listObjective[relatedTypeList[i]].toJSON();
				
				for(var j=0; j<json.relations[i].selectionList.length; j++) {
					if(json.relations[i].parent != null) {
						if(json.relations[i].selectionList[j].id == json.relations[i].parent.id) {
							json.relations[i].selectionList[j].selected = "selected";
						}
							
					}
				}
				json.relations[i].relationInfo = {};
				json.relations[i].relationInfo.name = relatedTypenameList[i];
				json.relations[i].relationInfo.parentId = relatedTypeList[i];
			}
			
			var html = this.modalBodyTemplate(json);
			this.$el.find('.modal-body').html(html);
			
			
			this.$el.modal({show: true, backdrop: 'static', keyboard: false});
			return this;
			
		},
		
		renderWithObjective: function(objective) {
			this.currentObjective = objective;
			this.render();
		}
		
	});
	
	var MainTblView = Backbone.View.extend({
		initialize: function(options){
		    this.collection.bind('reset', this.render, this);
		},
	
		el: "#mainCtr",
		selectedObjective: null,
		currentLineVal: null,
		
		newRowTemplate: Handlebars.compile($("#newRowTemplate").html()),
		mainCtrTemplate: Handlebars.compile($("#mainCtrTemplate").html()),
		tbodyTemplate: Handlebars.compile($("#tbodyTemplate").html()),
		objectiveRowTemplate: Handlebars.compile($("#objectiveRowTemplate").html()),
		unitLinkSltTemplate: Handlebars.compile($("#unitLinkSltTemplate").html()),
		linkSltTemplate: Handlebars.compile($("#linkSltTemplate").html()),
		parentLinkSltTemplate: Handlebars.compile($("#parentLinkSltTemplate").html()),
		
		
		modalView : new ModalView(),
		

		
		render: function() {
			// first render the control
			var json =  objectiveType.toJSON();
			json.pageParams = this.collection.toPageParamsJSON();
			

			json.relatedTypenameList = relatedTypenameList;
			
			if(hasParent.length > 0) {
				json.hasParent = true;
				json.parentTypeName = parentTypeName;
				json.parentTypeId = parentTypeId;
			}
			
			if(hasParent.length > 0) {
				json.hasUnit = true;
			}
			
			var html = this.mainCtrTemplate(json);

			this.$el.html(html);
			
			if(this.collection.length>0) {
				// then the inside row
				json=this.collection.toJSON();
				
				html = this.tbodyTemplate(json);
				this.$el.find('tbody').html(html);
			}

			// bind all cell
			this.collection.each(function(model){
				model.bind('change', this.renderObjective, this);
				this.renderObjective(model);
			}, this);

			return this;
		},
		
		events: {
			"click .menuNew" : "newRow",
			"click .menuDelete" : "deleteRow",
			"click .menuEdit"	: "editRow",
			
			"click a.pageLink" : "gotoPage"
		},
		
		newRow: function(e) {
			
			var newObj = new Objective();
			// and we'll have to do
			var relationCollection = new ObjectiveRelationsCollection();
			for(var j=0; j<relatedTypeList.length; j++ ) {
				relationCollection.push(new ObjectiveRelations());
			}
			newObj.set('relations', relationCollection);
			newObj.set('fiscalYear', fiscalYear);
			newObj.set('type', objectiveType);
			
			this.modalView.renderWithObjective(newObj);
			
		},
		editRow: function(e) {
			var objectiveId = $('input[name=rowRdo]:checked').parents('tr').attr('data-id');
			
			if((! $(e.currentTarget).hasClass('disabled') ) && $('input[name=rowRdo]:checked').length == 1) {
				var model = this.collection.get(objectiveId);
					
				this.modalView.renderWithObjective(model);
			
			} else {
				alert('กรุณาเลือกรายการที่ต้องการแก้ไข');
			}
		},
		
		renderObjective: function(objective) {
			
			var objectiveEl = this.$el.find('tr[data-id='+ objective.get('id') +']');
			
			var json = objective.toJSON();
			
			if(hasUnit.length > 0 ) {
				json.hasUnit = true;
			}
			
			if(hasParent.length > 0) {
				json.hasParent = true;
			}
			
			objectiveEl.html(this.objectiveRowTemplate(json));
			
		},
		
		deleteRow: function(e) {
			var objectiveId = $('input[name=rowRdo]:checked').parents('tr').attr('data-id');
			
			if( (! $(e.currentTarget).hasClass('disabled')) && $('input[name=rowRdo]:checked').length == 1 ) {
				
				var modelToDelete = this.collection.get(objectiveId);
				
				if(modelToDelete.get('isLeaf') == true) {
					if(confirm("คุณต้องการลบรายการ " + modelToDelete.get('name'))) {
					
						modelToDelete.destroy({
							success: _.bind(function() {					
								this.collection.remove(modelToDelete);
							
								// now we have to run through and reindex
								this.collection.each(function(model, index) {
									model.set('index', index);
								});
								
								this.collection.trigger('reset');
							},this)
						});
					}
					
					this.collection.trigger('reset');
				} else{
					alert('คุณต้องเข้าไปลบรายการจากรายการย่อยสุดเท่านั้น');
				}
			} else {
				alert('กรุณาเลือกรายการที่ต้องการลบ');
			}
		},
		
		gotoPage: function(e) {
			var pageNumber = $(e.target).attr('data-id');
			this.renderTargetPage(pageNumber);
		},
		
		renderTargetPage: function(pageNumber) {
			
			objectiveCollection.targetPage = pageNumber;
			
			objectiveCollection.fetch({
				silent: true,
				success: function(){
					
					
					//initializae o.get('relations')
					for(var i=0; i<objectiveCollection.length; i++) {
						
						var relationCollection = new ObjectiveRelationsCollection();
						for(var j=0; j<relatedTypeList.length; j++ ) {
							relationCollection.push(new ObjectiveRelations());
							
						}
						objectiveCollection.at(i).set('relations', relationCollection);
					}
					
					
					// now fetch the needed relation
					var relationsCollection = new ObjectiveRelationsCollection();
					relationsCollection.fetch({
						url: appUrl('/ObjectiveRelations/' + fiscalYear + '/relation/' + objectiveType.get('id') ),
						data: {ids: objectiveCollection.getIds()},
						success: function() {
							for(var k=0 ; k < relationsCollection.length; k++) {
								
								var relation = relationsCollection.at(k);
								
								var o = relation.get('objective');
								var parentTypeId = relation.get('parentType').get('id');
								var index = relatedTypeList.indexOf(parentTypeId.toString());
								var objRelations = o.get('relations');
								
								objRelations.remove(objRelations.at(index));
								objRelations.add(relation, {at: index});
								
							}
							
							objectiveCollection.trigger('reset');	
						}
						
					});
					
					
				}
			});
		}
		
	});
	
	mainTblView = new MainTblView({collection: objectiveCollection});

	objectiveType = new ObjectiveType({id: parseInt(typeId)});
	objectiveType.fetch({
		success: function(){
			
			var headLineStr = '<h4> ทะเบียน' + objectiveType.get('name');
			if(fiscalYear!= null && fiscalYear.length > 0 ) {
				headLineStr += 	' ปีงบประมาณ ' + fiscalYear;
			}
			headLineStr += '</h4>';
			$('#headLine').html(headLineStr);
			
			if(fiscalYear != null && fiscalYear.length > 0 ) {
				listObjective = [];
				_.each(relatedTypeList, function(typeId) {
					listObjective[typeId] = new  ObjectiveCollection();
					listObjective[typeId].fetch({
						url: appUrl('/Objective/' + fiscalYear + '/type/'+typeId ),
						success: function() {
							
						}
					});
				});

				if( parentTypeId.length > 0 ) {
					parentObjectiveCollection = new ObjectiveCollection();
					parentObjectiveCollection.fetch({
						url: appUrl("/Objective/"+ fiscalYear +"/type/" + parentTypeId), 
						success: function() {
							
						}
					});
				} 
				
				mainTblView.renderTargetPage(1);
				
			}
		}
	});
	
	
	



});

//-->
</script>