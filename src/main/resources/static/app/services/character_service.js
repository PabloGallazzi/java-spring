'use strict';

app.factory('characterService', ['$http', '$q', 'userService', function ($http, $q, userService) {

    var selectedCharacter = {};
    var accessToken = userService.getAccessToken();

    return {
        fetchCharacters: function (offset, limit, name) {
            var params = {offset: offset, limit: limit, name_starts_with: name};
            var config = {params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'}};
            return $http.get('/characters', config)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error while fetching characters');
                        return $q.reject(errResponse);
                    }
                );
        },

        getRanking: function (limit) {
            var params = {limit: limit};
            var config = {params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'}};
            return $http.get('characters/ranking/?access_token=' + accessToken, config).then(function (response) {
                return response.data;
            });
        },

        getSelectedCharacter: function () {
            return selectedCharacter;
        },

        setSelectedCharacter: function (character) {
            selectedCharacter = character;
        }
    };
}]);