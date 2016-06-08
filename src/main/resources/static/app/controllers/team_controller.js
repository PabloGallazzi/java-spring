app.controller('teamController', ['$scope', '$location', 'userService', 'errorService', 'teamsShareService', 'characterService',
    function($scope, $location, userService, errorService, teamsShareService, characterService) {

        $scope.teams = [];
        var accessToken = getCookie('access_token');
        var userId = getAuthenticatedUserId(accessToken);
        var teamController = this;
        teamController.getUsersTeams = getUsersTeams;
        teamController.getAuthenticatedUserId = getAuthenticatedUserId;
        teamController.getUsersTeams();
        $scope.isTeamDropdownVisible = false;
        $scope.isCharAddedSuccessful = false;
        $scope.errors = {};
        $scope.showError = errorService.showApiError;
        $scope.selectCharacter = selectCharacter;

        $scope.init = function(){
            checkIfACharWasAddedToTeam();
        };
        
        $scope.createTeam = function(){
            var teamName = $scope.teamName;
            
            userService.createTeam(teamName, userId, accessToken)
                .success(function() {
                    getUsersTeams();
                    $scope.teamName = null;
                })
                .error(function(error) {
                    console.error('Error creating a team');
                    $scope.errors.api = error.cause;
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
        
        $scope.addChars = function() {
            $location.path('/characters');
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
        
        $scope.showTeamDropdown = function(){
            return userService.getUserTeams(userId, accessToken)
                .success(function(teams){
                    var teamSize = teams.map(function(t){return t;}).length;
                    $scope.isTeamDropdownVisible = (teamSize > 0 );
                    return (teamSize > 0 );
                })
                .error(function(){
                    console.error('Error while fetching user teams');
                    return false;
                })
            ;
        };

        $scope.addCharToTeam = function(teamId, character){
            userService.addCharToTeam(teamId, character)
                .success(function(){
                    $scope.charNameAdded = character.name;
                    goUserTeamsHome();
                })
                .error(function(){
                    console.error("error posting character to user team");
                })
            ;
        };

        function goUserTeamsHome(){
            $location.path('/teams').search({charAdded:"OK", charNameAdded:$scope.charNameAdded});
        }
        
        function checkIfACharWasAddedToTeam(){
            if($location.search().charAdded == "OK"){
                $scope.isCharAddedSuccessful = true;
                $scope.charNameAdded = $location.search().charNameAdded
            }
        }
        
        $scope.closeSuccessAlert = function(){
            $location.search({});
        };
        
        $scope.viewTeam = function(team){
            teamsShareService.setTeamCharacters(team.members);
            $location.path('/teams/characters', false);
        };

        $scope.getChars = function(){
            var characters = teamsShareService.getTeamCharacters();
            $scope.selectedTeamCharacters = characters.map(function(char){
                char.image_path = getImagePathFor(char);
                return char;
            });
        };

        function getImagePathFor(character) {
            return character.thumbnail.path + '.' + character.thumbnail.extension;
        }

        function selectCharacter(character) {
            characterService.setSelectedCharacter(character);
        }
    }
]);

app.factory('teamsShareService', [function(){
    var selectedTeamCharacters = [];

    return{
        setTeamCharacters: function(teamCharacters){
            selectedTeamCharacters = teamCharacters;
        },
        getTeamCharacters: function(){
            return selectedTeamCharacters;
        }
    };
}]);
