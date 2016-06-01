app.controller('teamController', ['$scope', 'userService', 
    function($scope, userService) {

        $scope.teams = [];
        var accessToken = getCookie('access_token');
        var userId = getAuthenticatedUserId(accessToken);
        var teamController = this;
        teamController.getUsersTeams = getUsersTeams;
        teamController.getAuthenticatedUserId = getAuthenticatedUserId;
        teamController.getUsersTeams();

        $scope.createTeam = function(){
            var teamName = $scope.teamName;
            
            userService.createTeam(teamName, userId, accessToken)
                .success(function() {
                    getUsersTeams();
                })
                .error(function() {
                    console.error('Error creating a team');
                })
            ;
        };
        
        $scope.deleteTeam = function(teamId){
            userService.deleteTeam(teamId, userId, accessToken)
                .success(function() {
                    getUsersTeams();
                })
                .error(function(){
                    console.error('Error deleting a team');    
                })
            ;
        };
        
        function getUsersTeams(){
            userService.getUserTeams(userId, accessToken)
                .success(function(teams){
                    $scope.teams = teams.map(function(t){return t;})    
                })
                .error(function(errResponse){
                    console.error('Error while fetching user teams');
                    return $q.reject(errResponse);
                })
            ;
        }
        
        function getAuthenticatedUserId(token) {
            return token.slice(0, token.indexOf('-'));
        }
    }
]);
