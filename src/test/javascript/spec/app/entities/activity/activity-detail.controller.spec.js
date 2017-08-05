'use strict';

describe('Activity Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockActivity, MockObject, MockLevel;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockActivity = jasmine.createSpy('MockActivity');
        MockObject = jasmine.createSpy('MockObject');
        MockLevel = jasmine.createSpy('MockLevel');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Activity': MockActivity,
            'Object': MockObject,
            'Level': MockLevel
        };
        createController = function() {
            $injector.get('$controller')("ActivityDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:activityUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
