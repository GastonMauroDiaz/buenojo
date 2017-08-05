'use strict';

describe('Level Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockLevel, MockCourse, MockExercise;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockLevel = jasmine.createSpy('MockLevel');
        MockCourse = jasmine.createSpy('MockCourse');
        MockExercise = jasmine.createSpy('MockExercise');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Level': MockLevel,
            'Course': MockCourse,
            'Exercise': MockExercise
        };
        createController = function() {
            $injector.get('$controller')("LevelDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:levelUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
