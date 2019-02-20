package me.kcaldwell.hackernewsreader.data;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    private @PrimaryKey long id;
    private String title;
    private Integer points;
    private String author;
    private long time;
    private String timeAgo;
    private String content;
    private Boolean deleted;
    private Boolean dead;
    private String type;
    private String url;
    private String domain;
    private RealmList<Item> comments;
    private int level;
    private int commentsCount;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getDead() {
        return dead;
    }

    public void setDead(Boolean dead) {
        this.dead = dead;
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

    public RealmList<Item> getComments() {
        return comments;
    }

    public void setComments(RealmList<Item> comments) {
        this.comments = comments;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", points=" + points +
                ", author='" + author + '\'' +
                ", time=" + time +
                ", timeAgo='" + timeAgo + '\'' +
                ", content='" + content + '\'' +
                ", deleted=" + deleted +
                ", dead=" + dead +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", domain='" + domain + '\'' +
                ", comments=" + comments +
                ", level=" + level +
                ", commentsCount=" + commentsCount +
                '}';
    }
}
