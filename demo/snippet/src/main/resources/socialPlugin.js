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

    SOCIAL.jmxDomain = "hawtio";
    SOCIAL.mbeanType = "SocialMedia";
    SOCIAL.attribute = "PublishData";
    SOCIAL.mbean = SOCIAL.jmxDomain + ":type=" + SOCIAL.mbeanType;

    SOCIAL.module = angular.module(SOCIAL.pluginName, ['bootstrap', 'ngResource', 'ngGrid', 'hawtioCore', 'hawtio-ui', 'hawtio-forms'])
        .config(function ($routeProvider) {
            $routeProvider.
                when('/social/chart', { templateUrl: SOCIAL.templatePath + 'areachart.html' }).
                when('/social/tweets', { templateUrl: SOCIAL.templatePath + 'searchtweets.html' }).
                when('/social/user', { templateUrl: SOCIAL.templatePath + 'userinfo.html' });
        });

    SOCIAL.module.run(function (workspace, viewRegistry, layoutFull,layoutTree) {

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
            type: 'read',
            mbean: SOCIAL.mbean
        }, onSuccess(render));

        // update display of metric
        function render(response) {
            $scope.likes = response.value['PublishData'];
            $scope.$apply();
        }

    }

    SOCIAL.FormController = function ($scope, $log, $http, jolokia, workspace, $location) {
        SOCIAL.log.info('FormController - starting up, yeah!');
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

/*        $scope.expectedLen = 0;*/

        $scope.tweetsGrid = {
            data: 'tweets',
            enableRowClickSelection: false,
            showSelectionCheckbox: false,
/*            enablePaging: true,
            showFooter: true,
            pagingOptions: $scope.pagingOptions,*/
            columnDefs: [
                {
                    field: 'tweet',
                    displayName: 'Tweet',
                    resizable: true,
                    width: 1500
                }
            ]
        }

/*        $scope.pagingOptions = {
            pageSizes: [5, 10, 25, 50],
            pageSize: 5,
            totalServerItems: 0,
            currentPage: 1
        };

        $scope.$watch('pagingOptions', function (newVal, oldVal) {
            data = $scope.tweets;
            SOCIAL.log.info(">> $watch called !")
            if (data != null || data != undefined) {
                if (newVal !== oldVal && newVal.currentPage !== oldVal.currentPage) {
                    var pageSize = $scope.pagingOptions.pageSize;
                    var page = $scope.pagingOptions.currentPage;

                    // set totalServerItems from data on server...
                    $scope.pagingOptions.totalServerItems = data.length;
                    $scope.totalServerItems = data.length;
                    $scope.gridOptions.totalServerItems = data.length;
                    $scope.expectedLen = data.length;

                    var pagedData = data.slice((page - 1) * pageSize, page * pageSize);
                    $scope.tweets = pagedData;
                    if (!$scope.$$phase) {
                        $scope.$apply();
                    }
                }
            }
        }, true);*/

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


        $scope.hover = function (isReply) {
            // Shows/hides the delete button on hover
            return $scope.isReply = false;
        };


        $scope.searchUser = function () {
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

//tag::snippetController[]
        $scope.searchTweets = function () {
            if (Core.isBlank($scope.keywords)) {
                return;
            }
            jolokia.request({
                type: 'exec',
                mbean: SOCIAL.mbean,
                operation: 'searchTweets',
                arguments: [$scope.keywords]
            }, {
                method: 'POST',
                success: function (response) {
                    /* Simple Table */
                    $scope.tweets = response.value.map(function (val) {
                        return { tweet: val };
                    });
                    Core.$apply($scope);
                },
                error: function (response) {
                    SOCIAL.log.warn("Failed to search for Tweets: ", response.error);
                    SOCIAL.log.info("Stack trace: ", response.stacktrace);
                    Core.$apply($scope);
                }
//end::snippetController[]
            })
        };

    };

    SOCIAL.AreaChartController = function ($scope, $routeParams, jolokia, $templateCache, localStorage, $element) {

        //$scope.mbean = $routeParams['mbean'];
        //$scope.attribute = $routeParams['attribute'];

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
            {type: 'read',
             mbean: SOCIAL.mbean,
             attribute: SOCIAL.attribute
            }
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

        Core.register(jolokia, $scope, $scope.req, onSuccess(render));
    }

    return SOCIAL;
}(SOCIAL || { }));

hawtioPluginLoader.addModule(SOCIAL.pluginName);