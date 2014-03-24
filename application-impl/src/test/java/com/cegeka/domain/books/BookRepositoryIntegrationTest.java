package com.cegeka.domain.books;

import com.cegeka.IntegrationTest;
import com.cegeka.application.Role;
import com.cegeka.domain.user.UserEntityTestFixture;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import javax.persistence.PersistenceException;

import java.util.List;

import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;
import static org.fest.assertions.Assertions.assertThat;

public class BookRepositoryIntegrationTest extends IntegrationTest {

    public static final String PICTURE_URL = "pictureUrl";
    public static final String TEST_USER_EMAIL_2 = "some_email@email.com";
    @Resource
    private BookRepository bookRepository;

    BookEntity aBookEntity;

    @Before
    public void setUp() {
        aBookEntity = new BookEntity("An introduction to TDD", "Andrei Petcu", "123");
        bookRepository.saveAndFlush(aBookEntity);
    }

    @Test
    public void canRetrieve() {
        List<BookEntity> all = bookRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all).contains(aBookEntity);
    }
}
