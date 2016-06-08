/**
 * Created by ivan on 03/06/16.
 */
app.controller('rankingController', ['$scope', 'characterService', function ($scope, characterService) {
    
    $scope.charLimit = 1;
    $scope.showAlert = false;

    $scope.getRanking = getRanking;
    $scope.isValidInput = isValidInput;

    function getRanking(limit) {
        characterService.getRanking(limit).then(function (characters) {
            $scope.characters = characters;
        });
    }

    function isValidInput() {
        return $scope.charLimit >= 1 && $scope.charLimit <= 5;
    }

}]);