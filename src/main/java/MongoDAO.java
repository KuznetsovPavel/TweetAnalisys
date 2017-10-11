import com.mongodb.*;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.net.UnknownHostException;
import java.util.List;

public class MongoDAO implements DataAccessObject{

    private static Mongo mongo;
    private static DB db;
    private static MongoDAO dao;

    private DBCollection lastTweetIDCollection;
    private DBCollection tweetsCollection;
    private DBCollection usersCollection;

    public static DataAccessObject createConnect() throws UnknownHostException {
        if (dao != null) return dao;
        mongo = new Mongo();
        db = mongo.getDB("tweetdata");
        db.dropDatabase();
        db = mongo.getDB("tweetdata");
        dao = new MongoDAO();
        return dao;
    }

    private MongoDAO() {
        lastTweetIDCollection = db.getCollection("lastTweetID");
        tweetsCollection = db.getCollection("tweets");
        usersCollection = db.getCollection("users");
    }

    @Override
    public long getMinID(String query) {
        final BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("proglang" , query);
        final DBCursor cursor = lastTweetIDCollection.find(dbObject);
        Long answer;
        if (cursor.hasNext()) {
            answer = (long) cursor.next().get("tweetID");
        } else {
            BasicDBObject doc = new BasicDBObject();
            doc.put("proglang", query);
            doc.put("tweetID", 0L);
            lastTweetIDCollection.insert(doc);
            answer = 0L;
        }
        return answer;
    }

    @Override
    public void putMinID(String query, long id) {
        final BasicDBObject dbObjectQuery = new BasicDBObject();
        dbObjectQuery.put("proglang" , query);
        final BasicDBObject dbObjectUpdate = new BasicDBObject();
        dbObjectUpdate.put("proglang" , query);
        dbObjectUpdate.put("tweetID" , id);
        lastTweetIDCollection.update(dbObjectQuery ,dbObjectUpdate, true, false);
    }

    @Override
    public void putTweet(Tweet tweet, String query) {
        final BasicDBObject newTweet = new BasicDBObject();
        newTweet.put("Id" , tweet.getId());
        newTweet.put("forQuery", query);
        newTweet.put("text", tweet.getText());
        newTweet.put("createdAt", tweet.getCreatedAt());
        newTweet.put("languageCode", tweet.getLanguageCode());
        newTweet.put("favoriteCount", tweet.getFavoriteCount());
        newTweet.put("fromUser", tweet.getFromUser());
        newTweet.put("fromUserId", tweet.getFromUserId());
        newTweet.put("retweetCount", tweet.getRetweetCount());
        final BasicDBObject tags = new BasicDBObject();
        final List<HashTagEntity> taglist = tweet.getEntities().getHashTags();
        int id = 0;
        for (HashTagEntity tag : taglist) {
            tags.put("tag_" + id++, tag.getText());
        }
        newTweet.put("tags", tags);
        tweetsCollection.insert(newTweet);
    }

    @Override
    public void putUser(TwitterProfile user) {
        final BasicDBObject newUser = new BasicDBObject();
        newUser.put("Id" , user.getId());
        newUser.put("createdDate", user.getCreatedDate());
        newUser.put("followersCount", user.getFollowersCount());
        newUser.put("friendsCount", user.getFriendsCount());
        newUser.put("language", user.getLanguage());
        newUser.put("location", user.getLocation());
        newUser.put("statusesCount", user.getStatusesCount());
        newUser.put("timeZone", user.getTimeZone());
        newUser.put("favoritesCount", user.getFavoritesCount());
        newUser.put("name", user.getName());
        newUser.put("screenName", user.getScreenName());
        newUser.put("utcOffset", user.getUtcOffset());
        usersCollection.insert(newUser);
    }

}
