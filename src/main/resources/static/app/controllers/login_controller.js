app.controller('loginController', ['$scope', '$location', 'userService', function($scope, $location, userService) {

    $scope.userName = null;
    $scope.userPassword = null;

    $scope.login = function() {

        var username = $scope.userName;
        var password = $scope.userPassword;
        userService.login(username, password)
            .success(function(user) {
                setCookie('access_token',user.access_token,1);
                userService.redirectHome();
            })
            .error(function (error) {
                console.error('Error while authenticating user');
            })
        ;
    };

}]);