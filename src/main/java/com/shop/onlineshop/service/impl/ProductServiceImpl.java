package com.shop.onlineshop.service.impl;

import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.repository.ProductRepository;
import com.shop.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void save(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findProductsByCategory(int categoryId) {
        return productRepository.findProductsByCategoryId(categoryId);
    }
}
