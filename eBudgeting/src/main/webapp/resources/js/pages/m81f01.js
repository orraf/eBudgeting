var ModalView = Backbone.View.extend({
	/**
	 * @memberOf ModalView
	 */
	initialize: function(options) {
		if(options.parentView != null) {
			this.parentView = options.parentView;
		}
	},
	el: "#modal",
	targerReportModalTemplate: Handlebars.compile($("#targerReportModalTemplate").html()),
	resultInputTemplate: Handlebars.compile($("#resultInputTemplate").html()),
	resultBudgetInputTemplate: Handlebars.compile($("#resultBudgetInputTemplate").html()),
	events : {
		"click #resultBudgetInputBtn" : "inputBudgetResult",
		"click #resultInputBtn" : "inputResult",
		"click #backToModalBtn" : "backToModal",
		"click #saveResultBtn" : "saveResult",
		"click #cancelBtn" : "cancelModal"
	},
	cancelModal: function(e) {
		this.$el.modal('hide');
		if(this.parentView != null) {
			this.parentView.render();
		}
	},
	
	backToModal: function(e) {
		this.render();
	},
	saveResult: function(e) {
		if(this.currentTargetResult.get("resultBudgetType") == true) {
			this.currentTargetResult.set('budgetResult',this.$el.find('#budgetResult').val());
			this.currentTargetResult.set('budgetFiscalMonth', this.$el.find('#budgetFiscalMonth').val());
		} else {
			this.currentTargetResult.set('reportedResultDate', this.$el.find('#reportedResultDate').val());
			this.currentTargetResult.set('result', this.$el.find('#result').val());
		}
		this.currentTargetResult.set('remark', this.$el.find('#remark').val());
		
		
		this.currentTargetResult.save(null, {
			success: _.bind(function() {
				alert("บันทึกการรายงานผลเรียบร้อยแล้ว");
				this.currentTargetReport.set('latestResult', this.currentTargetResult);
				this.currentTargetResult.get('report').get('target').set('filterReport', this.currentTargetReport);
				this.render();
			},this)
		});
	},
	inputResult: function(e) {
		this.currentTargetResult = new ActivityTargetResult();
		this.currentTargetResult.set('report', this.currentTargetReport);
		this.currentTargetResult.set('resultBudgetType', false);
		var json = {};
		json.unit = this.currentTargetReport.get("target").get("unit").toJSON();
		var html = this.resultInputTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		this.$el.find('#reportedResultDate').datepicker({
			autoclose: true,
			language: 'th',
			format: 'dd/mm/yyyy'
		});
		
		
	},
	inputBudgetResult: function(e) {
		this.currentTargetResult = new ActivityTargetResult();
		this.currentTargetResult.set('report', this.currentTargetReport);
		this.currentTargetResult.set('resultBudgetType', true);
		
		var monthlyReports = this.currentTargetReport.get("activityPerformance").get("monthlyBudgetReports");
		
		var json = {};
		json.month=[{fiscalMonth: 0, name:'ตุลาคม'},{fiscalMonth: 1, name:'พฤศจิกายน'},{fiscalMonth: 2, name:'ธันวาคม'},
		            {fiscalMonth: 3, name:'มกราคม'},{fiscalMonth: 4, name:'กุมภาพันธ์'},{fiscalMonth: 5, name:'มีนาคม'},
		            {fiscalMonth: 6, name:'เมษายน'},{fiscalMonth: 7,name:'พฤษภาคม'},{fiscalMonth: 8,name:'มิถุนายน'},
		            {fiscalMonth: 9, name:'กรกฎาคม'},{fiscalMonth: 10,name:'สิงหาคม'},{fiscalMonth: 11,name:'กันยายน'}];
		
		
		var monthNumber = (moment().month() + 3) % 12 ;
		json.month[monthNumber].current = true;
		
		monthlyReports.forEach(function(report) {
			if(report.get('bugetResult') != null) {
				json.month[report.get('fiscalMonth')].hasOldValue = true;
				json.month[report.get('fiscalMonth')].oldValue = report.get('budgetResult');
			}
		});
		
		var html = this.resultBudgetInputTemplate(json);
		this.$el.find('.modal-body').html(html);
		
		
		
	},
	renderWithReport: function(report) {
		this.currentTargetReport = report;
		this.render();
	},
	render : function() {
		if(this.currentTargetReport != null) {
			this.$el.find('.modal-header span').html("บันทึกผลการดำเนินงาน: "+ this.currentTargetReport.get('target').get('activity').get('name'));

			
			var json = this.currentTargetReport.toJSON();
			var html = this.targerReportModalTemplate(json);
			this.$el.find('.modal-body').html(html);
			
			var monthly = this.currentTargetReport.get("monthlyReports");
			var budgetMontly = this.currentTargetReport.get("activityPerformance").get("monthlyBudgetReports");
			
			if(monthly.length != 12) {
				alert("กิจกรรมนี้ยังไม่ได้มีการทำแผน");
				return;
			}
			
			var q1Plan, q1Result, q2Plan, q2Result, q3Plan, q3Result, q4Plan, q4Result;
			q1Plan = q2Plan = q3Plan = q4Plan = q1Result = q2Result = q3Result = q4Result = 0;
			
			var q1BudgetPlan, q1BudgetResult, q2BudgetPlan, q2BudgetResult, q3BudgetPlan, q3BudgetResult, q4BudgetPlan, q4BudgetResult;
			q1BudgetPlan = q2BudgetPlan = q3BudgetPlan = q4BudgetPlan = q1BudgetResult = q2BudgetResult = q3BudgetResult = q4BudgetResult = 0;
			
			
			
			for(var i=0; i<12; i++) {
				var report = monthly.at(i);
				var budgetReport = budgetMontly.at(i);
				if(i<3) {
					q1Plan += report.get("activityPlan");
					q1Result += report.get("activityResult");
					q1BudgetPlan += budgetReport.get("budgetPlan");
					q1BudgetResult += budgetReport.get("budgetResult");
				} else if(i<6) {
					q2Plan += report.get("activityPlan");
					q2Result += report.get("activityResult");
					q2BudgetPlan += budgetReport.get("budgetPlan");
					q2BudgetResult += budgetReport.get("budgetResult");
				} else if(i<9) {
					q3Plan += report.get("activityPlan");
					q3Result += report.get("activityResult");
					q3BudgetPlan += budgetReport.get("budgetPlan");
					q3BudgetResult += budgetReport.get("budgetResult");
				} else {
					q4Plan += report.get("activityPlan");
					q4Result += report.get("activityResult");
					q4BudgetPlan += budgetReport.get("budgetPlan");
					q4BudgetResult += budgetReport.get("budgetResult");
				}
			}
			
			this.$el.find("#Q1Plan").html("<strong>"+addCommas(q1Plan)+"</strong>");
			this.$el.find("#Q2Plan").html("<strong>"+addCommas(q2Plan)+"</strong>");
			this.$el.find("#Q3Plan").html("<strong>"+addCommas(q3Plan)+"</strong>");
			this.$el.find("#Q4Plan").html("<strong>"+addCommas(q4Plan)+"</strong>");
			
			this.$el.find("#Q1Result").html("<strong>"+addCommas(q1Result)+"</strong>");
			this.$el.find("#Q2Result").html("<strong>"+addCommas(q2Result)+"</strong>");
			this.$el.find("#Q3Result").html("<strong>"+addCommas(q3Result)+"</strong>");
			this.$el.find("#Q4Result").html("<strong>"+addCommas(q4Result)+"</strong>");
			
			this.$el.find("#totalResultTxt").val(addCommas(q1Result + q2Result + q3Result + q4Result));
			
			this.$el.find("#Q1BudgetPlan").html("<strong>"+addCommas(q1BudgetPlan)+"</strong>");
			this.$el.find("#Q2BudgetPlan").html("<strong>"+addCommas(q2BudgetPlan)+"</strong>");
			this.$el.find("#Q3BudgetPlan").html("<strong>"+addCommas(q3BudgetPlan)+"</strong>");
			this.$el.find("#Q4BudgetPlan").html("<strong>"+addCommas(q4BudgetPlan)+"</strong>");
			
			this.$el.find("#Q1BudgetResult").html("<strong>"+addCommas(q1BudgetResult)+"</strong>");
			this.$el.find("#Q2BudgetResult").html("<strong>"+addCommas(q2BudgetResult)+"</strong>");
			this.$el.find("#Q3BudgetResult").html("<strong>"+addCommas(q3BudgetResult)+"</strong>");
			this.$el.find("#Q4BudgetResult").html("<strong>"+addCommas(q4BudgetResult)+"</strong>");
			
			this.$el.find("#totalBudgetResultTxt").val(addCommas(q1BudgetResult + q2BudgetResult + q3BudgetResult + q4BudgetResult));
			
			this.$el.modal({show: true, backdrop: 'static', keyboard: false});
			return this;

		}
	}
});

var MainCtrView = Backbone.View.extend({
	/**
     *  @memberOf MainCtrView
     */
	initialize : function(options) {
		this.modalView = new ModalView({parentView: this});	

	},

	el : "#mainCtr",
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	
	
	events : {
		"click a.showReport" : "showReportModal"
	},
	showReportModal: function(e) {
		var targetReportId = $(e.target).parents('tr').attr('data-id');
		
		// now we're going the report of this target
		
		var report = ActivityTargetReport.findOrCreate({id: targetReportId});
		
		report.fetch({
			success: _.bind(function() {
				this.modalView.renderWithReport(report);
				report.get('target').set('filterReport', report);
			},this)
		});
		
	},
	setCollection: function(collectton) {
		this.collection = collectton;
		if( this.collection != null ) {
			this.collection.bind('reset', this.render, this);
		} 
	},
	
	render: function() {
		var json = {};
		if(this.collection != null) {
			json = this.collection.toJSON();
			for(var i=0; i< json.length; i++) {
				var act = json[i].filterActivities;
				for(var j=0; j< act.length; j++) {
					var tgt = act[j].filterTargets;
					tgt[0].firstRow = true;
					tgt[0].targetsLength = tgt.length;
					for(var k=0; k<tgt.length; k++) {
						var rpt = tgt[k].filterReport;
						if(rpt == null) {
							//we just return here
							return this;
						} else if(rpt.latestResult != null) {
							rpt.lastSaveTxt = moment.utc(rpt.latestResult.timestamp).fromNow();
							rpt.lastSaveTxt += " / " + rpt.latestResult.person.firstName + " " + rpt.latestResult.person.lastName;
						} else {
							rpt.lastSaveTxt = 'ยังไม่เคยรายงาน';
						}
					}
					
				}
			}
						
		}
		
		this.$el.html(this.mainCtrTemplate(json));
		
		return this;
	}
	
});