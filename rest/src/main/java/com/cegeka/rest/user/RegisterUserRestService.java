package com.cegeka.rest.user;

import com.cegeka.application.TokenNotFoundException;
import com.cegeka.application.UserFacade;
import com.cegeka.application.UserTo;
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

@Controller
public class RegisterUserRestService {
    private static Logger logger = LoggerFactory.getLogger(RegisterUserRestService.class);
    @Autowired
    private UserFacade userFacade;

    @RequestMapping(value = "/register_user")
    @ResponseBody
    public ResponseEntity doRegisterUser(@RequestBody UserTo userTO) {
        try {
            userFacade.registerUser(userTO);
        } catch (Exception e) {
            logger.error("There was an error registering the user.", e);
            return new ResponseEntity<String>("The email is taken or invalid.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm_token/{token}")
    @ResponseBody
    public ResponseEntity confirmRegistration(@PathVariable(value = "token") String token) {
        try {
            userFacade.confirmToken(token);
        } catch (TokenNotFoundException e) {
            logger.error("The confirmation token is invalid or the email is already confirmed.", e);
            return new ResponseEntity<String>("The confirmation token is invalid or the email is already confirmed.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
