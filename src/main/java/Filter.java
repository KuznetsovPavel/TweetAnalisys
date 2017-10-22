import checkers.Checker;
import checkers.KeywordChecker;
import checkers.QueryChecker;
import org.springframework.social.twitter.api.Tweet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Filter {
    public static void main(String[] args) {
        final String[] queryArray = {"kotlin", "java", "scala", "groovy", "cpp",
                "swift", "perl", "javascript", "python", "ruby", "haskell", "matlab"};
        try {
            DataAccessObject dao = MongoDAO.createConnect();
            Checker keywordChecker = new KeywordChecker("keywords.txt");
            for(String query: queryArray) {
                Checker queryChecker = new QueryChecker(query);
                List<Tweet> tweets = dao.removeTweets(query);
                tweets = tweets.stream()
                               .filter(queryChecker::check)
                               .filter(keywordChecker::check)
                               .collect(Collectors.toList());
                dao.putTweets(tweets, query);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
