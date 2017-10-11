import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;


public interface DataAccessObject{

    long getMinID(final String query);

    void putMinID(final String query, final long id);

    void putTweet(final Tweet tweet, String query);

    void putUser(final TwitterProfile user);
}
