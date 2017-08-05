app.service('RegisterModal', function ($state,$modal, $rootScope) {


  return function() {
    return $modal.open({
        templateUrl: 'scripts/appCustom/account/register/register.html',
        controller: 'RegisterController',
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
