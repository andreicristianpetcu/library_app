package com.cegeka.domain.books;

import com.cegeka.application.BookTo;

import java.util.UUID;

public class BookEntityTestFixture {
    public static final String HAMLET_ID = UUID.randomUUID().toString();
    private static final String HAMLET_TITLE = "Hamlet";
    private static final String HAMLET_AUTHOR = "Shakespeare";
    private static final String HAMLET_ISBN = "123456789";

    public static final String MACBETH_ID = UUID.randomUUID().toString();
    private static final String MACBETH_TITLE = "Macbeth";
    private static final String MACBETH_AUTHOR = "Shakespeare";
    private static final String MACBETH_ISBN = "123456789";

    private BookEntityTestFixture() {
    }

    public static BookEntity hamletBook() {
        BookEntity entity = new BookEntity(HAMLET_TITLE, HAMLET_AUTHOR, HAMLET_ISBN);
        entity.setId(HAMLET_ID);
        return entity;
    }

    public static BookEntity macbethBook() {
        BookEntity entity = new BookEntity(MACBETH_TITLE, MACBETH_AUTHOR, MACBETH_ISBN);
        entity.setId(MACBETH_ID);
        return entity;
    }

    public static BookTo hamletTo() {
        return new BookToMapper().toTo(hamletBook());
    }

    public static BookTo macbethTo() {
        return new BookToMapper().toTo(macbethBook());
    }
}
