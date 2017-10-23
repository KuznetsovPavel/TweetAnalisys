package checkers;

public class QueryChecker implements checkers.Checker {
    private final String query;

    public QueryChecker(String query) {
        this.query = query;
    }

    @Override
    public boolean check(String text) {
        text = text.toLowerCase();
        return text.contains(query);
    }
}
