'use strict';

angular.module('buenOjoApp')
    .controller('ImageCompletionExerciseGameController', function($timeout, $scope, $state, $stateParams, BuenOjoUtils,entity, ImageCompletionExercise, TagCloud, ExerciseTip, ImageCompletionExerciseTip, CourseLevelSessionFiltered, CourseLevelSessionAddScore, Activity, ActivityRouter, ActivityTrace) {
      $scope.center = {
      // must be empty so that the map does not flicker on load
      };

      $scope.custom_style = {
                image: {
                    icon: {
                        anchor: [0.5, 0.5],
                        anchorXUnits: 'fraction',
                        anchorYUnits: 'fraction',
                        opacity: 0.90,
                        src: 'assets/images/map-marker.png'
                    }
                }
            };

      $scope.mapMarker = {
        "lon":-73.0408532427631,"lat":-50.4417446363472,
        style: $scope.custom_style
      };

      angular.extend($scope, {
          center: $scope.center,
          custom_style: $scope.custom_style,
          marker: $scope.mapMarker,
          defaults: {
              controls: {
                  zoom: false,
                  rotate: false,
                  attribution: false
              },
              interactions: {
                  mouseWheelZoom: false,
                  dragPan: false,
                  doubleClickZoom: false,
                  select: false
              }

          },
          controls: [{
              name: 'zoom',
              active: false
          }, {
              name: 'rotate',
              active: false
          }, {
              name: 'attribution',
              active: false
          }]

      });

        // $scope.unregisterQuit = $scope.$on('$stateChangeStart', function( event ) {
        //
        //   if (event.targetScope.toStateParams.force == true || event.targetScope.toState.name == "imageCompletionScore" || event.targetScope.toState.name == 'imageCompletionExerciseResult' || event.targetScope.toState.name == "imageCompletionExerciseGame")
        //     return;
        //
        //   event.preventDefault();
        //   $scope.quitEvent = event;
        //   $scope.quitExercise();
        // });

        $scope.exerciseTip = undefined;
        $scope.scaleProperties = {
            id: 1
        };
        $scope.enrollment = $state.params.enrollment;
        $scope.activity = $state.params.activity;

        $scope.tolerance = 1;
        $scope.activity = $stateParams.activity;
        $scope.ResultType = Object.freeze({
            SOLVED: 0,
            SOLVED_WITH_ERROR: 1,
            NOT_SOLVED: 2,
            INCOMPLETE: 3,
            messages: ['Resuelto correctamente',
                'Resuelto con fallos',
                'No Resuelto',
                'Incompleto'
            ],
            scores: [1, 0.5, 0, 0],

            scoreFromResultType: function(resultType) {
                return {
                    score: this.scores[resultType],
                    message: this.messages[resultType],
                    type: resultType
                };
            }
        });

        $scope.TagCircleState = Object.freeze({
            PLAIN: 0,
            CORRECT: 1,
            WRONG: 2,
            HOVER: 3,
            states: [
                'plain',
                'correct',
                'wrong',
                'hover'
            ],
            classForState: function(state) {

                if (!state) return this.states[this.PLAIN];

                return this.states[state];
            },
            stateFromClass: function(klass) {
                return this.states.indexOf(klass, 0);
            }
        });

        $scope.checkAnswer = function(tagId, tagCircle) {
            var solution = $scope.imageCompletionExercise.imageCompletionSolution;
            var tagPairs = solution.tagPairs;
            var correct = tagPairs.find(function(element, index, array) {
                var correctTag = element.tag.id === tagId;
                var correctSlot = element.tagSlotId == tagCircle.number;

                return correctTag && correctSlot;
            });
            return correct;
        };


        $scope.textForTagCircle = function(circle){
            var tagName = $scope.imageCompletionExercise.imageCompletionSolution.tagPairs.find(function(element,index, array){
                  return element.tagSlotId === circle.number;
            });
            return tagName.tag.name;
        };

        $scope.tagDropped = function(event, element, tagCircle) {
            if (tagCircle.state === $scope.TagCircleState.WRONG || tagCircle.state === $scope.TagCircleState.CORRECT) return;

            var tagId = element.draggable.context.id;
            var isCorrectAnswer = $scope.checkAnswer(parseInt(tagId), tagCircle);

            $timeout(function() {
                if (isCorrectAnswer) {
                    tagCircle.state = $scope.TagCircleState.CORRECT;
                } else {
                    tagCircle.state = $scope.TagCircleState.WRONG;
                }

                $scope.answers++;
                if ($scope.answers === $scope.imageCompletionExercise.tagCircles.length) {
                    var result = $scope.evaluateExercise()
                    $scope.endExercise(result);

                } else {
                    var satelliteImage = $scope.imageCompletionExercise.satelliteImages[$scope.activeTab];
                    ImageCompletionExerciseTip.get({
                        id: tagId,
                        image: satelliteImage.id
                    }, function(result) {
                        $timeout(function() {
                            $scope.exerciseTip = result;
                        }, 10);
                    });
                }
            }, 100);
        };

        $scope.dragOver = function(event, element, tagCircle) {
            if (tagCircle.state === $scope.TagCircleState.PLAIN) {

                $timeout(function() {
                    tagCircle.state = $scope.TagCircleState.HOVER;
                }, 10);
            }
        };

        $scope.dragOut = function(event, element, tagCircle) {

            if (tagCircle.state === $scope.TagCircleState.HOVER) {
                $timeout(function() {
                    tagCircle.state = $scope.TagCircleState.PLAIN;
                }, 10);
            }
        };

        $scope.beforeDrop = function(event, element, tagCircle) {};

        $scope.colorPickerClasses = ["white-bg", "red-bg", "yellow-bg", "orange-bg", "green-bg"];
        $scope.tagCircleColors = ["white", "red", "yellow", "orange", "green"];
        $scope.activeTagCircleColor = $scope.tagCircleColors[0];
        $scope.imageCompletionExercise = entity;
        $scope.activeTab = 0;
        $scope.answers = 0;

        var imgScale = 0.5;

        $scope.load = function(id) {

            ImageCompletionExercise.get({
                id: id
            }, function(result) {
                $scope.imageCompletionExercise = result;

                BuenOjoUtils.pushEsriTopoToBack($scope.imageCompletionExercise.satelliteImages);
                $scope.imageCompletionExercise.imageCompletionSolution.tagPairs
                if ($scope.enrollment) {  // PARA PODER JUGAR SIN ENROLLMENT Y PROBAR JUEGOS

                  CourseLevelSessionFiltered.current({
                      courseId: $scope.enrollment.course.id
                  }, function(session) {
                      $scope.currentCourseLevelSession = session;
                  });

                  if ($scope.activity) {
                      ActivityTrace.start({
                          activityId: $scope.activity.id
                      },function(trace){
                          $scope.activityTrace = trace;

                          // FORZAR PERDER
                          // $timeout(function () {
                          //   $scope.playerLost();
                          // }, 1000);

                          // FORZAR GANAR
                          // $timeout(function () {
                          //   $scope.endExercise($scope.ResultType.scoreFromResultType($scope.ResultType.SOLVED));
                          // }, 1000);
                      });
                  }
                } else {
                  console.log("NO ENROLLMENT");
                }

                $scope.tabChanged(0);
                TagCloud.get({
                    id: result.id
                }, function(tagCloud) {

                    $scope.tagCloud = tagCloud;

                });

                $timeout(function() {

                    var properties = {
                        resolution: (result.satelliteImages[0].resolution / imgScale),
                        scaleHeight: 7,
                        segments: 5,
                        scaleUnits: 5000,
                        unit: 'm',
                        lineWidth: 0.5,
                        textPadding: -10,
                        textFont: '5pt Arial',
                        color: 'black'

                    };

                    $scope.scaleProperties = properties;

                }, 100);
                $scope.center = {
                    lat: result.satelliteImages[0].lat,
                    lon: result.satelliteImages[0].lon,
                    zoom: 4
                };

                $scope.mapMarker.lat = $scope.center.lat;
                $scope.mapMarker.lon = $scope.center.lon;

                $scope.imageCompletionExercise.tagCircles.forEach(function(circle, index, array) {
                    circle.state = $scope.TagCircleState.PLAIN;
                    circle.cssClass = $scope.TagCircleState.classForState(circle.state);

                    // this will be dynamic sometime in the future :)
                    circle.radioPx *= imgScale;
                    circle.x = circle.x * imgScale - circle.radioPx;
                    circle.y = circle.y * imgScale - circle.radioPx;

                    circle.height = circle.radioPx * 2;
                    circle.width = circle.height;
                    circle.cssStyle = {
                        top: circle.y,
                        left: circle.x,
                        width: circle.width,
                        height: circle.height,
                        'border-color': $scope.tagCircleColor
                    };
                    circle.text = $scope.textForTagCircle(circle);
                    circle.textCSS = {
                        top: circle.y+ circle.height,
                        left: circle.x,
                        width: circle.width,
                        color: $scope.tagCircleColor

                    };

                });
            });

        };

        $scope.load($stateParams.id);

        $scope.refresh = function() {
            $scope.clear();
            $scope.load();

        };

        $scope.clear = function() {
            $scope.imageCompletionExercise = null;
            $scope.tagCloud = null;
        };

        $scope.tabChanged = function(index) {
            $scope.copyright = $scope.imageCompletionExercise.satelliteImages[index].copyright;
            $scope.activeTab = index;

        };

        $scope.selectColor = function(index) {

            $scope.tagCircleColor = $scope.tagCircleColors[index];
            $scope.compassPath = 'assets/images/compass_' + index + '.png';
            $scope.imageCompletionExercise.tagCircles.forEach(function(circle, index, array) {
                circle.cssStyle["border-color"] = $scope.tagCircleColor;
                circle.textCSS.color = $scope.tagCircleColor;
            });
        }

        $scope.evaluateExercise = function() {
            if ($scope.answers === $scope.imageCompletionExercise.tagCircles.length) {
                var errors = 0;
                $scope.imageCompletionExercise.tagCircles.forEach(function(circle, index, array) {
                    if (circle.state == $scope.TagCircleState.WRONG) {
                        errors++;
                    }
                });

                if (errors === 0) {
                    return $scope.ResultType.scoreFromResultType($scope.ResultType.SOLVED);
                } else if (errors <= $scope.tolerance) {

                    return $scope.ResultType.scoreFromResultType($scope.ResultType.SOLVED_WITH_ERROR);
                } else {
                    return $scope.ResultType.scoreFromResultType($scope.ResultType.NOT_SOLVED);
                }
            } else {
                return $scope.ResultType.scoreFromResultType($scope.ResultType.INCOMPLETE);
            }
        };

        $scope.endExercise = function(result) {
            var score = result.score;
            var passed = false;
            switch (result.type) {
                case $scope.ResultType.SOLVED:
                    passed = true;
                    // score = $scope.imageCompletionExercise.totalScore;

                    // $scope.currentModal = '#exerciseSolvedDialog';
                    // $($scope.currentModal).modal('show');
                    break;
                case $scope.ResultType.SOLVED_WITH_ERROR:
                    passed = true;
                    // score = $scope.imageCompletionExercise.totalScore / 2;

                    // $scope.currentModal = '#exerciseSolvedWithErrorsDialog';
                    // $($scope.currentModal).modal('show');
                    break;
                case $scope.ResultType.NOT_SOLVED:
                case $scope.ResultType.INCOMPLETE:
                    passed = false;
                    // score = 0;

                    // $scope.currentModal = '#exerciseFailedDialog';
                    // $($scope.currentModal).modal('show');
                    break;
            }
            if ($scope.enrollment){
              CourseLevelSessionAddScore.update({
                  id: $scope.currentCourseLevelSession.id,
                  points: score,
                  experience: score,
              },function(session){
                $scope.activityTrace.endDate = new Date(Date.now());
                $scope.activityTrace.score = score;
                $scope.activityTrace.passed = passed;
                // if (!isFinite( $scope.activityTrace.activity)){
                //   $scope.activityTrace.activity = $scope.activityTrace.activity.id; // BUG: loop de IDs en el json por activity
                // }
                ActivityTrace.end($scope.activityTrace, function(trace){
                  console.log("ended trace" + trace);
                });
                $state.go("imageCompletionExerciseResult", {
                   enrollment: $scope.enrollment,
                   won: passed,
                   message: result.message,
                   courseLevelSession: session
                });
              });

            } else {
    					console.log("endGame NO ENROLLMENT");
    				}

        };

        $scope.cancelQuit = function() {
            $($scope.currentModal).modal('hide');
        };

        $scope.quitExercise = function() {
            $scope.currentModal = '#exitExerciseConfirmation';
            $($scope.currentModal).modal('show');
        };

        $scope.confirmQuit = function() {
            $($scope.currentModal).on('hidden.bs.modal', function() {
                $scope.playerLost();
                $scope.unregisterQuit();
                $state.go($scope.quitEvent.targetScope.toState);

            })
            $($scope.currentModal).modal('hide');
        };

        $scope.playerLost = function() {
            $scope.endExercise($scope.ResultType.scoreFromResultType($scope.ResultType.INCOMPLETE));
        };

        $scope.keepLearning = function() {
            // if ($scope.enrollment){
            //   $($scope.currentModal).on('hidden.bs.modal', function() {
            //       Activity.next({won: true}, function(transition){
            //           ActivityRouter.performTransition($scope.enrollment, transition);
            //       });
            //   });
            // }else {
            //   $scope.reload();
            // }

        };

        $scope.clear = function (){
            $scope.imageCompletionExercise.tagCircle.forEach(function(element,index,array){
                element.state= $scope.TagCircleState.PLAIN;
            });
            $scope.answers = 0;
            $scope.activeTab = 0;

        }
        $scope.reload = function() {
            $scope.clear();
            $scope.refresh();
        };
    });
