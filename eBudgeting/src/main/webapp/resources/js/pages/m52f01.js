var ModalView = Backbone.View.extend({
	/** 
	 * @memberOf ModalView
	 */
	initialize: function(categories) {
		this.categories = categories;
	},
	el: '#modal',

	events: {
		"click #closeBtn" : "close",
		"click #saveBtn" : "saveForm"
	},
	saveForm: function() {
		// update 
		this.currentAssetBudget.set('name', this.$el.find('#nameTxt').val());
		this.currentAssetBudget.set('code', this.$el.find('#codeTxt').val());
		this.currentAssetBudget.set('description', this.$el.find('#descriptionTxt').val());
		
		var cat = AssetCategory.findOrCreate({id: this.$el.find('#categorySlt').val()});
		
		this.currentAssetBudget.set('category', cat);
		
		if(this.currentAssetBudget.get('id') != null) {
			this.currentAssetBudget.url = appUrl('/AssetBudget/' + this.currentAssetBudget.get('id'));
		} else {
			this.currentAssetBudget.url = appUrl('/AssetBudget/');
		}
		
		this.currentAssetBudget.save(null,{
			success: _.bind(function() {
				alert('คุณได้ทำการบันทึกข้อมูลเรียบร้อยแล้ว');
				mainTblView.reRender();
				this.close();
			},this)
		});
	},
	close : function() {
		this.$el.modal('hide');
	},
	
	modalBodyTemplate : Handlebars.compile($("#modalBodyTemplate").html()),
	categorySltTemplate : Handlebars.compile($("#categorySltTemplate").html()),
	
	renderWithAssetBudget : function(assetBudget) {
		this.currentAssetBudget = assetBudget;
		var json = this.currentAssetBudget.toJSON();
		var html = this.modalBodyTemplate(json);
		
		if(this.currentAssetBudget.get('id') == null) {
			this.$el.find('.modal-header span').html("เพิ่มรายการใหม่");
		} else {
			
			this.$el.find('.modal-header span').html("แก้ไขรายการ");
		}
		
		this.$el.find('.modal-body').html(html);
		
		json = this.categories.toJSON();
		for(var i=0; i< this.categories.length; i++) {
			var cat = this.categories.at(i);
			var currentCat = this.currentAssetBudget.get('category');
			if(currentCat != null && cat.get('id') == currentCat.get('id')) {
				json[i].isSelected = true;
			}
		}
		
		html = this.categorySltTemplate(json);
		this.$el.find('#categorySlt').append(html);
		
		this.$el.modal({show: true, backdrop: 'static', keyboard: false});
		return this;
	}
});

var AssetSelectionView = Backbone.View.extend({
	/**
	 * @memberOf AssetSelectionView
	 */
	initialize : function() {
		this.assetTypes = new AssetTypeCollection();
		this.assetKinds = new AssetKindCollection();

		_.bindAll(this, 'renderType');
		_.bindAll(this, 'renderKind');
		
		this.assetTypes.bind('reset', this.renderType);
		this.assetKinds.bind('reset', this.renderKind);

		
	},
	selectionTemplate : Handlebars.compile($("#selectionTemplate").html()),
	assetGroupSelectionTemplate : Handlebars.compile($("#assetGroupSelectionTemplate").html()),
	assetKindDisabledSelectionTemplate : Handlebars.compile($("#assetKindDisabledSelectionTemplate").html()), 
	
	el : '#mainSlt',
	
	events: {
		"change #assetGroupSlt" : "assetGroupSltChange",
		"change #typeAssetTypeSlt" : "assetTypeSltChange",
		"change #typeAssetKindSlt" : "assetKindSltChange"
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
		var assetKind = AssetKind.findOrCreate(kindId);
		
		mainTblView.renderWithAssetKind(assetKind);
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

var MainTblView = Backbone.View.extend({
	/**
	 * @memberOf MainTblView
	 */
	initialize : function(categories) {
		categories.fetch({
			url: appUrl('/AssetCategory/all'),
			success: _.bind(function() {
				this.categories = categories;
				this.modalView = new ModalView(this.categories);
			}, this)
		});
	},
	el : '#mainTbl',
	events: {
		"click .menuNew" : "newForm",
		"click .menuEdit" : "editForm",
		"click .menuDelete" : "deleteRow"
	},
	mainTblTemplate: Handlebars.compile($("#mainTblTemplate").html()), 
	assetBudgetRowTemplate: Handlebars.compile($("#assetBudgetRowTemplate").html()),
	tbodyTemplate: Handlebars.compile($("#tbodyTemplate").html()),
	
	
	newForm : function() {
		this.currentAssetBudget = new AssetBudget();
		this.currentAssetBudget.set('kind', this.assetKind);
		this.modalView.renderWithAssetBudget(this.currentAssetBudget);

	},
	
	editForm : function(e) {
		var assetBudgetId = $(e.target).parents('tr').attr('data-id');
		this.currentAssetBudget = AssetBudget.findOrCreate(assetBudgetId);
		this.modalView.renderWithAssetBudget(this.currentAssetBudget);

		
	},	
	reRender: function() {
		this.renderWithAssetKind(this.assetKind);
	},
	
	renderWithAssetKind: function(assetKind) {
		this.assetKind = assetKind;
		this.collection = new AssetBudgetCollection();
		this.collection.fetch({
			url: appUrl("/AssetBudget/byKindId/" + this.assetKind.get('id')),
			success: _.bind(function() {
				var html = this.mainTblTemplate();
				var categories = new AssetCategoryCollection();
				
				this.$el.html(html);
				
				if(this.collection.length>0) {
					// OK we'll group into category...
					for(var i=0; i<this.collection.length; i++) {
						var asset = this.collection.at(i);
						var cat = asset.get('category');
						if(cat == null) {
							cat = AssetCategory.findOrCreate({id:0});
							cat.set('name', "ยังไม่ระบุประเภท");
							cat.set('class', 'error');
						} else {
							cat.set('class', 'info');
						}
						
						categories.add(cat);
						
						if(cat.get('assets') == null) {
							cat.set('assets', new AssetBudgetCollection());
						}
						cat.get('assets').add(asset);
					}
					
					
					// then the inside row
					//var json=this.collection.toJSON();
					var json = categories.toJSON();
					
					
					
					html = this.tbodyTemplate(json);
					this.$el.find('tbody').html(html);
				}

				// bind all cell
				this.collection.each(function(model){
					model.bind('change', this.renderAssetBudget, this);
					this.renderAssetBudget(model);
				}, this);
				
			}, this)
		});
	},
	
	renderAssetBudget: function(model) {
		var modelEl = this.$el.find('tr[data-id='+ model.get('id') +']');
		
		var json = model.toJSON();		
			
		modelEl.html(this.assetBudgetRowTemplate(json));

	},
	
	addAssetBudget: function(assetBudget) {
		var assetBudgetEl = this.$el.find('tr[data-id='+ assetBudget.get('id') +']');
		
		
		if(assetBudgetEl == null || assetBudgetEl.length == 0){
			this.collection.push(assetBudget);
			this.$el.find('tbody').append('<tr data-id='+ assetBudget.get('id')+ '></tr>');
			this.renderAssetBudget(assetBudget);
		} else {
			this.renderAssetBudget(assetBudget);
		}

	},
	deleteRow: function(e) {
		var assetBudgetId = $(e.target).parents('tr').attr('data-id');
		var trEl = $(e.target).parents('tr');
		var assetBudgetToDelete = AssetBudget.findOrCreate(assetBudgetId);

		if(confirm("คุณต้องการลบรายการ " + assetBudgetToDelete.get('name')) == true ) {

			assetBudgetToDelete.url = appUrl('/AssetBudget/' + assetBudgetToDelete.get('id'));
				assetBudgetToDelete.destroy({wait: true,
					success: _.bind(function() {					
						this.collection.remove(assetBudgetToDelete);
						trEl.remove();
					},this),
					error: _.bind(function(model, xhr, options) {
						alert("ไม่สามารถลบรายการได้ \n Error: " + xhr.responseText);
					},this)
				});
			}
	}
});


