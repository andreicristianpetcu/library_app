package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookFactory;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.infrastructure.NotifyBookAvailableCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.cegeka.domain.books.BookEntityTestFixture.aBook;
import static com.cegeka.domain.books.BookEntityTestFixture.aBookWithOneCopy;
import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookFacadeImplTest {

    @Mock
    private BookRepository bookRepositoryMock;
    @Mock
    private BookFactory bookFactoryMock;
    @Mock
    private BookToMapper bookToMapperMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private NotifyBookAvailableCommand notifyBookAvailableCommandMock;

    @InjectMocks
    private BookFacadeImpl bookFacade = new BookFacadeImpl();


    @Test
    public void getBooksCallsRepositoryFindAll() {
        when(bookRepositoryMock.findAll()).thenReturn(Collections.<BookEntity>emptyList());

        List<BookTo> books = bookFacade.getBooks(null);

        verify(bookRepositoryMock).findAll();
    }

    @Test
    public void saveBookCallsMapperAndRepositorySaveAndFlush() {
        BookTo bookTo = new BookTo(null, "One", "Two", "Three");
        BookTo expected = new BookTo("123", "One", "Two", "Three");

        BookEntity bookEntity = aBook();

        when(bookFactoryMock.toNewEntity(bookTo)).thenReturn(bookEntity);
        when(bookRepositoryMock.saveAndFlush(bookEntity)).thenReturn(bookEntity);
        when(bookToMapperMock.toTo(bookEntity, "current_user_id")).thenReturn(expected);

        BookTo result = bookFacade.saveBook(bookTo, "current_user_id");

        verify(bookFactoryMock).toNewEntity(bookTo);
        verify(bookRepositoryMock).saveAndFlush(bookEntity);
        verify(bookToMapperMock).toTo(bookEntity, "current_user_id");

        assertThat(result).isSameAs(expected);
    }

    @Test
    public void whenReturnBook_shouldSendEmailToWatchersAndClearWatchersList() {
        BookEntity book = aBookWithOneCopy();

        UserEntity romeo = romeoUser();
        UserEntity juliet = aUserEntity("juliet@mailinator.com");
        UserEntity secondWatcher = aUserEntity("secondWatcher@mailinator.com");
        secondWatcher.setLocale(Locale.JAPAN);

        when(bookRepositoryMock.findOne(book.getId())).thenReturn(book);
        when(userRepositoryMock.findOne(juliet.getId())).thenReturn(juliet);

        book.lendTo(juliet);
        book.addWatcher(romeo);
        book.addWatcher(secondWatcher);

        bookFacade.returnBook(book.getId(), juliet.getId());

        verify(notifyBookAvailableCommandMock).alertWatchersBookIsAvailable(book);
        assertThat(book.getWatchers()).isEmpty();
    }


    @Test
    public void whenBorrowerReturnsBook_shouldReturnBook() {
        //ARANGE
        BookEntity hamlet = aBook();
        UserEntity romeo = romeoUser();
        hamlet.lendTo(romeo);

        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);
        when(userRepositoryMock.findOne(romeo.getId())).thenReturn(romeo);

        when(bookToMapperMock.toTo(hamlet, romeo.getId())).thenReturn(null);

        //ACT
        BookTo result = bookFacade.returnBook(hamlet.getId(), romeo.getId());

        //ASSERT
        assertThat(hamlet.isLendTo(romeo)).isFalse();
        assertThat(hamlet.getBorrowers().size()).isEqualTo(0);
        verify(bookToMapperMock).toTo(hamlet, romeo.getId());
    }


    @Test
    public void whenWatchBook_shouldWatchBook() {
        //ARANGE
        BookEntity hamlet = aBook();
        UserEntity romeo = romeoUser();

        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);
        when(userRepositoryMock.findOne(romeo.getId())).thenReturn(romeo);

        when(bookToMapperMock.toTo(hamlet, romeo.getId())).thenReturn(null);

        //ACT
        bookFacade.watchBook(hamlet.getId(), romeo.getId());

        //ASSERT
        assertThat(hamlet.getWatchers()).contains(romeo);
        assertThat(hamlet.getWatchers().size()).isEqualTo(1);
    }

    @Test
    public void whenUnwatchBook_shouldUnwatchBook() {
        //ARANGE
        BookEntity hamlet = aBook();
        UserEntity romeo = romeoUser();

        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);
        when(userRepositoryMock.findOne(romeo.getId())).thenReturn(romeo);

        when(bookToMapperMock.toTo(hamlet, romeo.getId())).thenReturn(null);

        hamlet.addWatcher(romeo);

        //ACT
        bookFacade.unwatchBook(hamlet.getId(), romeo.getId());

        //ASSERT
        assertThat(hamlet.getWatchers().size()).isEqualTo(0);
    }

    @Test
    public void whenUnwatchBook_shouldDoNothingIfBookIsNotWatched() {
        //ARANGE
        BookEntity hamlet = aBook();
        UserEntity romeo = romeoUser();
        UserEntity juliet = aUserEntity("juliet@mailinator.com");

        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);
        when(userRepositoryMock.findOne(romeo.getId())).thenReturn(romeo);

        when(bookToMapperMock.toTo(hamlet, romeo.getId())).thenReturn(null);

        hamlet.addWatcher(juliet);

        //ACT
        bookFacade.unwatchBook(hamlet.getId(), romeo.getId());

        //ASSERT
        assertThat(hamlet.getWatchers().size()).isEqualTo(1);
        assertThat(hamlet.getWatchers()).contains(juliet);
    }

    @Test
    public void whenUpdatingNumberBookOfCopies_shouldSaveChanges() {
        BookEntity hamlet = aBook();
        when(bookRepositoryMock.findOne(hamlet.getId())).thenReturn(hamlet);

        bookFacade.updateAvailableCopies(hamlet.getId(), 10);

        assertThat(hamlet.availableCopies()).isEqualTo(10);
        verify(bookRepositoryMock).save(hamlet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenUpdatingNumberOfBookCopiesForInexistentBookId_shouldThrowException() {
        //mocking
        String book_id = "book_id";
        when(bookRepositoryMock.findOne(book_id)).thenReturn(null);

        //ACT
        bookFacade.updateAvailableCopies(book_id, 7);

        //ASSERT
        verify(bookRepositoryMock).findOne(book_id);
    }

}
