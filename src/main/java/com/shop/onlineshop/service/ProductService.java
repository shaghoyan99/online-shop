package com.shop.onlineshop.service;

import com.shop.onlineshop.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void save(Product product);

    void deleteById(int id);

    List<Product> findAll();

    Product findById(int id);

    List<Product> findProductsByCategory(int categoryId);

}
