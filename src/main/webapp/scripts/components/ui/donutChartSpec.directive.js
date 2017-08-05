app.directive("donutChartSpec", function() {
    return {
        restrict: 'EA',
        scope: {
            percentage: '='

        },
        link: function(scope, elem, attrs) {

            scope.$watch('percentage', function() {
                scope.clear();
                scope.draw();
            });
            scope.clear = function() {
                if (scope.svg){
									scope.svg.select("*").remove();
									scope.svg.select("text").remove();
								}
            };

            scope.draw = function() {
							var width = 120,
	                height = 120,
	                τ = 2 * Math.PI; // http://tauday.com/tau-manifesto
	            var arc = d3.svg.arc()
	                .innerRadius(50)
	                .outerRadius(60)
	                .startAngle(0);

	            //Create the SVG container, and apply a transform such that the origin is the
	            //center of the canvas. This way, we don't need to position arcs individually.
	          	if(!scope.svg) {
								scope.svg = d3.select(elem[0]).append("svg")
										.attr("width", width)
										.attr("height", height)
										.append("g")
										.attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
							}
							var svg = scope.svg;
	            //Add the background arc, from 0 to 100% (τ).
	            var background = svg.append("path")
	                .datum({
	                    endAngle: τ
	                })
	                .style("fill", "#ddd")
	                .attr("d", arc);

	            //Add the foreground arc in orange, currently showing 12.7%.
	            var percentage = 0;
	            if (scope.percentage != null) percentage =
	                Math.round(scope.percentage) / 100;

	            /*   if (scope.courseInformation.exerciseCount==0) percentage=0;
	            else percentage=scope.courseInformation.approvedExercises/scope.courseInformation.exerciseCount
	            percentage=percentage/100;
	            */
	            var foreground = svg.append("path")
	                .datum({
	                    endAngle: percentage * τ
	                })
	                .style("fill", "green")
	                .attr("d", arc);

	            var label = svg.append("text")
	                .attr("dy", ".35em")
	                .style("text-anchor", "middle")
	                .text(percentage * 100 + '%');

	            function arcTween(transition, newAngle) {
	                transition.attrTween("d", function(d) {
	                    var interpolate = d3.interpolate(d.endAngle, newAngle);
	                    return function(t) {
	                        d.endAngle = interpolate(t);
	                        return arc(d);
	                    };
	                });

	            }
            };


        }
    };
});
