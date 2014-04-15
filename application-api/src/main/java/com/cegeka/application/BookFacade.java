package com.cegeka.application;

import java.util.List;

public interface BookFacade {

    List<BookTo> getBooks(String userId);

    BookTo saveBook(BookTo newBook, String userId);

    BookTo borrowBook(String bookId, String userId);

    BookTo returnBook(String bookId, String userId);

    BookTo getBook(String bookId, String currentUserId);

    BookTo watchBook(String bookId, String userId);

    BookTo unwatchBook(String bookId, String userId);
}
