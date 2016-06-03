app.controller('registerController', ['$scope', '$location', 'userService', 'errorService',
    function($scope, $location, userService, errorService) {

    $scope.username = null;
    $scope.password = null;
    $scope.errors = {};

    $scope.showError = errorService.showApiError;

    $scope.register = function() {

        var username = $scope.username;
        var password = $scope.password;
        userService.register(username, password)
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