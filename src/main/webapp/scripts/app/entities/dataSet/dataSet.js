'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dataSet', {
                parent: 'entity',
                url: '/dataSets',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Cargar Datos'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dataSet/dataSets.html',
                        controller: 'DataSetController'
                    }
                },
                resolve: {
                }
            })

            .state('dataSet.confirm',{
              parent: 'dataSet',
              url: '/confirm',
              data:{
                authorities: ['ROLE_USER'],
                pageTitle: ""
              },
              params: {
                dataset: null
              },
              views: {
                  'content@': {
                      templateUrl: 'scripts/app/entities/dataSet/dataSetConfirm.html',
                      controller: 'DataSetConfirmController'
                  }
              },
              resolve: {
              }
            })
            .state('dataSet.result',{
              parent: 'dataSet.confirm',
              url: '/result',
              data:{
                authorities: ['ROLE_USER'],
                pageTitle: ""
              },
              params: {
                result: null
              },
              onEnter:  ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                  $modal.open({
                      templateUrl: 'scripts/app/entities/dataSet/LoaderResult.html',
                      controller: 'LoaderResultController',
                      size: 'lg',
                      resolve: {

                      }
                  }).result.then(function(result) {
                      $state.go('dataSet', null, { reload: true });
                  }, function() {
                      $state.go('dataSet', null, { reload: true });
                  })
              }]
            })
          });
