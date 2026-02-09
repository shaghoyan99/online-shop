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

    @GetMapping("/admin/categories/{id}/edit")
    public String editCategory(@PathVariable int id, ModelMap modelMap) {
        modelMap.addAttribute("category", categoryService.findById(id));
        return "admin/category-form";
    }

    @PostMapping("/admin/categories/{id}/edit")
    public String editCategoryPost(@PathVariable int id, @ModelAttribute Category category) {
        Category existing = categoryService.findById(id);
        if (existing == null) {
            return "redirect:/admin/categories";
        }
        existing.setName(category.getName());
        categoryService.save(existing);
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }
}
