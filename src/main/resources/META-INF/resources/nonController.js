angular.module('non', ['ngMaterial'])
.config(function($mdThemingProvider) {

  var redMap = $mdThemingProvider.extendPalette('red', {
    '500': '#5e0d0c',
    'contrastDefaultColor': 'dark'
  });
  $mdThemingProvider.definePalette('redMap', redMap);

  $mdThemingProvider.theme('default')
    .primaryPalette('redMap')
})
.controller('nonController',['$scope', '$http',
    function($scope, $http) {
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
}]);