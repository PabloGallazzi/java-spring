/**
 * Created by ivan on 11/05/16.
 */
'use strict';
app.controller('favoritesController',
    ['$scope',
        '$location',
        '$route',
        'favoritesService',
        'characterService',
        function ($scope,
                  $location,
                  $route,
                  favoritesService,
                  characterService) {

            var self = this;
            $scope.favorites = [];

            // Helpers

            self.getImagePathFor = function (character) {
                return character.thumbnail.path + "." + character.thumbnail.extension;
            };

            self.getUserIdBy = function (token) {
                return token.slice(0, token.indexOf("-"));
            };

            // Actual functions

            self.reloadPage = function () {
                $route.reload();
            };

            self.selectFavorite = function (character) {
                characterService.setSelectedCharacter(character);
            };

            self.addFavorite = function (character) {
                var token = getCookie('access_token');
                var userId = self.getUserIdBy(token);
                favoritesService.addFavorite(userId, token, character).success(function (response) {
                    self.reloadPage();
                });
            };

            self.removeFavorite = function (character) {
                var token = getCookie('access_token');
                var userId = self.getUserIdBy(token);
                favoritesService.removeFavorite(userId, token, character).success(function (response) {
                    self.reloadPage();
                });
            };

            self.isFavorite = function (character) {
                var condition = function (a_character) {
                    return a_character.id == character.id;
                };
                if ($scope.favorites.length == 0) {
                    self.fetchAllFavorites();
                }
                return $scope.favorites.some(condition);
            };

            self.fetchAllFavorites = function () {
                var token = getCookie('access_token');
                var userId = self.getUserIdBy(token);
                favoritesService.getFavoritesByToken(userId, token).success(function (favorites) {
                    $scope.favorites = favorites.map(function (character) {
                        character.image_path = self.getImagePathFor(character);
                        return character;
                    })
                });
            };

            self.fetchAllFavorites();

        }]);