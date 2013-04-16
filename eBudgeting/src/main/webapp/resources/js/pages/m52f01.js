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

