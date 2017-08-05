var customDirectives = angular.module('customDirectives',[]);
customDirectives.directive('customPopover', function ($compile) {
    return {
        restrict: 'A',
        link: function (scope, el, attrs) {
        	$(el).popover({
        	  trigger: 'manual' ,
                html: true,
                content: attrs.popoverhtml,
                placement: attrs.popoverPlacement,
                container: '#tooltipContainer'
            });
        	
        	$(el).attr("keep","no");
        	$(el).attr("showing", "no");
        	$(el).bind('hidden.bs.popover', function () {
        		$(el).attr("showing", "no");
        		});
        	$(el).bind('shown.bs.popover', function () {
        		$(el).attr("showing", "yes");
        		});
        	
        	
        	$(el).bind("mouseenter",
        		
        			function ()  {
        		if ($(el).attr("showing")=="no")
        			$(el).popover('show'); }
        	);
        	$(el).bind("mouseleave",
        			function ()  {
        		if ($(el).attr("keep")=="no")
        			$(el).popover('hide'); }
        	);
        	$(el).bind("click",
        		
        	function ()  {
        		if ($(el).attr("showing")=="yes" && $(el).attr("keep")=="no")
        			{
        				$(el).attr("keep","yes");
        			}
        		else
        		if ($(el).attr("showing")=="yes" && $(el).attr("keep")=="yes")
        		{
        			$(el).attr("keep","no");
        			$(el).popover('hide');
    			}
        		else
        			if ($(el).attr("showing")=="no")
            		{
            			$(el).attr("keep","yes");
            			$(el).popover('show');
        			}
            			
        	}
        			
        	);
        	scope.$on("forceHide",
        			function ()  {
        		$(el).attr("keep", "no");
        		$(el).popover('hide');}
        	);
         
        	
        }
    };
});