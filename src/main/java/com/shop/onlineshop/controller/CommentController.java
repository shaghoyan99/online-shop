package com.shop.onlineshop.controller;

import com.shop.onlineshop.model.Comment;
import com.shop.onlineshop.model.Product;
import com.shop.onlineshop.service.CommentService;
import com.shop.onlineshop.service.ProductService;
import com.shop.onlineshop.service.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ProductService productService;

    @PostMapping("/addComment")
    public String addComment(@RequestParam("comment") String commentText,
                             @RequestParam("productId") int productId,
                             @AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (productId == 0) {
            return "redirect:/products";
        }
        Product product = productService.findById(productId);
        if (product == null || product.getCategory() == null) {
            return "redirect:/products";
        }
        Comment comment = new Comment();
        comment.setComment(commentText);
        comment.setProduct(product);
        comment.setUser(currentUser.getUser());
        commentService.save(comment);
        return "redirect:/products/" + productId;
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable int id) {
        Comment comment = commentService.findById(id);
        if (comment == null || comment.getProduct() == null || comment.getProduct().getCategory() == null) {
            return "redirect:/products";
        }
        int productId = comment.getProduct().getId();
        int categoryId = comment.getProduct().getCategory().getId();
        commentService.deleteById(id);
        return "redirect:/products/" + productId;
    }

}
