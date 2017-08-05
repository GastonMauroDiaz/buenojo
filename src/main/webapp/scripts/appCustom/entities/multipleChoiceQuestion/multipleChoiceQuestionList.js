'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceQuestionList', {
                parent: 'entity',
                url: '/multipleChoiceQuestionsList',
                data: {
                    authorities: [],
                    pageTitle: 'MultipleChoiceQuestions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/multipleChoiceQuestion/multipleChoiceQuestionsList.html',
                        controller: 'MultipleChoiceQuestionListController'
                    }
                },
                resolve: {
                }
            })
            .state('multipleChoiceQuestionListByContainer', {
                parent: 'entity',
                url: '/multipleChoiceQuestionsList/{id}',
                data: {
                    authorities: [],
                    pageTitle: 'MultipleChoiceQuestions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/multipleChoiceQuestion/multipleChoiceQuestionsList.html',
                        controller: 'MultipleChoiceQuestionListController'
                    }
                },
                resolve: {
             	   
             }
            });
    });
