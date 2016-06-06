/**
 * Created by ivan on 04/06/16.
 */
app.controller('commonsController', ['$scope', 'userService', function ($scope, userService) {
    
    $scope.users = [];
    $scope.teams = [];
    $scope.characters = [];
    $scope.user_1 = '';
    $scope.user_2 = '';
    $scope.team_1 = '';
    $scope.team_2 = '';

    $scope.getIntersection = getIntersection;
    $scope.getTeams = getTeams;
    $scope.getUsers = getUsers;

    function getIntersection() {
        var id1 = $scope.team_1.team_id;
        var id2 = $scope.team_2.team_id;
        userService.getTeamsIntersection(id1, id2).then(function (characters) {
            $scope.characters = characters;
        }, function (err) {
            console.error(err);
        });
    }

    function getUsers() {
        userService.getUsers().then(function (users) {
            $scope.users = users;
        }, function (err) {
            console.error(err);
        })
    }

    function getTeams(user) {
        userService.getUserTeams(user.user_id).then(function (response) {
            $scope.teams[user.user_name] = response.data;
        }, function (err) {
            console.error(err);
        })
    }

}]);