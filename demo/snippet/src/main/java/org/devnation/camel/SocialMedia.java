package org.devnation.camel;

import org.devnation.camel.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.List;

public class SocialMedia implements SocialMediaMBean {

    private static final transient Logger LOG = LoggerFactory.getLogger(SocialMedia.class);

    private MBeanServer mBeanServer;
    private ObjectName objectName;
    private Integer result;
    private TwitterService twitterService;
    private Integer counter;

    public void setPublishData(Integer content) {
        this.result = content;
    }

    public Integer getPublishData() {
        return this.result;
    }

    public void setTweetsCounter(Integer val) {
        this.counter = val;
    }

    public Integer getTweetsCounter() {
        return this.counter;
    }

    @Override
    public String userInfo(String id) throws TwitterException {
        return null;
    }

    // tag::snippetService[]
    public List<String> searchTweets(String keywords) throws TwitterException {
        return twitterService.searchTweets(keywords); 
    }

    public void init() {
        try {
            if (objectName == null) {
                objectName = new ObjectName("hawtio:type=SocialMedia");
            }
            if (mBeanServer == null) {
                mBeanServer = ManagementFactory.getPlatformMBeanServer();
            }
            try {
                mBeanServer.registerMBean(this, objectName);
            } catch (InstanceAlreadyExistsException iaee) {
                // Try to remove and re-register
                LOG.info("Re-registering SchemaLookup MBean");
                mBeanServer.unregisterMBean(objectName);
                mBeanServer.registerMBean(this, objectName);
            }
        } catch (Exception e) {
            LOG.warn("Exception during initialization: ", e);
            throw new RuntimeException(e);
        }
    }
    // end::snippetService[]

    public void destroy() {
        try {
            if (objectName != null && mBeanServer != null) {
                mBeanServer.unregisterMBean(objectName);
            }
        } catch (Exception e) {
            LOG.warn("Exception unregistering mbean: ", e);
            throw new RuntimeException(e);
        }
    }

    public void setTwitterService(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

}
