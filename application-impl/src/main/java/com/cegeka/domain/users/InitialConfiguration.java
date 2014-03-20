package com.cegeka.domain.users;

import com.cegeka.application.Role;
import com.cegeka.application.UserProfileTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class InitialConfiguration {
    public static final String TEST_USER_PASSWORD = "test";
    private static String[] emails = {"romeo@mailinator.com", "juliet@mailinator.com"};
    private static String adminEmail = "admin@mailinator.com";
    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Autowired
    private org.springframework.context.ApplicationContext applicationContext;

    @PostConstruct
    public void configure() {
        InitialConfiguration proxyBean = applicationContext.getBean(this.getClass());
        proxyBean.createTestUsersIfNeeded();
        proxyBean.createAdminUserIfNeeded();
    }

    public void createTestUsersIfNeeded() {
        for (String email : emails) {
            if (!userExists(email)) {
                createUserWithRole(email, newArrayList(Role.USER));
            }
        }
    }

    public void createAdminUserIfNeeded() {
        if (!userExists(adminEmail)) {
            createUserWithRole(adminEmail, newArrayList(Role.ADMIN, Role.USER));
        }
    }

    private void createUserWithRole(String email, List<Role> roles) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        setProfileForEmail(email, userEntity);
        userEntity.setPassword(passwordEncoder.encode(TEST_USER_PASSWORD));

        for (Role role : roles) {
            userEntity.addRole(role);
        }
        userEntity.setConfirmed(true);
        userRepository.saveAndFlush(userEntity);
    }

    private void setProfileForEmail(String email, UserEntity userEntity) {
        UserProfileTo userProfileTo = email.contains(emails[0]) ? getRomeoProfile() : getJulietProfile();
        userEntity.getProfile().setFirstName(userProfileTo.getFirstName());
        userEntity.getProfile().setLastName(userProfileTo.getLastName());
        userEntity.getProfile().setPictureUrl(userProfileTo.getPictureUrl());
    }

    private boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private UserProfileTo getJulietProfile() {
        UserProfileTo julietProfile = new UserProfileTo();
        julietProfile.setFirstName("Juliet");
        julietProfile.setLastName("Zeldenthuis");
        julietProfile.setPictureUrl("juliet");

        return julietProfile;
    }

    private UserProfileTo getRomeoProfile() {
        UserProfileTo romeoProfile = new UserProfileTo();
        romeoProfile.setFirstName("Romeo");
        romeoProfile.setLastName("Zeldenthuis");
        romeoProfile.setPictureUrl("romeo");
        return romeoProfile;
    }
}
