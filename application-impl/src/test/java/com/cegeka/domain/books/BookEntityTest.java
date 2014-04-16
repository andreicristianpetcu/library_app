package com.cegeka.domain.books;

import org.junit.Test;

public class BookEntityTest {

    @Test
    public void givenAnAvailableBook_ThenIsAvailable(){

//        new BookEntityBuilder.withDefaults()
//                .withCopies(3)
//                .build();

        BookEntity entity = BookEntityTestFixture.newValidBook();
//        entity.setCopies();
    }

}
