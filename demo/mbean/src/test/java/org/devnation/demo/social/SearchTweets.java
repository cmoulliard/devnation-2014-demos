package org.devnation.demo.social;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class SearchTweets {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchTweets.class);

    public static void main(String[] args) throws TwitterException {

        Twitter twitter = new TwitterFactory().getInstance();
        QueryResult result = null;

        // WORKS WITH Twitter4j 3.0.x
        // Tweeter API 1.1
        try {
            Query query = new Query("cmoulliard");
            do {
                result = twitter.search(query);
                List<Status> tweets = result.getTweets();
                for (Status tweet : tweets) {
                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                }
            } while ((query = result.nextQuery()) != null);

            query = new Query("cmoulliard");
            result = twitter.search(query);
            for (Status status : result.getTweets()) {
                LOGGER.info("@" + status.getUser().getScreenName() + ":" + status.getText());
            }

            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

}