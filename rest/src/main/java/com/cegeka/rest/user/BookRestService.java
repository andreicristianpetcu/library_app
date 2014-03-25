package com.cegeka.rest.user;

import com.cegeka.application.*;
import com.cegeka.application.security.LoggedInUser;
import com.cegeka.application.security.UserDetailsTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @RequestMapping(value="/book/{id}", method = GET)
    @ResponseBody
    public BookTo getUser(@PathVariable("id") String bookId){
        return new BookTo(bookId, "New book", "Library App", "123");
    }

    //TODO: HATEOAS? return location as well?
    @RequestMapping(value = "/book", method = POST)
    @ResponseBody
    public ResponseEntity saveBook(@RequestBody BookTo bookTo) {
        bookFacade.saveBook(bookTo);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
}
