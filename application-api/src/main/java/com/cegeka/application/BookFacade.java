package com.cegeka.application;

import java.util.List;

public interface BookFacade {

    List<BookTo> getBooks();

    BookTo getBook(String userId);

    BookTo saveBook(BookTo newBook);

    void borrowBook(String bookId);
}
