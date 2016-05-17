package com.hacker.project.hackernewsproj.data;

/**
 * @author Navin Prasad <navin.prasad@fastacash.com>
 * @since 17/May/2016
 */


public class NewsData {

    private int comments;
    private String heading;
    private String urlString;
    private String authorName;
    private long timePassed;

    public NewsData(String heading, String urlString, String authorName, int comments, long timePassed) {
        this.heading = heading;
        this.urlString = urlString;
        this.authorName = authorName;
        this.comments = comments;
        this.timePassed = timePassed;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getTimePassed() {
        return timePassed;
    }

    public void setTimePassed(long timePassed) {
        this.timePassed = timePassed;
    }

}
