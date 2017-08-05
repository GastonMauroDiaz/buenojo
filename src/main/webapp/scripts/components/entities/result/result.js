'use strict';

angular.module('buenOjoApp')
    .config(function($stateProvider) {
        $stateProvider
            .state('imageCompletionExerciseResult', {
                parent: 'imageCompletionExerciseGame',
                url: '/result',
                params: { won: null, enrollment: null, message:null, courseLevelSession: null},
                data: {},
                onEnter: ['$stateParams', '$state', '$modal','Activity','ActivityRouter', function($stateParams, $state, $modal, Activity, ActivityRouter) {
                    $modal.open({

                        templateUrl: 'scripts/components/entities/result/resultPopupContainer.html',
                        controller: 'ResultController',
                        size: 'md',
                        resolve: {
                            entity: function() {
                                return {
                                    won: $stateParams.won,
                                    enrollment: $stateParams.enrollment,
                                    message: $stateParams.message,
                                    courseLevelSession: $stateParams.courseLevelSession

                                };
                            }
                        }
                    }).result.then(function(result) {
                        // Activity.next({
                        //     won: $stateParams.won,
                        //     enrollment: $stateParams.enrollment
                        //   },
                        //   function(transition) {
                        //       $scope.transition = transition;
                        //       ActivityRouter.performTransition($stateParams.enrollment, transition);
                        //   });

                    }, function() {
                        $state.go('courseslist',{force: true});
                    })
                }]

            })
            .state('photoLocationResult', {
                parent: 'photoLocationExerciseGame',
                url: '/result',
                data: {},
                params: { won: null, enrollment: null, message:null, courseLevelSession: null},
                onEnter: ['$stateParams', '$state', '$modal','Activity','ActivityRouter', function($stateParams, $state, $modal, Activity, ActivityRouter) {
                  $modal.open({

                      templateUrl: 'scripts/components/entities/result/resultPopupContainer.html',
                      controller: 'ResultController',
                      size: 'md',
                      resolve: {
                          entity: function() {
                              return {
                                won: $stateParams.won,
                                enrollment: $stateParams.enrollment,
                                message: $stateParams.message,
                                courseLevelSession: $stateParams.courseLevelSession

                              };
                          }
                      }
                  }).result.then(function(result) {

                  }, function() {
                      $state.go('courseslist', {});
                  })
              }]

          })
            .state('multipleChoiceResult', {
                parent: 'multipleChoiceExerciseContainerPlayWithSession',
                url: '/result',
                data: {

                },
                  params: { won: null, enrollment: null, message:null, courseLevelSession: null},
                onEnter: ['$stateParams', '$state', '$modal','Activity','ActivityRouter', function($stateParams, $state, $modal, Activity, ActivityRouter) {
                  $modal.open({

                    templateUrl: 'scripts/components/entities/result/resultPopupContainer.html',
                    controller: 'ResultController',
                      size: 'md',
                      resolve: {
                          entity: function() {
                              return {
                                won: $stateParams.won,
                                enrollment: $stateParams.enrollment,
                                message: $stateParams.message,
                                courseLevelSession: $stateParams.courseLevelSession

                              };
                          }
                      }
                  }).result.then(function(result) {

                  }, function() {
                     $state.go('courseslist', {});
                  })
              }]

          })
          .state('hangManResult', {
              parent: 'hangManGamePlayContainer',
              url: '/result',
              data: {

              },
              params: { won: null, enrollment: null, message:null, courseLevelSession: null},
              onEnter: ['$stateParams', '$state', '$modal','Activity','ActivityRouter', function($stateParams, $state, $modal, Activity, ActivityRouter) {
                $modal.open({

                  templateUrl: 'scripts/components/entities/result/resultPopupContainer.html',
                  controller: 'ResultController',
                    size: 'md',
                    resolve: {
                        entity: function() {
                            return {
                              won: $stateParams.won,
                              enrollment: $stateParams.enrollment,
                              message: $stateParams.message,
                              courseLevelSession: $stateParams.courseLevelSession

                            };
                        }
                    }
                }).result.then(function(result) {

                }, function() {
                   $state.go('courseslist', {});
                })
            }]

        });
    });
