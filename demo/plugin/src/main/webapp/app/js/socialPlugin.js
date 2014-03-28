var templateUrl = '/social/app/html/';

var socialPlugin = angular.module('social', ['hawtioCore'])
  .config(function($routeProvider) {
    $routeProvider.
      when('/social', { templateUrl: templateUrl + 'social.html' }).
      when('/social/form', { templateUrl: templateUrl + 'form.html' });
  });


socialPlugin.run(function(workspace, viewRegistry, layoutFull) {

    // tell the app to use the full layout, also could use layoutTree
    // to get the JMX tree or provide a URL to a custom layout
    viewRegistry["social"] = layoutFull;

    // Set up top-level link to our plugin
    workspace.topLevelTabs.push({
      id: "social",
      content: "Social",
      title: "Social plugin loaded dynamically",
      isValid: function() { return true; },
      href: function() { return "#/social"; },
      isActive: function() { return workspace.isLinkActive("social"); }

    });

  });

// tell the hawtio plugin loader about our plugin so it can be
// bootstrapped with the rest of angular
hawtioPluginLoader.addModule('social');

var SocialController = function($scope, jolokia) {
  $scope.message = "Data collected from Social Camel Component";
  $scope.likes = "0"

  // register a watch with jolokia on this mbean to
  // get updated metrics
  Core.register(jolokia, $scope, {
    type: 'read', mbean: 'hawtio:type=SocialMedia',
    arguments: []
  }, onSuccess(render));

  // update display of metric
  function render(response) {
    $scope.likes = response.value['PublishData'];
    $scope.$apply();
  }

}

var FormController = function($scope, $log, jolokia, workspace, $location) {
    $log.info('FormController - starting up, yeah!');
    $scope.forms = {};
    $scope.username = '';

    $scope.twitterForm = {
        properties: {
            "SearchUser": { description: "Twitter user label", "type": "java.lang.String" }
        }
    };

    $scope.searchUser = function() {
        if (Core.isBlank($scope.username)) {
            return;
        }
        $log.warn("Search called for : " + $scope.username);
    };

}

var AreaChartController = function($scope, $routeParams, jolokia, $templateCache, localStorage, $element) {

    $scope.mbean = $routeParams['mbean'];
    $scope.attribute = $routeParams['attribute'];
    //$scope.duration = localStorage['updateRate'];

    //$scope.width = 308;
    //$scope.height = 296;
    $scope.width = 1280;
    $scope.height = 300;
    $scope.delay = 0;
    $scope.duration = 0;
    $scope.label = "Nber of Likes"

    $scope.template = "";
    $scope.entries = [];

    $scope.data = {
        entries: $scope.entries
    };

    $scope.req = [
        {type: 'read', mbean: 'hawtio:type=SocialMedia', attribute: 'PublishData'}
    ];

    function render(response) {
        $scope.entries.push({
            time: response.timestamp,
            count: response.value
        });

        $scope.entries = $scope.entries.last(15);

        if ($scope.template === "") {
            $scope.template = $templateCache.get("areaChart");
        }

        $scope.data = {
            _type: "date_histogram",
            entries: $scope.entries
        };

        Core.$apply($scope);
    }

    // Core.register(jolokia, $scope, $scope.req, onSuccess($scope.render));
    // register a watch with jolokia on this mbean to
    // get updated metrics
/*    Core.register(jolokia, $scope, {
        type: 'read', mbean: 'hawtio:type=SocialData',
        arguments: []
    }, onSuccess(render));*/

    Core.register(jolokia, $scope, $scope.req, onSuccess(render));

}



