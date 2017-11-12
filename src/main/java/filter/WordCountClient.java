package filter;

import db.DataAccessObject;
import db.MongoDAO;
import model.SimpleTweet;

import java.util.*;
import java.util.stream.Collectors;

public class WordCountClient {
    public static void main(String[] args) {
        final String query = "ruby";
        DataAccessObject dao = MongoDAO.createConnect();
        List<SimpleTweet> tweets = dao.getSimpleTweets(query);
        List<String> words = tweets.stream()
                                   .filter(e -> e.getLang().equals("en"))
                                   .map(SimpleTweet::getText)
                                   .flatMap(e -> Arrays.stream(e.split("\\W")))
                                   .collect(Collectors.toList());
        Map<String, Integer> wordCountMap = new HashMap<>();
        for(String word: words){
            Integer count = wordCountMap.putIfAbsent(word, 1);
            if (count != null)
                wordCountMap.put(word, count + 1);
        }
        List<Map.Entry<String,Integer>> wordCountList = new ArrayList<>(wordCountMap.entrySet());
        wordCountList.sort(Map.Entry.comparingByValue());
        Collections.reverse(wordCountList);
        wordCountList.stream().limit(1000).forEach(System.out::println);
        dao.putSimpleTweets(tweets, query);
    }
}
