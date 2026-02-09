package com.shop.onlineshop.service.impl;

import com.shop.onlineshop.exception.UserEmailNotFoundException;
import com.shop.onlineshop.model.User;
import com.shop.onlineshop.model.UserRole;
import com.shop.onlineshop.repository.UserRepository;
import com.shop.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findOptionalByEmail(String email) {
        return userRepository.findOptionalByEmail(email);
    }


}
