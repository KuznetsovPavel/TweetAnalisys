package filter;

import db.DataAccessObject;
import db.MongoDAO;
import filter.checkers.Checker;
import filter.checkers.KeywordChecker;
import filter.checkers.QueryChecker;
import filter.checkers.WordNumberChecker;
import model.SimpleTweet;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrintClient {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            System.out.println("Query:");
            String query = scanner.nextLine();
            System.out.println("Language code:");
            String lang = scanner.nextLine();
            System.out.println("Frequency (1 of ?):");
            int freq = Integer.parseInt(scanner.nextLine());
            Function<String, String> onlyTextMapper = OnlyTextMapper.getInstance(true);
            Checker wordNumberChecker = new WordNumberChecker();
            Checker queryChecker = new QueryChecker(query);
            DataAccessObject dao = MongoDAO.createConnect();
            List<SimpleTweet> tweets = dao.getSimpleTweets(query);
            List<String> tweetTexts = tweets.stream()
                                            .filter(e -> e.getLang().equals(lang))
                                            .map(SimpleTweet::getText)
                                            .filter(wordNumberChecker::check)
                                            .filter(queryChecker::check)
                                            .map(onlyTextMapper)
                                            .collect(Collectors.toList());
            int count = 0;
            for(String text: tweetTexts){
                if(count % freq == 0)
                    System.out.format("%s%n----------------------%n", text);
            }
        }
    }
}
