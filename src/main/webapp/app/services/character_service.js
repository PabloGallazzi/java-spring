'use strict';
 
app.factory('characterService', ['$http', '$q', function($http, $q){

    var selectedCharacter = {};

    return {
            fetchAllCharacters: function(offset, limit) {
                    return $http.get('http://localhost:8080/characters?offset='+offset+'&limit='+limit)
                            .then(
                                    function(response){
                                        return response.data;
                                    }, 
                                    function(errResponse){
                                        console.error('Error while fetching characters');
                                        return $q.reject(errResponse);
                                    }
                            );
            },

            getSelectedCharacter: function(){
                return selectedCharacter;
            },

            setSelectedCharacter: function(character){
                selectedCharacter = character;
            }
    };
}]);