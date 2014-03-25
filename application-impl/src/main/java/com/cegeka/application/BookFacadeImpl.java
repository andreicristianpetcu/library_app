package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.confirmation.ConfirmationService;
import com.cegeka.domain.users.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.cegeka.application.Role.USER;

@Service
@Transactional(readOnly = true)
public class BookFacadeImpl implements BookFacade {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookToMapper bookToMapper;

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public List<BookTo> getBooks() {
        return bookToMapper.from(bookRepository.findAll());
    }

    @Override
    public BookTo getBook(String bookId) {
        return bookToMapper.toTo(bookRepository.findOne(bookId));
    }

    @Override
    @Transactional
    public BookTo saveBook(BookTo newBook) {
        BookEntity bookEntity = bookToMapper.toNewEntity(newBook);
        BookEntity bookEntity1 = bookRepository.saveAndFlush(bookEntity);
        return bookToMapper.toTo(bookEntity1);
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void setBookToMapper(BookToMapper bookToMapper) {
        this.bookToMapper = bookToMapper;
    }
}
