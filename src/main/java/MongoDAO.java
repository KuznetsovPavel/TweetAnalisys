import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDAO implements DataAccessObject{

    private static MongoDAO dao;
    private static MongoDatabase database;

    private MongoCollection lastTweetIDCollection;
    private MongoCollection tweetsCollection;
    private MongoCollection usersCollection;
    private MongoCollection thematicTweetsCollection;

    public static DataAccessObject createConnect() {
        if (dao != null) return dao;
        database = new MongoClient().getDatabase("tweetdata");
        dao = new MongoDAO();
        dao.disableLogs();
        return dao;
    }

    private MongoDAO() {
        lastTweetIDCollection = database.getCollection("lastTweetID");
        tweetsCollection = database.getCollection("tweets");
        usersCollection = database.getCollection("users");
        thematicTweetsCollection = database.getCollection("thematicTweets");
    }

    private void disableLogs() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.OFF);
    }


    @Override
    public long getMinID(String query) {
        final Document doc = new Document("proglang", query);
        final MongoCursor cursor = lastTweetIDCollection.find(doc).iterator();
        final Long answer;
        if (cursor.hasNext()) {
            final Document document = (Document) cursor.next();
            answer = (long) document.get("tweetID");
        } else {
            final Document document = new Document("proglang", query).append("tweetID", 0L);
            lastTweetIDCollection.insertOne(document);
            answer = 0L;
        }
        return answer;
    }

    @Override
    public void putMinID(String query, long id) {
        lastTweetIDCollection.updateOne(eq("proglang", query),
                new Document("$set", new Document("tweetID", id)));
    }

    @Override
    public void putTweet(Tweet tweet, String query) {
        final Document newTweet = new Document();
        newTweet.put("Id" , tweet.getId());
        newTweet.put("forQuery", query);
        newTweet.put("text", tweet.getText());
        newTweet.put("createdAt", tweet.getCreatedAt());
        newTweet.put("languageCode", tweet.getLanguageCode());
        newTweet.put("favoriteCount", tweet.getFavoriteCount());
        newTweet.put("fromUser", tweet.getFromUser());
        newTweet.put("fromUserId", tweet.getFromUserId());
        newTweet.put("retweetCount", tweet.getRetweetCount());
        final Document tags = new Document();
        final List<HashTagEntity> taglist = tweet.getEntities().getHashTags();
        int id = 0;
        for (HashTagEntity tag : taglist) {
            tags.put("tag_" + id++, tag.getText());
        }
        newTweet.put("tags", tags);
        final Document filter = new Document("Id", tweet.getId()).append("forQuery", query);
        tweetsCollection.replaceOne(filter, newTweet, new UpdateOptions().upsert(true));
    }

    @Override
    public void putUser(TwitterProfile user) {
        final Document newUser = new Document();
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
        final Document filter = new Document("Id", user.getId());
        usersCollection.replaceOne(filter, newUser, new UpdateOptions().upsert(true));
    }

    @Override
    public List<Tweet> removeTweets(String query) {
        return null;
    }

    @Override
    public void putTweets(List<Tweet> tweets, String query) {

    }

    @Override
    public List<SimpleTweet> getSimpleTweets(String query) {
        final Document doc = new Document("forQuery", query);
        final MongoCursor cursor = tweetsCollection.find(doc).iterator();
        List<SimpleTweet> list = new ArrayList<>();
        if (cursor.hasNext()) {
            final Document document = (Document) cursor.next();
            final SimpleTweet tweet = new SimpleTweet(document.getLong("Id"),
                    document.getString("forQuery"),
                    document.getString("text"),
                    (Date)document.get("createdAt"),
                    document.getString("languageCode"),
                    document.getInteger("favoriteCount"),
                    document.getString("fromUser"),
                    document.getLong("fromUserId"),
                    document.getInteger("retweetCount"),
                    null);
            //TODO: add get tweet tags
            list.add(tweet);
        }
        return list;
    }

    @Override
    public void putSimpleTweets(List<SimpleTweet> tweets, String query) {
        for (SimpleTweet tweet : tweets) {
            final Document newTweet = new Document();
            newTweet.put("Id" , tweet.getId());
            newTweet.put("forQuery", query);
            newTweet.put("text", tweet.getText());
            newTweet.put("createdAt", tweet.getDate());
            newTweet.put("languageCode", tweet.getLang());
            newTweet.put("favoriteCount", tweet.getFavoriteCount());
            newTweet.put("fromUser", tweet.getFromUser());
            newTweet.put("fromUserId", tweet.getFromUserId());
            newTweet.put("retweetCount", tweet.getRetweetCount());
            final Document tags = new Document();
            final List<String> taglist = tweet.getTags();
            int id = 0;
            for (String tag : taglist) {
                tags.put("tag_" + id++, tag);
            }
            newTweet.put("tags", tags);
            thematicTweetsCollection.insertOne(newTweet);
        }
    }
}
