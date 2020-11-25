var app = angular.module('non', ['ngMaterial']);

app.config(function($mdThemingProvider) {

  var redMap = $mdThemingProvider.extendPalette('red', {
    '500': '#5e0d0c',
    'contrastDefaultColor': 'dark'
  });
  $mdThemingProvider.definePalette('redMap', redMap);

  $mdThemingProvider.theme('default')
    .primaryPalette('redMap')
})

app.controller('nonController',['$scope', '$http',
    function($scope, $http) {

        $scope.showStates = function() {
            $http.get("/states").then(function (response) {
                console.log(response)
                var data = response.data
                var i;
                for (i = 0; i < data.length; i++) {
                    item = data[i]
                    if(item.status == "NAUGHTY") {
                       $scope.naughtyStates = item.states
                    } else if(item.status == "NICE"){
                       $scope.niceStates = item.states
                    }
                }
            });
            $scope.showCountryLeaderboards = false
            $scope.showStateLeaderboards = true
        }
        $scope.showCountries = function() {
            $http.get("/countries").then(function (response) {
                console.log(response)
                var data = response.data
                var i;
                for (i = 0; i < data.length; i++) {
                    item = data[i]
                    if(item.status == "NAUGHTY") {
                       $scope.naughtyCountries = item.countries
                    } else if(item.status == "NICE"){
                       $scope.niceCountries = item.countries
                    }
                }
            });
            $scope.showStateLeaderboards = false
            $scope.showCountryLeaderboards = true
        }
}]);