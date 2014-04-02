package com.cegeka.application;

import java.util.Date;

public class BookLoanTo {
    private String id;
    private BookTo book;
    private UserTo user;
    private Date startDate;
    private Date endDate;

    public BookLoanTo() {
    }

    public BookLoanTo(String id, BookTo book, UserTo user) {
        this.id = id;
        this.book = book;
        this.user = user;
    }

    public BookLoanTo(BookTo book, UserTo user) {
        this.book = book;
        this.user = user;
    }

    public BookLoanTo(String id, BookTo book, UserTo user, Date startDate) {
        this.id = id;
        this.book = book;
        this.user = user;
        this.startDate = startDate;
    }

    public BookLoanTo(BookTo book, UserTo user, Date startDate) {
        this.book = book;
        this.user = user;
        this.startDate = startDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookTo getBook() {
        return book;
    }

    public void setBook(BookTo book) {
        this.book = book;
    }

    public UserTo getUser() {
        return user;
    }

    public void setUser(UserTo user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookLoanTo)) return false;

        BookLoanTo that = (BookLoanTo) o;

        if (book != null ? !book.equals(that.book) : that.book != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (book != null ? book.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }
}
