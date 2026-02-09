package com.shop.onlineshop.service;

import com.shop.onlineshop.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void save(User user);

    List<User> findAll();

    Optional<User> findOptionalByEmail(String email);
}
