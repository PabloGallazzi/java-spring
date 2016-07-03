'use strict';
app.controller('charactersController', ['$scope', '$location', '$timeout', 'characterService', function ($scope, $location, $timeout, characterService) {
    $scope.name = "";
    $scope.offset = 0;
    $scope.characters = [];
    $scope.totalCharacters = $scope.characters.length;
    $scope.nameChangedPromise = null;

    // Pagination variables.
    $scope.charsPerPage = 9;
    $scope.pagination = {
        current: 1,
        last: $scope.totalCharacters
    };

    getResultsPage(1);

    $scope.pageChanged = function(newPage) {
        getResultsPage(newPage);
    };

    function getResultsPage(pageNumber) {
        $scope.pagination.current = pageNumber;
        $scope.offset = (pageNumber-1) * $scope.charsPerPage;
        fetchCharacters();
    }

    $scope.nameChange = function () {
        if ($scope.nameChangedPromise) {
            $timeout.cancel($scope.nameChangedPromise);
        }
        $scope.offset = 0;
        $('.pagination').children('li[type=number]').removeClass('active');
        $($('li[type=number]')[0]).addClass('active');
        $scope.nameChangedPromise = $timeout(fetchCharacters(), 3000);
    };

    $scope.setSelectedCharacter = function (char) {
        var filter = function findById(character) {
            return character.id == char.id;
        };
        var character = $scope.characters.find(filter);
        characterService.setSelectedCharacter(character);
    };

    function fetchCharacters() {
        characterService.fetchCharacters($scope.offset, $scope.charsPerPage, $scope.name)
            .then(
                function (d) {
                    $scope.totalCharacters = d.data.total;
                    $scope.characters = d.data.results;
                },
                function (errResponse) {
                    console.error('Error while fetching characters');
                }
            );
    }

}]);