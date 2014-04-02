package com.cegeka.application;

import com.cegeka.domain.bookloans.BookLoanEntity;
import com.cegeka.domain.bookloans.BookLoanRepository;
import com.cegeka.domain.bookloans.BookLoanToMapper;
import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.books.BookToMapper;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.domain.users.UserToMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Transactional(readOnly = true)
public class BookLoanFacadeImpl implements BookLoanFacade {

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookToMapper bookToMapper;

    @Autowired
    private UserToMapper userToMapper;

    @Autowired
    private BookLoanToMapper bookLoanToMapper;

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public List<BookLoanTo> getBookLoans() {
        return bookLoanToMapper.from(bookLoanRepository.findAll());
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public BookLoanTo saveBookLoan(BookLoanTo newBookLoan) {
        BookLoanTo bookLoanTo = checkNotNull(newBookLoan);
        BookEntity book = bookRepository.findOne(checkNotNull(bookLoanTo.getBook()).getId());
        UserEntity user = userRepository.findOne(checkNotNull(bookLoanTo.getUser()).getId());
        BookLoanEntity bookLoan = new BookLoanEntity(book, user, checkNotNull(bookLoanTo.getStartDate()));

        return bookLoanToMapper.toTo(bookLoanRepository.saveAndFlush(bookLoan));

    }
}
