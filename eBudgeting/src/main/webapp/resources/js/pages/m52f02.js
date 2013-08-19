var ModalView = Backbone.View.extend({
	initialize: function(){
	    
	},
	el: "#modal",
	assetCategory : null,
	modalTemplate: Handlebars.compile($("#modalTemplate").html()),
	
	events: {
		"click #cancelBtn" : "close",
		"click #saveBtn" : "save"
	},
	
	render: function() {
		if(this.currentAssetCategory != null) {
			
			this.$el.find('.modal-header span').html("เพิ่มประเภทงบลงทุน");
			
			var html = this.modalTemplate(this.currentAssetCategory.toJSON());
			this.$el.find('.modal-body').html(html);

			
			this.$el.modal({show: true, backdrop: 'static', keyboard: false});
			return this;
		}	
	},
	
	renderWith: function(assetCategory) {
		this.currentAssetCategory = assetCategory;
		this.render();
	},
	
	save: function(e) {
		// ok
		var newModel=false;
		if(this.currentAssetCategory.get('id') == null) {
			newModel = true;
		}
		this.currentAssetCategory.save({
			name: this.$el.find('input[id=nameTxt]').val(),
			code: this.$el.find('input[id=codeTxt]').val()
		},{
			success : _.bind(function(model) {
				
				if(newModel) {
					assetCategories.add(model);
				}
				assetCategories.trigger('reset');
				this.$el.modal('hide');
			},this)
		});
	},
	
	close: function() {
		this.$el.modal('hide');
	}
	
});

var MainCtrView = Backbone.View.extend({
	initialize: function() {
		this.collection.bind('reset', this.render, this);
	},
	events: {
		"click .menuNew" : "newassetCategory",
		"click .menuEdit" : "editassetCategory",
		"click .menuDelete" : "deleteassetCategory",
		"click .lineSave" : "saveLine",
		"click .cancelLineSave" : "cancelSaveLine",
		"click a.pageLink" : "gotoPage"
	},
	mainCtrTpl: Handlebars.compile($("#mainCtrTemplate").html()),
	newRowTpl : Handlebars.compile($('#newRowTemplate').html()),
	rowTpl : Handlebars.compile($("#rowTemplate").html()),
	el: '#mainCtr',
	collection : assetCategories,
	
	modalView : new ModalView(),
	
	render: function() {
		var json = {};
		json.pageParams = this.collection.toPageParamsJSON();
		
		this.$el.html(this.mainCtrTpl(json));
		this.$el.find('tbody').html(this.rowTpl(this.collection.toJSON()));
	},
	
	gotoPage: function(e) {
		var pageNumber = $(e.target).attr('data-id');
		this.renderTargetPage(pageNumber);
	},
	
	renderTargetPage: function(pageNumber) {
		this.collection.targetPage = pageNumber;
		this.collection.fetch({
			success: function() {
				assetCategories.trigger('reset');
			}
		});
	},
	
	newassetCategory: function(e) {
		if(! $(e.currentTarget).hasClass('disabled') ) {
			
			this.modalView.renderWith(new AssetCategory());
		}
	},
	
	saveLine: function(e) {
		// ok
		var newModel=false;
		if(this.currentAssetCategory.get('id') == null) {
			newModel = true;
		}
		this.currentAssetCategory.save({
			name: this.$el.find('input[id=nameTxt]').val()
		},{
			success : _.bind(function(model) {
				
				if(newModel) {
					assetCategories.add(model);
				}
				assetCategories.trigger('reset');
			},this)
		});
	},
	
	cancelSaveLine: function(e) {
		this.collection.trigger('reset');
	},
	
	editassetCategory: function(e) {
		var tuId = this.$el.find('input[name=rowRdo]:checked').val();
		var assetCategory = AssetCategory.findOrCreate(tuId);
		
		this.currentAssetCategory=assetCategory;
		this.modalView.renderWith(this.currentAssetCategory);
	},
	
	deleteassetCategory: function(e) {
		var tuId = this.$el.find('input[name=rowRdo]:checked').val();
		var assetCategory = AssetCategory.findOrCreate(tuId);
		
		if(confirm('คุณต้องการลบหน่วยนับ \"'+ assetCategory.get('name') + '\"')==true) {
			assetCategory.destroy({
				success: function() {
					assetCategories.trigger('reset');
				}
			});
		}
		
	}
});