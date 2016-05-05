var app = angular.module('MarvelApiPOC', ['ngRoute','ngResource']);

app.config(function ($routeProvider) {

    $routeProvider.when("/home", {
        controller: "homeController",
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

    $routeProvider.when("/favorites", {
        controller: "favoriteController",
        templateUrl: "app/views/user/favorites.html"
    });

    $routeProvider.when("/characters/:id", {
        controller: "characterController",
        templateUrl: "app/views/user/character.html"
    });

    $routeProvider.when("/login", {
        controller: "loginController",
        templateUrl: "login.html"
    })

    $routeProvider.otherwise({ redirectTo: "/home" });

});

'use strict';
app.controller('homeController', function($scope) {

});

app.controller('characterController', ['$scope', '$location', 'characterService', function($scope, $location, characterService) {
    var controller = this;
    $scope.selectedCharacter = characterService.getSelectedCharacter();
}]);