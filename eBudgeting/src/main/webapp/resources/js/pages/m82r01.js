var MainTblView = Backbone.View.extend({
	/**
	 * @memberOf MainTblView
	 */
	initialize: function(options) {
		
	},
	objectiveTblTemplate: Handlebars.compile($('#objectiveTblTemplate').html()),
	objectiveTbodyTemplate: Handlebars.compile($('#objectiveTbodyTemplate').html()),
	el: "#mainTbl",
	
	render: function(e) {
		this.objectiveCollection = new ObjectiveCollection();
		
		this.$el.html("<div><img src='" + appUrl('/resources/graphics/loading.gif') + "'/></div>");
		
		this.objectiveCollection.fetch({
			url: appUrl('/Objective/ListAllInFiscalYear/' + fiscalYear),
			success: _.bind(function(model, response, options) {
				var html = this.objectiveTblTemplate();
				this.$el.html(html);
				for(var i=0; i<this.objectiveCollection.length; i++) {
					var obj=this.objectiveCollection.at(i);
					var json = obj.toJSON();
					json.padding = parseInt(obj.get('parentLevel')) * 25;
					var html = this.objectiveTbodyTemplate(json);
					this.$el.find('tbody').append(html);
				}
					
			},this)
		}); 
		
		
		return this;
	}
});
