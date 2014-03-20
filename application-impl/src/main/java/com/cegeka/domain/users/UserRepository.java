package com.cegeka.domain.users;

import com.cegeka.domain.DelegatingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends DelegatingRepository<UserEntity, String> {

    public UserEntity findByEmail(String email) {
        List<UserEntity> resultList = entityManager.createQuery("select u from UserEntity u where u.email = :email", UserEntity.class)
                .setParameter("email", email)
                .getResultList();
        return resultList.size() == 1 ? resultList.get(0) : null;
    }
}
