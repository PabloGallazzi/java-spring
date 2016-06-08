/**
 * Created by ivan on 11/05/16.
 */
'use strict';

app.factory('favoritesService', ['$http', '$q', 'userService', function ($http, $q, userService) {

    var favoritesService = this;
    var accessToken = userService.getAccessToken();
    var userId = userService.getAuthenticatedUserId();
    var params = {access_token: accessToken};
    var config = {params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'}};

    favoritesService.getFavorites = getFavorites;
    favoritesService.addFavorite = addFavorite;
    favoritesService.removeFavorite = removeFavorite;
    favoritesService.isFavorite = isFavorite;

    return favoritesService;

    function getFavorites() {
        var url = '/users/' + userId + '/characters/favorites';
        return $http.get(url, config).then(function (response) {
            return response.data;
        }, function (err) {
            console.error('Error while getting favorites');
            $q.reject(err);
        });
    }

    function addFavorite(character) {
        var url = '/users/' + userId + '/characters/favorites';
        return $http.post(url, character, config);
    }

    function removeFavorite(character) {
        var url = 'users/' + userId + '/characters/favorites/' + character.id;
        return $http.delete(url, config);
    }

    function isFavorite(character) {
        var url = 'users/' + userId + '/characters/favorites/' + character.id;
        return $http.get(url, config);
    }


}]);
