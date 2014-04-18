package com.cegeka.domain.books;

import org.junit.Test;

import javax.validation.ConstraintViolationException;

import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static org.fest.assertions.Assertions.assertThat;

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

    @Test
     public void canIncreaseTheNumberOfCopiesWhenBookIsNotBorrowed() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(2)
                .build();

        book.updateNumberOfCopies(3);

        assertThat(book.getCopies()).isEqualTo(3);
        assertThat(book.availableCopies()).isEqualTo(3);
    }

    @Test
    public void canIncreaseTheNumberOfCopiesWhenBookIsBorrowed() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(2)
                .build();

        book.lendTo(aUserEntity("romeo@mailinator.com"));

        book.updateNumberOfCopies(3);

        assertThat(book.getCopies()).isEqualTo(3);
        assertThat(book.availableCopies()).isEqualTo(2);
    }

    @Test
    public void canDecreaseTheNumberOfCopies() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(3)
                .build();

        book.lendTo(aUserEntity("romeo@mailinator.com"));
        book.lendTo(aUserEntity("juliet@mailinator.com"));

        book.updateNumberOfCopies(2);

        assertThat(book.getCopies()).isEqualTo(2);
        assertThat(book.availableCopies()).isEqualTo(0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void cannotUpdateCopiesWithNegativeValue() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(2)
                .build();
        book.updateNumberOfCopies(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotDecreaseNumberOfCopiesBellowNumberOfBorrowedBooks() {
        BookEntity book = new BookEntityTestFixture()
                .withDefaults()
                .withCopies(2)
                .build();

        book.lendTo(aUserEntity("romeo@mailinator.com"));
        book.lendTo(aUserEntity("juliet@mailinator.com"));
        book.updateNumberOfCopies(1);
    }

}
