app.service('userService', ['$http', function ($http) {

    var userService = this;
    var selectedUser = {};

    userService.getUserByIdAndToken = getUserByIdAndToken;
    userService.login = login;
    userService.singUp = singUp;
    userService.redirectHome = redirectHome;
    userService.createTeam = createTeam;
    userService.getUserTeams = getUserTeams;
    userService.getUsers = getUsers;
    userService.getSelectedUser = getSelectedUser;
    userService.selectUser = selectUser;

    return userService;

    function getUserByIdAndToken(username, accessToken) {

        var data = {params: {access_token: accessToken}};

        return $http.get('/users/' + username, data).success(function (user) {
            return user;
        });
    }

    function login(username, password) {

        var data = {"user_name": username, "user_password": password};
        var json = angular.toJson(data);
        var config = {headers: {'Content-Type': 'application/json;charset=utf-8;'}};

        return $http.post('/users/authenticate', json, config);
    }

    function singUp(username, password) {

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
        var data = {params: {access_token: accessToken}};
        return $http.get('/users/' + userId + '/teams', data);
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

    function selectUser(user) {
        selectedUser = user;
    }

}]);