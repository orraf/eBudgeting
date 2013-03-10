var MainCtrView = Backbone.View.extend({
	/**
     *  @memberOf MainCtrView
     */
	initialize : function(options) {
		

	},

	el : "#mainCtr",
	mainCtrTemplate : Handlebars.compile($("#mainCtrTemplate").html()),
	
	events : {
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
		}
		this.$el.html(this.mainCtrTemplate(json));
		

	}
	
});