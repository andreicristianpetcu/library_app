package com.cegeka.application;

import java.util.List;

public interface BookLoanFacade {

    List<BookLoanTo> getBookLoans();

    BookLoanTo saveBookLoan(BookLoanTo newBookLoan);

    BookLoanTo borrowBook(String bookId);
}
