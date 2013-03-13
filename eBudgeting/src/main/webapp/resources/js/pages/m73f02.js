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
				
				this.organizationSearch();
				
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
		"click .editTarget" : "editActivityTarget"
		
	},
	
	backToActivity: function(e) {
		this.render();
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




var MainSelectionView = Backbone.View.extend({
	mainSelectionTemplate : Handlebars.compile($("#mainSelectionTemplate").html()),
	selectionTemplate : Handlebars.compile($("#selectionTemplate").html()),
	type102DisabledSelectionTemplate : Handlebars.compile($("#type102DisabledSelection").html()),
	/**
     *  @memberOf MainSelectionView
     */
	initialize: function() {
		
		this.type102Collection = new ObjectiveCollection();
		
		_.bindAll(this, 'renderInitialWith');
		_.bindAll(this, 'renderType102');
		this.type102Collection.bind('reset', this.renderType102);
	},
	events: {
		"change select#type101Slt" : "type101SltChange",
		"change select#type102Slt" : "type102SltChange",
	},
	type101SltChange : function(e) {
		var type101Id = $(e.target).val();
		if(type101Id != 0) {
			this.type102Collection.fetch({
				url: appUrl('/Objective/' + type101Id + '/childrenOnlyWithCurrentActivityRegulator'),
				success: _.bind(function() {
					this.type102Collection.trigger('reset');
				}, this)
			});
		}
		
		mainCtrView.emptyTbl();
		
	},
	type102SltChange : function(e) {
		var type102Id = $(e.target).val();
		if(type102Id != 0) {
			var obj = Objective.findOrCreate(type102Id);
			mainCtrView.renderMainTblWithParent(obj);
		}
		
	},
	
	renderType102: function(e) {
		var json = this.type102Collection.toJSON();
		json.type =  {};
		json.type.name = "กิจกรรมรอง";
		json.type.id = 102;
		var html = this.selectionTemplate(json);
		
		// now render 
		this.$el.find('#type102Div').empty();
		this.$el.find('#type102Div').html(html);
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
		this.assignTargetValueModalView = new AssignTargetValueModalView({parentView: this});
	},

	el : "#mainCtr",
	loadingTpl : Handlebars.compile($("#loadingTemplate").html()),
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	mainTblTemplate : Handlebars.compile($("#mainTblTemplate").html()),
	mainTblTbodyTemplate : Handlebars.compile($("#mainTblTbodyTemplate").html()),
	
	events : {
		"click .newActivityBtn" : "newActivity",
		"click .menuDelete" : "deleteActivity",
		"click .menuEdit" : "editActivity",
		"click .newActivitityChild" : "newChildActivity",
		"click .assignTargetLnk" : "assignTarget"
			
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

		this.modalView.renderWithActivity(activity);
	},
	
	deleteActivity : function(e) {
		var activityId = $(e.target).parents('tr').attr('data-id');
		var activity = Activity.findOrCreate(activityId);
		if(activity != null) {
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
		
		
		var newActivity = new Activity();
		newActivity.set('forObjective', this.currentObjective);
		newActivity.set('parent', Activity.findOrCreate(parentActivityId));
		
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
		this.rootSelection.url = appUrl("/Objective/currentActivityRegulator/" + fiscalYear);
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
		this.activities.url = appUrl("/Activity/currentRegulator/forObjective/" + this.currentObjective.get('id'));
		this.activities.fetch({
			success : _.bind(function() {
			
				var json = this.activities.toJSON();
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