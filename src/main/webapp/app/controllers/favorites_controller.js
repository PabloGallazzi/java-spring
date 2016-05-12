/**
 * Created by ivan on 11/05/16.
 */
'use strict';
app.controller('favoritesController', ['$scope', '$location', '$cookieStore', 'favoritesService', function($scope, $location, $cookieStore, favoritesService) {

    obtainFavorites();

    function obtainFavorites() {
        var token = $cookieStore.get('access_token');
        var userId = getUserIdBy(token);
        favoritesService.getFavoritesByToken(userId, token).success(function(favorites) {
            $scope.favorites = favorites.map(function(character) {
                character.image_path = getImagePathFor(character);
                return character;
            })
        });
    }

    function getImagePathFor(character) {
        return character.thumbnail.path + "." + character.thumbnail.extension;
    }

    function getUserIdBy(token) {
        return token.slice(0, token.indexOf("-"));
    }
}]);