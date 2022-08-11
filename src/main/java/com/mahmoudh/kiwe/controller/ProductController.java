package com.mahmoudh.kiwe.controller;


import com.mahmoudh.kiwe.dto.ImageResponse;
import com.mahmoudh.kiwe.dto.InsertProductsResponse;
import com.mahmoudh.kiwe.dto.Message;
import com.mahmoudh.kiwe.dto.ProductDetailsResponse;
import com.mahmoudh.kiwe.entity.Image;
import com.mahmoudh.kiwe.entity.Product;
import com.mahmoudh.kiwe.entity.User;
import com.mahmoudh.kiwe.service.CsvService;
import com.mahmoudh.kiwe.service.ProductService;
import com.mahmoudh.kiwe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    ResponseEntity<Message> createProduct(@RequestBody Product product, Principal principal) {
        try {
            String username = principal.getName();
            User user = userService.getUserByEmailOrUserName(username);
            product.setUser(user);
            if (product.getName() == null || product.getCategory() == null || product.getQuantity() == null)
                return new ResponseEntity<>(new Message("complete all requirement"), HttpStatus.BAD_REQUEST);
            productService.saveProduct(product);
            return new ResponseEntity<>(new Message("product successfully registered"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new Message("something wrong happen"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getProductById(@PathVariable Integer id, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByEmailOrUserName(username);
        Product p = productService.findProductById(id);
        if (!p.getUser().getUserId().equals(user.getUserId())) {
            return new ResponseEntity<>(new Message("this product I belong to anther user"), HttpStatus.BAD_REQUEST);
        }
        ProductDetailsResponse productDetails = new ProductDetailsResponse(p);
        return new ResponseEntity<>(productDetails, HttpStatus.OK);
    }

    @GetMapping("")
    ResponseEntity<?> getAllProduct(Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByEmailOrUserName(username);
        List<ProductDetailsResponse> result = new ArrayList<>();
        for (Product p : user.getProduct()) {
            ProductDetailsResponse productDetails = new ProductDetailsResponse(p);
            result.add(productDetails);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Autowired
    CsvService csvService;
    @PostMapping(value = "/insertFile")
    ResponseEntity InsertProducts(@RequestParam("file") MultipartFile file, Principal principal) {
        try{
            String username = principal.getName();
            User user = userService.getUserByEmailOrUserName(username);
            List<Product> products=csvService.csvToProducts(file,user);
            InsertProductsResponse res=productService.insertMultipleProduct(products);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(new Message("something wrong happen"), HttpStatus.BAD_REQUEST);
        }
    }


}
