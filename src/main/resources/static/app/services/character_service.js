'use strict';
 
app.factory('characterService', ['$http', '$q', function($http, $q){

    var selectedCharacter = {};

    return {
            fetchCharacters: function(offset, limit, name) {
                var params = { offset: offset, limit:limit, name_starts_with: name };
                var config = { params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'} };
                    return $http.get('/characters', config)
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