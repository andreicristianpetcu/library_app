package com.cegeka.domain.bookloan;

import com.cegeka.IntegrationTest;
import com.cegeka.application.Role;
import com.cegeka.domain.bookloans.BookLoanEntity;
import com.cegeka.domain.bookloans.BookLoanRepository;
import com.cegeka.domain.books.BookEntity;
import com.cegeka.domain.books.BookRepository;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import org.fest.assertions.Index;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.Index.atIndex;
import static org.springframework.data.jpa.domain.Specifications.where;

public class BookLoanRepositoryIntegrationTest extends IntegrationTest {

    public static final String PICTURE_URL = "pictureUrl";
    public static final String TEST_USER_EMAIL_2 = "some_email@email.com";

    @Resource
    private BookRepository bookRepository;
    @Resource
    private BookLoanRepository bookLoanRepository;
    @Resource
    private UserRepository userRepository;

    UserEntity romeo, juliet;
    BookEntity hamlet, macbeth;

    @Before
    public void setUp() {
        romeo = new UserEntity();
        romeo.setEmail("romeo@mailinator.com");
        romeo.setPassword("juliet");
        romeo.addRole(Role.USER);
        romeo.setConfirmed(true);

        juliet = new UserEntity();
        juliet.setEmail("juliet@mailinator.com");
        juliet.setPassword("romeo");
        juliet.addRole(Role.USER);
        juliet.setConfirmed(true);

        hamlet = new BookEntity("Hamlet", "Shakespeare", "123");
        macbeth = new BookEntity("Macbeth", "Shakespeare", "234");

        userRepository.saveAndFlush(romeo);
        userRepository.saveAndFlush(juliet);
        bookRepository.saveAndFlush(hamlet);
        bookRepository.saveAndFlush(macbeth);
    }

    @Test
    public void canSaveNewLoan() {
        List<BookLoanEntity> allBefore = bookLoanRepository.findAll();
        assertThat(allBefore.size()).isEqualTo(0);
        BookLoanEntity bookLoan = new BookLoanEntity(hamlet, romeo);

        bookLoanRepository.saveAndFlush(bookLoan);

        List<BookLoanEntity> allAfter = bookLoanRepository.findAll();
        assertThat(allAfter.size()).isEqualTo(1);
        assertThat(allAfter).contains(bookLoan, atIndex(0));
    }

    @Test
    public void canSaveTwoLoans() {
        List<BookLoanEntity> allBefore = bookLoanRepository.findAll();
        assertThat(allBefore.size()).isEqualTo(0);
        BookLoanEntity bookLoan1 = new BookLoanEntity(hamlet, romeo);
        BookLoanEntity bookLoan2 = new BookLoanEntity(hamlet, juliet);
        BookLoanEntity bookLoan3 = new BookLoanEntity(macbeth, juliet);

        bookLoanRepository.saveAndFlush(bookLoan1);
        bookLoanRepository.saveAndFlush(bookLoan2);
        bookLoanRepository.saveAndFlush(bookLoan3);

        List<BookLoanEntity> allHamletLoans = bookLoanRepository.findAll(where(bookIs(hamlet)).and(userIs(romeo)));
        assertThat(allHamletLoans.size()).equals(1);
    }

    private Specification<BookLoanEntity> userIs(final UserEntity userEntity) {
        return new Specification<BookLoanEntity>() {
            @Override
            public Predicate toPredicate(Root<BookLoanEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("user"), userEntity);
            }
        };
    }

    private Specification<BookLoanEntity> bookIs(final BookEntity bookEntity) {
        return new Specification<BookLoanEntity>() {
            @Override
            public Predicate toPredicate(Root<BookLoanEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("book"), bookEntity);
            }
        };
    }


}
