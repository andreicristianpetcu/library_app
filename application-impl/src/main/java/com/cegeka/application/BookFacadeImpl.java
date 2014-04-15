package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookFactory;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.infrastructure.EmailComposer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class BookFacadeImpl implements BookFacade {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookFactory bookFactory;

    @Autowired
    private EmailComposer emailComposer;

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public List<BookTo> getBooks(String userId) {
        return bookFactory.from(bookRepository.findAll(), userId);
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public BookTo getBook(String bookId, String currentUserId) {
        return bookFactory.toTo(bookRepository.findOne(bookId), currentUserId);
    }

    @Override
    @Transactional
    public BookTo watchBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.addWatcher(user);
        bookRepository.flush();
        return bookFactory.toTo(book, userId);
    }

    @Override
    public BookTo unwatchBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.removeWatcher(user);
        bookRepository.flush();
        return bookFactory.toTo(book, userId);
    }

    @Override
    @Transactional
    public BookTo saveBook(BookTo newBook, String userId) {
        BookEntity bookEntity = bookFactory.toNewEntity(newBook);
        bookEntity = bookRepository.saveAndFlush(bookEntity);
        return bookFactory.toTo(bookEntity, userId);
    }

    @Override
    @Transactional
    public BookTo borrowBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.lendTo(user);
        bookRepository.flush();
        return bookFactory.toTo(book, user.getId());
    }

    @Override
    @Transactional
    public BookTo returnBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        int availableCopiesBeforeReturn = book.getAvailableCopies();

        if(book.isLendTo(user)) {
            book.returnFrom(user);
            bookRepository.flush();
        }

        int availableCopiesAfterReturn = book.getAvailableCopies();
        if ( availableCopiesBeforeReturn == 0 && availableCopiesAfterReturn > 0) {
            //TODO: send link to book
            //TODO: better handling of templates
            for(UserEntity watcher : book.getWatchers()) {
                Map<String, Object> values = new HashMap<String, Object>();
                values.put("user_name", watcher.getProfile().getFirstName());
                values.put("book_title", book.getTitle());
                values.put("book_author", book.getAuthor());
                emailComposer.sendEmail(watcher.getEmail(), "notify-book-available-subject", "notify-book-available-content", watcher.getLocale(), values);
            }
            book.clearAllWatchers();
        }

        return bookFactory.toTo(book, userId);
    }
}
