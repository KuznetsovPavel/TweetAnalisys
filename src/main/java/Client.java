import org.springframework.social.twitter.api.Tweet;

import java.io.IOException;
import java.sql.SQLException;

public class Client {

    public static void main(String[] args) {
        final String consumerKey = "0h4ZiKeHW7dMSVFFJx2AlEKaX";
        final String consumerSecret = "gUdAQssFu6Hk6pbXys3kOIMRAE2pAgyYABQ0MBL3i9XkC63Z9H";
        //final String[] queryArray = {"java", "kotlin", "scala", "groovy", "cpp",
        //        "swift", "perl", "javascript", "python", "ruby"};
        final String[] queryArray = {"scala", "groovy", "cpp",
                "swift", "perl", "javascript", "python", "ruby"};
        long testcount = 0;
        try (DataAccessObject dao = PostgreDAO.createDAO()) {
            for (String query : queryArray) {
                final long minID = dao.getMinID(query);
                Loader loader = new Loader(consumerKey, consumerSecret, query, minID);
                Tweet tweet = loader.load();
                dao.putMinID(query, Long.parseLong(tweet.getId()));
                while (true){
                    if (tweet == null) break;
                    dao.putTweet(tweet);
                    dao.putHashTag(tweet.getEntities().getHashTags(), Long.parseLong(tweet.getId()));
                    dao.putUser(tweet.getUser());
                    tweet = loader.load();
                    if (++testcount % 500 == 0) {
                        System.out.println();
                        System.out.println();
                        System.out.println(testcount);
                        System.out.println(query);
                        System.out.println(tweet.getCreatedAt());
                        System.out.println();
                        System.out.println();
                        System.out.println();
                        System.out.println();
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
    }

}
