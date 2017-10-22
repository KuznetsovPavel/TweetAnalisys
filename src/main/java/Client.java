import org.springframework.social.twitter.api.Tweet;

public class Client {

    public static void main(String[] args) {
        final String consumerKey = "0h4ZiKeHW7dMSVFFJx2AlEKaX";
        final String consumerSecret = "gUdAQssFu6Hk6pbXys3kOIMRAE2pAgyYABQ0MBL3i9XkC63Z9H";
        final String[] queryArray = {"kotlin", "java", "scala", "groovy", "cpp",
                "swift", "perl", "javascript", "python", "ruby", "haskell", "matlab"};
        //final String[] queryArray = {"perl", "javascript", "python", "ruby", "haskell", "matlab"};
        long testcount = 0;
        DataAccessObject dao = MongoDAO.createConnect();
        for (String query : queryArray) {
            final long minID = dao.getMinID(query);
            //final long minID = 921034459947982849L;
            final Loader loader = new Loader(consumerKey, consumerSecret, query, minID);
            Tweet tweet = loader.load();
            //dao.putMinID(query, Long.parseLong(tweet.getId()));
            while (true) {
                if (tweet == null) break;
                dao.putTweet(tweet, query);
                dao.putUser(tweet.getUser());
                tweet = loader.load();
                if (++testcount % 500 == 0) {
                    System.out.println("\n\n");
                    System.out.println("load tweet: " + testcount);
                    System.out.println("for query: " + query);
                    System.out.println("last date: " + tweet.getCreatedAt());
                    System.out.println("\n\n");
                }
            }
        }
    }

}
