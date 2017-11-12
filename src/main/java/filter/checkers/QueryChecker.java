package filter.checkers;

import filter.OnlyTextMapper;

import java.util.function.Function;

public class QueryChecker implements filter.checkers.Checker {
    private final String query;
    private final Function<String, String> onlyTextMapper = OnlyTextMapper.getInstance(false);

    public QueryChecker(String query) {
        this.query = query;
    }

    @Override
    public boolean check(String text) {
        text = onlyTextMapper.apply(text);
        text = text.toLowerCase();
        return text.contains(query);
    }
}
