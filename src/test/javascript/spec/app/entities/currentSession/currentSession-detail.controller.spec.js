'use strict';

describe('CurrentSession Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCurrentSession, MockUser, MockCourseLevelSession;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCurrentSession = jasmine.createSpy('MockCurrentSession');
        MockUser = jasmine.createSpy('MockUser');
        MockCourseLevelSession = jasmine.createSpy('MockCourseLevelSession');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CurrentSession': MockCurrentSession,
            'User': MockUser,
            'CourseLevelSession': MockCourseLevelSession
        };
        createController = function() {
            $injector.get('$controller')("CurrentSessionDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:currentSessionUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
