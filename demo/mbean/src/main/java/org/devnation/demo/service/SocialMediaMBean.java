package org.devnation.demo.service;

import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.List;

public interface SocialMediaMBean {

    /* Attributes */
    void setPublishData(Integer content);
    public Integer getPublishData();

    /*void setSearchTweets(String keywords);
    public String[] getSearchTweets();*/

    /* Operations */
    void sayHello();
    List<String> searchTweets(String keywords) throws TwitterException;
}
