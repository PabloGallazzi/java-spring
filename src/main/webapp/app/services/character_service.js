'use strict';
 
app.factory('characterService', ['$http', '$q', function($http, $q){

    var selectedCharacter = {};

    return {
            fetchAllCharacters: function(offset, limit) {
                var params = { offset: offset, limit:limit };
                var config = { params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'} };
                    return $http.get('/characters',config)
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
            },

            getCharactersByName : function(name) {
                var params = { name_starts_with: name, limit:9 };
                var config = { params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'} };

                return $http.get('/characters',config)
                    .then(
                        function(response){
                            return response.data;
                        },
                        function(errResponse){
                            console.error('Error while fetching characters');
                            return $q.reject(errResponse);
                        }
                    );
            }
    };
}]);