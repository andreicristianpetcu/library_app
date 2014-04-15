package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by monicat on 08/04/2014.
 */
public class BookFactoryTest {

    BookFactory bookFactory = new BookFactory();

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
}
