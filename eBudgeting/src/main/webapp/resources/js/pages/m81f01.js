var BudgetProposalSelectionView = Backbone.View.extend({
	/**
	 * @memberOf BudgetProposalSelectionView
	 */
	initialize : function(options) {
		if (options.organizationId != null) {
			this.organizationId = options.organizationId;
			this.organization = Organization.findOrCreate({
				id : this.organizationId
			});

		}
	},
	objectiveSelectionTemplate : Handlebars.compile($("#objectiveSelectionTemplate").html()),
	el : "#budgetSlt",
	events : {
		"click .objectiveSelect" : "objectiveSelect"
	},
	objectiveSelect : function(e) {
		var objectiveId = $(e.target).parents('li').attr('data-id');
		var objective = Objective.findOrCreate(objectiveId);

		// show Loading...
		//		mainTblView.showLoading(objective);
		//		
		//		mainTblView.renderWithObjective(objective);

		mainTblView.showLoading();

		objectiveCollection.url = appUrl('/Objective/fiscalYear/' + fiscalYear + '/' + objectiveId + '/findByActivityTargetReportOfOwnerId/' + organizationId);
		objectiveCollection.fetch({
			success : function() {
				mainTblView.setCollection(objectiveCollection);
				mainTblView.render();
			}
		});
	},
	render : function() {
		this.organization.fetch({
			success : _.bind(function(model, response, options) {
				this.renderSelection();
			}, this)
		});

		return this;
	},
	renderSelection : function() {
		this.rootSelection = new ObjectiveCollection();
		this.rootSelection.url = appUrl("/Objective/currentActivityOwner/" + fiscalYear + "/ownerId/" + organizationId);
		this.rootSelection.fetch({
			success : _.bind(function() {
				var json = this.rootSelection.toJSON();
				var html = this.objectiveSelectionTemplate(json);
				this.$el.html(html);
			}, this)
		});
	}
});
var ModalView = Backbone.View.extend({
	/**
	 * @memberOf ModalView
	 */
	initialize : function(options) {
		if (options.parentView != null) {
			this.parentView = options.parentView;
		}
	},
	el : "#modal",
	targetReportModalTemplate : Handlebars.compile($("#targetReportModalTemplate").html()),
	resultInputTemplate : Handlebars.compile($("#resultInputTemplate").html()),
	resultBudgetInputTemplate : Handlebars.compile($("#resultBudgetInputTemplate").html()),
	targetReportByMonthTemplate : Handlebars.compile($("#targetReportByMonthTemplate").html()),
	events : {
		"click #resultBudgetInputBtn" : "inputBudgetResult",
		"click #resultInputBtn" : "inputResult",
		"click #backToModalBtn" : "backToModal",
		"click #saveResultBtn" : "saveResult",
		"click #cancelBtn" : "cancelModal",
		"click .budgetResultLnk" : "showBudgetResult",
		"click .editResultLnk" : "editResult",
		"click .activityResultLnk" : "showActivityResult"
	},
	cancelModal : function(e) {
		this.$el.modal('hide');
		if (this.parentView != null) {
			this.parentView.render();
		}
	},

	showBudgetResult : function(e) {
		var fiscalMonth = $(e.target).parents('tr').attr('data-fiscalmonth');
		var monthlyBudgetResultId = $(e.target).parents('td').attr('data-id');
		var targetResultId = $(e.target).attr('data-id');

		var latestResult = this.currentTargetReport.get('latestResult');



		if (latestResult != null && latestResult.get('budgetFiscalMonth') == fiscalMonth &&
			latestResult.get('resultBudgetType') == true) {
			this.currentTargetResult = latestResult;

			var monthlyBudget = MonthlyBudgetReport.find(monthlyBudgetResultId);
			monthlyBudget.set('targetResultId', this.currentTargetResult.get('id'));

			this.renderBudgetResult(fiscalMonth, this.currentTargetResult.get('id'));

		} else if (targetResultId == null) {
			this.currentTargetResult = new ActivityTargetResult();
			this.currentTargetResult.fetch({
				url : appUrl('/ActivityTargetResult/findBgtResultByReport/' + this.currentTargetReport.get('id') + '/fiscalMonth/' + fiscalMonth),
				success : _.bind(function(model, response, options) {


					var monthlyBudget = MonthlyBudgetReport.find(monthlyBudgetResultId);
					monthlyBudget.set('targetResultId', this.currentTargetResult.get('id'));



					this.renderBudgetResult(fiscalMonth, model.get('id'));

				}, this)
			});
		} else {

			this.currentTargetResult = ActivityTargetResult.find(targetResultId);
			this.renderBudgetResult(fiscalMonth, targetResultId);
		}

		return false;
	},

	showActivityResult : function(e) {
		var fiscalMonth = $(e.target).parents('tr').attr('data-fiscalMonth');

		this.currentTargetResultCollection = new ActivityTargetResultCollection();

		this.currentTargetResultCollection.fetch({
			url : appUrl('/ActivityTargetResult/findResultByReport/' + this.currentTargetReport.get('id') + '/fiscalMonth/' + fiscalMonth),
			success : _.bind(function() {
				var html = this.targetReportByMonthTemplate(this.currentTargetResultCollection.toJSON());
				this.$el.find('.modal-body').html(html);

			}, this),
			error : function(model, xhr, options) {

				alert(xhr);
				alert('error: ' + xhr);

			}
		});


		return false;
	},

	backToModal : function(e) {
		this.$el.find('.modal-header span').html("บันทึกผลการดำเนินงาน: " + this.currentTargetReport.get('target').get('activity').get('name'));
		this.render();
	},
	saveResult : function(e) {
		// first disable this button!
		$(e.target).attr('disabled', 'disabled');
		$(e.target).html('กำลังบันทึกผล...');

		var report = this.currentTargetReport;

		if (this.currentTargetResult.get("resultBudgetType") == true) {
			this.currentTargetResult.set('budgetResult', parseFloat(this.$el.find('#budgetResult').val()));
			this.currentTargetResult.set('budgetFiscalMonth', parseInt(this.$el.find('#budgetFiscalMonth').val()));

			var monthlyFiscal = report.get('activityPerformance').get('monthlyBudgetReports').at(this.currentTargetResult.get('budgetFiscalMonth'));
			monthlyFiscal.set('budgetResult', this.currentTargetResult.get('budgetResult'));

		} else {
			this.currentTargetResult.set('reportedResultDate', this.$el.find('#reportedResultDate').val());

			var oldResult = this.currentTargetResult.get('result');
			if (oldResult == null)
				oldResult = 0.0;
			var newResult = parseFloat(this.$el.find('#result').val());
			var adjustResult = newResult - oldResult;



			if (this.currentTargetResult.get('budgetFiscalMonth') == null) {
				// we have to calculate ourself here
				var resultDate = this.$el.find('#reportedResultDate').val();

				var month = parseInt(resultDate.substring(3, 5));
				this.currentTargetResult.set('budgetFiscalMonth', (month + 2) % 12);

			}

			var monthlyAct = report.get('monthlyReports').at(this.currentTargetResult.get('budgetFiscalMonth'));

			if (monthlyAct.get('activityResult') != null) {
				monthlyAct.set('activityResult', monthlyAct.get('activityResult') + adjustResult);
			} else {
				monthlyAct.set('activityResult', adjustResult);
			}



			this.currentTargetResult.set('result', newResult);


		}
		var remark = this.$el.find('#remark').val() == null ? "" : this.$el.find('#remark').val();
		var resultText = this.$el.find('#resultText').val() == null ? "" : this.$el.find('#resultText').val();
		var problemAndSuggestion = this.$el.find('#problemAndSuggestion').val() == null ? "" :this.$el.find('#problemAndSuggestion').val();
		
		this.currentTargetResult.set('remark', remark);
		this.currentTargetResult.set('resultText', resultText);
		this.currentTargetResult.set('problemAndSuggestion', problemAndSuggestion);


		this.currentTargetResult.save(null, {
			success : _.bind(function(model, response, options) {
				alert("บันทึกการรายงานผลเรียบร้อยแล้ว");
				this.currentTargetReport.set('latestResult', this.currentTargetResult);
				this.currentTargetResult.get('report').get('target').set('filterReport', this.currentTargetReport);
				this.currentTargetResult.set('id', parseInt(response));
				// and update the monthly accordingly

				this.render();
			}, this),
			error : function(model, xhr, options) {
				alert("Error Status Code: " + xhr.status + " " + xhr.statusText + "\n" + xhr.responseText);
			}
		});

		return false;
	},

	editResult : function(e) {
		var targetResultId = $(e.target).parents('tr').attr('data-id');
		this.currentTargetResult = ActivityTargetResult.findOrCreate(targetResultId);
		var json = {};
		json.unit = this.currentTargetReport.get("target").get("unit").toJSON();
		json.updateResult = true;
		json.result = this.currentTargetResult.toJSON();
		var html = this.resultInputTemplate(json);
		this.$el.find('.modal-header span').html("บันทึกผลการดำเนินงาน: " + this.currentTargetReport.get('target').get('activity').get('name'));

		this.$el.find('.modal-body').html(html);

		this.currentTargetResult.url = appUrl('/ActivityTargetResult/' + this.currentTargetResult.get('id'));

	},

	inputResult : function(e) {
		this.currentTargetResult = new ActivityTargetResult();
		this.currentTargetResult.set('report', this.currentTargetReport);
		this.currentTargetResult.set('resultBudgetType', false);
		var json = {};
		json.unit = this.currentTargetReport.get("target").get("unit").toJSON();
		var html = this.resultInputTemplate(json);
		this.$el.find('.modal-header span').html("บันทึกผลการดำเนินงาน: " + this.currentTargetReport.get('target').get('activity').get('name'));

		this.$el.find('.modal-body').html(html);

		this.$el.find('#reportedResultDate').datepicker({
			isBE : true,
			autoConversionField : false,
			beforeShow : function() {
				setTimeout(function() {
					$('.ui-datepicker').css('z-index', 9999);
				}, 0);
			}
		});


	},
	inputBudgetResult : function(e) {
		this.renderBudgetResult();
	},

	renderBudgetResult : function(fiscalMonth, targetResultId) {




		if (targetResultId == null) {

			this.currentTargetResult = new ActivityTargetResult();
			this.currentTargetResult.set('report', this.currentTargetReport);
			this.currentTargetResult.set('resultBudgetType', true);

		} else {
			this.currentTargetResult = ActivityTargetResult.findOrCreate(targetResultId);
		}

		var monthlyReports = this.currentTargetReport.get("activityPerformance").get("monthlyBudgetReports");

		var json = {};
		var fiscalMonths = [ {
			fiscalMonth : 0,
			name : 'ตุลาคม'
		}, {
			fiscalMonth : 1,
			name : 'พฤศจิกายน'
		}, {
			fiscalMonth : 2,
			name : 'ธันวาคม'
		},
			{
				fiscalMonth : 3,
				name : 'มกราคม'
			}, {
				fiscalMonth : 4,
				name : 'กุมภาพันธ์'
			}, {
				fiscalMonth : 5,
				name : 'มีนาคม'
			},
			{
				fiscalMonth : 6,
				name : 'เมษายน'
			}, {
				fiscalMonth : 7,
				name : 'พฤษภาคม'
			}, {
				fiscalMonth : 8,
				name : 'มิถุนายน'
			},
			{
				fiscalMonth : 9,
				name : 'กรกฎาคม'
			}, {
				fiscalMonth : 10,
				name : 'สิงหาคม'
			}, {
				fiscalMonth : 11,
				name : 'กันยายน'
			} ];

		// now remove the month that has already been reported!
		for (var i = 0; i < 11; i++) {
			var result = monthlyReports.at(i).get('budgetResult');
			if (result != null) {
				// then we can remove this month!
				fiscalMonths[i].disabled = true;
			}
		}
		// now 



		if (fiscalMonth == null) {
			json.month = fiscalMonths;
			var monthNumber = (moment().month() + 3) % 12;
			json.month[monthNumber].current = true;
			monthlyReports.forEach(function(report) {
				if (report.get('bugetResult') != null) {
					json.month[report.get('fiscalMonth')].hasOldValue = true;
					json.month[report.get('fiscalMonth')].oldValue = report.get('budgetResult');
				}
			});
		} else {

			json.month = [];
			json.month.push(fiscalMonths[fiscalMonth]);

			json.month[0].disabled = false;

			json.result = this.currentTargetReport.get('activityPerformance').get('monthlyBudgetReports').at(fiscalMonth).get('budgetResult');
			if (this.currentTargetResult != null) {
				json.remark = this.currentTargetResult.get('remark');
			} else {
				json.remark = '';
			}
			json.activityResultId = this.currentTargetReport.get('activityPerformance').get('monthlyBudgetReports').at(fiscalMonth).get('id');

		}


		this.$el.find('.modal-header span').html("บันทึกผลการใช้จ่ายงบประมาณ: " + this.currentTargetReport.get('target').get('activity').get('name'));


		var html = this.resultBudgetInputTemplate(json);
		this.$el.find('.modal-body').html(html);

	},
	renderWithReport : function(report) {
		this.currentTargetReport = report;
		this.render();
	},
	render : function() {
		if (this.currentTargetReport != null) {
			this.$el.find('.modal-header span').html("บันทึกผลการดำเนินงาน: " + this.currentTargetReport.get('target').get('activity').get('name'));


			var json = this.currentTargetReport.toJSON();

			var actName = this.currentTargetReport.get('target').get('activity').get('name');
			if (actName == "สำรวจรังวัด" ||
				actName == "รับคำขอ" ||
				actName == "อนุมัติ" ||
				actName == "โค่น" ||
				actName == "ปลูกแทน" ||
				actName == "จ่ายเงินสงเคราะห์" ||
				actName == "จ่ายวัสดุสงเคราะห์"
			) {
				json.disableInput = true;
			} else {
				json.disableInput = false;
			}


			var html = this.targetReportModalTemplate(json);
			this.$el.find('.modal-body').html(html);

			var monthly = this.currentTargetReport.get("monthlyReports");
			var budgetMontly = this.currentTargetReport.get("activityPerformance").get("monthlyBudgetReports");

			if (monthly.length != 12) {
				alert("กิจกรรมนี้ยังไม่ได้มีการทำแผน");
				return;
			}

			var q1Plan = new BigNumber(0.0);
			var q1Result = new BigNumber(0.0);
			var q1BudgetPlan = new BigNumber(0.0);
			var q1BudgetResult = new BigNumber(0.0);

			var q2Plan = new BigNumber(0.0);
			var q2Result = new BigNumber(0.0);
			var q2BudgetPlan = new BigNumber(0.0);
			var q2BudgetResult = new BigNumber(0.0);

			var q3Plan = new BigNumber(0.0);
			var q3Result = new BigNumber(0.0);
			var q3BudgetPlan = new BigNumber(0.0);
			var q3BudgetResult = new BigNumber(0.0);

			var q4Plan = new BigNumber(0.0);
			var q4Result = new BigNumber(0.0);
			var q4BudgetPlan = new BigNumber(0.0);
			var q4BudgetResult = new BigNumber(0.0);




			for (var i = 0; i < 12; i++) {
				var report = monthly.at(i);
				var budgetReport = budgetMontly.at(i);
				if (budgetReport == null) {
					alert("กิจกรรมนี้ยังไม่ได้ทำแผนใช้จ่าย");
					return;
				}
				var ap = report.get("activityPlan") == null ? new BigNumber(0.0) : new BigNumber(report.get("activityPlan"));
				var ar = report.get("activityResult") == null ? new BigNumber(0.0) : new BigNumber(report.get("activityResult"));
				var bp = budgetReport.get("budgetPlan") == null ? new BigNumber(0.0) : new BigNumber(budgetReport.get("budgetPlan"));
				var br = budgetReport.get("budgetResult") == null ? new BigNumber(0.0) : new BigNumber(budgetReport.get("budgetResult"));
				if (i < 3) {
					q1Plan = q1Plan.plus(ap);
					q1Result = q1Result.plus(ar);
					q1BudgetPlan = q1BudgetPlan.plus(bp);
					q1BudgetResult = q1BudgetResult.plus(br);
				} else if (i < 6) {
					q2Plan = q2Plan.plus(ap);
					q2Result = q2Result.plus(ar);
					q2BudgetPlan = q2BudgetPlan.plus(bp);
					q2BudgetResult = q2BudgetResult.plus(br);
				} else if (i < 9) {
					q3Plan = q3Plan.plus(ap);
					q3Result = q3Result.plus(ar);
					q3BudgetPlan = q3BudgetPlan.plus(bp);
					q3BudgetResult = q3BudgetResult.plus(br);
				} else {
					q4Plan = q4Plan.plus(ap);
					q4Result = q4Result.plus(ar);
					q4BudgetPlan = q4BudgetPlan.plus(bp);
					q4BudgetResult = q4BudgetResult.plus(br);
				}
			}

			this.$el.find("#Q1Plan").html("<strong>" + addCommas(q1Plan.toNumber()) + "</strong>");
			this.$el.find("#Q2Plan").html("<strong>" + addCommas(q2Plan.toNumber()) + "</strong>");
			this.$el.find("#Q3Plan").html("<strong>" + addCommas(q3Plan.toNumber()) + "</strong>");
			this.$el.find("#Q4Plan").html("<strong>" + addCommas(q4Plan.toNumber()) + "</strong>");

			this.$el.find("#Q1Result").html("<strong>" + addCommas(q1Result.toNumber()) + "</strong>");
			this.$el.find("#Q2Result").html("<strong>" + addCommas(q2Result.toNumber()) + "</strong>");
			this.$el.find("#Q3Result").html("<strong>" + addCommas(q3Result.toNumber()) + "</strong>");
			this.$el.find("#Q4Result").html("<strong>" + addCommas(q4Result.toNumber()) + "</strong>");

			this.$el.find("#totalResultTxt").val(addCommas(q1Result.plus(q2Result).plus(q3Result).plus(q4Result).toNumber()));

			this.$el.find("#Q1BudgetPlan").html("<strong>" + addCommas(q1BudgetPlan.toNumber()) + "</strong>");
			this.$el.find("#Q2BudgetPlan").html("<strong>" + addCommas(q2BudgetPlan.toNumber()) + "</strong>");
			this.$el.find("#Q3BudgetPlan").html("<strong>" + addCommas(q3BudgetPlan.toNumber()) + "</strong>");
			this.$el.find("#Q4BudgetPlan").html("<strong>" + addCommas(q4BudgetPlan.toNumber()) + "</strong>");

			this.$el.find("#Q1BudgetResult").html("<strong>" + addCommas(q1BudgetResult.toNumber()) + "</strong>");
			this.$el.find("#Q2BudgetResult").html("<strong>" + addCommas(q2BudgetResult.toNumber()) + "</strong>");
			this.$el.find("#Q3BudgetResult").html("<strong>" + addCommas(q3BudgetResult.toNumber()) + "</strong>");
			this.$el.find("#Q4BudgetResult").html("<strong>" + addCommas(q4BudgetResult.toNumber()) + "</strong>");

			this.$el.find("#totalBudgetResultTxt").val(addCommas(q1BudgetResult.plus(q2BudgetResult).plus(q3BudgetResult).plus(q4BudgetResult).toNumber()));

			this.$el.modal({
				show : true,
				backdrop : 'static',
				keyboard : false
			});
			return this;

		}
	}
});

var MainTblView = Backbone.View.extend({
	/**
     *  @memberOf MainTblView
     */
	initialize : function(options) {
		this.modalView = new ModalView({
			parentView : this
		});

	},

	el : "#mainTbl",
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	showLoadingTemplate : Handlebars.compile($("#showLoadingTemplate").html()),


	events : {
		"click a.showReport" : "showReportModal"
	},
	showLoading : function(e) {
		this.$el.html(this.showLoadingTemplate());
	},
	showReportModal : function(e) {
		var targetReportId = $(e.target).parents('tr').attr('data-id');

		// now we're going the report of this target

		var report = ActivityTargetReport.findOrCreate({
			id : targetReportId
		});

		report.fetch({
			success : _.bind(function() {
				// now setup the latest result!
				var latestResult = report.get('latestResult');

				if (latestResult != null && latestResult.get('resultBudgetType') == true) {
					report.get('activityPerformance').get('monthlyBudgetReports').at(latestResult.get('budgetFiscalMonth'))
						.set('targetResultId', latestResult.get('id'));
				}

				this.modalView.renderWithReport(report);
				report.get('target').set('filterReport', report);
			}, this)
		});

	},
	setCollection : function(collectton) {
		this.collection = collectton;
		if (this.collection != null) {
			this.collection.bind('reset', this.render, this);
		}
	},

	render : function() {
		var json = {};
		if (this.collection != null) {
			json = this.collection.toJSON();
			for (var i = 0; i < json.length; i++) {
				var act = json[i].filterActivities;
				for (var j = 0; j < act.length; j++) {
					var tgt = act[j].filterTargets;
					tgt[0].firstRow = true;
					tgt[0].targetsLength = tgt.length;
					for (var k = 0; k < tgt.length; k++) {
						var rpt = tgt[k].filterReport;
						if (rpt == null) {
							//we just return here
							return this;
						} else if (rpt.latestResult != null) {
							rpt.lastSaveTxt = moment.utc(rpt.latestResult.timestamp).fromNow();
							if (rpt.latestResult.person != null) {
								rpt.lastSaveTxt += " / " + rpt.latestResult.person.firstName + " " + rpt.latestResult.person.lastName;
							}
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