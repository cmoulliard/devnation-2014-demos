var templateUrl = '/hawtio/simple/app/html/';

var simplePlugin = angular.module('simple', ['hawtioCore'])
  .config(function($routeProvider) {
    $routeProvider.
      when('/simple', { templateUrl: templateUrl + 'simple.html' });
  })


simplePlugin.run(function(workspace, viewRegistry, layoutFull) {

    // tell the app to use the full layout, also could use layoutTree
    // to get the JMX tree or provide a URL to a custom layout
    viewRegistry["simple"] = layoutFull;

    // Set up top-level link to our plugin
    workspace.topLevelTabs.push({
      id: "simple",
      content: "Simple",
      title: "Simple plugin loaded dynamically",
      isValid: function() { return true; },
      href: function() { return "#/simple"; },
      isActive: function() { return workspace.isLinkActive("simple"); }

    });

  });

// tell the hawtio plugin loader about our plugin so it can be
// bootstrapped with the rest of angular
hawtioPluginLoader.addModule('simple');

var SimpleController = function($scope, jolokia) {
  $scope.message = "Data collected from Social Camel Component";
  $scope.likes = "0"

  // register a watch with jolokia on this mbean to
  // get updated metrics
  Core.register(jolokia, $scope, {
    type: 'read', mbean: 'hawtio:type=SocialData',
    arguments: []
  }, onSuccess(render));

  // update display of metric
  function render(response) {
    $scope.likes = response.value['PublishData'];
    $scope.$apply();
  }

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
        {type: 'read', mbean: 'hawtio:type=SocialData', attribute: 'PublishData'}
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



