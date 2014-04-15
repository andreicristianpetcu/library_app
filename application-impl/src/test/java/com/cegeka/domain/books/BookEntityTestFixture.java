package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import com.cegeka.application.BookToMapper;
import org.joda.time.DateTime;

import java.util.UUID;

public class BookEntityTestFixture {
    public static final String HAMLET_ID = UUID.randomUUID().toString();
    private static final String HAMLET_TITLE = "Hamlet";
    private static final String HAMLET_AUTHOR = "Shakespeare";
    private static final String HAMLET_ISBN = newRandomISBN();

    public static final String MACBETH_ID = UUID.randomUUID().toString();
    private static final String MACBETH_TITLE = "Macbeth";
    private static final String MACBETH_AUTHOR = "Shakespeare";
    private static final String MACBETH_ISBN = newRandomISBN();

    private BookEntityTestFixture() {
    }

    public static BookEntity hamletBook() {
        BookEntity entity = new BookEntity(HAMLET_TITLE, HAMLET_AUTHOR, HAMLET_ISBN);
        entity.setId(HAMLET_ID);
        entity.setCopies(new Integer((int) (Math.random()*10 + 1)));
        return entity;
    }

    public static BookEntity macbethBook() {
        BookEntity entity = new BookEntity(MACBETH_TITLE, MACBETH_AUTHOR, MACBETH_ISBN);
        entity.setId(MACBETH_ID);
        entity.setCopies(new Integer((int) (Math.random()*10 + 1)));
        return entity;
    }

    public static BookEntity newValidBook() {
        BookEntity entity = new BookEntity("Othello", MACBETH_AUTHOR, newRandomISBN());
        entity.setId(UUID.randomUUID().toString());
        entity.setCopies(new Integer((int) (Math.random() * 10 + 1)));
        return entity;
    }

    public static BookEntity newValidBookWithoutId() {
        BookEntity entity = new BookEntity("Othello", MACBETH_AUTHOR, newRandomISBN());
        entity.setCopies(new Integer((int) (Math.random() * 10 + 1)));
        entity.setDetails(new BookDetailsEntity(new DateTime().toString(), "publisher", "coverImageUrl", "description"));
        return entity;
    }

    private static String newRandomISBN() {
        return "" + Math.round(Math.random() * 123456789);
    }

    public static BookTo hamletTo() {
        return new BookToMapper().toTo(hamletBook(), null);
    }

    public static BookTo macbethTo() {
        return new BookToMapper().toTo(macbethBook(), null);
    }
}
