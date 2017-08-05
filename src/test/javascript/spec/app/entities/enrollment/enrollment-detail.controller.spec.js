'use strict';

describe('Enrollment Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockEnrollment, MockCourse, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockEnrollment = jasmine.createSpy('MockEnrollment');
        MockCourse = jasmine.createSpy('MockCourse');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Enrollment': MockEnrollment,
            'Course': MockCourse,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("EnrollmentDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:enrollmentUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
