package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.Category;
import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.model.User;
import com.shop.onlineshop.service.CategoryService;
import com.shop.onlineshop.service.ProductService;
import com.shop.onlineshop.service.UserService;
import com.shop.onlineshop.service.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    @Value("${online.shop.upload.image.directory,path}")
    private String imageDirectoryPath;


    @GetMapping("/")
    public String home(@AuthenticationPrincipal CurrentUser currentUser, ModelMap modelMap){
        if (currentUser != null){
            modelMap.addAttribute("user",currentUser.getUser());
            modelMap.addAttribute("categories", categoryService.findAll());
        }
        return "index";
    }

    @GetMapping("/{id}/products")
    public String getProductsByCategory(@PathVariable int id, Model model) {
        Category category = categoryService.findById(id);
        List<Product> products = productService.findProductsByCategory(category.getId());
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        return "product";
    }

    @GetMapping("/products/{productId}")
    public String productDetails(
            @PathVariable int productId,
            @AuthenticationPrincipal CurrentUser currentUser,
            Model model) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        if (currentUser != null) {
            model.addAttribute("user", currentUser.getUser());
        }
        return "product-details";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String msg,ModelMap modelMap){
        modelMap.addAttribute("msg",msg);
        return "loginPage";
    }

    @GetMapping("/register")
    public String registerPage(@RequestParam(required = false) String msg, ModelMap modelMap) {
        modelMap.addAttribute("msg", msg);
        return "registerPage";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        if (userService.findOptionalByEmail(user.getEmail()).isPresent()) {
            return "redirect:/register?msg=Username already exists!";
        }
        userService.save(user);
        return "redirect:/login?msg=Registration successful, pls login!";
    }

    @GetMapping("/image/get")
    public @ResponseBody byte[] getImage(@RequestParam("photo") String photoName) {
        File file = new File(imageDirectoryPath + photoName);
        if (file.exists() && file.isFile()) {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                throw new RuntimeException("File not found");
            }
        }
        return null;
    }
}
