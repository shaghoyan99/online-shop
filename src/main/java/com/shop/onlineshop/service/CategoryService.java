package com.shop.onlineshop.service;

import com.shop.onlineshop.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    void save(Category category);

    void deleteById(int id);

    Category findById(int id);

}
