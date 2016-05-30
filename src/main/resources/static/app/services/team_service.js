'use strict';

app.factory('teamService', ['$http', '$q', function($http, $q){

    return {
        createTeam : function(teamName, userId, accessToken) {
            var data = {team_name: teamName};
            var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
            console.log(teamName + userId + accessToken);
            return $http.post('/users/' + userId + '/teams?access_token=' + accessToken, data, config);
        }

    };

}]);