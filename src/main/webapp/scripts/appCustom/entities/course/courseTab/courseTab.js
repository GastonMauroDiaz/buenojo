'use strict';

angular.module('buenOjoApp')
    .config(function($stateProvider) {
        $stateProvider.state('courseTab', {
            url: '/tabCursos',
            //	abstract: true,
            parent: 'entity',
            url: '/cursos/:id',
            params: {
              enrollment: null,
              
              id: null,
            },
            data: {
                authorities: ['ROLE_COURSE_STUDENT'],
                pageTitle: 'Curso'
            },
            views: {
                "content@": {
                    controller: 'courseTabController',
                    templateUrl: 'scripts/appCustom/entities/course/courseTab/courseTab.html'
                },

            }
        })

    });
