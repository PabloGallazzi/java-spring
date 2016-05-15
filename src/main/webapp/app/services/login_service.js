/**
 * Created by ivan on 04/05/16.
 */
'use strict';

app.factory('loginService', ['$http', '$q', 'userService', function($http, $q, userService){

    var loginService = this;

    loginService.login = login;
    loginService.userService = userService;

    return loginService;

    function login(username, password) {

        var data = { user_name: username, user_password: password };
        var config = { headers: {'Content-Type': 'application/json;charset=utf-8;'} };

        return $http.post('/users/authenticate', data, config); // Returning promise of authentication
    }

}]);