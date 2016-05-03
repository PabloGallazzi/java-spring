'use strict';
app.controller('charactersController', ['$scope', '$location', 'characterService', function($scope, $location, characterService) {
    var self = this;

    // Evento para manejar el páginado de los personajes.
    $('.pagination').children('li[type=number]').click(function(evt){
        $('.pagination').children('li[type=number]').removeClass('active');
        $(evt.currentTarget).addClass("active");
        var page = evt.currentTarget.textContent;
        var limit= 9,
            offset= page * 9 - limit;
        self.fetchAllCharacters(offset,limit);
    });

    $scope.characters = [];

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
                            $scope.characters = d.data.results;
                       },
                        function(errResponse){
                            console.error('Error while fetching Currencies');
                        }
               );
    };

    // Carga la primer página.
    $('.active').click();
}]);