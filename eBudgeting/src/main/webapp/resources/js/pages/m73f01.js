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
		"click #organizationOwnerSearchBtn" : "organizationOwnerSearch"
	},
	
	organizationOwnerSearch: function(e) {
		var query = this.$el.find('#oraganizationOwnerQueryTxt').val();
		
		this.organizationSearchList = new OrganizationCollection();
		this.organizationSearchList.url = appUrl("/Organization/code/00/findByName");
		this.organizationSearchList.fetch({
			data: {
				query: query
			},
			type: 'POST',
			success: _.bind(function() {
				var html=this.organizationOwnerSearchTbodyTemplate(this.organizationSearchList.toJSON());
				$('#organizationOwnerSearchTbl').find('tbody').html(html);
			},this)
		});		
	},
	
	organizationSearch: function(e) {
		var query = this.$el.find('#oraganizationQueryTxt').val();
		
		this.organizationSearchList = new OrganizationCollection();
		this.organizationSearchList.url = appUrl("/Organization/findByName");
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


var ModalView = Backbone.View.extend({
	/**
     *  @memberOf ModalView
     */
	initialize : function(options) {
		this.parentView = options.parentView;
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
				url: appUrl('/Objective/' + type101Id + '/children'),
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
		this.modalView = new ModalView({parentView: this});
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
		this.rootSelection.url = appUrl("/Objective/currentOwner/" + fiscalYear);
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