// default the perspective - test1
/*
(function (Perspective) {
    var metadata = Perspective.metadata || {};
    // new line
    Perspective.metadata = metadata;
    Perspective.defaultPerspective = "Wiki";
    Perspective.defaultPageLocation = "#/wiki/view";

    metadata["Wiki"] = {
        label: "Wiki",
        isValid: function (workspace) { return true; },
        lastPage: "#/wiki/view",
        topLevelTabs: {
            includes: [
                {
                    href: "#/wiki"
                },
                {
                    href: "#/logs"
                }
            ],
            excludes: [
                {
                    href: "#/jmx"
                },
                {
                    href: "#/dashboard"
                },
                {
                    href: "#/jvm"
                }
            ]
        }
    };

})

    (Perspective || {});*/
