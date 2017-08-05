'use strict';

angular
		.module('buenOjoApp')
		.controller(
				'CourseLevelMapGraphController',
				function($scope, $state, CourseLevelMap, CourseLevelMapGraph, CourseLevelSession, CourseLevelSessionFiltered, CourseLevelSessionEnforceEnrollment,CourseLevelSessionEnforceEnrollmentRandom,
						$stateParams) {
					$scope.courseLevelMaps = [];
					$scope.courseLevelSessions= [];
					$scope.loadAllCourseLevelSession =
						  function(courseId) {
				            CourseLevelSessionFiltered.query(
				            		{
				            			courseId : courseId
									},

				            		function(result) {
										$scope.courseLevelSessions = result;
										$scope.buildNodeGraph();
									}
							);
				        };

				    $scope.buildNodeGraph = function()
				    {

				    	// Create the input graph

				    	function getLevel(node, edges)
						{
							var level=0;
							var parents = edges.get({
								  fields: ['to'],    // output the specified fields only
								  filter: function (item) {
								    return (item.from == node.id);
								  }
							});
							while (parents.length!=0)
							{
								level++;
								parents = edges.get({
									fields: ['to'],    // output the specified fields only
									filter: function (item) {
										return (item.from == parents[0].to);
									}
								});
							}
							return level;
						}

						function getColor(node)
						{
							var color="gray";

							var nodeStatus;
							nodeStatus= new vis.DataSet($scope.courseLevelSessions).
							get(
							{
								fields: ['status'],    // output the specified fields only
								filter: function (item) {
									return (item.courseLevelMap.id == node.id);
								}
							});
							if (nodeStatus[0]!=undefined)
							switch (nodeStatus[0].status)
							{
								case 'NotStarted':
									color='grey';
								break;
								case 'Done':
									color='lime';
								break;
								case 'Cancelled':
									color='red';
								break;
								case 'InProgress':
									color='yellow';
								break;

							}
							return color;
						}
						var nodes = [];
						var edges = [];
						var i;
						var usedNodes = [];
						for (i = 0; i < $scope.courseLevelMaps.length; i++) {
							var courseLevelMapEntry = $scope.courseLevelMaps[i];
							if (usedNodes
									.indexOf(courseLevelMapEntry.level.id) == -1) {
								nodes
										.push({
											id : courseLevelMapEntry.level.id,
											label : courseLevelMapEntry.level.name
										});
								usedNodes
										.push(courseLevelMapEntry.level.id);
							}
							if (courseLevelMapEntry.parent != null) {
								edges
										.push({
											from : courseLevelMapEntry.level.id,
											to : courseLevelMapEntry.parent.id,
											arrows : 'from'
										});
							}
						}
						edges = new vis.DataSet(edges);
						for (i=0;i<nodes.length;i++)
							nodes[i]["level"]=getLevel(nodes[i],edges);
						for (i=0;i<nodes.length;i++)
							nodes[i]["color"]=getColor(nodes[i]);

						nodes = new vis.DataSet(nodes);

						var container = document
								.getElementById('mynetwork');
						var data = {
							nodes : nodes,
							edges : edges
						};
						var options = {
							layout : {
								hierarchical : {
									direction : 'UD'
								}
							}
						};
						var network = new vis.Network(
								container, data, options);

						/*********************/
				    	var g = new dagreD3.graphlib.Graph()
				    	  .setGraph({})
				    	  .setDefaultEdgeLabel(function() { return {}; });

				    	// Here we"re setting nodeclass, which is used by our custom drawNodes function
				    	// below.
	/*			    	g.setNode(0,  { label: "TOP",       class: "type-TOP" });
				    	g.setNode(1,  { label: "S",         class: "type-S" });
				    	g.setNode(2,  { label: "NP",        class: "type-NP" });
				    	g.setNode(3,  { label: "DT",        class: "type-DT" });
				    	g.setNode(4,  { label: "This",      class: "type-TK" });
				    	g.setNode(5,  { label: "VP",        class: "type-VP" });
				    	g.setNode(6,  { label: "VBZ",       class: "type-VBZ" });
				    	g.setNode(7,  { label: "is",        class: "type-TK" });
				    	g.setNode(8,  { label: "NP",        class: "type-NP" });
				    	g.setNode(9,  { label: "DT",        class: "type-DT" });
				    	g.setNode(10, { label: "an",        class: "type-TK" });
				    	g.setNode(11, { label: "NN",        class: "type-NN" });
				    	g.setNode(12, { label: "example",   class: "type-TK" });
				    	g.setNode(13, { label: ".",         class: "type-." });
				    	g.setNode(14, { label: "sentence",  class: "type-TK" });
*/
				    	data.nodes.forEach(function(v) {

				    		var node =g.setNode(v.id, {label: v.label, class:"type-Node "+ v.color});
				    	//	node.rx = node.ry = 5;

					    	});

				    	data.edges.forEach(function(v) {

				    		g.setEdge(v.to, v.from, {lineInterpolate:"step", arrowhead: "undirected" });
					    	});

				    	/*
				      g.nodes().forEach(function(v) {
				    	  var node = g.node(v);
				    	  // Round the corners of the nodes
				    	  node.rx = node.ry = 5;
				    	});*/

				    	// Set up edges, no special attributes.
				    	/*g.setEdge(3, 4);
				    	g.setEdge(2, 3);
				    	g.setEdge(1, 2);
				    	g.setEdge(6, 7);
				    	g.setEdge(5, 6);
				    	g.setEdge(9, 10);
				    	g.setEdge(8, 9);
				    	g.setEdge(11,12);
				    	g.setEdge(8, 11);
				    	g.setEdge(5, 8);
				    	g.setEdge(1, 5);
				    	g.setEdge(13,14);
				    	g.setEdge(1, 13);
				    	g.setEdge(0, 1)
*/
				    	// Create the renderer
				    	var render = new dagreD3.render();

				    	// Set up an SVG group so that we can translate the final graph.
				    	var svg = d3.select("svg"),
				    	    svgGroup = svg.append("g");

				    	// Run the renderer. This is what draws the final graph.
				    	render(d3.select("svg g"), g);

				    	// Center the graph
				    	var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
				    	svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
				    	svg.attr("height", g.graph().height + 40);


				    };


					$scope.loadAllByCourse = function(course) {
						CourseLevelMapGraph
								.query(
										{
											courseId : course
										},
										function(result) {
											$scope.courseLevelMaps = result;
											$scope.loadAllCourseLevelSession($stateParams.id);
										});
						};





					$scope.loadAllByCourse($stateParams.id);
					/*$scope.loadAllCourseLevelSession($stateParams.id);
					*/

					$scope.refresh = function() {
						$scope.loadAllByCourse(null);
						$scope.loadAllCourseLevelSession(null);
						$scope.clear();
					};

					$scope.clear = function() {
						$scope.courseLevelMapGraph = {
							id : null
						};
					};

					 $scope.enforceEnrollment = function () {
				            CourseLevelSessionEnforceEnrollment.query(
				            		{
										courseId : $stateParams.id
									},function(result) {
										$scope.loadAllByCourse($stateParams.id);

							});

					 };

					 $scope.enforceEnrollmentRandom = function () {
						 CourseLevelSessionEnforceEnrollmentRandom.query(
				            		{
										courseId : $stateParams.id
									},function(result) {
											$scope.loadAllByCourse($stateParams.id);

							});

					 };


				});
