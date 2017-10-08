import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.Collections;
import java.util.List;

public class NewLoad {

    public static void main(final String[] args) {
        final String consumerKey = "0h4ZiKeHW7dMSVFFJx2AlEKaX";
        final String consumerSecret = "gUdAQssFu6Hk6pbXys3kOIMRAE2pAgyYABQ0MBL3i9XkC63Z9H";
        final Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret);
        final String query = "#java";
        final int pageSize = 120;
        final int sinceID = 0;
        long maxID = twitter.searchOperations().search(query).getSearchMetadata().getMaxId();
        int count = 0;
        for (int i = 0; i < 2000; i++) {
            final SearchResults results = twitter.searchOperations().search(query,
                    pageSize, sinceID, maxID - 1);
            final List<Tweet> tweets = results.getTweets();
            Collections.reverse(tweets);
            maxID = Long.parseLong(tweets.get(0).getId());
            System.out.println(count++);


        }
    }
}
