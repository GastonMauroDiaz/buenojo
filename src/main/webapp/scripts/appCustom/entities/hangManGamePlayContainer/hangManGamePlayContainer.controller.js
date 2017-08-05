'use strict';

angular
    .module('buenOjoApp')
    .controller(
        'HangManGamePlayContainerController',
        function($scope, $state, $timeout, $stateParams, $modal,
            HangManGameContainer,
            HangManExerciseImageResourceByContainer,
            HangManExerciseByContainer,
            HangManExerciseOptionByContainer,
            HangManExerciseHintByContainer,
            HangManExerciseDelimitedAreaByContainer,
            HangManExerciseIsCorrect,
            HangManExerciseWhichAreIncorrect,
						CourseLevelSessionFiltered,
						Enrollment,
						ActivityTrace,
						CourseLevelSessionAddScore
						) {

            $scope.checkboxModel = [];
            $scope.incorrectPool = [];
            $scope.preIncorrectPool = [];
            $scope.failedAttempt = [];
            $scope.successAttempt = [];
            $scope.correctPool = [];
            $scope.preCorrectPool = [];
            $scope.hangManExercises = [];
            $scope.hangManExercisesImages = [];
            $scope.hangManExercisesImagesHighLight = [];
            $scope.hangManExerciseDelimitedArea = [];
            $scope.optionClass = [];
            $scope.errorCount = 0;
            $scope.wait = false;
            $scope.wellCompleted = false;
            $scope.hangManExercisesHints = [];
            $scope.stop = false;
            $scope.lastHintShown = '';
            $scope.lastEnabled = 1;
            $scope.errorFactor = 2;
            $scope.maxErrorTolerance = 7;
            $scope.maxScore = 14;

            $scope.getScore = function() {
                if ($scope.errorCount >= $scope.maxErrorTolerance) return 0;

                if ($scope.errorCount == 0) return $scope.maxScore;

                return $scope.maxScore - ($scope.errorFactor * $scope.errorCount);

            };

            $scope.refresh = function() {
                $scope.loadAllExercises($stateParams.id);
                $scope.clear();
            };

            $scope.incrementError = function() {
                $scope.errorCount++;
                $scope.vibrateHangMan();
                
                if ($scope.errorCount >= $scope.maxErrorTolerance)
                    $scope.endGame();
            };

            $scope.enrollment = $stateParams.enrollment;
            $scope.activity = $stateParams.activity;
            $scope.loadAllExercises = function(id) {
                if ($scope.enrollment) {
                    CourseLevelSessionFiltered.current({
                        courseId: $scope.enrollment.course.id
                    }, function(session) {
                        $scope.currentCourseLevelSession = session;
                    });

                    if ($scope.activity) {
                        ActivityTrace.start({
                            activityId: $scope.activity.id
                        }, function(trace) {
                            $scope.activityTrace = trace;

                            // FORZAR PERDER
                            // $timeout(function() {
                            //     $scope.errorCount = 10;
                            //     $scope.endGame();
                            // }, 1000);

                            // FORZAR GANAR
                            // $timeout(function() {
                            //     $scope.errorCount = 0;
                            //     $scope.endGame();
                            // }, 1000);
                        });
                    }
                } else {
                    console.log("NO ENROLLMENT");
                }

                HangManExerciseByContainer.query({
                    id: id
                }, function(result) {
                    $scope.hangManExercises = result;
                    $scope.loadAllExercisesImages($stateParams.id);

                });
            };

            $scope.getStyle = function(me) {
                /*	if (me>$scope.lastEnabled)
                		{
                			return {
                				color : 'red',
                				'text-decoration' : 'none',
                				'border-bottom' : '4px solid #151F46'
                			}
                		}
                		else*/
                if (me == $scope.questionSelected)
                    return {
                        color: '#151F46',
                        'text-decoration': 'none',
                        'border-bottom': '4px solid #151F46'
                    }
                else
                    return {
                        'text-decoration': 'none',
                        'border-bottom': '4px solid #A7A7A6',
                        color: '#A7A7A6'
                    };
            };

            $scope.getStyleHint = function(me) {
                if (me == $scope.hintSelected)
                    return {
                        color: '#151F46',
                        'text-decoration': 'none',
                        'border-bottom': '4px solid #151F46'
                    }
                else
                    return {
                        'text-decoration': 'none',
                        'border-bottom': '4px solid #A7A7A6',
                        color: '#A7A7A6'
                    };
            };

            $scope.getExerciseIdByOrder = function(order) {
                if (order < 1 || order > $scope.hangManExercises.length) {
                    alert('Orden incorrecto:' + order);
                    return;
                }
                var i;
                for (i = 0;
                    (i < $scope.hangManExercises.length && $scope.hangManExercises[i].exerciseOrder != order); i++)
                ;
                return $scope.hangManExercises[i].id;
            };

            $scope.getHangManExerciseHint = function(exercise) {
                if ($scope.hangManExercisesHints[exercise] == undefined)
                    $scope.hangManExercisesHints[exercise] = [];
                return $scope.hangManExercisesHints[exercise];

            };

            $scope.getClockColor = function() {
                if ($scope.stop)
                    return {
                        'color': 'red'
                    };
                else
                    return {
                        'color': 'rgb(31, 37, 86)'
                    };
            };

            $scope.exerciseIdExists = function(id) {
                var i;
                for (i = 0;
                    (i < $scope.hangManExercises.length && $scope.hangManExercises[i].id != id); i++)
                ;
                return ($scope.hangManExercises[i].id == id);
            };

            $scope.showHint = function(el) {
                $(el).popover('show');
                /*					if ($scope.lastHintShown!='')
                			$scope.hideHint($scope.lastHintShown);

                		$scope.lastHintShown=el;
                 */
            };

            $scope.hideHint = function(el) {
                $(el).popover('hide');
                /*	$(el).mouseleave();
                		$scope.lastHintShown='';
                 */
            };

            $scope.getHintsByExerciseId = function(exerciseId) {
                if (!$scope.exerciseIdExists(exerciseId)) {
                    alert('No existe ejercicio:' + exerciseId);
                    return false;
                }
                return $scope.hangManExercisesHints[exerciseId];
            };

            $scope.getReferenceHintsByExerciseId = function(exerciseId) {
                if (!$scope.exerciseIdExists(exerciseId)) {
                    alert('No existe ejercicio:' + exerciseId);
                    return false;
                }
                return 'hangManExercisesHints[' + exerciseId + ']';
            };

            $scope.getHintsNumbers = function(exerciseId) {
                var hints;
                hints = $scope.getHintsByExerciseId(exerciseId);

                if (hints != false && hints != undefined) {
                    var hintNumber = [];

                    for (var i = 0; i < (hints.length - 1); i++) {

                        hintNumber.push(i + 1);
                    }
                    return hintNumber;
                }
            };

            $scope.pickRandomHint = function() {
                var hints;
                var exerciseId = $scope
                    .getExerciseIdByOrder($scope.questionSelected);
                hints = $scope.getHintsByExerciseId(exerciseId);
                if (hints != false && hints != undefined &&
                    hints.length > 0) {
                    var hintNumber = [];
                    var random = Math.floor(Math.random() *
                        (hints.length - 1)) + 1;
                    hints[random].visible = true;
                }
                $scope.changeHintSelected(random);
            };

            $scope.getHintsNumbersByExerciseOrder = function(order) {
                return $scope.getHintsNumbers($scope
                    .getExerciseIdByOrder(order));
            };

            $scope.loadAllExercisesImages = function(id) {

                HangManExerciseImageResourceByContainer
                    .query({
                            id: id
                        },
                        function(result) {
                             for (var i = 0; i < result.length; i++) {
                                if (result[i][1]
                                    .indexOf('arearesaltada') > -1)
                                    $scope.hangManExercisesImagesHighLight[result[i][0]] =
                                    result[i][1];
                                else
                                    $scope.hangManExercisesImages[result[i][0]] =
                                    result[i][1];
                            }
                            for (var i = 0; i < $scope.hangManExercises.length; i++) {
                                var last;
                                if ($scope.hangManExercisesImages[$scope.hangManExercises[i].id] == undefined) {
                                    if (last == null)
                                        alert('No hay imagen previa');
                                    else
                                        $scope.hangManExercisesImages[$scope.hangManExercises[i].id] = last;
                                } else

                                    last = $scope.hangManExercisesImages[$scope.hangManExercises[i].id];
                            }
                            $scope
                                .loadAllExercisesHints($stateParams.id);
                        });

            };

            $scope.loadAllExercisesHints = function(id) {

                HangManExerciseHintByContainer
                    .query({
                            id: id
                        },
                        function(result) {
                            for (var i = 0; i < result.length; i++) {

                                $scope.getHangManExerciseHint(result[i][0])[result[i][2]] = {
                                    text: result[i][1],
                                    x: result[i][3] / 2,
                                    y: result[i][4] / 2,
                                    visible: false
                                }
                            }
                            $scope
                                .loadAllOptionsByContainer($stateParams.id);
                        });

            };

            $scope.loadAllExercises($stateParams.id);

            $scope.changeSelected = function(newValue) {
                if (newValue <= $scope.lastEnabled) {
                    $scope.questionSelected = newValue;
                    $scope.changeHintSelected(0);
                }
            }

            $scope.changeHintSelected = function(newValue) {
                if (newValue != undefined) {
                    $scope.hintSelected = newValue;
                    if (newValue != 0)
                        $timeout(

                            function() {
                                if ($scope.lastHintShown !=
                                    $scope.hangManExercisesHints[
                                        $scope.getExerciseIdByOrder($scope.questionSelected)][newValue].show)
                                    $scope.showHint(
                                        $scope.hangManExercisesHints[$scope
                                            .getExerciseIdByOrder($scope.questionSelected)][newValue].show);
                                else {
                                    $scope.hideHint(
                                        $scope.hangManExercisesHints[$scope
                                            .getExerciseIdByOrder($scope.questionSelected)][newValue].show);
                                }
                            },
                            0);
                    else {
                        /*		if ($scope.lastHintShown!='')
                        			$scope.hideHint($scope.lastHintShown);
                         */
                        $scope.$broadcast("forceHide");
                    }

                }
            }

            $scope.getOptionsByExerciseId = function(exerciseId) {
                if (!$scope.exerciseIdExists(exerciseId)) {
                    alert('No existe ejercicio:' + exerciseId);
                    return false;
                }
                return $scope.hangManExerciseOptions[exerciseId];
            }

            $scope.getOptionNumbers = function(exerciseId) {
                var hints;
                hints = $scope.getOptionsByExerciseId(exerciseId);
                if (hints != false && hints != undefined) {
                    var hintNumber = [];

                    for (var i = 0; i < (hints.length); i++)
                        hintNumber.push(i);
                    return hintNumber;
                }
            };

            $scope.hangManExerciseOptions = [];

            $scope.loadAllOptionsByContainer = function(id) {
                HangManExerciseOptionByContainer
                    .query({
                            id: id
                        },
                        function(result) {
                            for (var i = 0; i < result.length; i++) {
                                var currentOption = {};
                                currentOption.text = result[i][1];
                                currentOption.id = result[i][0];
                                if ($scope.hangManExerciseOptions[result[i][2]] == null)
                                    $scope.hangManExerciseOptions[result[i][2]] = [];
                                $scope.hangManExerciseOptions[result[i][2]]
                                    .push(currentOption);
                            }
                            $scope
                                .loadAllExercisesDelimitedArea($stateParams.id);

                        });
            };

            $scope.finalize = function(exerciseId) {
                var responses = [];

                if ($scope.checkboxModel[exerciseId] != undefined) {
                    for (var property in $scope.checkboxModel[exerciseId].value) {
                        if ($scope.checkboxModel[exerciseId].value
                            .hasOwnProperty(property)) {
                            if ($scope.checkboxModel[exerciseId].value[property] != undefined)
                                if ($scope.checkboxModel[exerciseId].value[property] != "")
                                    responses
                                    .push($scope.checkboxModel[exerciseId].value[property]);

                        }
                    }
                    $scope.preCorrectPool = [];
                    for (var i = 0; i < responses.length; i++)
                        $scope.preCorrectPool.push(exerciseId + "_" +
                            responses[i]);
                    if (responses.length != 0)
                        HangManExerciseIsCorrect
                        .get({
                                id: exerciseId,
                                responses: responses
                                    .toString()
                            },
                            function(result) {
                                if (result[0] == "false") {
                                    $scope.errorCount++;
                                    $scope.vibrateHangMan();
                                    $scope.preCorrectPool = [];
                                    if ($scope.errorCount >= 7)
                                        $scope.endGame();
                                    else
                                        HangManExerciseWhichAreIncorrect
                                        .get({
                                                id: exerciseId,
                                                responses: responses
                                                    .toString()
                                            },
                                            function(
                                                result) {
                                                $scope.preIncorrectPool = [];
                                                for (var i = 0; i < result.length; i++) {

                                                    $scope.preIncorrectPool
                                                        .push(exerciseId +
                                                            "_" +
                                                            result[i]);

                                                    $scope.optionClass[$scope.questionSelected +
                                                        "_" +
                                                        result[i]] = "mal";

                                                    for (var property in $scope.checkboxModel[exerciseId].value) {
                                                        if ($scope.checkboxModel[exerciseId].value
                                                            .hasOwnProperty(property)) {
                                                            if ($scope.checkboxModel[exerciseId].value[property] != undefined)
                                                                if ($scope.checkboxModel[exerciseId].value[property] == result[i]) {
                                                                    $scope.checkboxModel[exerciseId].value[property] = "";
                                                                    break;
                                                                }
                                                        }

                                                    }
                                                }
                                                for (var property in $scope.checkboxModel[exerciseId].value) {
                                                    if ($scope.checkboxModel[exerciseId].value
                                                        .hasOwnProperty(property)) {
                                                        if ($scope.checkboxModel[exerciseId].value[property] != undefined)
                                                            if ($scope.checkboxModel[exerciseId].value[property] != "") {

                                                                $scope.optionClass[$scope.questionSelected +
                                                                    "_" +
                                                                    $scope.checkboxModel[exerciseId].value[property]] = "bien";
                                                            }
                                                    }
                                                }
                                                $scope.pickRandomHint();
                                                $scope.stop = true;
                                                $timeout(
                                                    function() {
                                                        $scope.stop = false;
                                                    },
                                                    5000);
                                                $scope.lastFailed = exerciseId;
                                                $scope.afterFinalizeCallBack = function() {
                                                    $scope.failedAttempt[$scope.lastFailed] = true;
                                                    $scope.incorrectPool = $scope.incorrectPool
                                                        .concat($scope.preIncorrectPool);
                                                    $scope.preIncorrectPool = [];

                                                }
                                            });
                                    $scope.forceWait();
                                } else {
                                    for (var property in $scope.checkboxModel[exerciseId].value) {
                                        if ($scope.checkboxModel[exerciseId].value
                                            .hasOwnProperty(property)) {
                                            if ($scope.checkboxModel[exerciseId].value[property] != undefined)
                                                if ($scope.checkboxModel[exerciseId].value[property] != "") {

                                                    $scope.optionClass[$scope.questionSelected +
                                                        "_" +
                                                        $scope.checkboxModel[exerciseId].value[property]] = "bien";
                                                }
                                        }
                                    }
                                    if ($scope.questionSelected >= ($scope.hangManExercises.length)) {
                                        $scope.wellCompleted = true;
                                        $scope.endGame();
                                    } else {
                                        $scope
                                            .pickRandomHint();
                                        $scope.successAttempt[exerciseId] = true;
                                        $scope.correctPool = $scope.correctPool
                                            .concat($scope.preCorrectPool);
                                        $scope.preCorrectPool = [];

                                        $scope.afterFinalizeCallBack = function() {
                                            $scope.lastEnabled++;
                                            $scope
                                                .changeSelected($scope.questionSelected + 1);
                                        }
                                        $scope.forceWait();
                                    }
                                }
                            });
                } else {
                    alert("Aun no se ingreso ninguna respuesta");
                }
            };

            $scope.loadAllExercisesDelimitedArea = function(id) {

                HangManExerciseDelimitedAreaByContainer
                    .query({
                            id: id
                        },
                        function(result) {
                            for (var i = 0; i < result.length; i++) {
                                $scope.hangManExerciseDelimitedArea[result[i][0]] = {
                                    x: result[i][1] / 2,
                                    y: result[i][2] / 2,
                                    radius: (result[i][3] / 2)
                                }
                            }
                            $scope.changeSelected(1);
                        });
            };

            $scope.clear = function() {
                $scope.hangManGameContainer = {
                    name: null,
                    id: null
                };
            };

            $scope.messageForScore = function(score) {
                if (score === $scope.maxScore) {
                    return "Completaste el ejercicio.\n ¡Felicitaciones!";
                } else if (score > 0 && score < $scope.maxScore) {
                    return "¡Ejercicio resuelto con fallos!";
                }

                return "Ejercicio no resuelto. \n No te desanimes...¡Seguí intentando!";
            };

            $scope.endGame = function() {
                // $('#myModal').modal('show');
                var score = $scope.getScore();
                var passed = score > 0;
                var message = $scope.messageForScore(score);
                if ($scope.enrollment) {
                    CourseLevelSessionAddScore.update({
                        id: $scope.currentCourseLevelSession.id,
                        points: score,
                        experience: score,
                    }, function(session) {
                        $scope.activityTrace.endDate = new Date(Date.now());
                        $scope.activityTrace.score = score;
                        $scope.activityTrace.passed = passed;
                        // if (!isFinite($scope.activityTrace.activity)) {
                        //     $scope.activityTrace.activity = $scope.activityTrace.activity.id; // BUG: loop de IDs en el json por activity
                        // }
                        ActivityTrace.end($scope.activityTrace, function(trace) {
                            console.log("ended trace" + trace);
                        });
                        $state.go("hangManResult", {
                            enrollment: $scope.enrollment,
                            won: passed,
                            message: message,
                            courseLevelSession: $scope.currentCourseLevelSession
                        });
                    });

                } else {
                    console.log("endGame NO ENROLLMENT");
                }
            };

            $scope.vibrateHangMan = function() {
                $('#Capa_1').vibrate({
                    speed: 30, // The time in ms between each rotation
                    duration: 5000, // The whole "animation" duration, you can use "fast" and "slow"
                    spread: 5 // The spread of the animation, beware of huge values (parkinson's style)
                });
            };

            $scope.afterFinalize = function(ev) {
                if ((!$scope.stop) &&
                    ((ev.type == "click") || ([32, 13]
                        .indexOf(ev.keyCode) != -1))) {
                    $scope.wait = false;
                    $scope.afterFinalizeCallBack();
                }

            };

            $scope.forceWait = function() {
                $scope.wait = true;
            };

            $scope.timerRunning = false;

            $scope.startTimer = function() {
                $scope.$broadcast('timer-start');
                $scope.timerRunning = true;
            };

            $scope.stopTimer = function() {
                $scope.$broadcast('timer-stop');
                $scope.timerRunning = false;
            };
            $scope.reload = function() {
              $scope.refresh();
            };

        });
