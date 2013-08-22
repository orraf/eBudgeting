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
		this.rootSelection.url = appUrl("/Objective/BudgetAsset/ficalYear/" + fiscalYear);
		this.rootSelection.fetch({
			success: _.bind(function() {
				var json = this.rootSelection.toJSON();
				var html = this.objectiveSelectionTemplate(json);
				this.$el.html(html);
			}, this)
		});
	}
});

var AssignAssetPlanModal = Backbone.View.extend({
	/**
     *  @memberOf AssignAssetPlanModal
     */
	initialize : function(options) {
		this.parentView = options.parentView;
		this.assetMethods = new AssetMethodCollection();
		this.assetMethods.fetch({
			url: appUrl('/AssetMethod/all')
		});
	},
	assignAssetPlanModalTemplate: Handlebars.compile($("#assignAssetPlanModalTemplate").html()),
	assetMethodOptionTemplate: Handlebars.compile($("#assetMethodOptionTemplate").html()),
	stepInputTemplate: Handlebars.compile($("#stepInputTemplate").html()),
	
	budgetPlanTemplate: Handlebars.compile($("#budgetPlanTemplate").html()),
	budgetPlanTblTemplate: Handlebars.compile($("#budgetPlanTblTemplate").html()),
	
	el : "#assignAssetPlanModal",
	
	events: {
		"change #inputAssetMethod" : "changeAssetMethod",
		"click #saveAssetPlanBtn" : "saveAssetPlan",
		"change #contractedBudgetPlan" : "changeContractedBudgetPlan",
		"change #estimatedCost" : "changeEstimatedCost",
		
		"change input.datepickerTxt" : "changeInputDatepicker",
		"click #cancelBtn" : "cancel",
		"change input#assetBudgetPlanLengthTxt" : "assetBudgetPlanLength",
		"change .assetBudgetPlanTxt" : "assetBudgetPlanChange"
	},
	cancel:function(e) {
		this.$el.modal('hide');
	},
	changeContractedBudgetPlan: function(e) {
		var dataField = $(e.target).attr('data-field');
		this.currentAssetAllocation.set(dataField, $(e.target).val());
	},
	changeEstimatedCost: function(e) {
		var dataField = $(e.target).attr('data-field');
		this.currentAssetAllocation.set(dataField, $(e.target).val());
		
	},
	assetBudgetPlanChange: function(e) {
		var index = $(e.target).parents('tr').attr('data-index');
		
		var dataField = $(e.target).attr('data-field');
		
		var currentPlans = this.currentAssetAllocation.get('assetBudgetPlans');
		
		var plan = currentPlans.at(index);
		
		if(dataField == "planAmount" ) {
			plan.set(dataField, parseFloat($(e.target).val()));
		} else if(dataField == "planDate" || dataField == "planInstallmentDate") {
			plan.set(dataField, formatThDateToISO($(e.target).val()));	
		}
	},
	assetBudgetPlanLength: function(e) {
		
		var length = $(e.target).val();
		var currentPlans = this.currentAssetAllocation.get('assetBudgetPlans');
		
		console.log(currentPlans.toJSON());
		
		if(currentPlans.length == length) {
			//do nothing here
			return false;
		} else if(currentPlans.length > length){
			// we have to reduce 
			for(var i=currentPlans.length; i>length; i--) {
				currentPlans.pop();
			}
		} else {
			for(var i=currentPlans.length; i<length; i++) {
				var newPlan = new AssetBudgetPlan();
				newPlan.set('assetAllocation', this.currentAssetAllocation);
				
				currentPlans.push(newPlan);
			}
		}
		
		
		console.log(currentPlans.toJSON());
		
		var html=this.budgetPlanTblTemplate(currentPlans.toJSON());
		this.$el.find('#budgetPlanDiv').html(html);
		
		$('.datepickerTxt').datepicker({                
            isBE: true,
            autoConversionField: false,
             beforeShow: function() {
                setTimeout(function(){
                	$('.ui-datepicker').css('z-index', 9999);
                }, 0);
             }
		});
		
	},
	
	
	changeAssetMethod: function(e) {
		var selectAssetMethodId = $(e.target).val();
		if(selectAssetMethodId != 0) {
			this.currentAssetMethod = AssetMethod.findOrCreate(selectAssetMethodId);
			this.currentAssetAllocation.set('assetMethod', this.currentAssetMethod);
			
			var json = this.currentAssetMethod.get('steps').toJSON();
			
			for(var i=0; i<json.length; i++) {
				
				json[i].planBegin=null;
				json[i].planEnd=null;
			}
			var html = this.stepInputTemplate(json);
			this.$el.find('#assetDateTab').html(html);
			
			json = {};
			json.plans = this.currentAssetAllocation.get('assetBudgetPlans');
			json.length = this.currentAssetAllocation.get('assetBudgetPlans').length;
			html = this.budgetPlanTemplate(json);
			this.$el.find('#assetBudgetTab').html(html);
			
			if(this.currentAssetAllocation.get('assetBudgetPlans').length > 0) {
				html=this.budgetPlanTblTemplate(this.currentAssetAllocation.get('assetBudgetPlans').toJSON());
				this.$el.find('#budgetPlanDiv').html(html);
			}
			
			
			$('.datepickerTxt').datepicker({
			    isBE: true,
                autoConversionField: false,
                 beforeShow: function() {
                    setTimeout(function(){
                    	$('.ui-datepicker').css('z-index', 9999);
                    }, 0);
                 }
			});
			
		}
	},
	changeInputDatepicker: function(e) {
		
		if($(e.target).val().length>0) {
			$(e.target).parents('div.control-group').removeClass('error');
		}
	},
	
	saveAssetPlan: function(e) {
		var isValid = true;
		// first check!
		_.forEach(this.$el.find('input[type=text]'),function(input) {
			var $el = $(input);
			if($el.val() == null || $el.val().length == 0) {
				isValid = false;
				$el.parents('div.control-group').addClass('error');
			}
		});
		
		if(!isValid) {
			alert('กรุณากรอกข้อมูลให้ครบถ้วน');
			return;
		}
		
		//create stepReport
		var stepReports = new AssetStepReportCollection();
		for(var i=0; i<this.currentAssetMethod.get('steps').length; i++) {
			var stepReport = new AssetStepReport();
			
			stepReport.set('step', this.currentAssetMethod.get('steps').at(i));
			stepReport.set('planBegin', formatThDateToISO(this.$el.find('#planBegin_'+i).val()));
			stepReport.set('planEnd', formatThDateToISO(this.$el.find('#planEnd_'+i).val()));
			stepReport.set('assetAllocation', this.currentAssetAllocation);
			stepReport.set('stepOrder', i);
			stepReports.add(stepReport);
		}
		this.currentAssetAllocation.set('assetStepReports', stepReports);
		this.currentAssetAllocation.set('assetMethod', this.currentAssetMethod);
		//console.log(this.currentAssetAllocation.toJSON());
		
		// now reformat planDate
		var currentPlans = this.currentAssetAllocation.get('assetBudgetPlans');
		if(currentPlans.length >0) {
			
			var sum = 0.0;
			
			
			for(var i=0; i< currentPlans.length; i++) {
				sum += parseFloat(currentPlans.at(i).get('planAmount'));
				
			}
			
			if(sum != this.currentAssetAllocation.get('unitBudget') * this.currentAssetAllocation.get('quantity')){
				alert('กรุณาระบุแผนการเบิกจ่ายให้เท่ากับวงเงินงบประมาณ');
				return false;
			}
		}
		
		
		$.ajax({
			type: "POST",
			url: appUrl('/AssetAllocation/saveAssetPlan/' + this.currentAssetAllocation.get('id')),
			data: JSON.stringify(this.currentAssetAllocation.toJSON()),
			success: function() {
				alert("บันทึกข้อมูลเสร็จแล้ว");
			},
			dataType: "json",
			contentType: "application/json; charset=UTF-8"
		});
		
		
	},
	
	render: function(assetAllocation) {
		this.currentAssetAllocation = assetAllocation;
		
		this.$el.find('.modal-header span').html("บันทึกแผนงบลงทุน");
		
		var json = this.currentAssetAllocation.toJSON();
		var html = this.assignAssetPlanModalTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		//console.log(this.currentAssetAllocation.get('operator').get('id'));
		//console.log(currentOrganizationId);
		if(this.currentAssetAllocation.get('operator').get('id') != currentOrganizationId) {
			$('#saveAssetPlanBtn').hide();
		} else {
			$('#saveAssetPlanBtn').show();
		}
		
		
		// now populate the option!
		json = this.assetMethods.toJSON();
		if(this.currentAssetAllocation.get('assetMethod') != null) {
			for(var i=0; i<json.length; i++) {
				if(json[i].id == this.currentAssetAllocation.get('assetMethod').get('id')){
					json[i].selected=true;
					
					this.currentAssetAllocation.fetch({
						url: appUrl('/AssetAllocation/' + this.currentAssetAllocation.get('id')),
						success: _.bind(function() {

							
							this.currentAssetMethod = this.currentAssetAllocation.get('assetMethod');
							
							var json = this.currentAssetAllocation.get('assetMethod').get('steps').toJSON();
							for(var i=0; i<json.length; i++) {
								var reports = this.currentAssetAllocation.get('assetStepReports');
								
								json[i].planBegin=reports.at(i).get('planBegin');
								json[i].planEnd=reports.at(i).get('planEnd');
							}
							var html = this.stepInputTemplate(json);
							this.$el.find('#assetDateTab').html(html);
							
							json = {};
							json.plans = this.currentAssetAllocation.get('assetBudgetPlans');
							json.length = this.currentAssetAllocation.get('assetBudgetPlans').length;
							html = this.budgetPlanTemplate(json);
							this.$el.find('#assetBudgetTab').html(html);
							
							if(this.currentAssetAllocation.get('assetBudgetPlans').length > 0) {
								html=this.budgetPlanTblTemplate(this.currentAssetAllocation.get('assetBudgetPlans').toJSON());
								this.$el.find('#budgetPlanDiv').html(html);
							}
							
							
							$('.datepickerTxt').datepicker({                
			                    isBE: true,
			                    autoConversionField: false,
			                     beforeShow: function() {
			                        setTimeout(function(){
			                        	$('.ui-datepicker').css('z-index', 9999);
			                        }, 0);
			                     }
							});
							
						}, this)
					});
					
//					reports = new AssetStepReportCollection();
//					reports.fetch({
//						url: appUrl('/AssetStepReport/allByAssetAllocation/'+this.currentAssetAllocation.get('id')),
//						success: _.bind(function() {
//							
//							
//							var json = this.currentAssetAllocation.get('assetMethod').get('steps').toJSON();
//							for(var i=0; i<json.length; i++) {
//								
//								json[i].planBegin=moment(reports.at(i).get('planBegin')).format('DD/MM/YYYY');
//								json[i].planEnd=moment(reports.at(i).get('planEnd')).format('DD/MM/YYYY');
//							}
//							var html = this.stepInputTemplate(json);
//							this.$el.find('#assetDateTab').html(html);
//							
//							html = this.budgetPlanTemplate();
//							this.$el.find('#assetBudgetTab').html(html);
//							
//							$('.datepicker').datepicker({
//							    format: 'dd/mm/yyyy'
//							});
//						},this)
//					});
					
				}
			}
		}
		this.$el.find('#inputAssetMethod').append(this.assetMethodOptionTemplate(json));
		
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
		
		this.assignAssetPlanModal = new AssignAssetPlanModal({parentView: this});
	},

	el : "#mainTbl",
	loadingTpl : Handlebars.compile($("#loadingTemplate").html()),
	mainTblTemplate : Handlebars.compile($("#mainTblTemplate").html()),
	mainTblTbodyAssetAllocationTemplate:  Handlebars.compile($("#mainTblTbodyAssetAllocationTemplate").html()),
	
	events : {
		"click a.assetPlanLnk" : "assetPlan"
	},
	assetPlan: function(e) {
		var assetAllocationId = $(e.target).parents('tr').attr('data-id');
		var assetAllocation = AssetAllocation.findOrCreate(assetAllocationId);
	
		
		this.assignAssetPlanModal.render(assetAllocation);
		return false;
		
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
		this.assetAllocations  = new AssetAllocationCollection();
		this.assetAllocations.fetch({
			url: appUrl('/AssetAllocation/currentUser/forObjective/'+this.objective.get('id')),
			success: _.bind(function(model, response, options) {
				var json = this.objective.toJSON();
				var html = this.mainTblTemplate(json);
				this.$el.html(html);
				
				json = this.assetAllocations.toJSON();
								
				html = this.mainTblTbodyAssetAllocationTemplate(json);
				this.$el.find('tbody').append(html);
				
			},this)
		});
/*		
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
							}
						}
						
					}
					
				}
				
			},this)
		});		
		*/
	}
});