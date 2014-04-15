package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import com.cegeka.application.BorrowerTo;
import com.cegeka.domain.users.UserEntity;
import org.junit.Test;

import static com.cegeka.domain.books.BookEntityTestFixture.newValidBook;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by monicat on 08/04/2014.
 */
public class BookFactoryTest {

    BookFactory bookFactory = new BookFactory();

    @Test
    public void testToToForLentBook () {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(1);
        BookDetailsEntity bookDetailsEntity = new BookDetailsEntity(
                "2014", "Nemira",
                "http://sciencelakes.com/data_images/out/13/8811007-funny-dog-face.jpg", "Odiseea spatiala 2001");
        book.setDetails(bookDetailsEntity);
        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.lendTo(romeo);

        BookTo bookTo = bookFactory.toTo(book, romeo.getId());

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getPublishedDate()).isEqualTo(book.getDetails().getPublishedDate());
        assertThat(bookTo.getPublisher()).isEqualTo(book.getDetails().getPublisher());
        assertThat(bookTo.getCoverImage()).isEqualTo(book.getDetails().getCoverImage());
        assertThat(bookTo.getDescription()).isEqualTo(book.getDetails().getDescription());

        assertThat(bookTo.getAvailableCopies()).isEqualTo(0);
        assertThat(bookTo.getBorrowers().size()).isEqualTo(1);
        assertThat(bookTo.getBorrowers().get(0).getId()).isEqualTo(romeo.getId());
        assertThat(bookTo.getBorrowers().get(0).getUserName()).isEqualTo(romeo.getProfile().getFullName());
        assertThat(bookTo.isBorrowedByCurrentUser()).isEqualTo(true);
    }

    @Test
    public void whenBookIsWatched_shouldBeWatched () {
        BookEntity book = newValidBook();
        book.setId("book_id");

        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.addWatcher(romeo);

        BookTo bookTo = bookFactory.toTo(book, romeo.getId());

        assertThat(bookTo.isWatchedByCurrentUser()).isEqualTo(true);
    }

    @Test
    public void whenBookIsWatchedBySomeOneElse_shouldNotBeWatched () {
        BookEntity book = newValidBook();
        book.setId("book_id");

        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.addWatcher(romeo);

        BookTo bookTo = bookFactory.toTo(book, "juliet_id");

        assertThat(bookTo.isWatchedByCurrentUser()).isEqualTo(false);
    }


    @Test
    public void testToToForNullUser () {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(2);

        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.lendTo(romeo);

        BookTo bookTo = bookFactory.toTo(book, null);

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(1);
        assertThat(bookTo.getBorrowers().size()).isEqualTo(1);
        assertThat(bookTo.isBorrowedByCurrentUser()).isEqualTo(false);
    }

    @Test
    public void testToToForNullDetails() {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(2);
        book.setDetails(null);
        BookTo bookTo = bookFactory.toTo(book, null);

        assertThat(bookTo.getPublishedDate()).isEqualTo(null);
        assertThat(bookTo.getPublisher()).isEqualTo(null);
        assertThat(bookTo.getDescription()).isEqualTo(null);
        assertThat(bookTo.getCoverImage()).isEqualTo(null);
    }

    @Test
    public void testToNewEntity() {
        BookTo bookTo = new BookTo("7", "Title", "Author", "123");
        bookTo.setPublishedDate("2006");
        bookTo.setPublisher("Nemira");
        bookTo.setDescription("An awesome romance novel");
        bookTo.setCoverImage("http://sciencelakes.com/data_images/out/13/8811007-funny-dog-face.jpg");
        BookEntity bookEntity = bookFactory.toNewEntity(bookTo);

        assertThat(bookTo.getTitle()).isEqualTo(bookEntity.getTitle());
        assertThat(bookTo.getAuthor()).isEqualTo(bookEntity.getAuthor());
        assertThat(bookTo.getIsbn()).isEqualTo(bookEntity.getIsbn());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(bookEntity.getCopies());

        BookDetailsEntity details = bookEntity.getDetails();
        assertThat(details).isNotNull();
        assertThat(details.getPublishedDate()).isEqualTo(bookTo.getPublishedDate());
        assertThat(details.getPublisher()).isEqualTo(bookTo.getPublisher());
        assertThat(details.getDescription()).isEqualTo(bookTo.getDescription());
        assertThat(details.getCoverImage()).isEqualTo(bookTo.getCoverImage());
    }

    @Test
    public void testToToForNoBorrowers () {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(2);

        BookTo bookTo = bookFactory.toTo(book, null);

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(2);
        assertThat(bookTo.getBorrowers().size()).isEqualTo(0);
        assertThat(bookTo.isBorrowedByCurrentUser()).isEqualTo(false);
    }

    @Test
    public void testToToForMultipleBorrowers () {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(3);

        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.lendTo(romeo);
        book.lendTo(romeo);

        BookTo bookTo = bookFactory.toTo(book, "random_user_id");

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(1);
        assertThat(bookTo.getBorrowers().size()).isEqualTo(2);
        BorrowerTo borrowerTo = new BorrowerTo(romeo.getId(), romeo.getProfile().getFullName(), romeo.getEmail());
        assertThat(bookTo.getBorrowers()).contains(borrowerTo);
        assertThat(bookTo.isBorrowedByCurrentUser()).isEqualTo(false);

    }
}
