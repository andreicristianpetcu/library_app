package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class BookFactory {

    public BookEntity toNewEntity(BookTo bookTo) {
        BookEntity bookEntity = new BookEntity(bookTo.getTitle(), bookTo.getAuthor(), bookTo.getIsbn());
        bookEntity.setCopies(bookTo.getAvailableCopies());
        bookEntity.setDetails(new BookDetailsEntity(bookTo.getPublishedDate(), bookTo.getPublisher(),
                bookTo.getCoverImage(), bookTo.getDescription()));
        return bookEntity;
    }
}
