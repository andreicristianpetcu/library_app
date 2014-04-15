package com.cegeka.domain.books;

import com.cegeka.IntegrationTest;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static com.cegeka.domain.books.BookEntityTestFixture.*;
import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

public class BookRepositoryIntegrationTest extends IntegrationTest {

    @Resource
    private BookRepository bookRepository;

    @Resource
    private UserRepository userRepository;

    private BookEntity hamlet, macbeth;
    private UserEntity romeo;
    private UserEntity juliet;

    @Before
    public void setUp() {
        hamlet = bookRepository.saveAndFlush(hamletBook());
        macbeth = bookRepository.saveAndFlush(macbethBook());
        romeo = userRepository.saveAndFlush(romeoUser());
        juliet = userRepository.saveAndFlush(aUserEntity());

        hamlet.lendTo(romeo);
        bookRepository.flush();
    }

    @Test
    public void canRetrieveAll() {
        List<BookEntity> all = bookRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(hamletBook());
        assertThat(all).contains(macbethBook());
    }

    @Test
    public void canRetrieveOne() {
        BookEntity one = bookRepository.findOne(macbeth.getId());
        assertThat(one).isEqualTo(macbethBook());
    }

    @Test
    public void canRetrieveOneBorrowed() {
        BookEntity one = bookRepository.findOne(hamlet.getId());
        assertThat(one).isEqualTo(hamletBook());
        assertThat(one.isLendTo(romeoUser()));
    }

    @Test
    public void canRetrieveOneWatched() {
        BookEntity othello = newValidBook();
        othello.addWatcher(romeo);
        othello.addWatcher(juliet);
        bookRepository.saveAndFlush(othello);

        BookEntity one = bookRepository.findOne(othello.getId());
        assertThat(one.getWatchers()).contains(romeo);
        assertThat(one.getWatchers()).contains(juliet);
    }

    @Test
    public void whenIdDoesNotExistReturnsNull() {
        BookEntity one = bookRepository.findOne("WrongId");
        assertThat(one).isNull();
    }

    @Test
    public void canSaveOneItem() {
        BookEntity bookEntity = newValidBook();
        BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
        assertThat(bookEntity.getId()).isNotNull();
        assertThat(bookEntityReturned).isSameAs(bookEntity);

        List<BookEntity> all = bookRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
        assertThat(all).contains(hamletBook());
        assertThat(all).contains(bookEntity);
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
            book.lendTo(romeo);
            book.lendTo(juliet);
            fail("Overborrowed!");
        } catch (ConstraintViolationException e) {
        }
    }

    @Test
    public void checkBookWithDetails() {
        BookEntity book = newValidBook();
        book.setCopies(1);
        BookDetailsEntity details = new BookDetailsEntity("2010", "Nemira",
                "http://sciencelakes.com/data_images/out/13/8811007-funny-dog-face.jpg", "Funny book");
        book.setDetails(details);
        bookRepository.saveAndFlush(book);

        BookEntity one = bookRepository.findOne(book.getId());
        assertThat(one.getDetails()).isEqualTo(details);

    }
}
