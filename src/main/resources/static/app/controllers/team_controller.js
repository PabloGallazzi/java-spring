app.controller('teamController', ['$scope', 'userService', 
    function($scope, userService) {

        $scope.token = null;

        $scope.createTeam = function () {
            var teamName = $scope.teamName;
            var token = getCookie('access_token');
            var userId = getAuthenticatedUserId(token);
            
            userService.createTeam(teamName, userId, token)
                .success(function() {
                    console.log('team created');    
                })
                .error(function() {
                    console.error('Error creating a team');
                })
            ;
        };

        function getAuthenticatedUserId() {
            return token.slice(0, token.indexOf('-'));
        }
    }
]);
