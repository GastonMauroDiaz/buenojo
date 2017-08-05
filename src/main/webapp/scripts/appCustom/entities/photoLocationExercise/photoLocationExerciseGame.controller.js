'use strict';

angular.module('buenOjoApp')
    .controller('PhotoLocationExerciseGameController', function($timeout, $scope, $state, $stateParams, entity, BuenOjoUtils, BuenOjoMathUtils, PhotoLocationExercise, PhotoLocationExerciseGame, PhotoLocationExtraImageExercise, PhotoLocationExtraSatelliteImageExercise, Activity, ActivityTrace, Enrollment, CourseLevelSessionFiltered,CourseLevelSessionAddScore) {
        $scope.Constants = Object.freeze({ "satellitePanel" : { "width": 395,
                                                              "height": 395},
                                          "photoPanel" : {"width": 615,
                                                          "height": 415},
                                          "satelliteImageSize": {"width": 1024,
                                                        "height": 1024}
                                          });

        $scope.sightImages = Object.freeze([
            'yellow-sight',
            'blue-sight',
            'black-sight',
            'red-sight',
            'green-sight',
            'violet-sight'
        ]);
        $scope.ResultType = Object.freeze({
            SOLVED: 0,
            SOLVED_WITH_ERROR: 1,
            NOT_SOLVED: 2,

            messages: ['Resuelto correctamente',
                'Resuelto con fallos',
                'No Resuelto',
            ],
        });

        $scope.PhotoLocationState = Object.freeze({
            PAIR_IMAGE: 0,
            PAIR_COMPLETED: 1,
            PHOTO_LOCATION: 2,
            DEFEAT: 3,
            PAIR_IMAGE_DEFEAT: 4,

            getName: function(state) {
                var names = ["¡Encontrá el par!", "¡Encontrá el par!",
                    'Dado un objeto delimitado en la imagen, encontrarlo en la foto y viceversa.',
                    'Dado un objeto delimitado en la imagen, encontrarlo en la foto y viceversa.',
                    'Dado un objeto delimitado en la imagen, encontrarlo en la foto y viceversa.'
                ];
                return names[state];
            }
        });

        $scope.showScales = false;
        $('#carousel-photo-location').on('slide.bs.carousel', function(evt) {
            var photoId = evt.relatedTarget.id;
            $scope.currentTerrainPhoto = BuenOjoUtils.numberFromID(photoId);

        });

        $('#carousel-photo-location-2').on('slide.bs.carousel', function(evt) {
            var photoId = evt.relatedTarget.id;
            $scope.currentSatelliteImage = BuenOjoUtils.numberFromID(photoId);
            $timeout(function() {
                $scope.copyrightImage = $scope.exercise.satelliteSlides[$scope.currentSatelliteImage].copyright;
            }, 10);
        });

        $scope.enrollment = $state.params.enrollment;
        $scope.activity = $state.params.activity;


        $scope.bail = function(state) {

            $scope.bailCount--;
            var halfRemainingSeconds = $scope.remainingTime / 2;
      //      $scope.$broadcast('timer-add-cd-seconds', -halfRemainingSeconds);

            // $scope.$broadcast('timer-stop', -halfRemainingSeconds);
            $scope.$broadcast('timer-set-countdown', halfRemainingSeconds);
            if (state === $scope.PhotoLocationState.PAIR_IMAGE) {
                $scope.nextState(true);
            } else if (state === $scope.PhotoLocationState.PHOTO_LOCATION) {
                // $timeout(function() {
                    $scope.cameraDropped($scope.exercise.beacon.x, $scope.exercise.beacon.y);
                    $scope.exercise.beacon.ok = true;
                    $scope.exercise.beacon.showBadge = true;
                    $scope.hideCameraControl = true;
                // }, 10);
            }
        };

        $scope.nextState = function(success) {
            switch ($scope.currentState) {
                case $scope.PhotoLocationState.PAIR_IMAGE:

                    $scope.showPairingResult = true;
                    $scope.pairResultOk = success;
                    if (success) {

                        var terrainIndex = BuenOjoUtils.findIndexInArray($scope.exercise.terrainSlides, function(element, index, array) {
                            return $scope.exercise.terrainPhoto.id === element.id;
                        });

                        var correctImage = $scope.exercise.satelliteImages.find(function(image) {
                            return image.imageType !== 'EsriTopo';
                        });
                        var satelliteIndex = BuenOjoUtils.findIndexInArray($scope.exercise.satelliteSlides, function(element, index, array) {
                            return correctImage.id === element.id;
                        });

                        $('#carousel-photo-location').carousel(terrainIndex)
                        $('#carousel-photo-location-2').carousel(satelliteIndex)
                        $timeout(function() {
                            $scope.currentState = $scope.PhotoLocationState.PHOTO_LOCATION;
                            $scope.$broadcast('timer-resume');
                            $scope.consigna = $scope.PhotoLocationState.getName($scope.currentState);
                        }, 3000);
                    } else {
                        $scope.PhotoLocationState.PAIR_IMAGE_DEFEAT;
                    }
                    break;
                case $scope.PhotoLocationState.PAIR_COMPLETED:
                    $timeout(function() {
                        $scope.consigna = $scope.PhotoLocationState.getName($scope.currentState)
                    }, 10);

                    break;
                default:
                    break;
            }

        };

        $scope.checkPair = function() {
            $scope.$broadcast('timer-stop');
            var terrainPhoto = $scope.exercise.terrainSlides[$scope.currentTerrainPhoto];
            var satelliteImage = $scope.exercise.satelliteSlides[$scope.currentSatelliteImage];
            var satelliteImagePairCorrect = $scope.exercise.satelliteImages.find(function(image) {
                return image.id === satelliteImage.id;
            });
            if (satelliteImagePairCorrect && $scope.exercise.terrainPhoto.id === terrainPhoto.id) {
                console.log("win pairing");
                $scope.nextState(true);
            } else {
                console.log("lose pairing");
                $scope.nextState(false);
                $timeout($scope.lose, 2000);
            }
        };

        $scope.checkMarks = function() {
            var correctAnswers = 0;
            var possibleAswers = $scope.exercise.sightPairs.length + 1;

            $timeout(function() {
                $scope.exercise.sightPairs.forEach(function(sight, index, array) {
                    if (sight.satelliteSightCSSStyle) {
                        var ok = $scope.sightOnTarget(sight);
                        sight.ok = ok;
                        sight.showBadge = true;
                        if (ok) {
                            correctAnswers++;
                        }
                        sight.satelliteSightCSSStyle['z-index'] = 2; // sight stay in the way if placed in the middle of the image
                    }

                });
            }, 10);
            var beaconOK = false;
            if ($scope.exercise.beacon.beaconCSSStyle) {

                beaconOK = $scope.checkBeacon();
            }
            var score = $scope.scoreForAnswers(correctAnswers, possibleAswers, beaconOK);

            $scope.endExercise(score);
        };
        $scope.sightCenter = function(sight) {
            var element = $('#pl-sight-terrain-0')[0];
            var sightWidth = element.clientWidth;
            var sightHeight = element.clientHeight;
            return {
                x: sight.satelliteSightCSSStyle.left+(sightWidth/2),
                y: sight.satelliteSightCSSStyle.top +(sightHeight/2)
            };
        };

        $scope.checkBeacon = function() {
            var beaconOK = $scope.beaconOnTarget($scope.exercise.beacon.beaconCSSStyle.left, $scope.exercise.beacon.beaconCSSStyle.top, $scope.exercise.beacon);
            $timeout(function() {
                $scope.exercise.beacon.ok = beaconOK
                $scope.exercise.beacon.showBadge = true;
            }, 10);
            return beaconOK;
        };

        $scope.resultForScore = function(score) {
            if (score === 0) {
                return $scope.ResultType.NOT_SOLVED;
            } else if (score < $scope.exercise.totalScore) {
                return $scope.ResultType.SOLVED_WITH_ERROR;
            }
            return $scope.ResultType.SOLVED;
        };

        $scope.scoreForAnswers = function(correctAnswers, possibleAswers, beaconOK) {
            // score info on: https://docs.google.com/document/d/1JRoEOM1_6hDQyDwrGejxddn1zTaWyv9mnuLjo3zH5DU
            var score = 0;
            var correctAnswerRatio = correctAnswers / possibleAswers;
            var allSightsOK = correctAnswers === possibleAswers;
            if ($scope.bailCount > 0) {

                if (beaconOK) {
                    if (allSightsOK) {
                        score = $scope.exercise.totalScore;
                    } else {

                        if (correctAnswerRatio > 0.75) {
                            score = $scope.exercise.totalScore * correctAnswerRatio;
                        } else {
                            score = 0;
                        }
                    }

                }
            } else { // used bail
                if (allSightsOK) {
                    score = $scope.exercise.totalScore / 2;
                } else {
                    if (correctAnswerRatio > 0.75) {

                        score = ($scope.exercise.totalScore / 2) * correctAnswerRatio;
                    } else {
                        score = 0;
                    }
                }
            }
            return score;
        };

        $scope.droppableOnTarget = function(droppableX, droppableY, targetX, targetY, tolerance) {
            var d = BuenOjoMathUtils.distance(droppableX, droppableY, targetX, targetY) <= tolerance;
            return d;
        };

        $scope.sightOnTarget = function(sight) {
            var sightCenter = $scope.sightCenter(sight);
            return $scope.droppableOnTarget(sightCenter.x, sightCenter.y, sight.satelliteX, sight.satelliteY, sight.satelliteTolerance);
        };

        $scope.beaconOnTarget = function(x, y, beacon) {
            return $scope.droppableOnTarget(x, y, beacon.x, beacon.y, beacon.tolerance);
        };

        $scope.lose = function() {
            $scope.endExercise(0);
        };

        $scope.accept = function() {
            switch ($scope.currentState) {
                case $scope.PhotoLocationState.PAIR_IMAGE:
                    $scope.checkPair();

                    break;
                case $scope.PhotoLocationState.PAIR_COMPLETED:
                    $scope.nextState(true);
                    break;
                case $scope.PhotoLocationState.PHOTO_LOCATION:
                    $scope.checkMarks();

                    break;
                default:
                    break;

            }
        };

        /// load

        $scope.showCountdown = true;
        $scope.showScales = true;
        $scope.imgLoaded = function(){

              $scope.scaleResources();

              $scope.$on('timer-tick', function(event, args) {
                  $scope.remainingTime = args['millis'] / 1000;
              });

                $scope.$broadcast('timer-start');

        };



        $scope.scaleResources = function() {
            $timeout(function() {
                // var photoPanel = $scope.currentState === $scope.PhotoLocationState.PHOTO_LOCATION? $('.pl-photo-panel')[1]:$('.pl-photo-panel')[0];
                var scaledTerrainImageWidth = $scope.Constants.photoPanel.width;
                // var satellitePanel =  $('.pl-satellite-image-panel')[0];

                // var satelliteImageElement =  $('#satelliteImage-0')[0];
                var scaledSatelliteWidth = $scope.Constants.satellitePanel.width;
                var scaledSatelliteHeight =  $scope.Constants.satellitePanel.height;
                var scaledTerrainImageHeight = $scope.Constants.photoPanel.height;
                var satelliteImageScaleW = scaledSatelliteWidth / $scope.Constants.satelliteImageSize.width;
                var satelliteImageScaleH = scaledSatelliteHeight / $scope.Constants.satelliteImageSize.height;

                $scope.exercise.beacon.x = satelliteImageScaleW <= 1 && satelliteImageScaleW > 0 ? $scope.exercise.beacon.x * satelliteImageScaleW : $scope.exercise.beacon.x / satelliteImageScaleW;
                $scope.exercise.beacon.y = satelliteImageScaleH <= 1 && satelliteImageScaleH > 0 ? $scope.exercise.beacon.y * satelliteImageScaleH : $scope.exercise.beacon.y / satelliteImageScaleH;
                $scope.exercise.beacon.tolerance = $scope.exercise.beacon.tolerance * satelliteImageScaleW;

                $scope.exercise.satelliteSlides.forEach(function(element, index, array) {

                     $scope.setScaleProperties(element);
                });
                $scope.exercise.satelliteImages.forEach(function(element, index, array){
                  $scope.setScaleProperties(element);
                });
                var terrainImage =  $('#terrainImage')[0];
                $scope.exercise.sightPairs.forEach(function(element, index, array) {
                    element.satelliteX = (satelliteImageScaleW > 0 && satelliteImageScaleW <= 1) ? element.satelliteX * satelliteImageScaleW : element.satelliteX / satelliteImageScaleW;
                    element.satelliteY = (satelliteImageScaleH > 0 && satelliteImageScaleH <= 1) ? element.satelliteY * satelliteImageScaleH : element.satelliteY / satelliteImageScaleH;
                    var centerScale = (satelliteImageScaleH > satelliteImageScaleW) ? satelliteImageScaleH : satelliteImageScaleW;
                    element.tolerance = (centerScale > 0 && centerScale <= 1) ? centerScale * element.tolerance : centerScale * element.tolerance;
                    var imageScaleW = scaledTerrainImageWidth / terrainImage.naturalWidth;
                    var imageScaleH = scaledTerrainImageHeight / terrainImage.naturalHeight;
                    element.terrainX = element.terrainX * imageScaleW;
                    element.terrainY = element.terrainY * imageScaleH;
                    element.ok = false;

                    var terrainSightCSSStyle = {
                        left: (element.terrainX-15),
                        top: (element.terrainY-15)

                    };
                    element.terrainSightCSSStyle = terrainSightCSSStyle;
                    // FIX: saco mouse held por pedido del usuario
                    // $(("#pl-sight-terrain-" + index)).bind('mouseheld', function(e) {
                    //     $timeout(function() {
                    //         $scope.sightClicked(element);
                    //
                    //     }, 10);
                    // });
                });
            }, 10);
        };

        $scope.setScaleProperties = function(element) {
          var satelliteImage = element.satelliteImage;
          var units = satelliteImage.meters / 4;
          var figures = BuenOjoMathUtils.getNumberFigures(units);

          var roundedUnits = BuenOjoMathUtils.roundInt(units, figures - 1);
          var res = satelliteImage.meters / $scope.Constants.satellitePanel.width;
          var properties = {
              resolution: res,
              scaleHeight: 7,
              segments: 4,
              scaleUnits: roundedUnits,
              unit: 'm',
              lineWidth: 0.5,
              textPadding: -10,
              textFont: '5pt Arial',
              color: 'black'
          };

          if (element.scaleProperties) {
              element.scaleProperties.resolution = properties.resolution;
              element.scaleProperties.scaleHeight = properties.scaleHeight;
              element.scaleProperties.segments = properties.segments;
              element.scaleProperties.scaleUnits = properties.scaleUnits;
              element.scaleProperties.unit = properties.unit;
          } else {
              element.scaleProperties = properties;
          }
        };


        // NO BORRAR:
        // $scope.scaleResources = function() {
        //     $timeout(function() {
        //         var photoPanel = $scope.currentState === $scope.PhotoLocationState.PHOTO_LOCATION? $('.pl-photo-panel')[1]:$('.pl-photo-panel')[0];
        //         var scaledTerrainImageWidth = photoPanel.clientWidth;
        //         var satellitePanel =  $('.pl-satellite-image-panel')[0];
        //
        //         var satelliteImageElement =  $('#satelliteImage-0')[0];
        //         var scaledSatelliteWidth = satelliteImageElement.clientWidth;
        //         var scaledSatelliteHeight = satelliteImageElement.clientWidth;
        //         var scaledTerrainImageHeight = photoPanel.clientHeight;
        //         var satelliteImageScaleW = scaledSatelliteWidth / satelliteImageElement.naturalWidth;
        //         var satelliteImageScaleH = scaledSatelliteHeight / satelliteImageElement.naturalHeight;
        //
        //         $scope.exercise.beacon.x = satelliteImageScaleW <= 1 && satelliteImageScaleW > 0 ? $scope.exercise.beacon.x * satelliteImageScaleW : $scope.exercise.beacon.x / satelliteImageScaleW;
        //         $scope.exercise.beacon.y = satelliteImageScaleH <= 1 && satelliteImageScaleH > 0 ? $scope.exercise.beacon.y * satelliteImageScaleH : $scope.exercise.beacon.y / satelliteImageScaleH;
        //         $scope.exercise.beacon.tolerance = $scope.exercise.beacon.tolerance * satelliteImageScaleW;
        //
        //         $scope.exercise.satelliteSlides.forEach(function(element, index, array) {
        //
        //              $scope.setScaleProperties(element);
        //         });
        //         $scope.exercise.satelliteImages.forEach(function(element, index, array){
        //           $scope.setScaleProperties(element);
        //         });
        //         var terrainImage =  $('#terrainImage')[0];
        //         $scope.exercise.sightPairs.forEach(function(element, index, array) {
        //             element.satelliteX = (satelliteImageScaleW > 0 && satelliteImageScaleW <= 1) ? element.satelliteX * satelliteImageScaleW : element.satelliteX / satelliteImageScaleW;
        //             element.satelliteY = (satelliteImageScaleH > 0 && satelliteImageScaleH <= 1) ? element.satelliteY * satelliteImageScaleH : element.satelliteY / satelliteImageScaleH;
        //             var centerScale = (satelliteImageScaleH > satelliteImageScaleW) ? satelliteImageScaleH : satelliteImageScaleW;
        //             element.tolerance = (centerScale > 0 && centerScale <= 1) ? centerScale * element.tolerance : centerScale * element.tolerance;
        //             var imageScaleW = scaledTerrainImageWidth / terrainImage.naturalWidth;
        //             var imageScaleH = scaledTerrainImageHeight / terrainImage.naturalHeight;
        //             element.terrainX = element.terrainX * imageScaleW;
        //             element.terrainY = element.terrainY * imageScaleH;
        //             element.ok = false;
        //
        //             var terrainSightCSSStyle = {
        //                 left: element.terrainX,
        //                 top: element.terrainY
        //
        //             };
        //             element.terrainSightCSSStyle = terrainSightCSSStyle;
        //             // FIX: saco mouse held por pedido del usuario
        //             // $(("#pl-sight-terrain-" + index)).bind('mouseheld', function(e) {
        //             //     $timeout(function() {
        //             //         $scope.sightClicked(element);
        //             //
        //             //     }, 10);
        //             // });
        //         });
        //     }, 10);
        // };
        // $scope.setScaleProperties = function(element) {
        //   var satelliteImage = element.satelliteImage;
        //   var units = satelliteImage.meters / 4;
        //   var figures = BuenOjoMathUtils.getNumberFigures(units);
        //
        //   var roundedUnits = BuenOjoMathUtils.roundInt(units, figures - 1);
        //   var res = satelliteImage.meters / $('#pl-sight-satellite-overlay')[0].clientWidth;
        //   var properties = {
        //       resolution: res,
        //       scaleHeight: 7,
        //       segments: 4,
        //       scaleUnits: roundedUnits,
        //       unit: 'm',
        //       lineWidth: 0.5,
        //       textPadding: -10,
        //       textFont: '5pt Arial',
        //       color: 'black'
        //   };
        //
        //   if (element.scaleProperties) {
        //       element.scaleProperties.resolution = properties.resolution;
        //       element.scaleProperties.scaleHeight = properties.scaleHeight;
        //       element.scaleProperties.segments = properties.segments;
        //       element.scaleProperties.scaleUnits = properties.scaleUnits;
        //       element.scaleProperties.unit = properties.unit;
        //   } else {
        //       element.scaleProperties = properties;
        //   }
        // };

        $scope.load = function(id) {
            PhotoLocationExerciseGame.get({
                id: id
            }, function(result) {
                $scope.exercise = result;

                BuenOjoMathUtils.shuffle($scope.exercise.terrainSlides);
                BuenOjoMathUtils.shuffle($scope.exercise.satelliteSlides);
                $timeout(function() {
                    $scope.countdown = result.totalTimeInSeconds;


                    if ($scope.enrollment) {  // PARA PODER JUGAR SIN ENROLLMENT Y PROBAR JUEGOS
                      // CourseLevelSessionFiltered.query({courseId: $scope.exercise.course.id},function(sessions){
                      //    $scope.currentCourseLevelSession = sessions[0];
                      // });
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
                    $scope.currentState = $scope.PhotoLocationState.PAIR_IMAGE;
                    // $scope.currentState = $scope.PhotoLocationState.PHOTO_LOCATION; // force photolocation
                    $scope.currentTerrainPhoto = 0;
                    $scope.currentSatelliteImage = 0;
                    $scope.bailCount = 1;
                    $scope.consigna = $scope.PhotoLocationState.getName($scope.currentState);

                    $scope.acceptBtnMsg = "FINALIZAR";
                }, 10);
            });

        };
        $scope.load($stateParams.id);


        // $scope.scaleSatelliteResources = function() {
        //     var imagePanel = $scope.currentState === $scope.PhotoLocationState.PHOTO_LOCATION?$('.pl-satellite-image-panel')[1]:$('.pl-satellite-image-panel')[0];
        //     var realWidth = imagePanel.clientWidth;
        //
        //     var satelliteImageScaleW = realWidth / $('#satelliteImage-0')[0].naturalWidth;
        //     var satelliteImageScaleH = realWidth / $('#satelliteImage-0')[0].naturalHeight;
        //
        //     $scope.exercise.sightPairs.forEach(function(element, index, array) {
        //         element.satelliteX = (satelliteImageScaleW > 0 && satelliteImageScaleW <= 1) ? element.satelliteX * satelliteImageScaleW : element.satelliteX / satelliteImageScaleW;
        //         element.satelliteY = (satelliteImageScaleH > 0 && satelliteImageScaleH <= 1) ? element.satelliteY * satelliteImageScaleH : element.satelliteY / satelliteImageScaleH;
        //         var centerScale = (satelliteImageScaleH > satelliteImageScaleW) ? satelliteImageScaleH : satelliteImageScaleW;
        //         element.tolerance = (centerScale > 0 && centerScale <= 1) ? centerScale * element.tolerance : centerScale * element.tolerance;
        //     });
        //     $scope.exercise.beacon.x = satelliteImageScaleW <= 1 && satelliteImageScaleW > 0 ? $scope.exercise.beacon.x * satelliteImageScaleW : $scope.exercise.beacon.x / satelliteImageScaleW;
        //     $scope.exercise.beacon.y = satelliteImageScaleH <= 1 && satelliteImageScaleH > 0 ? $scope.exercise.beacon.y * satelliteImageScaleH : $scope.exercise.beacon.y / satelliteImageScaleH;
        //     $scope.exercise.beacon.tolerance = $scope.exercise.beacon.tolerance * satelliteImageScaleW;
        //
        //     $scope.exercise.satelliteSlides.forEach(function(element, index, array) {
        //         $scope.setScaleProperties(element);
        //     });
        //     // $scope.exercise.satelliteImages.forEach(function(element,index,array){
        //     //   $scope.setScaleProperties(element);
        //     // });
        // };
        $scope.currentTabIndex = 0;
        $scope.tabChanged = function(index){
          $scope.currentTabIndex = index;
        };
        // $('#pl-satellite-slides-carrousel').imagesLoaded().done(function(instance) {
        //     console.log('pl-satellite-slides-carrousel loaded');
        //
        //     $timeout(function() {
        //         $scope.scaleSatelliteResources();
        //         $scope.showScales = true;
        //         $scope.$on('timer-tick', function(event, args) {
        //             $scope.remainingTime = args['millis'] / 1000;
        //         });
        //         $scope.$broadcast('timer-start');
        //
        //
        //     }, 1000);
        //
        // });
        // $('#pl-tarrain-slides-carrousel').imagesLoaded().done(function(instance) {
        //     $timeout(function() {
        //         $scope.scaleTerrainResources();
        //     }, 1000);
        // });

        // $scope.scaleTerrainResources = function() {
        //     $scope.exercise.sightPairs.forEach(function(element, index, array) {
        //         var photoPanel = $scope.currentState === $scope.PhotoLocationState.PHOTO_LOCATION? $('.pl-photo-panel')[1]:$('.pl-photo-panel')[0];
        //         var realWidth = photoPanel.clientWidth;
        //         var realHeight = photoPanel.clientHeight;
        //         var imageScaleW = realWidth / $('#terrainImage')[0].naturalWidth;
        //         var imageScaleH = realHeight / $('#terrainImage')[0].naturalHeight;
        //         element.ok = false;
        //         element.terrainX = element.terrainX * imageScaleW;
        //         element.terrainY = element.terrainY * imageScaleH;
        //         var terrainSightCSSStyle = {
        //             left: element.terrainX,
        //             top: element.terrainY
        //
        //         };
        //         element.terrainSightCSSStyle = terrainSightCSSStyle;
        //         // $(("#pl-sight-terrain-" + index)).bind('mouseheld', function(e) {
        //         //     $scope.sightClicked(element);
        //         // });
        //     });
        // };
        // end load

        $scope.sightClicked = function(element) {
          var satellitePanel =  $('#pl-sight-satellite-overlay')[0];
          element.satelliteSightCSSStyle = {

              left: (satellitePanel.clientWidth / 2),
              top: (satellitePanel.clientHeight / 2),
              'z-index': 400
          };
        };


        // DRAG AND DROP
        $scope.sightShouldDrop = function(event, element, sight) {
            return event.target.id === 'pl-sight-satellite-overlay';
        };

        $scope.sightStarted = function(event, element, sight) {};

        $scope.elementDropped = function(event, element, model) {

            if (element.draggable[0].id === 'pl-camera-control') {
                var overlayRect = event.target.getBoundingClientRect();
                var x = event.clientX - overlayRect.left;
                var y = event.clientY - overlayRect.top;
                $scope.cameraDropped(x, y);
            } else {

                $scope.sightDropped(element, element.position.left, element.position.top);
            }
        };

        $scope.sightDropped = function(element, x, y) {
            var sightIndex = BuenOjoUtils.numberFromID(element.draggable[0].id);
            var sight = $scope.exercise.sightPairs[sightIndex];
            sight.satelliteSightCSSStyle.left = x;
            sight.satelliteSightCSSStyle.top = y;
            sight.satelliteSightCSSStyle['z-index'] = 2;
        };

        $scope.cameraDropped = function(x, y) {
            $timeout(function() {
                $scope.exercise.beacon.beaconCSSStyle = {
                    left: x-15,
                    top: y-11
                };
            }, 10);
        };

        // END EXERCISE
        $scope.timeUp = function() {
            $scope.checkMarks();
        };

        $scope.endExercise = function(score) {
            var result = $scope.resultForScore(score);
            $scope.bailCount = 0;
            $scope.finalScore = score;
            var passed = false;
            switch (result) {
                case $scope.ResultType.SOLVED:
                    // $scope.currentModal = '#exerciseSolvedDialog';
                    // $($scope.currentModal).modal('show');
                    passed = true;
                    break;
                case $scope.ResultType.SOLVED_WITH_ERROR:
                    // $scope.currentModal = '#exerciseSolvedWithErrorsDialog';
                    // $($scope.currentModal).modal('show');
                    passed = true;

                    break;
                case $scope.ResultType.NOT_SOLVED:
                    // $scope.currentModal = '#exerciseFailedDialog';
                    // $($scope.currentModal).modal('show');
                    passed = false;
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
                if (!isFinite( $scope.activityTrace.activity)){
                  $scope.activityTrace.activity = $scope.activityTrace.activity.id; // BUG: loop de IDs en el json por activity
                }
                ActivityTrace.end($scope.activityTrace, function(trace){
                  console.log("ended trace" + trace);
                });
                $state.go("photoLocationResult", {
                   enrollment: $scope.enrollment,
                   won: passed,
                   message: result.message,
                   courseLevelSession: session
                });
              });

            }

        };

        $scope.quitExercise = function() {
            $('#exitExerciseConfirmation').modal('show');
        };

        $scope.confirmQuit = function() {
            $('#exitExerciseConfirmation').on('hidden.bs.modal', function() {
                $state.go("courseslist", {}, {
                    reload: true
                });
            });
            $('#exitExerciseConfirmation').modal('hide');
        };
        // FIX: no quieren que haya este hold  to click
        // jQuery(function($) {
        //     function startTrigger(e, data) {
        //         var $elem = $(this);
        //         $elem.data('mouseheld_timeout', setTimeout(function() {
        //             $elem.trigger('mouseheld');
        //         }, e.data));
        //     }
        //
        //     function stopTrigger() {
        //         var $elem = $(this);
        //         clearTimeout($elem.data('mouseheld_timeout'));
        //     }
        //     var mouseheld = $.event.special.mouseheld = {
        //         setup: function(data) {
        //             var $this = $(this);
        //             $this.bind('mousedown', +data || mouseheld.time, startTrigger);
        //             $this.bind('mouseleave mouseup', stopTrigger);
        //         },
        //         teardown: function() {
        //             var $this = $(this);
        //             $this.unbind('mousedown', startTrigger);
        //             $this.unbind('mouseleave mouseup', stopTrigger);
        //         },
        //         time: 750 // default to 750ms
        //     };
        // });
    });
