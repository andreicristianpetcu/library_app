package com.cegeka.domain.bookloans;

import com.cegeka.application.BookLoanTo;
import com.cegeka.domain.books.BookToMapper;
import com.cegeka.domain.users.UserToMapper;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class BookLoanToMapper {

    @Autowired
    private BookToMapper bookToMapper;
    @Autowired
    private UserToMapper userToMapper;
    
    public BookLoanTo toTo(BookLoanEntity bookLoanEntity) {
        BookLoanTo bookLoanTo = new BookLoanTo();
        bookLoanTo.setBook(bookToMapper.toTo(bookLoanEntity.getBook()));
        bookLoanTo.setUser(userToMapper.toTo(bookLoanEntity.getUser()));
        bookLoanTo.setStartDate(bookLoanEntity.getStartDate());
        bookLoanTo.setEndDate(bookLoanEntity.getEndDate());
        return bookLoanTo;
    }

    public List<BookLoanTo> from(List<BookLoanEntity> all) {
        return newArrayList(transform(all, bookEntityToBookToTransform()));
    }

    private Function<? super BookLoanEntity, BookLoanTo> bookEntityToBookToTransform() {
        return new Function<BookLoanEntity, BookLoanTo>() {
            @Override
            public BookLoanTo apply(BookLoanEntity input) {
                return toTo(input);
            }
        };
    }

}
