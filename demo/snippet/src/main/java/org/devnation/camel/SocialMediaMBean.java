package org.devnation.camel;

import twitter4j.TwitterException;

import java.util.List;

// tag::snippetInterface[]
public interface SocialMediaMBean {

    /* Attributes */
    void setTweetsCounter(Integer val);
    public Integer getTweetsCounter();

    /* Operations */
    List<String> searchTweets(String keywords) throws TwitterException;
    String userInfo(String id) throws TwitterException;
    // end::snippetInterface[]

    /* Attributes */
    void setPublishData(Integer content);
    public Integer getPublishData();
}
