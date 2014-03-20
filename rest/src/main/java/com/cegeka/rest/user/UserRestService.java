package com.cegeka.rest.user;

import com.cegeka.application.UserTo;
import com.cegeka.application.security.LoggedInUser;
import com.cegeka.application.security.UserDetailsTO;
import com.cegeka.application.UserFacade;
import com.cegeka.application.UserProfileTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class UserRestService {


    public static final String THERE_WAS_AN_ERROR_SAVING_THE_USER = "There was an error saving the user.";
    @Resource
    private LoggedInUser loggedInUser;

    @Autowired
    private UserFacade userFacade;
    private Logger logger = LoggerFactory.getLogger(UserRestService.class);

    @RequestMapping("/user/info")
    @ResponseBody
    public UserDetailsTO getLoggedInUser() {
        return loggedInUser.getLoggedInUserToBeSentToGUI();
    }

    @RequestMapping("/user/profile/{userId}")
    @ResponseBody
    public UserProfileTo getProfile(@PathVariable("userId") String userId) {
        return userFacade.getProfile(userId);
    }

    @RequestMapping("/users")
    @ResponseBody
    public List<UserTo> getUsers(){
        return userFacade.getUsers();
    }

    @RequestMapping(value="/user/{id}", method = GET)
    @ResponseBody
    public UserTo getUser(@PathVariable("id") String userId){
        return userFacade.getUser(userId);
    }
    @RequestMapping(value = "/user", method = POST)
    @ResponseBody
    public ResponseEntity saveUser(@RequestBody UserTo userTO) {
        try {
            userFacade.updateOrCreateNewUser(userTO);
        } catch (Exception e) {
            logger.error(THERE_WAS_AN_ERROR_SAVING_THE_USER, e);
            return new ResponseEntity<String>(THERE_WAS_AN_ERROR_SAVING_THE_USER + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }
}
