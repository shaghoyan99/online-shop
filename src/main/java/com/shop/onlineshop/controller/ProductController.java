package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.service.CategoryService;
import com.shop.onlineshop.service.ProductService;
import com.shop.onlineshop.service.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
    public String productDetails(@AuthenticationPrincipal CurrentUser currentUser,
                                 ModelMap modelMap,
                                 @PathVariable int id) {
        modelMap.addAttribute("product", productService.findById(id));
        if (currentUser != null) {
            modelMap.addAttribute("user", currentUser.getUser());
        }
        return "product-details";
    }

    @GetMapping("/admin/products/{id}/details")
    public String adminProductDetails(@PathVariable int id) {
        return "redirect:/products/" + id;
    }

    @GetMapping("/admin/products/{id}/delete")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/{id}/edit")
    public String editProduct(@PathVariable int id, ModelMap modelMap){
        modelMap.addAttribute("product", productService.findById(id));
        modelMap.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/admin/products/{id}/edit")
    public String editProductPost(@PathVariable int id,
                                  @ModelAttribute Product product,
                                  @RequestParam(value = "photo", required = false) MultipartFile multipartFile){
        Product existing = productService.findById(id);
        if (existing == null) {
            return "redirect:/admin/products";
        }

        product.setId(id);
        product.setComments(existing.getComments());

        if (product.getCategory() == null || product.getCategory().getId() == 0) {
            product.setCategory(existing.getCategory());
        }

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(imageDirectoryPath + fileName);
            try {
                multipartFile.transferTo(file);
                product.setPictureName(fileName);
            } catch (IOException e) {
                throw new RuntimeException("File not found");
            }
        } else {
            product.setPictureName(existing.getPictureName());
        }

        productService.save(product);
        return "redirect:/admin/products";
    }


}
