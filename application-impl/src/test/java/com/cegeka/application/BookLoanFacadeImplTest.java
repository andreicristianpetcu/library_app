package com.cegeka.application;

import com.cegeka.domain.bookloans.BookLoanEntity;
import com.cegeka.domain.bookloans.BookLoanRepository;
import com.cegeka.domain.bookloans.BookLoanToMapper;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.books.BookToMapper;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.domain.users.UserToMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.cegeka.domain.books.BookEntityTestFixture.*;
import static com.cegeka.domain.user.UserEntityTestFixture.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by monicat on 01/04/2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class BookLoanFacadeImplTest {

    @InjectMocks
    private BookLoanFacadeImpl bookLoanFacade = new BookLoanFacadeImpl();

    @Mock
    private BookLoanRepository bookLoanRepositoryMock;
    @Mock
    private BookRepository bookRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private BookLoanToMapper bookLoanToMapperMock;
    @Mock
    private BookToMapper bookToMapperMock;
    @Mock
    private UserToMapper userToMapperMock;


    @Test
    public void testGetBookLoansCallsRepository() throws Exception {
        when(bookLoanRepositoryMock.findAll()).thenReturn(Collections.<BookLoanEntity>emptyList());

        List<BookLoanTo> bookLoans = bookLoanFacade.getBookLoans();

        verify(bookLoanRepositoryMock).findAll();
        assertThat(bookLoans.size()).isEqualTo(0);
    }

    @Test
    public void testSaveBookLoan() throws Exception {
        Date now = new Date();
        BookLoanTo expectedTo = new BookLoanTo(hamletTo(), romeoUserTo(), now);
        BookLoanEntity expectedEntity = new BookLoanEntity(hamletBook(), romeoUser(), now);

        when(bookLoanToMapperMock.toTo(expectedEntity)).thenReturn(expectedTo);
        when(bookRepositoryMock.findOne(HAMLET_ID)).thenReturn(hamletBook());
        when(userRepositoryMock.findOne(ROMEO_ID)).thenReturn(romeoUser());
        when(userToMapperMock.toTo(romeoUser())).thenReturn(romeoUserTo());
        when(bookToMapperMock.toTo(hamletBook())).thenReturn(hamletTo());

        BookTo bookTo = new BookTo();
        bookTo.setId(HAMLET_ID);
        UserTo userTo = new UserTo();
        userTo.setId(ROMEO_ID);

        BookLoanTo newBookLoanTo = new BookLoanTo(bookTo, userTo, now);
        bookLoanFacade.saveBookLoan(newBookLoanTo);

        verify(bookLoanRepositoryMock).saveAndFlush(expectedEntity);
    }
}
