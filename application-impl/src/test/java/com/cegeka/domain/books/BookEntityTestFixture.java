package com.cegeka.domain.books;

import org.joda.time.DateTime;

import java.util.UUID;

public class BookEntityTestFixture extends BookEntity.Builder {

    public BookEntityTestFixture() {
        super();
    }

    public BookEntity.Builder withDefaults() {
        withId(UUID.randomUUID().toString())
                .withCopies(2)
                .withTitle("Default Title")
                .withAuthor("Default Author")
                .withDetails(defaultBookDetails())
                .withIsbn(newRandomISBN());

        return this;
    }

    public static BookEntity aBook() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .build();
        return book;
    }

    public static BookEntity aBookWithOneCopy() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(1)
                .build();
        return book;
    }

    public static BookEntity aBookWithThreeCopies() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(3)
                .build();
        return book;
    }

    public static BookEntity aBookWithoutId() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withId(null)
                .build();
        return book;
    }

    public static BookEntity aBookWithoutDetails() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withDetails(null)
                .build();
        return book;
    }


    private static String newRandomISBN() {
        return "" + Math.round(Math.random() * 123456789);
    }

    private static BookDetails defaultBookDetails() {
        return new BookDetails(new DateTime().toString(), "publisher", "coverImageUrl", "description");
    }
}
