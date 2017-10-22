package checkers;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordChecker implements Checker {
    private final List<Keyword> keywordList = new ArrayList<>();

    public KeywordChecker(String keyWordFile) throws IOException {
        final ClassPathResource resource = new ClassPathResource(keyWordFile);
        try(BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))){
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
    }

    @Override
    public boolean check(String text) {
        text = text.toLowerCase();
        for(Keyword keyword : keywordList){
            Pattern pattern = Pattern.compile("(^|\\W)" + keyword.getWord() + "(\\W|$)");
            Matcher matcher = pattern.matcher(text);
            if (matcher.find())
                return keyword.isGood();
        }
        return true;
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
            if (good == o.good)
                return 0;
            if (good)
                return -1;
            return 1;
        }

        public String getWord() {
            return word;
        }

        public boolean isGood() {
            return good;
        }
    }
}
