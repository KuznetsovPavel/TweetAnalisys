package filter.checkers;

import filter.OnlyTextMapper;

import java.util.function.Function;
import java.util.regex.Pattern;

public class WordNumberChecker implements Checker {
    private static final int wordNumberThreshold = 3;
    private final Function<String, String> onlyTextMapper = OnlyTextMapper.getInstance(true);

    @Override
    public boolean check(String text) {
        text = onlyTextMapper.apply(text);
        String[] split = text.split("\\S+");
        int count = 0;
        for(String word: split){
            count++;
        }
        return count > wordNumberThreshold;
    }
}
