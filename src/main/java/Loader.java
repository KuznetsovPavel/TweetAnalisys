
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.client.RestTemplate;

public class Loader {

    public static void main(final String[] args) {
        final String appId = "0h4ZiKeHW7dMSVFFJx2AlEKaX";
        final String appSecret = "gUdAQssFu6Hk6pbXys3kOIMRAE2pAgyYABQ0MBL3i9XkC63Z9H";
        final int numberOfTweets = 20;
        final String appToken = fetchApplicationAccessToken(appId, appSecret);
        final List<Tweet> tweets = searchTwitter("#java", appToken, numberOfTweets);
        int count = 1;
        for (Tweet tweet : tweets) {
            System.out.println("\ntwit number: " + count++);
            System.out.println(tweet.getText());
        }
    }

    private static List<Tweet> searchTwitter(final String query, final String appToken, final int numberOfTweets) {
        final RestTemplate rest = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + appToken);
        final HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        final Map result = rest.exchange("https://api.twitter.com/1.1/search/tweets.json?q={query}&count={numberOfTweets}",
                HttpMethod.GET, requestEntity, Map.class, query, numberOfTweets).getBody();
        final List<Map<String, ?>> statuses = (List<Map<String, ?>>) result.get("statuses");
        final List<Tweet> tweets = new ArrayList<Tweet>();
        for (Map<String, ?> status : statuses) {
            tweets.add(new Tweet(Long.valueOf(status.get("id").toString()), status.get("text").toString()));
        }
        return tweets;
    }

    private static String fetchApplicationAccessToken(String appId, String appSecret) {
        OAuth2Operations oauth = new OAuth2Template(appId, appSecret, "",
                "https://api.twitter.com/oauth2/token");
        return oauth.authenticateClient().getAccessToken();
    }

}