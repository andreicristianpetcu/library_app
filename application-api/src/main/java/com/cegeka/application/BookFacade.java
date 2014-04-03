package com.cegeka.application;

import java.util.List;

public interface BookFacade {

    List<BookTo> getBooks();

    BookTo getBook(String userId);

    BookTo saveBook(BookTo newBook);

    BookTo borrowBook(String bookId, String userId);

    BookTo returnBook(String bookId, String userId);
}
