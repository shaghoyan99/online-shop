package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.User;
import com.shop.onlineshop.service.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal CurrentUser currentUser){
        if (currentUser == null){
            return null;
        }
        return currentUser.getUser();
    }
}
