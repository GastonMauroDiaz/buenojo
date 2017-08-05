'use strict';

describe('PhotoLocationImage Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationImage, MockImageResource, MockPhotoLocationKeyword;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationImage = jasmine.createSpy('MockPhotoLocationImage');
        MockImageResource = jasmine.createSpy('MockImageResource');
        MockPhotoLocationKeyword = jasmine.createSpy('MockPhotoLocationKeyword');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationImage': MockPhotoLocationImage,
            'ImageResource': MockImageResource,
            'PhotoLocationKeyword': MockPhotoLocationKeyword
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationImageDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationImageUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
