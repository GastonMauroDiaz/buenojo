'use strict';

angular
		.module('buenOjoApp')
		.controller(
				'tabRecorridoController',
				function($scope, $state, $q, ActivityTrace, Principal, User, BuenOjoUtils,ParseLinks, CourseLevelMap, CourseLevelMapGraph, CourseLevelSession, CourseLevelSessionFiltered, CourseLevelSessionEnforceEnrollment,EnrollmentCurrentUserAndCourse,CourseLevelSessionEnforceEnrollmentRandom,
						$stateParams) {

					$scope.page = 0;
					$scope.courseLevelMaps = [];
					$scope.courseLevelSessions= [];
					$scope.activityTraces= [];
					$scope.loadAll = function() {

							$q.when(Principal.identity()).then(function(identity){
									if (!identity) {
										 return $q.reject();
									}
									var userPromise = User.get({login: identity.login},function(user){
											$scope.user = user;
									}).$promise;
									$q.all([userPromise,$scope.enrollmentPromise]).then(function(){
													if (!$scope.enrollment || !$scope.user) {
															return $q.reject();
													}
													return $scope.enrollment.$promise;
									}).then(function(){

											$scope.loadAllByCourse($stateParams.id);
											$scope.loadTraces();

									});
							});


								// EnrollmentCurrentUserAndCourse.get({id: $scope.courseId}, function(enrollment){
								// 	$scope.enrollment = enrollment;
								// 	$scope.loadTraces();
								// });

	        };

					$scope.loadTraces = function() {
						ActivityTrace.queryByEnrollment({enrollmentId: $scope.enrollment.id, page: $scope.page, size: 20}, function(result, headers) {
								$scope.links = ParseLinks.parse(headers('link'));
								$scope.activityTraces = result;
								$scope.activityMap = {};
								result.forEach(function(element,index, array){
										if (!isFinite(element.activity)){
												$scope.activityMap[element.activity.id] = element.activity;
										}
										element.duration = BuenOjoUtils.duration(element.startDate, element.endDate);
								});
						})
					};


					$scope.describe = function (trace) {
						 if (isFinite(trace.activity)) {
							  return BuenOjoUtils.describeActivity($scope.activityMap[trace.activity]);
						 }
						 return BuenOjoUtils.describeActivity(trace.activity);
					};


					$scope.loadPage = function(page) {
							$scope.page = page;
							$scope.loadAll();
					};
					$scope.loadAll();

					$scope.loadAllCourseLevelSession =
						  function(courseId) {
				            CourseLevelSessionFiltered.userAndCourse(
				            		{	userId: $scope.user.id,
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
									return (item.courseLevelMap!=undefined&&(item.courseLevelMap.id == node.id));
								//PROBLEMA DE MAPA EN BLANCO, PROBLEMA  DE UNDEFINED
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

						var data = {
							nodes : nodes,
							edges : edges
						};

						/*********************/
				    	var g = new dagreD3.graphlib.Graph()
				    	  .setGraph({})
				    	  .setDefaultEdgeLabel(function() { return {}; });

				    	data.nodes.forEach(function(v) {

				    		var node =g.setNode(v.id, {label: v.label, class:"type-Node "+ v.color});

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
				    	var svg = d3.select("#svg-canvas"),
				    	    svgGroup = svg.append("g");

				    	// Run the renderer. This is what draws the final graph.
				    	render(d3.select("#svg-canvas g"), g);

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






					/*$scope.loadAllCourseLevelSession($stateParams.id);
					*/

					$scope.refresh = function() {
						$scope.loadAllByCourse($stateParams.id);

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
