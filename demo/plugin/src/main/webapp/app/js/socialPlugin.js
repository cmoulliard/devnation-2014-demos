/**
 * @module SOCIAL
 * @main SOCIAL
 *
 * The main entrypoint for the Social module
 *
 */
var SOCIAL = (function (SOCIAL) {

    SOCIAL.pluginName = "SOCIAL";
    SOCIAL.log = Logger.get(SOCIAL.pluginName);

    SOCIAL.templateUrl = '/social/app/html/';

    SOCIAL.jmxDomain   = "hawtio";
    SOCIAL.mbeanType   = "SocialMedia";
    SOCIAL.mbean       = SOCIAL.jmxDomain + ":type=" + SOCIAL.mbeanType;

    SOCIAL.module = angular.module(SOCIAL.pluginName, ['bootstrap', 'ngResource','hawtioCore','hawtio-forms','datatable'])
        .config(function ($routeProvider) {
            $routeProvider.
                when('/social/chart', { templateUrl: SOCIAL.templateUrl + 'areachart.html' }).
                when('/social/tweets', { templateUrl: SOCIAL.templateUrl + 'searchtweets.html' });
        });

    SOCIAL.module.run(function (workspace, viewRegistry, layoutFull) {

        // tell the app to use the full layout, also could use layoutTree
        // to get the JMX tree or provide a URL to a custom layout
        viewRegistry["social"] = layoutFull;

        // Set up top-level link to our plugin
        workspace.topLevelTabs.push({
            id: "social",
            content: "Social",
            title: "Social plugin loaded dynamically",
            isValid: function () {
                return true;
            },
            href: function () {
                return "#/social";
            },
            isActive: function () {
                return workspace.isLinkActive("social");
            }

        });

    });

    SOCIAL.SocialController = function ($scope, jolokia) {
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

    SOCIAL.FormController = function ($scope, $log, jolokia, workspace, $location) {
        $log.info('FormController - starting up, yeah!');
        $scope.forms = {};
        $scope.username = '';
        $scope.keywords = '';
        $scope.reponse = '';

        SOCIAL.log.info("Current config: ", $scope.currentConfig);

        $scope.formConfig = {
            properties: {
                "username": { description: "Twitter user", "type": "java.lang.String" }
            }
        };

        $scope.tweetsGrid = {
            selectedItems: [],
            data: 'tweets',
            showFilter: true,
            filterOptions: {
                filterText: ''
            },
            showSelectionCheckbox: false,
            enableRowClickSelection: true,
            multiSelect: false,
            primaryKeyFn: function (entity, idx) {
                return entity.group + "/" + entity.name
            },
            columnDefs: [
                {
                    field: 'tweet',
                    displayName: 'Tweet',
                    resizable: true,
                    width: 150
                }
            ]
        }


        $scope.searchUser = function(json, form) {
            SOCIAL.log.info("Form :", json);
        };

        $scope.searchTweets = function () {
            if (Core.isBlank($scope.keywords)) {
                return;
            }
            SOCIAL.log.warn("Search for : " + $scope.keywords);

            jolokia.request({
                type: 'exec',
                mbean: SOCIAL.mbean,
                operation: 'searchTweets',
                arguments: [$scope.keywords]
            }, {
                method: 'POST',
                success: function (response) {
                    /* TextArea = Response */
                    list = response.value;
                    result = "";
                    for (var record in list) {
                        result += list[record] + String.fromCharCode(13) ;
                    }
                    $scope.response = result;

                    /* Simple Table */
                    $scope.tweets = response.value;

                    // Reset keywords field
                    $scope.keywords = '';

                    Core.$apply($scope);
                },
                error: function (response) {
                    SOCIAL.log.warn("Failed to search for Tweets: ", response.error);
                    SOCIAL.log.info("Stack trace: ", response.stacktrace);
                    Core.$apply($scope);
                }

            })
        };

    };

    SOCIAL.AreaChartController = function ($scope, $routeParams, jolokia, $templateCache, localStorage, $element) {

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

    return SOCIAL;
}(SOCIAL || { }));

hawtioPluginLoader.addModule(SOCIAL.pluginName);