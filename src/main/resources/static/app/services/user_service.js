app.service('userService', ['$http', function ($http) {

    var userService = this;
    var selectedUser = {};
    var accessToken = getCookie('access_token');

    userService.getUserByIdAndToken = getUserByIdAndToken;
    userService.login = login;
    userService.register = register;
    userService.redirectHome = redirectHome;
    userService.createTeam = createTeam;
    userService.getUserTeams = getUserTeams;
    userService.deleteTeam = deleteTeam;
    userService.getUsers = getUsers;
    userService.getSelectedUser = getSelectedUser;
    userService.selectUser = selectUser;
    userService.getIntersectionOf = getIntersectionOf;
    userService.getTeams = getTeams;
    userService.addCharToTeam = addCharToTeam;

    return userService;

    function getUserByIdAndToken(username, accessToken) {

        var data = {params: {access_token: accessToken}};

        return $http.get('/users/' + username, data).success(function (user) {
            return user;
        });
    }

    function getAuthenticatedUserId() {
        return accessToken.slice(0, accessToken.indexOf('-'));
    }

    function login(username, password) {

        var data = {"user_name": username, "user_password": password};
        var json = angular.toJson(data);
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};

        return $http.post('/users/authenticate', json, config);
    }

    function register(username, password) {

        var data = {user_name: username, user_password: password};
        var json = angular.toJson(data);
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};

        return $http.post('/users', json, config);
    }

    function redirectHome(accessToken) {
        window.location = "home";
    }

    function createTeam(teamName, userId, accessToken) {
        var data = {team_name: teamName};
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.post('/users/' + userId + '/teams?access_token=' + accessToken, data, config);
    }

    function getUserTeams(userId, accessToken) {
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.get('/users/' + userId + '/teams?access_token=' + accessToken, config);
    }

    function getUsers(token) {
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.get('/users?access_token=' + token, config).then(function (response) {
            return response.data;
        });
    }

    function getSelectedUser(accessToken) {
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.get('/users/' + selectedUser.user_id + '?access_token=' + accessToken, config).then(function (response) {
            return response.data;
        });
    }

    function deleteTeam(teamId, userId, accessToken) {
        var data = {params: {access_token: accessToken}};
        return $http.delete('/users/' + userId + '/teams/' + teamId, data);
    }

    function selectUser(user) {
        selectedUser = user;
    }

    function getIntersectionOf(token, id_1, id_2) {
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.get('/teams/commons/' + id_1 + '/' + id_2 + '?access_token=' + token, config).then(function (response) {
            return response.data;
        });
    }

    function getTeams(token) {
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.get('/teams?access_token=' + token, config).then(function (response) {
            return response.data;
        })
    }

    function addCharToTeam(teamId, character){
        var userId = getAuthenticatedUserId();
        var data = {
            id: character.id,
            name: character.name,
            description: character.description,
            elected_times: character.elected_times,
            thumbnail: character.thumbnail
        };
        
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};
        return $http.post('/users/' + userId + '/teams/' + teamId + '/characters?access_token=' + accessToken, data, config);
    }

}]);