package org.devnation.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class TwitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);
    private org.devnation.camel.TwitterFactory tf;
    private Twitter twitter;

    public List<String> searchTweets(String keywords) throws TwitterException {

        List<String> tweets = new ArrayList<String>();
        Query query = new Query(keywords);
        QueryResult result = null;

        twitter = getTwitterInstance();

        do {
            result = twitter.search(query);
            List<Status> tweetList = result.getTweets();
            for (Status tweet : tweetList) {
                String message = "@" + tweet.getUser().getScreenName() + " - " + tweet.getText();
                //tweets.add(message);
                LOGGER.debug(message);

                StringBuilder builder = new StringBuilder();
                builder.append("{\"tweet\":\"");
                builder.append(message);
                builder.append("\"}");

                //tweets.add(builder.toString());
                tweets.add(message);

            }
        } while ((query = result.nextQuery()) != null);

        return tweets;
    }

    private Twitter getTwitterInstance() {
        if (twitter != null) {
            return twitter;
        } else {
            twitter = tf.getInstance();
        }
        return twitter;
    }


    public void setTwitterFactory(TwitterFactory tf) {
        this.tf = tf;
    }
}
