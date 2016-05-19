app.controller('singupController', ['$scope', '$location', 'userService', 'errorService',
    function($scope, $location, userService, errorService) {

    $scope.username = null;
    $scope.password = null;
    $scope.errors = {};

    $scope.showError = errorService.showApiError;

    $scope.singUp = function() {

        var username = $scope.username;
        var password = $scope.password;
        userService.singUp(username, password)
            .success(function(user) {
                $location.path("/");
            })
            .error(function (error) {
                console.error('Error while creating user account');
                $scope.errors.api = error.cause;
            })
        ;
    };

}]);