'use strict';
app.controller('charactersController', ['$scope', '$location','$timeout', 'characterService', function($scope, $location,$timeout, characterService,cfpLoadingBar) {
    var self = this;
    $scope.name = "";
    $scope.characters = [];
    $scope.filteredCharacters = [];
    $scope.nameChangedPromise = null;
    $scope.totalCharacters = null;
    $scope.charsPerPage = 9;
    $scope.cantPages = Math.floor($scope.totalCharacters/$scope.charsPerPage) + $scope.totalCharacters%$scope.charsPerPage;

    $scope.currentPage = 1;
    $scope.numPerPage = 9;
    $scope.maxSize = 5;
    $scope.totalItems = 64;

    $scope.numPages = function () {
        return Math.ceil($scope.characters.length / $scope.numPerPage);
    };

    $scope.$watch("currentPage + numPerPage", function() {
        var begin = (($scope.currentPage - 1) * $scope.numPerPage)
            , end = begin + $scope.numPerPage;

        $scope.filteredCharacters = $scope.characters.slice(begin, end);
    });

    $scope.nameChange = function(){
        if($scope.nameChangedPromise){
            $timeout.cancel($scope.nameChangedPromise);
        }
        $scope.nameChangedPromise = $timeout(self.getCharactersByName($scope.name),3000);
    }

    // Evento para manejar el páginado de los personajes.
    $('li[type=number]').click(function(evt){
        $('.pagination').children('li[type=number]').removeClass('active');
        $(evt.currentTarget).addClass("active");
        var page = evt.currentTarget.textContent;
        var limit= 9,
            offset= page * 9 - limit;
        self.fetchAllCharacters(offset,limit);
    });

    $('.nextpage').click(function(evt){
        $('li[type=number]').children().each(function(){
            var pageNumber = parseInt(this.textContent);
            this.textContent = pageNumber + 5;
        });
        $('li[type=number]').first().click();
    });

    $('.previouspage').click(function(evt){
        $('li[type=number]').children().each(function(){
            var pageNumber = parseInt(this.textContent);
            this.textContent = pageNumber + 5;
        });
        $('li[type=number]').first().click();
    });

    $scope.setSelectedCharacter = function(evt){
        var id = evt.currentTarget.dataset.id;
        var filter = function findById(character) { return character.id == id;},
            character = $scope.characters.find(filter);
        characterService.setSelectedCharacter(character);
    };

    self.fetchAllCharacters = function(offset,limit){
      characterService.fetchAllCharacters(offset,limit)
          .then(
                       function(d) {
                           $scope.totalCharacters = d.data.total;
                           $scope.characters = d.data.results;
                       },
                        function(errResponse){
                            console.error('Error while fetching characters');
                        }
               );
    };

    self.getCharactersByName = function(name){

        characterService.getCharactersByName(name)
            .then(
                function(d) {
                    $scope.totalCharacters = d.data.total;
                    $scope.characters = d.data.results;
                },
                function(errResponse){
                    console.error('Error while fetching characters');
                }
            );
    };

    // Carga la primer página.
    $('.active').click();
}]);