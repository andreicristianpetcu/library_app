package com.cegeka.domain.books;

import com.cegeka.IntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static com.cegeka.domain.books.BookEntityTestFixture.hamletBook;
import static org.fest.assertions.Assertions.assertThat;

public class BookRepositoryIntegrationTest extends IntegrationTest {

    public static final String PICTURE_URL = "pictureUrl";
    public static final String TEST_USER_EMAIL_2 = "some_email@email.com";
    @Resource
    private BookRepository bookRepository;

    private BookEntity hamlet;

    @Before
    public void setUp() {
        hamlet = bookRepository.saveAndFlush(hamletBook());
    }

    @Test
    public void canRetrieveAll() {
        List<BookEntity> all = bookRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all).contains(hamletBook());
    }

    @Test
    public void canRetrieveOne() {
        BookEntity one = bookRepository.findOne(hamlet.getId());
        assertThat(one).isEqualTo(hamletBook());
    }

    @Test
    public void whenIdDoesNotExistReturnsNull() {
        BookEntity one = bookRepository.findOne("WrongId");
        assertThat(one).isNull();
    }

    @Test
    public void canSaveOneItem () {
        BookEntity bookEntity = new BookEntity("One", "Two", "Three");
        BookEntity bookEntityReturned = bookRepository.saveAndFlush(bookEntity);
        assertThat(bookEntity.getId()).isNotNull();
        assertThat(bookEntityReturned).isSameAs(bookEntity);

        List<BookEntity> all = bookRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all).contains(hamletBook());
        assertThat(all).contains(bookEntity);
    }

}
