package org.devnation.demo.camel.twitter;

import org.apache.camel.Body;
import org.devnation.demo.camel.Service;
import org.apache.camel.builder.RouteBuilder;

public class WebSocketTwitterRoute extends RouteBuilder {

    protected String consumerKey;
    protected String consumerSecret;
    protected String accessToken;
    protected String accessTokenSecret;
    protected String keywords;
    protected String delay;

    @Override
    public void configure() {

        from("twitter://search?type=polling&delay=" + delay + "&useSSL=true&keywords=" + keywords + "&" + getUriTokens())
                .routeId("Tweet-Store-WS")
                .delay(5000)

                // We receive the Twitter4J Status that we will use
                // use to extract info text, isfavorited, is Retweeted, geolocation, isoLanguageCode, contributors
                // and create A JSON message used late ron to store it in ES
                .setHeader("tweet-full").method(Service.class, "getJSONTweet")
                //.log(">> Tweet complete : ${header.JSON-Tweet}")

                //.bean(Service.class, "tweetToJSON")
                //.log(">> JSON Tweet message : ${body}")

                // Message is stored using insight in ElasticSearch
                .bean(Service.class, "store")

                // We set the Body object with our
                .setBody().method(Service.class,"getJSONTweetText")

                // Data pushed to WS clients connected
                .to("websocket://0.0.0.0:9090/tweetTopic?sendToAll=true&staticResources=classpath:webapp");
    }

    protected String getUriTokens() {
        return "consumerKey=" + consumerKey + "&consumerSecret=" + consumerSecret + "&accessToken="
                + accessToken + "&accessTokenSecret=" + accessTokenSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }


    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
