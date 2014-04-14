package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.books.BookToMapper;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.infrastructure.EmailComposer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.cegeka.domain.books.BookEntityTestFixture.hamletBook;
import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookFacadeImplTest {

    @Mock
    private BookRepository bookRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private BookToMapper bookToMapperMock;
    @Mock
    private EmailComposer emailComposer;


    @InjectMocks
    private BookFacadeImpl bookFacade = new BookFacadeImpl();

    @Test
    public void getBooksCallsRepositoryFindAll () {
        when(bookRepositoryMock.findAll()).thenReturn(Collections.<BookEntity>emptyList());

        List<BookTo> books = bookFacade.getBooks(null);

        verify(bookRepositoryMock).findAll();
    }

    @Test
    public void saveBookCallsMapperAndRepositorySaveAndFlush () {
        BookTo bookTo = new BookTo(null, "One", "Two", "Three");
        BookTo expected = new BookTo("123", "One", "Two", "Three");

        BookEntity bookEntity = hamletBook();

        when(bookToMapperMock.toNewEntity(bookTo)).thenReturn(bookEntity);
        when(bookRepositoryMock.saveAndFlush(bookEntity)).thenReturn(bookEntity);
        when(bookToMapperMock.toTo(bookEntity, null)).thenReturn(expected);

        BookTo result = bookFacade.saveBook(bookTo, null);

        verify(bookToMapperMock).toNewEntity(bookTo);
        verify(bookRepositoryMock).saveAndFlush(bookEntity);
        verify(bookToMapperMock).toTo(bookEntity, null);

        assertThat(result).isSameAs(expected);
    }

    @Test
    public void whenReturnBook_shouldSendEmailToWatchers () {
        BookEntity hamlet = hamletBook();
        hamlet.setCopies(1);

        UserEntity romeo = romeoUser();
        UserEntity juliet = aUserEntity();
        UserEntity secondWatcher = aUserEntity();
        secondWatcher.setLocale(Locale.JAPAN);

        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);
        when(userRepositoryMock.findOne(juliet.getId())).thenReturn(juliet);

        hamlet.lendTo(juliet);
        hamlet.addWatcher(romeo);
        hamlet.addWatcher(secondWatcher);

        bookFacade.returnBook(hamlet.getId(), juliet.getId());

        verify(emailComposer).sendEmail(eq(romeo.getEmail()), eq("?"), eq("?"), eq(romeo.getLocale()), anyMap());
        verify(emailComposer).sendEmail(eq(secondWatcher.getEmail()), eq("?"), eq("?"), eq(secondWatcher.getLocale()),
                anyMap());
        assertThat(hamlet.getWatchers()).isEmpty();
    }

    @Test
    public void whenBorrowerReturnsBook_shouldReturnBook () {
        //ARANGE
        BookEntity hamlet = hamletBook();
        UserEntity romeo = romeoUser();
        hamlet.lendTo(romeo);

        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);
        when(userRepositoryMock.findOne(romeo.getId())).thenReturn(romeo);

        when(bookToMapperMock.toTo(hamlet, romeo.getId())).thenReturn(null);

        //ACT
        BookTo result = bookFacade.returnBook(hamlet.getId(), romeo.getId());

        //ASSERT
        Assert.assertFalse("return method didn't return the book", hamlet.isLendTo(romeo));
        assertThat(hamlet.getBorrowers().size()).isEqualTo(0);
        verify(bookToMapperMock).toTo(hamlet, romeo.getId());
    }


}
