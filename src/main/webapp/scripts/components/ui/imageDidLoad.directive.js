app.directive('imageDidLoad', function($parse) {
    return {
        restrict: 'A',

        link: function(scope, element, attrs) {
    	 	if (scope.imageInQueue == null) scope.imageInQueue= 0;

        	scope.imageInQueue++;
        	element.bind('load', function() {
        		scope.imageInQueue--;
        		if (scope.imageInQueue==0)
        			$parse(attrs.imageDidLoad)(scope);
            });
        }
    };
});
