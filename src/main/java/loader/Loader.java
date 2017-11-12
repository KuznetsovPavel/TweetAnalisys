package loader;

import org.springframework.social.InternalServerErrorException;
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
    final private String query;

    private Queue<Tweet> queue;
    private long minID;
    private long maxID;

    Loader(final String consumerKey, final String consumerSecret, final String query, final long minID) {
        this.twitter = new TwitterTemplate(consumerKey, consumerSecret);
        this.minID = minID;
        this.query = query;
        final List<Tweet> tweets = twitter.searchOperations().search(query).getTweets();
        this.maxID = Long.parseLong(tweets.get(0).getId());
        loadPage();
    }

    public Tweet load() {
        Tweet tweet = queue.poll();
        int count = 0;
        final int maxNullAnswer = 10;
        while (tweet == null) {
            if (++count >= maxNullAnswer) return null; // if load data unreal
            loadPage();
            tweet = queue.poll();
        }
        return tweet;
    }

    private void loadPage() {
        final int pageSize = 120; // It is max page size
        SearchResults results = null;
        int errCount = 0;
        while (results == null) {
            if (errCount++ >= 10) {
                break;
            }
            try {
                results = twitter.searchOperations().search(query,
                        pageSize, minID + 1, maxID);
                System.out.println("Data received");
                System.out.println();
            } catch (RateLimitExceededException | InternalServerErrorException e) {
                e.printStackTrace();
                results = null;
                System.out.println("Thread sleep...");
                sleep();
            }
        }
        if (results == null || results.getTweets().size() <= 1) {
            queue = new LinkedList<>();
        } else {
            final List<Tweet> tweets = results.getTweets();
            maxID = Long.parseLong(tweets.get(tweets.size() - 1).getId());
            queue = new LinkedList<>(tweets);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(1000 * 60 * 10); // 15 minutes is limit for framework
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
