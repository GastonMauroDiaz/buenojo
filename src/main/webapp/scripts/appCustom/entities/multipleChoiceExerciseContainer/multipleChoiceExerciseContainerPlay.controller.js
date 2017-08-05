'use strict';

angular
    .module('buenOjoApp')
    .controller(
        'MultipleChoiceExerciseContainerPlayController',
        function($timeout, $scope, $state, $modal, $stateParams,
            MultipleChoiceExerciseContainer,
            MultipleChoiceQuestionByContainer,
            MultipleChoiceAnswerByContainer, CourseLevelSessionFiltered, Activity, ActivityTrace) {

            $scope.enrollment = $state.params.enrollment;
            $scope.activity = $state.params.activity;
						$scope.loadCourseLevelSession = function() {
							CourseLevelSessionFiltered.current({
									courseId: $scope.enrollment.course.id
							}, function(session) {
									$scope.currentCourseLevelSession = session;
							});
						};


            $scope.currentExperience = function() {
                return $scope.getGoodCount() * 20;
            }

            $scope.currentPercentage = function() {
                if ($scope.multipleChoiceQuestionSolved.length == 0) return 0;
                else
                    return ($scope.multipleChoiceQuestionSolved.length / $scope.multipleChoiceQuestions.length) * 100;
            }

            $scope.currentCount = function() {
                return $scope.multipleChoiceQuestionSolved.length;
            }

            if ($state.$current.data.withSession == true) { //No hay cursos todavia cuando haya hay que reemplazar el por el course Id
                $scope.loadCourseLevelSession();
            }

            $scope.multipleChoiceExerciseContainers = [];
            $scope.multipleChoiceQuestions = [];
            $scope.multipleChoiceQuestionSolved = [];
            $scope.multipleChoiceAnswers = [];
            $scope.currentQuestion = 0;
            $scope.checkboxModel = [];
            $scope.stop = false;
            $scope.wait = false;
            $scope.stop = false;

            $scope.shuffle = function(array) {
                var currentIndex = array.length,
                    temporaryValue, randomIndex;

                // While there remain elements to shuffle...
                while (0 !== currentIndex) {

                    // Pick a remaining element...
                    randomIndex = Math.floor(Math.random() * currentIndex);
                    currentIndex -= 1;

                    // And swap it with the current element.
                    temporaryValue = array[currentIndex];
                    array[currentIndex] = array[randomIndex];
                    array[randomIndex] = temporaryValue;
                }

                return array;
            }

            $scope.finalizarOSiguiente = function() {
                if ($scope.currentQuestion == $scope.multipleChoiceQuestions.length - 1)
                    return "FINALIZAR";
                else
                    return "SIGUIENTE";

            }
            $scope.getAnswersByQuestion = function(id) {
                var output = [];
                for (var i = 0; i < $scope.multipleChoiceAnswers.length; i++)
                    if ($scope.multipleChoiceAnswers[i].multipleChoiceQuestion.id == id)
                        output.push($scope.multipleChoiceAnswers[i]);

                return output;
            }

            $scope.getGoodCount = function() {
                var output = 0;
                for (var i = 0; i < $scope.multipleChoiceQuestions.length; i++)
                    if ($scope.multipleChoiceQuestions[i].wasCorrect)
                        output++;
                return output;
            }
            $scope.getAnswerIsRight = function(id, idAnswer) {
                for (var i = 0; i < $scope.multipleChoiceAnswers.length; i++)
                    if ($scope.multipleChoiceAnswers[i].multipleChoiceQuestion.id == id)
                        if ($scope.multipleChoiceAnswers[i].id == idAnswer)
                            return ($scope.multipleChoiceAnswers[i].isRight == true);

            }

            $scope.nextQuestion = function() {
                if ($scope.currentQuestion < $scope.multipleChoiceQuestions.length - 1)
                    $scope.currentQuestion++;
                else
                    $scope.endGame();

            }

            $scope.endGame = function() {
                // $('#myModal').modal('show');

                if ($scope.enrollment) {
                  var score =  $scope.getGoodCount();
                  $q.when(CourseLevelSessionAddScore.update({
                      id: $scope.currentCourseLevelSession.id,
                      points: score,
                      experience: score,
                  }).$promise).then(function(session){

                    var passed = score > 0;
                    $scope.activityTrace.endDate = new Date(Date.now());
                    $scope.activityTrace.score = score;
                    $scope.activityTrace.passed = passed;

                    // if (isFinite($scope.activityTrace.activity)) {
                    //     $scope.activityTrace.activity = $scope.activityTrace.activity.id; // BUG: loop de IDs en el json por activity
                    // }
                    ActivityTrace.end($scope.activityTrace, function(trace) {
                        console.log("ended trace" + trace);
                        $state.go("multipleChoiceResult", {
                            enrollment: $scope.enrollment,
                            won: passed,
                            message: $scope.messageForResult($scope.getResults()),
                            courseLevelSession: $scope.currentCourseLevelSession
                        });
                    });


                  });

                }
            };


            $scope.getResults = function() {
                if ($scope.getGoodCount() == $scope.multipleChoiceQuestions.length)
                    return 'Complete';
                else if ($scope.getGoodCount() == 0)
                    return 'Unresolved';
                else if ($scope.getGoodCount() < $scope.multipleChoiceQuestions.length)
                    return 'CompleteWithFailures';

            };

            $scope.messageForResult = function(result) {

                if (result === "CompleteWithFailures") return "Resuelto con fallos";
                if (result === "Unresolved") return "No Resuelto";
                if (result === "Complete") return "Resuelto Completo";
                return ""
            };

            $scope.finalize = function() {
                $scope.multipleChoiceQuestionSolved
                    .push($scope.multipleChoiceQuestions[$scope.currentQuestion].id);
                if ($scope.checkAnswers() == true) {
                    $scope.multipleChoiceQuestions[$scope.currentQuestion].wasCorrect = true;
                    $scope.checkAll();
                    $scope.wait = true;
                    $timeout(function() {
                        $scope.stop = false;
                        $scope.wait = false;
                        $scope.nextQuestion();
                    }, 3000);

                } else {
                    $scope.multipleChoiceQuestions[$scope.currentQuestion].wasCorrect = false;
                    $scope.checkAll();

                    $scope.stop = true;
                    $scope.wait = true;

                    $timeout(function() {
                        $scope.stop = false;
                        $scope.wait = false;
                        $scope.nextQuestion();
                    }, 3000);
                }

            }

            $scope.checkAnswers = function() {
                var output = true;
                var answers = $scope
                    .getAnswersByQuestion($scope.multipleChoiceQuestions[$scope.currentQuestion].id);
                for (var i = 0;
                    (i < answers.length && output == true); i++) {
                    var userChecked = ($scope.checkboxModel[$scope.multipleChoiceQuestions[$scope.currentQuestion].id +
                            '_' + answers[i].id].value != undefined) &&
                        ($scope.checkboxModel[$scope.multipleChoiceQuestions[$scope.currentQuestion].id +
                            '_' + answers[i].id].value == 'checked');
                    output = (userChecked == $scope
                        .getAnswerIsRight(
                            $scope.multipleChoiceQuestions[$scope.currentQuestion].id,
                            answers[i].id));
                }
                return output;
            }

            $scope.checkAll = function() {
                var answers = $scope
                    .getAnswersByQuestion($scope.multipleChoiceQuestions[$scope.currentQuestion].id);
                for (var i = 0;
                    (i < answers.length == true); i++) {
                    $scope.checkboxModel[$scope.multipleChoiceQuestions[$scope.currentQuestion].id +
                        '_' + answers[i].id] = {
                        value: 'checked'
                    }

                }

            }

            $scope.getCheckClass = function(questionId, answerId) {
                if ($.inArray(questionId,
                        $scope.multipleChoiceQuestionSolved) > -1)
                    if ($scope.getAnswerIsRight(questionId, answerId))
                        return 'bien';
                    else
                        return 'mal';
            }

            $scope.loadAnswers = function() {
                MultipleChoiceAnswerByContainer.query({
                    id: $stateParams.id
                }, function(result) {

                    $scope.multipleChoiceAnswers = $scope.shuffle(result);

                });
            }

            $scope.multipleChoiceQuestions = [];
            $scope.loadAll = function() {
                MultipleChoiceQuestionByContainer.query({
                    id: $stateParams.id
                }, function(result) {
                    if ($scope.activity) {
                        ActivityTrace.start({
                            activityId: $scope.activity.id
                        }, function(trace) {
                            $scope.activityTrace = trace;

                            // FORZAR PERDER
                            // $timeout(function () {
                            //   $scope.endGame();
                            // }, 1000);

                            // FORZAR GANAR
                            // $timeout(function () {
                            //   $scope.endExercise($scope.ResultType.scoreFromResultType($scope.ResultType.SOLVED));
                            // }, 1000);
                        });
                    }
                    $scope.multipleChoiceQuestions = result;
                    $scope.loadAnswers();
                });

            };
            $scope.loadAll();

            $scope.getClockColor = function() {
                if ($scope.stop)
                    return {
                        'color': 'red'
                    };
                else
                    return {
                        'color': 'rgb(31, 37, 86)'
                    };
            }

            $scope.refresh = function() {
                $scope.loadAll();
                $scope.clear();
            };

            $scope.clear = function() {
                $scope.multipleChoiceExerciseContainer = {
                    name: null,
                    id: null
                };
            };

            $scope.afterFinalize = function(ev) {
                if ((!$scope.stop) &&
                    ((ev.type == "click") || ([32, 13]
                        .indexOf(ev.keyCode) != -1))) {
                    $scope.wait = false;
                    $scope.afterFinalizeCallback();
                }

            }

            $scope.afterFinalizeCallback = function() {

            }


            $scope.timerRunning = false;

            $scope.startTimer = function() {
                $scope.$broadcast('timer-start');
                $scope.timerRunning = true;
            };

            $scope.stopTimer = function() {
                $scope.$broadcast('timer-stop');
                $scope.timerRunning = false;
            };


        });
