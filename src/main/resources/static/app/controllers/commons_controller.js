/**
 * Created by ivan on 04/06/16.
 */
app.controller('commonsController', ['$scope', 'userService', function($scope, userService) {

    var commonsController = this;

    $scope.token = getCookie('access_token');
    $scope.characters = [];
    $scope.team_1 = '';
    $scope.team_2 = '';
    $scope.getIntersection = getIntersection;
    $scope.getTeams = getTeams;

    function getIntersection() {
        var id1 = $scope.team_1.team_id;
        var id2 = $scope.team_2.team_id;
        userService.getIntersectionOf($scope.token, id1, id2).then(function (characters) {
            $scope.characters = characters;
        }, function(err) {
            console.error(err);
        });
    }

    function getTeams() {
        userService.getTeams($scope.token).then(function (teams) {
            $scope.teams = teams;
        }, function (err) {
            console.error(err);
        })
    }

}]);