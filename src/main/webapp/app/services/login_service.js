/**
 * Created by ivan on 04/05/16.
 */
'use strict';

app.factory('loginService', ['$http', '$q', function($http, $q){

    return {
        login: function(email, password) {

            var data = {
                user_name: email,
                user_password: password
            };

            var config = {
                headers : {
                    'Content-Type': 'application/json;charset=utf-8;'
                }
            };

            $http.post('/users/authenticate', data, config)
                .success(function (data, status, headers, config) {
                    console.log(data)
                })
                .error(function (data, status, header, config) {
                    console.log(data);
                    console.log(status);
                });
        }
    }
}]);