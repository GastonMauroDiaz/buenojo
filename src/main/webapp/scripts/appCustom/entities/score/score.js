'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('imageCompletionScore',{
              parent: 'imageCompletionExerciseGame',
              url: '/score/{count}/{experience}/{percentage}',
              data: {},
              onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({

                    templateUrl: 'scripts/appCustom/entities/score/scorePopupContainer.html',
                      controller: 'ScoreController',
                      size: 'md',
                      resolve: {
                          entity: function () {
                                return {
                                  exerciseCount: $stateParams.count,
                                  experience: $stateParams.experience,
                                  percentage: $stateParams.percentage,

                                };
                            }
                      }
                  }).result.then(function(result) {
                      $state.go('^');
                  }, function() {
                      $state.go('^');
                  })
              }]

            })
            .state('photoLocationScore',{
              parent: 'photoLocationExerciseGame',
              url: '/score/{count}/{experience}/{percentage}',
              data: {},
              onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({

                    templateUrl: 'scripts/appCustom/entities/score/scorePopupContainer.html',
                      controller: 'ScoreController',
                      size: 'md',
                      resolve: {
                          entity: function () {
                                return {
                                  exerciseCount: $stateParams.count,
                                  experience: $stateParams.experience,
                                  percentage: $stateParams.percentage,

                                };
                            }
                      }
                  }).result.then(function(result) {
                      $state.go('^');
                  }, function() {
                      $state.go('^');
                  })
              }]

            })
            .state('multipleChoiceScore', {
                parent: 'multipleChoiceExerciseContainerPlayWithSession',
            	url: '/score/{count}/{experience}/{percentage}',
                    data: {

                    },
                    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                         	$modal.open({

                        	templateUrl: 'scripts/appCustom/entities/score/scorePopupContainer.html',
                            controller: 'ScoreController',
                            size: 'md',
                            resolve: {
                            	  entity: function () {
                                      return {
                                    	  exerciseCount: $stateParams.count,
                                    	  experience: $stateParams.experience,
                                    	  percentage: $stateParams.percentage,

                                      };
                                  }
                            }
                        }).result.then(function(result) {
                            $state.go('^');
                        }, function() {
                            $state.go('^');
                        })
                    }]
                });
        });
