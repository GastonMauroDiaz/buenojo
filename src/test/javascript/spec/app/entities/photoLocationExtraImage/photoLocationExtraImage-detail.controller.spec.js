'use strict';

describe('PhotoLocationExtraImage Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationExtraImage, MockPhotoLocationImage, MockCourse;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationExtraImage = jasmine.createSpy('MockPhotoLocationExtraImage');
        MockPhotoLocationImage = jasmine.createSpy('MockPhotoLocationImage');
        MockCourse = jasmine.createSpy('MockCourse');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationExtraImage': MockPhotoLocationExtraImage,
            'PhotoLocationImage': MockPhotoLocationImage,
            'Course': MockCourse
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationExtraImageDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationExtraImageUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
