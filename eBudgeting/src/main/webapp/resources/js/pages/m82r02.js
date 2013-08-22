var MainTblView = Backbone.View.extend({
	/**
	 * @memberOf MainTblView
	 */
	initialize: function(options) {
		
		
	},
	organiationNodeTemplate: Handlebars.compile($('#organiationNodeTemplate').html()),
	
	el: "#mainTbl",
	
	render: function(e) {
		this.rootOrg = new Organization();
		this.rootOrg.fetch({
			url: appUrl('/Organization/findRoot'),
			success: _.bind(function(e) {
				// try to render the thing?
				var length=this.rootOrg.get('children').length;
				var grandChildren = new OrganizationCollection();
				var greatGrandchildren = new OrganizationCollection();
				
				for(var i=0; i<length ; i++) {
				
					var child = this.rootOrg.get('children').at(i);
					
					grandChildren.add(child.get('children').toArray());
					
					var json = child.toJSON();
					if(child.get('id') == 101010000)
						json.isHome = true;
					else 
						json.isHome = false;
					
					if(i%4 == 1 )
						json.isClearLeft = true;
					
					var html = this.organiationNodeTemplate(json);
					this.$el.find("#primaryNav").append(html);
				}
				
				// now go through the third one
				for(var i=0; i<grandChildren.length; i++) {
					var org = grandChildren.at(i);
					
					if(org != null) {
						
						greatGrandchildren.add(org.get('children').toArray());
					
						var html = this.organiationNodeTemplate(org.toJSON());
						
						var el = "li#" + org.get('parent').get('id') + " > ul";
						
						if($(el).length == 0) {
							this.$el.find("li#" + org.get('parent').get('id')).append("<ul></ul>");
						}
						
						this.$el.find("li#" + org.get('parent').get('id') + " > ul").append(html);
					}
				}
				
				// now go through the forth one
				for(var i=0; i<greatGrandchildren.length; i++) {
					var org = greatGrandchildren.at(i);
					
					if(org != null) {
						
						
						var html = this.organiationNodeTemplate(org.toJSON());
						
						var el = "li#" + org.get('parent').get('id') + " > ul";
						
						if($(el).length == 0) {
							this.$el.find("li#" + org.get('parent').get('id')).append("<ul></ul>");
						}
						
						this.$el.find("li#" + org.get('parent').get('id') + " > ul").append(html);
					}
				}
				
				
			},this)
		});
		
		
		return this;
	}
});
