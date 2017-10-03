import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.List;

public class NewLoad {

    public static void main(final String[] args) {
        final String consumerKey = "0h4ZiKeHW7dMSVFFJx2AlEKaX";
        final String consumerSecret = "gUdAQssFu6Hk6pbXys3kOIMRAE2pAgyYABQ0MBL3i9XkC63Z9H";
        final Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret);
        final String query = "java";
        final int pageSize = 100;
        final int sinceID = 0;
        long maxID = twitter.searchOperations().search(query).getSearchMetadata().getMaxId();
        final int numberOfTweets = 1000;
        int count = 0;
        for (int i = 0; i < numberOfTweets / pageSize; i++) {
            final SearchResults results = twitter.searchOperations().search(query,
                    pageSize, sinceID, maxID - 1);
            final List<Tweet> tweets = results.getTweets();
            for (Tweet tweet : tweets) {
                System.out.println("Tweet: " + ++count);
                System.out.println(tweet.getCreatedAt());
                System.out.println(tweet.getId());
                System.out.println(tweet.getText());
                System.out.println();
                maxID = Long.parseLong(tweet.getId());
            }

        }
    }
}
