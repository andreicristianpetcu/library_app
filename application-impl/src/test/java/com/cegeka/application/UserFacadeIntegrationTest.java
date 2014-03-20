package com.cegeka.application;

import com.cegeka.IntegrationTest;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.domain.users.UserToMapper;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import java.util.List;

import static com.cegeka.application.UserToTestFixture.*;
import static com.cegeka.domain.user.UserEntityTestFixture.*;

public class UserFacadeIntegrationTest extends IntegrationTest {

    @Resource
    private UserFacade userFacade;
    @Resource
    private UserRepository userRepository;
    @Resource
    private UserToMapper userToMapper;

    @Test
    public void givenAUserWithAdminRole_thenGetUsersReturnsAListOfUsers() {
        UserEntity user =createUserAndAttachToSecurityContext(Role.ADMIN);
        List<UserTo> users = userFacade.getUsers();
        Assertions.assertThat(users).containsOnly(userToMapper.toTo(user));
    }

    @Test(expected = AccessDeniedException.class)
    public void roleAdminIsNeededForListingAllUsers() {
        createUserAndAttachToSecurityContext(Role.USER);
        userFacade.getUsers();
    }

    @Test(expected = AccessDeniedException.class)
    public void regularUsersShouldNotBeAbleToCreateNewUsers(){
        UserEntity loggedInUser =createUserAndAttachToSecurityContext(Role.USER);
        userFacade.createNewUser(new UserTo());
    }
    @Test
    public void adminsShouldBeAbleToCreateNewUsers(){
        UserEntity admin =createUserAndAttachToSecurityContext(Role.ADMIN);
        UserTo userTo = aUserTo();

        Assertions.assertThat(userRepository.findByEmail(userTo.getEmail())).isNull();
        userFacade.createNewUser(userTo);
        Assertions.assertThat(userRepository.findByEmail(userTo.getEmail())).isNotNull();
    }

    @Test
    public void regularUsersCanEditTheirOwnAccount(){
        UserEntity loggedInUser = createUserAndAttachToSecurityContext(Role.USER);
        UserTo loggedInUserTo = new UserToMapper().toTo(loggedInUser);
        userFacade.updateUser(loggedInUserTo);
    }

    @Test(expected = AccessDeniedException.class)
    public void regularUsersCanNotChangeTheirRights(){
        UserEntity loggedInUser = createUserAndAttachToSecurityContext(Role.USER);
        UserTo loggedInUserTo = new UserToMapper().toTo(loggedInUser);
        loggedInUserTo.getRoles().add(Role.ADMIN);
        userFacade.updateUser(loggedInUserTo);
    }


    @Test(expected = AccessDeniedException.class)
    public void regularUsersCannotEditOtherAccounts(){
        UserEntity loggedInUser = createUserAndAttachToSecurityContext(Role.USER);
        UserEntity otherUser = aUserEntity();
        otherUser.setEmail("other@email.com");
        otherUser =  userRepository.saveAndFlush(otherUser);
        UserTo otherUserTo = new UserToMapper().toTo(otherUser);
        userFacade.updateUser(otherUserTo);
    }

    private UserEntity createUserAndAttachToSecurityContext(Role role) {
        UserEntity normalUser = userRepository.saveAndFlush(aUserEntityWithExtraRole(role));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(EMAIL, PASSWORD));
        return normalUser;
    }


}
