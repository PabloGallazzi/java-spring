/**
 * Created by ivan on 11/05/16.
 */
'use strict';

app.factory('favoritesService', ['$http', '$q', function ($http, $q) {

    var favoritesService = this;

    favoritesService.getFavoritesByToken = getFavoritesByToken;
    favoritesService.addFavorite = addFavorite;
    favoritesService.removeFavorite = removeFavorite;
    favoritesService.isFavorite = isFavorite;

    return favoritesService;

    function getFavoritesByToken(user_id, token) {
        var params = {access_token: token};
        var config = {params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        var url = '/users/' + user_id + '/characters/favorites';
        return $http.get(url, config).then(function (response) {
            console.log('On favoriteService: ' + response.data);
            return response.data;
        }, function (err) {
            console.error('Error while getting favorites');
            $q.reject(err);
        });
    }

    function addFavorite(user_id, token, character) {
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        var url = '/users/' + user_id + '/characters/favorites?access_token=' + token;
        return $http.post(url, character, config);
    }

    function removeFavorite(user_id, token, character) {
        var params = {access_token: token};
        var config = {params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        var url = 'users/' + user_id + '/characters/favorites/' + character.id;
        return $http.delete(url, config);
    }

    function isFavorite(user_id, token, character) {
        var params = {access_token: token};
        var config = {params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        var url = 'users/' + user_id + '/characters/favorites/' + character.id;
        return $http.get(url, config);
    }


}]);