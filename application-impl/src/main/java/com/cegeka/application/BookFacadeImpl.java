package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.books.BookToMapper;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookFacadeImpl implements BookFacade {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookToMapper bookToMapper;

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public List<BookTo> getBooks(String userId) {
        return bookToMapper.from(bookRepository.findAll(), userId);
    }

    @Override
    @Transactional
    public BookTo saveBook(BookTo newBook, String userId) {
        BookEntity bookEntity = bookToMapper.toNewEntity(newBook);
        bookEntity = bookRepository.saveAndFlush(bookEntity);
        return bookToMapper.toTo(bookEntity, userId);
    }

    @Override
    @Transactional
    public BookTo borrowBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.lendTo(user);
        bookRepository.flush();
        return bookToMapper.toTo(book, user.getId());
    }

    @Override
    @Transactional
    public BookTo returnBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        if(book.isLendTo(user)) {
            book.returnFrom(user);
            bookRepository.flush();
        }
        return bookToMapper.toTo(book, userId);
    }

    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void setBookToMapper(BookToMapper bookToMapper) {
        this.bookToMapper = bookToMapper;
    }
}
