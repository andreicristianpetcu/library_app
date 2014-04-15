package com.cegeka.domain.books;

import com.cegeka.application.BookTo;
import com.cegeka.application.BorrowerTo;
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
public class BookFactory {

    public BookTo toTo(BookEntity bookEntity, String currentUserId) {
        BookTo bookTo = new BookTo(bookEntity.getId(), bookEntity.getTitle(), bookEntity.getAuthor(), bookEntity.getIsbn());
        bookTo.setBorrowedByCurrentUser(false);

        List<BorrowerTo> borrowerToList = new ArrayList<BorrowerTo>();
        List<UserEntity> borrowers = bookEntity.getBorrowers();
        for (UserEntity borrower : borrowers) {
            BorrowerTo borrowerTo = new BorrowerTo(borrower.getId(), borrower.getProfile().getFullName(), borrower.getEmail());
            borrowerToList.add(borrowerTo);
            if (borrowerTo.getId().equals(currentUserId)) {
                bookTo.setBorrowedByCurrentUser(true);
            }
        }
        bookTo.setBorrowers(borrowerToList);
        bookTo.setAvailableCopies(bookEntity.getAvailableCopies());

        BookDetailsEntity details = bookEntity.getDetails();
        if (details != null) {
            bookTo.setPublishedDate(details.getPublishedDate());
            bookTo.setPublisher(details.getPublisher());
            bookTo.setDescription(details.getDescription());
            bookTo.setCoverImage(details.getCoverImage());
        }

        bookTo.setWatchedByCurrentUser(false);
        List<UserEntity> watchers = bookEntity.getWatchers();
        for (UserEntity watcher : watchers) {
            if (watcher.getId().equals(userId)) {
                bookTo.setWatchedByCurrentUser(true);
            }
        }

        return bookTo;
    }


    //TODO: this is a Factory method;
    public BookEntity toNewEntity(BookTo bookTo) {
        BookEntity bookEntity = new BookEntity(bookTo.getTitle(), bookTo.getAuthor(), bookTo.getIsbn());
        bookEntity.setCopies(bookTo.getAvailableCopies());
        bookEntity.setDetails(new BookDetailsEntity(bookTo.getPublishedDate(), bookTo.getPublisher(),
                bookTo.getCoverImage(), bookTo.getDescription()));
        return bookEntity;
    }

    public List<BookTo> from(List<BookEntity> all, String userId) {
        return newArrayList(transform(all, bookEntityToBookToTransform(userId)));
    }

    private Function<? super BookEntity, BookTo> bookEntityToBookToTransform(final String userId) {
        return new Function<BookEntity, BookTo>() {
            @Override
            public BookTo apply(BookEntity input) {
                return toTo(input, userId);
            }
        };
    }

}
