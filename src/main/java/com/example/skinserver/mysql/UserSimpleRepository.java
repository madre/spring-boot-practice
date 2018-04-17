package com.example.skinserver.mysql;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * Created by chanson.cc on 2018/4/17.
 */
public class UserSimpleRepository extends SimpleJpaRepository<User, Long> {

    public UserSimpleRepository(JpaEntityInformation<User, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public UserSimpleRepository(Class<User> domainClass, EntityManager em) {
        super(domainClass, em);
    }
}
