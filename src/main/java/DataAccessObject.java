import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.List;

public interface DataAccessObject{

    long getMinID(final String query);

    void putMinID(final String query, final long id);

    void putTweet(final Tweet tweet, String query);

    void putUser(final TwitterProfile user);

    List<Tweet> removeTweets(String query);

    void putTweets(List<Tweet> tweets, String query);
}
