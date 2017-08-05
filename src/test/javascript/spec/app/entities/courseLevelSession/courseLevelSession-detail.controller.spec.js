'use strict';

describe('CourseLevelSession Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCourseLevelSession, MockCourseLevelMap, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCourseLevelSession = jasmine.createSpy('MockCourseLevelSession');
        MockCourseLevelMap = jasmine.createSpy('MockCourseLevelMap');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CourseLevelSession': MockCourseLevelSession,
            'CourseLevelMap': MockCourseLevelMap,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("CourseLevelSessionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:courseLevelSessionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
