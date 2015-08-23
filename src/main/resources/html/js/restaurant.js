var restaurantApp = angular.module('restaurantApp', ['ngResource', 'ui.bootstrap']);

restaurantApp.service('LocalRestaurant', function() {
    var localRestaurant;

    this.setRestaurant = function (restaurant) {
        localRestaurant = restaurant;
    };

    this.getRestaurant = function () {
        return localRestaurant;
    };
});


restaurantApp.controller('RestaurantController', function ($scope, $window, $resource, LocalRestaurant) {
    var RestaurantLocator = $resource('/service/restaurant/nearest/:longitude/:latitude',
        {latitude: '@latitude', longitude: '@longitude'}, {});

    $scope.findRestaurantNearestToMe = function() {
        window.navigator.geolocation.getCurrentPosition(function (position) {
            RestaurantLocator.get({latitude: position.coords.latitude, longitude: position.coords.longitude},
                function (foundRestaurant) {
                    $scope.nearestRestaurant = foundRestaurant;
                    LocalRestaurant.setRestaurant(foundRestaurant);
                });
        });
    };
    $scope.findRestaurantNearestToMe();
});

restaurantApp.controller('OrderController', function ($scope, $resource, LocalRestaurant){
    $scope.pizzas = [
        {name: "Marinara",                  family:"Traditional"},
        {name: "Margherita",                family:"Traditional"},
        {name: "Napoletana",                family:"Traditional"},
        {name: "Romana",                    family:"Red"},
        {name: "Diavola",                   family:"Red"},
        {name: "Vesuvio",                   family:"Red"},
        {name: "Capricciosa",               family:"Red"},
        {name: "Quattro Formaggi",          family:"White"},
        {name: "Prosciutto Cotto",          family:"White"},
        {name: "Ortolana",                  family:"White"},
        {name: "Pomodorini e Rucola",       family:"White"},
        {name: "Salsiccia e Funghi",        family:"White"}
    ];

    $scope.sizes = ["big", "medium", "small"];
    $scope.messages = [];

    $scope.sendOrder = function () {
        $scope.order.restaurantShopId = LocalRestaurant.getRestaurant().openStreetMapId;
        var PizzaOrder = $resource('/service/restaurant/order');
        PizzaOrder.save($scope.order,
            function(order) {
                $scope.messages.push({type: 'success', msg: 'Order sent!'})
            }
        );
    }

    $scope.closeAlert = function(index){
        $scope.messages.splice(index, 1);
    }

});
