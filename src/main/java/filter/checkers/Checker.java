package filter.checkers;

import model.SimpleTweet;

public interface Checker {
    default boolean check(SimpleTweet tweet){
        return check(tweet.getText());
    }

    boolean check(String text);
}
