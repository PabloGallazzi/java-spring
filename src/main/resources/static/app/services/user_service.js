app.service('userService', ['$http', function($http){

    var userService = this;

    userService.getUserByIdAndToken = getUserByIdAndToken;
    userService.login = login;
    userService.singUp = singUp;
    userService.redirectHome = redirectHome;
    userService.createTeam = createTeam;
    userService.getUserTeams = getUserTeams;

    return userService;

    function getUserByIdAndToken(username, accessToken) {

        var data = { params: { access_token: accessToken } };

        return $http.get('/users/' + username, data).success(function(user) {
            return user;
        });
    }

    function login(username, password) {

        var data = { "user_name": username, "user_password": password };
        var json = angular.toJson(data);
        var config = { headers: {'Content-Type': 'application/json;charset=utf-8;'} };

        return $http.post('/users/authenticate', json, config);
    }

    function singUp(username, password) {

        var data = { user_name: username, user_password: password };
        var json = angular.toJson(data);
        var config = { headers: {'Content-Type': 'application/json;charset=utf-8;'} };

        return $http.post('/users', json, config);
    }

    function redirectHome(accessToken){
        window.location ="home";
    }

    function createTeam(teamName, userId, accessToken) {
        var data = {team_name: teamName};
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.post('/users/' + userId + '/teams?access_token=' + accessToken, data, config);
    }

    function getUserTeams(userId, accessToken){
        var data = { params :{ access_token: accessToken } };
        return $http.get('/users/'+userId+'/teams', data);
    }

}]);