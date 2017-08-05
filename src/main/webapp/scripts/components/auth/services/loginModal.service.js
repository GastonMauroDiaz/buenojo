app.service('LoginModal', function ($state,$modal, $rootScope) {


  return function() {
    return $modal.open({
        templateUrl: 'scripts/appCustom/account/login/login.html',
        controller: 'LoginController',
        size: 'lg',
        resolve: {
            entity: function () {
                return {
                                                  };
            }
        }
    }).result.then(function(result) {
      alert(1);

      $state.go('login', {}, { reload: true });
    }, function() {
      $state.go('home');

    });

  }
});
