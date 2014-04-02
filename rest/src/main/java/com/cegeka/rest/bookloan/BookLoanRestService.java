package com.cegeka.rest.bookloan;

import com.cegeka.application.BookLoanFacade;
import com.cegeka.application.BookLoanTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by monicat on 01/04/2014.
 */

@Controller
public class BookLoanRestService {

    private Logger logger = LoggerFactory.getLogger(BookLoanRestService.class);

    @Autowired
    private BookLoanFacade bookLoanFacade;

    @RequestMapping("/books")
    @ResponseBody
    public List<BookLoanTo> getBooks(){
        return bookLoanFacade.getBookLoans();
    }

    @RequestMapping(value = "/bookLoan", method = POST)
    @ResponseBody
    public ResponseEntity saveBook(@RequestBody BookLoanTo bookLoanTo) {
        BookLoanTo result = bookLoanFacade.saveBookLoan(bookLoanTo);
        return new ResponseEntity<BookLoanTo>(result, HttpStatus.OK);
    }
}
