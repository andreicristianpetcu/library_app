package com.cegeka.application;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class BookTo {
    private String id;
    private String title;
    private String author;
    private String isbn;
    private Integer availableCopies;
    private boolean borrowedByCurrentUser;
    private List<BorrowerTo> borrowers;
    private String publishedDate;
    private String publisher;
    private String coverImage;
    private String description;
    private boolean watchedByCurrentUser;

    public BookTo() {
    }

    public BookTo(String id, String title, String author, String isbn) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isBorrowedByCurrentUser() {
        return borrowedByCurrentUser;
    }

    public void setBorrowedByCurrentUser(boolean borrowedByCurrentUser) {
        this.borrowedByCurrentUser = borrowedByCurrentUser;
    }

    public List<BorrowerTo> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(List<BorrowerTo> borrowers) {
        this.borrowers = borrowers;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that);
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

    public boolean isWatchedByCurrentUser() {
        return watchedByCurrentUser;
    }

    public void setWatchedByCurrentUser(boolean watchedByCurrentUser) {
        this.watchedByCurrentUser = watchedByCurrentUser;
    }
}
