package org.devnation.camel;

import twitter4j.TwitterException;

import java.util.List;

public interface SocialMediaMBean {

    /* Attributes */
    void setPublishData(Integer content);
    public Integer getPublishData();

    // tag::snippetInterface[]
    /* Attributes */
    void setTweetsCounter(Integer val);
    public Integer getTweetsCounter();

    /* Operations */
    List<String> searchTweets(String keywords) throws TwitterException;
    String userInfo(String id) throws TwitterException;
    // end::snippetInterface[]
}
