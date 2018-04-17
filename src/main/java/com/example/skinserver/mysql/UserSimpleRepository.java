package com.example.skinserver.mysql;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by chanson.cc on 2018/4/17.
 */
public interface UserSimpleRepository extends CrudRepository<User, Long> {

    List<User> findByName(String name);

    Long deleteByName(String name);
}
