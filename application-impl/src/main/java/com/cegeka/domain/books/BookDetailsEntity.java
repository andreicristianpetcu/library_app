package com.cegeka.domain.books;

/**
 * Created by monicat on 10/04/2014.
 */
public class BookDetailsEntity {
    private String publishedDate;
    private String publisher;
    private String coverImage;
    private String description;

    public BookDetailsEntity() {
    }

    public BookDetailsEntity(String publishedDate, String publisher, String coverImage, String description) {
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.coverImage = coverImage;
        this.description = description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
