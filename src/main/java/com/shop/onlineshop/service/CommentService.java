package com.shop.onlineshop.service;

import com.shop.onlineshop.model.Comment;

public interface CommentService {

    void save(Comment comment);

    Comment findById(int id);

    void deleteById(int id);
}
