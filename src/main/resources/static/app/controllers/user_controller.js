/**
 * Created by ivan on 31/05/16.
 */
app.controller('userController', ['$scope', 'userService', function ($scope, userService) {

    $scope.user = {};
    $scope.team = [];
    $scope.token = getCookie('access_token');
    $scope.getUsers = getUsers;
    $scope.selectUser = selectUser;
    $scope.getSelectedUser = getSelectedUser;

    function getUsers() {
        userService.getUsers($scope.token).then(function (users) {
            $scope.users = users.map(function (user) {
                user.type = user.admin ? 'Administrator' : 'User';
                user.last_access
                return user;
            });
        })
    }

    function selectUser(user) {
        userService.selectUser(user);
    }

    function getSelectedUser() {
        userService.getSelectedUser($scope.token).then(function (user) {
            $scope.user = user;
            $scope.user.teams_qty = user.teams.length;
            $scope.user.favorites_qty = user.favorites.length;
            $scope.team = user.teams[Math.floor(Math.random() * user.teams.length)];
        });
    }

}]);