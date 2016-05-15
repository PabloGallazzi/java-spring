'use strict';
app.controller('charactersController', ['$scope', '$location','$timeout', 'characterService', function($scope, $location,$timeout, characterService,cfpLoadingBar) {
    var self = this;
    $scope.name = "";

    var nameChangedPromise;
    $scope.nameChange = function(){
        if(nameChangedPromise){
            $timeout.cancel(nameChangedPromise);
        }
        nameChangedPromise = $timeout(self.getCharactersByName($scope.name),3000);
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

    //$('#txtCharacterName').change(function () {
      //  self.getCharactersByName($('#txtCharacterName').val());
    //})

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
                            console.error('Error while fetching characters');
                        }
               );
    };

    self.getCharactersByName = function(name){

        characterService.getCharactersByName(name)
            .then(
                function(d) {
        //            cfpLoadingBar.complete();
                    $scope.characters = d.data.results;
                },
                function(errResponse){
          //          cfpLoadingBar.complete();
                    console.error('Error while fetching characters');
                }
            );
    };

    // Carga la primer página.
    $('.active').click();
}]);