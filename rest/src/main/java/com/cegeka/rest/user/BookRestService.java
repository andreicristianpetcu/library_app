package com.cegeka.rest.user;

import com.cegeka.application.BookTo;
import com.cegeka.application.UserFacade;
import com.cegeka.application.UserProfileTo;
import com.cegeka.application.UserTo;
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

    @RequestMapping("/books")
    @ResponseBody
    public List<BookTo> getBooks(){
        return new ArrayList<BookTo>(){
            {
                add(new BookTo("0", "Welcome to Node JS 2nd edition", "Monica", "123"));
                add(new BookTo("1", "Welcome to Angular 2nd edition", "Andrei", "123"));
                add(new BookTo("2", "Welcome to Scrum 2nd edition", "Cristina", "123"));
            }
        };
    }

    @RequestMapping(value="/book/{id}", method = GET)
    @ResponseBody
    public BookTo getUser(@PathVariable("id") String bookId){
        return new BookTo(bookId, "New book", "Library App", "123");
    }

    @RequestMapping(value = "/book", method = POST)
    @ResponseBody
    public ResponseEntity saveBook(@RequestBody BookTo bookTo) {
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
}
