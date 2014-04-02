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


}
