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

    SOCIAL.templatePath = '/social/app/html/';

    SOCIAL.jmxDomain   = "hawtio";
    SOCIAL.mbeanType   = "SocialMedia";
    SOCIAL.mbean       = SOCIAL.jmxDomain + ":type=" + SOCIAL.mbeanType;

    SOCIAL.module = angular.module(SOCIAL.pluginName, ['bootstrap', 'ngResource','hawtioCore','hawtio-forms','datatable'])
        .config(function ($routeProvider) {
            $routeProvider.
                when('/social/chart', { templateUrl: SOCIAL.templatePath + 'areachart.html' }).
                when('/social/tweets', { templateUrl: SOCIAL.templatePath + 'searchtweets.html' }).
                when('/social/user', { templateUrl: SOCIAL.templatePath + 'userinfo.html' });
        });

    SOCIAL.module.run(function (workspace, viewRegistry, layoutFull) {


        Core.addCSS('/social/app/css/table_bootstrap.css');

        // tell the app to use the full layout, also could use layoutTree
        // to get the JMX tree or provide a URL to a custom layout
        viewRegistry["social"] = layoutFull;

        // tell hawtio that we have our own custom layout for
        // our view
        // viewRegistry["social"] = SOCIAL.templatePath + "socialLayout.html";

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
        $scope.form = {};
        $scope.username = '';
        $scope.keywords = '';
        $scope.reponse = '';

        $scope.isReply = false;

        $scope.id = '';
        $scope.name = '';
        $scope.screenName = '';
        $scope.location = '';
        $scope.description = '';
        $scope.followersCount = '';
        $scope.friendsCount = '';
        $scope.favouritesCount = '';
        $scope.timeZone = '';
        $scope.lang = '';
        $scope.createdAt = '';


        /* ISSUE with binding

         SOCIAL.log.info("Current config: ", $scope.currentConfig);

         $scope.searchUser = function(json, form) {
         SOCIAL.log.info("Form :", json);
         };

        $scope.formConfig = {
            properties: {
                "username": { description: "Twitter user", "type": "java.lang.String" }
            }
        };
        */

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

        $scope.hover = function(isReply) {
            // Shows/hides the delete button on hover
            return $scope.isReply = false;
        };


        $scope.searchUser = function() {
            if (Core.isBlank($scope.username)) {
                return;
            }
            SOCIAL.log.warn("User searched : " + $scope.username);

            jolokia.request({
                type: 'exec',
                mbean: SOCIAL.mbean,
                operation: 'userInfo',
                arguments: [$scope.username]
            }, {
                method: 'POST',
                success: function (response) {
                    /* TextArea = Response
                    $scope.response = JSON.stringify(response);
                    */

                    $scope.isReply = true;

                    value = JSON.parse(response['value']);
                    $scope.id = value['id'];
                    $scope.name = value['name'];
                    $scope.screenName = value['screenName'];
                    $scope.location = value['location'];
                    $scope.description = value['description'];
                    $scope.followersCount = value['followersCount'];
                    $scope.friendsCount = value['friendsCount'];
                    $scope.favouritesCount = value['favouritesCount'];
                    $scope.timeZone = value['timeZone'];
                    $scope.lang = value['lang'];
                    $scope.createdAt = value['createdAt'];

                    // Reset Username field
                    $scope.username = '';

                    Core.$apply($scope);
                },
                error: function (response) {
                    SOCIAL.log.warn("Failed to search for Tweets: ", response.error);
                    SOCIAL.log.info("Stack trace: ", response.stacktrace);
                    Core.$apply($scope);
                }

            })
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
                    //$scope.tweets = response.value;
                    $scope.tweets = response.value.map(function(val) { return { tweet: val };});

                    SOCIAL.log.debug("tweets: ", response.value);

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