/**
 * Created by ivan on 03/06/16.
 */
app.controller('rankingController', ['$scope', 'characterService', function ($scope, characterService) {
    
    $scope.charactersLimit = 5;
    $scope.token = getCookie('access_token');

    $scope.getCharactersLimitedBy = getCharactersLimitedBy;

    function getCharactersLimitedBy(limit) {
        characterService.getCharactersLimitedBy(limit, $scope.token).then(function (characters) {
            $scope.characters = characters;
        });
    }
    
}]);