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
		"change select#unitSlt" : "unitChange",
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
	
	unitChange : function(e) {
		var unitId = $(e.target).val();
		this.currentActivity.set('unit', TargetUnit.findOrCreate(unitId));
	},
	
	modelChange: function(e) {
		var value = $(e.target).val();
		var modelName = $(e.target).attr('data-modelName');
		
		if(this.currentActivity!=null) {
			this.currentActivity.set(modelName,value);
		}
	},
	
	render: function() {
		if(this.currentActivity.get('id') == null) {
			
			this.$el.find('.modal-header span').html("เพิ่มทะเบียนรายการย่อย");
		} else {
			this.$el.find('.modal-header span').html(
					"แก้ไขทะเบียนรายการย่อย");
		}
		
		var json = this.currentActivity.toJSON();
		
		json.unitSelectionList = listTargetUnits.toJSON();
		
		// loop through the parent
		for(var i=0; i<json.unitSelectionList.length; i++) {
			if(this.currentActivity.get('unit') != null) {
				if(json.unitSelectionList[i].id == this.currentActivity.get('unit').get('id')) {
					json.unitSelectionList[i].selected = "selected";
				}
			}
		}
			
		var html = this.modalTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		
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
	},

	el : "#mainCtr",
	loadingTpl : Handlebars.compile($("#loadingTemplate").html()),
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	mainTblTemplate : Handlebars.compile($("#mainTblTemplate").html()),
	mainTblTbodyTemplate : Handlebars.compile($("#mainTblTbodyTemplate").html()),
	
	events : {
		"click .newActivityBtn" : "newActivity",
		"click .menuDelete" : "deleteActivity",
		"click .menuEdit" : "editActivity"
			
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
	
	newActivity: function() {
		var newActivity = new Activity();
		newActivity.set('forObjective', this.currentObjective);
		
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
				var json = this.activities.toJSON();
				var html = this.mainTblTbodyTemplate(json);
				
				this.$el.find("#mainTbl tbody").html(html);
			},this)
		});
		
	}
	
});