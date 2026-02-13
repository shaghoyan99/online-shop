package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.Category;
import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.service.CategoryService;
import com.shop.onlineshop.service.ProductService;
import com.shop.onlineshop.service.UserService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    @Value("${online.shop.upload.image.directory,path}")
    private String imageDirectoryPath;

    @GetMapping("/home")
    public String home(){
        return "admin/home";
    }

    @GetMapping("/products")
    public String products(ModelMap modelMap){
        modelMap.addAttribute("products", productService.findAll());
        return "admin/products";
    }

    @GetMapping("/users")
    public String users(ModelMap modelMap){
        modelMap.addAttribute("users",userService.findAll());
        return "admin/users";
    }

    @GetMapping("/categories")
    public String categories(ModelMap modelMap){
        modelMap.addAttribute("categories",categoryService.findAll());
        return "admin/categories";
    }

    @GetMapping("/createProduct")
    public String createProduct(@AuthenticationPrincipal CurrentUser currentUser,ModelMap modelMap){
        modelMap.addAttribute("categories",categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/createProduct")
    public String createProductPost(@ModelAttribute Product product,@RequestParam("photo") MultipartFile multipartFile){
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            File file = new File(imageDirectoryPath + fileName);
            try{
                multipartFile.transferTo(file);
                product.setPictureName(fileName);
            }catch (IOException e){
                throw new RuntimeException("File not found");
            }
        }
        productService.save(product);
        return "redirect:/admin/products";
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

    @GetMapping("/createCategory")
    public String createCategory(){
        return "admin/category-form";
    }

    @PostMapping("/createCategory")
    public String createCategoryPost(@ModelAttribute Category category){
        categoryService.save(category);
        return "redirect:/admin/categories";
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
