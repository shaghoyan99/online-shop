package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.Category;
import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.service.CategoryService;
import com.shop.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/categories/{id}/products")
    public String getProductsByCategory(@PathVariable int id, Model model) {
        Category category = categoryService.findById(id);
        List<Product> products = category != null
                ? productService.findProductsByCategory(category.getId())
                : List.of();
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "product";
    }
}
