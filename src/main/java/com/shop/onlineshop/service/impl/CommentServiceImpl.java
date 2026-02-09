package com.shop.onlineshop.service.impl;

import com.shop.onlineshop.model.Comment;
import com.shop.onlineshop.repository.CommentRepository;
import com.shop.onlineshop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment findById(int id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }
}
