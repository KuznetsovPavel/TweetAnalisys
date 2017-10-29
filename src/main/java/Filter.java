import checkers.Checker;
import checkers.KeywordChecker;
import checkers.QueryChecker;
import model.SimpleTweet;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    public static void main(String[] args) {
        final String[] queryArray = {"kotlin", "java", "scala", "groovy", "cpp",
                "swift", "perl", "javascript", "python", "ruby", "haskell", "matlab"};
        try {
            DataAccessObject dao = MongoDAO.createConnect();
            int count = 0;
            for(String query: queryArray) {
                System.out.printf("%s: start filtering for query %s...%n", LocalTime.now(), query);
                Checker queryChecker = new QueryChecker(query);
                Checker keywordChecker = new KeywordChecker("filter/keywords.txt", "filter/config.txt", query);
                List<SimpleTweet> tweets = dao.getSimpleTweets(query);
                count += tweets.size();
                tweets = tweets.stream()
                               .filter(queryChecker::check)
                               .filter(keywordChecker::check)
                               .collect(Collectors.toList());
                dao.putSimpleTweets(tweets, query);
                System.out.printf("%s: %d tweets is filtered%n", LocalTime.now(), count);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
