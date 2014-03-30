package org.devnation.demo.camel;

//import io.fabric8.insight.storage.StorageService;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.fusesource.insight.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.util.Date;

public class Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    private static StorageService storageService;
    private static String ES_TYPE = "tweet";
    private static MBeanServer mBeanServer;
    private static ObjectName objectName;
    private static Attribute attribute;
    private static Integer tweetsCounter = 0;

    public static void store(String data) {
        storageService.store(ES_TYPE, generateTimeStamp(), data);
    }

    public static String tweetToJSON(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"tweet\":\"");
        builder.append(message);
        builder.append("\"}");
        return builder.toString();
    }

    public static Long generateTimeStamp() {
        Date date = new java.util.Date();
        return new Timestamp(date.getTime()).getTime();
    }

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
