package com.cegeka.domain.books;

import org.junit.Test;

public class BookEntityTest {

    @Test
    public void givenAnAvailableBook_ThenIsAvailable(){

//        new Builder.withDefaults()
//                .withCopies(3)
//                .build();

        BookEntity entity = BookEntityTestFixture.aBook();
//        entity.setCopies();
    }

}
