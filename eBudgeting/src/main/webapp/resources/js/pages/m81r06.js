var AssignTargetValueModalView = Backbone.View.extend({
	/**
     *  @memberOf AssignTargetValueModalView
     */
	initialize : function(options) {
		this.parentView = options.parentView;
	},
	assignTargetValueModalTemplate: Handlebars.compile($("#assignTargetValueModalTemplate").html()),
	
	organizationSearchTbodyTemplate: Handlebars.compile($("#organizationSearchTbodyTemplate").html()),
	organizationTargetValueTbodyTemplate: Handlebars.compile($("#organizationTargetValueTbodyTemplate").html()),
	
	
	el : "#assignTargetValueModal",
	
	setCurrentTarget: function(target) {
		this.currentTarget = target;
	},
	
	setCurrentActivity: function(activity){
		this.currentActivity = activity;
	},
	
	events: {
		"click #organizationSearchBtn" : "organizationSearch",
		"click .addOrganization" : "addOrganizationTarget",
		"click .removeOrganizationTarget" : "removeOrganizationTarget",
		
		"change .proposalAllocated" : "changeProposalAllocated",
		
		"click #saveAssignTargetBtn" : "saveAssignTarget",
		"click #cancelBtn" : "cancelAssignTarget"
	},
	resetProposalsInList: function() {
		if(this.targetReports != null) {
			this.targetReports.pluck('owner').forEach(function(owner) {
				owner.set('_inProposalList', false);
			});
		}
			
		if(this.organizationSearchList != null) {
			this.organizationSearchList.reset();
		}
	},

	cancelAssignTarget: function(e) {
		this.resetProposalsInList();
		this.$el.modal('hide');
	},
	saveAssignTarget: function(e) {
		var sum=0;
		// now put the sum up
		_.forEach(this.$el.find("input.proposalAllocated"), function(el) {
			sum += parseInt($(el).val());
		});
		
		if(sum != parseInt($('#totalInputTxt').val().replace(/,/g, ''))) {
			alert("กรุณาตรวจสอบการจัดสรร ค่าเป้าหมายที่จัดสรรให้หน่วยงานรวมแล้วไม่เท่ากับค่าเป้าหมายที่จัดสรรไว้");
			return;
		}
 		
 		this.$el.find('button#saveAssignTargetBtn').html('<icon class="icon-refresh icon-spin"></icon> กำลังบันทึกข้อมูล...');
 		
 		// we should be ready to save the 
 		Backbone.sync('create', this.targetReports, {
 			success: _.bind(function() {
 				alert('บันทึกเรียบร้อยแล้ว');
 				this.cancelAssignTarget();
 			},this)
 		});
	},
	changeProposalAllocated: function(e) {
		// validate this one first
		if( isNaN( +$(e.target).val() ) ) {
			$(e.target).parent('div').addClass('control-group error');
			alert("กรุณาใส่ข้อมูลเป็นตัวเลข");
		} else {
			$(e.target).parent('div').removeClass('control-group error'); 
			var i = $(e.target).parents('tr').prevAll('tr').length;
			
			this.targetReports.at(i).set('targetValue', $(e.target).val());
			
		}
		this.updateSumTarget();	
	},
	addOrganizationTarget: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		var newTargetReport = new ActivityTargetReport();
		newTargetReport.set('owner', organization);
		newTargetReport.set('targetValue', 0);
		newTargetReport.set('target', this.currentTarget);
		
		this.targetReports.push(newTargetReport);
		
		var html=this.organizationTargetValueTbodyTemplate(this.targetReports.toJSON());
		$('#organizationProposalTbl').find('tbody').html(html);
		
		this.updateSumTarget();
		
		organization.set('_inProposalList', true);
		//refresh the other list
		var html=this.organizationSearchTbodyTemplate(this.organizationSearchList.toJSON());
		$('#organizationSearchTbl').find('tbody').html(html);
		

	},
	updateSumTarget: function() {
		var sum=0;
		// now put the sum up
		_.forEach(this.$el.find("input.proposalAllocated"), function(el) {
			if(!isNaN(parseInt($(el).val()))) {
				sum += parseInt($(el).val());
			}
			
		});
		
		$('#sumTotalAllocated').html(addCommas(sum));

	},
	removeOrganizationTarget: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		
		
		// now remove this one 
		for(var i=0; i<this.targetReports.length; i++) {
			if(this.targetReports.at(i).get('owner') == organization) {
				
				var y = confirm("คุณต้องการลบการจัดสรรงบประมาณของหน่วยงาน " + organization.get('name'));
				if(y==true) {
					
					var p = this.targetReports.at(i);
					
					// we remove this on from our list
					this.targetReports.remove(p);
					
					p.get('owner').set('_inProposalList', false);
			
					var html=this.organizationTargetValueTbodyTemplate(this.targetReports.toJSON());
					$('#organizationProposalTbl').find('tbody').html(html);
					
					this.updateSumTarget();
					
					//refresh the other list
					var html=this.organizationSearchTbodyTemplate(this.organizationSearchList.toJSON());
					$('#organizationSearchTbl').find('tbody').html(html);
				}
			}
		}
		

	},
	
	organizationSearch: function(e) {
		var query = this.$el.find('#oraganizationQueryTxt').val();
		
		this.organizationSearchList = new OrganizationCollection();
		this.organizationSearchList.url = appUrl("/Organization/parentId/0/findByName");
		this.organizationSearchList.fetch({
			data: {
				query: query
			},
			type: 'POST',
			success: _.bind(function() {
				var html=this.organizationSearchTbodyTemplate(this.organizationSearchList.toJSON());
				$('#organizationSearchTbl').find('tbody').html(html);
			},this)
		});
		
	},

	render: function() {
		
		
		this.$el.find('.modal-header span').html("จัดสรรเป้าหมาย: " + this.currentActivity.get('name'));
		
		var json = this.currentTarget.toJSON();
		
		var html = this.assignTargetValueModalTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		//now fill in 
		this.targetReports = new ActivityTargetReportCollection();
		this.targetReports.url = appUrl('/ActivityTargetReport/findByTarget/' 
				+ this.currentTarget.get('id'));
		this.targetReports.fetch({
			success: _.bind(function() {
				
				this.targetReports.pluck('owner').forEach(function(owner) {
					owner.set('_inProposalList', true);
				});

				var html=this.organizationTargetValueTbodyTemplate(this.targetReports.toJSON());
				$('#organizationProposalTbl').find('tbody').html(html);	
				
				this.updateSumTarget();
			},this)
		});
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	}
});


var ActivityTargetTableView = Backbone.View.extend({
	/**
     *  @memberOf ActivityTargetTableView
     */
	initialize : function(options) {
		this.currentActivity = options.activity;
	},
	
	activityTargetTableTemplate: Handlebars.compile($("#activityTargetTableTemplate").html()),
	activityTargetTemplate:  Handlebars.compile($("#activityTargetTemplate").html()),
	
	setCurrentActivity : function(activity) {
		this.currentActivity = activity;
	},
	
	el : '#activityTarget',
	render : function() {
		
		var json = this.currentActivity.toJSON();
		for(var i=0; i<json.targets.length; i++) {
			json.targets[i].idx = i;
		}
		var html = this.activityTargetTableTemplate(json);
		e1 = json;
		this.$el.html(html);
	},
	
	events : {
		"click .newActivityTargetBtn" : "newActivityTarget",
		"change select#unitSlt" : "unitChange",
		"change .targetModel" : "targetModelChange",
		"click .addActivityTargetBtn" : "addActivityTarget",
		"click .backToActivity" : "backToActivity",
		"click .deleteTarget" : "deleteActivityTarget",
		"click .editTarget" : "editActivityTarget",
		"click #provincialTarget" : "provincialTargetToggle"
	},
	
	backToActivity: function(e) {
		this.render();
	},
	
	provincialTargetToggle: function(e) {
		var checked = this.$el.find('#provincialTarget').is(":checked");
		this.currentActivityTarget.set('provincialTarget', checked);
		
		// now render properly
		if(checked) {
			this.$el.find('#budgetAllocated').attr('disabled',true);
			this.$el.find('#budgetAllocated').val('(ส่วนภูมิภาค)');
		} else {
			this.$el.find('#budgetAllocated').removeAttr('disabled');
			this.$el.find('#budgetAllocated').val(this.currentActivityTarget.get('budgetAllocated'));
		}
		
	},
	
	editActivityTarget : function(e) {
		var index = $(e.target).parents('tr').attr('data-idx');
		var target = this.currentActivity.get('targets').at(index);
		
		this.currentActivityTarget = target;
		
		var json = this.currentActivityTarget.toJSON();
		
		json.unitSelectionList = listTargetUnits.toJSON();
		json.headerString="แก้ไขเป้าหมาย";
		
		// loop through the parent
		for(var i=0; i<json.unitSelectionList.length; i++) {
			if(this.currentActivityTarget.get('unit') != null) {
				if(json.unitSelectionList[i].id == this.currentActivityTarget.get('unit').get('id')) {
					json.unitSelectionList[i].selected = "selected";
				}
			}
		}
		
		var html = this.activityTargetTemplate(json);
		
		this.$el.html(html);
		
	},
	
	deleteActivityTarget: function(e) {
		var index = $(e.target).parents('tr').attr('data-idx');
		var target = this.currentActivity.get('targets').at(index);
		var answer = confirm ("คุณต้องการลบเป้าหมาย " + target.get('unit').get('name') + " ?");
		if(answer == true) {
			this.currentActivity.get('targets').remove(target);
			this.render();
		}
	},
	
	targetModelChange: function(e) {
		var value = $(e.target).val();
		var modelName = $(e.target).attr('data-modelName');
		
		if(this.currentActivityTarget!=null) {
			this.currentActivityTarget.set(modelName,value);
		}
	},
	addActivityTarget : function() {
		if(this.currentActivityTarget.get('unit') ==  null) {
			alert('กรุณาระบุหน่วยนับ');
			return;
		}
		
		if(isNaN(parseInt(this.currentActivityTarget.get('targetValue'))) ) {
			alert('กรุณาใส่ค่าเป้าหมายเป็นตัวเลข');
			return;
		}
		
		if(this.currentActivityTarget.get('provincialTarget')) {
			this.currentActivityTarget.set('budgetAllocated', 0);
		}
		
		if(!this.currentActivity.get('targets').include(this.currentActivityTarget)) {
			this.currentActivity.get('targets').add(this.currentActivityTarget);
		}
		this.render();
			 
	},
	unitChange : function(e) {
		var unitId = $(e.target).val();
		this.currentActivityTarget.set('unit', TargetUnit.findOrCreate(unitId));
	},
	newActivityTarget: function() {
		this.currentActivityTarget = new ActivityTarget();
		this.currentActivityTarget.set('activity', this.currentActivity);
		this.currentActivityTarget.set('provincialTarget', false);
		
		var json = this.currentActivityTarget.toJSON();
		
		json.unitSelectionList = listTargetUnits.toJSON();
		
		json.headerString="เพิ่มเป้าหมาย";
		// loop through the parent
		for(var i=0; i<json.unitSelectionList.length; i++) {
			if(this.currentActivityTarget.get('unit') != null) {
				if(json.unitSelectionList[i].id == this.currentActivityTarget.get('unit').get('id')) {
					json.unitSelectionList[i].selected = "selected";
				}
			}
		}
		
		var html = this.activityTargetTemplate(json);
		
		this.$el.html(html);
	}
});


var ModalView = Backbone.View.extend({
	/**
     *  @memberOf ModalView
     */
	initialize : function(options) {
		this.parentView = options.parentView;
		
		this.currentChildrenOrganization = new OrganizationCollection();
		this.currentChildrenOrganization.url = appUrl('/Organization/currentSession/children');
		this.currentChildrenOrganization.fetch();
		
	},
	modalTemplate: Handlebars.compile($("#modalTemplate").html()),
	el : "#modal",
	
	events : {
		"change .model" : "modelChange",
		"click #saveBtn" : "saveModel",
		"click #cancelBtn" : "cancel"
			
		
	},
	
	cancel : function(e) {
		this.$el.modal('hide');
	},
	
	saveModel : function(e) {
		var regulatorId=this.$el.find('#regulatorSlt').val();
		
		if(regulatorId < 0) {
			alert("กรุณาระบุส่วนงานที่รับผิดชอบ");
			return;
		}
		
		this.currentActivity.set('regulator', Organization.findOrCreate(regulatorId));

		
		this.currentActivity.save(null, {
			success: _.bind(function() {
				alert('บันทึกข้อมูลเรียบร้อยแล้ว');
				this.$el.modal('hide');
				this.parentView.renderMainTblWithParent(this.currentActivity.get('forObjective'));
				
			},this)
		});
	},
	

	
	modelChange: function(e) {
		var value = $(e.target).val();
		var modelName = $(e.target).attr('data-modelName');
		
		if(this.currentActivity!=null) {
			this.currentActivity.set(modelName,value);
		}
	},
	
	render: function() {
		var string = "";
		if(this.currentActivity.get('parent') == null) {
			string = "กิจกรรมย่อย";
		} else {
			string = "กิจกรรมเสริม";
		}
		
		if(this.currentActivity.get('id') == null) {
			
			this.$el.find('.modal-header span').html("เพิ่ม" + string);
		} else {
			this.$el.find('.modal-header span').html(
					"แก้ไข" + string);
		}
		
		var json = this.currentActivity.toJSON();
		
		
		
		if(parentCurrentOrganizationId == 0) {
			json.currentChildrenOraganization = this.currentChildrenOrganization.toJSON();
			var regulator = this.currentActivity.get('regulator');
			if(regulator != null) {
				var regulatorId = regulator.get('id');
				var foundOrgInJson = _.find(json.currentChildrenOraganization, function(o) { return o.id == regulatorId; });
				if(foundOrgInJson != null) {
					foundOrgInJson.selected = true;
				}
			}
		} else {
			var org = this.currentChildrenOrganization.findWhere({id: currentOrganizationId}) ;
			json.currentChildrenOraganization = org.toJSON();
			json.disabledChldrenOrganizationSlt = true;
		}
		
		
		
		
		
		var html = this.modalTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		// now render the currentActivityTargetTable
		
		this.activityTargetTableView = new ActivityTargetTableView({
			activity: this.currentActivity
		});
		
		this.activityTargetTableView.render();
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	},
	
	renderWithActivity: function(activity) {
		this.currentActivity = activity;
		this.render();
	}

});

var MainSelectionView = Backbone.View.extend({
	mainSelectionTemplate : Handlebars.compile($("#mainSelectionTemplate").html()),
	selectionTemplate : Handlebars.compile($("#selectionTemplate").html()),
	type102DisabledSelectionTemplate : Handlebars.compile($("#type102DisabledSelection").html()),
	/**
     *  @memberOf MainSelectionView
     */
	initialize: function() {
		
	},
	events: {
		"click .report" : "loadReport"
	},
	
	loadReport: function(e) {
		var startMonth = this.$el.find('#startMonth').val();
		var endMonth = this.$el.find('#endMonth').val();
		var objId = this.$el.find("#objId").val();
		window.open(appUrl("/m81r06.xls/" + fiscalYear + "/" +  startMonth + "/" + endMonth + "/" + objId +"/file/m81r06.xls"));
		return false;
	},
	
	render: function() {
		
		if(this.rootChildrenObjectiveCollection != null) {
			var json = this.rootChildrenObjectiveCollection.toJSON();
			
			var html = this.mainSelectionTemplate(json);
			this.$el.html(html);
		}
	}, 
	renderInitialWith: function(collection) {
		this.rootChildrenObjectiveCollection = collection;
		this.render();
	}
	
});


var MainCtrView = Backbone.View.extend({
	/**
     *  @memberOf MainCtrView
     */
	initialize : function() {
		//this.collection.bind('reset', this.render, this);
		this.$el.html(this.loadingTpl());
		this.modalView = new ModalView({parentView: this});
		this.assignTargetValueModalView = new AssignTargetValueModalView({parentView: this});
	},

	el : "#mainCtr",
	loadingTpl : Handlebars.compile($("#loadingTemplate").html()),
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	mainTblTemplate : Handlebars.compile($("#mainTblTemplate").html()),
	mainTblTbodyTemplate : Handlebars.compile($("#mainTblTbodyTemplate").html()),
	
	events : {
		
		
			
	},
	
	
	assignTarget: function(e) {
		var activityId = $(e.target).parents('tr').attr('data-id');
		var activity = Activity.findOrCreate(activityId);
		
		var targetId = $(e.target).parents('li').attr('data-id');
		var activityTarget = ActivityTarget.findOrCreate(targetId);
		
		this.assignTargetValueModalView.setCurrentActivity(activity);
		this.assignTargetValueModalView.setCurrentTarget(activityTarget);
		this.assignTargetValueModalView.render();
	},
	
	editActivity: function(e) {
		var activityId = $(e.target).parents('tr').attr('data-id');
		var activity = Activity.findOrCreate(activityId);
		if(parentCurrentOrganizationId != 0){
			if(currentOrganizationId != activity.get("regulator").get("id")) {
				alert("คุณไม่สามารถแก้ไขกิจกรรมย่อยที่ส่วนงานของคุณไม่ได้รับผิดชอบได้");
				return false;
			}
		}

		this.modalView.renderWithActivity(activity);
	},
	
	deleteActivity : function(e) {
		var activityId = $(e.target).parents('tr').attr('data-id');
		var activity = Activity.findOrCreate(activityId);
		if(activity != null) {
			if(parentCurrentOrganizationId != 0){
				if(currentOrganizationId != activity.get("regulator").get("id")) {
					alert("คุณไม่สามารถลบกิจกรรมย่อยที่ส่วนงานของคุณไม่ได้รับผิดชอบได้");
					return false;
				}
			}
			
			var answer = confirm("คุณต้องการลบกิจกรรมย่อย " + activity.get('name') + " ?" );
			if(answer == true ) {
				activity.destroy({
					success: _.bind(function() {
						alert("คุณได้ลบข้อมูลเรียบร้อยแล้ว");
						this.renderMainTblWithParent(this.currentObjective);
					},this)
				});
			} else {
				// noting happend
				return false;
			}
			
		}
		
	},
	
	newChildActivity: function(e) {
		var parentActivityId = $(e.target).parents('tr').attr('data-id');
		var parentActivity = Activity.findOrCreate(parentActivityId);
		if(parentCurrentOrganizationId != 0){
			if(currentOrganizationId != parentActivity.get("regulator").get("id")) {
				alert("คุณไม่สามารถเพิ่มกิจกรรมเสริมที่ส่วนงานของคุณไม่ได้รับผิดชอบได้");
				return false;
			}
		}
		
		var newActivity = new Activity();
		newActivity.set('forObjective', this.currentObjective);
		newActivity.set('parent', parentActivity);
		
		this.modalView.renderWithActivity(newActivity);
		
		
	},
	
	newActivity: function() {
		var newActivity = new Activity();
		newActivity.set('forObjective', this.currentObjective);
		newActivity.set('parent', null);
		
		this.modalView.renderWithActivity(newActivity);
	},
	
	render: function() {
		this.$el.html(this.mainCtrTemplate());
		
		this.mainSelectionView = new MainSelectionView({el: "#mainCtr #mainSelection"});

		this.rootSelection = new ObjectiveCollection();
		this.rootSelection.url = appUrl("/Objective/" + fiscalYear + "/type/102");
		this.rootSelection.fetch({
			success: _.bind(function() {
				this.mainSelectionView.renderInitialWith(this.rootSelection);
			}, this)
		});

	},
	
	emptyTbl: function() {
		this.$el.find("#mainTbl").empty();
	},
	
	renderMainTblWithParent: function(obj) {
		this.currentObjective = obj;

		this.$el.find("#mainTbl").html(this.mainTblTemplate({}));
		
		// first find the activities
		// and put them in the table 
		this.activities = new ActivityCollection();
		this.activities.url = appUrl("/Activity/currentOwner/forObjective/" + this.currentObjective.get('id'));
		this.activities.fetch({
			success : _.bind(function() {
				var flatActivities = new ActivityCollection();
				
				for(var i=0; i<this.activities.length; i++) {
					flatActivities.push(this.activities.at(i));
					this.addChildrenTo(flatActivities, this.activities.at(i).get('children'));
				}
				
				var json = flatActivities.toJSON();
				var html = this.mainTblTbodyTemplate(json);
				
				this.$el.find("#mainTbl tbody").html(html);
			},this)
		});
		
	},
	
	addChildrenTo : function(flatCollection, collection) {
		if(collection == null || collection.length == 0) {
			return;
		} 
		
		for(var i=0; i<collection.length; i++) {
			flatCollection.push(collection.at(i));
			this.addChildrenTo(flatCollection, collection.at(i).get('children'));
		}
		
	} 
	
});