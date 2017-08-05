'use strict';

angular.module('buenOjoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('multipleChoiceQuestionPlay', {
                parent: 'entity',
                url: '/multipleChoiceQuestionsPlay/{id}',
                data: {
                    authorities: ['ROLE_DEMO_USER', 'ROLE_COURSE_STUDENT'],
                    pageTitle: 'MultipleChoiceQuestions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/appCustom/entities/multipleChoiceQuestion/multipleChoiceQuestionsPlay.html',
                        controller: 'MultipleChoiceQuestionPlayController'
                    }
                },
                resolve: {
                	   entity: ['$stateParams', 'MultipleChoiceQuestion', function($stateParams, MultipleChoiceQuestion) {
                           return MultipleChoiceQuestion.get({id : $stateParams.id});
                       }]
                }
            });
    });
