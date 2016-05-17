app.controller('singupController', ['$scope', '$location', 'userService', function($scope, $location, userService) {

    $scope.username = null;
    $scope.password = null;

    $scope.singUp = function() {

        var username = $scope.username;
        var password = $scope.password;
        userService.singUp(username, password)
            .success(function(user) {
                $location.path("/");
            })
            .error(function (error) {
                console.error('Error while creating user account');
            })
        ;
    };

}]);