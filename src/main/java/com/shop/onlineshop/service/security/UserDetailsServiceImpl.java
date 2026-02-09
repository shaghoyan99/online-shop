package com.shop.onlineshop.service.security;

import com.shop.onlineshop.exception.UserEmailNotFoundException;
import com.shop.onlineshop.model.User;
import com.shop.onlineshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserEmailNotFoundException {
        User user = userService.findOptionalByEmail(username)
                .orElseThrow(() ->
                        new UserEmailNotFoundException("User not found with email: " + username));
        return new CurrentUser(user);
    }
}
