var app = angular.module('appUser', ['ngRoute','ngResource','angular-loading-bar', 'ngAnimate']);
app.filter('range', function() {
    return function(input, total) {
        total = parseInt(total);
        for (var i=0; i<total; i++)
            input.push(i);
        return input;
    };
});

app.config(function ($routeProvider, cfpLoadingBarProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/user/home.html"
    });

    $routeProvider.when("/characters", {
        controller: "charactersController",
        templateUrl: "app/views/user/characters.html"
    });

    $routeProvider.when("/teams", {
        controller: "teamController",
        templateUrl: "app/views/user/teams.html"
    });

    $routeProvider.when("/teams/characters", {
        controller: "teamController",
        templateUrl: "app/views/user/team_characters.html"
    });

    $routeProvider.when("/characters/:id", {
        controller: "characterController",
        templateUrl: "app/views/user/character.html"
    });

    $routeProvider.when("/favorites", {
        controller: "favoritesController",
        templateUrl: "app/views/user/favorites.html"
    });
    cfpLoadingBarProvider.includeSpinner = true;
    //$routeProvider.otherwise({ redirectTo: "/home" });

});

app.controller('characterController', ['$scope', '$location', 'characterService', function($scope, $location, characterService) {
    var controller = this;
    $scope.selectedCharacter = characterService.getSelectedCharacter();
}]);