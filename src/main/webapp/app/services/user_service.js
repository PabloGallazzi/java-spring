/**
 * Created by ivan on 04/05/16.
 */
'use strict';

app.service('userService', ['$http', function($http){

    var userService = this;

    userService.getUserByIdAndToken = getUserByIdAndToken;

    return userService;

    function getUserByIdAndToken(username, accessToken) {

        var data = { params: { access_token: accessToken } };

        return $http.get('/users/' + username, data).success(function(user) {
            return user;
        });
    }

}]);