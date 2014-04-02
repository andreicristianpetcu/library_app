package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import com.cegeka.domain.users.UserEntity;
import com.google.common.base.Function;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class BookToMapper {
    public BookTo toTo(BookEntity bookEntity) {
        UserEntity borrower = bookEntity.getBorrower();
        String username = borrower != null ? borrower.getProfile().getFullName() : null;
        BookTo bookTo = new BookTo(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn(), username);
        bookTo.setAvailable(bookTo.getUsername() == null);
        return bookTo;
    }

    public BookEntity toNewEntity(BookTo bookTo) {
        BookEntity bookEntity = new BookEntity(bookTo.getTitle(), bookTo.getAuthor(), bookTo.getIsbn());
        return bookEntity;
    }

    public List<BookTo> from(List<BookEntity> all) {
        return newArrayList(transform(all, bookEntityToBookToTransform()));
    }

    private Function<? super BookEntity, BookTo> bookEntityToBookToTransform() {
        return new Function<BookEntity, BookTo>() {
            @Override
            public BookTo apply(BookEntity input) {
                return toTo(input);
            }
        };
    }

}
