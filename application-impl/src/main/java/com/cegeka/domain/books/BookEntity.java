package com.cegeka.domain.books;

import com.cegeka.domain.users.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "BOOKS")
public class BookEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    @Min(1)
    private Integer copies;

    @Column(unique = true)
    @NotNull
    private String isbn;

    @ManyToMany
    @JoinTable(name = "BOOK_BORROWER",
            joinColumns = {@JoinColumn(name = "BOOK_ID", nullable = false, updatable = false)})
    private List<UserEntity> borrowers = new ArrayList<UserEntity>();

    @OneToOne(cascade = ALL)
    @JoinColumn(referencedColumnName = "ID")
    private BookDetailsEntity details;

    public BookEntity() {
    }

    public BookEntity(String title, String author, String isbn) {
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

    public List<UserEntity> getBorrowers() {
        return borrowers;
    }

    public void setBorrowers(List<UserEntity> borrowers) {
        this.borrowers = borrowers;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookEntity)) return false;

        BookEntity that = (BookEntity) o;

        if (isbn != null ? !isbn.equals(that.isbn) : that.isbn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return isbn != null ? isbn.hashCode() : 0;
    }

    public void lendTo(UserEntity userEntity) {
        if (getBorrowers().size() < copies) {
            getBorrowers().add(userEntity);
        } else throw new ConstraintViolationException(Collections.EMPTY_SET);
    }

    public boolean isLendTo(UserEntity userEntity) {
        return getBorrowers().contains(userEntity);
    }

    public void returnFrom(UserEntity user) {
        getBorrowers().remove(user);
    }

    public BookDetailsEntity getDetails() {
        return details;
    }

    public void setDetails(BookDetailsEntity details) {
        this.details = details;
    }
}

