package com.example.myLogin.repository;

import java.util.Optional;

import com.example.myLogin.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByMail(String mail);
}
