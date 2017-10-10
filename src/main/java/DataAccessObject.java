import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface DataAccessObject extends Closeable{
    long getMinID(final String query) throws SQLException;

    void putMinID(final String query, final long id) throws SQLException;

    void putTweet(final Tweet tweet) throws SQLException;

    void putUser(final TwitterProfile user) throws SQLException;

    boolean isExistUser(final long id) throws SQLException;

    void putHashTag(final List<HashTagEntity> hashTags, final long id) throws SQLException;
}
