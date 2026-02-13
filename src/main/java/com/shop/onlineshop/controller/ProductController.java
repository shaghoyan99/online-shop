package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.service.CategoryService;
import com.shop.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Value("${online.shop.upload.image.directory,path}")
    private String imageDirectoryPath;

    @GetMapping("admin/products/{id}")
    public String productDetails(ModelMap modelMap,
                                 @PathVariable int id) {
        modelMap.addAttribute("product", productService.findById(id));
        return "product-details";
    }

    @GetMapping("/products/{productId}")
    public String productDetails(
            @PathVariable int productId,
            Model model) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        return "product-details";
    }

}
