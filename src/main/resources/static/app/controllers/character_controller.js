'use strict';
app.controller('charactersController', ['$scope', '$location', '$timeout', 'characterService', function ($scope, $location, $timeout, characterService, cfpLoadingBar) {
    var self = this;
    $scope.name = "";
    $scope.offset = 0;
    $scope.characters = [];
    $scope.totalCharacters = $scope.characters.length;
    $scope.filteredCharacters = [];
    $scope.nameChangedPromise = null;

    // Pagination variables.
    $scope.charsPerPage = 9;
    $scope.maxPages = 10;
    $scope.cantPages = Math.ceil($scope.totalCharacters / $scope.charsPerPage);
    $scope.rangePages = $scope.cantPages > $scope.maxPages ? $scope.maxPages : $scope.cantPages;

    $scope.nameChange = function () {
        if ($scope.nameChangedPromise) {
            $timeout.cancel($scope.nameChangedPromise);
        }
        $scope.offset = 0;
        $('.pagination').children('li[type=number]').removeClass('active');
        $($('li[type=number]')[0]).addClass('active');
        $scope.nameChangedPromise = $timeout(self.fetchCharacters(), 3000);
    };

    $('.nextpage').click(function () {
        self.fetchCharacters();
    });

    $('.previouspage').click(function () {
        self.fetchCharacters();
    });

    $scope.setSelectedCharacter = function (char) {
        var filter = function findById(character) {
            return character.id == char.id;
        };
        var character = $scope.characters.find(filter);
        characterService.setSelectedCharacter(character);
    };

    self.fetchCharacters = function () {
        characterService.fetchCharacters($scope.offset, $scope.charsPerPage, $scope.name)
            .then(
                function (d) {
                    $scope.totalCharacters = d.data.total;
                    $scope.characters = d.data.results;
                    self.calcPagination();
                },
                function (errResponse) {
                    console.error('Error while fetching characters');
                }
            );
    };

    self.calcPagination = function () {
        $scope.cantPages = Math.ceil($scope.totalCharacters / $scope.charsPerPage);
        $scope.rangePages = $scope.cantPages > $scope.maxPages ? $scope.maxPages : $scope.cantPages;
    };

    $scope.changePage = function ($event) {
        $('.pagination').children('li[type=number]').removeClass('active');
        $($event.currentTarget).addClass("active");
        var page = $event.currentTarget.textContent;
        $scope.offset = page * 9 - $scope.charsPerPage;
        self.fetchCharacters();
    };

    // Carga la primer página.
    self.fetchCharacters();

}]);