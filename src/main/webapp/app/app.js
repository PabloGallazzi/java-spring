var app = angular.module('appMarvelApi', ['ngRoute','ngResource']);

app.config(function ($routeProvider) {

    $routeProvider.when("/", {
        controller: "",
        templateUrl: "app/views/home.html"
    });

    $routeProvider.when("/login", {
        controller: "loginController",
        templateUrl: "app/views/login.html"
    });

    $routeProvider.when("/register", {
        controller: "signupController",
        templateUrl: "app/views/singup.html"
    });

    // $routeProvider.otherwise({ redirectTo: "/" });

});

app.controller('signupController', ['$scope', '$location', 'characterService', function($scope, $location, characterService) {
    var self = this;
    $scope.selectedCharacter = characterService.getSelectedCharacter();
}]);

app.controller('signinController', ['$scope', '$location', 'characterService', function($scope, $location, characterService) {
    var self = this;
    $scope.selectedCharacter = characterService.getSelectedCharacter();
}]);