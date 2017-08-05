'use strict';

angular.module('buenOjoApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('multipleChoiceQuestionPlayRandom', {
            parent: 'entity',
            url: '/multipleChoiceQuestionsPlayRandom',
            data: {
                authorities: [],
                pageTitle: 'MultipleChoiceQuestions'
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/appCustom/entities/multipleChoiceQuestion/multipleChoiceQuestionsPlay.html',
                    controller: 'MultipleChoiceQuestionPlayRandomController'
                }
            },
            resolve: {
            	   
                  
                   
                   
               
            }
        });
});
