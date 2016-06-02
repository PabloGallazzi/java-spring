/**
 * Created by ivan on 31/05/16.
 */
app.controller('userController', ['$scope', 'userService', function ($scope, userService) {

    var userController = this;

    $scope.selectedUser = {};
    $scope.token = getCookie('access_token');
    $scope.getUsers = getUsers;

    userController.getUserInfo = getUserInfo;
    userController.selectUser = selectUser;

    function getUsers() {
        userService.getUsers($scope.token).then(function (users) {
            $scope.users = users.map(function (user) {
                user.type = user.admin ? 'Administrator' : 'User';
                return user;
            });
        })
    }

    function getUserInfo(userId) {
        userService.getUserInfo($scope.token, userId).then(function (user) {
            $scope.selectedUser = user;
        });
    }

    function selectUser(user) {
        $scope.selectedUser = user;
    }

}]);