package com.cegeka.domain.books;

import com.cegeka.IntegrationTest;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import static com.cegeka.domain.books.BookEntityTestFixture.newValidBook;
import static com.cegeka.domain.books.BookEntityTestFixture.newValidBookWithoutId;
import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class BookRepositoryIntegrationTest extends IntegrationTest {

    @Resource
    private BookRepository bookRepository;

    @Resource
    private UserRepository userRepository;

    UserEntity romeo;
    UserEntity juliet;

    @Before
    public void setUp() {
        romeo = aUserEntity("romeo@mailinator.com");
        juliet = aUserEntity("juliet@mailinator.com");

        userRepository.save(newArrayList(romeo, juliet));
    }

    @Test
    public void givenANewBookWithoutId_WhenPersisting_ThenIdIsAssigned() {
        BookEntity macbeth = bookRepository.save(newValidBookWithoutId());

        BookEntity persistedBook = bookRepository.findOne(macbeth.getId());

        assertBook(macbeth, persistedBook);
        assertBookDetails(macbeth.getDetails(), persistedBook.getDetails());
    }


    @Test
    public void whenIdDoesNotExistReturnsNull() {
        BookEntity one = bookRepository.findOne("WrongId");
        assertThat(one).isNull();
    }

    @Test
    public void emptyTitleThrowsError() {
        try {
            BookEntity bookEntity = newValidBook();
            bookEntity.setTitle(null);
            BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
            fail("Saved book with null title");
        } catch (ConstraintViolationException e) {
        }
    }

    @Test
    public void emptyAuthorThrowsError() {
        try {
            BookEntity bookEntity = newValidBook();
            bookEntity.setAuthor(null);
            BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
            fail("Saved book with null author");
        } catch (ConstraintViolationException e) {
        }
    }

    @Test
    public void emptyIsbnThrowsError() {
        try {
            BookEntity bookEntity = newValidBook();
            bookEntity.setIsbn(null);
            BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
            fail("Saved book with null ISBN");
        } catch (ConstraintViolationException e) {
        }
    }

    @Test
    public void emptyCopiesThrowsError() {
        try {
            BookEntity bookEntity = newValidBook();
            bookEntity.setCopies(null);
            BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
            fail("Saved book with null copies");
        } catch (ConstraintViolationException e) {
        }
    }

    @Test
    public void zeroCopiesThrowsError() {
        try {
            BookEntity bookEntity = newValidBook();
            bookEntity.setCopies(0);
            BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
            fail("Saved book with null copies");
        } catch (ConstraintViolationException e) {
        }
    }

    @Test
    public void uniqueISBN() {
        try {
            BookEntity bookEntity1 = newValidBook();
            BookEntity bookEntity2 = newValidBook();
            bookEntity2.setIsbn(bookEntity1.getIsbn());
            bookRepository.saveAndFlush(bookEntity1);
            bookRepository.saveAndFlush(bookEntity2);
            fail("Saved two books with same isbn");
        } catch (PersistenceException e) {
        }
    }

    @Test
    public void borrowTwice() {
        BookEntity book = newValidBook();
        book.setCopies(2);

        book.lendTo(romeo);
        book.lendTo(juliet);

        bookRepository.saveAndFlush(book);

        assertThat(book.isLendTo(romeo)).isTrue();
        assertThat(book.isLendTo(juliet)).isTrue();
    }

    @Test
    public void checkLimit() {
        try {
            BookEntity book = newValidBook();
            book.setCopies(1);
            book.lendTo(aUserEntity("romeo@mailinator.com"));
            book.lendTo(aUserEntity("romeo@mailinator.com"));
            fail("Overborrowed!");
        } catch (ConstraintViolationException e) {
        }
    }

    private void assertBookDetails(BookDetailsEntity macbethDetails, BookDetailsEntity persistedBookDetails) {
        assertThat(macbethDetails.getCoverImage()).isEqualTo(persistedBookDetails.getCoverImage());
        assertThat(macbethDetails.getDescription()).isEqualTo(persistedBookDetails.getDescription());
        assertThat(macbethDetails.getPublishedDate()).isEqualTo(persistedBookDetails.getPublishedDate());
        assertThat(macbethDetails.getPublisher()).isEqualTo(persistedBookDetails.getPublisher());

    }

    private void assertBook(BookEntity expected, BookEntity actual) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.availableCopies()).isEqualTo(expected.availableCopies());
        assertThat(actual.getIsbn()).isEqualTo(expected.getIsbn());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getAuthor()).isEqualTo(expected.getAuthor());
    }

}
