app.service('userService', ['$http', function ($http) {

    var userService = this;
    var selectedUser = {};
    var accessToken = getCookie('access_token');
    var userId = getAuthenticatedUserId();
    var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};

    userService.login = login;
    userService.register = register;
    userService.redirectHome = redirectHome;

    // Setters
    userService.createTeam = createTeam;
    userService.deleteTeam = deleteTeam;
    userService.selectUser = selectUser;
    userService.addCharToTeam = addCharToTeam;
    userService.deleteChar = deleteChar;

    // Getters
    userService.getAccessToken = getAccessToken;
    userService.getAuthenticatedUserId = getAuthenticatedUserId;
    userService.getSelectedUser = getSelectedUser;
    userService.getTeams = getTeams;
    userService.getTeamsIntersection = getTeamsIntersection;
    userService.getUser = getUser;
    userService.getUserTeams = getUserTeams;
    userService.getUsers = getUsers;

    return userService;

    function getUser(username, accessToken) {
        var data = {params: {access_token: accessToken}};
        return $http.get('/users/' + username, data).then(function (response) {
            return response.data;
        });
    }

    function getAuthenticatedUserId() {
        return accessToken.slice(0, accessToken.indexOf('-'));
    }

    function getAccessToken() {
        return accessToken;
    }

    function login(username, password) {
        var data = {"user_name": username, "user_password": password};
        var json = angular.toJson(data);
        return $http.post('/users/authenticate', json, config);
    }

    function register(username, password) {
        var data = {user_name: username, user_password: password};
        var json = angular.toJson(data);
        return $http.post('/users', json, config);
    }

    function redirectHome() {
        window.location = "home";
    }

    function createTeam(teamName) {
        var data = {team_name: teamName};
        return $http.post('/users/' + userId + '/teams?access_token=' + accessToken, data, config);
    }

    function getUserTeams(user_id) {
        // i need to receive user id in order to support User & Admin requests.
        return $http.get('/users/' + user_id + '/teams?access_token=' + accessToken, config);
    }

    function getUsers() {
        return $http.get('/users?access_token=' + accessToken, config).then(function (response) {
            return response.data;
        });
    }

    function getSelectedUser() {
        return $http.get('/users/' + selectedUser.user_id + '?access_token=' + accessToken, config).then(function (response) {
            return response.data;
        });
    }

    function deleteTeam(teamId) {
        var data = {params: {access_token: accessToken}};
        return $http.delete('/users/' + userId + '/teams/' + teamId, data);
    }

    function selectUser(user) {
        selectedUser = user;
    }

    function getTeamsIntersection(id_1, id_2) {
        return $http.get('/teams/commons/' + id_1 + '/' + id_2 + '?access_token=' + accessToken, config).then(function (response) {
            return response.data;
        });
    }

    function getTeams() {
        return $http.get('/teams?access_token=' + accessToken, config).then(function (response) {
            return response.data;
        })
    }

    function addCharToTeam(teamId, character){
        var data = {
            id: character.id,
            name: character.name,
            description: character.description,
            elected_times: character.elected_times,
            thumbnail: character.thumbnail
        };

        return $http.post('/users/' + userId + '/teams/' + teamId + '/characters?access_token=' + accessToken, data, config);
    }

    function deleteChar(charId, teamId){
        var data = {params: {access_token: accessToken}};
        return $http.delete('/users/' + userId + '/teams/' + teamId + '/characters/' + charId, data);
    }

}]);