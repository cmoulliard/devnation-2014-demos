/**
 * @module SOCIAL
 */
var SOCIAL = (function (SOCIAL) {

    /**
     * @property breadcrumbs
     * @type {{content: string, title: string, isValid: isValid, href: string}[]}
     *
     * Data structure that defines the sub-level tabs for
     * our plugin, used by the navbar controller to show
     * or hide tabs based on some criteria
     */
    SOCIAL.breadcrumbs = [
        {
            content: '<i class="icon-comments"></i> User',
            title: "Search statistics about a User",
            href: "#/social/user"
        },
        {
            content: '<i class="icon-cogs"></i> Tweets',
            title: "Search Tweets",
            href: "#/social/tweets"
        }
    ];

    /**
     * @function NavBarController
     *
     * @param $scope
     * @param workspace
     *
     * The controller for this plugin's navigation bar
     *
     */
    SOCIAL.NavBarController = function ($scope, $location) {

        $scope.breadcrumbs = SOCIAL.breadcrumbs;

    };

    return SOCIAL;
}(SOCIAL || { }));
