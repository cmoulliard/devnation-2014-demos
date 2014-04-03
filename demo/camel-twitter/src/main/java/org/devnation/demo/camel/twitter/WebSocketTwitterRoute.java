package org.devnation.demo.camel.twitter;

import org.apache.camel.Body;
import org.devnation.demo.camel.Service;
import org.apache.camel.builder.RouteBuilder;
import org.json.JSONException;
import org.json.JSONObject;

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
                .convertBodyTo(String.class)
                .delay(5000)
                .log(">> Tweet received : ${body}")
                .bean(Service.class, "messageToJSON")
                .log(">> JSON Tweet : ${body}")
                .bean(Service.class, "store")
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
