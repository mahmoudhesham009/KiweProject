package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.entity.Product;
import com.mahmoudh.kiwe.repository.ProductRepository;
import com.mahmoudh.kiwe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepo;
    @Override
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product findProductById(Integer productId) {
        return productRepo.findById(productId).get();
    }

    @Override
    public List<Product> findProductsByUserId(Integer userId) {
        return findProductsByUserId(userId);
    }
}
