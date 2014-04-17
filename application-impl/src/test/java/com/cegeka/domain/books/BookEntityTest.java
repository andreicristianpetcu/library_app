package com.cegeka.domain.books;

import org.junit.Test;

import javax.validation.ConstraintViolationException;

public class BookEntityTest {

    @Test(expected = ConstraintViolationException.class)
    public void emptyTitleThrowsError() {
        new BookEntityTestFixture()
                .withDefaults()
                .withTitle(null)
                .build();
    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyAuthorThrowsError() {
        new BookEntityTestFixture()
                .withDefaults()
                .withAuthor(null)
                .build();

    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyIsbnThrowsError() {
        new BookEntityTestFixture()
                .withDefaults()
                .withIsbn(null)
                .build();

    }

    @Test(expected = ConstraintViolationException.class)
    public void emptyCopiesThrowsError() {
        new BookEntityTestFixture()
                .withDefaults()
                .withCopies(null)
                .build();

    }

    @Test(expected = ConstraintViolationException.class)
    public void zeroCopiesThrowsError() {
        new BookEntityTestFixture()
                .withDefaults()
                .withCopies(0)
                .build();

    }

}
