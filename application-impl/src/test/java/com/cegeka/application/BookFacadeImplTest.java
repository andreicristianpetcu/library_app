package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.users.BookToMapper;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserToMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static com.cegeka.domain.user.UserEntityTestFixture.*;
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
}
