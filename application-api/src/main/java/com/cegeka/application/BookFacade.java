package com.cegeka.application;

import java.util.List;

public interface BookFacade {

    List<BookTo> getBooks(String userId);

    BookTo saveBook(BookTo newBook, String userId);

    BookTo borrowBook(String bookId, String userId);

    BookTo returnBook(String bookId, String userId);

    BookTo getBook(String bookId, String currentUserId);

    void watchBook(String bookId, String userId);

    void unwatchBook(String bookId, String userId);
}
