package checkers;

import org.springframework.social.twitter.api.Tweet;

public interface Checker {
    default boolean check(Tweet tweet){
        return check(tweet.getText());
    }

    boolean check(String text);
}
