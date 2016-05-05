/**
 * Created by ivan on 04/05/16.
 */
'use strict';
app.controller('loginController', ['$scope', '$location', 'loginService', function($scope, $location, loginService) {

    $scope.userEmail = null;
    $scope.userPassword = null;
    
    $scope.login = function() {

        var username = $scope.userEmail;
        var password = $scope.userPassword;

        return loginService.login(username, password)
    };

    $('.active').click();
}]);