'use strict';

angular.module('buenOjoApp')
    .factory('Activity', function($resource, DateUtils) {
        return $resource('api/activities/:id', {}, {
            'query': {
                method: 'GET',
                isArray: true
            },
            'get': {
                method: 'GET',
                transformResponse: function(data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {
                method: 'PUT'
            },
            'next': {
                method: 'GET',
                url: 'api/activities/:courseId/next/:won'
            },
            'level': {
              method: 'GET',
              url: 'api/activities/level/:levelId',
              isArray: true
            }
        });
    });

angular.module('buenOjoApp')
    .service('ActivityType', function($state) {

        return Object.freeze({
            HangMan: 0,
            MultipleChoice: 1,
            ImageCompletion: 2,
            PhotoLocation: 3,
            INVALID: -1,

            fromName: function(name) {
                if (name == "HangMan") return this.HangMan;
                if (name == "MultipleChoice") return this.MultipleChoice;
                if (name == "ImageCompletion") return this.ImageCompletion;
                if (name == "PhotoLocation") return this.PhotoLocation;
                return this.INVALID;

            },
            describe: function(type) {
              if (type == "HangMan") return 'Ahorcado';
              if (type == "MultipleChoice") return 'Multiple choice';
              if (type == "ImageCompletion") return 'Arrastrar y Soltar';
              if (type == "PhotoLocation") return 'Fotolocalización';
            },
            exerciseStateFromType: function(type) {
                if (type == "HangMan") return 'hangManGameContainer';
                if (type == "MultipleChoice") return 'multipleChoiceExerciseContainer';
                if (type == "ImageCompletion") return 'imageCompletionExercise';
                if (type == "PhotoLocation") return 'photoLocationExercise';
                return '';
            },
            goToExerciseDetail: function(activity) {

                $state.go(this.exerciseStateFromType(activity.type) + '.detail', {
                    id: activity.exerciseId
                });

            },
            gameStateForType: function(type) {
                if (type == "HangMan") return 'hangManGamePlayContainer';
                if (type == "MultipleChoice") return 'multipleChoiceExerciseContainerPlayWithSession';
                if (type == "ImageCompletion") return 'imageCompletionExerciseGame';
                if (type == "PhotoLocation") return 'photoLocationExerciseGame'
                return '^';
            }
        });
    });

angular.module('buenOjoApp')
    .service('ActivityRouter', function($state, ActivityType) {

        return {
            performTransition: function(enrollment, transition) {
              var params = {force: true};// all transitions must happen
              var state = undefined;
              var reload = false;

                // somehow the user got to the transition screen. terminate the course.
                if (transition.next == null && transition.previous == null && transition.courseFinished){
                    $state.go('courseslist');
                    return;
                }
                if ( isFinite(transition.next) && transition.next == transition.previous.id ) {
                  params = {
                     id: transition.previous.exerciseId,
                     enrollment: enrollment,
                     activity: transition.previous

                 };
                 reload = true;

                 state = ActivityType.gameStateForType(transition.previous.type);

                }else {
                  params = {
                     id: transition.next.exerciseId,
                     enrollment: enrollment,
                     activity: transition.next,

                 };
                 state = ActivityType.gameStateForType(transition.next.type);
                }
                $state.go(state, params, {reload: reload});



            }
        };
    });

angular.module('buenOjoApp')
    .service('Result', function() {
        return {
            modal: function() {
                return ['$stateParams', '$state', '$modal','Activity','ActivityRouter', function($stateParams, $state, $modal, Activity, ActivityRouter) {
                    $modal.open({

                        templateUrl: 'scripts/appCustom/entities/result/resultPopupContainer.html',
                        controller: 'ResultController',
                        size: 'md',
                        resolve: {
                            entity: function() {
                                return {
                                    exerciseCount: $stateParams.count,
                                    experience: $stateParams.experience,
                                    percentage: $stateParams.percentage,

                                };
                            }
                        }
                    }).result.then(function(result) {
                        ActivityRouter.performTransition($stateParams.transition);
                    }, function() {
                        $state.go('coursesList', {});
                    })
                }];
            }
        };
    });
