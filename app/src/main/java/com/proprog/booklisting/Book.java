package com.proprog.booklisting;

/**
 * Created by mohamedAHMED on 2017-11-10.
 */

public class Book {
    private String title;
    private String publisher;
    private String thumbnail;

    public Book(String title, String publisher, String thumbnail) {
        this.title = title;
        this.publisher = publisher;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
