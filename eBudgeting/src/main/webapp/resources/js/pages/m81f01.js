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
	events : {
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
		this.currentTargetResult.set('result', this.$el.find('#result').val());
		this.currentTargetResult.set('remark', this.$el.find('#remark').val());
		this.currentTargetResult.set('reportedResultDate', this.$el.find('#reportedResultDate').val());
		
		this.currentTargetResult.save(null, {
			success: _.bind(function() {
				alert("บันทึกการรายงานผลเรียบร้อยแล้ว");
				this.currentTargetReport.set('latestResult', this.currentTargetResult);
				this.render();
			},this)
		});
	},
	inputResult: function(e) {
		this.currentTargetResult = new ActivityTargetResult();
		this.currentTargetResult.set('report', this.currentTargetReport);
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
	renderWithReport: function(report) {
		this.currentTargetReport = report;
		this.render();
	},
	render : function() {
		if(this.currentTargetReport != null) {
			this.$el.find('.modal-header span').html("จัดสรรเป้าหมาย: ");

			
			var json = this.currentTargetReport.toJSON();
			var html = this.targerReportModalTemplate(json);
			this.$el.find('.modal-body').html(html);
			
			var monthly = this.currentTargetReport.get("monthlyReports");
			
			if(monthly.length != 12) {
				alert("กิจกรรมนี้ยังไม่ได้มีการทำแผน");
				return;
			}
			
			var q1Plan, q1Result, q2Plan, q2Result, q3Plan, q3Result, q4Plan, q4Result;
			q1Plan = q2Plan = q3Plan = q4Plan = q1Result = q2Result = q3Result = q4Result = 0;
			
			for(var i=0; i<12; i++) {
				var report = monthly.at(i);
				if(i<3) {
					q1Plan += report.get("activityPlan");
					q1Result += report.get("activityResult");
				} else if(i<6) {
					q2Plan += report.get("activityPlan");
					q2Result += report.get("activityResult");					
				} else if(i<9) {
					q3Plan += report.get("activityPlan");
					q3Result += report.get("activityResult");
				} else {
					q4Plan += report.get("activityPlan");
					q4Result += report.get("activityResult");
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
		var targetReportId = $(e.target).parents('li').attr('data-id');
		
		// now we're going the report of this target
		
		var report = ActivityTargetReport.findOrCreate({id: targetReportId});
		
		report.fetch({
			success: _.bind(function() {
				this.modalView.renderWithReport(report);
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