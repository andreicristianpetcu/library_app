package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.books.BookToMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.cegeka.domain.books.BookEntityTestFixture.hamletBook;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookFacadeImplTest {

    private BookFacadeImpl bookFacade = new BookFacadeImpl();

    @Mock
    private BookRepository bookRepositoryMock;
    @Mock
    private BookToMapper bookToMapperMock;

    @Before
    public void setUp() {
        bookFacade.setBookRepository(bookRepositoryMock);
        bookFacade.setBookToMapper(bookToMapperMock);
    }

    @Test
    public void getBooksCallsRepositoryFindAll () {
        when(bookRepositoryMock.findAll()).thenReturn(Collections.<BookEntity>emptyList());

        List<BookTo> books = bookFacade.getBooks();

        verify(bookRepositoryMock).findAll();
    }

    @Test
    public void getBookCallsRepositoryFindOne () {
        BookEntity bookEntity = hamletBook();
        bookEntity.setId("123");

        when(bookRepositoryMock.findOne("123")).thenReturn(bookEntity);

        bookFacade.getBook("123");

        verify(bookRepositoryMock).findOne("123");
    }

    @Test
    public void saveBookCallsMapperAndRepositorySaveAndFlush () {
        BookTo bookTo = new BookTo(null, "One", "Two", "Three", null);
        BookTo expected = new BookTo("123", "One", "Two", "Three", null);

        BookEntity bookEntity = hamletBook();

        when(bookToMapperMock.toNewEntity(bookTo)).thenReturn(bookEntity);
        when(bookRepositoryMock.saveAndFlush(bookEntity)).thenReturn(bookEntity);
        when(bookToMapperMock.toTo(bookEntity)).thenReturn(expected);

        BookTo result = bookFacade.saveBook(bookTo);

        verify(bookToMapperMock).toNewEntity(bookTo);
        verify(bookRepositoryMock).saveAndFlush(bookEntity);
        verify(bookToMapperMock).toTo(bookEntity);

        assertThat(result).isSameAs(expected);
    }

}
