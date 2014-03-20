package com.cegeka.application;

import com.cegeka.domain.confirmation.ConfirmationService;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserProfileMapper;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.domain.users.UserToMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.cegeka.application.Role.USER;

@Service
public class UserFacadeImpl implements UserFacade {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationService confirmationService;
    @Resource
    private UserToMapper userToMapper;

    @Override
    public void registerUser(UserTo user) {
        UserEntity userEntity = userToMapper.toNewEntity(user);
        userEntity.addRole(USER);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.saveAndFlush(userEntity);
        confirmationService.sendConfirmationEmailTo(userEntity);
    }

    @Override
    public UserTo findByEmail(String email) {
        return userToMapper.toToWithPassword(userRepository.findByEmail(email));
    }

    @Override
    public UserProfileTo getProfile(String userId) {
        UserEntity userEntity = userRepository.findOne(userId);
        return UserProfileMapper.fromUserProfileEntity(userEntity.getProfile());
    }

    @Override
    public void confirmToken(String token) {
        confirmationService.confirmToken(token);
    }

    @Override
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).ADMIN)")
    public List<UserTo> getUsers() {
        return userToMapper.from(userRepository.findAll());
    }

    @Override
    public UserTo getUser(String userId) {
        return userToMapper.toTo(userRepository.findOne(userId));
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setConfirmationService(ConfirmationService confirmationService) {
        this.confirmationService = confirmationService;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(T(com.cegeka.application.Role).ADMIN,T(com.cegeka.application.Role).USER )")
    public void updateOrCreateNewUser(UserTo userTo) {
        UserFacade userFacadeBean = applicationContext.getBean(UserFacade.class);
        if (userTo.getId() == null) {
            userFacadeBean.createNewUser(userTo);
        } else {
            userFacadeBean.updateUser(userTo);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole(T(com.cegeka.application.Role).ADMIN)")
    public void createNewUser(UserTo userTo) {
        UserEntity userEntity = userToMapper.toNewEntity(userTo);
        userRepository.save(userEntity);

    }

    @Override
    public void updateUser(UserTo userTo) {
        checkThatLoggedInUserHasTheRightToEdit(userTo);
        UserEntity userEntity = userRepository.findOne(userTo.getId());
        if (userEntity != null) {
            userToMapper.toExistingEntity(userEntity, userTo);
        } else {
            throw new IllegalArgumentException("invalid user id");
        }
        userRepository.save(userEntity);
    }

    private void checkThatLoggedInUserHasTheRightToEdit(UserTo userTo) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (email == null) {
            throw new AccessDeniedException("no user is logged in");
        }
        UserEntity loggedInUser = userRepository.findByEmail(email);
        if (loggedInUser == null) {
            throw new AccessDeniedException("Could not find user: " + email + " this is unexpected...");
        }

        if (loggedInUser.getRoles().contains(Role.ADMIN)) {
            return;
        } else if (loggedInUser.getRoles().contains(Role.USER) && loggedInUser.getId().equals(userTo.getId())) {
            if(loggedInUser.getRoles().equals(userTo.getRoles())){
                return;
            }else{
                throw new AccessDeniedException("You are not allowed to change roles");
            }
        } else {
            throw new AccessDeniedException("You are not logged in or you are trying to edit another user's profile");
        }

    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setUserToMapper(UserToMapper userToMapper) {
        this.userToMapper = userToMapper;
    }
}
