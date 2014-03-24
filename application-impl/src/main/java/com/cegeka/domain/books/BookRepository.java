package com.cegeka.domain.books;

import com.cegeka.domain.DelegatingRepository;
import com.cegeka.domain.users.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository extends DelegatingRepository<BookEntity, String> {

}
