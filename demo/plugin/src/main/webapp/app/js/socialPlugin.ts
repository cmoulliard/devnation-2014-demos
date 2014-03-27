module Social {

    var pluginName = 'Social';
    var base_url = 'app/social/html';

    /* Application level module which depends on filters, controllers, and services */
    angular.module(pluginName, ['bootstrap', 'ngResource'])

        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider
                .when('/social', {templateUrl: base_url + '/index.html'})
        }])

        .run(($location:ng.ILocationService, workspace:Workspace, viewRegistry, helpRegistry) => {

            // Use Full Layout of Hawtio
            viewRegistry[pluginName] = 'app/social/html/index.html';

            helpRegistry.addUserDoc(pluginName, 'app/Social/doc/help.md');

            // Set up top-level link to our plugin
            workspace.topLevelTabs.push({
                content: "Social",
                title: "Social",
                isValid: (workspace) => true,
                href: () => '#/Social',
                isActive: (workspace:Workspace) => workspace.isLinkActive("Social")
            });

        });

    hawtioPluginLoader.addModule(pluginName);
}
