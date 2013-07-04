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
	},
	
	setCurrentActivity: function(activity){
		this.currentActivity = activity;
	},
	
	events: {
		"click #organizationSearchBtn" : "organizationSearch",
		"click .addOrganization" : "addOrganizationTarget",
		"click .removeOrganizationTarget" : "removeOrganizationTarget",
		
		"change .proposalAllocated" : "changeProposalAllocated",
		"change .budgetAllocated" : "changeBudgetAllocated",
		
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
		mainTblView.updateSumTable();
	},
	saveAssignTarget: function(e) {
		var sum=0.0;
		// now put the sum up
//		_.forEach(this.$el.find("input.proposalAllocated"), function(el) {
//			sum += parseInt($(el).val());
//		});
//		
//		if(sum != parseInt($('#totalInputTxt').val().replace(/,/g, ''))) {
//			alert("กรุณาตรวจสอบการจัดสรร ค่าเป้าหมายที่จัดสรรให้หน่วยงานรวมแล้วไม่เท่ากับค่าเป้าหมายที่จัดสรรไว้");
//			return;
//		}
 		
		sum = 0;
		_.forEach(this.$el.find("input.budgetAllocated"), function(el) {
			sum += parseFloat($(el).val());
		});
		
 		this.$el.find('button#saveAssignTargetBtn').html('<icon class="icon-refresh icon-spin"></icon> กำลังบันทึกข้อมูล...');
 		
 		// now set reportLevel to 2
 		for(var i=0; i<this.targetReports.length; i++) {
 			this.targetReports.at(i).set('reportLevel', 2);
 		}
 		
 		// we should be ready to save the 
 		Backbone.sync('create', this.targetReports, {
 			success: _.bind(function() {
 				alert('บันทึกเรียบร้อยแล้ว');
 				
 				this.currentTarget.set('budgetAllocated', sum);
 				//now update
 				
 				$('#target_'+ this.currentTarget.get('id') +'-budgetAllocated').html(addCommas(sum));
 				$('#target_'+ this.currentTarget.get('id') +'-budgetAllocated').attr('data-value', sum);
 				
 				this.cancelAssignTarget();
 			},this)
 		});
	},
	changeBudgetAllocated: function(e) {
		// validate this one first
		if( isNaN( +$(e.target).val() ) ) {
			$(e.target).parent('div').addClass('control-group error');
			alert("กรุณาใส่ข้อมูลเป็นตัวเลข");
		} else {
			$(e.target).parent('div').removeClass('control-group error'); 
			var i = $(e.target).parents('tr').prevAll('tr').length;
			
			this.targetReports.at(i).get('activityPerformance').set('budgetAllocated', $(e.target).val());
			this.updateSumTarget();	
		}
		
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
			this.updateSumTarget();	
		}
		
	},
	addOrganizationTarget: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		var newTargetReport = new ActivityTargetReport();
		newTargetReport.set('owner', organization);
		newTargetReport.set('targetValue', 0);
		newTargetReport.set('target', this.currentTarget);
		

		var newPerformance = new ActivityPerformance();
		newPerformance.set('activity', this.currentActivity);
		newPerformance.set('owner', organization);
		
		newTargetReport.set('activityPerformance', newPerformance);
		
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
		var sum=0.0;
		// now put the sum up
		_.forEach(this.$el.find("input.proposalAllocated"), function(el) {
			sum += parseFloat($(el).val());
		});
		
		$('#sumTotalAllocated').html(addCommas(sum));
		
		
		// now sum the budgetAllocated
		var sumBudgetAllocated = 0.0;
		this.targetReports.forEach(function(report) {
			var budgetAllocated = parseFloat(report.get("activityPerformance").get("budgetAllocated"));
			sumBudgetAllocated += budgetAllocated;
		});
		
		if(isNaN(sumBudgetAllocated)) {
			sumBudgetAllocated = 0;
		}
				
		
		// now update the ui
		this.$el.find('#totalBudgetInputTxt').val(addCommas(sumBudgetAllocated));
		$('#sumTotalBudgetAllocated').html(addCommas(sumBudgetAllocated));
		

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
		
		if(query = null) {
			query ="";
		}
		
		this.organizationSearchList = new OrganizationCollection();
		this.organizationSearchList.url = appUrl("/Organization/parentId/"+organizationId+"/findByName");
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
		
		var json = this.currentTargetReport.toJSON();
		
		var html = this.assignTargetValueModalTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		//now fill in 
		this.targetReports = new ActivityTargetReportCollection();
		this.targetReports.url = appUrl('/ActivityTargetReport/findByTarget/' + this.currentTarget.get('id') 
				+ '/parentOrganization/' + organizationId);
		
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
		
		if(isNaN(parseFloat(this.currentActivityTarget.get('targetValue'))) ) {
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
	budgetProposalSelectionTemplate: Handlebars.compile($("#budgetProposalSelectionTemplate").html()),
	el: "#budgetSlt",
	events: {
		"click .budgetProposalSelect" : "budgetProposalSelect"
	},
	budgetProposalSelect: function(e) {
		var propossalId = $(e.target).parents('li').attr('data-id');
		var proposal = BudgetProposal.findOrCreate(propossalId);
		
		mainTblView.renderWithProposal(proposal);
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
		this.budgetProposals = new BudgetProposalCollection();
		this.budgetProposals.fetch({
			url: appUrl('/BudgetProposal/findByFiscalyearAndOwner/' + fiscalYear + '/' + organizationId),
			success: _.bind(function(model, response, options) {
				var json = this.budgetProposals.toJSON();
				var html = this.budgetProposalSelectionTemplate(json);
				this.$el.html(html);
			},this)
		});
	}
});

var MainTblView = Backbone.View.extend({
	/**
     *  @memberOf MainTblView
     */
	initialize : function(options) {
		this.assignTargetValueModalView = new AssignTargetValueModalView({parentView: this});
		this.totalAllocatedBudget = 0;
		this.totalAllocatedBudgetLeft = 0;
		this.allocatedBudget = 0;
	},
	mainTblTemplate : Handlebars.compile($("#mainTblTemplate").html()),
	mainTblTbodyActivityTemplate:  Handlebars.compile($("#mainTblTbodyActivityTemplate").html()),
	mainTblTbodyObjectiveTemplate: Handlebars.compile($("#mainTblTbodyObjectiveTemplate").html()),
	el : "#mainTbl",
	events: {
		"click .assignTargetValueLnk" : "assignTargetValueLnk"
	},
	assignTargetValueLnk: function(e) {
		var targetId = $(e.target).parents('li').attr('data-id');
		var activityTargetReport = ActivityTargetReport.findOrCreate(targetId);
		
		this.assignTargetValueModalView.setCurrentActivity(activityTargetReport.get('target').get('activity'));
		this.assignTargetValueModalView.setCurrentTargetReport(activityTargetReport);
		this.assignTargetValueModalView.render();
	},
	updateSumTable : function() {
		var tableSum = 0.0;
		$('.budgetAllocatedSpn').each(function(index) {
			var budget = parseFloat($(this).attr('data-value'));
			if(isNaN(budget)) {
				budget = 0.0;
			}
			tableSum += budget;
		});
		
		this.totalAllocatedBudget=tableSum;
		this.allocatedBudget = this.proposal.get('amountAllocated');
		this.totalAllocatedBudgetLeft = this.allocatedBudget - this.totalAllocatedBudget;
		
		this.$el.find('#totalAllocatedBudget').html(addCommas(this.totalAllocatedBudget) + " บาท");
		this.$el.find('#totalAllocatedBudgetLeft').html(addCommas(this.totalAllocatedBudgetLeft) + " บาท");
	},
	renderWithProposal: function(proposal) {
		this.proposal = proposal;
		
		this.childObjectives = new ObjectiveCollection();
		this.childObjectives.fetch({
			url: appUrl('/Objective/getChildrenAndloadActivityAndOwnerId/'
					+proposal.get('forObjective').get('id')+'/' + organizationId),
			success: _.bind(function(model, response, options) {
				var json = this.proposal.toJSON();
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
				
				// now update value
				this.updateSumTable();
				
			},this)
		});
		
		
	}
	
});