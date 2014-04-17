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
    private BookToMapper bookToMapper;

    @Autowired
    private EmailComposer emailComposer;

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public List<BookTo> getBooks(String userId) {
        return bookToMapper.from(bookRepository.findAll(), userId);
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    public BookTo getBook(String bookId, String currentUserId) {
        return bookToMapper.toTo(bookRepository.findOne(bookId), currentUserId);
    }

    @Override
    @Transactional
    public BookTo watchBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.addWatcher(user);
        bookRepository.flush();
        return bookToMapper.toTo(book, userId);
    }

    @Override
    public BookTo unwatchBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.removeWatcher(user);
        bookRepository.flush();
        return bookToMapper.toTo(book, userId);
    }

    @Override
    @Transactional
    public BookTo saveBook(BookTo newBook, String userId) {
        BookEntity bookEntity = bookFactory.toNewEntity(newBook);
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
    public BookTo returnBook(String bookId, String currentUserId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(currentUserId);

        boolean bookWasUnavailable = !book.isAvailable();
        book.returnFrom(user);

        if (bookWasUnavailable) {
            alertWatchersBookIsAvailable(book);
            book.clearAllWatchers();
        }

        return bookToMapper.toTo(book, currentUserId);
    }

    @Override
    @Transactional
    public void updateAvailableCopies(String bookId, int numberOfCopies) {
        BookEntity book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Bad Book id. The book does not exist");
        }
        book.updateAvailableCopies(numberOfCopies);
    }

    private void alertWatchersBookIsAvailable(BookEntity book) {
        for (UserEntity watcher : book.getWatchers()) {
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("user", watcher);
            values.put("book", book);
            values.put("link", "http://libraryapp.cegeka.com:8000/#/book/" + book.getId());
            emailComposer.sendEmail(watcher.getEmail(), "notify-book-available-subject", "notify-book-available-content", watcher.getLocale(), values);
        }
    }
}
