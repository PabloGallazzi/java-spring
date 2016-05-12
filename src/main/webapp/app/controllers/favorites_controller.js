/**
 * Created by ivan on 11/05/16.
 */
'use strict';
app.controller('favoritesController', ['$scope', '$location', '$cookieStore', 'favoritesService', function($scope, $location, $cookieStore, favoritesService) {

    $scope.token = $cookieStore.get('access_token');

    getFavoritesByToken($scope.token);

    function getFavoritesByToken(token) {
        var userFavorites = favoritesService.getFavoritesByToken(token);
        console.log(userFavorites);
    }
}]);