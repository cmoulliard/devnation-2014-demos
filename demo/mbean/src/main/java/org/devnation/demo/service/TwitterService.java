package org.devnation.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.*;

import java.util.ArrayList;
import java.util.List;

public class TwitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);
    private TwitterFactory tf;
    private Twitter twitter;

    public List<String> searchTweets(String keywords) throws TwitterException {

        List<String> tweets = new ArrayList<String>();
        Query query = new Query(keywords);
        query.setMaxId();
        QueryResult result = null;

        twitter = getTwitterInstance();
        result = twitter.search(query);
        do {
            result = twitter.search(query);
            List<Status> tweetList = result.getTweets();
            for (Status tweet : tweetList) {
                tweets.add("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
                LOGGER.info("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            }
        } while ((query = result.nextQuery()) != null);

        return tweets;

/**        for (Status status : result.getTweets()) {
 String tweet = "@" + status.getUser().getScreenName() + ":" + status.getText();
 tweets.add(tweet);
 LOGGER.info("@" + status.getUser().getScreenName() + ":" + status.getText());
 }

 return a list of statuses

 "statuses": [
 {
 "contributors": null,
 "coordinates": null,
 "created_at": "Thu Mar 27 15:11:34 +0000 2014",
 "entities": {
 "hashtags": [
 {
 "indices": [
 39,
 45
 ],
 "text": "camel"
 },
 {
 "indices": [
 46,
 54
 ],
 "text": "fabric8"
 },
 {
 "indices": [
 55,
 62
 ],
 "text": "hawtio"
 }
 ],
 "symbols": [],
 "urls": [{
 "display_url": "goo.gl/GoGS3L",
 "expanded_url": "http://goo.gl/GoGS3L",
 "indices": [
 99,
 121
 ],
 "url": "http://t.co/IMHYRmjqSG"
 }],
 "user_mentions": [{
 "id": 2231839172,
 "id_str": "2231839172",
 "indices": [
 82,
 96
 ],
 "name": "DevNation",
 "screen_name": "DevNationConf"
 }]
 },
 "favorite_count": 0,
 "favorited": false,
 "geo": null,
 "id": 449202071066398720,
 "id_str": "449202071066398720",
 "in_reply_to_screen_name": null,
 "in_reply_to_status_id": null,
 "in_reply_to_status_id_str": null,
 "in_reply_to_user_id": null,
 "in_reply_to_user_id_str": null,
 "lang": "en",
 "metadata": {
 "iso_language_code": "en",
 "result_type": "recent"
 },
 "place": null,
 "possibly_sensitive": false,
 "retweet_count": 0,
 "retweeted": false,
 "source": "web",
 "text": "I count on you to follow my talk about #camel #fabric8 #hawtio &amp; Social Media @DevNationConf - http://t.co/IMHYRmjqSG",
 "truncated": false,
 "user": {
 "contributors_enabled": false,
 "created_at": "Sun Oct 04 16:19:58 +0000 2009",
 "default_profile": false,
 "default_profile_image": false,
 "description": "Engineer - Architect @RedHat, Apache Committer & PMC (Camel, Karaf, DeltaSpike, AsciiDoctor), Technology Evangelist, Teacher, Speaker, Mountain Biker",
 "entities": {
 "description": {"urls": []},
 "url": {"urls": [{
 "display_url": "cmoulliard.github.io",
 "expanded_url": "http://cmoulliard.github.io",
 "indices": [
 0,
 22
 ],
 "url": "http://t.co/vLW51vSoxI"
 }]}
 },
 "favourites_count": 12,
 "follow_request_sent": false,
 "followers_count": 510,
 "following": false,
 "friends_count": 298,
 "geo_enabled": false,
 "id": 79766620,
 "id_str": "79766620",
 "is_translation_enabled": false,
 "is_translator": false,
 "lang": "en",
 "listed_count": 40,
 "location": "Florennes, Belgium",
 "name": "Charles Moulliard",
 "notifications": false,
 "profile_background_color": "FFFFFF",
 "profile_background_image_url": "http://pbs.twimg.com/profile_background_images/705099061/9713df7343cbde9c94a4de2193db2478.jpeg",
 "profile_background_image_url_https": "https://pbs.twimg.com/profile_background_images/705099061/9713df7343cbde9c94a4de2193db2478.jpeg",
 "profile_background_tile": false,
 "profile_image_url": "http://pbs.twimg.com/profile_images/2679063101/b3f3bd3286f23bb3fd022f336366e15a_normal.png",
 "profile_image_url_https": "https://pbs.twimg.com/profile_images/2679063101/b3f3bd3286f23bb3fd022f336366e15a_normal.png",
 "profile_link_color": "363636",
 "profile_sidebar_border_color": "696969",
 "profile_sidebar_fill_color": "000000",
 "profile_text_color": "696969",
 "profile_use_background_image": true,
 "protected": false,
 "screen_name": "cmoulliard",
 "statuses_count": 1474,
 "time_zone": "Brussels",
 "url": "http://t.co/vLW51vSoxI",
 "utc_offset": 3600,
 "verified": false
 }
 },

 -->
 **/

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
