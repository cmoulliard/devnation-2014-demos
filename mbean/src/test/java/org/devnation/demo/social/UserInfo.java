package org.devnation.demo.social;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.util.List;

public class UserInfo {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfo.class);

    public static void main(String[] args) throws TwitterException {

        Twitter twitter = new TwitterFactory().getInstance();

        try {
/*            String result = twitter.getScreenName();
            LOGGER.info("Screen name : " + result);*/

            ResponseList<User> users = twitter.searchUsers("cmoulliard",-1);
            User user = users.get(0);
            System.out.println(
                    "User info Name : " + user.getName()
                    + ", TimeZone : " + user.getTimeZone()
                    + ", Language : " + user.getLang()
                    + ", Description : " + user.getDescription()
                    + ", Location : " + user.getLocation()
                    + ", Followers : " + user.getFollowersCount()
                    + ", Friends : " + user.getFriendsCount()
                    + ", Favourited : " + user.getFavouritesCount()
                    + ", Tweets : " + user.getStatusesCount()
            );

            Gson gson = new Gson();
            String userJSON = gson.toJson(user);
            System.out.println("User JSON : " + userJSON);

/*            IDs ids = twitter.getFollowersIDs("cmoulliard", -1);
            System.out.println("Followers : " + ids.getIDs().length);

            ids = twitter.getFriendsIDs("cmoulliard", -1);
            System.out.println("Follow : " + ids.getIDs().length);

            ResponseList<Status> response = twitter.getMentionsTimeline();
            System.out.println("Mentions : " + response.size());

            ResponseList<Status> fav = twitter.getFavorites("cmoulliard");
            System.out.println("Favorited : " + fav.size());

            ResponseList<Status> tweets = twitter.getUserTimeline("cmoulliard");
            System.out.println("Nber tweets : " + tweets.size());*/



            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
    }

}