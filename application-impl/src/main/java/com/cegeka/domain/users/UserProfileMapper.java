package com.cegeka.domain.users;


import com.cegeka.application.UserProfileTo;

public class UserProfileMapper {
    private UserProfileMapper() {
    }

    public static UserProfileTo fromUserProfileEntity(UserProfileEntity userProfileEntity) {
        UserProfileTo userProfileTo = new UserProfileTo();
        userProfileTo.setFirstName(userProfileEntity.getFirstName());
        userProfileTo.setLastName(userProfileEntity.getLastName());
        userProfileTo.setPictureUrl(userProfileEntity.getPictureUrl());
        return userProfileTo;
    }
}
