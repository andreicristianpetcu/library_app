package com.cegeka.domain.bookloans;

import com.cegeka.application.BookLoanTo;
import org.junit.Test;

import java.util.Date;

import static com.cegeka.domain.books.BookEntityTestFixture.hamletBook;
import static com.cegeka.domain.books.BookEntityTestFixture.hamletTo;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUser;
import static com.cegeka.domain.user.UserEntityTestFixture.romeoUserTo;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by monicat on 01/04/2014.
 */
public class BookLoanToMapperTest {

    BookLoanToMapper bookLoanToMapper = new BookLoanToMapper();

    @Test
    public void testToTo() throws Exception {
        Date now = new Date();

        BookLoanEntity bookLoanEntity = new BookLoanEntity(hamletBook(), romeoUser(), now);
        BookLoanTo expected = new BookLoanTo(hamletTo(), romeoUserTo(), now);

        BookLoanTo actual = bookLoanToMapper.toTo(bookLoanEntity);

        assertThat(actual).isEqualTo(expected);
    }
}
