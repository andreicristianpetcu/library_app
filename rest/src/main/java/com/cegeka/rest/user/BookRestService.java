package com.cegeka.rest.user;

import com.cegeka.application.BookFacade;
import com.cegeka.application.BookTo;
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
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class BookRestService {

    private Logger logger = LoggerFactory.getLogger(BookRestService.class);

    @Autowired
    private BookFacade bookFacade;

    @RequestMapping("/books")
    @ResponseBody
    public List<BookTo> getBooks(){
        return bookFacade.getBooks();
    }

    //TODO: HATEOAS? return location as well?
    @RequestMapping(value = "/book", method = POST)
    @ResponseBody
    public ResponseEntity saveBook(@RequestBody BookTo bookTo) {
        bookFacade.saveBook(bookTo);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/book", method = PUT)
    @ResponseBody
    public ResponseEntity borrowBook(@RequestBody String bookId) {
        BookTo bookTo = bookFacade.borrowBook(bookId);
        return new ResponseEntity<BookTo>(bookTo, HttpStatus.OK);
    }
}
