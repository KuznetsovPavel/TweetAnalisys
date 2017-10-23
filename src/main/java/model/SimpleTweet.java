package model;

import java.util.Date;
import java.util.List;

public class SimpleTweet {
    private long id;
    private String query;
    private String text;
    private Date date;
    private String lang;
    private int favoriteCount;
    private String fromUser;
    private long fromUserId;
    private int retweetCount;
    private List<String> tags;

    public SimpleTweet(long id, String query, String text, Date date, String lang, int favoriteCount,
                       String fromUser, long fromUserId, int retweetCount, List<String> tags) {
        this.id = id;
        this.query = query;
        this.text = text;
        this.date = date;
        this.lang = lang;
        this.favoriteCount = favoriteCount;
        this.fromUser = fromUser;
        this.fromUserId = fromUserId;
        this.retweetCount = retweetCount;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
