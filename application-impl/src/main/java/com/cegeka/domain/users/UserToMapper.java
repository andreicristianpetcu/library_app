package com.cegeka.domain.users;

import com.cegeka.application.Role;
import com.cegeka.application.UserTo;
import com.google.common.base.Function;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Service
@Scope(value = SCOPE_SINGLETON)
public class UserToMapper {
    public UserTo toTo(UserEntity userEntity) {
        UserTo userTo = new UserTo();
        userTo.setId(userEntity.getId());
        userTo.setEmail(userEntity.getEmail());
        userTo.setRoles( new HashSet<Role>(userEntity.getRoles()));
        userTo.setConfirmed(userEntity.isConfirmed());
        userTo.setFirstName(userEntity.getProfile().getFirstName());
        userTo.setLastName(userEntity.getProfile().getLastName());
        userTo.setLocale(userEntity.getLocale());
        return userTo;
    }

    public UserTo toToWithPassword(UserEntity userEntity) {
        UserTo userTo = toTo(userEntity);
        userTo.setPassword(userEntity.getPassword());
        return userTo;
    }

    public UserEntity toNewEntity(UserTo userTo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userTo.getEmail());
        userEntity.setPassword(userTo.getPassword());
        userEntity.getProfile().setFirstName(userTo.getFirstName());
        userEntity.getProfile().setLastName(userTo.getLastName());
        userEntity.setConfirmed(userTo.getConfirmed());
        //TODO: map roles
        return userEntity;
    }

    public List<UserTo> from(List<UserEntity> all) {
        return newArrayList(transform(all, userEntityToUserToTransform()));
    }

    private  Function<UserEntity, UserTo> userEntityToUserToTransform() {
        return new Function<UserEntity, UserTo>() {
            @Override
            public UserTo apply(UserEntity input) {
                return toTo(input);
            }
        };
    }

    public void toExistingEntity(UserEntity userEntity, UserTo userTo) {
        userEntity.setEmail(userTo.getEmail());
        userEntity.getProfile().setFirstName(userTo.getFirstName());
        userEntity.getProfile().setLastName(userTo.getLastName());
        userEntity.setConfirmed(userTo.getConfirmed());
        userEntity.getRoles().clear();
        userEntity.getRoles().addAll(userTo.getRoles());
    }
}
