package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import com.cegeka.domain.users.UserEntity;
import com.google.common.base.Function;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class BookToMapper {

    public BookTo toTo(BookEntity bookEntity) {
        BookTo bookTo = new BookTo(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn());

        List<String> borrowerIds = new ArrayList<String>();
        List<UserEntity> borrowers = bookEntity.getBorrowers();
        for (UserEntity borrower : borrowers) {
            borrowerIds.add(borrower.getId());
        }
        bookTo.setUserIds(borrowerIds);

        bookTo.setAvailableCopies(bookEntity.getCopies() - bookEntity.getBorrowers().size());
        return bookTo;
    }

    public BookEntity toNewEntity(BookTo bookTo) {
        BookEntity bookEntity = new BookEntity(bookTo.getTitle(), bookTo.getAuthor(), bookTo.getIsbn());
        bookEntity.setCopies(bookTo.getAvailableCopies());
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
