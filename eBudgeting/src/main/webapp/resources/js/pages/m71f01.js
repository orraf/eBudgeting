var ModalView = Backbone.View.extend({
	
	/**
     *  @memberOf ModalView
     */
	initialize : function() {
		
	},

	el : "#modal",

	currentBudgetTypeSelection: [],
	currentFormulaStrategySelection: null,
	
	modalTemplate : Handlebars.compile($('#modalTemplate').html()),
	inputAllDivTemplate : Handlebars.compile($('#inputAllDivTemplate').html()),
	inputAllocationRecordTemplate : Handlebars.compile($('#inputAllocationRecordTemplate').html()), 
	allocationRecordCellTemplate: Handlebars.compile($('#allocationRecordCellTemplate').html()),
	allocationRecordAssetCellTemplate :Handlebars.compile($('#allocationRecordAssetCellTemplate').html()),
	inputFormTemplate : Handlebars.compile($('#inputFormTemplate').html()), 
	assetAllocationDetailTemplate : Handlebars.compile($('#assetAllocationDetailTemplate').html()),
	
	organizationProposalTbodyTemplate: Handlebars.compile($('#organizationProposalTbodyTemplate').html()),
	organizationSearchTbodyTemplate: Handlebars.compile($('#organizationSearchTbodyTemplate').html()), 
	
	inputOwnerTemplate : Handlebars.compile($('#inputOwnerTemplate').html()), 
	organizationOwnerSearchTbodyTemplate: Handlebars.compile($('#organizationOwnerSearchTbodyTemplate').html()), 
	organizationOwnerTbodyTemplate: Handlebars.compile($('#organizationOwnerTbodyTemplate').html()),
	
	assetAllocationTbodyTemplate :Handlebars.compile($('#assetAllocationTbodyTemplate').html()),
	
	events : {
		"click .removeProposal" : "removeProposal",
		"click .saveProposal" : "saveProposal",
		"click .saveOwner" : "saveOwner",
		"click .editProposal" : "editProposal",
		"click #cancelBtn" : "cancelModal",
		"click .close" : "cancelModal",
		"click .backToProposal" : "backToProposal",
		"click .copytoNextYear" : "copyToNextYear",
		"click .addAllocationRecord" : "addAllocationRecord",
		
		"click .addAssetAllocationRecord" : "addAssetAllocationRecord",
		"click .editAssetAllocationRecord" : "editAssetAllocationRecord",
		"click .assetAllocationDetail" : "assetAllocationDetail",
		"click .deleteAssetAllocation" : "deleteAssetAllocation",
		"change .assetAllocationNumber" : "changeAssetAllocationNumber",
		"click #saveAssetAllocationBtn" : "saveAssetAllocation",
		"change .assetAllocationOnwerSlt" : "changeAssetAllocationOnwerSlt",
		"click #backToProposalFromAssetBtn" : "backToProposalFromAsset",
		"click .removeOrganizationAssetProposal" : "removeOrganizationAssetProposal",
		
		"click .editAllocationRecord" : "editAllocationRecord",
		"click #organizationSearchBtn" : "organizationSearch",
		"click #organizationOwnerSearchBtn" : "organizationOwnerSearch",
		"click .removeOrganizationProposal" : "removeOrganizationProposal",
		"click .removeOrganizationOwner" : "removeOrganizationOwner",
		"click .addOrganization" : "addOrganizationProposal",
		"click .addOrganizationOwner" : "addOrganizationOwner",
		"change .proposalAllocated" : "changeProposalAllocated",
		"click #addOwnerBtn" : "addOwner"
			

	},
	
	assetAllocationDetail :function(e) {
		this.saveDetailHtml = this.$el.find('.modal-body').html();
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		this.currentOrganization = organization;
		
		// now hold children org
		this.childrenOrganization = new OrganizationCollection();
		this.childrenOrganization.fetch({
			url: appUrl('/Organization/parentId/'+organizationId+'/findByName'),
			type: 'POST',
			data : {
				query: ""
			}
		});
		
		for(var i=0; i<this.proposals.length; i++) {
			if(this.proposals.at(i).get('owner') == organization) {
				this.currentProposal = this.proposals.at(i);
				break;
			}
		}
		
		var json = this.currentProposal.toJSON();
		var html = this.assetAllocationDetailTemplate(json);
		
		this.$el.find('.modal-body').html(html);
		
		this.assetSelectionView = new AssetSelectionView({parentView: this});
		this.assetSelectionView.setElement('#assetSlt');
		this.assetSelectionView.render();
		
		// and put the entry in the table body 
		this.assetAllocations = new AssetAllocationCollection();
		this.assetAllocations.fetch({
			url: appUrl('/AssetAllocation/findByParentOwner/'+  this.currentOrganization.get('id') +
					'/forObjective/'+ this.currentProposal.get('forObjective').get('id') +
					'/budgetType/' + this.currentBudgetType.get('id')),
			success: _.bind(function(){
				
				for(var i=0; i<this.assetAllocations.length; i++) {
					var record = this.assetAllocations.at(i);
					var json = record.toJSON();
					json.organizations = this.childrenOrganization.toJSON();
					if(record.get('owner') != null) {
						_.each(json.organizations,function(org) {
							if(org.id == record.get('owner').get('id')) {
								org.selected = true;
							}
						});
					}
					var html = this.assetAllocationTbodyTemplate(json);
					this.$el.find('#assetTbl tbody').append(html);		
				}
				
			},this)
		});
		
	},
	saveAssetAllocation : function(e) {
		// first check all owner is not zero!
		var allSelected = true;
		var allSlt = this.$el.find('.assetAllocationOnwerSlt');
		
		for(var i=0; i< allSlt.length; i++) {
			if($(allSlt[i]).val() == 0) {
				allSelected = false;
			}
		}
		
		if(allSelected) {
			Backbone.sync('update', this.assetAllocations, {
				url: appUrl('/AssetAllocation/saveCollection'),
			    success: _.bind(function(model, response, options){                                                                                                                                                                                                                                                                                                                                          
			                       
			    	// now sync up this Objective
			    	mainCtrView.updateAllocationForObjective(this.objective);
			    	mainCtrView.updateAllocationForObjective(this.objective.get('parent'));
			    	
			    	alert("บันทึกข้อมูลเรียบร้อย");
			      
			    },this),
			    error: function(model, xhr, options) {
			    	alert("Error Status Code: " + xhr.status + " " + xhr.statusText + "\n" + xhr.responseText);
			    }
			 });
		} else {
			alert('กรุณาเลือกหน่วยงาน');
		}
	},
	backToProposalFromAsset: function(e) {
		this.renderAllocationRecordInput();
	},
	changeAssetAllocationOnwerSlt: function(e) {
		var assetAllocationId = $(e.target).parents('tr').attr('data-id');
		var assetAllocation = AssetAllocation.findOrCreate(assetAllocationId);
		
		var ownerId = $(e.target).val();
		if(ownerId > 0) {
			var owner = Organization.findOrCreate(ownerId);
			assetAllocation.set('owner', owner);
		}
		
	},
	changeAssetAllocationNumber : function(e) {
		var type = $(e.target).attr('data-type');
		var value = $(e.target).val();
		var assetAllocationId = $(e.target).parents('tr').attr('data-id');
		var assetAllocation = AssetAllocation.findOrCreate(assetAllocationId);
		
		if(!isNaN(parseInt(value))) {
			assetAllocation.set(type,value);
													 
			this.$el.find('#totalAssetAllocation-' + assetAllocationId)
				.html(addCommas(assetAllocation.get('unitBudget') * assetAllocation.get('quantity')) + ' บาท');
		}
		
		
	},
	deleteAssetAllocation: function(e) {
		var assetAllocationId = $(e.target).parents('tr').attr('data-id');
		var assetAllocation = AssetAllocation.findOrCreate(assetAllocationId);
	
		var y = confirm("คุณต้องการลบการจัดสรรรายการ " + assetAllocation.get('assetBudget').get('name'));
		if(y==true) {
			
			assetAllocation.destroy({
				url: appUrl('/AssetAllocation/'+assetAllocationId),
				success: _.bind(function() {
					$(e.target).parents('tr').remove();
					this.assetAllocations.remove(assetAllocation);
				},this),
				error: _.bind(function(model, xhr, options) {
					alert("ไม่สามารถลบรายการได้ \n Error: " + xhr.responseText);
				},this)
			});
		}
		
	},
	addAssetBudgetAllocation: function(assetBudget) {
		var record = new AssetAllocation();
		record.set('assetBudget', assetBudget);
		record.set('fiscalYear', fiscalYear);
		record.set('forObjective', this.currentProposal.get('forObjective'));
		record.set('budgetType', this.currentBudgetType);
		record.set('parentOwner', this.currentOrganization);
		record.set('proposal', this.currentProposal);
		
		
		record.save(null, {
			success: _.bind(function(model, response, options) {
				this.assetAllocations.push(record);
				
				var json = record.toJSON();
				json.organizations = this.childrenOrganization.toJSON();
				var html = this.assetAllocationTbodyTemplate(json);
				this.$el.find('#assetTbl tbody').append(html);	
				
				this.currentProposal.set('id', response.proposal.id);
				this.currentProposal.set('amountAllocated',response.proposal.amountAllocated);
				
				
				
			},this)
		});
		
		
		
	},

	addOwner: function(e) {
		this.renderOwnerInput();
	},
	
	changeProposalAllocated: function(e) {
		// validate this one first
		if( isNaN( +$(e.target).val() ) ) {
			$(e.target).parent('div').addClass('control-group error');
			alert("กรุณาใส่ข้อมูลเป็นตัวเลข");
		} else {
			$(e.target).parent('div').removeClass('control-group error'); 
			var i = $(e.target).parents('tr').prevAll('tr').length;
			
			this.proposals.at(i).set('amountAllocated', $(e.target).val());
			this.updateSumProposal();	
		}
		
	},
	
	addOrganizationOwner: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		
		this.owners.push(organization);
		
		var json=this.owners.toJSON();
		
		var html=this.organizationOwnerTbodyTemplate(json);
		$('#organizationOwnerTbl').find('tbody').html(html);
		
		
		organization.set('_inProposalList', true);
		//refresh the other list
		var html=this.organizationOwnerSearchTbodyTemplate(this.organizationSearchList.toJSON());
		$('#organizationOwnerSearchTbl').find('tbody').html(html);
				
	},
	
	addOrganizationProposal: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		var newProposal = new BudgetProposal();
		newProposal.set('owner', organization);
		newProposal.set('budgetType', this.currentBudgetType);
		newProposal.set('amountAllocated', 0);
		newProposal.set('forObjective', this.objective);
		
		this.proposals.push(newProposal);
		
		var json = this.proposals.toJSON();
		if(this.assetAllocation == true) {
			json.assetAllocation = true;
		}
		var html=this.organizationProposalTbodyTemplate(json);
		$('#organizationProposalTbl').find('tbody').html(html);
		
		this.updateSumProposal();
		
		organization.set('_inProposalList', true);
		//refresh the other list
		var html=this.organizationSearchTbodyTemplate(this.organizationSearchList.toJSON());
		$('#organizationSearchTbl').find('tbody').html(html);
		
	},
	removeProposalOrganization: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		
		// now remove this one 
		for(var i=0; i<this.proposals.length; i++) {
			if(this.proposals.at(i).get('owner') == organization) {
				
				var y = confirm("คุณต้องการลบการจัดสรรงบประมาณของหน่วยงาน " + organization.get('name'));
				if(y==true) {
					
					var p = this.proposals.at(i);
					
					// we remove this on from our list
					this.proposals.remove(p);
					
					p.get('owner').set('_inProposalList', false);
					
					// now if p is already in the database
					if(p.get('id') != null) {
						p.destroy({
							success: _.bind(function(model, response, options) {
								var json = this.proposals.toJSON();
								json.assetAllocation = this.assetAllocation;
								var html=this.organizationProposalTbodyTemplate(json);
								$('#organizationProposalTbl').find('tbody').html(html);
								
								this.updateSumProposal();
								
								//refresh the other list
								var html=this.organizationSearchTbodyTemplate(this.organizationSearchList.toJSON());
								$('#organizationSearchTbl').find('tbody').html(html);
							},this),
							error: function(model, xhr, options) {
								alert("Error Status Code: " + xhr.status + " " + xhr.statusText + "\n" + xhr.responseText);
							}	
 						});
					} else {
						var json = this.proposals.toJSON();
						json.assetAllocation = this.assetAllocation;
						var html=this.organizationProposalTbodyTemplate(json);
						$('#organizationProposalTbl').find('tbody').html(html);
						
						this.updateSumProposal();
						
						//refresh the other list
						var html=this.organizationSearchTbodyTemplate(this.organizationSearchList.toJSON());
						$('#organizationSearchTbl').find('tbody').html(html);
					}
				}
			}
		}
		
	},
	removeOrganizationAssetProposal: function(e) {
		
		this.assetAllocation = true;
		this.removeProposalOrganization(e);
	},
	removeOrganizationProposal: function(e) {
		this.assetAllocation = false;
		this.removeProposalOrganization(e);
	},
	removeOrganizationOwner: function(e) {
		var organizationId = $(e.target).parents('tr').attr('data-id');
		var organization = Organization.findOrCreate(organizationId);
		
		
		// now remove this one 
		for(var i=0; i<this.owners.length; i++) {
			if(this.owners.at(i) == organization) {
				
				var y = confirm("คุณต้องการลบหน่วยงาน " + organization.get('name'));
				if(y==true) {
					
					var o = this.owners.at(i);
					
					// we remove this on from our list
					this.owners.remove(o);
					
					o.set('_inProposalList', false);
			
					var html=this.organizationOwnerTbodyTemplate(this.owners.toJSON());
					$('#organizationOwnerTbl').find('tbody').html(html);
					
					//refresh the other list
					var html=this.organizationOwnerSearchTbodyTemplate(this.organizationSearchList.toJSON());
					$('#organizationOwnerSearchTbl').find('tbody').html(html);
				}
			}
		}
		
		
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
	resetProposalsInList: function() {
		if(this.proposals != null) {
			this.proposals.pluck('owner').forEach(function(owner) {
				owner.set('_inProposalList', false);
			});
		}
		
		if(this.owners != null) {
			this.owners.forEach(function(owner) {
				owner.set('_inProposalList', false);
			});
		}
	
		if(this.organizationSearchList != null) {
			this.organizationSearchList.reset();
		}
	},
	backToProposal: function(e) {
		this.resetProposalsInList();
		if(this.currentAllocationRecord.get('id') == null) {
			// we must remove this one from this.objective;
			this.objective.get('allocationRecords').remove(this.currentAllocationRecord);
			this.currentAllocationRecord = null;
		}
		this.render();
	},
	cancelModal : function(e) {
		this.$el.modal('hide');
		mainCtrView.renderMainTbl();
	},
	copyToNextYear : function(e) {
		var valueToCopy = $('#totalInputTxt').val();
		valueToCopy = valueToCopy.replace(/,/g, '');
		this.$el.find('#amountRequestNext1Year').val(valueToCopy);
		this.$el.find('#amountRequestNext2Year').val(valueToCopy);
		this.$el.find('#amountRequestNext3Year').val(valueToCopy);
	},
	updateBudgetTypeSelectionLevelWithModel: function(level, model) {
		//set the previos level to this model
		this.currentBudgetTypeSelection[level-2]=model;
		// and remove all the rest forward
		for(var i=level-1; i<this.budgetTypeSelectionArray.length; i++) {
			this.currentBudgetTypeSelection[i] = null;
		}
		
		level = level-1;
		var btView = this.budgetTypeSelectionArray[level];
		if(btView == null) {
			$('#input-form').html(this.inputFormTemplate( new AllocationRecord() ));
			this.currentBudgetType = model;
			return;
		}
		
		// reset input area
		if(level < this.budgetTypeSelectionArray.length) {
			$('#input-form').empty();
		} 
		// render the next selection
		btView.setRootModel(model);
		btView.render();
		
		// and for the rest set to null
		for(var i=level+1; i<this.budgetTypeSelectionArray.length; i++) {
			this.budgetTypeSelectionArray[i].setRootModel(null);
			this.budgetTypeSelectionArray[i].render();
		}
		
		// reset the current budget type
		this.currentBudgetType = null;
		
	},
	
	saveOwner: function(e) {
		$.ajax({
			type: 'POST',
			data: {
				ownerIds: this.owners.pluck('id')
			},
			url: appUrl('/Objective/' + this.objective.get('id') + '/saveOwners'),
			success: _.bind(function() {
				alert("บันทึกข้อมูลเรียบร้อย");
				this.owners.forEach(function(owner) {
					owner.set("_inProposalList", false);
				});
				this.render();
			}, this)
		});
	},

	
 	saveProposal: function(e) {
 		var sum=0;
		// now put the sum up
		_.forEach(this.$el.find("input.proposalAllocated"), function(el) {
			sum += parseInt($(el).val());
		});
		
		if(sum != parseInt($('#totalInputTxt').val())) {
			alert("กรุณาตรวจสอบการจัดสรร งบประมาณที่จัดสรรให้หน่วยงานรวมแล้วไม่เท่ากับงบประมาณที่จัดสรรไว้");
			return;
		}
 		
 		this.$el.find('button.saveProposal').html('<icon class="icon-refresh icon-spin"></icon> กำลังบันทึกข้อมูล...');
		
		var record = this.currentAllocationRecord;
		
		record.set('amountAllocated', parseInt($('#totalInputTxt').val()));
		record.set('index', 0);
		
		// put proposal into somewhat better thing
		var proposalJson = [];
		for(var i=0; i<this.proposals.length; i++) {
			var p = this.proposals.at(i);
			var pJson = {};
			pJson.amountAllocated=p.get('amountAllocated');
			pJson.organizationId=p.get('owner').get('id');
			proposalJson.push(pJson);
		}
		
		var allocationJson = record.toJSON();
		allocationJson.forObjectiveId = allocationJson.forObjective.id;
		allocationJson.budgetTypeId = allocationJson.budgetType.id;
		allocationJson.forObjective = null;
		allocationJson.budgetType = null;
		
		
		//now ready for save
		$.ajax({
			type: 'POST',
			data: {
				allocationRecordJsonString: JSON.stringify(allocationJson),
				proposalsJsonString: JSON.stringify(proposalJson)
			},
			url: appUrl('/AllocationRecord/SaveWithProposals'),
			success: _.bind(function(response) {
				record.set('id', response.id);
				this.objective.get("allocationRecords").push(record);
				alert("บันทึกข้อมูลเรียบร้อย");
				this.resetProposalsInList();
				this.render();
			},this)
		});
		
	},
	editProposal : function(e) {
		var obpId = $(e.target).parents('li').attr('data-id');
		var obp = ObjectiveBudgetProposal.findOrCreate(obpId);

		// we'll begin by render the budgetTypeSelectionView
		this.renderEditProposal(obp);

	},

	removeProposal : function(e) {
		$(e.target).parents('li').css('text-decoration', 'line-through');
		var obpId = $(e.target).parents('li').attr('data-id');
		var obp = ObjectiveBudgetProposal.findOrCreate(obpId);
		
		if (obp != null) {
			// we can start deleting it now.. 

			var r = confirm("คุณต้องการนำรายการนี้ออก?");
			if (r == true) {
				$.ajax({
					type : 'DELETE',
					url : appUrl('/ObjectiveBudgetProposal/' + obpId),
					success : _.bind(function() {
							this.objective.get('filterObjectiveBudgetProposals').remove(obp);
							this.render();
						}, this)
					});

			} else {
				$(e.target).parents('li').css('text-decoration', '');
			}
			return false;

		}
	},
	
	renderEditProposal: function(obp) {
		this.currentObjectiveBudgetProposal = obp;
		
		var json=obp.toJSON();
		json.next1Year = fiscalYear+1;
		json.next2Year = fiscalYear+2;
		json.next3Year = fiscalYear+3;
		
		this.$el.find('.modal-body').html(this.inputAllDivTemplate(json));		
		
	},
	editAssetAllocationRecord:function(e) {
		this.assetAllocation = true;
		this.renderEditAllocationRecord(e);
	},
	editAllocationRecord: function(e) {
		this.assetAllocation = false;
		this.renderEditAllocationRecord(e);
	},
	
	renderEditAllocationRecord: function(e) {
		this.currentAllocationRecord = AllocationRecord.findOrCreate($(e.target).parents('div[data-id]').attr('data-id'));
		
		var budgetTypeId = $(e.target).parents('td').attr('data-budgetTypeId');
		this.currentBudgetType = BudgetType.findOrCreate(budgetTypeId);
		
		this.currentBudgetProposalList = new BudgetProposalCollection();
		this.renderAllocationRecordInput();
		this.organizationSearch();	
		
		// update the sum
	},
	
	addAssetAllocationRecord: function(e) {
		this.assetAllocation = true;
		this.renderAddAllocationRecord(e);
	},
	

	
	addAllocationRecord: function(e) {
		this.assetAllocation = false;
		this.renderAddAllocationRecord(e);
	},
	renderAddAllocationRecord: function(e) {
		this.currentAllocationRecord = new AllocationRecord();
		
		var budgetTypeId = $(e.target).parents('td').attr('data-budgetTypeId');
		this.currentBudgetType = BudgetType.findOrCreate(budgetTypeId);
		this.currentAllocationRecord.set('budgetType', this.currentBudgetType);
		this.currentAllocationRecord.set('forObjective', this.objective);
		this.objective.get('allocationRecords').push(this.currentAllocationRecord);
		
		
		this.renderAllocationRecordInput();
		this.organizationSearch();
	},
	
	renderOwnerInput: function() {
		this.$el.find('.modal-body').html(this.inputOwnerTemplate());
		// now get the owner of this objective
		this.owners = new OrganizationCollection();
		this.owners.url = appUrl('/Organization/ownObjective/' + this.objective.get('id'));
		this.owners.fetch({
			success: _.bind(function() {
				var html=this.organizationOwnerTbodyTemplate(this.owners.toJSON());
				$('#organizationOwnerTbl').find('tbody').html(html);
				
				this.owners.forEach(function(owner) {
					owner.set('_inProposalList', true);
				});
				
				
				
			}, this)
		});
		
	},
	
	renderAllocationRecordInput: function() {
		var json = this.currentAllocationRecord.toJSON();
		if(this.assetAllocation == true) {
			json.assetAllocation = true;
		}
		this.$el.find('.modal-body').html(this.inputAllocationRecordTemplate(json));
		
		// now get the budgetProposal of this objective
		this.proposals = new BudgetProposalCollection();
		this.proposals.url = appUrl('/BudgetProposal/find/'+fiscalYear+
				'/' + this.objective.get('id') + 
				'/' + this.currentAllocationRecord.get('budgetType').get('id'));
		
		this.proposals.fetch({
			success: _.bind(function(model, response, options) {
				var json = this.proposals.toJSON();
				if(this.assetAllocation == true) {
					json.assetAllocation = true;
				}
				var html=this.organizationProposalTbodyTemplate(json);
				
				$('#organizationProposalTbl').find('tbody').html(html);
				
				this.updateSumProposal();
				
				this.proposals.pluck('owner').forEach(function(owner) {
					owner.set('_inProposalList', true);
				});
				
			}, this)
		});
		
		
	},
	updateSumProposal: function() {
		var sum=0;
		// now put the sum up
		this.proposals.forEach(function(proposal) {
			var amountAllocated = parseInt(proposal.get('amountAllocated'));
			if(!isNaN(amountAllocated)) {
				sum += proposal.get('amountAllocated');
			}
		});
		
		$('#sumTotalAllocated').html(addCommas(sum));
		if($('#totalInputTxt')  != null) {
			$('#totalInputTxt').val(sum);
		}
	},
	
	render : function() {
		if (this.objective != null) {
			var json = this.objective.toJSON();
			json.readOnly = readOnly;
			var html = this.modalTemplate(json);
			this.$el.find('.modal-header span').html(this.objective.get('name'));
			this.$el.find('.modal-body').html(html);
			
			// now we'll just have to mapped the corrected budgetType
			var records = this.objective.get('allocationRecords');
			var sum1=0, sum2=0, sum3=0, sum4=0;
			
			records.forEach(_.bind(function(record) {
				var budgetTypeId=record.get('budgetType').get('id');
				var html = "";
				if(record.get('budgetType').get('name').indexOf('ลงทุน') > 0 ) {
					html = this.allocationRecordAssetCellTemplate(record.toJSON());
				} else {
					html = this.allocationRecordCellTemplate(record.toJSON());
				}
				$('td[data-budgettypeid='+budgetTypeId+']').html(html);
				
				if(budgetTypeId==9 || budgetTypeId==8) {
					sum1 += parseInt(record.get('amountAllocated'));
				} else if(budgetTypeId==10 || budgetTypeId==7) {
					sum2 += parseInt(record.get('amountAllocated'));
				} else if(budgetTypeId==13 || budgetTypeId==11) {
					sum3 += parseInt(record.get('amountAllocated'));
				} else if(budgetTypeId==14 || budgetTypeId==12) {
					sum4 += parseInt(record.get('amountAllocated'));
				} 
			},this));
			
			// we can render sum
			$('td#sum1').html(addCommas(sum1) + ' บาท');
			$('td#sum2').html(addCommas(sum2) + ' บาท');
			$('td#sum3').html(addCommas(sum3) + ' บาท');
			$('td#sum4').html(addCommas(sum4) + ' บาท');
			
			
		}

		
		this.$el.modal({
			show : true,
			backdrop : 'static',
			keyboard : false
		});
		return this;
	},

	renderWith : function(currentObjective) {
		this.objective = currentObjective;
		this.render();
	}
});

var MainSelectionView = Backbone.View.extend({
	mainSelectionTemplate : Handlebars.compile($("#mainSelectionTemplate").html()),
	selectionTemplate : Handlebars.compile($("#selectionTemplate").html()),
	type102DisabledSelectionTemplate : Handlebars.compile($("#type102DisabledSelection").html()),
	type103DisabledSelectionTemplate : Handlebars.compile($("#type103DisabledSelection").html()),
	
	/**
     *  @memberOf MainSelectionView
     */
	initialize: function() {
		
		this.type102Collection = new ObjectiveCollection();
		this.type103Collection = new ObjectiveCollection();
		
		_.bindAll(this, 'renderInitialWith');
		_.bindAll(this, 'renderType102');
		_.bindAll(this, 'renderType103');
		this.type102Collection.bind('reset', this.renderType102);
		this.type103Collection.bind('reset', this.renderType103);
	},
	events: {
		"change select#type101Slt" : "type101SltChange",
		"change select#type102Slt" : "type102SltChange",
		"change select#type103Slt" : "type103SltChange"
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
			this.type103Collection.fetch({
				url: appUrl('/Objective/' + type102Id + '/children'),
				success: _.bind(function() {
					this.type103Collection.trigger('reset');
				}, this)
			});
		}
		
		this.$el.find('#type103Div').empty();
		this.$el.find('#type103Div').html(this.type103DisabledSelectionTemplate());
		
		mainCtrView.emptyTbl();
	},
	
	type103SltChange : function(e) {
		var type103Id = $(e.target).val();
		if(type103Id != 0) {
			var obj = Objective.findOrCreate(type103Id);
			obj.url = appUrl("/Objective/loadObjectiveAllocationRecord/" + obj.get('id'));
			obj.fetch({
				success: function(model, xhr, option) {
					mainCtrView.renderMainTblWithParent(obj);
				}
			});
		} else {
			mainCtrView.emptyTbl();
		}
	
	},
	
	renderType102: function(e) {
		var json = this.type102Collection.toJSON();
		json.type =  {};
		json.type.name = "ยุทธศาสตร์";
		json.type.id = "102";
		var html = this.selectionTemplate(json);
		
		// now render 
		this.$el.find('#type102Div').empty();
		this.$el.find('#type102Div').html(html);
		
		this.$el.find('#type103Div').empty();
		this.$el.find('#type103Div').html(this.type103DisabledSelectionTemplate());
		
		
	},
	
	renderType103: function(e) {
		var json = this.type103Collection.toJSON();
		json.type =  {};
		json.type.name = "ผลผลิต/โครงการ";
		json.type.id = "103";
		var html = this.selectionTemplate(json);
		
		// now render 
		this.$el.find('#type103Div').empty();
		this.$el.find('#type103Div').html(html);
		
		
	},
	
	render: function() {
		// render this view
		if(this.rootChildrenObjectiveCollection != null) {
			var json = this.rootChildrenObjectiveCollection.toJSON();
			
			var html = this.mainSelectionTemplate(json);
			this.$el.html(html);
		}
	}, 
	renderInitialWith: function(objective) {
		
		this.rootObjective = objective;
		
		// now get this rootObjective Children
		this.rootChildrenObjectiveCollection = new ObjectiveCollection();
		
		this.rootChildrenObjectiveCollection.fetch({
			url: appUrl('/Objective/' + this.rootObjective.get('id') + '/children'),
			success : _.bind(function() {
				
				this.render();
			},this)
		});
		
	}
	
});

var AssetSelectionView = Backbone.View.extend({
	/**
	 * @memberOf AssetSelectionView
	 */
	initialize : function(options) {
		if(options != null) {
			this.parentView = options.parentView;
		}
		
		this.assetTypes = new AssetTypeCollection();
		this.assetKinds = new AssetKindCollection();
		this.assetBudgets = new AssetBudgetCollection();

		_.bindAll(this, 'renderType');
		_.bindAll(this, 'renderKind');
		_.bindAll(this, 'renderAssetBudget');
		
		this.assetTypes.bind('reset', this.renderType);
		this.assetKinds.bind('reset', this.renderKind);
		this.assetBudgets.bind('reset', this.renderAssetBudget);

		
	},
	selectionTemplate : Handlebars.compile($("#selectionTemplate").html()),
	assetGroupSelectionTemplate : Handlebars.compile($("#assetGroupSelectionTemplate").html()),
	assetKindDisabledSelectionTemplate : Handlebars.compile($("#assetKindDisabledSelectionTemplate").html()), 
	assetBudgetDisabledSelectionTemplate : Handlebars.compile($("#assetBudgetDisabledSelectionTemplate").html()), 
	
	events: {
		"change #assetGroupSlt" : "assetGroupSltChange",
		"change #typeAssetTypeSlt" : "assetTypeSltChange",
		"change #typeAssetKindSlt" : "assetKindSltChange",
		"change #typeAssetBudgetSlt" : "assetBudgetSltChange",
		"click #addAssetBudget" : "addAssetBudget"
	},
	
	assetGroupSltChange : function(e) {
		var groupId = $(e.target).val();
		if(groupId != 0) {
			this.assetTypes.fetch({
				url: appUrl('/AssetType/byGroupId/' + groupId),
				success: _.bind(function() {
					this.assetTypes.trigger('reset');
				}, this)
			});
		}
	},
	assetTypeSltChange : function(e) {
		var typeId = $(e.target).val();
		if(typeId != 0) {
			this.assetKinds.fetch({
				url: appUrl('/AssetKind/byTypeId/' + typeId),
				success: _.bind(function() {
					this.assetKinds.trigger('reset');
				}, this)
			});
		}
	},
	assetKindSltChange: function(e) {
		var kindId = $(e.target).val();
		if(kindId != 0) {
			this.assetBudgets.fetch({
				url: appUrl('/AssetBudget/byKindId/' + kindId),
				success: _.bind(function() {
					this.assetBudgets.trigger('reset');
				}, this)
			});
		}
		
	},
	addAssetBudget: function(e) {
		this.currentAssetBudgetId = this.$el.find("#typeAssetBudgetSlt").val();
		if(this.currentAssetBudgetId != 0) {
			this.currentAssetBudget = AssetBudget.findOrCreate(this.currentAssetBudgetId);
			
			this.parentView.addAssetBudgetAllocation(this.currentAssetBudget);
			
		} else {
			alert("กรุณาเลือกรายการงบลงทุน");
		}
	},
	assetBudgetSltChange: function(e) {

	},
	renderType: function(e) {
		var json = this.assetTypes.toJSON();
		json.type =  {};
		json.type.name ="ประเภท";
		json.type.id = "AssetType";
		var html = this.selectionTemplate(json);
		
		// now render 
		this.$el.find('#assetTypeSltDiv').empty();
		this.$el.find('#assetTypeSltDiv').html(html);
		
		this.$el.find('#assetKindSltDiv').empty();
		this.$el.find('#assetKindSltDiv').html(this.assetKindDisabledSelectionTemplate());

		this.$el.find('#assetBudgetSltDiv').empty();
		this.$el.find('#assetBudgetSltDiv').html(this.assetBudgetDisabledSelectionTemplate());
		
		
	},
	renderKind: function(e) {
		var json = this.assetKinds.toJSON();
		json.type =  {};
		json.type.name = "ชนิด";
		json.type.id = "AssetKind";
		var html = this.selectionTemplate(json);
		
		// now render 
		this.$el.find('#assetKindSltDiv').empty();
		this.$el.find('#assetKindSltDiv').html(html);
	
		this.$el.find('#assetBudgetSltDiv').empty();
		this.$el.find('#assetBudgetSltDiv').html(this.assetBudgetDisabledSelectionTemplate());
	
	},
	renderAssetBudget: function(e) {
		var json = this.assetBudgets.toJSON();
		json.type =  {};
		json.type.name = "รายการ";
		json.type.id = "AssetBudget";
		var html = this.selectionTemplate(json);
		
		// now render 
		this.$el.find('#assetBudgetSltDiv').empty();
		this.$el.find('#assetBudgetSltDiv').html(html);
		
		this.$el.find('#typeAssetBudgetSlt').after('<button style="margin-left: 15px;" id="addAssetBudget" class="btn btn-primary">Add</button>');
		
	},

	render: function() {
		if(this.$el != null) {
			
			this.assetGroups = new AssetGroupCollection();
			this.assetGroups.url = appUrl('/AssetGroup/');
			this.assetGroups.fetch({
				success: _.bind(function() {
					var html = this.assetGroupSelectionTemplate(this.assetGroups.toJSON());
					this.$el.html(html);
				},this)
			});
			
		}
		
		return this;
	}
});


var MainCtrView = Backbone.View.extend({
	/**
     *  @memberOf MainCtrView
     */
	initialize : function() {
		//this.collection.bind('reset', this.render, this);
		_.bindAll(this, 'detailModal');
		
		// puting loading sign
		this.$el.html(this.loadingTpl());
	},

	el : "#mainCtr",
	loadingTpl : Handlebars.compile($("#loadingTemplate").html()),
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	mainTblTpl : Handlebars.compile($("#mainTblTemplate").html()),
	modalView : new ModalView(),

	 
	events : {
		"click input[type=checkbox].bullet" : "toggle",
		"click .detail" : "detailModal"
	},

	detailModalWithObjectiveId : function(id) {
		var currentObjective = Objective.findOrCreate({id: id});
	
		currentObjective.url = appUrl("/Objective/loadObjectiveAllocationRecord/" + currentObjective.get('id'));
		currentObjective.fetch({
			success: _.bind(function(model, xhr, option) {
				this.modalView.renderWith(currentObjective);
			},this)
		});
		
	},
	
	detailModal : function(e) {
		var currentObjectiveId = $(e.target).parents('tr').attr('data-id');
		var currentObjective = Objective.findOrCreate(currentObjectiveId);
		
		currentObjective.url = appUrl("/Objective/loadObjectiveAllocationRecord/" + currentObjective.get('id'));
		currentObjective.fetch({
			success: _.bind(function(model, xhr, option) {
				this.modalView.renderWith(currentObjective);
			},this)
		});
	},
	
	updateAllocationForObjective: function(objective) {
		
		objective.fetch({
    		url: appUrl('/Objective/loadObjectiveAllocationRecord/' + objective.get('id')),
    		success: function(model, response, options) {
    			
    			var records = objective.get('allocationRecords');
    			var sumAllocation = 0;
    			for(var i=0; i<records.length; i++) {
    				sumAllocation += records.at(i).get('amountAllocated');
    			}
    			
    			// we have to update the number 
    			$('tr[data-id='+objective.get('id')+'] span#sumAllocationSpan')
    					.html(addCommas(sumAllocation));
       		}
    	});
	},
	render : function() {
		this.$el.html(this.mainCtrTemplate());
		this.mainSelectionView = new MainSelectionView({el: "#mainCtr #mainSelection"});

		this.rootObjective = new Objective(); 
		this.rootObjective.fetch({
			url: appUrl('/Objective/ROOT/'+fiscalYear),
			success : _.bind(function() {
				this.mainSelectionView.renderInitialWith(this.rootObjective);
			},this)
		});
	},
	renderInputAll: function() {
		
	},
	renderMainTblWithParent: function(parentObjective){
		this.currentParentObjective = parentObjective;
		this.renderMainTbl();
	},
	renderMainTbl :function() {
		if(this.currentParentObjective!=null)	{
			this.$el.find('#mainTbl').html(this.loadingTpl());
			// getRootCollection
			objectiveCollection = new ObjectiveCollection();
			this.collection = new ObjectiveCollection();
			
			objectiveCollection.url = appUrl("/ObjectiveWithAllocationRecords/" + this.currentParentObjective.get('id') + "/children");
			
			objectiveCollection.fetch({
				success : _.bind( function() {
					// we will now sorted out this mess!
					var i;
					for (i = 0; i < objectiveCollection.length; i++) {
						var o = objectiveCollection.at(i);
						if (o.get('parent') != null) {
							var parentId = o.get('parent').get('id');

							var parentObj = objectiveCollection.get(parentId);
							if (parentObj != null) {
								parentObj.get('children').add(o);
							}
						}
						
						if(o.get('type').get('id') == 105) {
							o.set('_planBudgetLevel', true);
						}
					}
					
					this.collection.add(objectiveCollection.where({parent: this.currentParentObjective}));
					
					
					
					
					var json = this.collection.toJSON();
					json.allRecords = {};
					json.objective = this.currentParentObjective.toJSON();
					this.$el.find('#mainTbl').html(this.mainTblTpl(json));
					
					this.$el.find('#mainTbl tbody td:first-child', this).each(function(i){
				        $(this).html((i+1) + ".");
				    });
				
				}, this)
			});
		}
	},
		
	emptyTbl: function(e) {
		this.$el.find('#mainTbl').empty();
	},

	toggle : function(e) {
		var l = e;
		var id = $(l.target).parents('tr').attr('data-id');
		var showChildren = $(l.target).parents('tr').attr('showChildren');
		if(showChildren == "true") {
			$(l.target).parents('tr').attr('showChildren', "false");
		} else {
			$(l.target).parents('tr').attr('showChildren', "true");
		}
		$(l.target).next('label').find('icon.label-caret').toggleClass("icon-caret-right icon-caret-down");

		var currentTr = $(l.target).parents('tr');
		
		currentTr.nextAll('[parentPath*=".' + id + '."]').each(function(el) {
			var $el = $(this);
			
			if(showChildren == "true") {
				// this is hide
					$el.hide();
			} else {
				// this is show
					$el.show();
			}
			
		}); 
	}

});
