package org.devnation.camel;

import org.apache.camel.builder.RouteBuilder;

import java.util.Date;

public class TwitterExample extends RouteBuilder {

    protected String consumerKey;
    protected String consumerSecret;
    protected String accessToken;
    protected String accessTokenSecret;
    protected String keywords;
    protected String delay;

    public void configure() {
        // tag::snippetRoute[]
        from("twitter://search?type=polling&delay="
                + delay + "&useSSL=true&keywords="
                + keywords + "&" + getUriTokens())
                .choice()
                   .when().simple("${body.tweet.text} > 'java'")
                      .bean("Service", "push")
                .otherwise()
                      .to(">> Tweets received");
        // end::snippetRoute[]

        // tag::snippetRouteP[]
        Date now = new Date();
        String tweet = "Demo: this is a tweet posted on " + now.toString();

        from("direct:post")
            .setBody().constant(tweet)
            .to("twitter://timeline/user?" + getUriTokens());
        // end::snippetRouteP[]
    }

    // tag::snippetOAuth[]
    protected String getUriTokens() {
        return "consumerKey=" + consumerKey
                + "&consumerSecret=" + consumerSecret
                + "&accessToken=" + accessToken
                + "&accessTokenSecret=" + accessTokenSecret;
    }
    // end::snippetOAuth[]

}
