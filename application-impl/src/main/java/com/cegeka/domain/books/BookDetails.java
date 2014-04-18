package com.cegeka.domain.books;

import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;

@Embeddable
public class BookDetails {

    private String publishedDate;
    private String publisher;
    private String coverImage;

    @Type(type = "text")
    private String description;

    BookDetails(){  }

    public BookDetails(String publishedDate, String publisher, String coverImage, String description) {
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.coverImage = coverImage;
        this.description = description;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getDescription() {
        return description;
    }
}
