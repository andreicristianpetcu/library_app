package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class BookFactory {

    public BookEntity toNewEntity(BookTo bookTo) {
        BookEntity entity = new BookEntity.Builder()
                .withDetails(detailsFrom(bookTo))
                .withCopies(bookTo.getAvailableCopies())
                .withTitle(bookTo.getTitle())
                .withAuthor(bookTo.getAuthor())
                .withIsbn(bookTo.getIsbn())
                .build();
        return entity;
    }

    private BookDetails detailsFrom(BookTo bookTo) {
        return new BookDetails(bookTo.getPublishedDate(), bookTo.getPublisher(),
                bookTo.getCoverImage(), bookTo.getDescription());
    }
}
