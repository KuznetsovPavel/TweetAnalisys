import org.springframework.social.RateLimitExceededException;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Loader {

    final private Twitter twitter;

    private Queue<Tweet> queue;
    private long maxID;

    Loader(final String consumerKey, final String consumerSecret) {
        this.twitter = new TwitterTemplate(consumerKey, consumerSecret);
        //TODO: maxId should be init with DB
        this.maxID = 9999999999999L;
    }

    public Tweet load(final String query) {
        if (queue == null) {
            loadPage(query);
        }
        Tweet tweet = queue.poll();
        if (tweet == null) {
            loadPage(query);
            tweet = queue.poll();
        }
        return tweet;
    }

    private void loadPage(final String query) {
        final int pageSize = 120; // It is max page size
        final int sinceID = 0; // It is min ID
        SearchResults results = null;
        while (results == null) {
            try {
                results = twitter.searchOperations().search(query,
                        pageSize, sinceID, maxID - 1);
            } catch (RateLimitExceededException e) {
                e.printStackTrace();
                System.out.println("Thread sleep...");
                sleep();
            }
        }
        final List<Tweet> tweets = results.getTweets();
        maxID = Long.parseLong(tweets.get(tweets.size() - 1).getId());
        queue = new LinkedList<>(tweets);
    }

    private void sleep() {
        try {
            Thread.sleep(1000 * 60 * 15); // 15 minutes is limit for framework
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
