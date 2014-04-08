package com.cegeka.rest.user;

import com.cegeka.application.BookFacade;
import com.cegeka.application.BookTo;
import com.cegeka.application.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class BookRestService {

    private Logger logger = LoggerFactory.getLogger(BookRestService.class);

    @Autowired
    private BookFacade bookFacade;


    @RequestMapping("/books")
    @ResponseBody
    public List<BookTo> getBooks(){
        return bookFacade.getBooks(getCurrentUserId());
    }

    @RequestMapping(value = "/book", method = POST)
    @ResponseBody
    public ResponseEntity saveBook(@RequestBody BookTo bookTo) {
        bookFacade.saveBook(bookTo, getCurrentUserId());
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/borrow", method = POST)
    @ResponseBody
    public ResponseEntity borrowBook(@RequestBody String bookId) {
        BookTo bookTo = bookFacade.borrowBook(bookId, getCurrentUserId());
        return new ResponseEntity<BookTo>(bookTo, HttpStatus.OK);
    }

    @RequestMapping(value = "/return", method = POST)
    @ResponseBody
    public ResponseEntity returnBook(@RequestBody String bookId) {
        BookTo bookTo = bookFacade.returnBook(bookId, getCurrentUserId());
        return new ResponseEntity<BookTo>(bookTo, HttpStatus.OK);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String userId = null;
        if (principal instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) principal).getUserId();
        }
        return userId;
    }
}
