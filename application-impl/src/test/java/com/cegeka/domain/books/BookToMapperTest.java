package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import com.cegeka.domain.users.UserEntity;
import org.junit.Test;

import static com.cegeka.domain.books.BookEntityTestFixture.newValidBook;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by monicat on 08/04/2014.
 */
public class BookToMapperTest {

    BookToMapper bookToMapper = new BookToMapper();

    @Test
    public void testToToForLentBook () {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(1);

        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.lendTo(romeo);

        BookTo bookTo = bookToMapper.toTo(book, romeo.getId());

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(0);
        assertThat(bookTo.getUserIds().size()).isEqualTo(1);
        assertThat(bookTo.getUserIds()).contains(romeo.getId());
        assertThat(bookTo.isBorrowedByCurrentUser()).isEqualTo(true);

    }

    @Test
    public void testToToForNullUser () {
        BookEntity book = newValidBook();
        book.setId("book_id");
        book.setCopies(2);

        UserEntity romeo = romeoUser();
        romeo.setId("romeo_id");

        book.lendTo(romeo);

        BookTo bookTo = bookToMapper.toTo(book, null);

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(1);
        assertThat(bookTo.getUserIds().size()).isEqualTo(1);
        assertThat(bookTo.getUserIds()).contains(romeo.getId());
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

        BookTo bookTo = bookToMapper.toTo(book, "random_user_id");

        assertThat(bookTo.getId()).isEqualTo(book.getId());
        assertThat(bookTo.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(bookTo.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookTo.getAvailableCopies()).isEqualTo(1);
        assertThat(bookTo.getUserIds().size()).isEqualTo(2);
        assertThat(bookTo.getUserIds()).contains(romeo.getId());
        assertThat(bookTo.isBorrowedByCurrentUser()).isEqualTo(false);

    }
}
