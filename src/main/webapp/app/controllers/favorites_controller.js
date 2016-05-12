/**
 * Created by ivan on 11/05/16.
 */
'use strict';
app.controller('favoritesController', ['$scope', '$location', '$cookieStore', 'favoritesService', function($scope, $location, $cookieStore, favoritesService) {

    $scope.token = $cookieStore.get('access_token');
    obtainFavorites();

    function obtainFavorites() {
        var token = $scope.token;
        var delimiterPosition = token.indexOf("-");
        var userId = token.slice(0, delimiterPosition);
        favoritesService.getFavoritesByToken(userId, token).success(function(favorites) {
            $scope.favorites = favorites;
        });
    }
}]);