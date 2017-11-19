package filter;

import db.DataAccessObject;
import db.MongoDAO;
import filter.checkers.Checker;
import filter.checkers.KeywordChecker;
import filter.checkers.QueryChecker;
import filter.checkers.WordNumberChecker;
import model.SimpleTweet;

import java.io.IOException;
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
            System.out.println("Remaining tweets or not?");
            final boolean remaining = scanner.nextBoolean();
            System.out.println("Language codes:");
            scanner.nextLine();
            String langs[] = scanner.nextLine().split(" ");
            System.out.println("Include them or not?");
            final boolean includeLangs = scanner.nextBoolean();
            System.out.println("Frequency (1 of ?):");
            scanner.nextLine();
            int freq = Integer.parseInt(scanner.nextLine());
            Function<String, String> onlyTextMapper = OnlyTextMapper.getInstance(true);
            Checker wordNumberChecker = new WordNumberChecker();
            Checker queryChecker = new QueryChecker(query);
            Checker keywordChecker = new KeywordChecker("filter/keywords.txt", "filter/config.txt", query);
            DataAccessObject dao = MongoDAO.createConnect();
            List<SimpleTweet> tweets = dao.getSimpleTweets(query);
            List<String> tweetTexts = tweets.stream()
                                            .filter(e -> includeLangs
                                                    ? Arrays.stream(langs).anyMatch(l -> l.equals(e.getLang()))
                                                    : Arrays.stream(langs).noneMatch(l -> l.equals(e.getLang()))
                                            )
                                            .filter(wordNumberChecker::check)
                                            .filter(queryChecker::check)
                                            .filter(e -> remaining == keywordChecker.check(e))
                                            .map(SimpleTweet::getText)
                                            .map(onlyTextMapper)
                                            .collect(Collectors.toList());
            int count = 0;
            for(String text: tweetTexts){
                if(count % freq == 0)
                    System.out.format("%s%n----------------------%n", text);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
