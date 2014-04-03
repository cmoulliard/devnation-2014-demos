package org.devnation.camel;

import facebook4j.PostUpdate;
import org.apache.camel.builder.RouteBuilder;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class FacebookExample extends RouteBuilder {

    String FACEBOOK_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    Properties properties = new Properties();

    public void configure() throws Exception {

        // tag::snippetRoute[]
        // start with a 30 day window for the first delayed poll
        String since = "RAW("
                + new SimpleDateFormat(FACEBOOK_DATE_FORMAT).format(
                new Date(System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS)))
                + ")";

        from("facebook://searchPosts?reading.limit=10&" +
                "reading.locale=en.US&reading.since=" + since + "&" +
                "consumer.initialDelay=1000&" +
                "consumer.sendEmptyMessageWhenIdle=true&"
                + getOauthParams());
        // end::snippetRoute[]

        // tag::snippetRouteP[]
        PostUpdate post = new PostUpdate(new URL("http://facebook4j.org"))
                .picture(new URL("http://facebook4j.org/images/hero.png"))
                .name("Facebook4J - A Java library for " +
                        "the Facebook Graph API")
                .caption("facebook4j.org")
                .description("Facebook4J is a Java library" +
                        " for the Facebook Graph API.");

        from("direct:post")
                .setBody().constant(post)
                .to("facebook://postFeed/inBody=postUpdate");
        // end::snippetRouteP[]

    }

    // tag::snippetOAuth[]
    protected String getOauthParams() {
        return "oAuthAppId=" + properties.get("oAuthAppId") +
                "&oAuthAppSecret=" + properties.get("oAuthAppSecret")
                + (properties.get("oAuthAccessToken") != null
                ? ("&oAuthAccessToken=" + properties.get("oAuthAccessToken")) : "");
    }
    // end::snippetOAuth[]
}
