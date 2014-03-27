package org.devnation.camel;

import org.apache.camel.builder.RouteBuilder;

public class ExampleRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("amq:queue:quotes")
           .filter().xpath("/quote/product/ = 'widget")
                .bean("QuotesService", "widget")
           .filter().xpath("/quote/product/ = 'gadget")
                .bean("QuotesService","gadget");

    }
}
