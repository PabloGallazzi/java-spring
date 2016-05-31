app.controller('loginController', ['$scope', '$location', 'userService', 'errorService',
    function ($scope, $location, userService, errorService) {

        $scope.userName = null;
        $scope.userPassword = null;
        $scope.errors = {};
        $scope.showError = errorService.showApiError;

        $scope.login = function () {

            var username = $scope.userName;
            var password = $scope.userPassword;
            userService.login(username, password)
                .success(function (user) {
                    setCookie('access_token', user.access_token, 190);
                    userService.redirectHome();
                })
                .error(function (error) {
                    console.error('Error while authenticating user');
                    $scope.errors.api = error.error;
                })
            ;
        };

    }]);