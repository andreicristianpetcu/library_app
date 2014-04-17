package com.cegeka.application;

import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookFactory;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.infrastructure.EmailComposer;
import com.cegeka.infrastructure.NotifyBookAvailableCommand;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class BookFacadeImpl implements BookFacade {

    @Resource
    private BookRepository bookRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private BookFactory bookFactory;

    @Resource
    private BookToMapper bookToMapper;

    @Resource
    private EmailComposer emailComposer;

    @Resource
    private NotifyBookAvailableCommand notifyBookAvailableCommand;

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
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    @Transactional
    public BookTo watchBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.addWatcher(user);
        bookRepository.flush();
        return bookToMapper.toTo(book, userId);
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    @Transactional
    public BookTo unwatchBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.removeWatcher(user);
        bookRepository.flush();
        return bookToMapper.toTo(book, userId);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).ADMIN)")
    public BookTo saveBook(BookTo newBook, String userId) {
        BookEntity bookEntity = bookFactory.toNewEntity(newBook);
        bookEntity = bookRepository.saveAndFlush(bookEntity);
        return bookToMapper.toTo(bookEntity, userId);
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    @Transactional
    public BookTo borrowBook(String bookId, String userId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(userId);
        book.lendTo(user);
        bookRepository.flush();
        return bookToMapper.toTo(book, user.getId());
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).USER)")
    @Transactional
    public BookTo returnBook(String bookId, String currentUserId) {
        BookEntity book = bookRepository.findOne(bookId);
        UserEntity user = userRepository.findOne(currentUserId);

        boolean bookWasUnavailable = !book.isAvailable();
        book.returnFrom(user);

        if (bookWasUnavailable) {
            notifyBookAvailableCommand.alertWatchersBookIsAvailable(book);
            book.clearAllWatchers();
        }

        return bookToMapper.toTo(book, currentUserId);
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).ADMIN)")
    @Transactional
    public void updateAvailableCopies(String bookId, int numberOfCopies) {
        BookEntity book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Bad Book id. The book does not exist");
        }
        book.updateAvailableCopies(numberOfCopies);
        bookRepository.save(book);
    }
}
