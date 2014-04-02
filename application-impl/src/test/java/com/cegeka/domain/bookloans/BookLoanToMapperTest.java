package com.cegeka.domain.bookloans;

import com.cegeka.application.BookLoanTo;
import com.cegeka.domain.books.BookToMapper;
import com.cegeka.domain.users.UserToMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static com.cegeka.domain.books.BookEntityTestFixture.hamletBook;
import static com.cegeka.domain.books.BookEntityTestFixture.hamletTo;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUserTo;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by monicat on 01/04/2014.
 */
@RunWith(MockitoJUnitRunner.class)
public class BookLoanToMapperTest {

    @InjectMocks
    private BookLoanToMapper bookLoanToMapper = new BookLoanToMapper();

    @Mock
    private BookToMapper bookToMapperMock;

    @Mock
    private UserToMapper userToMapperMock;

    @Test
    public void testToTo() throws Exception {
        Date now = new Date();

        BookLoanEntity bookLoanEntity = new BookLoanEntity(hamletBook(), romeoUser(), now);
        BookLoanTo expected = new BookLoanTo(hamletTo(), romeoUserTo(), now);

        when(userToMapperMock.toTo(romeoUser())).thenReturn(romeoUserTo());
        when(bookToMapperMock.toTo(hamletBook())).thenReturn(hamletTo());

        BookLoanTo actual = bookLoanToMapper.toTo(bookLoanEntity);

        assertThat(actual).isEqualTo(expected);
    }
}
