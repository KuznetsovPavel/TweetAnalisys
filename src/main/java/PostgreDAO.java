import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.TwitterProfile;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PostgreDAO implements DataAccessObject{

    private Connection connection;
    private PreparedStatement statementGetMinID;
    private PreparedStatement statementPutMinID;
    private PreparedStatement statementPutTweet;
    private PreparedStatement statementPutUser;
    private PreparedStatement statementIsExistUser;
    private PreparedStatement statementPutHashTag;

    private PostgreDAO(){}

    public static PostgreDAO createDAO() throws ClassNotFoundException, SQLException {
        PostgreDAO dao = new PostgreDAO();
        final String url = "jdbc:postgresql:tweetdata";
        final String name = "postgres";
        final String password = "postgres";
        Class.forName("org.postgresql.Driver");
        dao.connection = DriverManager.getConnection(url, name, password);
        dao.statementGetMinID = dao.connection.prepareStatement(
                "select tweetidinsite from lasttweet where progLang = ?");
        dao.statementPutMinID = dao.connection.prepareStatement(
                "update lastTweet set tweetIDinSite = ? where progLang = ?");
        dao.statementPutTweet = dao.connection.prepareStatement(
                "insert into tweet (text, lang, date, retweet, favorite, profile) " +
                        "values (?, ?, ?::timestamp with time zone, ?, ?, ?)");
        dao.statementPutUser = dao.connection.prepareStatement(
                "insert into profile values (?, ?, ?::date, ?, ?, ?, ?, ?, ?, ?) " +
                        "on conflict (userID) " +
                        "do update set " +
                        "location = default, " +
                        "date = default," +
                        "lang = default, " +
                        "statuses = excluded.statuses, " +
                        "friends = excluded.friends," +
                        "followers = excluded.followers, " +
                        "favorites = excluded.favorites, " +
                        "verified = excluded.verified," +
                        "timeZone = default");
        dao.statementIsExistUser = dao.connection.prepareStatement(
                "select exists (select * from profile where userID = ?)");
        dao.statementPutHashTag = dao.connection.prepareStatement(
                "insert into hashTag values (?, ?)");
        return dao;
    }

    @Override
    public long getMinID(final String query) throws SQLException {
        statementGetMinID.setString(1, query);
        final ResultSet result = statementGetMinID.executeQuery();
        result.next();
        String resultString = result.getString(1);
        return Long.parseLong(resultString);
    }

    @Override
    public void putMinID(final String query, final long id) throws SQLException {
        statementPutMinID.setLong(1, id);
        statementPutMinID.setString(2, query);
        statementPutMinID.executeUpdate();
    }

    @Override
    public void putTweet(final Tweet tweet) throws SQLException {
        statementPutTweet.setString(1, prepapeString(tweet.getText(), 150));
        statementPutTweet.setString(2, prepapeString(tweet.getLanguageCode(), 5));
        String date = tweet.getCreatedAt().toString();
        // cast to type timestamp with time zone
        date = date.substring(0, 20) + date.substring(24) + " "+ date.substring(20, 23);
        statementPutTweet.setString(3, date);
        statementPutTweet.setInt(4, tweet.getRetweetCount());
        statementPutTweet.setInt(5, tweet.getFavoriteCount());
        statementPutTweet.setLong(6, tweet.getFromUserId());
        statementPutTweet.executeUpdate();
    }

    @Override
    public void putUser(final TwitterProfile user) throws SQLException {
        statementPutUser.setLong(1, user.getId());
        statementPutUser.setString(2, prepapeString(user.getLocation(), 30));
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        final String month = dateFormat.format(user.getCreatedDate());
        final String date = user.getCreatedDate().toString();
        statementPutUser.setString(3, date.substring(24) + "-" + month + "-" + date.substring(8, 10));
        statementPutUser.setString(4, prepapeString(user.getLanguage(), 5));
        statementPutUser.setInt(5, user.getStatusesCount());
        statementPutUser.setInt(6, user.getFriendsCount());
        statementPutUser.setInt(7, user.getFollowersCount());
        statementPutUser.setInt(8, user.getFollowersCount());
        statementPutUser.setBoolean(9, user.isVerified());
        statementPutUser.setString(10, prepapeString(user.getTimeZone(), 30));
        statementPutUser.executeUpdate();
    }

    @Override
    public boolean isExistUser(final long id) throws SQLException {
        statementIsExistUser.setLong(1, id);
        final ResultSet result = statementIsExistUser.executeQuery();
        result.next();
        String res = result.getString(1);
        return Boolean.parseBoolean(res);
    }

    @Override
    public void putHashTag(final List<HashTagEntity> hashTags, final long id) throws SQLException {
        for (HashTagEntity hashTag : hashTags) {
            statementPutHashTag.setLong(1, id);
            statementPutHashTag.setString(2, prepapeString(hashTag.getText(), 20));
            statementPutHashTag.executeUpdate();
        }
    }


    private String prepapeString(final String s, final int size) {
        if (s == null) return "";
        if (s.length() > size)
            return s.substring(0, size);
        return  s;
    }

    @Override
    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
