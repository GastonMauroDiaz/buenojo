'use strict';

describe('PhotoLocationKeyword Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPhotoLocationKeyword;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPhotoLocationKeyword = jasmine.createSpy('MockPhotoLocationKeyword');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PhotoLocationKeyword': MockPhotoLocationKeyword
        };
        createController = function() {
            $injector.get('$controller')("PhotoLocationKeywordDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'buenOjoApp:photoLocationKeywordUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
