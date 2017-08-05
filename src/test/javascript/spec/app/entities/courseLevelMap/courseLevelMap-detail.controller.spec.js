'use strict';

describe('CourseLevelMap Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCourseLevelMap, MockCourse, MockLevel;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCourseLevelMap = jasmine.createSpy('MockCourseLevelMap');
        MockCourse = jasmine.createSpy('MockCourse');
        MockLevel = jasmine.createSpy('MockLevel');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CourseLevelMap': MockCourseLevelMap,
            'Course': MockCourse,
            'Level': MockLevel
        };
        createController = function() {
            $injector.get('$controller')("CourseLevelMapDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:courseLevelMapUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
