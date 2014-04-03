package org.devnation.camel;

import org.apache.camel.builder.RouteBuilder;

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
