var BudgetProposalSelectionView = Backbone.View.extend({
	/**
	 * @memberOf BudgetProposalSelectionView
	 */
	initialize: function(options) {
		if(options.organizationId != null) {
			this.organizationId = options.organizationId;
			this.organization = Organization.findOrCreate({
				id: this.organizationId 
			});
			
		}
	},
	objectiveSelectionTemplate: Handlebars.compile($("#objectiveSelectionTemplate").html()),
	el: "#budgetSlt",
	events: {
		"click .objectiveSelect" : "objectiveSelect"
	},
	objectiveSelect: function(e) {
		var objectiveId = $(e.target).parents('li').attr('data-id');
		var objective = Objective.findOrCreate(objectiveId);
		
		mainTblView.renderWithObjective(objective);
	},
	render: function() {
		this.organization.fetch({
			success: _.bind(function(model, response, options) {
				this.renderSelection();
			},this)
		});
		
		return this;
	},
	renderSelection: function() {
		this.rootSelection = new ObjectiveCollection();
		this.rootSelection.url = appUrl("/Objective/currentActivityOwner/" + fiscalYear);
		this.rootSelection.fetch({
			success: _.bind(function() {
				var json = this.rootSelection.toJSON();
				var html = this.objectiveSelectionTemplate(json);
				this.$el.html(html);
			}, this)
		});
	}
});

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
	
	setCurrentTargetReport: function(targetReport) {
		this.currentTargetReport = targetReport;
		this.currentTarget = targetReport.get('target');
		this.currentPerformance = targetReport.get('activityPerformance');
	},
	
	setCurrentActivity: function(activity){
		this.currentActivity = activity;
	},
	
	events: {
		
		"change input.monthlyReportPlan" : "changeMonthlyReportPlan",
		"change input.monthlyBudgetPlan" : "changeMonthlyBudgetPlan",
		
		"click #saveAssignTargetBtn" : "saveAssignTarget",
		"click #cancelBtn" : "cancelAssignTarget"
	},
	
	changeMonthlyReportPlan: function(e) {
		var idx = $(e.target).attr('data-idx');
		var planTxt = parseInt($(e.target).val());
		this.currentTargetReport.get('monthlyReports').at(idx).set('activityPlan', planTxt);
	},
	changeMonthlyBudgetPlan: function(e) {
		var idx = $(e.target).attr('data-idx');
		var planTxt = parseInt($(e.target).val());
		this.currentTargetReport.get('activityPerformance').get('monthlyBudgetReports').at(idx).set('budgetPlan', planTxt);
	},
	
	cancelAssignTarget: function(e) {
		this.$el.modal('hide');
	},
	saveAssignTarget: function(e) {
		var sum=0;
		// now put the sum up
		_.forEach(this.$el.find("input.monthlyReportPlan"), function(el) {
			var value = parseInt($(el).val());
			sum += isNaN(value)?0:value;
		});
		
		if(sum != this.currentTargetReport.get('targetValue')) {
			alert("กรุณาตรวจสอบ แผนการดำเนินงาน รวมแล้วไม่เท่ากับค่าเป้าหมาย");
			return;
		}
		
		sum=0;
		_.forEach(this.$el.find("input.monthlyBudgetPlan"), function(el) {
			var value = parseInt($(el).val());
			sum += isNaN(value)?0:value;
		});
		if(sum != this.currentTargetReport.get('activityPerformance').get('budgetAllocated')) {
			alert("กรุณาตรวจสอบ แผนการเบิกจ่ายงบประมาณ รวมแล้วไม่เท่ากับงบที่ได้รับจัดสรร");
			return;
		}
 		
 		this.$el.find('a#saveAssignTargetBtn').html('<icon class="icon-refresh icon-spin"></icon> กำลังบันทึกข้อมูล...');
 		
 		// we should be ready to save the 
 		this.currentTargetReport.save(null, {
 			url: appUrl('/ActivityTargetReport/saveReportPlan/' + this.currentTargetReport.get('id')),
 			success : _.bind(function() {
 				alert('บันทึกเสร็จแล้ว');
 				this.$el.modal('hide');
 				this.currentTargetReport.urlRoot = appUrl('/ActivityTargetReport/');
 				this.$el.find('a#saveAssignTargetBtn').html('บันทึกข้อมูล');
 			},this)
 		});
	},
	
	render: function() {
		
		
		this.$el.find('.modal-header span').html("กิจกรรม: " + this.currentActivity.get('name'));
		
		var json = this.currentTargetReport.toJSON();
		var html = this.assignTargetValueModalTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	}
});





var MainTblView = Backbone.View.extend({
	/**
     *  @memberOf MainTblView
     */
	initialize : function() {
		//this.collection.bind('reset', this.render, this);
		
		this.assignTargetValueModalView = new AssignTargetValueModalView({parentView: this});
	},

	el : "#mainTbl",
	loadingTpl : Handlebars.compile($("#loadingTemplate").html()),
	mainTblTemplate : Handlebars.compile($("#mainTblTemplate").html()),
	mainTblTbodyActivityTemplate:  Handlebars.compile($("#mainTblTbodyActivityTemplate").html()),
	mainTblTbodyObjectiveTemplate: Handlebars.compile($("#mainTblTbodyObjectiveTemplate").html()),
	
	events : {
		"click .assignTargetLnk" : "assignTarget"
	},
	
	assignTarget: function(e) {
		var targetId = $(e.target).parents('li').attr('data-id');
		var activityTargetReport = ActivityTargetReport.findOrCreate(targetId);
		var activityPerformance = activityTargetReport.get('activityPerformance');
		var activity = activityPerformance.get('activity');
		
		activityTargetReport.fetch({
			url: appUrl('/ActivityTargetReport/' + activityTargetReport.get('id')),
			success: _.bind(function() {
				if(activityTargetReport.get('monthlyReports') == null){
					activityTargetReport.set('monthlyReports', new MonthlyActivityReportCollection()); 
				}
				
				var monthlyReports = activityTargetReport.get('monthlyReports');
				// now go through each 12 month
				for(var i=0; i<12; i++) {
					if(monthlyReports.at(i) == null) {
						var monthlyReport = new MonthlyActivityReport();
						
						monthlyReport.set('fiscalMonth', i);
						monthlyReport.set('report', activityTargetReport);	
						
						monthlyReports.add(monthlyReport, {at: i});
					}
				}
				
				
				var monthlyBudgetReports = activityTargetReport.get('activityPerformance').get('monthlyBudgetReports');
				// now go through each 12 month
				for(var i=0; i<12; i++) {
					if(monthlyBudgetReports.at(i) == null) {
						var monthlyReport = new MonthlyBudgetReport();
						
						monthlyReport.set('fiscalMonth', i);
						monthlyReport.set('activityPerformance', activityTargetReport.get('activityPerformance'));	
						monthlyReport.set('owner', activityTargetReport.get('owner'));
						
						monthlyBudgetReports.add(monthlyReport, {at: i});
					}
				}
				
				this.assignTargetValueModalView.setCurrentActivity(activity);
				this.assignTargetValueModalView.setCurrentTargetReport(activityTargetReport);
				this.assignTargetValueModalView.render();		
			},this)
		});
		
		
	},
	
	emptyTbl: function() {
		this.$el.find("#mainTbl").empty();
	},
	
	renderWithObjective: function(obj) {
		this.objective = obj;
		this.childObjectives = new ObjectiveCollection();
		this.childObjectives.fetch({
			url: appUrl('/Objective/getChildrenAndloadActivityAndOwnerId/'
					+obj.get('id')+'/' + organizationId),
			success: _.bind(function(model, response, options) {
				var json = this.objective.toJSON();
				var html = this.mainTblTemplate(json);
				this.$el.html(html);
				
				for(var i=0; i< this.childObjectives.length; i++) {
					var child = this.childObjectives.at(i);
					json = child.toJSON();
					html = this.mainTblTbodyObjectiveTemplate(json);
					this.$el.find('tbody').append(html);
					
					var activities = child.get('filterActivities');
					for(var j=0; j<activities.length; j++) {
						var act = activities.at(j);
						json =act.toJSON();
						json.padding=30;
						html=this.mainTblTbodyActivityTemplate(json);
						this.$el.find('tbody').append(html);
						
						if(act.get('children').length>0) {
							var childrenAct = act.get('children');
							for(var k=0; k<childrenAct.length; k++) {
								var childAct = childrenAct.at(k);
								json = childAct.toJSON();
								json.padding=60;
								html=this.mainTblTbodyActivityTemplate(json);
								this.$el.find('tbody').append(html);
								
								if(childAct.get('children') !=  null && childAct.get('children').length >0 ) {
									var grandChildrenAct = childAct.get('children');
									for(var l=0; l<grandChildrenAct.length; l++) {
										var grandChildAct = grandChildrenAct.at(l);
										json = grandChildAct.toJSON();
										json.padding=90;
										html=this.mainTblTbodyActivityTemplate(json);
										this.$el.find('tbody').append(html);
									}
									
								}
							}
						}
						
					}
					
				}
				
			},this)
		});		
	}
});