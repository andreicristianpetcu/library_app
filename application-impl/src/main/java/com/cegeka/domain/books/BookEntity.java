package com.cegeka.domain.books;

import com.cegeka.domain.users.UserEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    @ManyToMany
    @JoinTable(name = "BOOK_WATCHER",
            joinColumns = {@JoinColumn(name = "BOOK_ID", nullable = false, updatable = false)})
    private List<UserEntity> watchers = new ArrayList<UserEntity>();

    private BookEntity() {
    }

    public void addWatcher(UserEntity user) {
        this.watchers.add(user);
    }

    public void removeWatcher(UserEntity user) {
        this.watchers.remove(user);
    }

    public void clearAllWatchers() {
        watchers.clear();
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

    public int availableCopies() {
        return copies - getBorrowers().size();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public List<UserEntity> getBorrowers() {
        return borrowers;
    }

    public Integer getCopies() {
        return copies;
    }

    public void updateAvailableCopies(int availableCopies){
        this.copies = availableCopies;
    }

    public BookDetailsEntity getDetails() {
        return details;
    }

    public List<UserEntity> getWatchers() {
        return watchers;
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

    @Override
    public String toString() {
        return super.toString();
    }

    public boolean isAvailable() {
        return availableCopies() > 0;
    }

    static class Builder {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();;

        private String id;
        private String title;
        private String author;
        private Integer copies;
        private String isbn;
        private List<UserEntity> borrowers = new ArrayList<UserEntity>();
        private BookDetailsEntity details;
        private List<UserEntity> watchers = new ArrayList<UserEntity>();

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder withCopies(Integer copies) {
            this.copies = copies;
            return this;
        }

        public Builder withIsbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder withBorrowers(List<UserEntity> borrowers) {
            this.borrowers = borrowers;
            return this;
        }

        public Builder withDetails(BookDetailsEntity details) {
            this.details = details;
            return this;
        }

        public Builder withWatchers(List<UserEntity> watchers) {
            this.watchers = watchers;
            return this;
        }

        public BookEntity build() throws ConstraintViolationException {
            BookEntity entity = new BookEntity();
            entity.id = id;
            entity.title = title;
            entity.author = author;
            entity.copies = copies;
            entity.isbn = isbn;
            entity.borrowers = borrowers;
            entity.details = details;
            entity.watchers = watchers;

            Set<ConstraintViolation<BookEntity>> constraintViolations = validator.validate(entity);
            if (!constraintViolations.isEmpty()) {
                throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(constraintViolations));
            }
            return entity;
        }

    }
}
