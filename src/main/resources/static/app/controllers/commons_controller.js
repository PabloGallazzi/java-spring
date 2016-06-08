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
    $scope.intersected = false;

    $scope.getIntersection = getIntersection;
    $scope.getTeams = getTeams;
    $scope.getUsers = getUsers;
    $scope.clearSelection = clearSelection;
    $scope.showResult = showResult;
    $scope.canDoIntersection = function () {
        return $scope.team_1 && $scope.team_2 && !$scope.intersected
    };

    function getIntersection() {
        var id1 = $scope.team_1.team_id;
        var id2 = $scope.team_2.team_id;
        userService.getTeamsIntersection(id1, id2).then(function (characters) {
            $scope.intersected = true;
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
        newSelection();
        var a = userService.getUserTeams(user.user_id).then(function (response) {
            $scope.teams[user.user_name] = response.data;
            return response.data;
        }, function (err) {
            console.error(err);
        });
        console.log(a);
    }

    function showResult() {
        var onTrue = "Check the characters in common!";
        var onFalse = "The teams: " + $scope.team_1.team_name + " and " + $scope.team_2.team_name + " have no characters in common. Try again changing teams!";
        return $scope.characters.length ? onTrue : onFalse;
    }

    function clearSelection() {
        newSelection();
        $scope.characters = [];
        $scope.team_1 = '';
        $scope.team_2 = '';
    }

    function newSelection() {
        $scope.intersected = false;
    }

}]);