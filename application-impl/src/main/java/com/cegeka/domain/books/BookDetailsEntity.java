package com.cegeka.domain.books;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by monicat on 10/04/2014.
 */
@Entity
@Table(name = "BOOK_DETAILS")
public class BookDetailsEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String publishedDate;
    private String publisher;
    private String coverImage;

    @Type(type="text")
    private String description;

    public BookDetailsEntity() {
    }

    public BookDetailsEntity(String publishedDate, String publisher, String coverImage, String description) {
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.coverImage = coverImage;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
