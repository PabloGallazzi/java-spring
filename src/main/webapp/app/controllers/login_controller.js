    /**
 * Created by ivan on 04/05/16.
 */
'use strict';
app.controller('loginController', ['$scope', '$location','$cookieStore', 'loginService', function($scope, $location, $cookieStore, loginService) {

    $scope.userEmail = null;
    $scope.userPassword = null;

    $scope.login = function() {

        var username = $scope.userEmail;
        var password = $scope.userPassword;
        $location.path('/home');
        loginService.login(username, password).success(function(user) {
            $scope.userLogged = user;
            $cookieStore.put('access_token', user.access_token);
            $location.path('/home');
        });
    };

}]);