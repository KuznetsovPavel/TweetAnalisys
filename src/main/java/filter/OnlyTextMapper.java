package filter;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlyTextMapper implements Function<String, String> {
    private static final String usernamePattern = "@\\w+";
    private static final String tagPattern = "#[^,!\\.\\?\\s]+";
    private static final String sillyUrlPattern = "\\S+\\.\\S+|https?://\\S+";
    private final Pattern pattern;

    private OnlyTextMapper(boolean removeTags){
        if (removeTags) {
            pattern = Pattern.compile(String.format("%s|%s|%s",
                                                    usernamePattern,
                                                    tagPattern,
                                                    sillyUrlPattern));
        } else {
            pattern = Pattern.compile(String.format("%s|%s",
                                                    usernamePattern,
                                                    sillyUrlPattern));
        }
    }

    @Override
    public String apply(String str) {
        Matcher matcher = pattern.matcher(str);
        StringBuilder mappedStrBuilder = new StringBuilder();
        int startOnlyText = 0;
        int endOnlyText;
        while(matcher.find()){
            endOnlyText = matcher.start();
            mappedStrBuilder.append(str.substring(startOnlyText, endOnlyText));
            startOnlyText = matcher.end();
        }
        endOnlyText = str.length();
        mappedStrBuilder.append(str.substring(startOnlyText, endOnlyText));
        return mappedStrBuilder.toString();
    }

    private static final class Wrapper{
        private static final OnlyTextMapper REMOVE_TAGS_INSTANCE = new OnlyTextMapper(true);
        private static final OnlyTextMapper NOT_REMOVE_TAGS_INSTANCE = new OnlyTextMapper(false);
    }

    public static OnlyTextMapper getInstance(boolean removeTags){
        if(removeTags)
            return Wrapper.REMOVE_TAGS_INSTANCE;
        else
            return Wrapper.NOT_REMOVE_TAGS_INSTANCE;
    }
}
