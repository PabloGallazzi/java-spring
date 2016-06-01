/**
 * Created by ivan on 31/05/16.
 */
app.controller('userDataController', ['$scope', 'userService', function ($scope) {

    var userDataController = this;

    $scope.selectedUser = {};
    $scope.token = getCookie('access_token');

    userDataController.getUsers = getUsers;
    userDataController.getUserInfo = getUserInfo;
    userDataController.selectUser = selectUser;

    function getUsers() {
        userService.getUsers($scope.token).then(function (users) {
            $scope.users = users;
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