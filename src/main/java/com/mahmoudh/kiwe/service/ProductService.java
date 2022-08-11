package com.mahmoudh.kiwe.service;

import com.mahmoudh.kiwe.entity.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    Product findProductById(Integer productId);

    List<Product> findProductsByUserId(Integer userId);
}
