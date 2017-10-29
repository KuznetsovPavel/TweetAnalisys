package checkers;

import model.SimpleTweet;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordChecker implements Checker {
    private final List<Keyword> keywordList = new ArrayList<>();
    private final Set<String> badLanguages = new HashSet<>();

    public KeywordChecker(String keywordFile, String configFile, String query) throws IOException {
        final ClassPathResource keywordResource = new ClassPathResource(keywordFile);
        try(BufferedReader reader = new BufferedReader(new FileReader(keywordResource.getFile()))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                String word = split[0];
                boolean good = split[1].equals("+");
                int priority = Integer.parseInt(split[2]);
                Keyword keyword = new Keyword(word, good, priority);
                keywordList.add(keyword);
            }
        }
        Collections.sort(keywordList);
        final ClassPathResource configResource = new ClassPathResource(configFile);
        try(BufferedReader reader = new BufferedReader(new FileReader(configResource.getFile()))){
            String line;
            while((line = reader.readLine()) != null) {
                String[] split = line.split(" ");
                if (split[0].equals(query)){
                    for(int i = 1; i < split.length; ++i)
                        badLanguages.add(split[i]);
                }
            }
        }
    }

    @Override
    public boolean check(SimpleTweet tweet) {
        if (badLanguages.contains(tweet.getLang()))
            return check(tweet.getText(), false);
        else
            return check(tweet.getText(), true);
    }

    @Override
    public boolean check(String text) {
        return check(text, true);
    }

    public boolean check(String text, boolean afterAll) {
        text = text.toLowerCase();
        for(Keyword keyword : keywordList){
            Pattern pattern = Pattern.compile("(^|\\W)" + keyword.getWord() + "(\\W|$)");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find())
                return keyword.isGood();
        }
        return afterAll;
    }

    private static class Keyword implements Comparable<Keyword> {
        private final String word;
        private final boolean good;
        private final int priority;

        Keyword(String word, boolean good, int priority) {
            this.word = word;
            this.good = good;
            this.priority = priority;
        }

        @Override
        public int compareTo(Keyword o) {
            int pc = Integer.compare(priority, o.priority);
            if (pc != 0)
                return pc;
            return -Boolean.compare(good, o.good);
        }

        public String getWord() {
            return word;
        }

        public boolean isGood() {
            return good;
        }
    }
}
