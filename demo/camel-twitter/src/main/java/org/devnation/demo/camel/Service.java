package org.devnation.demo.camel;

//import io.fabric8.insight.storage.StorageService;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.fusesource.insight.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;

import twitter4j.Place;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.devnation.demo.camel.InsightUtils.formatDate;
import static org.devnation.demo.camel.InsightUtils.quote;

public class Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private static final String MSG_TYPE = "tweet";

    private static StorageService storageService;
    private static String ES_TYPE = "insight-tweet";
    private static MBeanServer mBeanServer;
    private static ObjectName objectName;
    private static Attribute attribute;
    private static Integer tweetsCounter = 0;

    /*public static void store(@Body String data) {*/
    public static void store(@Header("tweet-full") String data) {
        storageService.store(ES_TYPE, generateTimeStamp(), data);
    }

    public static String getJSONTweetText(@Body Status status) {
        return tweetToJSON(status.getText());
    }

    public static String getJSONTweet(@Body Status status) {

        Place place = status.getPlace();
        String country = ((place == null)? "" : place.getCountry());

        String data = "{ " +
                " \"" + "timestamp" + "\" : \"" + formatDate(generateTimeStamp()) + "\"," +
                " \"" + "createdAt" + "\" : \"" + status.getCreatedAt().toString() + "\"," +
                " \"" + "id" + "\" : \"" + status.getId() + "\"," +
                " \"" + "text" + "\" : \"" + status.getText() + '\'' + "\"," +
                " \"" + "isFavorited" + "\" : \"" + status.isFavorited() + "\"," +
                " \"" + "isRetweeted" + "\" : \"" + status.isRetweeted() + "\"," +
                " \"" + "favoriteCount" + "\" : \"" + status.getFavoriteCount() + "\"," +
                " \"" + "inReplyToScreenName" + "\" : \"" + status.getInReplyToScreenName() + '\'' + "\"," +
                " \"" + "geoLocation" + "\" : \"" + status.getGeoLocation() + "\"," +
                " \"" + "place" + "\" : \"" + status.getPlace() + "\"," +
                " \"" + "retweetCount" + "\" : \"" + status.getRetweetCount() + "\"," +
                " \"" + "isoLanguageCode" + "\" : \"" + status.getIsoLanguageCode() + "\"," +
                " \"" + "user" + "\" : \"" + status.getUser().getName() + "\"," +
                " \"" + "country" + "\" : \"" + country + "\"" +
                " }";
        return data;
    }

    public static String tweetToJSON(String message) {
        StringBuilder writer = new StringBuilder();
        writer.append("{ \"tweet\" : ");
        quote(message, writer);
        writer.append(", \"timestamp\" : ");
        quote(formatDate(System.currentTimeMillis()), writer);
        writer.append(" }");
        return writer.toString();
    }

    public static Long generateTimeStamp() {
        Date date = new java.util.Date();
        return new Timestamp(date.getTime()).getTime();
    }

/*    public static void increaseCounter(Exchange exchange) throws MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, InvalidAttributeValueException {

        objectName = new ObjectName("hawtio:type=SocialMedia");

        Object obj = (String) exchange.getIn().getBody();

        if(obj!= null) {
            // Get Management server
            if (mBeanServer == null) {
                mBeanServer = ManagementFactory.getPlatformMBeanServer();
            }

            // Set attribute with Content
            attribute = new Attribute("TweetsCounter", tweetsCounter++);

            // Publish it
            mBeanServer.setAttribute(objectName, attribute);
        }
    }*/

    public void setStorageService(StorageService storageService) {
        Service.storageService = storageService;
    }

}
