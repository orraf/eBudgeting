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
		this.objectiveCollection.fetch({
			url: appUrl('/Objective/findAllByFiscalYear/' + fiscalYear),
			success: _.bind(function(model, response, options) {
				var html = this.objectiveTblTemplate();
				this.$el.html(html);
				
				// now we'll go through 
				var queue = new ObjectiveCollection();
				
				// sort the collection;
				this.objectiveCollection.comparator = function(o) {
					return o.get('code');
				};
				// reverse the sort
				this.objectiveCollection.comparator = 
					reverseSortBy(this.objectiveCollection.comparator);
				
				this.objectiveCollection.sort();
				
				//push in the queue {
				queue.add(this.objectiveCollection.toArray());
				
				while(!queue.isEmpty()) {
					var obj=queue.pop();
					var json = obj.toJSON();
					json.padding = parseInt(obj.get('parentLevel')) * 25;
					var html = this.objectiveTbodyTemplate(json);
					this.$el.find('tbody').append(html);
					
					// now push the children on the queue
					if(obj.get('children') != null) {
						obj.get('children').comparator = function(o) {
							return o.get('code');
						};
						obj.get('children').comparator = 
							reverseSortBy(obj.get('children').comparator);
						obj.get('children').sort();
						queue.add(obj.get('children').toArray());
					}
				}
				
			},this)
		}); 
		
		
		return this;
	}
});
