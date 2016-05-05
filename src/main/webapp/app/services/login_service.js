/**
 * Created by ivan on 04/05/16.
 */
'use strict';

app.factory('loginService', ['$http', '$q', function($http, $q){

    return {
        login: function(email, password) {
            var request = $.param({
                user_name: email,
                user_password: password
            });

            $http.post('http://localhost:8080/users/authenticate/', request)
                .success(function(response) {
                    console.log(response);
                    return response;
                }
            )
        }
    }
    
}]);