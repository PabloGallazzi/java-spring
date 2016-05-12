/**
 * Created by ivan on 11/05/16.
 */
'use strict';

app.factory('favoritesService', ['$http', '$q', function($http, $q){

    var favoritesService = this;

    favoritesService.getFavoritesByToken = getFavoritesByToken;

    return favoritesService;

    function getFavoritesByToken(user_id, token) {

        console.log("giladaservice");

        var params = { access_token: token };
        var config = { params: params, headers: {'Content-Type': 'application/json;charset=utf-8;'} };

        return $http.get('/users/' + user_id + '/characters/favorites', config).success(function(favorites) {
            return favorites;
        });

    }

}]);
