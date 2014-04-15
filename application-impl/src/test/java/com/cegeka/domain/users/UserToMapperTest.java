package com.cegeka.domain.users;

import com.cegeka.application.Role;
import com.cegeka.application.UserTo;
import com.cegeka.domain.user.UserEntityTestFixture;
import org.junit.Test;

import java.util.Locale;

import static com.cegeka.application.UserToTestFixture.aUserTo;
import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static org.fest.assertions.Assertions.assertThat;

public class UserToMapperTest {
    private UserToMapper userToMapper = new UserToMapper();

    @Test
    public void toToCorrectlyMapsFields() {
        UserEntity given = aUserEntity("romeo@mailinator.com");

        UserTo actual = userToMapper.toTo(given);

        assertMappedTo(given, actual);
        assertThat(actual.getPassword()).isNull();
    }

    @Test
    public void toToUsesASafeCopyOfRoles(){
        UserEntity given = aUserEntity("romeo@mailinator.com");
        UserTo actual = userToMapper.toTo(given);
        actual.getRoles().add(Role.ADMIN);

        assertThat(actual.getRoles()).isNotEqualTo(given.getRoles());

    }

    @Test
    public void mapperShouldCopyPasswordTo_whenToToWithPasswordIsCalled() {
        UserEntity given = aUserEntity("romeo@mailinator.com");

        UserTo actual = userToMapper.toToWithPassword(given);

        assertMappedTo(given, actual);
        assertThat(actual.getPassword()).isEqualTo(UserEntityTestFixture.PASSWORD);
    }

    @Test
    public void mapToNewEntityFromToShouldMapFields() {
        UserTo given = aUserTo();

        UserEntity actual = userToMapper.toNewEntity(given);

        assertMappedEntity(given, actual);
        assertThat(actual.getId()).isNull();
        assertThat(actual.getLocale()).isEqualTo(Locale.ENGLISH);
    }

    @Test
    public void mapToExistingEntity_ShouldMapRoles(){
        UserEntity userEntity = aUserEntity("romeo@mailinator.com");

        UserTo userTo = aUserTo();
        userTo.getRoles().remove(Role.USER);
        userTo.getRoles().add(Role.ADMIN);

        userToMapper.toExistingEntity(userEntity,userTo);

        assertThat(userEntity.getRoles()).isEqualTo(userTo.getRoles());
    }

    private void assertMappedEntity(UserTo given, UserEntity actual) {
        assertThat(actual.isConfirmed()).isEqualTo(given.getConfirmed());
        assertThat(actual.getEmail()).isEqualTo(given.getEmail());
        assertThat(actual.getProfile().getFirstName()).isEqualTo(given.getFirstName());
        assertThat(actual.getProfile().getLastName()).isEqualTo(given.getLastName());
        //assertThat(actual.getRoles()).containsOnly(given.getRoles().toArray()); TODO: map roles
    }

    private void assertMappedTo(UserEntity given, UserTo actual) {
        assertThat(actual.getId()).isEqualTo(given.getId());
        assertThat(actual.getConfirmed()).isEqualTo(given.isConfirmed());
        assertThat(actual.getEmail()).isEqualTo(given.getEmail());
        assertThat(actual.getFirstName()).isEqualTo(given.getProfile().getFirstName());
        assertThat(actual.getLastName()).isEqualTo(given.getProfile().getLastName());
        assertThat(actual.getLocale()).isEqualTo(given.getLocale());
        assertThat(actual.getRoles()).containsOnly(given.getRoles().toArray());
    }

}
