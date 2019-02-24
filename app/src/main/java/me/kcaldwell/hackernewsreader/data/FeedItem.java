package me.kcaldwell.hackernewsreader.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FeedItem extends RealmObject {

    private @PrimaryKey
    long id;
    private String title;
    private Integer points;
    private String author;
    private long time;
    private String timeAgo;
    private int commentsCount;
    private String type;
    private String url;
    private String domain;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "FeedItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", points=" + points +
                ", author='" + author + '\'' +
                ", time=" + time +
                ", timeAgo='" + timeAgo + '\'' +
                ", commentsCount=" + commentsCount +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
