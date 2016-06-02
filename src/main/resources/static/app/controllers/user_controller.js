/**
 * Created by ivan on 31/05/16.
 */
app.controller('userController', ['$scope', 'userService', function ($scope, userService) {

    $scope.user = {};
    $scope.token = getCookie('access_token');
    $scope.getUsers = getUsers;
    $scope.selectUser = selectUser;
    $scope.getSelectedUser = getSelectedUser;
    $scope.getRandomTeam = getRandomTeam;

    function getUsers() {
        userService.getUsers($scope.token).then(function (users) {
            $scope.users = users.map(function (user) {
                user.type = user.admin ? 'Administrator' : 'User';
                user.favorites_qty = user.favorites.length;
                user.teams_qty = user.teams.length;
                return user;
            });
        })
    }

    function selectUser(user) {
        userService.selectUser(user);
    }

    function getSelectedUser() {
        $scope.user = userService.getSelectedUser();
    }

    function getRandomTeam() {
        var teams = $scope.user.teams;
        $scope.team = teams[Math.floor(Math.random() * teams.length)];
    }

}]);