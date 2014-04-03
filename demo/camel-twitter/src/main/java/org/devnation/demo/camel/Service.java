package org.devnation.demo.camel;

//import io.fabric8.insight.storage.StorageService;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.fusesource.insight.storage.StorageService;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.devnation.demo.camel.InsightUtils.formatDate;
import static org.devnation.demo.camel.InsightUtils.quote;

public class Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

/*    private static final String JSON_PREFIX = "{ ";
    private static final String JSON_SUFFIX = " }";
    private static final String JSON_SPACE_BEFORE = " \"";
    private static final String JSON_SPACE_AFTER = "\" ";
    private static final String JSON_QUOTE = "\"";
    private static final String JSON_COLON = ":";*/
    private static final String MSG_TYPE = "tweet";


    private static StorageService storageService;
    private static String ES_TYPE = "insight-tweet";
    private static MBeanServer mBeanServer;
    private static ObjectName objectName;
    private static Attribute attribute;
    private static Integer tweetsCounter = 0;

    public static void store(@Body String data) {
        storageService.store(ES_TYPE, generateTimeStamp(), data);
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

    /*    public static String messageToJSON(String message) {
        return "{ " +
                "\"tweet\" : \"" + message + "\", " +
                "\"timestamp\" : \"" + Service.formatDate(generateTimeStamp()) + "\"" +
                " }";
    }*/

/*
    public static String messageToJSON2(String message) {
        StringBuilder writer = new StringBuilder();
        writer.append("{ ");
        writer.append("\"tweet\" : ");
        quote(message, writer);
        writer.append(",\n  \"timestamp\" : ");
        quote(Service.formatDate(generateTimeStamp()), writer);
        writer.append(" }");

        return writer.toString();
    }*/

/*    public static String tweetToJSON(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(JSON_PREFIX);
        sb.append(JSON_QUOTE + MSG_TYPE + JSON_QUOTE);
        sb.append(JSON_COLON);
        sb.append(JSON_SPACE_BEFORE + message + JSON_SPACE_AFTER);
        sb.append(JSON_SUFFIX);
        return sb.toString();
    }*/


    /*    public String generateMessage(@Body String message) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("tweet",message);
        return json.toString();
    }*/

    public static void increaseCounter(Exchange exchange) throws MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, InvalidAttributeValueException {

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
    }

    public void setStorageService(StorageService storageService) {
        Service.storageService = storageService;
    }

}
