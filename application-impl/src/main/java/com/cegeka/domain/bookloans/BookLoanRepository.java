package com.cegeka.domain.bookloans;

import com.cegeka.domain.DelegatingRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookLoanRepository extends DelegatingRepository<BookLoanEntity, String> {

}
