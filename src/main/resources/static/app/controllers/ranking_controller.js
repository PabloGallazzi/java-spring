/**
 * Created by ivan on 03/06/16.
 */
app.controller('rankingController', ['$scope', 'characterService', function ($scope, characterService) {
    
    $scope.charactersLimit = 5;

    $scope.getRanking = getRanking;

    function getRanking(limit) {
        characterService.getRanking(limit).then(function (characters) {
            $scope.characters = characters;
        });
    }
    
}]);