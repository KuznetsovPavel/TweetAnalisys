package filter;

import filter.checkers.Checker;
import filter.checkers.KeywordChecker;
import filter.checkers.QueryChecker;
import db.DataAccessObject;
import db.MongoDAO;
import filter.checkers.WordNumberChecker;
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
                Checker wordNumberChecker = new WordNumberChecker();
                List<SimpleTweet> tweets = dao.getSimpleTweets(query);
                count += tweets.size();
                tweets = tweets.stream()
                               .filter(wordNumberChecker::check)
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
